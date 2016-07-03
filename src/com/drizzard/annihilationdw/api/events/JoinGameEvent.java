package com.drizzard.annihilationdw.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.drizzard.annihilationdw.handlers.Team;

public class JoinGameEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private Player player;
	private Team team;

	/**
	 * Called when players joins active game
	 * @param player player who joined the game
	 * @param team current team of player
	 * @see Team
	 */
	public JoinGameEvent(Player player, Team team) {
		this.player = player;
		this.team = team;
	}

	/**
	 * @return Returns the player involved in this event
	 */
	public Player getPlayer() {
		return player;
	}
	
	public Team getTeam() {
		return team;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
