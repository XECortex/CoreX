package org.ddnss.xecortex.corex_old.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.ddnss.xecortex.corex_old.Main;
import org.ddnss.xecortex.corex_old.utils.LightSource;
import org.ddnss.xecortex.corex_old.utils.PlayerData;

public class PlayerEventListener implements Listener {
    private Main plugin;

    public PlayerEventListener(Main plugin) {
        this.plugin = plugin;
    }

    // Player interact event
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();

        this.plugin.awayFromKeyboardTask.playerInteraction(player);

        if (item == null) return;

        // Handle interactions
        switch (action) {
            case RIGHT_CLICK_BLOCK:
                // Increase a block's light level by rightclicking it with glowstone dust
                if (item.getType().equals(Material.GLOWSTONE_DUST)) {
                    Block block = event.getClickedBlock();
                    int level = 15;

                    if (player.isSneaking())
                        level = block.getLightLevel() + 1;

                    if (level > 15) player.sendActionBar("§e§lThis block is already glowing at the highest light level");
                    else {
                        LightSource.setBlockLightLevel(block.getLocation(), level);

                        if (level == 15) player.sendActionBar("§e§lThis block is now glowing at the highest light level");
                        else player.sendActionBar("§e§lBlock light level was set to §6§l" + level);
                    }

                    event.setCancelled(true);
                }
                // Heal broken bones with a bandage
                else if (item.getType().equals(Material.PAPER) && item.getItemMeta().getDisplayName().equals("Bandage")) {
                    PlayerData playerData = this.plugin.getPlayerData(player);

                    if (playerData.hasBrokenBones(player)) {
                        playerData.healBrokenBones(player);

                        // Consume one item if the player is in survival mode
                        if (player.getGameMode().equals(GameMode.SURVIVAL)) event.getItem().setAmount(event.getItem().getAmount() - 1);
                    }

                    event.setCancelled(true);
                }
                break;
            case LEFT_CLICK_BLOCK:
                // Decrease a block's light level by leftclicking it with glowstone dust
                if (event.getItem().getType().equals(Material.GLOWSTONE_DUST)) {
                    Block block = event.getClickedBlock();
                    int level = 0;

                    if (player.isSneaking())
                        level = ((int) block.getLightLevel()) - 1;

                    if (level < 0) player.sendActionBar("§e§lThis block is already completely dark");
                    else {
                        LightSource.setBlockLightLevel(block.getLocation(), 0);
                        LightSource.setBlockLightLevel(block.getLocation(), level);

                        if (level == 0) player.sendActionBar("§e§lThis block is no longer glowing");
                        else player.sendActionBar("§e§lBlock light level was set to §6§l" + level);
                    }

                    event.setCancelled(true);
                }
                break;
            case RIGHT_CLICK_AIR:
                // Heal broken bones with a bandage
                if (item.getType().equals(Material.PAPER) && item.getItemMeta().getDisplayName().equals("Bandage")) {
                    PlayerData playerData = this.plugin.getPlayerData(player);

                    if (playerData.hasBrokenBones(player)) {
                        playerData.healBrokenBones(player);

                        // Consume one item if the player is in survival mode
                        if (player.getGameMode().equals(GameMode.SURVIVAL)) event.getItem().setAmount(event.getItem().getAmount() - 1);
                    }

                    event.setCancelled(true);
                }
                break;
        }
    }

    // Player move event
    // Used to detect if the player is afk
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // Test if the player was actually walking
        if (event.getFrom().distance(event.getTo()) > .05D) this.plugin.awayFromKeyboardTask.playerInteraction(player);
    }

    // Player join and quit event
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Set the player's max. health
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(6D * 2D);

        // Restore player data
        // TODO: See comment

        // TODO: Special spawn locations on first join

        // Prevent the player from flying if the the player was in creative mode
        player.setFlying(false);

        // Set the server resource pack
        // player.setResourcePack("https://lb12.ddnss.org/cdn/content/160468753827133a9c-pack.zip", "ea147294522da036a144162ba0b28115b296f2e0");

        // Set the join message
        event.setJoinMessage("§a▎ §7" + player.getDisplayName() + " §rjoined the server");
        player.setWalkSpeed(.2f);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerQuitEvent.QuitReason quitReason = event.getReason();

        if (quitReason.equals(PlayerQuitEvent.QuitReason.KICKED))
            // When the player got kicked, the leave message from the PlayerKickEvent is used instead of the quit message
            event.setQuitMessage("");
        else
            // Set the quit message
            event.setQuitMessage("§4▎ §7" + player.getDisplayName() + " §rleft the server");

        // TODO: Cleanup player data
        this.plugin.awayFromKeyboardTask.playerInteraction(player, true);
        this.plugin.awayFromKeyboardTask.cleanup(player);
    }

    // Modify the kick message (Mainly because of AFK-kicks)
    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        String reason = event.getReason();

        // Broadcast the kick message to all players because setLeaveMessage does not work for whatever reason
        event.setLeaveMessage("");

        // If the player got kicked because of inactivity
        if (reason.equals("You got kicked from the server due inactivity"))
            Bukkit.broadcastMessage("§4▎ §7" + player.getDisplayName() + " §rwas kicked from the server due inactivity");
        else
            Bukkit.broadcastMessage("§4▎ §7" + player.getDisplayName() + " §rwas kicked from the server.\n§7Reason: " + reason);
    }

    // Player death and respawn event
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        // Heavy impact death sound, hell yeah
        player.playSound(player.getLocation(), "minecraft:block.conduit.deactivate", 100, 1);

        // Send the player his death coordinates
        player.sendMessage("§cYou died at §4" + (int) Math.floor(player.getLocation().getX()) + ", " + (int) Math.floor(player.getLocation().getY()) + ", " + (int) Math.floor(player.getLocation().getZ()));
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        this.plugin.awayFromKeyboardTask.playerInteraction(player);

        // TODO: Special respawn locations
    }

    // Bed enter event
    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        // TODO: Better sleeping
        // private HashMap<Player, World> sleepingPlayers = new HashMap<>();
    }
}
