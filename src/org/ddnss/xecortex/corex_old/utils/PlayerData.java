package org.ddnss.xecortex.corex_old.utils;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class PlayerData {
    private Set<Player> brokenBones = new HashSet<>();

    public boolean hasBrokenBones(Player player) {
        return brokenBones.contains(player);
    }

    public void healBrokenBones(Player player) {
        brokenBones.remove(player);
    }

    public void makeBrokenBones(Player player) {
        brokenBones.add(player);
    }
}
