package org.ddnss.xecortex.corex_old.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.ddnss.xecortex.corex_old.Main;
import org.ddnss.xecortex.corex_old.BossBarCompass;

public class SyncTickTask extends BukkitRunnable {
    private Main plugin;
    private BossBarCompass bossBarCompass;

    public SyncTickTask(Main plugin) {
        this.plugin = plugin;
        this.bossBarCompass = new BossBarCompass(plugin);

        this.runTaskTimer(plugin, 0L, 1L);
    }

    @Override
    public void run() {
        // Sync updates for each player
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            this.bossBarCompass.updateCompass(player);
        }
    }
}
