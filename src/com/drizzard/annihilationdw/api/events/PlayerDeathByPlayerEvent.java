package com.drizzard.annihilationdw.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a player is killed by another player. If you want to cancel a death event where a
 * player was killed by
 * another player, handle this event rather than EntityDamagedByEntityEvent.
 * Created by jasper on 6/26/16.
 */
public class PlayerDeathByPlayerEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    boolean cancelled = false;
    Player damager;
    Player victim;

    public PlayerDeathByPlayerEvent(Player victim, Player damager) {
        this.victim = victim;
        this.damager = damager;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getVictim() {
        return victim;
    }

    public void setVictim(Player victim) {
        this.victim = victim;
    }

    public Player getDamager() {
        return damager;
    }

    public void setDamager(Player damager) {
        this.damager = damager;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
}
