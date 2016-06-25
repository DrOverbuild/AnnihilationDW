package com.nekrosius.annihilationdw.inventories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.nekrosius.annihilationdw.api.objects.Ability;
import com.nekrosius.annihilationdw.utils.ItemStackGenerator;

public class AbilitiesMenu {
	
	public static void setup(Player player) {
		Inventory inv = Bukkit.createInventory(player, getSize(Ability.abilities.size()), ChatColor.BOLD + "Abilities Menu");
		for(Ability ability : Ability.abilities) {
			inv.addItem(ItemStackGenerator.createItem(ability.getIcon(), 0, ability.getIconData(), ChatColor.RED + ability.getName(), ability.getDescription()));
		}
		player.openInventory(inv);
	}
	
	private static int getSize(int items) {
		int rows = (int)Math.ceil(((double)items / 9.0));
		return Math.min(rows * 9, 54);
	}

}
