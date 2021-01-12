package org.ddnss.xecortex.corex.listeners

import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.ddnss.xecortex.corex.Main
import org.ddnss.xecortex.corex.util.InvalidConfigException

class PlayerListener(val plugin: Main) : Listener {
    // Player chat event
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

        // Set the format of the message
        if (plugin.config.getBoolean("chat.format-chat"))
                event.format = plugin.config.getString("chat.message-format") ?: throw InvalidConfigException("chat.message-format")
    }

    // Player join, quit, kick and ban event
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player

        // Modify the join message
        event.joinMessage = plugin.config.getString("messages.join-message") ?: throw InvalidConfigException("messages.join-message")
    }
}