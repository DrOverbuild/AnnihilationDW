package com.nekrosius.drizzardwars.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.nekrosius.drizzardwars.Main;
import com.nekrosius.drizzardwars.files.MessageFile;
import com.nekrosius.drizzardwars.handlers.Game;
import com.nekrosius.drizzardwars.handlers.MessageHandler;
import com.nekrosius.drizzardwars.handlers.PlayerHandler;
import com.nekrosius.drizzardwars.handlers.ScoreboardHandler;
import com.nekrosius.drizzardwars.managers.MapManager;

public class VoteCommand implements CommandExecutor {
	
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
		if(args.length == 1) {
			int map;
			try{
				map = Integer.parseInt(args[0]);
			}catch(NumberFormatException e){
				MessageHandler.sendMessage(player, MessageFile.getMessage("vote.invalid-usage"));
				return true;
			}
			// Count the vote
			if(map > 0 && map <= MapManager.amountOfMaps()){
				if(!MapManager.getVotableMaps().contains(MapManager.getMap(map))){
					String str = MessageHandler.formatString(MessageFile.getMessage("vote.map-unavailable"),MapManager.getMap(map).getName());
					MessageHandler.sendMessage(player,str);
					return true;
				}
				PlayerHandler.setPlayerVote(player, map);
				String str = MessageFile.getMessage("vote.successful");
				str = MessageHandler.formatString(str, MapManager.getMap(map).getName());
				MessageHandler.sendMessage(player, str);
				for(Player p : Bukkit.getOnlinePlayers()){
					ScoreboardHandler.update(p);
				}
			}else{
				MessageHandler.sendMessage(player, MessageFile.getMessage("vote.invalid-usage"));
				return true;
			}
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
}