package com.nekrosius.drizzardwars.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.nekrosius.drizzardwars.Main;
import com.nekrosius.drizzardwars.files.MessageFile;
import com.nekrosius.drizzardwars.handlers.MessageHandler;
import com.nekrosius.drizzardwars.handlers.Points;
import com.nekrosius.drizzardwars.handlers.ScoreboardHandler;

public class PointsCommand implements CommandExecutor {
	
	private Main pl;
	public PointsCommand(Main plugin) {
		pl = plugin;
	}

	ChatColor c1 = ChatColor.RED;
	ChatColor c2 = ChatColor.GRAY;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(!player.isOp()){
				MessageHandler.sendMessage(player, MessageHandler.formatInteger(MessageFile.getMessage("commands.points"), Points.getPoints(player)));
				return true;
			}
			if(args.length == 0){
				MessageHandler.sendMessage(player, MessageHandler.formatInteger(MessageFile.getMessage("commands.points"), Points.getPoints(player)));
				player.sendMessage(c1 + "--- Commands ---");
				player.sendMessage(c1 + "/" + cmdLabel + " add <playerName> <amount> " + c2 + 
						"adds specified amount of points to player's balance!");
				player.sendMessage(c1 + "/" + cmdLabel + " withdraw <playerName> <amount> " + c2 + 
						"removes specified amount of points from player's balance!");
				player.sendMessage(c1 + "/" + cmdLabel + " set <playerName> <amount> " + c2 + 
						"sets speciified amount of points as player's balance!");
				return true;
			} else if(args.length == 3){
				if(Bukkit.getPlayer(args[1]) == null){
					MessageHandler.sendMessage(player, MessageFile.getMessage("party.offline"));
					return true;	
				}
				Player target = Bukkit.getPlayer(args[1]);
				if(args[0].equalsIgnoreCase("add")){
					int amount;
					try{
						amount = Integer.parseInt(args[2]);
					}catch(NumberFormatException e){
						player.sendMessage(ChatColor.RED + "Please type number!");
						return true;
					}
					Points.addPoints(target, amount);
					player.sendMessage(c2 + "You've added " + c1 + "" + amount + c2 + " points for " + c1 + target.getName());
				}
				else if(args[0].equalsIgnoreCase("set")) {
					int amount;
					try{
						amount = Integer.parseInt(args[2]);
					}catch(NumberFormatException e){
						player.sendMessage(ChatColor.RED + "Please type number!");
						return true;
					}
					Points.setPoints(target, amount);
					player.sendMessage(c2 + "You've set " + c1 + "" + amount + c2 + " points for " + c1 + target.getName());
				}
				else if(args[0].equalsIgnoreCase("withdraw")) {
					int amount;
					try{
						amount = Integer.parseInt(args[2]);
					}catch(NumberFormatException e){
						player.sendMessage(ChatColor.RED + "Please type number!");
						return true;
					}
					if(amount >= Points.getPoints(target)){
						Points.setPoints(target, 0);
					}else{
						Points.setPoints(target, Points.getPoints(target) - amount);
					}
					player.sendMessage(c2 + "You've removed " + c1 + "" + amount + c2 + " points for " + c1 + target.getName());
				}
				ScoreboardHandler.update(target);
				
			}else {
				player.sendMessage(c1 + "--- Commands ---");
				player.sendMessage(c1 + "/" + cmdLabel + " add <playerName> <amount> " + c2 + 
						"adds specified amount of points to player's balance!");
				player.sendMessage(c1 + "/" + cmdLabel + " withdraw <playerName> <amount> " + c2 + 
						"removes specified amount of points from player's balance!");
				player.sendMessage(c1 + "/" + cmdLabel + " set <playerName> <amount> " + c2 + 
						"sets speciified amount of points as player's balance!");
			}
		} else {
			if(args.length == 0){
				sender.sendMessage(c1 + "--- Commands ---");
				sender.sendMessage(c1 + "/" + cmdLabel + " add <playerName> <amount> " + c2 + 
						"adds specified amount of points to player's balance!");
				sender.sendMessage(c1 + "/" + cmdLabel + " withdraw <playerName> <amount> " + c2 + 
						"removes specified amount of points from player's balance!");
				sender.sendMessage(c1 + "/" + cmdLabel + " set <playerName> <amount> " + c2 + 
						"sets speciified amount of points as player's balance!");
				return true;
			} else if(args.length == 3){
				if(Bukkit.getPlayer(args[1]) == null){
					sender.sendMessage(MessageHandler.format(MessageFile.getMessage("party.offline")));
					return true;	
				}
				Player target = Bukkit.getPlayer(args[1]);
				if(args[0].equalsIgnoreCase("add")){
					int amount;
					try{
						amount = Integer.parseInt(args[2]);
					}catch(NumberFormatException e){
						sender.sendMessage(ChatColor.RED + "Please type number!");
						return true;
					}
					Points.addPoints(target, amount);
					sender.sendMessage(c2 + "You've added " + c1 + "" + amount + c2 + " points for " + c1 + target.getName());
				}
				else if(args[0].equalsIgnoreCase("set")) {
					int amount;
					try{
						amount = Integer.parseInt(args[2]);
					}catch(NumberFormatException e){
						sender.sendMessage(ChatColor.RED + "Please type number!");
						return true;
					}
					Points.setPoints(target, amount);
					sender.sendMessage(c2 + "You've set " + c1 + "" + amount + c2 + " points for " + c1 + target.getName());
				}
				else if(args[0].equalsIgnoreCase("withdraw")) {
					int amount;
					try{
						amount = Integer.parseInt(args[2]);
					}catch(NumberFormatException e){
						sender.sendMessage(ChatColor.RED + "Please type number!");
						return true;
					}
					if(amount >= Points.getPoints(target)){
						Points.setPoints(target, 0);
					}else{
						Points.setPoints(target, Points.getPoints(target) - amount);
					}
					sender.sendMessage(c2 + "You've removed " + c1 + "" + amount + c2 + " points for " + c1 + target.getName());
				}
				ScoreboardHandler.update(target);
				
			}else {
				sender.sendMessage(c1 + "--- Commands ---");
				sender.sendMessage(c1 + "/" + cmdLabel + " add <playerName> <amount> " + c2 + 
						"adds specified amount of points to player's balance!");
				sender.sendMessage(c1 + "/" + cmdLabel + " withdraw <playerName> <amount> " + c2 + 
						"removes specified amount of points from player's balance!");
				sender.sendMessage(c1 + "/" + cmdLabel + " set <playerName> <amount> " + c2 + 
						"sets speciified amount of points as player's balance!");
			}
		}
		return true;
	}
	
	public Main getPlugin() {
		return pl;
	}
}
