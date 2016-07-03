package com.drizzard.annihilationdw.abilities;

import java.util.ArrayList;
import java.util.List;

import com.drizzard.annihilationdw.handlers.PlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.drizzard.annihilationdw.Main;

public class Ability implements Listener {
	
	public static List<Ability> abilities = new ArrayList<Ability>();
	
	public static Main plugin = (Main)Bukkit.getPluginManager().getPlugin("AnnihilationDW");
	
	private String name;
	private Material icon;
	private int iconData = 0;
	private List<String> description = new ArrayList<String>();
	
	/**
	 * Adds ability to the system
	 * @param ability class, which will be added
	 */
	public void registerAbility(Ability ability) {
		Bukkit.getPluginManager().registerEvents(ability, plugin);
		abilities.add(ability);
	}

	/**
	 * Called whenever the ability is added to a player. This happens when the player's kit is being set up. Override
	 * this to start up any needed timers.
	 * @param player The player to whom the ability is being added
	 */
	public void initialize(Player player){

	}

	/**
	 * Called whenever the ability is removed from a player. This happens when the player changes kits, leaves the
	 * server, or when the game ends. Override this to clear any lists or cancel any running timers.
	 * @param player
	 */
	public void cleanup(Player player){

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

	/**
	 * @return the description
	 */
	public List<String> getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(List<String> description) {
		this.description = description;
	}
	
	/**
	 * Adds line to existing description
	 */
	public void addDescription(String line) {
		this.description.add(line);
	}
	
	public boolean hasAbility(Ability ab, Player player) {
		for(Ability abs : PlayerHandler.getAbilities(player)) {
			if(abs.getName().equals(ab.getName())) return true;
		}
		return false;
	}
	
	/**
	 * @param name of the ability to be searched
	 * @return ability with specified name
	 */
	public static Ability getAbility(String name) {
		for(Ability ab : abilities) {
			if(ab.getName().equalsIgnoreCase(name)) return ab;
		}
		return null;
	}
	
}
