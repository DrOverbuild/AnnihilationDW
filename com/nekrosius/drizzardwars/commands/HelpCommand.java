package com.nekrosius.drizzardwars.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.nekrosius.drizzardwars.files.MessageFile;
import com.nekrosius.drizzardwars.handlers.MessageHandler;

/**
 * Created by jasper on 12/22/15.
 */
public class HelpCommand implements CommandExecutor{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

		List<String> messages = new ArrayList<>();

		messages.add(MessageHandler.format(MessageFile.getMessage("commands.help")));

		if(sender.isOp()){
			messages.add(ChatColor.GOLD + "/dw: " + ChatColor.RESET + MessageHandler.format(MessageFile.getMessage("commands.descriptions.dw")));
		}

		messages.add(ChatColor.GOLD + "/help: " + ChatColor.RESET + MessageHandler.format(MessageFile.getMessage("commands.descriptions.help")));
		messages.add(ChatColor.GOLD + "/party: " + ChatColor.RESET + MessageHandler.format(MessageFile.getMessage("commands.descriptions.party")));
		messages.add(ChatColor.GOLD + "/votar: " + ChatColor.RESET + MessageHandler.format(MessageFile.getMessage("commands.descriptions.votar")));

		if(sender.isOp()){
			messages.add(ChatColor.GOLD + "/points: " + ChatColor.RESET + MessageHandler.format(MessageFile.getMessage("commands.descriptions.points")));
		}

		sender.sendMessage(messages.toArray(new String[]{}));
		return true;
	}
}
