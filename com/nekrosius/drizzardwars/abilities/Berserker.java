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
	 * Player starts with 9 hearts but for every kill he makes
	 * gains 1 heart until a maximum of 13 hearts
	 */
	
	public Berserker() {
		registerAbility(this);
		setName("Berserker");
		setIcon(Material.DIAMOND_AXE);
	}
	
	@EventHandler
	public void onJoin(JoinGameEvent event) {
		event.getPlayer().setHealth(18D);
		event.getPlayer().setMaxHealth(26D);
	}
	
	@EventHandler
	public void onKill(PlayerDeathEvent event) {
		if(event.getEntity().getKiller() == null) return;
		if(Game.getGameState() != GameState.IN_GAME) return;
		if(! PlayerHandler.getAbility( event.getEntity().getKiller() ).getName().equals( this.getName() )) return;
		Player killer = event.getEntity().getKiller();
		
		// Adding 1 heart
		
		if(killer.getHealth() + 2 >= killer.getMaxHealth()) 
			killer.setHealth(killer.getMaxHealth());
		else 
			killer.setHealth(killer.getHealth() + 2D);
		
	}
	

}
