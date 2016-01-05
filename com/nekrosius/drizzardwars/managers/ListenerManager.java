package com.nekrosius.drizzardwars.managers;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import com.nekrosius.drizzardwars.Main;
import com.nekrosius.drizzardwars.handlers.Team;
import com.nekrosius.drizzardwars.listeners.GameListener;
import com.nekrosius.drizzardwars.listeners.InventoryListener;
import com.nekrosius.drizzardwars.listeners.MapSetupListener;
import com.nekrosius.drizzardwars.listeners.PlayerListener;
import com.nekrosius.drizzardwars.utils.Convert;

public class ListenerManager {
	
	private Main pl;
	public ListenerManager(Main plugin)
	{
		this.pl = plugin;
		PluginManager pm = pl.getServer().getPluginManager();
		pm.registerEvents(new PlayerListener(pl), pl);
		pm.registerEvents(new InventoryListener(pl), pl);
		pm.registerEvents(new MapSetupListener(pl), pl);
		pm.registerEvents(new GameListener(pl), pl);
	}
	public Main getMainClass() {
		return pl;
	}

	@SuppressWarnings("deprecation")
	public static void removeTeamWool(Player player) {
		for(ItemStack item : player.getInventory().getContents()){
			if(item != null){
				if(item.getType().equals(Material.WOOL)){
					for(Team team : TeamManager.getTeams()) {
						if(item.getData().getData() == (short) Convert.ChatColorToInt(team.getColor())){
							player.getInventory().remove(item);
						}
					}
				}
			}
		}
	}	
}