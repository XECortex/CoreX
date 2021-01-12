package org.ddnss.xecortex.corex_old;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.ddnss.xecortex.corex_old.commands.*;
import org.ddnss.xecortex.corex_old.commands.teleport.CommandHome;
import org.ddnss.xecortex.corex_old.commands.teleport.CommandSpawn;
import org.ddnss.xecortex.corex_old.commands.teleport.CommandTeleportRandom;
import org.ddnss.xecortex.corex_old.listeners.BlockEventListener;
import org.ddnss.xecortex.corex_old.listeners.PlayerEventListener;
import org.ddnss.xecortex.corex_old.listeners.ServerEventListener;
import org.ddnss.xecortex.corex_old.tasks.AwayFromKeyboardTask;
import org.ddnss.xecortex.corex_old.tasks.SyncTickTask;
import org.ddnss.xecortex.corex_old.utils.PlayerData;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;

public class Main extends JavaPlugin {
    private PlayerData playerData = new PlayerData();

    public PlayerData getPlayerData(Player player) {
        return this.playerData;
    }


    private Configuration storedPlayerData;
    private Configuration messages;
    public Configuration getPlayerData() {
        return storedPlayerData;
    }
    public Configuration getMessages() {
        return messages;
    }
    public Collection<Player> afkPlayers = new HashSet<Player>();
    public AwayFromKeyboardTask awayFromKeyboardTask;
    public ScoreboardTask scoreboardTask;

    @Override
    public void onEnable() {
        storedPlayerData = new Configuration(this, "playerdata_old.yml");

        // Load the language file
        // TODO: getMessage directly
        String locale = getConfig().getString("locale");

        if (!(locale.equals("en") || locale.equals("de"))) {
            locale = "en";
            getLogger().warning("You provided an invalid language value, setting to default. Available languages are: en, de");
        }

        messages = new Configuration(this, "messages-" + locale + ".yml");

        // Save the default configuration file
        saveDefaultConfig();
        storedPlayerData.saveConfig();
        messages.saveConfig();

        // Check if the plugin is up to date
        String upVersion = "0.0";

        try {
            upVersion = readStringFromURL("https://xecortex.ddnss.org/cdn/content/corex/version");
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        if (!upVersion.trim().equals(getDescription().getVersion())) getLogger().warning(messages.getConfig().getString("plugin.new-version"));
        if (!getConfig().getString("version").equals(getDescription().getVersion())) getLogger().warning(messages.getConfig().getString("plugin.outdated-config").replace("%1", getDescription().getVersion()).replace("%2", getConfig().getString("version")));

        // Check if all dependencies is installed
        // usePlugin = Bukkit.getPluginManager().isPluginEnabled("PLUGIN");
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null || Bukkit.getPluginManager().getPlugin("HolographicDisplays") == null || Bukkit.getPluginManager().getPlugin("ProtocolLib") == null) {
            getLogger().warning(messages.getConfig().getString("plugin.no-dep"));
            Bukkit.getPluginManager().disablePlugin(this);
        } else {
            // Register Event Listeners
            getServer().getPluginManager().registerEvents(new BlockEventListener(this), this);
            getServer().getPluginManager().registerEvents(new PlayerEventListener(this), this);
            getServer().getPluginManager().registerEvents(new ServerEventListener(this), this);

            // Register commands
            getCommand("afk").setExecutor(new CommandAFK());
            getCommand("cxreload").setExecutor(new CommandReload());
            getCommand("home").setExecutor(new CommandHome());
            getCommand("spawn").setExecutor(new CommandSpawn());
            getCommand("item").setExecutor(new CommandItem());
            getCommand("nickname").setExecutor(new CommandNick());
            getCommand("elytra").setExecutor(new CommandElytraKit());
            getCommand("tpr").setExecutor(new CommandTeleportRandom());
        }

        this.scoreboardTask = new ScoreboardTask(this);
        this.awayFromKeyboardTask = new AwayFromKeyboardTask(this);

        this.scoreboardTask.runTaskTimer(this, 0L, 1L);

        addCustomRecipes();



        new SyncTickTask(this);
    }

    private void addCustomRecipes() {
        // Bandage
        ItemStack bandage = new ItemStack(Material.PAPER, 2);
        ItemMeta bandageMeta = bandage.getItemMeta();

        bandageMeta.setDisplayName("§rBandage");
        bandage.setItemMeta(bandageMeta);

        NamespacedKey bandageKey = new NamespacedKey(this, "bandage");
        ShapedRecipe bandageRecipe = new ShapedRecipe(bandageKey, bandage);

        bandageRecipe.shape("P", "B");
        bandageRecipe.setIngredient('P', Material.PAPER);
        bandageRecipe.setIngredient('B', Material.BONE_MEAL);

        Bukkit.addRecipe(bandageRecipe);
    }

    @Override
    public void onDisable() {
        // Disable plugin lol
        NamespacedKey bandageKey = new NamespacedKey(this, "bandage");
        Bukkit.removeRecipe(bandageKey);
        storedPlayerData.saveConfig();
    }

    public static String readStringFromURL(String requestURL) throws IOException {
        try (Scanner scanner = new Scanner(new URL(requestURL).openStream(), StandardCharsets.UTF_8.toString())) {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }
    }

    public static String translateColors(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public boolean isPlayerAFK(Player player) {
        return this.afkPlayers.contains(player);
    }

    private void cleanupPlayerData(Player player) {
        player.setCollidable(true);
        player.setInvulnerable(false);
        this.afkPlayers.remove(player);
        // this.awayFromKeyboardTask.las.remove(player);
        this.scoreboardTask.bossBars.remove(player);
        player.setDisplayName(this.translateColors(this.getPlayerData().getConfig().getString("players." + player.getUniqueId() + ".nickname")));
        Bukkit.getScoreboardManager().getMainScoreboard().getTeam("dummy").addEntry(player.getName());
    }

    public void playNoteToPlayer(Player player, float pitch, long delay) {
        Bukkit.getScheduler().runTaskLater(this, () -> player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 100, pitch), delay);
    }
}