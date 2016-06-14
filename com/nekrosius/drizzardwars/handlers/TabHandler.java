package com.nekrosius.drizzardwars.handlers;

import com.nekrosius.drizzardwars.Main;
import com.nekrosius.drizzardwars.files.MessageFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class TabHandler {
	
	
	//private static Main plugin = (Main)Bukkit.getPluginManager().getPlugin("AnnihilationDW");
	
	//public static String pts;
	
	//Temp solution

	/**
	 * All this method does is, if the player is spectating, change the list name to ChatColor.gray + player.getName().
	 * If the player already has a team, then his nametag and his name on the tab list is already the color of his team.
	 * @param player
	 */
	public static void setColor(Player player){
		// We don't need to do any team managing here if players can't see the main scoreboard.

		String name = player.getName();

		if(PlayerHandler.isSpectating(player)){
			if(player.getName().length() >= 14){
				name = player.getName().substring(0, 14);
			}
			ChatColor color = ChatColor.GRAY;
			player.setPlayerListName(color+name);
			Main.println("Setting " + player.getName() + "'s tab color to Gray because he is spectating");
		}else{
			Main.println("Setting " + player.getName() + "'s tab color to his team's color because he is not spectating.");
			player.setPlayerListName(player.getName());
		}

//		if(Game.isGameStarted()){
//			if(!TeamManager.hasTeam(player)){
//				color = ChatColor.GREEN;
//			}else if(PlayerHandler.isSpectating(player)){
//				color = ChatColor.GRAY;
//			}else{
//				color = TeamManager.getTeam(player).getColor();
//			}
//			player.setPlayerListName(color + name);
//		}else{
//			player.setPlayerListName(color + name);
//		}
	}
	
	public static void updateAll(){
		/*
		for(Player p : Bukkit.getOnlinePlayers()){
			update(p);
		}
		*/
	}
	
	public static void update(Player player) {
		/*
		TabAPI.resetTabList(player);
		Team team = TeamManager.getTeam(player);
		TabAPI.setTabString(plugin, player, 0, 0, ChatColor.GOLD   + "------------");
		ChatColor color;
		if(PlayerHandler.isSpectating(player)) color = ChatColor.LIGHT_PURPLE;
		else if(Game.isGameStarted()) color = team.getColor();
		else color = ChatColor.AQUA;
		TabAPI.setTabString(plugin, player, 0, 1,          color   + player.getName());
		TabAPI.setTabString(plugin, player, 0, 2, ChatColor.GOLD   + "------------" + TabAPI.nextNull());
		TabAPI.setTabString(plugin, player, 1, 0, ChatColor.DARK_PURPLE    + "" + ChatColor.BOLD + "Drizzard");
		TabAPI.setTabString(plugin, player, 1, 1, ChatColor.YELLOW + "------------");
		TabAPI.setTabString(plugin, player, 1, 2, ChatColor.DARK_PURPLE    + "" + ChatColor.BOLD + "Wars");
		TabAPI.setTabString(plugin, player, 2, 0, ChatColor.GOLD   + "------------" + TabAPI.nextNull());
		TabAPI.setTabString(plugin, player, 2, 1, color  + "" + Points.getPoints(player) + " " + pts + TabAPI.nextNull());
		TabAPI.setTabString(plugin, player, 2, 2, ChatColor.GOLD   + "------------" + TabAPI.nextNull());
		fillPlayers(player);
		TabAPI.updatePlayer(player);
		*/
	}
	
	/*
	private static void fillPlayers(Player player) {
		boolean started = Game.isGameStarted();
		ChatColor color = ChatColor.GRAY;
		int playerId = 0;
		List<Player> players = getPlayerList();
		for(int x = 3; x < TabAPI.getVertSize(); x++){
			for(int y = 0; y < TabAPI.getHorizSize(); y++){
				if(playerId + 1 >= players.size()){
					return;
				}
				if(started){
					if(!TeamManager.hasTeam(players.get(playerId))){
						color = ChatColor.GRAY;
					}else if(PlayerHandler.isSpectating(players.get(playerId))){
						color = ChatColor.GRAY;
					}else{
						color = TeamManager.getTeam(players.get(playerId)).getColor();
					}
				}
				TabAPI.setTabString(plugin, player, x, y, color + players.get(playerId).getName());
				playerId++;
			}
		}
	}

	private static List<Player> getPlayerList(){
		List<Player> players = new ArrayList<Player>();
		for(Player p : Bukkit.getOnlinePlayers()){
			players.add(p);
		}
		return players;
	}
	*/
	
}