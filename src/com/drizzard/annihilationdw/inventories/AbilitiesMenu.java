package com.drizzard.annihilationdw.inventories;

import com.drizzard.annihilationdw.abilities.Ability;
import com.drizzard.annihilationdw.utils.ItemStackGenerator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

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
