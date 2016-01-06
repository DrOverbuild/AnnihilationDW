package com.nekrosius.drizzardwars.listeners;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.nekrosius.drizzardwars.handlers.*;
import com.nekrosius.drizzardwars.managers.BarManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.nekrosius.drizzardwars.Main;
import com.nekrosius.drizzardwars.files.ConfigFile;
import com.nekrosius.drizzardwars.files.MessageFile;
import com.nekrosius.drizzardwars.files.PlayerFile;
import com.nekrosius.drizzardwars.managers.TeamManager;
import com.nekrosius.drizzardwars.utils.Convert;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.scheduler.BukkitTask;

public class PlayerListener implements Listener {
	
	private Map<String, Integer> respawnTimer = new HashMap<String, Integer>();
	
	private Main pl;
	public PlayerListener(Main pl) {
		this.pl = pl;
	}
	
	@EventHandler
	public void onLogin(PlayerLoginEvent event) {
		if(Bukkit.getOnlinePlayers().size() >= ConfigFile.config.getInt("team-size") * 4){
			if(!event.getPlayer().hasPermission("drwars.spectator")){
				event.disallow(Result.KICK_FULL, MessageHandler.format(MessageFile.getMessage("kick.full")));
				return;
			}
		}

		if(Game.isGameStarted()){
			Team team = TeamManager.getTeam(event.getPlayer());
			if(team!=null) {
				if (team.getNexusHealth() <= 0 && !event.getPlayer().hasPermission("drwars.spectator")) {
					event.disallow(Result.KICK_OTHER, MessageHandler.format(MessageFile.getMessage("kick.lost")));
					return;
				}
			}

			if (Game.getPhase() > 3 && !event.getPlayer().hasPermission("drwars.spectator")) {
				event.disallow(Result.KICK_OTHER, MessageHandler.format(MessageFile.getMessage("kick.too-late")));
			}
		}
	}

