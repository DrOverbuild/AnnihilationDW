package com.nekrosius.annihilationdw.handlers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.nekrosius.annihilationdw.files.KitsFile;
import com.nekrosius.annihilationdw.files.MessageFile;
import com.nekrosius.annihilationdw.files.PlayerFile;
import com.nekrosius.annihilationdw.utils.ItemStackGenerator;

public class Kits {

	

	@SuppressWarnings("deprecation")
	public static void setup(Player player) {
		Inventory inv = Bukkit.createInventory(player, getInventorySize(), MessageHandler.format(MessageFile.getMessage("kits.menu-name")));
		Material material;
		String name;
		int icon;
		int iconData;
		int price;
		List<String> lore;
		int i = 0;
		for(String kit : KitsFile.config.getKeys(false)){
			lore = new ArrayList<String>();
			icon = KitsFile.config.getInt(kit + ".icon");
			iconData = KitsFile.config.getInt(kit + ".icon-data",0);
			material = Material.getMaterial(icon);
			name = MessageHandler.format(KitsFile.config.getString(kit + ".name"));
			price = KitsFile.config.getInt(kit + ".price");
			if(playerHas(player,icon)) {
				lore.add(MessageHandler.format(MessageFile.getMessage("kits.own")));
			}else{
				lore.add(MessageHandler.formatInteger(MessageFile.getMessage("kits.price"), price));
			}
			if(KitsFile.config.getBoolean(kit + ".vip-only")){
				lore.add(MessageHandler.format(MessageFile.getMessage("kits.vip-only")));
			}else{
				lore.add(MessageHandler.format(MessageFile.getMessage("kits.everyone")));
			}
			for(String desc : KitsFile.config.getStringList(kit + ".description")) {
				lore.add(MessageHandler.format(desc));
			}
			inv.setItem(i, ItemStackGenerator.createItem(material, 0, iconData, name, lore));
			i++;
		}
		player.openInventory(inv);
	}
	
	private static int getInventorySize() {
		int kits = getAmountOfKits();
		if (kits < 9) {
			return 9;
		}
		if (kits <= 54 && kits > 9) {
			int a = kits / 9;
			return a * 9;
		} else {
			return 54;
		}
	}
	
	private static int getAmountOfKits() {
		return KitsFile.config.getKeys(false).size();
	}
	
	public static String getKitName(int icon) {
		for(String kits : KitsFile.config.getKeys(false)){
			if(KitsFile.config.getInt(kits + ".icon") == icon) return kits;
		}
		return null;
	}
	
	public static String getKitName(String kit){
		return MessageHandler.format(KitsFile.config.getString(kit + ".name"));
	}
	
	public static boolean onlyVips(int icon) {
		for(String kits : KitsFile.config.getKeys(false)){
			if(KitsFile.config.getInt(kits + ".icon") == icon) return KitsFile.config.getBoolean(kits + ".vip-only");
		}
		return false;
	}
	
	public static int getPrice(int icon) {
		for(String kits : KitsFile.config.getKeys(false)){
			if(KitsFile.config.getInt(kits + ".icon") == icon) return KitsFile.config.getInt(kits + ".price");
		}
		return 0;
	}
	
	public static boolean playerHas(Player player, int icon){
		String kit = null;
		for(String kits : KitsFile.config.getKeys(false)){
			if(KitsFile.config.getInt(kits + ".icon") == icon) kit = kits;
		}
		if(kit == null) return false;
		PlayerFile.createConfig(player);
		if(PlayerFile.config.getStringList("kits").contains(kit)) return true;
		return false;
	}
	
	public static void buyKit(Player player, int icon) {
		Points.setPoints(player, Points.getPoints(player) - getPrice(icon));
		if(player.hasPermission("dw.vip")) {
			PlayerFile.createConfig(player);
			List<String> kits = PlayerFile.config.getStringList("kits");
			kits.add(getKitName(icon));
			PlayerFile.set("kits", kits);
			PlayerFile.saveConfig();
		}
	}
	
	@SuppressWarnings("deprecation")
	public static List<ItemStack> getKitItems(String kit){
		List<ItemStack> items = new ArrayList<ItemStack>();
		int id;
		int data;
		int amount;
		int enchId, level;
		String path;
		ItemStack item;
		for(String index : KitsFile.config.getConfigurationSection(kit + ".items").getKeys(false)) {
			path = kit + ".items." + index;
			id = KitsFile.config.getInt(path + ".id");
			amount = KitsFile.config.getInt(path + ".amount");
			data = KitsFile.config.getInt(path + ".data",0);
			if(amount == 0) amount = 1;
			item = ItemStackGenerator.createItem(Material.getMaterial(id),amount,data,null,null);
			for(String str : KitsFile.config.getStringList(path + ".enchantment")) {
				String[] enchantments = str.split(", ");
				enchId = Integer.parseInt(enchantments[0]);
				level = Integer.parseInt(enchantments[1]);
				item.addUnsafeEnchantment(Enchantment.getById(enchId), level);
			}
			items.add(item);
		}
		return items;
	}
	
	public static void setupKit(Player player) {
		if(PlayerHandler.getPlayerKit(player) == null) return;
		String kit = PlayerHandler.getPlayerKit(player);
		for(ItemStack item : getKitItems(kit)) {
			if(ItemStackGenerator.isHelmet(item)) player.getInventory().setHelmet(item);
			else if(ItemStackGenerator.isChestplate(item)) player.getInventory().setChestplate(item);
			else if(ItemStackGenerator.isLeggings(item)) player.getInventory().setLeggings(item);
			else if(ItemStackGenerator.isBoots(item)) player.getInventory().setBoots(item);
			else player.getInventory().addItem(item);
		}
	}
}