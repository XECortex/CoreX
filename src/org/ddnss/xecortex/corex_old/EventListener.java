/*package org.ddnss.xecortex.corex;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.ddnss.xecortex.corex.commands.CommandAFK;

import java.util.Collection;
import java.util.Random;

public class EventListener implements Listener {
    private final Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    public void onPlayerSleep(PlayerBedEnterEvent event) {
        // Run the check ofter one tick because the player sleep status set is delayed
        // TODO: rework this function
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (event.isCancelled() || !event.getPlayer().isSleeping()) return;

            Collection<? extends Player> onlinePlayers = Bukkit.getServer().getOnlinePlayers();
            int sleepingPlayers = getSleepingPlayers(onlinePlayers);
            float playersNeeded;
            int onlineSleepers = 0;
            String sleepMode = plugin.getConfig().getString("sleeping.mode");

            for (Player player : onlinePlayers) {
                // TODO: vanish
                if (player.getGameMode().equals(GameMode.SURVIVAL) || player.getGameMode().equals(GameMode.CREATIVE)) onlineSleepers++;
            }

            if (sleepMode.equals("percentage")) {
                playersNeeded = onlineSleepers * plugin.getConfig().getInt("sleeping.needed") / 100.0f;
            } else if (sleepMode.equals("absolute")) {
                int absolute = plugin.getConfig().getInt("sleeping.needed");

                if (absolute > onlineSleepers) absolute = onlineSleepers;

                playersNeeded = absolute;
            } else {
                plugin.getConfig().set("sleeping.mode", "all");
                plugin.saveConfig();
                plugin.reloadConfig();

                if (!sleepMode.equals("all")) plugin.getLogger().warning(plugin.getMessages().getConfig().getString("sleeping.invalid-sleep-mode").replace("%1", sleepMode));

                playersNeeded = onlinePlayers.size();
            }

            playersNeeded = (float) Math.ceil(playersNeeded);

            if (playersNeeded > onlineSleepers) playersNeeded = onlineSleepers;

            // TODO:
            Bukkit.getServer().broadcastMessage(plugin.getMessages().getConfig().getString("sleeping.needed-message").replace("%1", sleepingPlayers + "").replace("%2", (int) playersNeeded + ""));

            if (sleepingPlayers >= playersNeeded) {
                final float stillNeeded = playersNeeded;

                // Check again after n ticks
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    Collection<? extends Player> onlinePlayers1 = Bukkit.getServer().getOnlinePlayers();

                    if (getSleepingPlayers(onlinePlayers1) >= stillNeeded) {
                        // TODO:
                        Bukkit.getServer().broadcastMessage(plugin.getMessages().getConfig().getString("sleeping.done"));

                        for (Player player : onlinePlayers1) {
                            World world = player.getWorld();

                            if (world.isThundering() || world.hasStorm()) {
                                world.setThundering(false);
                                world.setStorm(false);
                            }

                            world.setTime(0);
                        }
                    }
                }, (long) plugin.getConfig().getInt("sleeping.time"));
            }
        }, 1L);
    }

    // Function to get the amount of sleeping players
    // TODO: make variable global and add/sub
    private int getSleepingPlayers(Collection<? extends Player> onlinePlayers) {
        int sleepingPlayers = 0;

        for (Player player : onlinePlayers) {
            if (player.isSleeping()) sleepingPlayers++;
        }

        return sleepingPlayers;
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (event.getFinalDamage() > 0D && entity instanceof LivingEntity) new CombatHologramTask(plugin, entity, event.getFinalDamage(), false).runTaskTimer(plugin, 0L, 1L);

        if (entity instanceof Player &&
                event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION ||
                event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION ||
                event.getCause() == EntityDamageEvent.DamageCause.FALL ||
                event.getCause() == EntityDamageEvent.DamageCause.FALLING_BLOCK ||
                event.getCause() == EntityDamageEvent.DamageCause.FLY_INTO_WALL
        ) {
            Player player = (Player) entity;
            Random rand = new Random();

            if (rand.nextBoolean() && rand.nextBoolean() && event.getFinalDamage() >= 4) breakBones(player);
        }
    }

    private void breakBones(Player player) {
        if (plugin.scoreboardTask.brokenBones.containsKey(player)) return;

        String[] hurtMessages = { "§c§lYour bones are broken.", "§c§lYou are hurt badly.", "§c§lYou are wounded.", "§c§lSomething feels not quiet right..." };
        String randomMessage = hurtMessages[new Random().nextInt(hurtMessages.length)];

        plugin.getPlayerData().getConfig().set("players." + player.getUniqueId() + ".broken-bones", true);
        plugin.scoreboardTask.brokenBones.put(player, randomMessage + " §r§4You should use a bandage");
        player.setWalkSpeed(.075f);
    }

    private void healBrokenBones(Player player) {
        if (plugin.scoreboardTask.brokenBones.containsKey(player)) {
            plugin.getPlayerData().getConfig().set("players." + player.getUniqueId() + ".broken-bones", false);
            plugin.scoreboardTask.brokenBones.remove(player);
            player.setWalkSpeed(.2f);
            player.sendActionBar("§a§lYou are healed");
        }
    }

    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        Entity entity = event.getEntity();

        if (event.getAmount() > 0D && entity instanceof LivingEntity) new CombatHologramTask(plugin, entity, event.getAmount(), true).runTaskTimer(plugin, 0L, 1L);
    }
}
*/