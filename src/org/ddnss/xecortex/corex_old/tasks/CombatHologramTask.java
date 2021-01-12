package org.ddnss.xecortex.corex_old.tasks;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.ddnss.xecortex.corex_old.Main;

public class CombatHologramTask extends BukkitRunnable {
    private Main plugin;
    private Entity entity;
    private Hologram hologram;
    private int ticks = 0;
    private float hologramOffsetX;
    private float hologramOffsetZ;
    private float hologramOffsetY = 0f;
    private float hologramOffsetYModifier = .05f;

    public enum CombatHologramType {
        COMBAT_HOLOGRAM, REGENERATION_HOLOGRAM
    }

    public CombatHologramTask(Main plugin, Entity entity, double value, CombatHologramType type) {
        this.plugin = plugin;
        this.entity = entity;

        // Create a new hologram
        this.hologram = HologramsAPI.createHologram(plugin, calcHologramLocation());
        this.runTaskTimer(plugin, 0L, 1L);
    }

    @Override
    public void run() {

    }

    // Calculate the location where the hologram should be displayed
    private Location calcHologramLocation() {
        Location loc = entity.getLocation();

        loc.add(this.hologramOffsetX, this.entity.getHeight() + (entity.isCustomNameVisible() ? 1f : .6f) + this.hologramOffsetY, this.hologramOffsetZ);
        return loc;
    }
}
