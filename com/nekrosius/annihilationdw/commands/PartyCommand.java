package com.nekrosius.annihilationdw.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.nekrosius.annihilationdw.Main;
import com.nekrosius.annihilationdw.files.MessageFile;
import com.nekrosius.annihilationdw.handlers.MessageHandler;
import com.nekrosius.annihilationdw.handlers.Party;
import com.nekrosius.annihilationdw.handlers.ScoreboardHandler;
import com.nekrosius.annihilationdw.managers.PartyManager;

public class PartyCommand implements CommandExecutor {

	private Main pl;
	public PartyCommand(Main plugin){
		pl = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!onCommandParty(sender,cmd,label,args)){
			sender.sendMessage(MessageHandler.formatString(MessageFile.getMessage("party.args"),label));
		}
		return true;
	}

	public boolean onCommandParty(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(!(sender instanceof Player)) return true;
		Player player = (Player) sender;

		if(args.length == 0){
			return false;
		}
		if(args.length == 1){
			if(args[0].equalsIgnoreCase("create")){
				create(player);
			} else if(args[0].equalsIgnoreCase("leave")) {
				leave(player);
			} else if(args[0].equalsIgnoreCase("accept")){
				accept(player);
			} else if(args[0].equalsIgnoreCase("deny")){
				deny(player);
			} else if(args[0].equalsIgnoreCase("list")){
				list(player);
			} else if(args[0].equalsIgnoreCase("help")) {
				help(player, commandLabel);
			}else{
				return false;
			}
			return true;
		}else if(args.length == 2){
			if(args[0].equalsIgnoreCase("invite")){
				invite(player,args[1]);
				return true;
			}

		}
		return false;
	}

	public void create(Player player){
		if(!player.hasPermission("dw.party")) {
			MessageHandler.sendMessage(player, MessageFile.getMessage("commands.not-vip"));
			return;
		}

		if(PartyManager.hasParty(player)){
			leave(player);
		}

		PartyManager.createParty(player);
		MessageHandler.sendMessage(player, MessageFile.getMessage("party.create"));
		ScoreboardHandler.updateAll();
	}

	public void leave(Player player){
		if(PartyManager.hasParty(player)){
			if(PartyManager.isLeader(player)){
				PartyManager.deleteParty(player);
				MessageHandler.sendMessage(player, MessageHandler.formatPlayer(MessageFile.getMessage("party.leave"), player));
			}else{
				for(Player p : PartyManager.getParty(player).getPlayers()){
					if(!p.equals(player)){
						MessageHandler.sendMessage(p, MessageHandler.formatPlayer(MessageFile.getMessage("party.left"), player));
					}
				}
				PartyManager.getParty(player).removePlayer(player);
				MessageHandler.sendMessage(player, MessageHandler.formatPlayer(MessageFile.getMessage("party.leave"), player));
			}
		}
		ScoreboardHandler.updateAll();

	}

	public void accept(Player player){
		Party party = PartyManager.getParty(PartyManager.getInviter(player));
		if(party == null){
			return;
		}

		if(party.isFull()){
			player.sendMessage(MessageFile.formatMessage("party.full"));
			return;
		}

		party.addPlayer(player);
		MessageHandler.sendMessage(player, MessageHandler.formatPlayer(MessageFile.getMessage("party.accept"), PartyManager.getInviter(player)));
		PartyManager.removeInvite(player);
		ScoreboardHandler.updateAll();

	}

	public void deny(Player player){
		MessageHandler.sendMessage(player, MessageHandler.formatPlayer(MessageFile.getMessage("party.deny"), PartyManager.getInviter(player)));
		PartyManager.removeInvite(player);
		ScoreboardHandler.updateAll();
	}

	public void list(Player player){
		if(PartyManager.hasParty(player)){
			player.sendMessage(ChatColor.GOLD + "---------------");
			player.sendMessage(ChatColor.LIGHT_PURPLE + "★  " + PartyManager.getParty(player).getLeader().getName());
			int i = 1;
			for(Player p : PartyManager.getParty(player).getPlayers()){
				player.sendMessage(ChatColor.GRAY + "" + i + ". " + p.getName());
				i++;
			}
			player.sendMessage(ChatColor.GOLD + "---------------");
		}else{
			player.sendMessage(MessageFile.formatMessage("party.alone"));
		}
	}

	public void invite(Player player, String toInvite){
		if(!player.hasPermission("dw.party")) {
			MessageHandler.sendMessage(player, MessageFile.getMessage("commands.not-vip"));
			return;
		}
		if(!PartyManager.hasParty(player)){
			PartyManager.createParty(player);
		}
		if(!PartyManager.isLeader(player)) return;
		if(Bukkit.getPlayer(toInvite) == null){
			MessageHandler.sendMessage(player, MessageFile.getMessage("party.offline"));
			return;
		}

		if(PartyManager.getParty(player).isFull()){
			player.sendMessage(MessageFile.formatMessage("party.full"));
			return;
		}

		Player target = Bukkit.getPlayer(toInvite);
		MessageHandler.sendMessage(player, MessageHandler.formatPlayer(MessageFile.getMessage("party.invite"), target));
		MessageHandler.sendMessage(target, MessageHandler.formatPlayer(MessageFile.getMessage("party.invited"), player));
		PartyManager.sendInvite(target, player);
	}

	public void help(Player player, String label){
		List<String> messages = new ArrayList<>();

		messages.add(MessageHandler.formatString(MessageFile.getMessage("commands.party_help.header"),label));
		messages.add(ChatColor.GOLD + "/" + label + " create: " + ChatColor.RESET + MessageFile.formatMessage("commands.party_help.create"));
		messages.add(ChatColor.GOLD + "/" + label + " invite <player>: " + ChatColor.RESET + MessageFile.formatMessage("commands.party_help.invite"));
		messages.add(ChatColor.GOLD + "/" + label + " accept: " + ChatColor.RESET + MessageFile.formatMessage("commands.party_help.accept"));
		messages.add(ChatColor.GOLD + "/" + label + " deny: " + ChatColor.RESET + MessageFile.formatMessage("commands.party_help.deny"));
		messages.add(ChatColor.GOLD + "/" + label + " leave: " + ChatColor.RESET + MessageFile.formatMessage("commands.party_help.leave"));
		messages.add(ChatColor.GOLD + "/" + label + " list: " + ChatColor.RESET + MessageFile.formatMessage("commands.party_help.list"));

		player.sendMessage(messages.toArray(new String[]{}));
	}
	
	public Main getMainClass()
	{
		return pl;
	}
}