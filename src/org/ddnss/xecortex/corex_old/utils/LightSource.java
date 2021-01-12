package org.ddnss.xecortex.corex_old.utils;

import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.Player;

public class LightSource {
    public static void setBlockLightLevel(Location loc, int level) {
        // Set block light level
        WorldServer worldServer = ((CraftWorld) loc.getWorld()).getHandle();
        LightEngineThreaded let = worldServer.getChunkProvider().getLightEngine();
        LightEngineLayerEventListener layer = let.a(EnumSkyBlock.BLOCK);
        LightEngineBlock leb = (LightEngineBlock) layer;
        BlockPosition position = new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        int finalLevel = (level < 0) ? 0 : Math.min(level, 15);
        int oldLevel = loc.getBlock().getLightLevel();

        if (layer == null) return;
        if (finalLevel == 0) leb.a(position);
        else if (leb.a(SectionPosition.a(position)) != null) leb.a(position, finalLevel);

        // Recalculate lighting
        if (!let.a()) return;

        leb.a(Integer.MAX_VALUE, true, true);

        // Send the light update to all players in the world
        PacketPlayOutLightUpdate packet = new PacketPlayOutLightUpdate();

        for (Player player : loc.getWorld().getPlayers()) {
            // TODO:
        }
    }
}
