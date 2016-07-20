package com.drizzard.annihilationdw.listeners;

import com.drizzard.annihilationdw.Main;
import com.drizzard.annihilationdw.files.StatSignFile;
import com.drizzard.annihilationdw.statsigns.StatSign;
import com.drizzard.annihilationdw.statsigns.StatSignManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

public class SignListener implements Listener {

    private final Main pl;

    public SignListener(Main pl) {
        this.pl = pl;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSignChange(SignChangeEvent evt) {
        if (!evt.isCancelled()) {
            if (evt.getLine(0).equalsIgnoreCase("[DW]")) {
                Player player = evt.getPlayer();
                if (player.hasPermission("dw.statsigns")) {
                    StatSignFile.StatType statType = StatSignFile.StatType.fromName(evt.getLine(1));
                    if (statType != null) {
                        if (isInt(evt.getLine(2))) {
                            int position = Integer.parseInt(evt.getLine(2));
                            if (position > 0) {
                                StatSign statSign = new StatSign(evt.getBlock().getLocation(), position - 1, statType);
                                StatSignManager.getInstance().addStatSign(statSign);
                                Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {

                                    @Override
                                    public void run() {
                                        statSign.update(StatSignFile.getStatData(statType));
                                    }

                                }, 1);
                                player.sendMessage(ChatColor.GREEN + "You placed a statistics sign!");
                            } else {
                                player.sendMessage(ChatColor.RED + "The 3rd line must be a number higher than 0!");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "The 3rd line must be a non-decimal number!");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "'" + evt.getLine(1) + "' isn't a statistic type! (Valid types are: points, kills, "
                                + "games, wins)");
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent evt) {
        if (!evt.isCancelled()) {
            Player player = evt.getPlayer();
            if (evt.getBlock().getType() == Material.SIGN_POST || evt.getBlock().getType() == Material.WALL_SIGN) {
                if (player.hasPermission("dw.statsigns")) {
                    StatSignManager.getInstance().removeStatSign(evt.getBlock().getLocation());
                } else {
                    evt.setCancelled(true);
                }
            } else {
                for (BlockFace face : new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST}) {
                    Block block = evt.getBlock().getRelative(face);
                    if (block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN) {
                        if (player.hasPermission("dw.statsigns")) {
                            StatSignManager.getInstance().removeStatSign(evt.getBlock().getLocation());
                        } else {
                            evt.setCancelled(true);
                            break;
                        }
                    }
                }
            }
        }
    }

    private boolean isInt(String integer) {
        try {
            Integer.parseInt(integer);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

}
