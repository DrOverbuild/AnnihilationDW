package com.nekrosius.drizzardwars.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.nekrosius.drizzardwars.Main;
import com.nekrosius.drizzardwars.files.MapFile;
import com.nekrosius.drizzardwars.managers.TeamManager;
import com.nekrosius.drizzardwars.utils.Convert;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.nekrosius.drizzardwars.files.ConfigFile;

public class Team {

	private org.bukkit.scoreboard.Team teamFromScoreboard;
	private ChatColor color;
//	private String name;
//	private String codeName;
//	private List<String> players;
	private int nexusHealth;
	private int kills;
	private Location nexusLocation;
	
	public Team(ChatColor color, String name, String codeName){
		teamFromScoreboard = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(codeName);
		if (teamFromScoreboard == null) {
			teamFromScoreboard = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(codeName);
		}
		teamFromScoreboard.setPrefix(color + "");
		setColor(color);
		setName(name);
		setPlayers(new ArrayList<String>());
		setNexusHealth(ConfigFile.config.getInt("nexus-health"));
		setKills(0);
	}
	
	public String getName() {
		return teamFromScoreboard.getDisplayName();
		//return name;
	}

	public void setName(String name) {
		teamFromScoreboard.setDisplayName(name);
		//this.name = name;
	}
	
	public List<String> getPlayersAsString() {
//		return players;
		List<String> players = teamFromScoreboard.getPlayers().stream().map(OfflinePlayer::getName).collect(Collectors.toList());
		return players;
	}

	/**
	 * Returns all players of the team that are online. If you want a list of all the players, even offline players, use
	 * Team.getPlayersAsString().
	 * @return
	 */
	public List<Player> getPlayers(){
		Set<OfflinePlayer> players = teamFromScoreboard.getPlayers();
		List<Player> onlinePlayers = new ArrayList<>();
		for(OfflinePlayer player:players){
			if(player.isOnline()){
				onlinePlayers.add(player.getPlayer());
			}
		}
		return onlinePlayers;
	}

	public void setPlayers(List<String> players) {
		for(OfflinePlayer p:teamFromScoreboard.getPlayers()){
			teamFromScoreboard.removePlayer(p);
		}

		for(String s:players){
			OfflinePlayer p = Bukkit.getOfflinePlayer(s);
			teamFromScoreboard.addPlayer(p);
		}
//		this.players = players;
	}
	
	public void addPlayer(Player p) {
//		players.add(p.getName());
		teamFromScoreboard.addPlayer(p);
	}
	
	public void removePlayer(Player p) {
//		players.remove(p.getName());
		teamFromScoreboard.removePlayer(p);
	}

	public ChatColor getColor() {
		return color;
	}

	/**
	 * Sets the color of the team, and also changes the color of teamFromScoreboard
	 * @param color The team's color or null if there is no color.
	 */
	public void setColor(ChatColor color) {
		this.color = color;
		if(color != null) {
			teamFromScoreboard.setPrefix(color + "");
		}else{
			teamFromScoreboard.setPrefix("");
		}
	}

	public int getNexusHealth() {
		return nexusHealth;
	}

	public void setNexusHealth(int nexusHealth) {
		this.nexusHealth = nexusHealth;
	}
	
	public void reduceNexusHealth(int amount) {
		this.nexusHealth -= amount;
	}

	public int getKills() {
		return kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}
	
	public void addKill(){
		this.kills++;
	}

	public Location getNexusLocation() {
		return nexusLocation;
	}

	public void setNexusLocation(Location nexusLocation) {
		this.nexusLocation = nexusLocation;
	}

	public String getCodeName() {
		return teamFromScoreboard.getName();
		//return codeName;
	}

//	public void setCodeName(String codeName) {
//		this.codeName = codeName;
//	}

	public Location getSpawnpoint(){
		return Convert.StringToLocation(MapFile.config.getString("team." + this.getCodeName() + ".spawnpoint"), true, true);
	}

	public Location getSpectatorSpawnpoint(){
		String spectatorSpawnStr = MapFile.config.getString("team." + this.getCodeName() + ".spectatorspawnpoint");
		if(spectatorSpawnStr != null){
			return Convert.StringToLocation(spectatorSpawnStr,true,true);
		}else {
			return this.getSpawnpoint();
		}
	}

}