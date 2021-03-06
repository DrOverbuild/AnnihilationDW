package com.drizzard.annihilationdw.commands;

import com.drizzard.annihilationdw.Main;
import com.drizzard.annihilationdw.files.MessageFile;
import com.drizzard.annihilationdw.handlers.MessageHandler;
import com.drizzard.annihilationdw.handlers.ScoreboardHandler;
import com.drizzard.annihilationdw.handlers.Stats;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @deprecated Points are now managed with /dw points
 */
@Deprecated
public class PointsCommand implements CommandExecutor {

    static ChatColor c1 = ChatColor.RED;
    static ChatColor c2 = ChatColor.GRAY;
    private Main pl;

    public PointsCommand(Main plugin) {
        pl = plugin;
    }

    public static void sendCommandUsage(CommandSender player, String cmdLabel) {
        player.sendMessage(c1 + "--- Commands ---");
        player.sendMessage(c1 + "/" + cmdLabel + " add <playerName> <amount> " + c2 +
                "adds specified amount of points to player's balance!");
        player.sendMessage(c1 + "/" + cmdLabel + " withdraw <playerName> <amount> " + c2 +
                "removes specified amount of points from player's balance!");
        player.sendMessage(c1 + "/" + cmdLabel + " set <playerName> <amount> " + c2 +
                "sets speciified amount of points as player's balance!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.isOp()) {
                MessageHandler.sendMessage(player, MessageHandler.formatInteger(MessageFile.getMessage("commands.points"), Stats.getStats(player)
                        .getPoints()));
                return true;
            }
            if (args.length == 0) {
                MessageHandler.sendMessage(player, MessageHandler.formatInteger(MessageFile.getMessage("commands.points"), Stats.getStats(player)
                        .getPoints()));
                sendCommandUsage(player, cmdLabel);
                return true;
            } else if (args.length == 3) {
                if (Bukkit.getPlayer(args[1]) == null) {
                    MessageHandler.sendMessage(player, MessageFile.getMessage("party.offline"));
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (args[0].equalsIgnoreCase("add")) {
                    int amount;
                    try {
                        amount = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.RED + "Please type number!");
                        return true;
                    }
                    Stats.getStats(target).addPoints(amount);
                    player.sendMessage(c2 + "You've added " + c1 + "" + amount + c2 + " points for " + c1 + target.getName());
                } else if (args[0].equalsIgnoreCase("set")) {
                    int amount;
                    try {
                        amount = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.RED + "Please type number!");
                        return true;
                    }
                    Stats.getStats(target).setPoints(amount);
                    player.sendMessage(c2 + "You've set " + c1 + "" + amount + c2 + " points for " + c1 + target.getName());
                } else if (args[0].equalsIgnoreCase("withdraw")) {
                    int amount;
                    try {
                        amount = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.RED + "Please type number!");
                        return true;
                    }
                    Stats stats = Stats.getStats(target);
                    if (amount >= stats.getPoints()) {
                        stats.setPoints(0);
                    } else {
                        stats.setPoints(stats.getPoints() - amount);
                    }
                    player.sendMessage(c2 + "You've removed " + c1 + "" + amount + c2 + " points for " + c1 + target.getName());
                }
                ScoreboardHandler.update(target);

            } else {
                sendCommandUsage(player, cmdLabel);
            }
        } else {
            if (args.length == 0) {
                sendCommandUsage(sender, cmdLabel);
                return true;
            } else if (args.length == 3) {
                if (Bukkit.getPlayer(args[1]) == null) {
                    sender.sendMessage(MessageHandler.format(MessageFile.getMessage("party.offline")));
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (args[0].equalsIgnoreCase("add")) {
                    int amount;
                    try {
                        amount = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(ChatColor.RED + "Please type number!");
                        return true;
                    }
                    Stats.getStats(target).addPoints(amount);
                    sender.sendMessage(c2 + "You've added " + c1 + "" + amount + c2 + " points for " + c1 + target.getName());
                } else if (args[0].equalsIgnoreCase("set")) {
                    int amount;
                    try {
                        amount = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(ChatColor.RED + "Please type number!");
                        return true;
                    }
                    Stats.getStats(target).setPoints(amount);
                    sender.sendMessage(c2 + "You've set " + c1 + "" + amount + c2 + " points for " + c1 + target.getName());
                } else if (args[0].equalsIgnoreCase("withdraw")) {
                    int amount;
                    try {
                        amount = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(ChatColor.RED + "Please type number!");
                        return true;
                    }
                    Stats stats = Stats.getStats(target);
                    if (amount >= stats.getPoints()) {
                        stats.setPoints(0);
                    } else {
                        stats.setPoints(stats.getPoints() - amount);
                    }
                    sender.sendMessage(c2 + "You've removed " + c1 + "" + amount + c2 + " points for " + c1 + target.getName());
                }
                ScoreboardHandler.update(target);

            } else {
                sendCommandUsage(sender, cmdLabel);
            }
        }
        return true;
    }

    public Main getPlugin() {
        return pl;
    }
}
