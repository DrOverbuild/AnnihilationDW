package com.nekrosius.drizzardwars.handlers;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.nekrosius.drizzardwars.Main;
import com.nekrosius.drizzardwars.files.ConfigFile;
import com.nekrosius.drizzardwars.files.MapFile;
import com.nekrosius.drizzardwars.managers.MapManager;
import com.nekrosius.drizzardwars.managers.TeamManager;
import com.nekrosius.drizzardwars.utils.Convert;
import com.nekrosius.drizzardwars.utils.ItemStackGenerator;

public class Lobby {
	
	private static Main plugin = (Main)Bukkit.getPluginManager().getPlugin("AnnihilationDW");

	public static void setupLobby(final Player player, boolean teleportPlayer){
		if(Game.getGameState().equals(GameState.LOBBY) && !Game.isCountdownStarted()){
			if(Bukkit.getOnlinePlayers().size() >= ConfigFile.config.getInt("minimum-player-amount")){
				Game.startCountdown();
			}
		}
		ScoreboardHandler.update(player);
		player.setHealth(player.getMaxHealth());
		player.setFoodLevel(20);
		player.getInventory().clear();
		player.getEnderChest().clear();
		player.setGameMode(GameMode.SURVIVAL);
		new BukkitRunnable(){
			@Override
			public void run() {
				if(PlayerHandler.isSpectating(player) && Game.isGameStarted()){
					player.getInventory().clear();
					MapFile.createConfig("plugins/AnnihilationDW/Maps/" + MapManager.getActiveMap().getName());
					if(teleportPlayer) {
						if (TeamManager.hasTeam(player)) {
							player.teleport(TeamManager.getTeam(player).getSpectatorSpawnpoint());
						} else {
							player.teleport(TeamManager.getTeam(0).getSpectatorSpawnpoint());
						}
					}
					//player.getInventory().addItem(ItemStackGenerator.createItem(Material.COMPASS, 0, 0, MessageHandler.format(MessageFile.getMessage("compass.default")), null));
					player.setGameMode(GameMode.SURVIVAL);
					player.setAllowFlight(true);
					PlayerHandler.hidePlayer(player);
				}
				else if(TeamManager.hasTeam(player) && Game.isGameStarted()){
					player.setAllowFlight(false);
					Game.setupGameInventory(player);
					Team team = TeamManager.getTeam(player);
					if(teleportPlayer) {
						player.teleport(team.getSpawnpoint());
					}
				} else if(!PlayerHandler.isPlayerPlaying(player)||Game.getGameState().equals(GameState.LOBBY)){
					player.setAllowFlight(false);
					setupItems(player);
					player.setLevel(0);
					player.setExp(0F);
					if(teleportPlayer) {
						if (ConfigFile.config.getString("spawn-location") == null) {
							player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
						} else {
							player.teleport(Convert.StringToLocation(ConfigFile.config.getString("spawn-location"), true, false));
						}
					}
				}
				TabHandler.setColor(player);
			}
		}.runTaskLater(plugin, 1L);
	}

	/**
	 * Calls setupLobby(player, true);
	 */
	public static void setupLobby(final Player player){
		setupLobby(player, true);
	}
	
	public static void setupItems(Player player)
	{
		player.getInventory().clear();
		player.getInventory().setHelmet(null);
		player.getInventory().setChestplate(null);
		player.getInventory().setLeggings(null);
		player.getInventory().setBoots(null);

		player.getInventory().setItem(0,ConfigFile.getKitSelectorItem());

//		player.getInventory().setItem(0, ItemStackGenerator.createItem(Material.STICK, 0, 0,
//				ChatColor.RED + "" + ChatColor.BOLD + "Kits",
//				Arrays.asList(ChatColor.GRAY + "Find all info about kits!")));
	}
}