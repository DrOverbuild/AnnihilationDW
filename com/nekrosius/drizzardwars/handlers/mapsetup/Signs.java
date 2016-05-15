package com.nekrosius.drizzardwars.handlers.mapsetup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.nekrosius.drizzardwars.Main;
import com.nekrosius.drizzardwars.files.MessageFile;
import com.nekrosius.drizzardwars.files.ShopFile;
import com.nekrosius.drizzardwars.handlers.Game;
import com.nekrosius.drizzardwars.handlers.MessageHandler;
import com.nekrosius.drizzardwars.utils.ItemStackGenerator;

public class Signs {
	
	private static Inventory wepShop = null;
	private static Inventory brewShop = null;
//	private static Map<Integer, List<ItemStack>> phaseWep = new HashMap<Integer, List<ItemStack>>();
//	private static Map<Integer, List<Integer>> phaseWepSlots = new HashMap<Integer, List<Integer>>();
//	private static Map<Integer, List<ItemStack>> phaseBrew = new HashMap<Integer, List<ItemStack>>();
//	private static Map<Integer, List<Integer>> phaseBrewSlots = new HashMap<Integer, List<Integer>>();
	private static Map<Integer, Integer> wepPrice = new HashMap<>();
	private static Map<Integer, Integer> brewPrice = new HashMap<>();

	public static void addSign(String type, Location loc, Player player, Block block, BlockFace clickedBlockFace) {
		Material signType = getSignType(clickedBlockFace);
		if(signType == null) return;
		if(signType.equals(Material.WALL_SIGN)){
//			loc.getWorld().getBlockAt(loc).getRelative(Main.getPlayerDirection(player).getOppositeFace()).setType(signType);
			loc.getWorld().getBlockAt(loc).getRelative(clickedBlockFace).setType(signType);
//			loc = loc.getWorld().getBlockAt(loc).getRelative(Main.getPlayerDirection(player).getOppositeFace()).getLocation();
			loc = loc.getWorld().getBlockAt(loc).getRelative(clickedBlockFace).getLocation();
			setSignFaceDirection(type, player, (Sign) loc.getWorld().getBlockAt(loc).getState(), block, clickedBlockFace);
		}else if(signType.equals(Material.SIGN_POST)){
			loc.getWorld().getBlockAt(loc).getRelative(BlockFace.UP).setType(signType);
			loc = loc.getWorld().getBlockAt(loc).getRelative(BlockFace.UP).getLocation();
			setSignFaceDirection(type, player, (Sign) loc.getWorld().getBlockAt(loc).getState(), block, clickedBlockFace);
		}
	}

	private static void setSignFaceDirection(String type, Player player, Sign sign, Block block, BlockFace blockFace){
		org.bukkit.material.Sign signData = null;
		if(sign.getType().equals(Material.WALL_SIGN)){
			updateWallSign(sign, block, blockFace);
		}else if(sign.getType().equals(Material.SIGN)){
			signData = new org.bukkit.material.Sign(Material.SIGN);
			signData.setFacingDirection(blockFace.getOppositeFace());
			sign.setData(signData);
		}else if(sign.getType().equals(Material.SIGN_POST)){
			signData = new org.bukkit.material.Sign(Material.SIGN_POST);
			signData.setFacingDirection(Main.getPlayerDirection(player).getOppositeFace());
			sign.setData(signData);
		}
		sign.setLine(0, ChatColor.DARK_RED + "[" + ChatColor.DARK_PURPLE + "Shop" + ChatColor.DARK_RED + "]");
		if(type.equalsIgnoreCase("brewing")){
			sign.setLine(1, "Brewing");
		}else if(type.equalsIgnoreCase("weapon")){
			sign.setLine(1, "Weapon");
		}
		sign.update();
	}

