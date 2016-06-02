package com.nekrosius.drizzardwars.listeners;

import com.nekrosius.drizzardwars.handlers.*;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.nekrosius.drizzardwars.Main;
import com.nekrosius.drizzardwars.files.ConfigFile;
import com.nekrosius.drizzardwars.files.KitsFile;
import com.nekrosius.drizzardwars.files.MapFile;
import com.nekrosius.drizzardwars.files.MessageFile;
import com.nekrosius.drizzardwars.files.ShopFile;
import com.nekrosius.drizzardwars.files.TeamsFile;
import com.nekrosius.drizzardwars.handlers.GameMap;
import com.nekrosius.drizzardwars.handlers.mapsetup.Blocks;
import com.nekrosius.drizzardwars.handlers.mapsetup.Phase;
import com.nekrosius.drizzardwars.inventories.AdminMenu;
import com.nekrosius.drizzardwars.inventories.MapsSetupMenu;
import com.nekrosius.drizzardwars.managers.ListenerManager;
import com.nekrosius.drizzardwars.managers.MapManager;
import com.nekrosius.drizzardwars.managers.TeamManager;
import com.nekrosius.drizzardwars.utils.Convert;
import com.nekrosius.drizzardwars.utils.ItemStackGenerator;

public class InventoryListener implements Listener{
	
