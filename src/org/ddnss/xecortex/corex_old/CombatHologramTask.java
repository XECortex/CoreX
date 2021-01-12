package org.ddnss.xecortex.corex_old;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class CombatHologramTask extends BukkitRunnable {
    private Entity entity;
    private Hologram hologram;
    private int ticks = 0;
    private float xOffset;
    private float zOffset;
    private float yAdder = 0f;
    private float yModifier = .05f;

    public CombatHologramTask(Main plugin, Entity entity, double damage, boolean heal) {
        this.entity = entity;
        this.xOffset = new Random().nextFloat();
        this.zOffset = new Random().nextFloat();

        if (new Random().nextBoolean()) this.xOffset *= -1;
        if (new Random().nextBoolean()) this.zOffset *= -1;

        this.hologram = HologramsAPI.createHologram(plugin, this.getLocation());

        hologram.appendTextLine(Main.translateColors((heal ? "&a+" : "&c-") + Math.round((float) damage * 10.f) / 10.f + "&c❤"));

        // Visibility radius
        this.hologram.getVisibilityManager().setVisibleByDefault(false);

        Player[] players = entity.getWorld().getPlayers().toArray(new Player[0]);
        int radius = 30;

        for (Player player : players) {
            if (player.getLocation().distance(entity.getLocation()) <= radius) this.hologram.getVisibilityManager().showTo(player);
        }
    }

    @Override
    public void run() {
        this.yAdder += yModifier;
        this.yModifier *= .75f;

        Location where = this.getLocation();

        this.hologram.teleport(where);

        if ((this.ticks >= 20 || entity.isDead())) {
            this.hologram.delete();
            this.cancel();
        }

        this.ticks++;
    }

    private Location getLocation() {
        return this.entity.getLocation().add(0f + this.xOffset / 2, this.entity.getHeight() + (this.entity.isCustomNameVisible() ? 1.f : .6f) + this.yAdder, 0f + this.zOffset / 2);
    }
}