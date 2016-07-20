package com.drizzard.annihilationdw.commands;

import com.drizzard.annihilationdw.Main;
import com.drizzard.annihilationdw.files.MapFile;
import com.drizzard.annihilationdw.files.MessageFile;
import com.drizzard.annihilationdw.handlers.MessageHandler;
import com.drizzard.annihilationdw.handlers.ScoreboardHandler;
import com.drizzard.annihilationdw.handlers.Stats;
import com.drizzard.annihilationdw.inventories.AdminMenu;
import com.drizzard.annihilationdw.inventories.MapsSetupMenu;
import com.drizzard.annihilationdw.managers.MapManager;
import com.drizzard.annihilationdw.managers.TeamManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class DWCommand implements CommandExecutor {

    static ChatColor c1 = ChatColor.RED;
    static ChatColor c2 = ChatColor.GRAY;
    private Main pl;

    public DWCommand(Main plugin) {
        pl = plugin;
    }

    public static void sendCommandUsage(CommandSender sender, String cmdLabel) {
        sender.sendMessage(c1 + "--- Commands ---");
        sender.sendMessage(c1 + "/" + cmdLabel + " <playerName>:" + c2 + " gets number of points of <playerName>");
        sender.sendMessage(c1 + "/" + cmdLabel + " add <playerName> <amount>" + c2 +
                " adds specified amount of points to player's balance!");
        sender.sendMessage(c1 + "/" + cmdLabel + " withdraw <playerName> <amount>" + c2 +
                " removes specified amount of points from player's balance!");
        sender.sendMessage(c1 + "/" + cmdLabel + " set <playerName> <amount>" + c2 +
                " sets speciified amount of points as player's balance!");
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

        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(MessageFile.getMessage("commands.not-player"));
                return true;
            }
            Player player = (Player) sender;
            if (!player.isOp()) {
                MessageHandler.sendMessage(player, MessageFile.getMessage("commands.not-op"));
                return true;
            }
            if (player.getWorld().getName().startsWith("plugins/AnnihilationDW/Maps")) {
                new TeamManager(pl, MapFile.config.getInt("team.amount"));
                MapsSetupMenu.setupMapMenu(player, MapManager.getMap(player.getWorld().getName()));
            } else {
                AdminMenu.setup(player);
            }
            return true;
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(MessageFile.getMessage("commands.not-player"));
                return true;
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("visible")) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        for (Player t : Bukkit.getOnlinePlayers()) {
                            p.showPlayer(t);
                        }
                    }
                } else if (args[0].equalsIgnoreCase("points")) {
                    pointsCommand((Player) sender, commandLabel, new String[]{});
                    return true;
                }
            }

            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("points")) {
                    String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
                    pointsCommand((Player) sender, commandLabel, newArgs);
                } else if (args[0].equalsIgnoreCase("party")) {
                    String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
                    pl.getCommand("party").getExecutor().onCommand(sender, pl.getCommand("party"), commandLabel + " party", newArgs);
                }
            }


        }
        return true;
    }

    public void pointsCommand(Player sender, String cmdLabel, String[] args) {
        if (args.length == 0) {
            MessageHandler.sendMessage(sender, MessageHandler.formatInteger(MessageFile.getMessage("commands.points"), Stats.getStats(sender)
                    .getPoints()));

            if (sender.isOp()) {
                sendCommandUsage(sender, cmdLabel + " points");
            }
            return;
        }

        if (!sender.isOp()) {
            sender.sendMessage(MessageFile.formatMessage("commands.not-op"));
            return;
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(MessageHandler.format(MessageFile.getMessage("party.offline")));
                return;
            }
            sender.sendMessage(MessageHandler.formatInteger(MessageHandler.formatString(
                    MessageFile.getMessage("commands.other-player-points"), target.getName()), Stats.getStats(target).getPoints()));
        } else if (args.length == 3) {
            if (Bukkit.getPlayer(args[1]) == null) {
                sender.sendMessage(MessageHandler.format(MessageFile.getMessage("party.offline")));
                return;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (args[0].equalsIgnoreCase("add")) {
                int amount;
                try {
                    amount = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Please type number!");
                    return;
                }
                Stats.getStats(target).addPoints(amount);
                sender.sendMessage(c2 + "You've added " + c1 + "" + amount + c2 + " points for " + c1 + target.getName());
            } else if (args[0].equalsIgnoreCase("set")) {
                int amount;
                try {
                    amount = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Please type number!");
                    return;
                }
                Stats.getStats(target).setPoints(amount);
                sender.sendMessage(c2 + "You've set " + c1 + "" + amount + c2 + " points for " + c1 + target.getName());
            } else if (args[0].equalsIgnoreCase("withdraw")) {
                int amount;
                try {
                    amount = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Please type number!");
                    return;
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
            sendCommandUsage(sender, cmdLabel + " points");
        }


    }

    public Main getMainClass() {
        return pl;
    }
}