	public static void setSignFaceDirection(String type, Player player, Sign sign, Block block) {
		org.bukkit.material.Sign signData = null;
		if(sign.getType().equals(Material.WALL_SIGN)){
			updateWallSign(sign, block);
		}else if(sign.getType().equals(Material.SIGN)){
			signData = new org.bukkit.material.Sign(Material.SIGN);
			signData.setFacingDirection(Main.getPlayerDirection(player).getOppositeFace());
			sign.setData(signData);
		}else if(sign.getType().equals(Material.SIGN_POST)){
			signData = new org.bukkit.material.Sign(Material.SIGN_POST);
			signData.setFacingDirection(Main.getPlayerDirection(player).getOppositeFace());
			sign.setData(signData);
		}
		sign.setLine(0, ChatColor.DARK_RED + "[" + ChatColor.DARK_PURPLE + "Shop" + ChatColor.DARK_RED + "]");
		if(type.equalsIgnoreCase("brewing")){
			sign.setLine(1, "Brewing");
		}else if(type.equalsIgnoreCase("weapon")){
			sign.setLine(1, "Weapon");
		}
		sign.update();
	}

	private static Material getSignType(BlockFace face){
		if(face.equals(BlockFace.UP)){
			return Material.SIGN_POST;
		}

		if(face.equals(BlockFace.DOWN)){
			return null;
		}

		return Material.WALL_SIGN;
	}
	
	private static Material getSignType(Player player, Location loc){
		Block block = loc.getWorld().getBlockAt(loc);
		if(block.getRelative(Main.getPlayerDirection(player).getOppositeFace()).getType().equals(Material.AIR)){
			return Material.WALL_SIGN;
		}else{
			return Material.SIGN_POST;
		}
	}

	private static void updateWallSign(Sign sign, Block block, BlockFace blockFace) {
		((org.bukkit.material.Sign)sign.getData()).setFacingDirection(blockFace);
		sign.update();
	}

