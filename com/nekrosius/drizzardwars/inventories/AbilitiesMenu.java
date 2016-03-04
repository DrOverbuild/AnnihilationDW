package com.nekrosius.drizzardwars.inventories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.nekrosius.drizzardwars.api.objects.Ability;
import com.nekrosius.drizzardwars.utils.ItemStackGenerator;

public class AbilitiesMenu {
	
	public static void setup(Player player) {
		Inventory inv = Bukkit.createInventory(player, getSize(Ability.abilities.size()), ChatColor.BOLD + "Abilities Menu");
		for(Ability ability : Ability.abilities) {
			inv.addItem(ItemStackGenerator.createItem(ability.getIcon(), 0, ability.getIconData(), ChatColor.RED + ability.getName(), ability.getDescription()));
		}
		player.openInventory(inv);
	}
	
	private static int getSize(int items) {
		for(int i = 9; i <= 54; i += 9) {
			if(i >= items) return i;
		}
		return 54;
	}

}
