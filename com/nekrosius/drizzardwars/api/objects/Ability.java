package com.nekrosius.drizzardwars.api.objects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;

import com.nekrosius.drizzardwars.Main;

public class Ability implements Listener {
	
	public static List<Ability> abilities = new ArrayList<Ability>();
	
	Main plugin = (Main)Bukkit.getPluginManager().getPlugin("DrizzardWars");
	
	private String name;
	private Material icon;
	private int iconData = 0;
	private List<String> description;
	
	/**
	 * Adds ability to the system
	 * @param ability class, which will be added
	 */
	public void registerAbility(Ability ability) {
		Bukkit.getPluginManager().registerEvents(ability, plugin);
		abilities.add(ability);
	}
	
	/**
	 * @return the name of ability
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set for ability
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return minecraft item as an icon
	 */
	public Material getIcon() {
		return icon;
	}

	/**
	 * @param icon the minecraft item to set as an icon
	 */
	public void setIcon(Material icon) {
		this.icon = icon;
	}

	/**
	 * @return the iconData
	 */
	public int getIconData() {
		return iconData;
	}

	/**
	 * @param iconData sets the material data (useful for wool and etc.)
	 */
	public void setIconData(int iconData) {
		this.iconData = iconData;
	}
	
}
