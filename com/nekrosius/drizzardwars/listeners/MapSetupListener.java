package com.nekrosius.drizzardwars.listeners;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.nekrosius.drizzardwars.Main;
import com.nekrosius.drizzardwars.files.MapFile;
import com.nekrosius.drizzardwars.files.TeamsFile;
import com.nekrosius.drizzardwars.handlers.Game;
import com.nekrosius.drizzardwars.handlers.Team;
import com.nekrosius.drizzardwars.handlers.mapsetup.Blocks;
import com.nekrosius.drizzardwars.handlers.mapsetup.Phase;
import com.nekrosius.drizzardwars.handlers.mapsetup.Protect;
import com.nekrosius.drizzardwars.handlers.mapsetup.Signs;
import com.nekrosius.drizzardwars.managers.MapManager;
import com.nekrosius.drizzardwars.managers.TeamManager;
import com.nekrosius.drizzardwars.utils.Convert;

public class MapSetupListener implements Listener{

	private Main pl;
	public MapSetupListener(Main plugin) {
		pl = plugin;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();		
		//ENDER FURNACE
		if(player.getItemInHand().getType().equals(Material.AIR)) return;
		if(player.getItemInHand().getType().equals(Material.ENDER_PEARL)) {
			if(player.getItemInHand().getItemMeta().getDisplayName() == null) return;
			if(!(player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.DARK_AQUA + "Set Ender Furnace"))) return;
			event.setCancelled(true);
			if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)){
				Block block = event.getClickedBlock();
				if(block.getType().equals(Material.BURNING_FURNACE)){
					List<String> furnaces = MapFile.config.getStringList("blocks.furnaces");
					int 	x = block.getLocation().getBlockX(),
							y = block.getLocation().getBlockY(),
							z = block.getLocation().getBlockZ();
					String pos = x + "," + y + "," + z;
					if(furnaces.contains(pos)){
						furnaces.remove(pos);
						MapFile.config.set("blocks.furnaces", furnaces);
						MapFile.saveConfig();
					}
					block.setType(Material.AIR);
					player.sendMessage(ChatColor.GRAY + "You've removed Ender Furnace!");
				}
			}else if(event.getAction().equals( Action.RIGHT_CLICK_BLOCK)){
				MapFile.createConfig(player.getWorld().getName());
				Block block = event.getClickedBlock();
				block.setType(Material.BURNING_FURNACE);
				Blocks.setBlockDiretion(player, block);
				List<String> furnaces = MapFile.config.getStringList("blocks.furnaces");
				int 	x = block.getLocation().getBlockX(),
						y = block.getLocation().getBlockY(),
						z = block.getLocation().getBlockZ();
				String pos = x + "," + y + "," + z;
				furnaces.add(pos);
				MapFile.config.set("blocks.furnaces", furnaces);
				MapFile.saveConfig();
				player.sendMessage(ChatColor.GRAY + "You've added Ender Furnace!");
			}
		}
		
		//UNPLACEABLE BLOCK WAND
		else if(player.getItemInHand().getType().equals(Material.DIAMOND_SPADE)) {
			if (event.getClickedBlock() != null) {
				if (player.getItemInHand().getItemMeta().getDisplayName() == null) return;
				if (!(player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.DARK_AQUA + "Unplaceable Block Wand")))
					return;
				event.setCancelled(true);
				Block block = event.getClickedBlock();
				Blocks.setUnplaceableBlock(player, block);
				Blocks.setUnplaceableBlockStatus(player, 1);
				MapFile.createConfig(player.getWorld().getName());
				if (MapFile.config.getStringList("blocks.unplaceable").contains(block.getType().toString() + "," + block.getData())) {
					player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
					player.sendMessage(ChatColor.GRAY + "You have selected " + ChatColor.GOLD + block.getType().toString());
					player.sendMessage(ChatColor.GRAY + "This block is already added!");
					player.sendMessage(ChatColor.GRAY + "Do you want to " + ChatColor.GOLD + "Remove" + ChatColor.GRAY + " it?");
					player.sendMessage(ChatColor.GRAY + "If not, write " + ChatColor.GOLD + "Cancel");
					player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
					Blocks.setUnplaceableBlockStatus(player, -1);
					return;
				}
				player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
				player.sendMessage(ChatColor.GRAY + "You have selected " + ChatColor.GOLD + block.getType().toString());
				player.sendMessage(ChatColor.GRAY + "To stop setup write " + ChatColor.GOLD + "CANCEL");
				player.sendMessage(ChatColor.GRAY + "Do you want add " + ChatColor.GOLD + "this" + ChatColor.GRAY + " data type");
				player.sendMessage(ChatColor.GRAY + "or " + ChatColor.GOLD + "all" + ChatColor.GRAY + " types of this block");
				player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
			}
		}
		//DIAMOND SPAWNS
		else if(player.getItemInHand().getType().equals(Material.DIAMOND)) {
			if(player.getItemInHand().getItemMeta().getDisplayName() == null) return;
			if(!(player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.DARK_AQUA + "Set Diamond Spawns"))) return;
			event.setCancelled(true);
			if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)){
				Block block = event.getClickedBlock();
				if(block.getType().equals(Material.DIAMOND_ORE)){
					MapFile.createConfig(player.getWorld().getName());
					List<String> diamonds = MapFile.config.getStringList("blocks.diamonds");
					int x = block.getLocation().getBlockX(),
							y = block.getLocation().getBlockY(),
							z = block.getLocation().getBlockZ();
					String coords = x + "," + y + "," + z;
					if(diamonds.contains(coords)){
						diamonds.remove(coords);
					}
					MapFile.set("blocks.diamonds", diamonds);
					MapFile.saveConfig();
					block.setType(Material.AIR);
					player.sendMessage(ChatColor.GRAY + "You've removed diamond spawn!");
				}
			}else if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
				Block block = event.getClickedBlock();
				MapFile.createConfig(player.getWorld().getName());
				List<String> diamonds = MapFile.config.getStringList("blocks.diamonds");
				int x = block.getLocation().getBlockX(),
						y = block.getLocation().getBlockY(),
						z = block.getLocation().getBlockZ();
				String coords = x + "," + y + "," + z;
				diamonds.add(coords);
				MapFile.set("blocks.diamonds", diamonds);
				MapFile.saveConfig();
				block.setType(Material.DIAMOND_ORE);
				player.sendMessage(ChatColor.GRAY + "You've added diamond spawn!");
			}
		}
		// REGENERATING BLOCKS
		else if(player.getItemInHand().getType().equals(Material.STICK)){
			if(player.getItemInHand().getItemMeta().getDisplayName() == null) return;
			if(player.getItemInHand().getItemMeta().getDisplayName().contains("Regenerating Block Helper")) {
				Blocks.deleteRegeneratingBlockInfo(player);
				event.setCancelled(true);
				if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)){
					Block block = event.getClickedBlock();
					Blocks.setRegeneratingBlock(player, block);
					Blocks.setRegeneratingBlockStatus(player, 1);
					MapFile.createConfig(player.getWorld().getName());
					if(MapFile.config.getStringList("blocks.unbreakable").contains(block.getType().toString() + "," + block.getData())
							|| Main.contains(MapFile.config.getStringList("blocks.regenerating"), block.getType().toString() + "," + block.getData())){
						player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
						player.sendMessage(ChatColor.GRAY + "You have selected " + ChatColor.GOLD + block.getType().toString());
						player.sendMessage(ChatColor.GRAY + "This block is already added!");
						player.sendMessage(ChatColor.GRAY + "Do you want to " + ChatColor.GOLD + "Remove" + ChatColor.GRAY + " it?");
						player.sendMessage(ChatColor.GRAY + "If not, write " + ChatColor.GOLD + "Cancel");
						player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
						Blocks.setRegeneratingBlockStatus(player, -1);
						return;
					}
					player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
					player.sendMessage(ChatColor.GRAY + "You have selected " + ChatColor.GOLD + block.getType().toString());
					player.sendMessage(ChatColor.GRAY + "To stop setup write " + ChatColor.GOLD + "CANCEL");
					player.sendMessage(ChatColor.GRAY + "Do you want add " + ChatColor.GOLD + "this" + ChatColor.GRAY + " data type");
					player.sendMessage(ChatColor.GRAY + "or " + ChatColor.GOLD + "all" + ChatColor.GRAY + " types of this block");
					player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
				}
			}
		}
		// PROTECTED AREA HELPER
		else if(player.getItemInHand().getType().equals(Material.BLAZE_ROD)) {
			if(player.getItemInHand().getItemMeta().getDisplayName() == null) return;
			if(player.getItemInHand().getItemMeta().getDisplayName().contains("Protected Area Helper")) {
				event.setCancelled(true);
				// POINT ONE
				if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)){
					Protect.setPointOne(player, event.getClickedBlock().getLocation());
					player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
					player.sendMessage(ChatColor.GRAY + "You selected first point of protected area!");
					player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
				}
				// POINT TWO
				else if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
					Protect.setPointTwo(player, event.getClickedBlock().getLocation());
					player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
					player.sendMessage(ChatColor.GRAY + "You selected second point of protected area!");
					player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
				}
				if(Protect.getPointOne(player) != null && Protect.getPointTwo(player) != null) {
					Protect.setProtectionStatus(player, 1);
					player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
					player.sendMessage(ChatColor.GRAY + "You've selected both points!" );
					player.sendMessage(ChatColor.GRAY + "Write in chat " + ChatColor.GOLD +  "name"  + ChatColor.GRAY + " of area!" );
					player.sendMessage(ChatColor.GRAY + "To cancel setup write " + ChatColor.GOLD + "CANCEL");
					player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
				}
			}
		}
		// SPECTATORS
		else if(player.getItemInHand().getType().equals(Material.FEATHER)) {
			if(player.getItemInHand().getType() == Material.AIR) return;	if(player.getItemInHand().getItemMeta().getDisplayName() == null) return;			if(!player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.ITALIC + "Set Spectators Spawn")) return;
			event.setCancelled(true);
			double x = player.getLocation().getX(),
					y = player.getLocation().getY(),
					z = player.getLocation().getZ();
			float yaw = player.getLocation().getYaw(),
					pitch = player.getLocation().getPitch();
			String pos = x + "," + y + "," + z + "," + yaw + "," + pitch;
			MapFile.createConfig(player.getWorld().getName());
			MapFile.config.set("spectators.spawnpoint", pos);
			MapFile.saveConfig();
			player.sendMessage(ChatColor.GRAY + "You've just set Spectators' spawnpoint position.");
		}
		// TEAM
		else if(player.getItemInHand().getType().equals(Material.WOOL)){
			if(player.getItemInHand().getItemMeta().getDisplayName() == null) return;
			// NEXUS
			if(player.getItemInHand().getItemMeta().getDisplayName().contains("NEXUS")){
				if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
					byte data = player.getItemInHand().getData().getData();
					for(Team team : TeamManager.getTeams()) {
						if(data == Convert.ChatColorToInt(team.getColor())){
							event.setCancelled(true);
							int x = event.getClickedBlock().getLocation().getBlockX(),
									y = event.getClickedBlock().getLocation().getBlockY(),
									z = event.getClickedBlock().getLocation().getBlockZ();
							String pos = x + "," + y + "," + z;
							MapFile.createConfig(player.getWorld().getName());
							MapFile.config.set("team." + team.getCodeName() + ".nexus", pos);
							MapFile.saveConfig();
							ChatColor color = team.getColor();
							player.sendMessage(ChatColor.GRAY + "You've just set " + color + team.getName() + ChatColor.GRAY + " Nexus position.");
						}
					}
				}
			}
			// SPECTATOR SPAWNPOINT
			else if(player.getItemInHand().getItemMeta().getDisplayName().contains("spectator")){
				byte data = player.getItemInHand().getData().getData();
				for(Team team : TeamManager.getTeams()) {
					if(data == Convert.ChatColorToInt(team.getColor())) {
						event.setCancelled(true);
						double x = player.getLocation().getX(),
								y = player.getLocation().getY(),
								z = player.getLocation().getZ();
						float yaw = player.getLocation().getYaw(),
								pitch = player.getLocation().getPitch();
						String pos = x + "," + y + "," + z + "," + yaw + "," + pitch;
						MapFile.createConfig(player.getWorld().getName());
						MapFile.config.set("team." + team.getCodeName() + ".spectatorspawnpoint", pos);
						MapFile.saveConfig();
						ChatColor color = team.getColor();
						player.sendMessage(ChatColor.GRAY + "You've just set " + color + team.getName() + ChatColor.GRAY + " spectator spawnpoint position.");
					}
				}
			}
			// PLAYERS
			else if(player.getItemInHand().getItemMeta().getDisplayName().contains("player")){
				byte data = player.getItemInHand().getData().getData();
				for(Team team : TeamManager.getTeams()) {
					if(data == Convert.ChatColorToInt(team.getColor())) {
						event.setCancelled(true);
						double x = player.getLocation().getX(),
								y = player.getLocation().getY(),
								z = player.getLocation().getZ();
						float yaw = player.getLocation().getYaw(),
								pitch = player.getLocation().getPitch();
						String pos = x + "," + y + "," + z + "," + yaw + "," + pitch;
						MapFile.createConfig(player.getWorld().getName());
						MapFile.config.set("team." + team.getCodeName() + ".spawnpoint", pos);
						MapFile.saveConfig();
						ChatColor color = team.getColor();
						player.sendMessage(ChatColor.GRAY + "You've just set " + color + team.getName() + ChatColor.GRAY + " Spawnpoint position.");
					}
				}
			}
		}
		// BREWING SHOP
		else if(player.getItemInHand().getType().equals(Material.BREWING_STAND_ITEM)) {
			if(player.getItemInHand().getItemMeta().getDisplayName() == null) return;
			if(player.getItemInHand().getItemMeta().getDisplayName().contains("Add Brewing Shop")) {
				if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
					event.setCancelled(true);
					Signs.addSign("brewing", event.getClickedBlock().getLocation(), player, event.getClickedBlock(), event.getBlockFace());
				}
			}
		}
		// WEAPON SHOP
		else if(player.getItemInHand().getType().equals(Material.ARROW)) {
			if(player.getItemInHand().getItemMeta().getDisplayName() == null) return;
			if(player.getItemInHand().getItemMeta().getDisplayName().contains("Add Weapon Shop")) {
				if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
					event.setCancelled(true);
					Signs.addSign("weapon", event.getClickedBlock().getLocation(), player, event.getClickedBlock(), event.getBlockFace());
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		// PHASES
		if(Phase.getPhaseTimeStatus(player) == 1){
			String msg = event.getMessage();
			int time = Convert.StringToTime(msg);
			player.sendMessage(ChatColor.GRAY + "You've set phase time to " + ChatColor.GOLD + Convert.ticksToReadableTimeFormat(time)
					+ ChatColor.GRAY + " (" + time + " ticks)");
			Phase.setPhaseTimeStatus(player, 0);
			MapFile.createConfig(player.getWorld().getName());
			MapFile.config.set("phase-time", time);
			MapFile.saveConfig();
			event.setCancelled(true);
		}
		// UNPLACEABLE BLOCKS
		else if(Blocks.getUnplaceableBlockStatus(player) == -1) {
			event.setCancelled(true);
			if(event.getMessage().equalsIgnoreCase("remove")) {
				MapFile.createConfig(player.getWorld().getName());
				if(MapFile.config.getStringList("blocks.unplaceable").contains(Blocks.getUnplaceableBlock(player).getType().toString() + "," + Blocks.getUnplaceableBlock(player).getData())){
					List<String> blocks = MapFile.config.getStringList("blocks.unplaceable");
					blocks.remove(Blocks.getUnplaceableBlock(player).getType().toString() + "," + Blocks.getUnplaceableBlock(player).getData());
					MapFile.config.set("blocks.unplaceable", blocks);
					MapFile.saveConfig();
					player.sendMessage(ChatColor.GRAY + "You've removed " + Blocks.getUnplaceableBlock(player).getType().toString() + " from unplaceable block list");
					Blocks.setUnplaceableBlockStatus(player, 0);
					Blocks.setUnplaceableBlock(player, null);
				}
			} else if(event.getMessage().equalsIgnoreCase("cancel")) {
				Blocks.setUnplaceableBlockStatus(player, 0);
				Blocks.setUnplaceableBlock(player, null);
				player.sendMessage(ChatColor.GRAY + "You've just cancelled the " + ChatColor.GOLD + "Unplaceable Block Setup");
			}
		}else if(Blocks.getUnplaceableBlockStatus(player) > 0) {
			event.setCancelled(true);
			if(event.getMessage().equalsIgnoreCase("cancel")){
				Blocks.setUnplaceableBlockStatus(player, 0);
				Blocks.setUnplaceableBlock(player, null);
				player.sendMessage(ChatColor.GRAY + "You've just cancelled the " + ChatColor.GOLD + "Unplaceable Block Setup");
			}
			if(Blocks.getUnplaceableBlockStatus(player) == 1) {
				String type = null;
				if(event.getMessage().equalsIgnoreCase("this")){
					player.sendMessage(ChatColor.GRAY + "Only block type " + ChatColor.GOLD + Blocks.getUnplaceableBlock(player).getData() + ChatColor.GRAY + " will be unplaceable!");
					type = Blocks.getUnplaceableBlock(player).getData() + "";
				}else if(event.getMessage().equalsIgnoreCase("all")){
					player.sendMessage(ChatColor.GRAY + "All types of this block will be unplaceable!");
					type = "0";
				}else{
					player.sendMessage(ChatColor.GRAY + "Unknown answer! Use one of these:");
					player.sendMessage(ChatColor.GOLD + "this; all; cancel");
					return;
				}
				Blocks.setUnplaceableBlockStatus(player, 0);
				MapFile.createConfig(player.getWorld().getName());
				List<String> blocks = MapFile.config.getStringList("blocks.unplaceable");
				blocks.add(Blocks.getUnplaceableBlock(player).getType().toString() + "," + type);
				MapFile.set("blocks.unplaceable", blocks);
				MapFile.saveConfig();
				player.sendMessage(ChatColor.GRAY + "This block will be unplaceable!");
			}
		}
		// REGENERATING BLOCKS
		else if(Blocks.getRegeneratingBlockStatus(player) == -1) {
			event.setCancelled(true);
			if(event.getMessage().equalsIgnoreCase("remove")) {
				MapFile.createConfig(player.getWorld().getName());
				if(MapFile.config.getStringList("blocks.unbreakable").contains(Blocks.getRegeneratingBlock(player).getType().toString() + "," + Blocks.getRegeneratingBlock(player).getData())){
					List<String> blocks = MapFile.config.getStringList("blocks.unbreakable");
					blocks.remove(Blocks.getRegeneratingBlock(player).getType().toString() + "," + Blocks.getRegeneratingBlock(player).getData());
					MapFile.config.set("blocks.unbreakable", blocks);
					MapFile.saveConfig();
					player.sendMessage(ChatColor.GRAY + "You've removed " + Blocks.getRegeneratingBlock(player).getType().toString() + " from unbreakable block list");
					Blocks.deleteRegeneratingBlockInfo(player);
					Blocks.setRegeneratingBlockStatus(player, 0);
					Blocks.setRegeneratingBlock(player, null);
				}else{
					List<String> blocks = MapFile.config.getStringList("blocks.regenerating");
					int id = Main.containsId(blocks, Blocks.getRegeneratingBlock(player).getType().toString() + "," + Blocks.getRegeneratingBlock(player).getData());
					blocks.remove(id);
					MapFile.config.set("blocks.regenerating", blocks);
					MapFile.saveConfig();
					player.sendMessage(ChatColor.GRAY + "You've removed " + Blocks.getRegeneratingBlock(player).getType().toString() + " from regenerating block list!");
					Blocks.deleteRegeneratingBlockInfo(player);
					Blocks.setRegeneratingBlockStatus(player, 0);
					Blocks.setRegeneratingBlock(player, null);
				}
			} else if(event.getMessage().equalsIgnoreCase("cancel")) {
				Blocks.setRegeneratingBlockStatus(player, 0);
				Blocks.setRegeneratingBlock(player, null);
				player.sendMessage(ChatColor.GRAY + "You've just cancelled the " + ChatColor.GOLD + "Regenerating Block Setup");
			}
		}
		else if(Blocks.getRegeneratingBlockStatus(player) > 0){
			event.setCancelled(true);
			if(event.getMessage().equalsIgnoreCase("cancel")){
				Blocks.setRegeneratingBlockStatus(player, 0);
				Blocks.setRegeneratingBlock(player, null);
				player.sendMessage(ChatColor.GRAY + "You've just cancelled the " + ChatColor.GOLD + "Regenerating Block Setup");
			}
			if(Blocks.getRegeneratingBlockStatus(player) == 1){
				player.sendMessage(event.getMessage());
				Blocks.addRegeneratingBlockInfo(player, Blocks.getRegeneratingBlock(player).getType().toString());
				if(event.getMessage().equalsIgnoreCase("this")){
					player.sendMessage(ChatColor.GRAY + "Only block type " + ChatColor.GOLD + Blocks.getRegeneratingBlock(player).getData() + ChatColor.GRAY + " will regenerate!");
					Blocks.addRegeneratingBlockInfo(player, Blocks.getRegeneratingBlock(player).getData()+"");
				}else if(event.getMessage().equalsIgnoreCase("all")){
					player.sendMessage(ChatColor.GRAY + "All types of this block will regenerate!");
					Blocks.addRegeneratingBlockInfo(player, "0");
				}else{
					player.sendMessage(ChatColor.GRAY + "Unknown answer! Use one of these:");
					player.sendMessage(ChatColor.GOLD + "this; all; cancel");
					return;
				}
				Blocks.setRegeneratingBlockStatus(player, 2);
				player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
				player.sendMessage(ChatColor.GRAY + "Do you want make it " + ChatColor.GOLD + "Unbreakable" + ChatColor.GRAY + " or " + ChatColor.GOLD + "Regenerating");
				player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
			}else if(Blocks.getRegeneratingBlockStatus(player) == 2){
				player.sendMessage(event.getMessage());
				if(event.getMessage().equalsIgnoreCase("unbreakable")){
					Blocks.setRegeneratingBlockStatus(player, 0);
					MapFile.createConfig(player.getWorld().getName());
					List<String> blocks = MapFile.config.getStringList("blocks.unbreakable");
					blocks.add(Blocks.getRegeneratingBlock(player).getType().toString() + "," + Blocks.getRegeneratingBlockInfo(player, 1));
					MapFile.set("blocks.unbreakable", blocks);
					MapFile.saveConfig();
					player.sendMessage(ChatColor.GRAY + "This block will be unbreakable!");
					return;
				}else if(event.getMessage().equalsIgnoreCase("regenerating")){
					player.sendMessage(ChatColor.GRAY + "This block will regenerate!");
					player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
					player.sendMessage(ChatColor.GRAY + "Would you like this block to break naturraly or add them to players' inventory?");
					player.sendMessage(ChatColor.GRAY + "Type " + ChatColor.GOLD + "Natural" + ChatColor.GRAY + " or " + ChatColor.GOLD + "UnNatural");
					player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
				}else{
					player.sendMessage(ChatColor.GRAY + "Unknown answer! Use one of these:");
					player.sendMessage(ChatColor.GOLD + "unbreakable; regenerating; cancel");
					return;
				}
				Blocks.setRegeneratingBlockStatus(player, 3);
			}else if(Blocks.getRegeneratingBlockStatus(player) == 3){
				player.sendMessage(event.getMessage());
				if(event.getMessage().equalsIgnoreCase("natural")){
					player.sendMessage(ChatColor.GRAY + "This block will be dropped naturally!");
					Blocks.addRegeneratingBlockInfo(player, "natural");
				}else if(event.getMessage().equalsIgnoreCase("unnatural")){
					player.sendMessage(ChatColor.GRAY + "This block will be added to player's inventory!");
					Blocks.addRegeneratingBlockInfo(player, "unnatural");
				}else{
					player.sendMessage(ChatColor.GRAY + "Unknown answer! Use one of these:");
					player.sendMessage(ChatColor.GOLD + "natural; unnatural; cancel");
					return;
				}
				player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
				player.sendMessage(ChatColor.GRAY + "For how much time block should be destroyed?");
				player.sendMessage(ChatColor.GRAY + "Type " + ChatColor.GOLD + "hh:mm:ss");
				player.sendMessage(ChatColor.GRAY + "For example: " + ChatColor.GOLD + "00:00:30" + ChatColor.GRAY + " for 30 seconds");
				player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
				Blocks.setRegeneratingBlockStatus(player, 4);
			}else if(Blocks.getRegeneratingBlockStatus(player) == 4) {
				player.sendMessage(event.getMessage());
				int time = Convert.StringToTime(event.getMessage());
				Blocks.addRegeneratingBlockInfo(player, time+"");
				player.sendMessage(ChatColor.GRAY + "You have set regenerating time to " + ChatColor.GOLD + Convert.ticksToReadableTimeFormat(time));
				player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
				player.sendMessage(ChatColor.GRAY + "When this block is broken, would you like it");
				player.sendMessage(ChatColor.GRAY + "To become " + ChatColor.GOLD + "Cobblestone" + ChatColor.GRAY + " or " + ChatColor.GOLD + "Air");
				player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
				Blocks.setRegeneratingBlockStatus(player, 5);
			}else if(Blocks.getRegeneratingBlockStatus(player) == 5) {
				player.sendMessage(event.getMessage());
				if(event.getMessage().equalsIgnoreCase("cobblestone")){
					player.sendMessage(ChatColor.GRAY + "When this block breaks it will become " + ChatColor.GOLD + "Cobblestone");
					Blocks.addRegeneratingBlockInfo(player, "COBBLESTONE");
				}else if(event.getMessage().equalsIgnoreCase("air")){
					player.sendMessage(ChatColor.GRAY + "When this block breaks it will become " + ChatColor.GOLD + "Air");
					Blocks.addRegeneratingBlockInfo(player, "AIR");
				}else{
					player.sendMessage(ChatColor.GRAY + "Unknown answer! Use one of these:");
					player.sendMessage(ChatColor.GOLD + "cobblestone; air; cancel");
					return;
				}
				player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
				player.sendMessage(ChatColor.GRAY + "When this block is broken, which item You want to drop?");
				player.sendMessage(ChatColor.GRAY + "Type: " + ChatColor.GOLD + "[ITEMID] [DATATYPE]");
				player.sendMessage(ChatColor.GRAY + "For example: " + ChatColor.GOLD + "278" + ChatColor.GRAY + " will drop diamond pickaxe!");
				player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
				Blocks.setRegeneratingBlockStatus(player, 6);
			}else if(Blocks.getRegeneratingBlockStatus(player) == 6) {
				player.sendMessage(event.getMessage());
				String[] args = event.getMessage().split(" ");
				int id;
				try{
					id = Integer.parseInt(args[0]);
				}catch(NumberFormatException e){
					player.sendMessage(ChatColor.GRAY + "Please type number!");
					return;
				}
				int data = 0;
				boolean hasData = true;
				if(args.length < 2){
					hasData = false;
				}else{
					data = Integer.parseInt(args[1]);
				}
				if(hasData){
					Blocks.addRegeneratingBlockInfo(player, id + ":" + data);
				}else{
					Blocks.addRegeneratingBlockInfo(player, id + ":0");
				}
				player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
				player.sendMessage(ChatColor.GRAY + "How much items the player will get?");
				player.sendMessage(ChatColor.GRAY + "Type: " + ChatColor.GOLD + "[AMOUNT]");
				player.sendMessage(ChatColor.GRAY + "If you want random value");
				player.sendMessage(ChatColor.GRAY + "Type: " + ChatColor.GOLD + "random(min,max)");
				player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
				Blocks.setRegeneratingBlockStatus(player, 7);
			}else if(Blocks.getRegeneratingBlockStatus(player) == 7) {
				player.sendMessage(event.getMessage());
				String[] args = event.getMessage().split(" ");
				int amount = 0;
				boolean random = false;
				String randomInterval = "";
				try{
					amount = Integer.parseInt(args[0]);
				}catch(NumberFormatException e){
					if(!event.getMessage().startsWith("random") && event.getMessage().length() < 11 && !event.getMessage().endsWith(")")){
						player.sendMessage(ChatColor.GRAY + "Please type number or random(min,max)");
						return;
					}
					String numbers = event.getMessage().substring(7, event.getMessage().length() - 1);
					String[] numbersArray = numbers.split(",");
					String 	min = numbersArray[0],
							max = numbersArray[1];
					randomInterval = min + ":" + max;
					random = true;
				}
				if(random){
					Blocks.addRegeneratingBlockInfo(player, randomInterval);
				}else{
					Blocks.addRegeneratingBlockInfo(player, amount + "");
				}
				player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
				player.sendMessage(ChatColor.GRAY + "How much XP the player will get?");
				player.sendMessage(ChatColor.GRAY + "Type: " + ChatColor.GOLD + "[AMOUNT]");
				player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
				Blocks.setRegeneratingBlockStatus(player, 8);
			}else if(Blocks.getRegeneratingBlockStatus(player) == 8) {
				player.sendMessage(event.getMessage());
				String[] args = event.getMessage().split(" ");
				int amount;
				try{
					amount = Integer.parseInt(args[0]);
				}catch(NumberFormatException e){
					player.sendMessage(ChatColor.GRAY + "Please type number!");
					return;
				}
				Blocks.addRegeneratingBlockInfo(player, amount + "");
				player.sendMessage(ChatColor.GRAY + "You've completed regenerating block setup!");
				MapFile.createConfig(player.getWorld().getName());
				List<String> blocks = MapFile.config.getStringList("blocks.regenerating");
				String info = "";
				for(String s : Blocks.getRegeneratingBlockInfoList(player)){
					info += s + ",";
				}
				info = info.substring(0, info.length() - 1);
				blocks.add(info);
				MapFile.set("blocks.regenerating", blocks);
				MapFile.saveConfig();
				Blocks.deleteRegeneratingBlockInfo(player);
				Blocks.setRegeneratingBlockStatus(player, 0);
			}
		}else if(Protect.getProtectionStatus(player) > 0){
			event.setCancelled(true);
			if(event.getMessage().equalsIgnoreCase("cancel")){
				Protect.setProtectionStatus(player, 0);
				Protect.setPointOne(player, null);
				Protect.setPointTwo(player, null);
				Protect.setName(player, null);
				Protect.setPVP(player, false);
				return;
			}
			if(Protect.getProtectionStatus(player) == 1) {
				player.sendMessage(event.getMessage());
				Protect.setName(player, event.getMessage());
				Protect.setProtectionStatus(player, 2);
				player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
				player.sendMessage(ChatColor.GRAY + "Should PVP be turned off or on in this area?");
				player.sendMessage(ChatColor.GRAY + "Type: " + ChatColor.GOLD + "ON" + ChatColor.GRAY + " or " + ChatColor.GOLD + "OFF");
				player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
			}else if(Protect.getProtectionStatus(player) == 2) {
				player.sendMessage(event.getMessage());
				if(event.getMessage().equalsIgnoreCase("on")) {
					Protect.setPVP(player, true);
				}else if(event.getMessage().equalsIgnoreCase("off")) {
					Protect.setPVP(player, false);
				}else{
					player.sendMessage(ChatColor.GRAY + "Unknown answer! Use one of these:");
					player.sendMessage(ChatColor.GOLD + "on; off; cancel");
					return;
				}
				player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
				player.sendMessage(ChatColor.GRAY + "You finished Protected Area Setup!");
				player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
				Protect.createProtectedArea(player);
			}
		}else if(Blocks.getTeamAmountStatus(player) > 0){
			event.setCancelled(true);
			if(event.getMessage().equalsIgnoreCase("cancel")){
				Blocks.setTeamAmountStatus(player, 0);
				return;
			}
			int amount;
			try{
				amount = Integer.parseInt(event.getMessage());
			}catch(NumberFormatException e){
				player.sendMessage(ChatColor.GRAY + "Type only number!");
				return;
			}
			if(amount > TeamsFile.getTeamsAmount()){
				player.sendMessage(ChatColor.GRAY + "There isn't enough teams! Maximum amount is " + TeamsFile.getTeamsAmount());
				return;
			}
			player.sendMessage(event.getMessage());
			player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
			player.sendMessage(ChatColor.GRAY + "This map will have " + amount + " teams!");
			player.sendMessage(ChatColor.GOLD + "-*-*-*-*-*-*-*-*-*-*-*");
			MapFile.createConfig(player.getWorld().getName());
			MapFile.config.set("team.amount", amount);
			MapFile.saveConfig();
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if(event.getBlock().getType().equals(Material.DIAMOND_ORE)) {
			if(!Game.isGameStarted()){
				Player player = event.getPlayer();
				if(!player.getWorld().getName().equals(MapManager.mapName)){
					event.setCancelled(true);
					player.sendMessage(ChatColor.GRAY + "To remove Diamonds use " + ChatColor.GOLD + "MapSetup (/drizzardwars)");
				}
			}
		}if(event.getBlock().getType().equals(Material.BURNING_FURNACE)) {
			if(!Game.isGameStarted()){
				Player player = event.getPlayer();
				if(!player.getWorld().getName().equals(MapManager.mapName)){
					event.setCancelled(true);
					player.sendMessage(ChatColor.GRAY + "To remove Ender Furnaces use " + ChatColor.GOLD + "MapSetup (/drizzardwars)");
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event){
		if(event.getBlock().getType().equals(Material.DIAMOND_ORE)) {
			Player player = event.getPlayer();
			if(!player.getWorld().getName().equals(MapManager.mapName)) {
				event.setCancelled(true);
				player.sendMessage(ChatColor.GRAY + "To add Diamonds use " + ChatColor.GOLD + "MapSetup (/drizzardwars)");
			}
		}
	}
	
	public Main getMainClass() {
		return pl;
	}
}
