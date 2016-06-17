package com.nekrosius.annihilationdw.abilities;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.nekrosius.annihilationdw.api.events.JoinGameEvent;
import com.nekrosius.annihilationdw.api.objects.Ability;

public class Archer extends Ability {
	
	public String NAME = "Archer";
	public Material ICON = Material.BOW;
	public int INTERVAL_IN_SECONDS = 20;
	public int ARROWS = 4;
	public double DAMAGE_BOOST = 1D;
	
	private Map<String, Boolean> active;
	
	public Archer() {
		registerAbility(this);
		setName(NAME);
		setIcon(ICON);
		addDescription("+" + DAMAGE_BOOST + " damage with a bow");
		addDescription("and every " + INTERVAL_IN_SECONDS + " seconds");
		addDescription("get " + ARROWS + " arrows!");
		
		active = new HashMap<String, Boolean>();
	}
	
	@EventHandler
	public void onJoin(JoinGameEvent event) {
		if(isActive(event.getPlayer())) return;
		setActive(event.getPlayer(), true);
		new BukkitRunnable() {
			@Override
			public void run() {
				if(!event.getPlayer().isOnline()) {
					setActive(event.getPlayer(), false);
					this.cancel();
					return;
				}
				if(event.getPlayer().isDead()) return;
				event.getPlayer().getInventory().addItem(new ItemStack(Material.ARROW, ARROWS));
				
			}
		}.runTaskTimer(plugin, INTERVAL_IN_SECONDS * 20, INTERVAL_IN_SECONDS * 20);
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		// Checking if both entities are players
		if(!(event.getEntity() instanceof Player && event.getDamager() instanceof Player)) return;
		
		// Checking if damage was done with projectiles
		if(!event.getCause().equals(DamageCause.PROJECTILE)) return;
		
		Player damager = (Player) event.getDamager();
		
		if(hasAbility(this, damager))
			event.setDamage(event.getDamage() + DAMAGE_BOOST);
	}
	
	boolean isActive(Player player) {
		if(active.get(player.getName()) == null) return false;
		return active.get(player.getName());
	}
	
	void setActive(Player player, boolean bool) {
		active.put(player.getName(), bool);
	}

}
