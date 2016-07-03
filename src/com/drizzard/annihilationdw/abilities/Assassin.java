package com.drizzard.annihilationdw.abilities;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.drizzard.annihilationdw.files.MessageFile;
import com.drizzard.annihilationdw.handlers.MessageHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.drizzard.annihilationdw.handlers.Game;
import com.drizzard.annihilationdw.handlers.GameState;
import com.drizzard.annihilationdw.utils.Cooldowns;
import com.drizzard.annihilationdw.utils.ItemStackGenerator;

public class Assassin extends Ability {
	
	public String NAME = "Assassin";
	public String SKILL = "Leap";
	public Material ICON = Material.CHAINMAIL_HELMET;
	public int DURATION_IN_SECONDS = 10;
	public int COOLDOWN_IN_SECONDS = 90;
	
//	private Map<String, Boolean> active;
	private Map<String, ItemStack[]> armor;
	
	public Assassin() {
		registerAbility(this);
		setName(NAME);
		setIcon(ICON);
		addDescription("Activating skill gives invisibility,");
		addDescription("haste and speed but removes armor until");
		addDescription("skill ends or you hit someone!");
		addDescription("Duration: " + DURATION_IN_SECONDS + " seconds");
		addDescription("Cooldown: " + COOLDOWN_IN_SECONDS + " seconds");
		
//		active = new HashMap<String, Boolean>();
		armor = new HashMap<String, ItemStack[]>();
	}

	@Override
	public void initialize(Player player) {
		player.getInventory().addItem(ItemStackGenerator.createItem(Material.SUGAR, 1, 0, ChatColor.RED + SKILL,
				Arrays.asList("Right-Click to activate!")));
	}

	@Override
	public void cleanup(Player player) {
		armor.remove(player);
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		
		// Only right click for this ability
		if(event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) return;
		
		// Checking if game has started
		if(Game.getGameState() != GameState.IN_GAME) return;
		
		if(!hasAbility(this, event.getPlayer())) return;
		
		if(! event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.RED + SKILL)) return;
		
		if(isActive(event.getPlayer())) {
			event.getPlayer().sendMessage(MessageFile.formatMessage("ability.active"));
			return;
		}
		
		// Checking if player can use ability or not yet (Cooldowns are in miliseconds)
		if(!Cooldowns.tryCooldown(event.getPlayer(), SKILL, COOLDOWN_IN_SECONDS * 1000)) {
			event.getPlayer().sendMessage(MessageHandler.formatLong("ability.cooling-down", Cooldowns.getCooldown(event.getPlayer(), SKILL) / 1000));
			return;
		}
		
		// Saving the armor and adding invisibility
		
		setArmor(event.getPlayer(), event.getPlayer().getInventory().getArmorContents());
		event.getPlayer().getInventory().setArmorContents(null);
		event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, DURATION_IN_SECONDS * 20, 0, false, false));
		
		// Re-applying the armor after the time is over
		new BukkitRunnable() {
			@Override
			public void run() {
				if(isActive(event.getPlayer()))
					event.getPlayer().getInventory().setArmorContents(getArmor(event.getPlayer()));
				armor.remove(event.getPlayer().getName());
			}
		}.runTaskLater(plugin, DURATION_IN_SECONDS * 20);
		
	}
	
	/**
	 * Remove the ability when player gets damaged
	 */
	@EventHandler
	public void onHit(EntityDamageByEntityEvent event) { 
		// Checking if both entities are players
		if(!(event.getEntity() instanceof Player && event.getDamager() instanceof Player)) return;
		
		Player damager = (Player) event.getDamager();
		
		if(!hasAbility(this, damager)) return;
		if(!isActive(damager)) return;
		
		damager.removePotionEffect(PotionEffectType.INVISIBILITY);
		damager.getInventory().setArmorContents(getArmor(damager));

		armor.remove(damager.getName());
		
	}
	
	boolean isActive(Player player) {
		if(armor.get(player.getName()) == null) return false;
		return armor.containsKey(player.getName());
	}
	
	ItemStack[] getArmor(Player player) {
		return armor.get(player.getName());
	}
	
	void setArmor(Player player, ItemStack[] armor) {
		this.armor.put(player.getName(), armor);
	}
	

}
