package com.nekrosius.drizzardwars.inventories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.nekrosius.drizzardwars.handlers.Game;
import com.nekrosius.drizzardwars.handlers.Maps;
import com.nekrosius.drizzardwars.managers.MapManager;
import com.nekrosius.drizzardwars.utils.ItemStackGenerator;

public class AdminMenu {
	
	public static void setup(Player player){
		Inventory inv = Bukkit.createInventory(player, 9, "Admin menu");
		// 1 SLOT - SET SPAWN
		inv.setItem(0, ItemStackGenerator.createItem(Material.GOLD_AXE, 0, 0
				, ChatColor.RED + "" + ChatColor.BOLD + "Set spawn"
				, Arrays.asList(ChatColor.DARK_GRAY + "Set lobby spawn location")));
		// 2 SLOT - MAP SETUP
		List<String> maps = new ArrayList<String>();
		maps.add(ChatColor.GOLD + "Setup your maps");
		for(Maps map : MapManager.getMaps()){
			maps.add(ChatColor.GRAY + "• " + map.getName());
		}
		inv.setItem(1, ItemStackGenerator.createItem(Material.BLAZE_ROD, 0, 0,
				ChatColor.GOLD + "" + ChatColor.BOLD + "Maps setup", maps));
		// 3 SLOT - START / STOP
		if(!Game.isGameStarted()){
			inv.setItem(2, ItemStackGenerator.createItem(Material.TORCH, 0, 0,
					ChatColor.GOLD + "" + ChatColor.BOLD + "Start game", null));
		}else{
			inv.setItem(2, ItemStackGenerator.createItem(Material.LEVER, 0, 0,
					ChatColor.GOLD + "" + ChatColor.BOLD + "Stop game", null));
		}
		// 4 SLOT - SAVE FILES
		inv.setItem(3, ItemStackGenerator.createItem(Material.POWERED_RAIL, 0, 0,
				ChatColor.GOLD + "" + ChatColor.BOLD + "Save files",
				Arrays.asList(ChatColor.GRAY + "• translations.yml",
						ChatColor.GRAY + "• kits.yml",
						ChatColor.GRAY + "• teams.yml",
						ChatColor.GRAY + "• shops.yml",
						ChatColor.GRAY + "• config.yml")));
		// 5 SLOT - RELOAD FILES
		inv.setItem(4, ItemStackGenerator.createItem(Material.DETECTOR_RAIL, 0, 0,
				ChatColor.GOLD + "" + ChatColor.BOLD + "Reload files",
				Arrays.asList(ChatColor.GRAY + "• translations.yml",
						ChatColor.GRAY + "• kits.yml",
						ChatColor.GRAY + "• teams.yml",
						ChatColor.GRAY + "• shops.yml",
						ChatColor.GRAY + "• config.yml")));
		player.openInventory(inv);
	}
}