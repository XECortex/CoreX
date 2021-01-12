package org.ddnss.xecortex.corex_old;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class BossBarCompass {
    private Main plugin;
    private Map<Player, BossBar> bossBars = new HashMap<>();
    private String compassTemplate = "A∣∣∣∣∣∣∣∣BB∣∣∣∣∣∣∣∣C∣∣∣∣∣∣∣∣DD∣∣∣∣∣∣∣∣E∣∣∣∣∣∣∣∣FF∣∣∣∣∣∣∣∣G∣∣∣∣∣∣∣∣HH∣∣∣∣∣∣∣∣";

    public BossBarCompass(Main plugin) {
        this.plugin = plugin;
    }

    public BossBar getBossBar(Player player) {
        return this.bossBars.get(player);
    }

    public void removeBossBar(Player player) {
        this.bossBars.remove(player);
    }

    public void updateCompass(Player player) {
        // Get the player's yaw and convert it in a 0-360 degree space
        float yaw = player.getLocation().getYaw() < 0 ? player.getLocation().getYaw() + 360 : player.getLocation().getYaw();

        // Create a new boss bar for the player if he doesn't have one yet
        BossBar bossBar = this.bossBars.computeIfAbsent(player, p -> {
            BossBar bar = Bukkit.createBossBar("COMPASS", BarColor.WHITE, BarStyle.SOLID);

            bar.setProgress(0D);
            bar.addPlayer(p);
            return bar;
        });

        String compass = this.compassTemplate;

        // “Rotate“ the compass template string
        int offset = (int) (compass.length() * yaw / 360) % compass.length();
        compass = compass.substring(offset) + compass.substring(0, offset);

        // Trim the compass string
        compass = compass.substring(compass.length() / 4 + 1, compass.length() / 4 * 3);

        // Insert the correct cardinal signs and colors in the template
        compass = compass
                .replace("A", "§cɴ")
                .replace("BB", "§7ɴᴇ")
                .replace("B∣", "§7ᴇ")
                .replace("∣B", "§7ɴ")
                .replace("C", "§fᴇ")
                .replace("DD", "§7ꜱᴇ")
                .replace("D∣", "§7ᴇ")
                .replace("∣D", "§7ꜱ")
                .replace("E", "§9ꜱ")
                .replace("FF", "§7ꜱᴡ")
                .replace("F∣", "§7ᴡ")
                .replace("∣F", "§7ꜱ")
                .replace("G", "§fᴡ")
                .replace("HH", "§7ɴᴡ")
                .replace("H∣", "§7ᴡ")
                .replace("∣H", "§7ɴ");
        compass = compass.replaceAll("(∣+)", "§8$1");
        compass = "§f∣" + compass + "§f∣";

        bossBar.setTitle(compass);
    }
}
