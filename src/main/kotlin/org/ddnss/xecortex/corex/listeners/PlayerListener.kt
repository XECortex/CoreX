package org.ddnss.xecortex.corex.listeners

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.ddnss.xecortex.corex.Main
import org.ddnss.xecortex.corex.util.InvalidConfigException

class PlayerListener(val plugin: Main) : Listener {
    @EventHandler
    fun onPlayerChat(event: AsyncPlayerChatEvent) {
        if (event.isCancelled)
            return

        val player = event.player
        var message = event.message

        // Highlight links in the chat in a blue color
        if (player.hasPermission("corex.chat.highlight-urls"))
            message = message.replace("((?:http[s]?://.)?(?:www\\.)?[-a-zA-Z0-9@:%._+~#=]{2,256}\\.[a-z]{2,6}\\b(?:[-a-zA-Z0-9@:%_+.~#?&/=]*))".toRegex(), "§b$1§r")

        // Allow players to use colors in the chat
        if (player.hasPermission("corex.chat.allow-colors"))
            message = ChatColor.translateAlternateColorCodes('&', message)

        event.message = message

        if (plugin.config.getBoolean("chat.format-chat"))
                event.format = plugin.config.getString("chat.message-format") ?: throw InvalidConfigException("chat.message-format")
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player

        event.joinMessage = String.format(plugin.config.getString("messages.join-message") ?: throw InvalidConfigException("messages.join-message"), player.displayName)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player

        if (!(event.reason.equals(QuitReason.KICK) || event.reason.equals(QuitReason.BAN)))
        event.quitMessage = String.format(plugin.config.getString("messages.quit-message") ?: throw InvalidConfigException("messages.quit-message"), player.displayName)
    }

    @EventHandler
    fun onPlayerKick(event: PlayerKickEvent) {
        event.leaveMessage = ""
        Bukkit.broadcastMessage("TODO:")
    }
}