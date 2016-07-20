package com.drizzard.annihilationdw.managers;

import com.drizzard.annihilationdw.handlers.Team;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jasper on 12/27/15.
 */
public class ProtectedChestManager {

    private static Map<Block, OfflinePlayer> protectedBlocks = new HashMap<>();

    public static void addProtectedBlock(Block block, OfflinePlayer player) {
        protectedBlocks.put(block, player);
    }

    public static void removeProtectedBlock(Block block) {
        protectedBlocks.remove(block);
    }

    public static void clearProtectedBlocks() {
        protectedBlocks.clear();
    }

    public static boolean blockIsProtected(Block block) {
        return protectedBlocks.containsKey(block);
    }

    public static OfflinePlayer getProtector(Block block) {
        return protectedBlocks.get(block);
    }

    public static boolean playerHasAccessToBlock(Player player, Block block) {
        if (blockIsProtectable(block)) {
            if (blockIsProtected(block)) {
                OfflinePlayer offlinePlayer = getProtector(block);
                Team protectorsTeam = TeamManager.getTeam(offlinePlayer);
                Team team = TeamManager.getTeam(player);

                if (protectorsTeam != null && team != null) {
                    if (protectorsTeam.equals(team) && !offlinePlayer.getName().equals(player.getName())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static boolean blockIsProtectable(Block block) {
        return block.getType().equals(Material.CHEST) ||
                block.getType().equals(Material.TRAPPED_CHEST) ||
                block.getType().equals(Material.FURNACE) ||
                block.getType().equals(Material.BURNING_FURNACE);
    }
}
