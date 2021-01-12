package org.ddnss.xecortex.corex_old.commands.teleport;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ddnss.xecortex.corex_old.Main;

import java.util.Random;

public class CommandTeleportRandom implements CommandExecutor {
    private final Main plugin = Main.getPlugin(Main.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player;

            if (args.length == 0 || Bukkit.getPlayer(args[0]) == null) player = (Player) sender;
            else player = Bukkit.getPlayer(args[0]);

            randomTeleport(player, 8000);
        } else sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getConfig().getString("command.error-player-only")));
        return true;
    }

    public void randomTeleport(Player player, int radius) {
        // Search safe location
        World world = player.getLocation().getWorld();
        Location loc = player.getLocation();
        Random rand = new Random();

        float x = (float) (Math.floor(rand.nextInt(radius)) + .5f);
        float z = (float) (Math.floor(rand.nextInt(radius)) + .5f);

        if (rand.nextBoolean()) x *= -1;
        if (rand.nextBoolean()) z *= -1;

        loc.setX(x);
        loc.setY(255);
        loc.setZ(z);

        while (loc.getWorld().getBlockAt(loc).getType() == Material.AIR) loc.subtract(0D, 1D, 0D);

        player.teleport(loc);
    }
}
