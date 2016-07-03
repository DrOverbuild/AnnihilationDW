package com.drizzard.annihilationdw.managers;

import com.drizzard.annihilationdw.files.ConfigFile;
import org.bukkit.entity.Player;
import org.inventivetalent.bossbar.BossBarAPI;

/**
 * Created by jasper on 12/14/15.
 */
public class BarManager {

	public static void setMessage(Player p, String message){
		removeBar(p);
		if(ConfigFile.config.getBoolean("boss-bar") && p.getServer().getPluginManager().getPlugin("BossBarAPI") != null) {
			BossBarAPI.setMessage(p, message);
		}
	}

	public static void setHealth(Player p, float health){
		if(ConfigFile.config.getBoolean("boss-bar") && p.getServer().getPluginManager().getPlugin("BossBarAPI") != null) {
			if (!BossBarAPI.hasBar(p) || BossBarAPI.getMessage(p) != null) {
				return;
			}

			if (health < 0) {
				health = 0;
			}

			if (health > 100) {
				health = 100;
			}

			String message = BossBarAPI.getMessage(p);
			removeBar(p);
			BossBarAPI.setMessage(p, message, health);
		}
	}

	public static void removeBar(Player p){
		if(ConfigFile.config.getBoolean("boss-bar") && p.getServer().getPluginManager().getPlugin("BossBarAPI") != null) {
			BossBarAPI.removeBar(p);
		}
	}

}
