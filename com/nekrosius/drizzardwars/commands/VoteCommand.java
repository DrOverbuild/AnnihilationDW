package com.nekrosius.drizzardwars.commands;

import com.nekrosius.drizzardwars.handlers.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.nekrosius.drizzardwars.Main;
import com.nekrosius.drizzardwars.files.MessageFile;
import com.nekrosius.drizzardwars.managers.MapManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class VoteCommand implements CommandExecutor, TabCompleter{
	
	private Main pl;
	public VoteCommand(Main plugin){
		pl = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage(MessageFile.getMessage("commands.not-player"));
			return true;
		}
		Player player = (Player) sender;
		if(Game.isGameStarted()){
			MessageHandler.sendMessage(player, MessageFile.getMessage("vote.started"));
			return true;
		}
		if(!Game.isCanVote()){
			MessageHandler.sendMessage(player, MessageFile.getMessage("vote.cant-vote"));
			return true;
		}
		if(args.length >= 1) {
			GameMap map;

			StringBuilder mapName = new StringBuilder(args[0]);
			if(args.length > 1) {
				String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
				for (String arg : newArgs) {
					mapName.append(" ").append(arg);
				}
			}

			map = MapManager.getMapOfName(mapName.toString());

			if(map == null){
				MessageHandler.sendMessage(player, MessageHandler.formatString(MessageFile.getMessage("vote.map-not-found"),mapName.toString()));
				return true;
			}

			if(!MapManager.getVotableMaps().contains(map)){
				String str = MessageHandler.formatString(MessageFile.getMessage("vote.map-unavailable"),map.getName());
				MessageHandler.sendMessage(player,str);
				return true;
			}

			PlayerHandler.setPlayerVote(player, map.getId());
			String str = MessageFile.getMessage("vote.successful");
			str = MessageHandler.formatString(str, map.getName());
			MessageHandler.sendMessage(player, str);
			ScoreboardHandler.updateAll();
		}else{
			MessageHandler.sendMessage(player, MessageFile.getMessage("vote.invalid-usage"));
			return true;
		}
		
		return true;
	}

	public Main getMainClass()
	{
		return pl;
	}

	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command cmd, String s, String[] args) {
		if(args.length == 1){
			List<String> completions = new ArrayList<>();
			for(GameMap map: MapManager.getVotableMaps()){
				if(map.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
					completions.add(map.getName());
				}
			}
			Collections.sort(completions);
			return completions;
		}
		return null;
	}
}