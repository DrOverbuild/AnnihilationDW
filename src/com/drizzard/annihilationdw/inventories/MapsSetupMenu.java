package com.drizzard.annihilationdw.inventories;

import com.drizzard.annihilationdw.handlers.GameMap;
import com.drizzard.annihilationdw.managers.MapManager;
import com.drizzard.annihilationdw.utils.ItemStackGenerator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public class MapsSetupMenu {

    public static void setup(Player player) {
        Inventory inv = Bukkit.createInventory(player, getInventorySize(), "MapSetup menu");
        for (GameMap map : MapManager.getMaps()) {
            inv.setItem(map.getId(), ItemStackGenerator.createItem(Material.PAPER, 0, 0
                    , ChatColor.RED + "" + ChatColor.BOLD + map.getName()
                    , Arrays.asList(ChatColor.GRAY + "Modify " + ChatColor.GOLD + map.getName())));
        }
        player.openInventory(inv);
    }

    public static void setupMapMenu(Player player, GameMap map) {
        Inventory inv = Bukkit.createInventory(player, 18, ChatColor.ITALIC + map.getName() + " menu");
        inv.setItem(0, ItemStackGenerator.createItem(Material.CHAINMAIL_HELMET, 0, 0
                , ChatColor.ITALIC + "Set Team Amount"
                , null));
        inv.setItem(1, ItemStackGenerator.createItem(Material.DIAMOND, 0, 0
                , ChatColor.ITALIC + "Set Diamond Spawns"
                , null));
        inv.setItem(2, ItemStackGenerator.createItem(Material.STICK, 0, 0
                , ChatColor.ITALIC + "Regenerating Block Helper"
                , null));
        inv.setItem(3, ItemStackGenerator.createItem(Material.BLAZE_ROD, 0, 0
                , ChatColor.ITALIC + "Protected Area Helper"
                , null));
        inv.setItem(4, ItemStackGenerator.createItem(Material.WOOD_DOOR, 0, 0
                , ChatColor.RED + "Back to Main Menu"
                , null));
        inv.setItem(5, ItemStackGenerator.createItem(Material.BEACON, 0, 0
                , ChatColor.ITALIC + "Nexus Helper"
                , null));
        inv.setItem(6, ItemStackGenerator.createItem(Material.BED, 0, 0
                , ChatColor.ITALIC + "Set Team Spawns"
                , null));
        inv.setItem(7, ItemStackGenerator.createItem(Material.FEATHER, 0, 0
                , ChatColor.ITALIC + "Set Spectators Spawns"
                , null));
        inv.setItem(8, ItemStackGenerator.createItem(Material.DIAMOND_SPADE, 0, 0
                , ChatColor.ITALIC + "Unplaceable Block Wand"
                , null));
        inv.setItem(9, ItemStackGenerator.createItem(Material.BREWING_STAND_ITEM, 0, 0
                , ChatColor.ITALIC + "Add Brewing Shop"
                , null));
        inv.setItem(10, ItemStackGenerator.createItem(Material.ARROW, 0, 0
                , ChatColor.ITALIC + "Add Weapon Shop"
                , null));
        inv.setItem(11, ItemStackGenerator.createItem(Material.BOW, 0, 0
                , ChatColor.ITALIC + "Add Change Kit Sign"
                , null));
        inv.setItem(12, ItemStackGenerator.createItem(Material.WATCH, 0, 0
                , ChatColor.ITALIC + "Set Phase Time"
                , null));
        player.openInventory(inv);
    }

    private static int getInventorySize() {
        /*int maps = MapManager.amountOfMaps() + 2;
        if (maps < 9) {
			return 9;
		}
		if (maps <= 54 && maps > 9) {
			int a = maps / 9;
			return a * 9;
		} else {
			return 54;
		}*/

        // Don't know what that is above but If maps is equal to 9, the method will return 54 because (maps > 9) does not
        // include 9.

        int rows = (int) Math.ceil(((double) MapManager.amountOfMaps() / 9.0));
        return Math.min(rows * 9, 54);
    }
}