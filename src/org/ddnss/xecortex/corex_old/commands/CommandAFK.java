package org.ddnss.xecortex.corex_old.commands;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ddnss.xecortex.corex_old.Main;

public class CommandAFK implements CommandExecutor {
    private final Main plugin = Main.getPlugin(Main.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player;

            if (args.length == 0 || Bukkit.getPlayer(args[0]) == null) player = (Player) sender;
            else {
                player = Bukkit.getPlayer(args[0]);
                args = (String[]) ArrayUtils.remove(args, 0);
            }

            if (!this.plugin.awayFromKeyboardTask.isPlayerAfk(player)) this.plugin.awayFromKeyboardTask.setPlayerAfk(player, StringUtils.join(args, " "));
            else this.plugin.awayFromKeyboardTask.playerInteraction(player);
        } else sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getConfig().getString("command.error-player-only")));
        return true;
    }
}
