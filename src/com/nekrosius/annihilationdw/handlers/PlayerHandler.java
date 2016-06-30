package com.nekrosius.annihilationdw.handlers;

import java.util.*;
import java.util.Map;

import com.nekrosius.annihilationdw.files.MessageFile;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.nekrosius.annihilationdw.abilities.Ability;
import com.nekrosius.annihilationdw.files.MapFile;
import com.nekrosius.annihilationdw.managers.BarManager;
import com.nekrosius.annihilationdw.managers.MapManager;
import com.nekrosius.annihilationdw.managers.TeamManager;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerHandler {

	static Map<String, Integer> vote = new HashMap<String, Integer>();
	static Map<String, Boolean> playing = new HashMap<String, Boolean>();
	static Map<String, String > kit = new HashMap<String, String>();
	static Map<String, Integer> compassStatus = new HashMap<String, Integer>();
	static Map<String, Boolean> spectating = new HashMap<String, Boolean>();
	static Map<String, Integer> gold = new HashMap<String, Integer>();
	static Map<String, List<Ability>> abilities = new HashMap<String, List<Ability>>();
	static List<String> hidden = new ArrayList<String>();
	
	public static void quit(Player player) {
		vote.remove(player.getName());
		compassStatus.remove(player.getName());
		clearAbilities(player);
		boolean isHidden = isPlayerHidden(player);
		for(Player p : Bukkit.getOnlinePlayers()){
			if(isPlayerHidden(p)){
				player.showPlayer(p);
			}
			if(isHidden){
				p.showPlayer(player);
			}
		}

		BarManager.removeBar(player);
		//Game.hasPlayers(player);
	}

	public static void setTeamOfPlayer(Player player, Team team) {
		TeamManager.setPlayerTeam(player, team);
	}
	
	public static void setPlayerVote(Player player, Integer map) {

		if(map == null){
			if(getPlayerVote(player) != null){
				MapManager.getMap(getPlayerVote(player)).removeVote();
			}
		}
		if(map != null){
			if(getPlayerVote(player) != null){
				if(getPlayerVote(player) != 0)
					MapManager.getMap(getPlayerVote(player)).removeVote();
			}
			MapManager.getMap(map).addVote();
		}
		vote.put(player.getName(), map);

		MapManager.getVotableMaps().sort(new Comparator<GameMap>() {
			@Override
			public int compare(GameMap o1, GameMap o2) {
				if(o1.getVotes() > o2.getVotes()) return -1;
				if(o1.getVotes() < o2.getVotes()) return 1;
				return 0;
			}
		});
	}
	
	public static Integer getPlayerVote(Player player){
		if(vote.get(player.getName()) != null) return vote.get(player.getName());
		return 0;
	}

	public static Boolean isPlayerPlaying(Player player) {
//		if(playerPlaying.get(player.getName()) != null) return playerPlaying.get(player.getName());
//		return false;
		return TeamManager.hasTeam(player);
	}

	public static void clearPlayersPlayingMap(){
		String[] strings = playing.keySet().toArray(new String[]{});
		for(String s:strings){
			playing.remove(s);
		}
	}

	public static void setPlayerPlaying(Player player, Boolean status) {
		playing.put(player.getName(), status);
	}
	
	public static void setPlayerKit(Player player, String name) {
		kit.put(player.getName(), name);
	}
	
	public static String getPlayerKit(Player player) {
		return kit.get(player.getName());
	}
	
	public static int getCompassStatus(Player player) {
		if(compassStatus.get(player.getName()) != null){
			return compassStatus.get(player.getName());
		}
		return 0;
	}
	
	public static void setCompassStatus(Player player, int status) {
		compassStatus.put(player.getName(), status);
	}

	public static int nextCompassStatus(Player player) {
		int currentStatus = getCompassStatus(player);
		int nextStatus = currentStatus + 1;
		List<Team> aliveTeams = TeamManager.getAliveTeams();
		while(!aliveTeams.contains(TeamManager.getTeam(nextStatus))){
			nextStatus++;
			if(nextStatus >= TeamManager.getTeams().size()) nextStatus = 0;
			if(nextStatus == currentStatus) break;
		}
		return nextStatus;
	}
	
	public static boolean isSpectating(Player player){
		if(spectating.get(player.getName()) != null) return spectating.get(player.getName());
		return false;
	}
	
	public static void setSpectating(Player player, boolean bool){
		if (bool){
			player.getInventory().clear();
			MapFile.createConfig("plugins/AnnihilationDW/Maps/" + MapManager.getActiveMap().getName());
			Location loc;
			Team t = TeamManager.getTeam(player);
			if(t != null){
				loc = t.getSpectatorSpawnpoint();
			}else {
				loc = TeamManager.getTeam(0).getSpectatorSpawnpoint();
			}
			player.teleport(loc);
			//player.getInventory().addItem(ItemStackGenerator.createItem(Material.COMPASS, 0, 0, MessageHandler.format(MessageFile.getMessage("compass.default")), null));
			player.setGameMode(GameMode.SURVIVAL);
			player.setAllowFlight(true);
			player.setFlying(true);
			PlayerHandler.hidePlayer(player);
		}else{
			player.setGameMode(GameMode.SURVIVAL);
			player.setLevel(0);
			player.setFlying(false);
			player.setFallDistance(0f);
			player.setAllowFlight(false);
			PlayerHandler.unhidePlayer(player);
		}
		spectating.put(player.getName(), bool);
	}
	
	public static List<String> getHiddenPlayers() {
		return hidden;
	}
	
	public static boolean isPlayerHidden(Player player) {
		return hidden.contains(player.getName());
	}
	
	public static void hidePlayer(Player player){
		hidden.add(player.getName());
		for(Player p : Bukkit.getOnlinePlayers()){
			p.hidePlayer(player);
		}
	}
	
	public static void unhidePlayer(Player player){
		hidden.remove(player.getName());
		for(Player p : Bukkit.getOnlinePlayers()){
			p.showPlayer(player);
		}
	}
	
	public static void hideHiddenPlayers(Player player) {
		if(player == null) return;
		for(String p : getHiddenPlayers()) {
			if(Bukkit.getPlayer(p) != null) {
				player.hidePlayer(Bukkit.getPlayer(p));
			}
		}
	}
	
	public static int amountOfGold(Player player){
		int amount = 0;
		for(ItemStack item : player.getInventory().getContents()){
			if(item != null)
				if(item.getType().equals(Material.GOLD_INGOT)){
					amount += item.getAmount();
				}
		}
		return amount;
	}

	public static void teamNexusIsDestroyed(Player player){
		if(player.hasPermission("dw.spectator")){
			PlayerHandler.setSpectating(player, true);
			Lobby.setupLobby(player);
			return;
		}else {
			PlayerHandler.quit(player);
			//PlayerHandler.setTeamOfPlayer(player, null);
		}
	}

	public static void setPlayerGold(Player player, Integer amount){
		if(gold == null){
			gold.remove(player);
		}
		else{
			gold.put(player.getName(), amount);
		}
	}

	public static Integer getPlayerGold(Player player) {
		return gold.get(player.getName());
	}

	public static void clearPlayerGold(){
		gold.clear();
	}

	public static ItemStack getKillReward(){
		ItemStack is = new ItemStack(Material.GOLD_INGOT, 3);
		if(MessageFile.getMessage("currency.title") != null && MessageFile.getMessage("currency.lore") != null) {
			ItemMeta isMeta = is.getItemMeta();
			isMeta.setDisplayName(MessageFile.formatMessage("currency.title"));
			isMeta.setLore(MessageHandler.formatList(MessageFile.getMessageList("currency.lore")));
			is.setItemMeta(isMeta);
		}
		return is;
	}
	
	public static void addAbility(Player player, Ability ab) {
		List<Ability> abs = getAbilities(player);
		abs.add(ab);
		abilities.put(player.getName(), abs);
		ab.initialize(player);
	}
	
	public static void setAbilities(Player player, List<Ability> ab) {
		abilities.put(player.getName(), ab);
		for(Ability ability : ab){
			ability.initialize(player);
		}
	}
	
	public static boolean hasAbilities(Player player) {
		return abilities.get(player.getName()).isEmpty();
	}
	
	public static List<Ability> getAbilities(Player player) {
		return abilities.get(player.getName());
	}

	public static void clearAbilities(Player player) {
		for(Ability ability : getAbilities(player)){
			ability.cleanup(player);
		}
		List<Ability> emptyAbilities= new ArrayList<>();
		setAbilities(player, emptyAbilities);
	}
}