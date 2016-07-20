package com.drizzard.annihilationdw.files;

import com.drizzard.annihilationdw.Main;
import com.drizzard.annihilationdw.handlers.MessageHandler;
import com.drizzard.annihilationdw.utils.ConfigLoader;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MessageFile {

    public static FileConfiguration config;
    static File file;
    private Main pl;

    public MessageFile(Main plugin) {
        createConfig();
        pl = plugin;
    }

    public static void createConfig() {
        (new File("plugins" + File.separator + "AnnihilationDW" + File.separator
                + "")).mkdirs();
        file = new File("plugins" + File.separator + "AnnihilationDW",
                "translations.yml");

//		config = YamlConfiguration.loadConfiguration(file);

        config = ConfigLoader.loadConfiguration(file);

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
        config.addDefault("player.join", "&c%p &8has joined the game! (%cp/%mp)");
        config.addDefault("player.leave", "&c%p &8has left the game! (%cp/%mp)");
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
        config.addDefault("shop.price", "&6Costs %t gold!");
        config.addDefault("shop.cant-afford", "&cYou don't have enough gold!");
        config.addDefault("commands.not-player", "This command can't be used by console!");
        config.addDefault("commands.not-op", "&cThis command is only for OPs!");
        config.addDefault("commands.not-vip", "&cThis command is only for VIPs!");
        config.addDefault("commands.points", "&7You have &c%t &7points!");
        config.addDefault("commands.other-player-points", "&7%s has &c%t&7 points!");
        config.addDefault("commands.help", "&e--------- &6AnnihilationDW Help &e---------");
        config.addDefault("commands.party_help.header", "&e------------------- &6/%s &e-------------------");
        config.addDefault("commands.party_help.create", "Create a party.");
        config.addDefault("commands.party_help.invite", "Invite a player to your party.");
        config.addDefault("commands.party_help.accept", "Accept an invitation to a party.");
        config.addDefault("commands.party_help.deny", "Deny an invitation to a party.");
        config.addDefault("commands.party_help.leave", "Leave your party.");
        config.addDefault("commands.party_help.list", "List players in your party.");
        config.addDefault("commands.descriptions.dw", "Manage AnnihilationDW.");
        config.addDefault("commands.descriptions.help", "Show commands.");
        config.addDefault("commands.descriptions.party", "Manage parties.");
        config.addDefault("commands.descriptions.votar", "Vote for a map.");
        config.addDefault("commands.descriptions.points", "Manage points.");
        config.addDefault("vote.successful", "&7You've voted for &c%s!");
        config.addDefault("vote.map-chosen", "&c%s &7will be next map!");
        config.addDefault("vote.started", "&cYou can't vote right now!");
        config.addDefault("vote.invalid-usage", "&7Usage: /votar <mapId>!");
        config.addDefault("vote.cant-vote", "&cYou can't vote right now!");
        config.addDefault("vote.reminder", "&7Use /vote <mapId> to vote for next map!");
        config.addDefault("vote.map-unavailable", "&cMap %s cannot be voted for at this moment.");
        config.addDefault("vote.map-not-found", "&c%s is not a valid map.");
        config.addDefault("kits.menu-name", "&6Choose your kit!");
        config.addDefault("kits.vip-only", "&cThis kit can only be used by VIP!");
        config.addDefault("kits.everyone", "&cThis kit can be used by everyone!");
        config.addDefault("kits.choose", "&7You've chosen %s &7kit!");
        config.addDefault("kits.cant-afford", "&cYou don't have enough points for this kit!");
        config.addDefault("kits.bought", "&7You have bought %s kit!");
        config.addDefault("kits.own", "&aYou already own this kit.");
        config.addDefault("kits.price", "&cRequires %t points");
        config.addDefault("kits.cant-change", "&cYou cannot change your kit right now!");
        config.addDefault("ability.lacking", "&cYou must have the %s ability to use.");
        config.addDefault("ability.cooling-down", "&cYou can use this ability in %t seconds.");
        config.addDefault("ability.active", "&cThis ability is currently active.");
        config.addDefault("compass.default", "&7Right click to change target nexus!");
        config.addDefault("compass.target", "&7Current target is %s &7Nexus!");
        config.addDefault("kick.full", "&cServer is full! Try again later!");
        config.addDefault("kick.too-late", "&cYou can no longer join!");
        config.addDefault("kick.lost", "&cYour team has lost!");
        config.addDefault("kick.after-phase-3", "&cThe game is after phase 3!");
        config.addDefault("party.create", "&7You've successfully created new party!");
        config.addDefault("party.offline", "&cPlayer is not found!");
        config.addDefault("party.invite", "&7You've invited %p to party!");
        config.addDefault("party.invited", "&7You've been invited to %p party! Write /party accept or /party deny");
        config.addDefault("party.joined", "&7%p has joined your party!");
        config.addDefault("party.left", "&7%p has leaved your party!");
        config.addDefault("party.leave", "&7You've left your party!");
        config.addDefault("party.accept", "&7You've accepted to join %p party!");
        config.addDefault("party.deny", "&7You've denied to join %p party!");
        config.addDefault("party.args", "&cInvalid arguments. Type \"/%s help\" to view help.");
        config.addDefault("party.alone", "&aYou are not in a party. Type \"&6/party create&a\" to create one.");
        config.addDefault("party.full", "&cThe party is full.");
        config.addDefault("party.scoreboard.alone", "&aYou are not in a party. Type \"&6/party create&a\" to create one.");
        config.addDefault("time.minutes", "&a%t minutes");
        config.addDefault("time.seconds", "&a%t seconds");
        config.addDefault("protection.protected", "&cThis %s is protected!");
        config.addDefault("protection.now-protected", "&cYou have protected this %s!");
        config.addDefault("protection.removed", "&cThis %s is no longer protected.");
        config.addDefault("vip-prefix", "&f[&e&lVIP&f]");
        config.addDefault("currency.title", "&6Gold Ingot");
        List<String> lore = new ArrayList<>();
        lore.add("&3For use in the shops");
        config.addDefault("currency.lore", lore);
        config.addDefault("signs.change-kit", "&4[&5Change Kit&4]");
        config.addDefault("scoreboard.lobby.header", "&l&6Drizzard Wars");
        config.addDefault("scoreboard.lobby.maps", "&7{map_name} votes:");
        List<String> contents1 = new ArrayList<>();
        contents1.add("{maps}");
        contents1.add("");
        contents1.add("&7Until start: &c{until_start}s");
        contents1.add("&7Points: &c{points}");
        contents1.add("");
        contents1.add("&6PARTY");
        contents1.add("{party}");
        config.addDefault("scoreboard.lobby.contents", contents1);
        config.addDefault("scoreboard.in_game.header", "&6&l{map_name}");
        config.addDefault("scoreboard.in_game.team_health", "{team_color}{team_name} Nexus: {team_nexus_health}");
        List<String> contents2 = new ArrayList<>();
        contents2.add("{team_health}");
        contents2.add("{team_color}Kills: {team_kills}");
        contents2.add("");
        contents2.add("{phase_message}");
        contents2.add("&aTime Remaining");
        contents2.add("&a{time_remaining}");
        config.addDefault("scoreboard.in_game.contents", contents2);
        config.options().copyDefaults(true);
        saveConfig();
    }

    public static void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void set(String key, Object value) {
        config.set(key, value);
    }

    public static String getMessage(String path) {
        return config.getString(path);
    }

    public static String formatMessage(String path) {
        return MessageHandler.format(getMessage(path));
    }

    public static List<String> getMessageList(String path) {
        return config.getStringList(path);
    }

    public Main getMainClass() {
        return pl;
    }
}