	private static void updateWallSign(Sign sign, Block block) {
		BlockFace[] blockFaces = {BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH};
		for (BlockFace bf : blockFaces) {
			Block bu = sign.getBlock().getRelative(bf);
			if ((bu.getType() == block.getType())) {
				((org.bukkit.material.Sign)sign.getData()).setFacingDirection(bf.getOppositeFace());
				sign.update();
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void setupShops(){
		// Lazy Instantiation Rule: only set up the shops when a player needs to access it.
		wepShop = null;
		brewShop = null;

		wepPrice.clear();
		brewPrice.clear();

//		// WEAPONS
//		wepShop = Bukkit.createInventory(null, ShopFile.config.getInt("weapons.inventory.rows")*9, MessageHandler.format(ShopFile.config.getString("weapons.inventory.name")));
//		String path;
//		for(String id : ShopFile.config.getConfigurationSection("weapons.items").getKeys(false)){
//			path = "weapons.items." + id;
//			int amount = ShopFile.config.getInt(path + ".amount");
//			if(amount == 0) ++amount;
//			int phase = ShopFile.config.getInt(path + ".phase");
//			if(phaseWep.get(phase) == null){
//				phaseWep.put(phase, new ArrayList<ItemStack>());
//				phaseWepSlots.put(phase, new ArrayList<Integer>());
//			}
//			List<ItemStack> items = phaseWep.get(phase);
//			ItemStack item = ItemStackGenerator.createItem(Material.getMaterial(ShopFile.config.getInt(path + ".id"))
//					, amount, 0, MessageHandler.format(ShopFile.config.getString(path + ".name"))
//					,Arrays.asList(MessageHandler.formatInteger(MessageFile.getMessage("shop.price"), ShopFile.config.getInt(path + ".price"))));
//			items.add(item);
//			List<Integer> slots = phaseWepSlots.get(phase);
//			slots.add(Integer.parseInt(id));
//			phaseWepSlots.put(phase, slots);
//			phaseWep.put(phase, items);
//		}
//
//		// BREWING
//
//		brewShop = Bukkit.createInventory(null, ShopFile.config.getInt("brewing.inventory.rows")*9, MessageHandler.format(ShopFile.config.getString("brewing.inventory.name")));
//		for(String id : ShopFile.config.getConfigurationSection("brewing.items").getKeys(false)){
//			path = "brewing.items." + id;
//			int amount = ShopFile.config.getInt(path + ".amount");
//			if(amount == 0) ++amount;
//			int phase = ShopFile.config.getInt(path + ".phase");
//			if(phaseBrew.get(phase) == null){
//				phaseBrew.put(phase, new ArrayList<ItemStack>());
//				phaseBrewSlots.put(phase, new ArrayList<Integer>());
//			}
//			List<ItemStack> items = phaseBrew.get(phase);
//			ItemStack item = ItemStackGenerator.createItem(Material.getMaterial(ShopFile.config.getInt(path + ".id"))
//					, amount, 0, MessageHandler.format(ShopFile.config.getString(path + ".name"))
//					,Arrays.asList(MessageHandler.formatInteger(MessageFile.getMessage("shop.price"), ShopFile.config.getInt(path + ".price"))));
//			items.add(item);
//			List<Integer> slots = phaseBrewSlots.get(phase);
//			slots.add(Integer.parseInt(id));
//			phaseBrewSlots.put(phase, slots);
//			phaseBrew.put(phase, items);
//		}
	}
	
	private static void setupWeaponsShop(){
//		int phase = Game.getPhase() + 1;
//		if(phaseWep.get(phase) == null) return;
//		int i = 0;
//		for(ItemStack item : phaseWep.get(phase)){
//			wepShop.setItem(phaseWepSlots.get(phase).get(i), item);
//			i++;
//		}

		wepShop = Bukkit.createInventory(null, ShopFile.config.getInt("weapons.inventory.rows")*9, MessageHandler.format(ShopFile.config.getString("weapons.inventory.name")));
		String path;
		int slotNumber = 0;
		for(String id:ShopFile.config.getConfigurationSection("weapons.items").getKeys(false)){
			path = "weapons.items." + id;
			int phase = ShopFile.config.getInt(path + ".phase",1);
			if(Game.getPhase() >= phase) {
				int type = ShopFile.config.getInt(path + ".id");
				int amount = ShopFile.config.getInt(path + ".amount",1);
				int data = ShopFile.config.getInt(path + ".data",0);
				int price = ShopFile.config.getInt(path + ".price");
				String name = MessageHandler.format(ShopFile.config.getString(path + ".name"));
				List<String> lore = Arrays.asList(MessageHandler.formatInteger(MessageFile.getMessage("shop.price"), price));
				ItemStack item = ItemStackGenerator.createItem(Material.getMaterial(type),amount,data,name,lore);
				wepShop.setItem(slotNumber,item);
				wepPrice.put(slotNumber,price);
				slotNumber++;
			}
		}

	}
	
	private static void setupBrewingShop(){
//		int phase = Game.getPhase() + 1;
//		if(phaseBrew.get(phase) == null) return;
//		int i = 0;
//		for(ItemStack item : phaseBrew.get(phase)){
//			brewShop.setItem(phaseBrewSlots.get(phase).get(i), item);
//			i++;
//		}

		brewShop = Bukkit.createInventory(null, ShopFile.config.getInt("brewing.inventory.rows")*9, MessageHandler.format(ShopFile.config.getString("brewing.inventory.name")));
		String path;
		int slotNumber = 0;
		for(String id:ShopFile.config.getConfigurationSection("brewing.items").getKeys(false)){
			path = "brewing.items." + id;
			int phase = ShopFile.config.getInt(path + ".phase",1);
			if(Game.getPhase() >= phase) {
				int type = ShopFile.config.getInt(path + ".id");
				int amount = ShopFile.config.getInt(path + ".amount",1);
				int data = ShopFile.config.getInt(path + ".data",0);
				int price = ShopFile.config.getInt(path + ".price");
				String name = MessageHandler.format(ShopFile.config.getString(path + ".name"));
				List<String> lore = Arrays.asList(MessageHandler.formatInteger(MessageFile.getMessage("shop.price"), price));
				ItemStack item = ItemStackGenerator.createItem(Material.getMaterial(type),amount,data,name,lore);
				brewShop.setItem(slotNumber,item);
				brewPrice.put(slotNumber, price);
				slotNumber++;
			}
		}
	}
	
	public static void openWeaponShop(Player player) {
		// Lazy instantiation
		if (wepShop == null){
			setupWeaponsShop();
		}
		player.openInventory(wepShop);
	}
	
	public static void openBrewingShop(Player player) {
		// Lazy instantiation
		if(brewShop == null){
			setupBrewingShop();
		}
		player.openInventory(brewShop);
	}

	public static int getPriceOfWeapon(int slot){
		return wepPrice.get(slot);
	}

	public static int getPriceOfBrewingItem(int slot){
		return brewPrice.get(slot);
	}
}