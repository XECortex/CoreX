package org.ddnss.xecortex.corex_old.tasks;

import org.bukkit.scheduler.BukkitRunnable;
import org.ddnss.xecortex.corex_old.Main;

public class AsyncTickTask extends BukkitRunnable {
    private Main plugin;

    public AsyncTickTask(Main plugin) {
        this.plugin = plugin;

        this.runTaskTimer(plugin, 0L, 1L);
    }

    @Override
    public void run() {

    }
}