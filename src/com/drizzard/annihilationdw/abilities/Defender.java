package com.drizzard.annihilationdw.abilities;

import com.drizzard.annihilationdw.handlers.Team;
import com.drizzard.annihilationdw.managers.TeamManager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Defender extends Ability {

    public int DURATION_IN_SECONDS = 5;
    public int EFFECT_LEVEL = 2;
    public Material ICON = Material.IRON_SWORD;
    public int KILL_EFFECT_DURATION_IN_SECONDS = 3;
    public int KILL_EFFECT_LEVEL = 1;
    public String NAME = "Defender";

    public Defender() {
        registerAbility(this);
        setName(NAME);
        setIcon(ICON);
        addDescription("Regeneration " + EFFECT_LEVEL + " for");
        addDescription(DURATION_IN_SECONDS + " seconds when staying near to Nexus and ");
        addDescription("Increased Damage " + KILL_EFFECT_LEVEL + " for");
        addDescription(KILL_EFFECT_DURATION_IN_SECONDS + " seconds for every kill near Nexus!");
    }

    @Override
    public void initialize(Player player) {
        new BukkitRunnable() {

            @Override
            public void run() {
                if (isNearNexus(player.getLocation(), TeamManager.getTeam(player))) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,
                            DURATION_IN_SECONDS * 20, EFFECT_LEVEL - 1));
                }
            }

        }.runTaskTimer(plugin, 40L, 40L);
    }

    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;
        Player killer = event.getEntity().getKiller();
        if (!hasAbility(this, killer)) return;
        // Adding Increased Damage effect is kill executed near his own nexus

        if (isNearNexus(killer.getLocation(), TeamManager.getTeam(killer))) {
            killer.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,
                    KILL_EFFECT_DURATION_IN_SECONDS * 20, KILL_EFFECT_LEVEL - 1));
        }
    }

    private boolean isNearNexus(Location loc, Team team) {
        if (team == null) {
            return false;
        }
        return loc.toVector().distance(team.getNexusLocation().toVector()) < 4D;
    }

}
