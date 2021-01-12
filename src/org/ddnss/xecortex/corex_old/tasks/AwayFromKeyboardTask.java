package org.ddnss.xecortex.corex_old.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.ddnss.xecortex.corex_old.Main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AwayFromKeyboardTask extends BukkitRunnable {
    private Main plugin;
    private Map<Player, Long> lastPlayerInteractionTimestamps = new HashMap<>();
    private Set<Player> afkPlayers = new HashSet<>();

    public AwayFromKeyboardTask(Main plugin) {
        this.plugin = plugin;

        this.runTaskTimer(plugin, 0L, 20L);
    }

    @Override
    // This function compares the current timestamp with the timestamp of the last interaction of a player
    // If the difference between those is larger than a defined amount of time, the player will be listed as away from keyboard
    public void run() {
        long now = System.nanoTime();

        for (Map.Entry<Player, Long> entry : this.lastPlayerInteractionTimestamps.entrySet()) {
            Player player = entry.getKey();
            long timestamp = entry.getValue();

            // AFK after five minute
            if (now - timestamp > 6e+10 * 5) {
                if (!isPlayerAfk(player)) setPlayerAfk(player);

                // Kick inactive players after ten minutes
                if (now - timestamp > 6e+10 * 10) {
                    player.kickPlayer("You got kicked from the server due inactivity");
                    this.lastPlayerInteractionTimestamps.remove(player);
                }
            }
        }
    }

    // Make a player visible marked as away from keyboard
    public void setPlayerAfk(Player player) {
        setPlayerAfk(player, "");
    }

    public void setPlayerAfk(Player player, String message) {
        if (!isPlayerAfk(player)) {
            this.afkPlayers.add(player);

            // Other player and entities shouldn't be able to interact with the player while he is away from keyboard
            player.setCollidable(false);
            player.setInvulnerable(true);

            // Send a message to all players so they can see that the player is now inactive
            Bukkit.broadcastMessage("§8▎ §7§l(AFK) §r" + player.getDisplayName() + " §7is now AFK" + (!message.isEmpty() ? ": §f" + message : ""));

            // Play a sound to the player
            // TODO: Note utils
            this.plugin.playNoteToPlayer(player, .707107f, 0L);
            this.plugin.playNoteToPlayer(player, .707107f, 2L);
        }
    }

    // If a player interacted with the game, save the current timestamp
    public void playerInteraction(Player player) {
        playerInteraction(player, false);
    }

    public void playerInteraction(Player player, boolean hideMessage) {
        this.lastPlayerInteractionTimestamps.put(player, System.nanoTime());

        // If the player was inactive and is now back
        if (isPlayerAfk(player)) {
            this.afkPlayers.remove(player);

            // Make the player able to interact again
            player.setCollidable(false);
            player.setInvulnerable(true);

            Bukkit.broadcastMessage("§8▎ §r" + player.getDisplayName() + " §7is no longer AFK");

            this.plugin.playNoteToPlayer(player, .707107f, 0L);
            this.plugin.playNoteToPlayer(player, .943874f, 2L);
        }
    }

    // Get if a player is marked as away from keyboard
    public boolean isPlayerAfk(Player player) {
        return this.afkPlayers.contains(player);
    }

    //
    public void cleanup(Player player) {
        this.afkPlayers.remove(player);
        this.lastPlayerInteractionTimestamps.remove(player);
    }
}