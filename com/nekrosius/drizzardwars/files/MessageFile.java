package com.nekrosius.drizzardwars.files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import com.nekrosius.drizzardwars.Main;
import com.nekrosius.drizzardwars.handlers.MessageHandler;
import com.nekrosius.drizzardwars.utils.ConfigLoader;

public class MessageFile {
	
	static File file;
	public static FileConfiguration config;
	
	private Main pl;
	public MessageFile(Main plugin) {
		createConfig();
		pl = plugin;
	}
	
	public static void createConfig() {
		(new File("plugins" + File.separator + "DrizzardWars" + File.separator
				+ "")).mkdirs();
		file = new File("plugins" + File.separator + "DrizzardWars",
				"translations.yml");

//		config = YamlConfiguration.loadConfiguration(file);

		config = ConfigLoader.loadConfiguration(file);

		if(!file.exists()){
			config.addDefault("general.all", "All");
			config.addDefault("general.team", "Team");
			config.addDefault("general.points", "points");
			config.addDefault("team.full", "&7This team is full! Choose another!");
			config.addDefault("team.destroyed", "%s team&7 Nexus has been destroyed!");
			List<String> info = new ArrayList<String>();
			info.add("&3---------------");
			info.add("&6Welcome to Drizzard Wars");
			info.add("&6Enjoy your stay!");
			info.add("&3---------------");
			config.addDefault("player.join-information", info);
			config.addDefault("player.join", "&c%p &8has joined the server!");
			config.addDefault("player.leave", "&c%p &8has left the server!");
			config.addDefault("player.kill", "%p&7 has killed %v!");
			config.addDefault("game.until-start", "&6The game will start in &c%t sec.&6!");
			config.addDefault("game.not-enough-players", "&7Game won't start. Minimum amount of players is &c%t!");
			config.addDefault("game.start", "&6The game has started! Good luck!");
			config.addDefault("game.nexus-broken", "&cYour team nexus has been damaged!");
			config.addDefault("game.winner", "%s &6has won the game!!");
			config.addDefault("phases.1", "&c1st phase");
			config.addDefault("phases.2", "&c2nd phase");
			config.addDefault("phases.3", "&c3rd phase");
			config.addDefault("phases.4", "&c4th phase");
			config.addDefault("phases.5", "&cLast phase");
			config.addDefault("phases.descriptions.1", "Initial phase. Prepare for battle.");
			config.addDefault("phases.descriptions.2", "PVP enabled and nexus invincibility disabled.");
			config.addDefault("phases.descriptions.3", "Diamonds now spawn.");
			config.addDefault("phases.descriptions.4", "Brewing items available in shop.");
			config.addDefault("phases.descriptions.5", "Nexus damage doubled.");
			config.addDefault("menu.save-successful", "&7Configuration files has been saved successfuly!");
			config.addDefault("menu.reload-successful", "&7Configuration files has been reloaded successfuly!");
			config.addDefault("scoreboard.until-start", "&7Until start: &c%t");
			config.addDefault("shop.price", "&6Costs %t gold!");
			config.addDefault("shop.cant-afford", "&cYou don't have enough gold!");
			config.addDefault("commands.not-player", "This command can't be used by console!");
			config.addDefault("commands.not-op", "&cThis command is only for OPs!");
			config.addDefault("commands.not-vip", "&cThis command is only for VIPs!");
			config.addDefault("commands.points", "&7You have &c%t &7points!");
			config.addDefault("commands.help","&e--------- &6DrizzardWars Help &e---------");
			config.addDefault("commands.party_help.header","&e------------------- &6/party &e-------------------");
			config.addDefault("commands.party_help.create","Create a party.");
			config.addDefault("commands.party_help.invite","Invite a player to your party.");
			config.addDefault("commands.party_help.accept","Accept an invitation to a party.");
			config.addDefault("commands.party_help.deny","Deny an invitation to a party.");
			config.addDefault("commands.party_help.leave","Leave your party.");
			config.addDefault("commands.party_help.list","List players in your party.");
			config.addDefault("commands.descriptions.dw", "Manage DrizzardWars.");
			config.addDefault("commands.descriptions.help", "Show commands.");
			config.addDefault("commands.descriptions.party", "Manage points.");
			config.addDefault("commands.descriptions.votar", "Vote for a map.");
			config.addDefault("commands.descriptions.points", "Configure DrizzardWars.");
			config.addDefault("vote.successful", "&7You've voted for &c%s!");
			config.addDefault("vote.map-chosen", "&c%s &7will be next map!");
			config.addDefault("vote.started", "&cYou can't vote right now!");
			config.addDefault("vote.invalid-usage", "&7Usage: /votar <mapId>!");
			config.addDefault("vote.cant-vote", "&cYou can't vote right now!");
			config.addDefault("vote.reminder", "&7Use /vote <mapId> to vote for next map!");
			config.addDefault("kits.menu-name", "&6Choose your kit!");
			config.addDefault("kits.vip-only", "&cThis kit can only be used by VIP!");
			config.addDefault("kits.everyone", "&cThis kit can be used by everyone!");
			config.addDefault("kits.choose", "&7You've chosen %s &7kit!");
			config.addDefault("kits.cant-afford", "&cYou don't have enough points for this kit!");
			config.addDefault("kits.bought", "&7You have bought %s kit!");
			config.addDefault("kits.own","&aYou already own this kit.");
			config.addDefault("kits.price", "&cRequires %t points");
			config.addDefault("compass.default", "&7Right click to change target nexus!");
			config.addDefault("compass.target", "&7Current target is %s &7Nexus!");
			config.addDefault("kick.full", "&cServer is full! Try again later!");
			config.addDefault("kick.too-late", "&cYou can no longer join!");
			config.addDefault("kick.lost", "&cYour team has lost!");
			config.addDefault("party.create", "&7You've successfully created new party!");
			config.addDefault("party.offline", "&cPlayer is not found!");
			config.addDefault("party.invite", "&7You've invited %p to party!");
			config.addDefault("party.invited", "&7You've been invited to %p party! Write /party accept or /party deny");
			config.addDefault("party.joined", "&7%p has joined your party!");
			config.addDefault("party.left", "&7%p has leaved your party!");
			config.addDefault("party.leave", "&7You've left your party!");
			config.addDefault("party.accept", "&7You've accepted to join %p party!");
			config.addDefault("party.deny", "&7You've denied to join %p party!");
			config.addDefault("party.args", "&cInvalid arguments. Type \"/party help\" to view help.");
			config.addDefault("party.alone", "&aYou are not in a party. Type \"&6/party create&a\" to create one.");
			config.addDefault("party.full","&cThe party is full.");
			config.addDefault("party.scoreboard.header","PARTY");
			config.addDefault("party.scoreboard.alone","&aYou are not in a party. Type \"&6/party create&a\" to create one.");
			config.addDefault("time.remaining", "&aT. Remaining");
			config.addDefault("time.minutes","&a%t minutes");
			config.addDefault("time.seconds", "&a%t seconds");
			config.addDefault("protection.protected","&cThis %s is protected!");
			config.addDefault("protection.now-protected","&cYou have protected this %s!");
			config.addDefault("protection.removed","&cThis %s is no longer protected.");
			config.options().copyDefaults(true);
			saveConfig();
			return;
		}
		if(!config.contains("general.all")){
			config.set("general.all", "All");
		}
		if(!config.contains("phases.1")){
			config.set("phases.1", "&c1st phase");
		}
		if(!config.contains("phases.2")){
			config.set("phases.2", "&c2nd phase");
		}
		if(!config.contains("phases.3")){
			config.set("phases.3", "&c3rd phase");
		}
		if(!config.contains("phases.4")){
			config.set("phases.4", "&c4th phase");
		}
		if(!config.contains("phases.5")){
			config.set("phases.5", "&cLast phase");
		}
		if(!config.contains("scoreboard.until-start")){
			config.set("scoreboard.until-start", "&7Until start: &c%t");
		}
		if(!config.contains("game.nexus-broken")){
			config.set("game.nexus-broken", "&cYour team nexus has been damaged!");
		}
		if(!config.contains("commands.points")){
			config.set("commands.points", "&7You have &c%t &7points!");
		}
		if(!config.contains("time.remaining")){
			config.set("time.remaining","&aT. Remaining");
		}
		if(!config.contains("time.seconds")){
			config.set("time.seconds", "&a%t seconds");
		}
		if(!config.contains("time.minutes")){
			config.set("time.minutes","&a%t minutes");
		}
		if(!config.contains("kits.price")){
			config.set("kits.price", "&cRequires %t points");
		}
		if(!config.contains("kits.own")){
			config.set("kits.own","&aYou already own this kit.");
		}
		if(!config.contains("commands.help")){
			config.set("commands.help", "&e--------- &6DrizzardWars Help &e---------");
		}
		if(!config.contains("commands.descriptions.dw")){
			config.set("commands.descriptions.dw", "Manage DrizzardWars.");
		}
		if(!config.contains("commands.descriptions.help")){
			config.set("commands.descriptions.help", "Show commands.");
		}
		if(!config.contains("commands.descriptions.party")){
			config.set("commands.descriptions.party", "Manage parties.");
		}
		if(!config.contains("commands.descriptions.votar")){
			config.set("commands.descriptions.votar", "Vote for a map.");
		}
		if(!config.contains("commands.descriptions.points")){
			config.set("commands.descriptions.points", "Manage points.");
		}
		if(!config.contains("commands.party_help.header")){
			config.set("commands.party_help.header", "&e------------------- &6/party &e-------------------");
		}
		if(!config.contains("commands.party_help.create")){
			config.set("commands.party_help.create", "Create a party.");
		}
		if(!config.contains("commands.party_help.invite")){
			config.set("commands.party_help.invite", "Invite a player to your party.");
		}
		if(!config.contains("commands.party_help.accept")){
			config.set("commands.party_help.accept", "Accept an invitation to a party.");
		}
		if(!config.contains("commands.party_help.deny")){
			config.set("commands.party_help.deny", "Deny an invitation to a party.");
		}
		if(!config.contains("commands.party_help.leave")){
			config.set("commands.party_help.leave", "Leave your party.");
		}
		if(!config.contains("commands.party_help.list")){
			config.set("commands.party_help.list", "List players in your party.");
		}
		if(!config.contains("party.args")){
			config.set("party.args", "&cInvalid arguments. Type \"/party help\" to view help.");
		}
		if(!config.contains("party.alone")){
			config.set("party.alone", "&aYou are not in a party. Type \"&6/party create&a\" to create one.");
		}
		if(!config.contains("party.full")){
			config.set("party.full", "&cThe party is full.");
		}
		if(!config.contains("party.scoreboard.header")){
			config.set("party.scoreboard.header","PARTY");
		}
		if(!config.contains("party.scoreboard.alone")){
			config.set("party.scoreboard.alone", "&aYou are not in a party. Type \"&6/party create&a\" to create one.");
		}
		if(!config.contains("phases.descriptions.1")){
			config.set("phases.descriptions.1","Initial phase. Prepare for battle.");
		}
		if(!config.contains("phases.descriptions.2")){
			config.set("phases.descriptions.2","PVP enabled and nexus invincibility disabled.");
		}
		if(!config.contains("phases.descriptions.3")){
			config.set("phases.descriptions.3","Diamonds now spawn.");
		}
		if(!config.contains("phases.descriptions.4")){
			config.set("phases.descriptions.4","Brewing items available in shop.");
		}
		if(!config.contains("phases.descriptions.5")){
			config.set("phases.descriptions.5","Nexus damage doubled.");
		}
		if(!config.contains("protection.protected")){
			config.set("protection.protected", "&cThis %s is protected!");
		}
		if(!config.contains("protection.now-protected")){
			config.set("protection.now-protected","&cYou have protected this %s!");
		}
		if(!config.contains("protection.removed")){
			config.set("protection.removed","&cThis %s is no longer protected.");
		}

		saveConfig();
	}
	
	public static void saveConfig() {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void set(String key, Object value)
	{
		config.set(key, value);
	}
	
	public static String getMessage(String path)
	{
		return config.getString(path);
	}

	public static String formatMessage(String path){
		return MessageHandler.format(getMessage(path));
	}

	public static List<String> getMessageList(String path)
	{
		return config.getStringList(path);
	}
	
	public Main getMainClass()
	{
		return pl;
	}
}