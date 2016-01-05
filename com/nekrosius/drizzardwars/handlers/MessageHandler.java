package com.nekrosius.drizzardwars.handlers;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.nekrosius.drizzardwars.files.MessageFile;
import com.nekrosius.drizzardwars.managers.TeamManager;

public class MessageHandler {
	
	public static String all;
	public static String team;
	public static String phase1;
	public static String phase2;
	public static String phase3;
	public static String phase4;
	public static String phase5;
	public static String untilStart;
	public static String nexusDmg;
	
	public static void sendMessage(Player player, String str) {
		if(str == null) return;
		player.sendMessage(format(str));
	}

	public static void sendMessage(Player player, List<String> str) {
		for(String st : str)
		{
			player.sendMessage(format(st));
		}
	}
	
	public static String format(String str) {
		if(str == null) return "";
		str = ChatColor.translateAlternateColorCodes('&', str);
		return str;
	}
	
	public static String formatPlayer(String str, Player player) {
		if(str == null) return "";
		if(player != null) str = str.replaceAll("%p", player.getName());
		return ChatColor.translateAlternateColorCodes('&', str);
	}
	
	public static String formatInteger(String str, int number) {
		if(str == null) return "";
		if(number != -1) str = str.replaceAll("%t", number + "");
		return  ChatColor.translateAlternateColorCodes('&', str);
	}
	
	public static String formatString(String str, String replacement) {
		if(str == null) return "";
		if(replacement != null) str = str.replace("%s", replacement);
		return ChatColor.translateAlternateColorCodes('&', str);
	}
	
	public static String formatPlayer(String str, Player victim, Player killer){
		if(str == null) return "";
		if(victim != null) str = str.replaceAll("%v", TeamManager.getTeam(victim).getColor() + victim.getName());
		if(killer != null) str = str.replaceAll("%p", TeamManager.getTeam(killer).getColor() + killer.getName());
		return ChatColor.translateAlternateColorCodes('&', str);
	}
	
	public static void loadMessages() {
		all = format(MessageFile.getMessage("general.all"));
		team = format(MessageFile.getMessage("general.team"));
		phase1 = format(MessageFile.getMessage("phases.1"));
		phase2 = format(MessageFile.getMessage("phases.2"));
		phase3 = format(MessageFile.getMessage("phases.3"));
		phase4 = format(MessageFile.getMessage("phases.4"));
		phase5 = format(MessageFile.getMessage("phases.5"));
		untilStart = MessageFile.getMessage("scoreboard.until-start");
		nexusDmg = format(MessageFile.getMessage("game.nexus-broken"));
	}
	
	public static String getPhaseMessage(int phase){
		if(phase == 1){
			return phase1;
		}else if(phase == 2) {
			return phase2;
		}else if(phase == 3) {
			return phase3;
		}else if(phase == 4) {
			return phase4;
		}else{
			return phase5;
		}
	}
}