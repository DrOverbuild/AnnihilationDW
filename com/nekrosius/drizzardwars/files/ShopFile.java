package com.nekrosius.drizzardwars.files;

import java.io.File;
import java.io.IOException;

import com.nekrosius.drizzardwars.utils.ConfigLoader;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.nekrosius.drizzardwars.Main;

public class ShopFile {
	
	static File file;
	public static FileConfiguration config;
	
	private Main pl;
	public ShopFile(Main plugin)
	{
		createConfig();
		pl = plugin;
	}
	
	public static void createConfig()
	{
		(new File("plugins" + File.separator + "DrizzardWars" + File.separator
				+ "")).mkdirs();
		file = new File("plugins" + File.separator + "DrizzardWars",
				"shops.yml");
		config = ConfigLoader.loadConfiguration(file);
		if(!file.exists()){
			config.addDefault("weapons.inventory.rows", 2);
			config.addDefault("weapons.inventory.name", "&dWeapons Shop");
			config.addDefault("weapons.items.0.name", "&dChainmail Helmet");
			config.addDefault("weapons.items.0.id", 302);
			config.addDefault("weapons.items.0.price", 10);
			config.addDefault("weapons.items.0.phase", 2);
			config.addDefault("weapons.items.1.name", "&dChainmail Chestplate");
			config.addDefault("weapons.items.1.id", 303);
			config.addDefault("weapons.items.1.price", 10);
			config.addDefault("weapons.items.1.phase", 2);
			config.addDefault("weapons.items.2.name", "&dChainmail Leggings");
			config.addDefault("weapons.items.2.id", 304);
			config.addDefault("weapons.items.2.price", 10);
			config.addDefault("weapons.items.2.phase", 2);
			config.addDefault("weapons.items.3.name", "&dChainmail Boots");
			config.addDefault("weapons.items.3.id", 305);
			config.addDefault("weapons.items.3.price", 10);
			config.addDefault("weapons.items.3.phase", 2);
			config.addDefault("weapons.items.4.name", "&dIron Sword");
			config.addDefault("weapons.items.4.id", 267);
			config.addDefault("weapons.items.4.price", 5);
			config.addDefault("weapons.items.4.phase", 2);
			config.addDefault("weapons.items.5.name", "&dBow");
			config.addDefault("weapons.items.5.id", 261);
			config.addDefault("weapons.items.5.price", 5);
			config.addDefault("weapons.items.5.phase", 2);
			config.addDefault("weapons.items.6.name", "&dArrows");
			config.addDefault("weapons.items.6.id", 262);
			config.addDefault("weapons.items.6.amount", 16);
			config.addDefault("weapons.items.6.price", 5);
			config.addDefault("weapons.items.6.phase", 2);
			config.addDefault("weapons.items.9.name", "&dFishing Rod");
			config.addDefault("weapons.items.9.id", 346);
			config.addDefault("weapons.items.9.price", 5);
			config.addDefault("weapons.items.9.phase", 1);
			config.addDefault("weapons.items.10.name", "&dCake");
			config.addDefault("weapons.items.10.id", 354);
			config.addDefault("weapons.items.10.price", 5);
			config.addDefault("weapons.items.10.phase", 1);
			config.addDefault("weapons.items.11.name", "&dRaw Beef");
			config.addDefault("weapons.items.11.id", 363);
			config.addDefault("weapons.items.11.amount", 3);
			config.addDefault("weapons.items.11.price", 5);
			config.addDefault("weapons.items.11.phase", 1);
			config.addDefault("weapons.items.12.name", "&dBook");
			config.addDefault("weapons.items.12.id", 340);
			config.addDefault("weapons.items.12.price", 5);
			config.addDefault("weapons.items.12.phase", 1);
			
			config.addDefault("brewing.inventory.rows", 3);
			config.addDefault("brewing.inventory.name", "&dBrewing Shop");
			config.addDefault("brewing.items.0.name", "&dBrewing Stand");
			config.addDefault("brewing.items.0.id", 379);
			config.addDefault("brewing.items.0.price", 10);
			config.addDefault("brewing.items.0.phase", 4);
			config.addDefault("brewing.items.1.name", "&dGlass Bottle");
			config.addDefault("brewing.items.1.id", 374);
			config.addDefault("brewing.items.1.amount", 3);
			config.addDefault("brewing.items.1.price", 1);
			config.addDefault("brewing.items.1.phase", 4);
			config.addDefault("brewing.items.2.name", "&dNether Wart");
			config.addDefault("brewing.items.2.id", 372);
			config.addDefault("brewing.items.2.price", 5);
			config.addDefault("brewing.items.2.phase", 4);
			config.addDefault("brewing.items.9.name", "&dRedstone");
			config.addDefault("brewing.items.9.id", 331);
			config.addDefault("brewing.items.9.price", 3);
			config.addDefault("brewing.items.9.phase", 4);
			config.addDefault("brewing.items.10.name", "&dGlowstone");
			config.addDefault("brewing.items.10.id", 348);
			config.addDefault("brewing.items.10.price", 3);
			config.addDefault("brewing.items.10.phase", 4);
			config.addDefault("brewing.items.11.name", "&dFermented Spider Eye");
			config.addDefault("brewing.items.11.id", 376);
			config.addDefault("brewing.items.11.price", 3);
			config.addDefault("brewing.items.11.phase", 4);
			config.addDefault("brewing.items.12.name", "&dGunpowder");
			config.addDefault("brewing.items.12.id", 289);
			config.addDefault("brewing.items.12.price", 3);
			config.addDefault("brewing.items.12.phase", 4);
			config.addDefault("brewing.items.18.name", "&dMagma Cream");
			config.addDefault("brewing.items.18.id", 378);
			config.addDefault("brewing.items.18.price", 2);
			config.addDefault("brewing.items.18.phase", 4);
			config.addDefault("brewing.items.19.name", "&dSugar");
			config.addDefault("brewing.items.19.id", 353);
			config.addDefault("brewing.items.19.price", 2);
			config.addDefault("brewing.items.19.phase", 4);
			config.addDefault("brewing.items.20.name", "&dGlistering Melon");
			config.addDefault("brewing.items.20.id", 382);
			config.addDefault("brewing.items.20.price", 2);
			config.addDefault("brewing.items.20.phase", 4);
			config.addDefault("brewing.items.21.name", "&dGhast Tear");
			config.addDefault("brewing.items.21.id", 370);
			config.addDefault("brewing.items.21.price", 15);
			config.addDefault("brewing.items.21.phase", 4);
			config.addDefault("brewing.items.22.name", "&dGolden Carrot");
			config.addDefault("brewing.items.22.id", 396);
			config.addDefault("brewing.items.22.price", 2);
			config.addDefault("brewing.items.22.phase", 4);
			config.addDefault("brewing.items.23.name", "&dSpider Eye");
			config.addDefault("brewing.items.23.id", 375);
			config.addDefault("brewing.items.23.price", 2);
			config.addDefault("brewing.items.23.phase", 4);
			config.options().copyDefaults(true);
		}
		saveConfig();
	}
	
	public static void saveConfig()
	{
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void set(String key, Object value)
	{
		config.set(key, value);
	}
	
	public Main getMainClass()
	{
		return pl;
	}
}