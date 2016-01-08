package com.nekrosius.drizzardwars.handlers;

import com.nekrosius.drizzardwars.files.MessageFile;
import com.nekrosius.drizzardwars.managers.PartyManager;
import com.nekrosius.drizzardwars.utils.WordWrap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.nekrosius.drizzardwars.managers.MapManager;
import com.nekrosius.drizzardwars.managers.TeamManager;
import com.nekrosius.drizzardwars.utils.SimpleScoreboard;

import java.util.List;

public class ScoreboardHandler {


	public static void updateAll() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			update(p);
		}
	}

	public static void update(Player player) {
		SimpleScoreboard sb;
		if (Game.getGameState().equals(GameState.LOBBY)) {
			sb = new SimpleScoreboard(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Drizzard Wars");
			int i = 1;
			for (Maps map : MapManager.getMaps()) {
				sb.add(ChatColor.RED + "" + i + ". " + ChatColor.GRAY + map.getName(), map.getVotes());
				i++;
			}
			sb.add(MessageHandler.formatInteger(MessageHandler.untilStart, Game.getCountdown()), -1);
			sb.add(ChatColor.GRAY + "Puntos: " + ChatColor.RED + "" + Points.getPoints(player), -2);
			sb.add("", -3);
			sb.add("PARTY", -4);
			int j = -5;
			if (PartyManager.hasParty(player)) {
				List<Player> players = PartyManager.getParty(player).getPlayers();
				Player leader = PartyManager.getParty(player).getLeader();

				if(players.contains(leader)){
					players.remove(leader);
				}

				sb.add(ChatColor.DARK_PURPLE + leader.getName(),j);
				j--;

				for(Player p:players){
					sb.add(ChatColor.GRAY + p.getName(), j);
					j--;
				}
			} else {
				List<StringBuilder> lines = WordWrap.wordWrap(MessageFile.formatMessage("party.alone"),20);

				for (StringBuilder builder : lines) {
					sb.add(builder.toString(), j);
					j--;

				}
			}

			org.bukkit.scoreboard.Team newTeam = sb.getScoreboard().registerNewTeam("lobby");
			newTeam.setPrefix(ChatColor.GREEN + "");
			for(Player p: Bukkit.getOnlinePlayers()){
				newTeam.addPlayer(p);
			}
		} else {
			sb = new SimpleScoreboard(ChatColor.GOLD + "" + ChatColor.BOLD + MapManager.getActiveMap().getName());
			if (!PlayerHandler.isSpectating(player)) {
				Team team = TeamManager.getTeam(player);
				if (team != null) {
					sb.add(team.getColor() + "Kills", team.getKills());
				}
			}
			for (Team team : TeamManager.getTeams()) {
				if (team.getNexusHealth() > 0) {
					sb.add(team.getColor() + team.getName() + " Nexus", team.getNexusHealth());
				}
			}
			sb.add("", -1);
			sb.add(MessageHandler.getPhaseMessage(Game.getPhase()), -2);
			sb.add(MessageHandler.format(MessageFile.getMessage("time.remaining")), -3);

			if (Game.getPhaseTime() > 60) {
				sb.add(MessageHandler.formatInteger(MessageFile.getMessage("time.minutes"), Game.getPhaseTime() / 60), -4);
			} else {
				sb.add(MessageHandler.formatInteger(MessageFile.getMessage("time.seconds"), Game.getPhaseTime()), -4);
			}

			for(Team t:TeamManager.getAliveTeams()){
				org.bukkit.scoreboard.Team newTeam = sb.getScoreboard().registerNewTeam(t.getCodeName());
				newTeam.setPrefix(t.getColor() + "");
				for(Player p: t.getAlivePlayers()){
					newTeam.addPlayer(p);
				}
			}
		}

//		for (org.bukkit.scoreboard.Team t : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) {
//			org.bukkit.scoreboard.Team newTeam = sb.getScoreboard().registerNewTeam(t.getName());
//			newTeam.setDisplayName(t.getDisplayName());
//			newTeam.setPrefix(t.getPrefix());
//			for (OfflinePlayer p : t.getAlivePlayers()) {
//				newTeam.addPlayer(p);
//			}
//		}

		sb.build();
		sb.send(player);
	}
}
