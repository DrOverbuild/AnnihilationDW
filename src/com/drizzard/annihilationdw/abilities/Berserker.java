package com.drizzard.annihilationdw.abilities;

import com.drizzard.annihilationdw.handlers.GameState;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.drizzard.annihilationdw.handlers.Game;

public class Berserker extends Ability {
	
	public double STARTING_HEALTH = 18D;
	public double MAX_HEALTH = 26D;
	public double INCREASE = 2D;
	
	public Berserker() {
		registerAbility(this);
		setName("Berserker");
		setIcon(Material.DIAMOND_AXE);
		addDescription("Player starts with " + STARTING_HEALTH / 2 + " hearts but ");
		addDescription("for every kill gets " + INCREASE / 2 + " heart");
		addDescription("until a maximum of " + MAX_HEALTH / 2 + " hearts");
	}

	@Override
	public void initialize(Player player) {
		player.setHealth(STARTING_HEALTH);
		player.setMaxHealth(MAX_HEALTH);
	}

	@Override
	public void cleanup(Player player) {
		player.setMaxHealth(20D);
	}

	@EventHandler
	public void onKill(PlayerDeathEvent event) {
		if(event.getEntity().getKiller() == null) return;
		if(Game.getGameState() != GameState.IN_GAME) return;
		Player killer = event.getEntity().getKiller();
		if(!hasAbility(this, killer)) return;
		
		// Adding 1 heart
		
		if(killer.getHealth() + INCREASE >= killer.getMaxHealth()) 
			killer.setHealth(killer.getMaxHealth());
		else 
			killer.setHealth(killer.getHealth() + INCREASE);
		
	}
	

}
