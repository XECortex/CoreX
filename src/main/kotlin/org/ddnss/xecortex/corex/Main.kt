package org.ddnss.xecortex.corex

import org.bukkit.plugin.java.JavaPlugin
import org.ddnss.xecortex.corex.listeners.PlayerEventListener

class Main : JavaPlugin() {
    override fun onEnable() {
        saveDefaultConfig();

        // Register event listeners
        server.pluginManager.registerEvents(PlayerEventListener(this), this)
    }

    override fun onDisable() {
    }
}