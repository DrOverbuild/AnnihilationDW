package com.nekrosius.annihilationdw.commands;

import com.nekrosius.annihilationdw.handlers.Points;
import com.nekrosius.annihilationdw.handlers.ScoreboardHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.nekrosius.annihilationdw.Main;
import com.nekrosius.annihilationdw.files.MapFile;
import com.nekrosius.annihilationdw.files.MessageFile;
import com.nekrosius.annihilationdw.handlers.MessageHandler;
import com.nekrosius.annihilationdw.inventories.AdminMenu;
import com.nekrosius.annihilationdw.inventories.MapsSetupMenu;
import com.nekrosius.annihilationdw.managers.MapManager;
import com.nekrosius.annihilationdw.managers.TeamManager;

import java.util.Arrays;

public class DWCommand implements CommandExecutor{
	
	private Main pl;

	static ChatColor c1 = ChatColor.RED;
	static ChatColor c2 = ChatColor.GRAY;

	public DWCommand(Main plugin){
		pl = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {	
		/*
		if(sender.isOp() && args.length > 0) {
			PlayerHandler.addAbility((Player) sender, Ability.getAbility(args[0]));
			Bukkit.getPluginManager().callEvent(new JoinGameEvent((Player) sender, null));
			Main.println(sender.getName() + " ability is set to " + args[0]);
			return true;
		}
		*/
		
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
			if(player.getWorld().getName().startsWith("plugins/AnnihilationDW/Maps")){
				new TeamManager(pl, MapFile.config.getInt("team.amount"));
				MapsSetupMenu.setupMapMenu(player, MapManager.getMap(player.getWorld().getName()));
			}else{
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
				}else if(args[0].equalsIgnoreCase("points")){
					pointsCommand((Player)sender, commandLabel, new String[] {});
					return true;
				}
			}

			if(args.length >= 1){
				if(args[0].equalsIgnoreCase("points")){
					String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
					pointsCommand((Player)sender, commandLabel, newArgs);
				}else if(args[0].equalsIgnoreCase("party")){
					String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
					pl.getCommand("party").getExecutor().onCommand(sender, pl.getCommand("party"),commandLabel + " party", newArgs);
				}
			}


		}
		return true;
	}

	public void pointsCommand(Player sender, String cmdLabel, String[] args){
		if(args.length == 0){
			MessageHandler.sendMessage(sender, MessageHandler.formatInteger(MessageFile.getMessage("commands.points"), Points.getPoints(sender)));

			if (sender.isOp()) {
				sendCommandUsage(sender, cmdLabel + " points");
			}
			return;
		}

		if(!sender.isOp()){
			sender.sendMessage(MessageFile.formatMessage("commands.not-op"));
			return;
		}

		if(args.length == 1){
			Player target = Bukkit.getPlayer(args[0]);
			if(target == null){
				sender.sendMessage(MessageHandler.format(MessageFile.getMessage("party.offline")));
				return;
			}
			sender.sendMessage(MessageHandler.formatInteger(MessageHandler.formatString(
					MessageFile.getMessage("commands.other-player-points"), target.getName()), Points.getPoints(target)));
		}else if(args.length == 3){
			if(Bukkit.getPlayer(args[1]) == null){
				sender.sendMessage(MessageHandler.format(MessageFile.getMessage("party.offline")));
				return;
			}
			Player target = Bukkit.getPlayer(args[1]);
			if(args[0].equalsIgnoreCase("add")){
				int amount;
				try{
					amount = Integer.parseInt(args[2]);
				}catch(NumberFormatException e){
					sender.sendMessage(ChatColor.RED + "Please type number!");
					return;
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
					return;
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
					return;
				}
				if(amount >= Points.getPoints(target)){
					Points.setPoints(target, 0);
				}else{
					Points.setPoints(target, Points.getPoints(target) - amount);
				}
				sender.sendMessage(c2 + "You've removed " + c1 + "" + amount + c2 + " points for " + c1 + target.getName());
			}
			Points.savePoints(target);
			ScoreboardHandler.update(target);
		}else{
			sendCommandUsage(sender,cmdLabel + " points");
		}


	}

	public static void sendCommandUsage(CommandSender sender, String cmdLabel){
		sender.sendMessage(c1 + "--- Commands ---");
		sender.sendMessage(c1 + "/" + cmdLabel + " <playerName>:" + c2 + " gets number of points of <playerName>");
		sender.sendMessage(c1 + "/" + cmdLabel + " add <playerName> <amount>" + c2 +
				" adds specified amount of points to player's balance!");
		sender.sendMessage(c1 + "/" + cmdLabel + " withdraw <playerName> <amount>" + c2 +
				" removes specified amount of points from player's balance!");
		sender.sendMessage(c1 + "/" + cmdLabel + " set <playerName> <amount>" + c2 +
				" sets speciified amount of points as player's balance!");
	}
	
	public Main getMainClass()
	{
		return pl;
	}
}