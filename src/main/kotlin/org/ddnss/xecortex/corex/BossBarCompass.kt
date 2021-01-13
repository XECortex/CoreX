package org.ddnss.xecortex.corex

import org.bukkit.Bukkit
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.Player

class BossBarCompass(private val plugin: Main) {
    private val compassTemplate = "A∣∣∣∣∣∣∣∣BB∣∣∣∣∣∣∣∣C∣∣∣∣∣∣∣∣DD∣∣∣∣∣∣∣∣E∣∣∣∣∣∣∣∣FF∣∣∣∣∣∣∣∣G∣∣∣∣∣∣∣∣HH∣∣∣∣∣∣∣∣"
    var bossBars = mutableMapOf<Player, BossBar>()

    fun updateCompass(player: Player) {
        // Get the player's yaw and convert it into 0-360 degree
        val yaw = if (player.location.yaw < 0) player.location.yaw + 360 else player.location.yaw

        // Create a new boss bar for the player if he doesn't have one yet
        var bossBar = bossBars.getOrPut(player) {
            Bukkit.createBossBar("TITLE", BarColor.WHITE, BarStyle.SOLID).apply {
                progress = 0.0
                addPlayer(player)
            }
        }

        var compass = compassTemplate

        // “Rotate“ the compass template string
        val offset = ((compass.length * yaw / 360) % compass.length).toInt()
        compass = compass.substring(offset) + compass.substring(0, offset)

        // Trim the compass string
        compass = compass.substring(compass.length / 4 + 1, compass.length / 4 * 3)

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
            .replace("∣H", "§7ɴ")
        compass = compass.replace("(∣+)".toRegex(), "§8$1")
        compass = "§f∣$compass§f∣"

        bossBar.setTitle(compass)
    }
}