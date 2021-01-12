package org.ddnss.xecortex.corex_old;

import com.comphenix.packetwrapper.WrapperPlayServerScoreboardScore;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.google.common.base.CaseFormat;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;

public class ScoreboardTask extends BukkitRunnable {
    private Main plugin;
    private final Scoreboard emptyBoard = Bukkit.getScoreboardManager().getNewScoreboard();
    private final Scoreboard mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    private Objective sideboard;
    private Objective buffer;
    public Map<String, String> lines = new HashMap();
    public Map<Player, BossBar> bossBars = new HashMap();
    public Map<Player, String> brokenBones = new HashMap();

    public ScoreboardTask(Main plugin) {
        this.plugin = plugin;

        if (mainScoreboard.getTeam("dummy") != null) mainScoreboard.getTeam("dummy").unregister();
        mainScoreboard.registerNewTeam("dummy");

        // Set lines
        this.lines.put("line-1", "");
        this.lines.put("line-2", "&7» (&6%player_world_time_24%&7)");
        this.lines.put("line-3", "&7»");
        this.lines.put("line-4", "&7» %player_x%, %player_y%, %player_z%");
        this.lines.put("line-5", "");
        this.lines.put("line-6", "&7» &fPing: %player_colored_ping% ms");

        if (this.mainScoreboard.getObjective("sideboardBuffer1") != null) this.mainScoreboard.getObjective("sideboardBuffer1").unregister();
        if (this.mainScoreboard.getObjective("sideboardBuffer2") != null) this.mainScoreboard.getObjective("sideboardBuffer2").unregister();

        this.sideboard = this.mainScoreboard.registerNewObjective("sideboardBuffer1", "dummy", "TITLE");
        this.buffer = this.mainScoreboard.registerNewObjective("sideboardBuffer2", "dummy", "TITLE");

        this.sideboard.setDisplaySlot(DisplaySlot.SIDEBAR);

        for (Team team : this.mainScoreboard.getTeams()) {
            if (team.getName().contains("hbr")) team.unregister();
        }

        ProtocolLibrary.getProtocolManager().addPacketListener(
                new PacketAdapter(plugin, PacketType.Play.Server.SCOREBOARD_SCORE) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        if (PacketType.Play.Server.SCOREBOARD_SCORE.equals(event.getPacketType())) {
                            PacketContainer packet = event.getPacket();
                            WrapperPlayServerScoreboardScore wrapper = new WrapperPlayServerScoreboardScore(packet);

                            // if (packet.getScoreboardActions().read(0) == EnumWrappers.ScoreboardAction.REMOVE)
                            String line = "";
                            line = lines.get(wrapper.getScoreName());
                            line = PlaceholderAPI.setPlaceholders(event.getPlayer(), line);

                            wrapper.setScoreName(line);
                            event.setPacket(wrapper.getHandle());
                        }
                        // else if (PacketType.Play.Server.SCOREBOARD_OBJECTIVE.equals(event.getPacketType())) {
                        //         WrapperPlayServerScoreboardObjective wrapper = new WrapperPlayServerScoreboardObjective(packet);
                        //         // TODO: custom per player sideboard title
                        //         wrapper.setObjectiveName("sideboard");
                        //         event.setPacket(wrapper.getHandle());
                        // }
                    }
                }
        );
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getServer().getOnlinePlayers().toArray(new Player[0])) {
            player.setScoreboard(this.mainScoreboard);

            int health = (int) Math.ceil(player.getHealth());

            createHealthTeam(health, this.plugin.awayFromKeyboardTask.isPlayerAfk(player));
            this.mainScoreboard.getTeam(this.plugin.awayFromKeyboardTask.isPlayerAfk(player) ? "afk-hbr" : "hbr" + health).addEntry(player.getName());

            player.setDisplayName(mainScoreboard.getEntryTeam(player.getName()).getPrefix() + "§r" + plugin.translateColors(plugin.getPlayerData().getConfig().getString("players." + player.getUniqueId() + ".nickname")));
            player.setPlayerListName(mainScoreboard.getEntryTeam(player.getName()).getPrefix() + "§r" + plugin.translateColors(plugin.getPlayerData().getConfig().getString("players." + player.getUniqueId() + ".nickname")) + "§r" + mainScoreboard.getEntryTeam(player.getName()).getSuffix());

            if (player.getWorld().getTime() % 10 == 0) updateBuffer();
            // Sunrise :)
            if (player.getWorld().getTime() == 0) player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5&lAs the sun rises, you feel safer. &r&dA new day begins."));

            // Ouch
            if (brokenBones.containsKey(player)) player.sendActionBar(brokenBones.get(player));
        }

        for (World world : Bukkit.getServer().getWorlds().toArray(new World[0])) {
            for (LivingEntity entity : world.getLivingEntities()) {
                if ((
                        entity instanceof Monster ||
                        entity.getType() == EntityType.PHANTOM ||
                        entity.getType() == EntityType.GHAST ||
                        entity.getType() == EntityType.IRON_GOLEM ||
                        entity.getType() == EntityType.MAGMA_CUBE ||
                        entity.getType() == EntityType.SLIME ||
                        entity.getType() == EntityType.SNOWMAN ||
                        entity.getType() == EntityType.VILLAGER
                    ) && !entity.isDead()) {
                    if (entity.getCustomName() == null) entity.setCustomName(ChatColor.translateAlternateColorCodes('&', getEntityName(entity.getType())));

                    int health = (int) Math.round(entity.getHealth());

                    createHealthTeam(health, false);
                    entity.setCustomNameVisible(true);
                    this.mainScoreboard.getTeam("hbr" + health).addEntry(entity.getUniqueId().toString());
                }
            }
        }
    }

    private void updateBuffer() {
        int i = 1;

        for (String line : this.lines.keySet()) {
            setBufferScore(line, i);
            i++;
        }

        swapBuffer();
    }

    private void setBufferScore(String name, int line) {
        this.buffer.getScore(ChatColor.translateAlternateColorCodes('&', name)).setScore(line);
    }

    private void swapBuffer() {
        this.buffer.setDisplaySlot(this.sideboard.getDisplaySlot());
        Objective swap = this.sideboard;
        this.sideboard = this.buffer;
        this.buffer = swap;
    }

    private void createHealthTeam(int health, boolean afk) {
        if (mainScoreboard.getTeam(afk ? "afk-hbr" : "hbr" + health) == null) {
            String color = (health <= 4) ? "c" : "a";
            Team team = mainScoreboard.registerNewTeam(afk ? "afk-hbr" : "hbr" + health);

            if (afk) {
                team.setPrefix("§7§l(AFK) ");
                team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            }

            team.setSuffix(ChatColor.translateAlternateColorCodes('&', " &" + color + health + "&c❤"));
            team.setCanSeeFriendlyInvisibles(false);
        }
    }

    // Get the human readable name of a entity type
    private String getEntityName(EntityType type) {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, type.name()).replaceAll("(?<=[a-z])([A-Z])(?=[a-z])", " $1");
    }
}