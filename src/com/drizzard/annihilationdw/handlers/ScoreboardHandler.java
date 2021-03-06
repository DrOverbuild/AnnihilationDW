package com.drizzard.annihilationdw.handlers;

import com.drizzard.annihilationdw.files.ConfigFile;
import com.drizzard.annihilationdw.files.MessageFile;
import com.drizzard.annihilationdw.managers.MapManager;
import com.drizzard.annihilationdw.managers.PartyManager;
import com.drizzard.annihilationdw.managers.TeamManager;
import com.drizzard.annihilationdw.utils.SimpleScoreboard;
import com.drizzard.annihilationdw.utils.WordWrap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardHandler {

    public static void updateAll() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            update(p);
        }
    }

    public static void update(Player player) {
        SimpleScoreboard sb;
        if (Game.getGameState().equals(GameState.LOBBY)) {


            sb = new SimpleScoreboard(formatScoreboardVariables(MessageHandler.format(MessageFile.getMessage("scoreboard.lobby.header")), player));

            int lineNumber = -1;

            for (String line : MessageHandler.scoreboardLobbyContents) {
                if (line.toLowerCase().contains("{maps}")) {
                    String template = MessageFile.getMessage("scoreboard.lobby.maps");

                    if (template.toLowerCase().contains("{map_votes}")) {
                        for (GameMap m : MapManager.getVotableMaps()) {
                            sb.add(MessageHandler.format(formatMapVariables(template, m)), lineNumber--);
                        }
                    } else {
                        for (GameMap m : MapManager.getVotableMaps()) {
                            sb.add(MessageHandler.format(formatMapVariables(template, m)), m.getVotes());
                        }
                    }
                } else if (line.toLowerCase().contains("{party}")) {
                    for (String line2 : getPartyLines(player)) {
                        if (line2.length() > 47) {
                            line2 = line2.substring(0, 47);
                        }
                        sb.add(MessageHandler.format(line2), lineNumber--);
                    }
                } else {
                    sb.add(MessageHandler.format(formatScoreboardVariables(line, player)), lineNumber--);
                }
            }

            org.bukkit.scoreboard.Team newTeam = sb.getScoreboard().registerNewTeam("lobby");
            newTeam.setPrefix(ChatColor.GREEN + "");

            for (Player p : Bukkit.getOnlinePlayers()) {
                newTeam.addEntry(p.getName());
            }

            for (String prefixLocation : ConfigFile.config.getConfigurationSection("prefixes").getKeys(false)) {
                String permission = ConfigFile.config.getString("prefixes." + prefixLocation + ".permission");
                boolean applyToOperators = ConfigFile.config.getBoolean("prefixes." + prefixLocation + ".include-admins");

                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.hasPermission(permission)) {
                        String prefix = MessageHandler.format(ConfigFile.config.getString("prefixes." + prefixLocation + ".prefix"));

                        if (prefix.length() >= 17) {
                            prefix = prefix.substring(0, 16);
                        }

                        org.bukkit.scoreboard.Team team = sb.getScoreboard().getTeam(prefixLocation);

                        if (team == null) {
                            team = sb.getScoreboard().registerNewTeam(prefixLocation);
                        }

                        team.setPrefix(prefix);

                        if (p.isOp() && !applyToOperators) {
                        } else {
                            team.addEntry(p.getName());
                        }
                    }
                }
            }
        } else {
            sb = new SimpleScoreboard(formatScoreboardVariables(MessageHandler.format(MessageFile.getMessage("scoreboard.in_game.header")), player));

            int lineNumber = -1;

            for (String line : MessageHandler.scoreboardInGameContents) {
                if (line.toLowerCase().contains("{team_health}")) {
                    String template = MessageFile.getMessage("scoreboard.in_game.team_health");
                    if (template.toLowerCase().contains("{team_nexus_health}")) {
                        for (Team team : TeamManager.getAliveTeams()) {
                            sb.add(MessageHandler.format(formatTeamHealthVariables(template, team)), lineNumber--);
                        }
                    } else {
                        for (Team team : TeamManager.getAliveTeams()) {
                            sb.add(MessageHandler.format(formatTeamHealthVariables(template, team)), team.getNexusHealth());
                        }
                    }
                } else {
                    sb.add(MessageHandler.format(formatScoreboardVariables(line, player)), lineNumber--);
                }
            }

            for (Team t : TeamManager.getAliveTeams()) {
                org.bukkit.scoreboard.Team newTeam = sb.getScoreboard().registerNewTeam(t.getCodeName());
                newTeam.setPrefix(t.getColor() + "");
                for (Player p : t.getAlivePlayers()) {
                    newTeam.addPlayer(p);
                }
            }
        }
        sb.build();
        sb.send(player);
    }

    public static String formatTeamHealthVariables(String line, Team team) {
        line = line.replace("{team_color}", team.getColor() + "");
        line = line.replace("{team_name}", team.getName());
        line = line.replace("{team_nexus_health}", team.getNexusHealth() + "");
        if (line.length() > 47) {
            line = line.substring(0, 47);
        }
        return line;
    }

    public static String formatMapVariables(String line, GameMap map) {
        line = line.replace("{map_name}", map.getName());
        line = line.replace("{map_votes}", map.getVotes() + "");
        if (line.length() > 47) {
            line = line.substring(0, 47);
        }
        return line;
    }

    public static String formatScoreboardVariables(String line, Player p) {
        line = line.replace("{until_start}", getUntilStart());
        line = line.replace("{until_start}", getUntilStart());
        line = line.replace("{points}", getPlayerPoints(p));
        line = line.replace("{map_name}", getActiveMapName());
        line = line.replace("{team_name}", getTeamName(p));
        line = line.replace("{team_color}", getTeamColor(p));
        line = line.replace("{team_kills}", getTeamKills(p));
        line = line.replace("{team_nexus_health}", getTeamNexusHealth(p));
        line = line.replace("{time_remaining}", getTimeRemaining());
        line = line.replace("{phase_message}", MessageHandler.getPhaseMessage(Game.getPhase()));
        if (line.length() > 47) {
            line = line.substring(0, 47);
        }
        return line;
    }

    private static String getTeamKills(Player p) {
        Team t = TeamManager.getTeam(p);
        if (t != null) {
            return "" + t.getKills();
        }
        return "";
    }

    private static String getTeamNexusHealth(Player p) {
        Team t = TeamManager.getTeam(p);
        if (t != null) {
            return "" + t.getNexusHealth();
        }
        return "";
    }

    private static String getTeamColor(Player p) {
        Team t = TeamManager.getTeam(p);
        if (t != null) {
            return t.getColor() + "";
        }
        return "";
    }

    private static String getTeamName(Player p) {
        Team team = TeamManager.getTeam(p);
        if (team != null) {
            return team.getName();
        }
        return "";
    }

    private static String getActiveMapName() {
        if (MapManager.getActiveMap() != null) {
            return MapManager.getActiveMap().getName();
        }
        return "";
    }

    public static String getUntilStart() {
        return "" + Game.getCountdown();
    }

    private static String getPlayerPoints(Player p) {
        return "" + Stats.getStats(p).getPoints();
    }

    private static String getTimeRemaining() {
        if (Game.getPhaseTime() > 60) {
            return MessageHandler.formatInteger(MessageFile.getMessage("time.minutes"), Game.getPhaseTime() / 60);
        } else {
            return MessageHandler.formatInteger(MessageFile.getMessage("time.seconds"), Game.getPhaseTime());
        }
    }

    private static List<String> getPartyLines(Player player) {
        List<String> partyLines = new ArrayList<>();
        if (PartyManager.hasParty(player)) {
            List<Player> players = PartyManager.getParty(player).getPlayers();
            Player leader = PartyManager.getParty(player).getLeader();

            if (players.contains(leader)) {
                players.remove(leader);
            }

            partyLines.add(ChatColor.DARK_PURPLE + leader.getName());

            for (Player p : players) {
                partyLines.add(ChatColor.GRAY + p.getName());
            }
        } else {
            List<StringBuilder> lines = WordWrap.wordWrap(MessageFile.formatMessage("party.scoreboard.alone"), 20);

            for (StringBuilder builder : lines) {
                partyLines.add(builder.toString());
            }
        }

        return partyLines;
    }
}
