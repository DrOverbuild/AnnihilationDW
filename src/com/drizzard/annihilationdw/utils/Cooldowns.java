package com.drizzard.annihilationdw.utils;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import com.drizzard.annihilationdw.abilities.Ability;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Cooldowns {

    private static Table<String, String, Long> cooldowns = HashBasedTable.create();

    public static long getCooldown(Player player, String key) {
        return calculateRemainder(cooldowns.get(player.getName(), key));
    }

    public static long setCooldown(Player player, String key, long delay) {
        return calculateRemainder(
                cooldowns.put(player.getName(), key, System.currentTimeMillis() + delay));
    }

    public static boolean tryCooldown(Player player, String key, long delay) {
        if (getCooldown(player, key) <= 0) {
            setCooldown(player, key, delay);
            return true;
        }
        return false;
    }

    public static boolean tryCooldown(Player player, String key, long delay, String finishMessage) {
        if (tryCooldown(player, key, delay)) {
            new BukkitRunnable() {

                @Override
                public void run() {
                    player.sendMessage(finishMessage);
                }
            }.runTaskLater(Ability.plugin, (int) (delay / 50));
            return true;
        } else {
            return false;
        }
    }

    private static long calculateRemainder(Long expireTime) {
        return expireTime != null ? expireTime - System.currentTimeMillis() : Long.MIN_VALUE;
    }

}