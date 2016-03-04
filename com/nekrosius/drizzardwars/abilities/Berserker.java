package com.nekrosius.drizzardwars.abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.nekrosius.drizzardwars.api.events.JoinGameEvent;
import com.nekrosius.drizzardwars.api.objects.Ability;
import com.nekrosius.drizzardwars.handlers.Game;
import com.nekrosius.drizzardwars.handlers.GameState;
import com.nekrosius.drizzardwars.handlers.PlayerHandler;

public class Berserker extends Ability {
	
	/**
	 * Player starts with 9 hearts but for every kill
	 * gets 1 heart until a maximum of 13 hearts
	 */
	
	private double STARTING_HEALTH = 18D;
	private double MAX_HEALTH = 26D;
	private double INCREASE = 2D;
	
	public Berserker() {
		registerAbility(this);
		setName("Berserker");
		setIcon(Material.DIAMOND_AXE);
		addDescription("Player starts with " + STARTING_HEALTH / 2 + " hearts but ");
		addDescription("for every kill gets " + INCREASE / 2 + " heart");
		addDescription("until a maximum of " + MAX_HEALTH / 2 + " hearts");
	}
	
	@EventHandler
	public void onJoin(JoinGameEvent event) {
		event.getPlayer().setHealth(STARTING_HEALTH);
		event.getPlayer().setMaxHealth(MAX_HEALTH);
	}
	
	@EventHandler
	public void onKill(PlayerDeathEvent event) {
		if(event.getEntity().getKiller() == null) return;
		if(Game.getGameState() != GameState.IN_GAME) return;
		Player killer = event.getEntity().getKiller();
		if(! PlayerHandler.hasAbility(killer)) return;
		if(! PlayerHandler.getAbility(killer).getName().equals( this.getName() )) return;
		
		// Adding 1 heart
		
		if(killer.getHealth() + INCREASE >= killer.getMaxHealth()) 
			killer.setHealth(killer.getMaxHealth());
		else 
			killer.setHealth(killer.getHealth() + INCREASE);
		
	}
	

}
