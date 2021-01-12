package org.ddnss.xecortex.corex_old.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.ddnss.xecortex.corex_old.Main;

public class ServerEventListener implements Listener {
    private Main plugin;

    public ServerEventListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        // TODO: cleanup
        String messageOfTheDay = ChatColor.translateAlternateColorCodes('&',
                plugin.getConfig().getString("motd.message.line1")
                .replace("\n", "\\n") + "\n" +
                plugin.getConfig().getString("motd.message.line2")
                .replace("\n", "\\n"));

        event.setMotd(messageOfTheDay);
    }
}
