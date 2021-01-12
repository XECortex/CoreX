package org.ddnss.xecortex.corex_old.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.ddnss.xecortex.corex_old.Main;

public class CommandReload implements CommandExecutor {
    private final Main plugin = Main.getPlugin(Main.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.reloadConfig();

            plugin.getMessages().reloadConfig();
            plugin.getPlayerData().reloadConfig();

        sender.sendMessage(plugin.getMessages().getConfig().getString("plugin.reload"));

        return true;
    }
}