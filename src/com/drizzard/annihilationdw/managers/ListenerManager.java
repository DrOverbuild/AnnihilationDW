package com.drizzard.annihilationdw.managers;

import com.drizzard.annihilationdw.Main;
import com.drizzard.annihilationdw.handlers.Team;
import com.drizzard.annihilationdw.listeners.GameListener;
import com.drizzard.annihilationdw.listeners.InventoryListener;
import com.drizzard.annihilationdw.listeners.MapSetupListener;
import com.drizzard.annihilationdw.listeners.PlayerListener;
import com.drizzard.annihilationdw.listeners.SignListener;
import com.drizzard.annihilationdw.utils.Convert;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

public class ListenerManager {

    private Main pl;

    public ListenerManager(Main plugin) {
        this.pl = plugin;
        PluginManager pm = pl.getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(pl), pl);
        pm.registerEvents(new InventoryListener(pl), pl);
        pm.registerEvents(new MapSetupListener(pl), pl);
        pm.registerEvents(new GameListener(pl), pl);
        pm.registerEvents(new SignListener(pl), pl);
    }

    @SuppressWarnings("deprecation")
    public static void removeTeamWool(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                if (item.getType().equals(Material.WOOL)) {
                    for (Team team : TeamManager.getTeams()) {
                        if (item.getData().getData() == (short) Convert.ChatColorToInt(team.getColor())) {
                            player.getInventory().remove(item);
                        }
                    }
                }
            }
        }
    }

    public Main getMainClass() {
        return pl;
    }
}