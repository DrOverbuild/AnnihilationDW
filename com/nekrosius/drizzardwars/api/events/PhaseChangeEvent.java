package com.nekrosius.drizzardwars.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PhaseChangeEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	
	private int phase;
	private int time;
	
	public PhaseChangeEvent(int phase, int time) {
		this.phase = phase;
		this.time = time;
	}

	/**
	 * 
	 * @return phase id (1-5)
	 */
    public int getPhase() {
		return phase;
	}
    
    /**
     * 
     * @return time in seconds of current phase
     */
	public int getTime() {
		return time;
	}

	public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
