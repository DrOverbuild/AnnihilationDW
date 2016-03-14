package com.nekrosius.drizzardwars.abilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.nekrosius.drizzardwars.api.events.JoinGameEvent;
import com.nekrosius.drizzardwars.api.objects.Ability;
import com.nekrosius.drizzardwars.handlers.Team;
import com.nekrosius.drizzardwars.managers.TeamManager;

public class Defender extends Ability {
	
	public String NAME = "Defender";
	public Material ICON = Material.IRON_SWORD;
	
	public int DURATION_IN_SECONDS = 5;
	public int EFFECT_LEVEL = 2;
	
	public int KILL_EFFECT_DURATION_IN_SECONDS = 3;
	public int KILL_EFFECT_LEVEL = 1;
	
	public Defender() {
		registerAbility(this);
		setName(NAME);
		setIcon(ICON);
		addDescription("Regeneration " + EFFECT_LEVEL + " for");
		addDescription(DURATION_IN_SECONDS + " seconds when staying near to Nexus and ");
		addDescription("Increased Damage " + KILL_EFFECT_LEVEL + " for");
		addDescription(KILL_EFFECT_DURATION_IN_SECONDS + " seconds for every kill near Nexus!");
	}
	
	public void onJoin(JoinGameEvent event) {
		if (event.getTeam() == null) return;
		if (!hasAbility(this, event.getPlayer())) return;

		// Checking if player is nearby nexus and granting effects

		new BukkitRunnable() {

			@Override
			public void run() {
				if (isNearNexus(event.getPlayer().getLocation(), event.getTeam())) {
					event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,
							DURATION_IN_SECONDS * 20, EFFECT_LEVEL - 1));
				}
			}

		}.runTaskTimer(plugin, 40L, 40L);
	}
	
	public void onKill(PlayerDeathEvent event) {
		if(event.getEntity().getKiller() == null) return;
		Player killer = event.getEntity().getKiller();
		if(!hasAbility(this, killer)) return;
		// Adding Increased Damage effect is kill executed near his own nexus
		
		if(isNearNexus(killer.getLocation(), TeamManager.getTeam(killer))) {
			killer.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,
					KILL_EFFECT_DURATION_IN_SECONDS * 20, KILL_EFFECT_LEVEL - 1));
		}
	}
	
	private boolean isNearNexus(Location loc, Team team) {
		return loc.toVector().distance(team.getNexusLocation().toVector()) < 4D;
	}

}
