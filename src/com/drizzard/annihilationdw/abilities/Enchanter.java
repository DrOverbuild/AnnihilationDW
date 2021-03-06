package com.drizzard.annihilationdw.abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Enchanter extends Ability {

    public int BOTTLE_CHANCE = 2;
    public Material ICON = Material.ENCHANTMENT_TABLE;
    public int MIN_IRON = 0,
            MAX_IRON = 1,
            MIN_COAL = 0,
            MAX_COAL = 1,
            MIN_GOLD = 0,
            MAX_GOLD = 2,
            MIN_DIAMOND = 1,
            MAX_DIAMOND = 4,
            MIN_EMERALD = 1,
            MAX_EMERALD = 4,
            MIN_LAPIS = 1,
            MAX_LAPIS = 3,
            MIN_REDSTONE = 1,
            MAX_REDSTONE = 3;
    public String NAME = "Enchanter";
    private Random generator;

    public Enchanter() {
        registerAbility(this);
        setName(NAME);
        setIcon(ICON);
        addDescription("Increased exp from mining and");
        addDescription(BOTTLE_CHANCE + "% chance to get EXP Bottle when mining.");
        generator = new Random();
    }

    @EventHandler
    public void onMine(BlockBreakEvent event) {
        if (!hasAbility(this, event.getPlayer())) return;

        Player player = event.getPlayer();

        // GIVING EXP BOOST

        if (event.getBlock().getType().equals(Material.IRON_ORE)) {
            player.giveExp(getRandom(MIN_IRON, MAX_IRON));
        } else if (event.getBlock().getType().equals(Material.COAL_ORE)) {
            player.giveExp(getRandom(MIN_COAL, MAX_COAL));
        } else if (event.getBlock().getType().equals(Material.GOLD_ORE)) {
            player.giveExp(getRandom(MIN_GOLD, MAX_GOLD));
        } else if (event.getBlock().getType().equals(Material.DIAMOND_ORE)) {
            player.giveExp(getRandom(MIN_DIAMOND, MAX_DIAMOND));
        } else if (event.getBlock().getType().equals(Material.EMERALD_ORE)) {
            player.giveExp(getRandom(MIN_EMERALD, MAX_EMERALD));
        } else if (event.getBlock().getType().equals(Material.LAPIS_ORE)) {
            player.giveExp(getRandom(MIN_LAPIS, MAX_LAPIS));
        } else if (event.getBlock().getType().equals(Material.REDSTONE_ORE)) {
            player.giveExp(getRandom(MIN_REDSTONE, MAX_REDSTONE));
        } else return;

        // EXP BOTTLE

        if (getRandom(0, 100) <= BOTTLE_CHANCE) {
            player.getInventory().addItem(new ItemStack(Material.EXP_BOTTLE));
        }

    }

    private int getRandom(int min, int max) {
        return min + generator.nextInt(max);
    }

}
