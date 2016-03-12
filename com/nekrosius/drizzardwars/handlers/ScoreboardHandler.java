package com.nekrosius.drizzardwars.handlers;

import java.util.ArrayList;
import java.util.List;

import com.avaje.ebeaninternal.server.cluster.mcast.Message;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.nekrosius.drizzardwars.files.MessageFile;
import com.nekrosius.drizzardwars.managers.MapManager;
import com.nekrosius.drizzardwars.managers.PartyManager;
import com.nekrosius.drizzardwars.managers.TeamManager;
import com.nekrosius.drizzardwars.utils.SimpleScoreboard;
import com.nekrosius.drizzardwars.utils.WordWrap;

public class ScoreboardHandler {

	public static void updateAll() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			update(p);
		}
	}

	public static void update(Player player) {
		SimpleScoreboard sb;
		if (Game.getGameState().equals(GameState.LOBBY)) {


			sb = new SimpleScoreboard(MessageHandler.format(MessageFile.getMessage("scoreboard.lobby.header")));


//			int i = 1;
//			for (Maps map : MapManager.getMaps()) {
//				sb.add(ChatColor.RED + "" + i + ". " + ChatColor.GRAY + map.getName(), map.getVotes());
//				i++;
//			}
//			sb.add(MessageHandler.formatInteger(MessageHandler.untilStart, Game.getCountdown()), -1);
//			sb.add(ChatColor.GRAY + StringUtils.capitalize(MessageHandler.format(MessageFile.getMessage("general.points")))
//					+ ": " + ChatColor.RED + Points.getPoints(player), -2);
//			sb.add("", -3);
//			sb.add(MessageHandler.format(MessageFile.getMessage("party.scoreboard.header")), -4);
//			int j = -5;

			// Put party lines here

			org.bukkit.scoreboard.Team newTeam = sb.getScoreboard().registerNewTeam("lobby");
			newTeam.setPrefix(ChatColor.GREEN + "");
			for(Player p: Bukkit.getOnlinePlayers()){
				newTeam.addPlayer(p);
			}
		} else {
			sb = new SimpleScoreboard(MessageHandler.format(MessageFile.getMessage("scoreboard.in_game.header")));
//			if (!PlayerHandler.isSpectating(player)) {
//				Team team = TeamManager.getTeam(player);
//				if (team != null) {
//					sb.add(team.getColor() + "Kills", team.getKills());
//				}
//			}
//			for (Team team : TeamManager.getTeams()) {
//				if (team.getNexusHealth() > 0) {
//					sb.add(team.getColor() + team.getName() + " Nexus", team.getNexusHealth());
//				}
//			}
//			sb.add("", -1);
//			sb.add(MessageHandler.getPhaseMessage(Game.getPhase()), -2);
//			sb.add(MessageHandler.format(MessageFile.getMessage("time.remaining")), -3);



			for(Team t:TeamManager.getAliveTeams()){
				org.bukkit.scoreboard.Team newTeam = sb.getScoreboard().registerNewTeam(t.getCodeName());
				newTeam.setPrefix(t.getColor() + "");
				for(Player p: t.getAlivePlayers()){
					newTeam.addPlayer(p);
				}
			}
		}
		sb.build();
		sb.send(player);
	}

	public static String formatScoreboardVariables(String line,Player p) {
		line = line.replace("{until_start",getUntilStart());
		line = line.replace("{until_start}",getUntilStart());
		line = line.replace("{points}",getPlayerPoints(p));
		line = line.replace("{map_name}",MapManager.getActiveMap().getName());
		line = line.replace("{team_name}", getTeamName(p));
		line = line.replace("{team_color", getTeamColor(p));
		line = line.replace("{time_remaining}", getTimeRemaining());
		return line;
	}

	private static String getTeamColor(Player p) {
		Team t = TeamManager.getTeam(p);
		if(t!=null){
			return t.getColor() + "";
		}
		return "";
	}

	private static String getTeamName(Player p) {
		Team team = TeamManager.getTeam(p);
		if(team!=null){
			return team.getName();
		}
		return "";
	}

	private static List<String> getPartyLines(Player player) {
		List<String> partyLines = new ArrayList<>();
		if (PartyManager.hasParty(player)) {
			List<Player> players = PartyManager.getParty(player).getPlayers();
			Player leader = PartyManager.getParty(player).getLeader();

			if(players.contains(leader)){
				players.remove(leader);
			}

			partyLines.add(ChatColor.DARK_PURPLE + leader.getName());

			for(Player p:players){
				partyLines.add(ChatColor.GRAY + p.getName());
			}
		} else {
			List<StringBuilder> lines = WordWrap.wordWrap(MessageFile.formatMessage("party.scoreboard.alone"),20);

			for (StringBuilder builder : lines) {
				partyLines.add(builder.toString());
			}
		}

		return partyLines;
	}

	public static String getUntilStart(){
		return "" + Game.getCountdown();
	}

	public static String getPlayerPoints(Player p){
		return "" + Points.getPoints(p);
	}

	public static String getTimeRemaining() {
		if (Game.getPhaseTime() > 60) {
			return MessageHandler.formatInteger(MessageFile.getMessage("time.minutes"), Game.getPhaseTime() / 60);
		} else {
			return MessageHandler.formatInteger(MessageFile.getMessage("time.seconds"), Game.getPhaseTime());
		}
	}
}
