package org.ddnss.xecortex.corex_old.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.ddnss.xecortex.corex_old.Main;

public class BlockEventListener implements Listener {
    private Main plugin;

    public BlockEventListener(Main plugin) {
        this.plugin = plugin;
    }

    // Break the player's afk timeout if he breaks or places a block
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        plugin.awayFromKeyboardTask.playerInteraction(event.getPlayer());
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        plugin.awayFromKeyboardTask.playerInteraction(event.getPlayer());
    }
}