	private Main pl;
	public InventoryListener(Main pl)
	{
		this.pl = pl;
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Player player = event.getPlayer();
			if(player.getItemInHand() != null) {
				if (player.getItemInHand().isSimilar(ConfigFile.getKitSelectorItem())) {
					if (player.getItemInHand().getItemMeta().hasDisplayName()) {
						if (player.getItemInHand().getItemMeta().getDisplayName().contains("Kits")) {
							event.setCancelled(true);
							Kits.setup(player);
							return;
						}
					}
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInventoryInteract(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if(event.getInventory().getTitle().equals(MessageHandler.format(MessageFile.getMessage("kits.menu-name")))) {
			processKitsInventoryClick(event,player);
		}else if(event.getInventory().getTitle().equals("Admin menu")) {
			processAdminMenuInventoryClick(event,player);
		}else if(event.getInventory().getName().equals("MapSetup menu")) {
			processChooseMapSetupMenuInventoryClick(event,player);
		}else if(event.getInventory().getName().startsWith(ChatColor.ITALIC + "")){
			processMapSetupMenuInventoryClick(event,player);
		}
	}

	private void processKitsInventoryClick(InventoryClickEvent event, Player player){
		event.setCancelled(true);
		if(event.getSlotType() == SlotType.OUTSIDE) return;
		if(event.getCurrentItem() == null) return;
		if(event.getCurrentItem().getType() == Material.AIR) return;
		int icon = event.getCurrentItem().getType().getId();
		String kitName = Kits.getKitName(icon);
		if(kitName != null){
			if(Kits.onlyVips(icon) && !player.hasPermission("drwars.vip")){
				MessageHandler.sendMessage(player, MessageFile.getMessage("kits.vip-only"));
				return;
			}
			String msg ;
			if(Kits.playerHas(player, icon)){
				PlayerHandler.setPlayerKit(player, kitName);
				msg = MessageFile.getMessage("kits.choose");
				msg = MessageHandler.formatString(msg, Kits.getKitName(kitName));
			}else{
				int price = Kits.getPrice(icon);
				if(Points.getPoints(player) >= price){
					Kits.buyKit(player, icon);
					String kit = Kits.getKitName(Kits.getKitName(icon));
					msg = MessageHandler.formatString(MessageFile.getMessage("kits.bought"), kit);
					PlayerHandler.setPlayerKit(player, kitName);
				}else{
					msg = MessageFile.getMessage("kits.cant-afford");
				}
			}
			MessageHandler.sendMessage(player, msg);
			TabHandler.update(player);
			player.closeInventory();
		}
	}

	private void processAdminMenuInventoryClick(InventoryClickEvent event, Player player){
		event.setCancelled(true);
		if(event.getCurrentItem() == null) return;
		if(event.getCurrentItem().getType() == Material.AIR) return;
		if(event.getSlot() == 0) {
			event.setCancelled(true);
			String world = player.getLocation().getWorld().getName();
			double x = player.getLocation().getX(),
					y = player.getLocation().getY(),
					z = player.getLocation().getZ();
			float yaw = player.getLocation().getYaw(),
					pitch = player.getLocation().getPitch();
			ConfigFile.config.set("spawn-location", world + "," + x + "," + y + "," + z + "," + yaw + "," + pitch);
			ConfigFile.saveConfig();
			player.closeInventory();
			player.sendMessage(ChatColor.GRAY + "You set new lobby spawn location!");
		} else if(event.getSlot() == 1){
			MapsSetupMenu.setup(player);
		} else if(event.getSlot() == 2){
			if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Start game")){
				GameMap map = MapManager.chooseMap();
				Game.start(map);
			}else if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Stop game")){
				Main.println("Finishing game because Operator ended game manually.");
				Game.finish(TeamManager.getMostKills());
			}
		} else if(event.getSlot() == 3){
			ConfigFile.saveConfig();
			KitsFile.saveConfig();
			MessageFile.saveConfig();
			ShopFile.saveConfig();
			TeamsFile.saveConfig();
			player.closeInventory();
			MessageHandler.sendMessage(player, MessageFile.getMessage("menu.save-successful"));
			return;
		} else if(event.getSlot() == 4){
			ConfigFile.createConfig();
			KitsFile.createConfig();
			MessageFile.createConfig();
			ShopFile.createConfig();
			TeamsFile.createConfig();
			MessageHandler.loadMessages();
			player.closeInventory();
			MessageHandler.sendMessage(player, MessageFile.getMessage("menu.reload-successful"));
			return;
		}
	}

	private void processChooseMapSetupMenuInventoryClick(InventoryClickEvent event, Player player){
		if(event.getCurrentItem() == null) return;
		if(event.getCurrentItem().getType() == null) return;
		if(event.getCurrentItem().getType().equals(Material.PAPER)) {
			event.setCancelled(true);
			int id = event.getSlot();
			GameMap map = MapManager.getMap(id);
			String folder = "plugins/DrizzardWars/Maps/" + map.getName();
			Bukkit.createWorld(new WorldCreator(folder));
			MapFile.createConfig(folder);
			player.getInventory().clear();
			if(!player.getWorld().getName().equalsIgnoreCase(folder)){
				if(Bukkit.getWorld(folder) == null) {
					Bukkit.createWorld(new WorldCreator(folder));
				}
				World world = Bukkit.getWorld(folder);
				if(world != null) {
					Location spawn = world.getSpawnLocation();
					if(spawn != null) {
						player.teleport(spawn);
					}else{
						player.sendMessage(ChatColor.RED + "Error loading map " + map.getName() + ": spawn point is null.");
						return;
					}
				}else {
					player.sendMessage(ChatColor.RED + "Error loading map " + map.getName());
					return;
				}
			}
			player.setGameMode(GameMode.CREATIVE);
			MapsSetupMenu.setupMapMenu(player, map);
		}
	}

	private void processMapSetupMenuInventoryClick(InventoryClickEvent event, Player player){
		if(event.getCurrentItem() == null) return;
		if(event.getCurrentItem().getType() == null) return;
		int slot = event.getSlot();
		event.setCancelled(true);
		//TEAM AMOUNT
		if(slot == 0){
			player.closeInventory();
			Blocks.setTeamAmountStatus(player, 1);
			player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
			player.sendMessage(ChatColor.GRAY + "How many teams will be on this map?");
			player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
		}
		//DIAMOND SPAWNS
		else if(slot == 1){
			ItemStack item = ItemStackGenerator.createItem(Material.DIAMOND, 0, 0, ChatColor.DARK_AQUA + "Set Diamond Spawns", null);
			player.getInventory().addItem(item);
			player.closeInventory();
			player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
			player.sendMessage(ChatColor.GRAY + "Right-Click block to");
			player.sendMessage(ChatColor.GRAY + "make it " + ChatColor.WHITE + "Diamond Spawn,");
			player.sendMessage(ChatColor.GRAY + "Left-Click " + ChatColor.WHITE + "Diamond Spawn");
			player.sendMessage(ChatColor.GRAY + "to remove it!");
			player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
		}
		//REGENERATING BLOCKS
		else if(slot == 2){
			ItemStack item = ItemStackGenerator.createItem(Material.STICK, 0, 0, ChatColor.DARK_AQUA + "Regenerating Block Helper", null);
			player.getInventory().addItem(item);
			player.closeInventory();
			player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
			player.sendMessage(ChatColor.GRAY + "Right-Click block to");
			player.sendMessage(ChatColor.GRAY + "make it " + ChatColor.WHITE + "Regenerating,");
			player.sendMessage(ChatColor.GRAY + "Left-Click " + ChatColor.WHITE + "Regenerating block");
			player.sendMessage(ChatColor.GRAY + "to remove it!");
			player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
		}
		//PROTECTED AREA
		else if(slot == 3){
			ItemStack item = ItemStackGenerator.createItem(Material.BLAZE_ROD, 0, 0, ChatColor.DARK_AQUA + "Protected Area Helper", null);
			player.getInventory().addItem(item);
			player.closeInventory();
			player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
			player.sendMessage(ChatColor.GRAY + "Select 2 corners!");
			player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
		}
		//SAVE AND EXIT
		else if(slot == 4){
			World world = player.getWorld();
			if(ConfigFile.config.getString("spawn-location") == null){
				player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
			}else{
				player.teleport(Convert.StringToLocation(ConfigFile.config.getString("spawn-location"), true, false));
			}
			Bukkit.unloadWorld(world, true);
			player.closeInventory();
			Lobby.setupLobby(player);
			AdminMenu.setup(player);
		}
		//NEXUS HELPER
		else if(slot == 5){
			ListenerManager.removeTeamWool(player);
			for(Team team : TeamManager.getTeams()) {
				ChatColor color = team.getColor();
				ItemStack item = ItemStackGenerator.createItem(Material.WOOL, 0, Convert.ChatColorToInt(color), color + "Set " + team.getName() + ChatColor.BOLD + " NEXUS", null);
				player.getInventory().addItem(item);
			}
			player.closeInventory();
			player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
			player.sendMessage(ChatColor.GRAY + "Right-Click with " + ChatColor.WHITE + "WOOL");
			player.sendMessage(ChatColor.GRAY + "to " + ChatColor.WHITE + "SET" + ChatColor.GRAY + " team Nexus!");
			player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
		}
		//TEAM SPAWNS
		else if(slot == 6){
			ListenerManager.removeTeamWool(player);
			for(Team team : TeamManager.getTeams()) {
				ChatColor color = team.getColor();
				ItemStack item = ItemStackGenerator.createItem(Material.WOOL, 0, Convert.ChatColorToInt(color), color + "Add " + team.getName() + " player spawnpoint", null);
				player.getInventory().addItem(item);
			}
			player.closeInventory();
			player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
			player.sendMessage(ChatColor.GRAY + "Click with " + ChatColor.WHITE + "WOOL");
			player.sendMessage(ChatColor.GRAY + "to " + ChatColor.WHITE + "ADD" + ChatColor.GRAY + " team spawnpoint!");
			player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
		}
		//SPECTATORS SPAWNS
		else if(slot == 7){
			ListenerManager.removeTeamWool(player);
			for(Team team : TeamManager.getTeams()) {
				ChatColor color = team.getColor();
				ItemStack item = ItemStackGenerator.createItem(Material.WOOL, 0, Convert.ChatColorToInt(color), color + "Set " + team.getName() + " spectator spawnpoint", null);
				player.getInventory().addItem(item);
			}
			player.closeInventory();
			player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
			player.sendMessage(ChatColor.GRAY + "Right-Click with " + ChatColor.WHITE + "WOOL");
			player.sendMessage(ChatColor.GRAY + "to " + ChatColor.WHITE + "SET" + ChatColor.GRAY + " team spectators' spawnpoint!");
			player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
		}
		//UNPLACEABLE BLOCKS
		if(slot == 8){
			ItemStack item = ItemStackGenerator.createItem(Material.DIAMOND_SPADE, 0, 0, ChatColor.DARK_AQUA + "Unplaceable Block Wand", null);
			player.getInventory().addItem(item);
			player.closeInventory();
			player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
			player.sendMessage(ChatColor.GRAY + "Select block to");
			player.sendMessage(ChatColor.GRAY + "make it " + ChatColor.WHITE + "unplaceable!");
			player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
		}
		//BREWING SHOP
		else if(slot == 12){
			ItemStack item = ItemStackGenerator.createItem(Material.BREWING_STAND_ITEM, 0, 0, ChatColor.DARK_AQUA + "Add Brewing Shop", null);
			player.getInventory().addItem(item);
			player.closeInventory();
			player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
			player.sendMessage(ChatColor.GRAY + "Right-Click to add");
			player.sendMessage(ChatColor.WHITE+ "Brewing Shop");
			player.sendMessage(ChatColor.GRAY + "Left-Click " + ChatColor.WHITE + "Brewing Shop");
			player.sendMessage(ChatColor.GRAY + "to remove it!");
			player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
		}
		//PHASE TIME
		else if(slot == 13){
			Phase.setPhaseTimeStatus(player, 1);
			player.closeInventory();
			player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
			player.sendMessage(ChatColor.GRAY + "To set Phase Time, write to");
			player.sendMessage(ChatColor.GRAY + "chat message in this format:");
			player.sendMessage(ChatColor.WHITE + "hh:mm:ss");
			player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
		}
		//SET WEAPON SHOP
		else if(slot == 14){
			ItemStack item = ItemStackGenerator.createItem(Material.ARROW, 0, 0, ChatColor.DARK_AQUA + "Add Weapon Shop", null);
			player.getInventory().addItem(item);
			player.closeInventory();
			player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
			player.sendMessage(ChatColor.GRAY + "Right-Click to add");
			player.sendMessage(ChatColor.GRAY + " " +ChatColor.WHITE + "Weapon Shop");
			player.sendMessage(ChatColor.GRAY + "Left-Click " + ChatColor.WHITE + "Weapon Shop");
			player.sendMessage(ChatColor.GRAY + "to remove it!");
			player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
		}
	}

	public Main getMainClass() {
		return pl;
	}
}
