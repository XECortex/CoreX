package org.ddnss.xecortex.corex

import org.bukkit.plugin.java.JavaPlugin
import org.ddnss.xecortex.corex.listeners.PlayerListener

class Main : JavaPlugin() {
    override fun onEnable() {
        // Save the configuration file to the disk
        saveDefaultConfig();

        // Register event listeners
        server.pluginManager.registerEvents(PlayerListener(this), this)
    }

    override fun onDisable() {
        
    }
}