	// TODO nametags
	/*
	@EventHandler
	public void onNameTag(AsyncPlayerReceiveNameTagEvent event) {
		if(Game.isGameStarted()){
			if(TeamManager.hasTeam(event.getNamedPlayer())){
				event.setTag(TeamManager.getTeam(event.getNamedPlayer()).getColor() + event.getNamedPlayer().getName());
			}else{
				event.setTag(ChatColor.LIGHT_PURPLE + event.getNamedPlayer().getName());
			}
		}else{
			event.setTag(ChatColor.GREEN + event.getNamedPlayer().getName());
		}
	}
	*/
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);
		final Player player = event.getPlayer();
		BarManager.removeBar(player);
		PlayerFile.createConfig(player);
		if(Game.getGameState().equals(GameState.LOBBY)){
			String msg = MessageFile.getMessage("player.join");
			msg = MessageHandler.formatPlayer(msg, player);
			for(Player p : Bukkit.getOnlinePlayers()){
				MessageHandler.sendMessage(p, msg);
			}
			Lobby.setupLobby(player);
			MessageHandler.sendMessage(player, MessageFile.getMessageList("player.join-information"));
		}else if(Game.getGameState().equals(GameState.IN_GAME)){
			if(!PlayerHandler.isPlayerPlaying(player)) {
				Team toJoin = TeamManager.getTeamToJoin();
				PlayerHandler.setTeamOfPlayer(player, toJoin);
				player.teleport(toJoin.getSpawnpoint());
				player.getInventory().clear();
				Game.setupGameInventory(player);
//				PlayerHandler.setPlayerPlaying(player, true);
				TabHandler.setColor(player);
				BarManager.setMessage(player, MessageHandler.getPhaseMessage(Game.getPhase()));
				//BossBarAPI.setMessage(player, MessageHandler.getPhaseMessage(Game.getPhase()));
//				int i = 0;
//				for(Player p : Bukkit.getOnlinePlayers()){
//					if(p != player){
//						if(BossBarAPI.getMessage(p) != null){
//							if(BossBarAPI.getHealth(p) > 1){
//								BossBarAPI.setMessage(player, BossBarAPI.getMessage(p), (int)BossBarAPI.getHealth(p));
//								break;
//							}
//						}
//					}
//					if(i == 30) break;
//				}
			}else{
				BarManager.setMessage(player, MessageHandler.getPhaseMessage(Game.getPhase()));
				//BossBarAPI.setMessage(player, MessageHandler.getPhaseMessage(Game.getPhase()));
				if(TeamManager.getTeam(player).getNexusHealth() <= 0){
					player.setHealth(0d);
					return;
				}
				TabHandler.setColor(player);
			}

			if(respawnTimer.containsKey(player.getName())){
				PlayerHandler.setSpectating(player,false);
				respawnTimer.remove(player.getName());
				Lobby.setupLobby(player);
			}
		}else{
			PlayerHandler.setSpectating(player,true);
			Lobby.setupLobby(player);
		}

		ScoreboardHandler.update(player);

		for(Player p : Bukkit.getOnlinePlayers()){
			player.showPlayer(p);
			p.showPlayer(player);
		}

		PlayerHandler.hideHiddenPlayers(player);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		event.setQuitMessage(null);
		Player player = event.getPlayer();
		if(Game.getGameState().equals(GameState.LOBBY)){
			String msg = MessageFile.getMessage("player.leave");
			msg = MessageHandler.formatPlayer(msg, player);
			for(Player p : Bukkit.getOnlinePlayers()){
				MessageHandler.sendMessage(p, msg);
			}
		}
		PlayerFile.setPoints(player, Points.getPoints(player));
		PlayerHandler.quit(player);

		// Kill off a team if the player who left was the only player in that team and Game.getPhase() > 3
		if(Game.isGameStarted() && Game.getPhase()>3){
			for(Team t:TeamManager.getTeams()){
				if(t.getPlayers().size() == 0 && t.getNexusHealth() > 0){
					TeamManager.destroyTeam(t);
				}
			}
		}

		if(Main.getAlivePlayers().size() == 1 && Game.isGameStarted()){
			Game.finish(null);
			Main.println("Finishing game because there are no players online who are not spectating.");
			return;
		}

		// The following ends the game if there's only one team left after player has left.
		List<Team> teams = TeamManager.getAliveTeams();
		if(Main.getAlivePlayers().size() == 2 && Game.isGameStarted()) {
			Team t = TeamManager.getTeam(player);
			if(t!=null){
				if(t.getPlayers().size() < 2){
					TeamManager.destroyTeam(t);
				}
			}
		}

	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		final Player player = event.getPlayer();
		if(TeamManager.hasTeam(player)){
			Team team = TeamManager.getTeam(player);
			if(team!=null) {
				event.setRespawnLocation(team.getSpectatorSpawnpoint());
			}
			PlayerHandler.setSpectating(player, true);
			if(team != null) {
				if (team.getNexusHealth() <= 0) {
					PlayerHandler.teamNexusIsDestroyed(player);
				}
			}
			if(Game.isGameStarted()){
				respawnTimer.put(player.getName(), Game.getRespawnTimer());
				new BukkitRunnable()
				{
					public void run(){
						if(!player.isOnline()){
							Main.println("Stopped respawn timer for " + player.getName() + " because he logged off.");
							this.cancel();
							return;
						}
						if(respawnTimer.get(player.getName()) != null){
							player.setLevel(respawnTimer.get(player.getName()));
							respawnTimer.put(player.getName(), respawnTimer.get(player.getName()) - 1);
							if(respawnTimer.get(player.getName()) < 0){
								respawnTimer.remove(player.getName());
								Team t = TeamManager.getTeam(player);
								if(t != null){
									player.teleport(t.getSpawnpoint());
								}
								PlayerHandler.setSpectating(player, false);
								Lobby.setupLobby(player, false);
								this.cancel();
							}
						}else{
							Main.println("Stopping task for respawn timer for " + player.getName() + ".");
							this.cancel();
						}
					}
				}.runTaskTimer(pl, 0L, 20L);
			}
		}
		Lobby.setupLobby(player);
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent event){
		if(!Game.isGameStarted()){
			if(!event.getPlayer().isOp())	
				event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent event){
		if(!Game.isGameStarted()){
			if(!event.getPlayer().isOp())	
				event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		final Player player = event.getEntity();
		event.setDeathMessage(null);
		if(!Game.isGameStarted()){
			event.getDrops().clear();
			event.setDroppedExp(0);
		}else{
			List<ItemStack> drops = event.getDrops();
			ListIterator<ItemStack> litr = drops.listIterator();
			while(litr.hasNext()){
	        	ItemStack item = litr.next();
				if(item.getType().equals(Material.LEATHER_HELMET) || item.getType().equals(Material.LEATHER_CHESTPLATE) || item.getType().equals(Material.LEATHER_LEGGINGS) || item.getType().equals(Material.LEATHER_BOOTS)){
					litr.remove();
				}else if(item.getType().equals(Material.COMPASS) || item.getType().equals(Material.WORKBENCH)){
					litr.remove();
				}else if(item.getType().equals(Material.WOOD_SWORD) || item.getType().equals(Material.WOOD_AXE) || item.getType().equals(Material.WOOD_PICKAXE)){
					litr.remove();
				}else if(item.getType().equals(Material.GOLD_INGOT)){
					litr.remove();
				}
	        }
		}
		Points.addPoints(player, Points.getDeathPoints());
		Team t = TeamManager.getTeam(player);
		if(t != null) {
			if (t.getNexusHealth() <= 0) {
//			t.removePlayer(player);
				if (!player.hasPermission("drwars.spectator")) {
					PlayerHandler.quit(player);
					player.kickPlayer(MessageHandler.format(MessageFile.getMessage("kick.lost")));
					return;
				}
			}
		}

		// Record amount of gold player has so we can give it back when he respawns.
		HashMap<Integer, ? extends ItemStack> goldIngots = player.getInventory().all(Material.GOLD_INGOT);
		int gold = 0;
		for(Integer i:goldIngots.keySet()){
			gold += goldIngots.get(i).getAmount();
		}
		if(gold > 0){
			PlayerHandler.setPlayerGold(player, gold);
		}

		player.spigot().respawn();
//		new BukkitRunnable(){
//			@Override
//			public void run() {
//				player.spigot().respawn();
//			}
//		}.runTaskLater(pl, 5L);
	}
	
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		if(!Game.isGameStarted()){
			event.setFoodLevel(20);
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event){
		if(event.getEntity() instanceof Player){
			if(!Game.isGameStarted()){
				event.setCancelled(true);
				if(event.getCause() == DamageCause.VOID){
					if(ConfigFile.config.getString("spawn-location") == null){
						event.getEntity().teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
					}else{
						event.getEntity().teleport(Convert.StringToLocation(ConfigFile.config.getString("spawn-location"), true, false));
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onCmd(PlayerCommandPreprocessEvent event) {
		if(event.getMessage().toLowerCase().startsWith("/me")){
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "This command is unavailable!");
		}
	}
	
	public Main getMainClass() {
		return pl;
	}
}