package com.drizzard.annihilationdw.api.events;

import com.drizzard.annihilationdw.handlers.Team;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NexusDestroyEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private Team defeated;
    private Player killer;

    public NexusDestroyEvent(Team defeated, Player killer) {
        this.defeated = defeated;
        this.killer = killer;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * @return the destroyed team
     *
     * @see Team
     */
    public Team getDefeatedTeam() {
        return defeated;
    }

    /**
     * @return the player who made the last hit
     */
    public Player getKiller() {
        return killer;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

}
