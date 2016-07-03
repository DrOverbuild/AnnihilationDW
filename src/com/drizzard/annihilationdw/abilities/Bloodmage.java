package com.drizzard.annihilationdw.abilities;

import com.drizzard.annihilationdw.api.events.PlayerDeathByPlayerEvent;
import com.drizzard.annihilationdw.files.MessageFile;
import com.drizzard.annihilationdw.handlers.Team;
import com.drizzard.annihilationdw.managers.TeamManager;
import com.drizzard.annihilationdw.utils.ItemStackGenerator;
import com.drizzard.annihilationdw.Main;
import com.drizzard.annihilationdw.handlers.MessageHandler;
import com.drizzard.annihilationdw.utils.Cooldowns;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.List;

/**
 * Created by jasper on 6/24/16.
 */
public class Bloodmage extends Ability {

	public String NAME = "Bloodmage";
	public String SKILL_NAME = ChatColor.RED +  "" + ChatColor.BOLD + "Corrupt";
	public Material ICON = Material.SPIDER_EYE;
	public int PERCENT_CHANGE_TO_INFLICT_POISON = 10;
	public int POISON_DURATION = 4;
	public int ABILITY_DURATION = 4;
	public int NUMBER_OF_MAX_HEARTS_TO_DECREASE = 2;
	public int ABILITY_RANGE = 3;
	public int COOLDOWN = 90;
	public String FINISHED_COOLING_DOWN_MESSAGE = ChatColor.GREEN + "You can now use " + SKILL_NAME + ChatColor.RESET + ChatColor.GREEN + " again.";

	public Bloodmage() {
		registerAbility(this);
		setName(NAME);
		setIcon(ICON);

		//TODO LOCALIZE THIS
		addDescription("* " + PERCENT_CHANGE_TO_INFLICT_POISON + "% chance swords will");
		addDescription(" inflict poison for " + POISON_DURATION + " seconds.");
		addDescription("* Get an item that reduces max health by ");
		addDescription(" " + NUMBER_OF_MAX_HEARTS_TO_DECREASE + " hearts of players within " + ABILITY_RANGE + " blocks of you");
		addDescription(" for " + ABILITY_DURATION + " seconds.");
	}

	@Override
	public void initialize(Player player) {
		player.getInventory().addItem(ItemStackGenerator.createItem(Material.FERMENTED_SPIDER_EYE, 1, 0, SKILL_NAME, Collections.singletonList("Right click to use.")));
	}

	@EventHandler
	public void playerKilledByPlayer(PlayerDeathByPlayerEvent event) {
		if (!hasAbility(this, event.getDamager())) return;

		event.setCancelled(true);
	}

	@EventHandler
	public void playerHitByPlayer(EntityDamageByEntityEvent event){
		if(!event.getDamager().getType().equals(EntityType.PLAYER)) return;
		if(!event.getEntity().getType().equals(EntityType.PLAYER)) return;

		Player victim = (Player)event.getEntity();
		Player damager = (Player)event.getDamager();

		if(!hasAbility(this, damager)) return;

		int random = Main.getRandom(0,100);
		if(random <= PERCENT_CHANGE_TO_INFLICT_POISON) {
			victim.addPotionEffect(new PotionEffect(PotionEffectType.POISON, POISON_DURATION, 0, false, false));
		}
	}

	@EventHandler
	public void playerUseItem(PlayerInteractEvent e){
		if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)&&!e.getAction().equals(Action.RIGHT_CLICK_AIR)) return;

		if(e.getItem() == null || !e.getItem().hasItemMeta() || !e.getItem().getItemMeta().hasDisplayName()) return;

		if(!e.getItem().getItemMeta().getDisplayName().equals(SKILL_NAME)) return;

		useSkill(e.getPlayer(), e.getItem());
	}

	@EventHandler
	public void playerInteractsWithEntity(PlayerInteractAtEntityEvent e){
		if(e.getPlayer().getItemInHand() == null || !e.getPlayer().getItemInHand().hasItemMeta() || !e.getPlayer().getItemInHand().getItemMeta().hasDisplayName()) return;

		if(!e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(SKILL_NAME)) return;

		useSkill(e.getPlayer(),e.getPlayer().getItemInHand());
	}

	void useSkill(Player p, ItemStack item){
		if(!hasAbility(this, p)){
			p.sendMessage(MessageHandler.formatString(MessageFile.getMessage("ability.lacking"), getName()));
			p.getInventory().remove(item);
			return;
		}

		if(!Cooldowns.tryCooldown(p, SKILL_NAME, COOLDOWN * 1000, FINISHED_COOLING_DOWN_MESSAGE)){
			p.sendMessage(MessageHandler.formatLong(MessageFile.getMessage("ability.cooling-down"), Cooldowns.getCooldown(p, SKILL_NAME) / 1000));
			return;
		}

		List<Entity> mobs = p.getNearbyEntities((double)ABILITY_RANGE, (double)ABILITY_RANGE, (double)ABILITY_RANGE);
		for(Entity mob : mobs){
			if(mob instanceof Player){
				Player affectedPlayer = (Player)mob;
				Team enemyTeam = TeamManager.getTeam(affectedPlayer);
				Team team = TeamManager.getTeam(p);
				if(enemyTeam != null && team != null) {
					if (!team.getName().equals(enemyTeam.getName())) {
						new MaxHealthRunnable(affectedPlayer, ABILITY_DURATION, NUMBER_OF_MAX_HEARTS_TO_DECREASE * 2);
					}
				}
			}
		}
	}
}

class MaxHealthRunnable extends BukkitRunnable {

	Player affectedPlayer;
	int numberOfMaxHeartsToDecrease;

	MaxHealthRunnable(Player affectedPlayer, int abilityDuration, int numberOfMaxHeartsToDecrease) {
		this.affectedPlayer = affectedPlayer;
		this.numberOfMaxHeartsToDecrease = numberOfMaxHeartsToDecrease;
		affectedPlayer.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, abilityDuration * 20, 0, false, false));
		affectedPlayer.setMaxHealth(affectedPlayer.getMaxHealth() - numberOfMaxHeartsToDecrease);
		this.runTaskLater(Ability.plugin, abilityDuration * 20);
	}

	@Override
	public void run() {
		affectedPlayer.setMaxHealth(affectedPlayer.getMaxHealth() + numberOfMaxHeartsToDecrease);
	}
}
