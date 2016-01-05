package com.nekrosius.drizzardwars.handlers.mapsetup;

import java.util.*;

import com.nekrosius.drizzardwars.files.MessageFile;
import com.nekrosius.drizzardwars.handlers.MessageHandler;
import com.nekrosius.drizzardwars.handlers.Team;
import com.nekrosius.drizzardwars.utils.WordWrap;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.nekrosius.drizzardwars.managers.TeamManager;

public class Phase {

	static Map<Player, Integer> phaseTimeStatus = new HashMap<Player, Integer>();
	
	public static int getPhaseTimeStatus(Player player) {
		if(phaseTimeStatus.get(player) != null) return phaseTimeStatus.get(player);
		return 0;
	}

	public static void setPhaseTimeStatus(Player player, Integer status) {
		phaseTimeStatus.put(player, status);
	}
	
	public static void sendPhaseMessage(Player player, int phase) {
		Team team = TeamManager.getTeam(player);
		ChatColor r = null;
		if(team == null){
			r = ChatColor.BLACK;
		}else{
			r = team.getColor();
		}
		ChatColor g = ChatColor.WHITE;

		List<String> messages = new ArrayList<>();
		if(phase == 1){
			messages.add("▒▒▒▒▒▒▒▒▒▒ ");
			messages.add("▒▒▒▒" + r + "▒▒" + g + "▒▒▒▒ ");
			messages.add("▒▒" + r + "▒▒▒▒" + g + "▒▒▒▒ ");
			messages.add("▒▒▒▒" + r + "▒▒" + g + "▒▒▒▒ ");
			messages.add("▒▒▒▒" + r + "▒▒" + g + "▒▒▒▒ ");
			messages.add("▒▒▒▒" + r + "▒▒" + g + "▒▒▒▒ ");
			messages.add("▒▒▒▒" + r + "▒▒" + g + "▒▒▒▒ ");
			messages.add("▒▒▒▒" + r + "▒▒" + g + "▒▒▒▒ ");
			messages.add("▒▒" + r + "▒▒▒▒▒▒" + g + "▒▒ ");
			messages.add("▒▒▒▒▒▒▒▒▒▒");
		}
		else if(phase == 2){
			messages.add("▒▒▒▒▒▒▒▒▒▒ ");
			messages.add("▒▒▒" + r + "▒▒▒▒" + g + "▒▒▒");
			messages.add("▒▒" + r + "▒" + g + "▒▒▒▒" + r + "▒" + g + "▒▒ ");
			messages.add("▒▒▒▒▒▒▒" + r + "▒" + g + "▒▒ ");
			messages.add("▒▒▒▒▒▒" + r + "▒" + g + "▒▒▒ ");
			messages.add("▒▒▒▒▒" + r + "▒" + g + "▒▒▒▒ ");
			messages.add("▒▒▒▒" + r + "▒" + g + "▒▒▒▒▒ ");
			messages.add("▒▒▒" + r + "▒" + g + "▒▒▒▒▒▒ ");
			messages.add("▒▒" + r + "▒▒▒▒▒▒" + g + "▒▒ ");
			messages.add("▒▒▒▒▒▒▒▒▒▒ ");
		}
		else if(phase == 3){
			messages.add("▒▒▒▒▒▒▒▒▒▒ ");
			messages.add("▒▒▒" + r + "▒▒▒▒" + g + "▒▒▒ ");
			messages.add("▒▒" + r + "▒" + g + "▒▒▒▒" + r + "▒" + g + "▒▒ ");
			messages.add("▒▒▒▒▒▒▒" + r + "▒" + g + "▒▒ ");
			messages.add("▒▒▒▒" + r + "▒▒▒" + g + "▒▒▒ ");
			messages.add("▒▒▒▒▒▒▒" + r + "▒" + g + "▒▒ ");
			messages.add("▒▒▒▒▒▒▒" + r + "▒" + g + "▒▒ ");
			messages.add("▒▒" + r + "▒" + g + "▒▒▒▒" + r + "▒" + g + "▒▒ ");
			messages.add("▒▒▒" + r + "▒▒▒▒" + g + "▒▒▒ ");
			messages.add("▒▒▒▒▒▒▒▒▒▒ ");
			Blocks.spawnDiamonds();
		}else if(phase == 4){
			messages.add("▒▒▒▒▒▒▒▒▒▒ ");
			messages.add("▒▒▒▒▒▒" + r + "▒" + g + "▒▒▒ ");
			messages.add("▒▒▒▒▒" + r + "▒▒" + g + "▒▒▒ ");
			messages.add("▒▒▒▒" + r + "▒" + g + "▒" + r + "▒" + g + "▒▒▒ ");
			messages.add("▒▒▒" + r + "▒" + g + "▒▒" + r + "▒" + g + "▒▒▒ ");
			messages.add("▒▒" + r + "▒" + g + "▒▒▒" + r + "▒" + g + "▒▒▒ ");
			messages.add("▒▒" + r + "▒▒▒▒▒▒" + g + "▒▒ ");
			messages.add("▒▒▒▒▒▒" + r + "▒" + g + "▒▒▒ ");
			messages.add("▒▒▒▒▒▒" + r + "▒" + g + "▒▒▒ ");
			messages.add("▒▒▒▒▒▒▒▒▒▒ ");
		}else if(phase == 5){
			messages.add("▒▒▒▒▒▒▒▒▒▒ ");
			messages.add("▒▒" + r + "▒▒▒▒▒▒" + g + "▒▒ ");
			messages.add("▒▒" + r + "▒" + g +"▒▒▒▒▒▒▒ ");
			messages.add("▒▒" + r + "▒" + g +"▒▒▒▒▒▒▒ ");
			messages.add("▒▒" + r + "▒▒▒▒▒" + g + "▒▒▒ ");
			messages.add("▒▒▒▒▒▒▒" + r + "▒" + g + "▒▒ ");
			messages.add("▒▒" + r + "▒" + g +"▒▒▒▒" + r + "▒" + g + "▒▒ ");
			messages.add("▒▒" + r + "▒" + g +"▒▒▒▒" + r + "▒" + g + "▒▒ ");
			messages.add("▒▒▒" + r + "▒▒▒▒" + g + "▒▒▒ ");
			messages.add("▒▒▒▒▒▒▒▒▒▒ ");
		}

		if(messages.size()>0) {
			List<StringBuilder> descriptionLines = WordWrap.wordWrap(MessageFile.formatMessage("phases.descriptions." + phase), 40);

			final int middleOfLeftColumn = messages.size() / 2;
			final int middleOfRightColumn = descriptionLines.size() / 2;
			int j = middleOfRightColumn;

			for (int i = middleOfLeftColumn; i >= 0; i--){
				if(j>=0){
					messages.set(i,messages.get(i) + r + descriptionLines.get(j).toString());
					j--;
				}
			}

			j = middleOfRightColumn+1;

			for(int i = middleOfLeftColumn; i<messages.size();i++){
				if(j<descriptionLines.size()){
					messages.set(i,messages.get(i) + r + descriptionLines.get(j).toString());
					j++;
				}
			}

			player.sendMessage(messages.toArray(new String[messages.size()]));
		}
	}
}