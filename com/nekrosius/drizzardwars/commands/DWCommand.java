package com.nekrosius.drizzardwars.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.nekrosius.drizzardwars.Main;
import com.nekrosius.drizzardwars.files.MapFile;
import com.nekrosius.drizzardwars.files.MessageFile;
import com.nekrosius.drizzardwars.handlers.MessageHandler;
import com.nekrosius.drizzardwars.inventories.AdminMenu;
import com.nekrosius.drizzardwars.inventories.MapsSetupMenu;
import com.nekrosius.drizzardwars.managers.MapManager;
import com.nekrosius.drizzardwars.managers.TeamManager;

public class DWCommand implements CommandExecutor{
	
	private Main pl;
	public DWCommand(Main plugin){
		pl = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {		
		if(args.length == 0){
			if(!(sender instanceof Player)){
				sender.sendMessage(MessageFile.getMessage("commands.not-player"));
				return true;
			}
			Player player = (Player) sender;
			if(!player.isOp()){
				MessageHandler.sendMessage(player, MessageFile.getMessage("commands.not-op"));
				return true;
			}
			if(player.getWorld().getName().startsWith("plugins/DrizzardWars/Maps")){
				new TeamManager(pl, MapFile.config.getInt("team.amount"));
				MapsSetupMenu.setupMapMenu(player, MapManager.getMap(player.getWorld().getName()));
			}else{
				//new TeamManager(pl, MapFile.config.getInt("team.amount"));
				AdminMenu.setup(player);
			}
			return true;
		}else{
			if(!(sender instanceof Player)){
				sender.sendMessage(MessageFile.getMessage("commands.not-player"));
				return true;
			}
			if(args.length == 1){
				if(args[0].equalsIgnoreCase("visible")){
					for(Player p : Bukkit.getOnlinePlayers()){
						for(Player t : Bukkit.getOnlinePlayers()){
							p.showPlayer(t);
						}
					}
				}
			}
		}
		return true;
	}

	
	public Main getMainClass()
	{
		return pl;
	}
}