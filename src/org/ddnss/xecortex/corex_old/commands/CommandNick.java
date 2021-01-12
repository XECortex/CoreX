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

public class CommandNick implements CommandExecutor {
    private final Main plugin = Main.getPlugin(Main.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player;

            if (args.length > 0) {
                if (Bukkit.getPlayer(args[0]) == null) player = (Player) sender;
                else {
                    player = Bukkit.getPlayer(args[0]);
                    args = (String[]) ArrayUtils.remove(args, 0);
                }

                if (args[0].equals("reset")) {
                    setNick(player, player.getName());
                    return true;
                }

                String nickname = StringUtils.join(args, ' ');

                if (ChatColor.stripColor(nickname).length() > plugin.getConfig().getInt("nicknames.maxlength")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getConfig().getString("nickname.maxlength")));
                    return true;
                }

                setNick(player, nickname);
            } else return false;
        } else sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getConfig().getString("command.error-player-only")));

        return true;
    }

    private void setNick(Player player, String nick) {
        plugin.getPlayerData().getConfig().set("players." + player.getUniqueId() + ".nickname", nick);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getConfig().getString("nickname.change").replace("%1", nick)));
    }
}
