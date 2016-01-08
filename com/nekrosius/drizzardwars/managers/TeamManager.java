package com.nekrosius.drizzardwars.managers;

import java.util.ArrayList;
import java.util.List;

import com.nekrosius.drizzardwars.files.MessageFile;
import com.nekrosius.drizzardwars.handlers.MessageHandler;
import com.nekrosius.drizzardwars.handlers.ScoreboardHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.nekrosius.drizzardwars.Main;
import com.nekrosius.drizzardwars.files.ConfigFile;
import com.nekrosius.drizzardwars.files.TeamsFile;
import com.nekrosius.drizzardwars.handlers.Game;
import com.nekrosius.drizzardwars.handlers.Team;
import com.nekrosius.drizzardwars.utils.Convert;

public class TeamManager {

	private Main pl;
	
	private static List<Team> teams = new ArrayList<Team>();
	
	public TeamManager(Main plugin, int amount)
	{
		teams = new ArrayList<Team>();
		pl = plugin;
		int createdTeams = 0;
		for(String path : TeamsFile.getTeams()){
			if(createdTeams < amount){
				String codeName = path;
				String name = TeamsFile.config.getString(path + ".name");
				ChatColor color = Convert.StringToChatColor(TeamsFile.config.getString(path + ".color"));
				addTeam(new Team(color, name, codeName));
				createdTeams++;
			}else break;
		}
		/*
		new BukkitRunnable(){
			@Override
			public void run() {
				System.out.println(TeamManager.hasTeam("BenRush"));
			}
		}.runTaskTimer(this, 20L, 20L);
		// TODO remove
		*/
	}
	
	public Main getMainPlugin()
	{
		return pl;
	}
	
	public static boolean isFull(Team team) {
		if(team.getAllPlayers().size() >= ConfigFile.config.getInt("team-size")){
			return true;
		}else{
			for(Team team2 : getTeams()){
				if(team2.getAllPlayers().size() + 1 >= team.getAllPlayers().size()) return false;
			}
		}
		return true;
	}
	
	public static Team getMostKills(){
		int max = -1;
		Team mostKills = null;
		for(Team team : getTeams()) {
			if(team.getKills() > max && team.getNexusHealth() > 0 && team.getAllPlayers().size() > 0){
				max = team.getKills();
				mostKills = team;
			}
		}
		return mostKills;
	}
	
	public static Team getTeamToJoin(){
		int min = 999999;
		Team toJoin = null;
		for(Team team : getTeams()) {
			if(team.getAllPlayers().size() < min && team.getNexusHealth() > 0){
				min = team.getAllPlayers().size();
				toJoin = team;
			}
		}
		if(toJoin == null) return getTeam(0);
		return toJoin;
	}
	
	public static List<Team> getTeams() {
		return teams;
	}
	
	public static Team getTeam(int index) {
		if(getTeams().size() <= index) return null;
		return teams.get(index);
	}
	
	public static void addTeam(Team team) {
		teams.add(team);
	}
	
	public static void removeTeam(Team team) {
		teams.remove(team);
	}
	
//	public static boolean hasTeam(Player player) {
//		for(Team team:getTeams()){
//			for(String pName:team.getAllPlayers()){
//				if (player.getName().equals(pName)) return true;
//			}
//		}
//
//		return false;
//	}

	public static boolean hasTeam(OfflinePlayer player){
		for(Team team:getTeams()){
			for(String p:team.getAllPlayers()){
				if(player.getName().equals(p)) return true;
			}
		}

		return false;
	}
	
	public static boolean allTeamsFull() {
		int max = ConfigFile.config.getInt("team-size");
		for(Team team : getTeams()) {
			if(team.getAllPlayers().size() < max) return false;
		}
		return true;
	}
	
	public static void hasWinner(){
		int aliveTeams = 0;
		for(Team team : getTeams()){
			if(team.getNexusHealth() > 0) ++aliveTeams;
		}
		if(aliveTeams == 1){
			Game.finish(getMostKills());
		}
	}
	
	public static Team getTeam(OfflinePlayer player) {
		if(!Game.isGameStarted()){
			return null;
		}

		for(Team t:getTeams()) {
			for (String s : t.getAllPlayers()) {
				if (s.equals(player.getName())) return t;
			}
		}

		return null;
	}

//	public static Team getTeam(Player player) {
//		if(!Game.isGameStarted()){
//			return null;
//		}
//
//		for(Team t:getTeams()) {
//			for (String s : t.getAllPlayers()) {
//				if (s.equals(player.getName())) return t;
//			}
//		}
//
//		return null;
//	}
	
	public static void setPlayerTeam(Player player, Team newTeam) {
		if(player == null) return;

		Team team = getTeam(player);

		if(newTeam == null){
			if(team != null){
				team.removePlayer(player);
			}
			return;
		}

		if(team != null){
			if(!team.equals(newTeam)){
				team.removePlayer(player);
				newTeam.addPlayer(player);
			}
		}else{
			newTeam.addPlayer(player);
		}
	}

	/**
	 * Returns a list of all the teams whose nexus health is greater than zero and has players that are currently
	 * online. If a team has players, but none of them are currently online, then that team is not included in the
	 * returned list.
	 * @return
	 */
	public static List<Team> getAliveTeams(){
		List<Team> teamsAlive = new ArrayList<>();
		for(Player p : Bukkit.getOnlinePlayers()){
			Team team = TeamManager.getTeam(p);
			if(team != null){
				if(!teamsAlive.contains(team) && team.getNexusHealth() > 0){
					teamsAlive.add(team);
				}
			}
		}
		return teamsAlive;
	}

	public static void destroyTeam(Team team){
		if(team == null)return;
		String msg = MessageHandler.formatString(MessageFile.getMessage("team.destroyed"), team.getColor() + team.getName());
		for(Player p : Bukkit.getOnlinePlayers()){
			MessageHandler.sendMessage(p, msg);
		}
		team.getNexusLocation().getWorld().getBlockAt(team.getNexusLocation()).setType(Material.AIR);
		if(team.getNexusHealth() > 0){
			team.setNexusHealth(0);
		}
		Player[] players = team.getAlivePlayers().toArray(new Player[]{});
		for(Player p : players){
			p.setHealth(0d);
		}
//		TeamManager.removeTeam(team);
		ScoreboardHandler.updateAll();
		TeamManager.hasWinner();
	}
}