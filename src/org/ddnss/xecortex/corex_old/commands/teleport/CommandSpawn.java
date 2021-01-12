package org.ddnss.xecortex.corex_old.commands.teleport;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ddnss.xecortex.corex_old.Main;

public class CommandSpawn implements CommandExecutor {
    private final Main plugin = Main.getPlugin(Main.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            // TODO: Permission check
            player.teleport(player.getWorld().getSpawnLocation());
        } else sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getConfig().getString("command.error-player-only")));

        return true;
    }
}
