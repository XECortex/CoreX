package org.ddnss.xecortex.corex_old.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.ddnss.xecortex.corex_old.Main;

public class CommandElytraKit implements CommandExecutor {
    private final Main plugin = Main.getPlugin(Main.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            ItemStack elytra = new ItemStack(Material.ELYTRA, 1);
            ItemStack rockets = new ItemStack(Material.FIREWORK_ROCKET, 64);

            elytra.addEnchantment(Enchantment.DURABILITY, 3);
            player.getInventory().addItem(elytra);
            player.getInventory().addItem(rockets);

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getConfig().getString("command.elytra-give")));
        } else sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getConfig().getString("command.error-player-only")));

        return true;
    }
}
