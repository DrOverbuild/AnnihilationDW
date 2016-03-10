package com.nekrosius.drizzardwars.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;

import com.nekrosius.drizzardwars.managers.MapManager;

/*
 * Util by BENAS "BENRUSH" NEKRO�IUS
 * Converts
 */

public class Convert {
	
	public static Location StringToLocation(String str, boolean YawAndPitch, boolean gameWorld) {
		String[] st = str.split(",");
		Location loc;
		if(gameWorld){
			double x = Double.parseDouble(st[0]),
					y = Double.parseDouble(st[1]),
					z = Double.parseDouble(st[2]);
			loc = new Location(Bukkit.getWorld(MapManager.mapName), x, y, z);
			if(YawAndPitch){
				float yaw = Float.parseFloat(st[3]),
						pitch = Float.parseFloat(st[4]);
				loc.setYaw(yaw);
				loc.setPitch(pitch);
			}
		}else{
			World world = Bukkit.getWorld(st[0]);
			double x = Double.parseDouble(st[1]),
					y = Double.parseDouble(st[2]),
					z = Double.parseDouble(st[3]);
			loc = new Location(world, x, y, z);
			if(YawAndPitch){
				float yaw = Float.parseFloat(st[4]),
						pitch = Float.parseFloat(st[5]);
				loc.setYaw(yaw);
				loc.setPitch(pitch);
			}
		}
		return loc;
	}

//	public static int StringToTime(String str) {
//		String[] st = str.split(" ");
//		int time = 0;
//		try{
//			time = Integer.parseInt(st[0]);
//		}catch(NumberFormatException e){
//			e.printStackTrace();
//		}
//		time *= 20;
//		String format = st[1];
//		if(format.toLowerCase().startsWith("s")){
//			time *= 1;
//		}
//		else if(format.toLowerCase().startsWith("m")){
//			time *= 60;
//		}
//		else if(format.toLowerCase().startsWith("h")){
//			time *= 3600;
//		}
//		return time;
//	}

	public static int StringToTime(String str){
		String[] times = str.split(":");
		int hours = 0;
		int minutes = 0;
		int seconds = 0;
		int index = 0;

		if(times.length > 2){
			try{
				hours = Integer.parseInt(times[index]);
				index++;
			}catch (NumberFormatException e){}
		}

		if(times.length > 1){
			try{
				minutes = Integer.parseInt(times[index]);
				index++;
			}catch (NumberFormatException e){}
		}

		if(times.length > 0){
			try{
				seconds = Integer.parseInt(times[index]);
			}catch (NumberFormatException e){}
		}
		return  hours*72000 + minutes*1200 + seconds*20;
	}

	public static String ticksToReadableTimeFormat(int i){
		StringBuilder str = new StringBuilder("");
		if(i > 72000){
			str.append(i / 72000).append(" hours, ");
			i = i%72000;
		}

		if(i > 1200){
			str.append(i / 1200).append(" minutes, ");
			i = i % 1200;
		}

		if(i > 20){
			str.append(i / 20).append(" seconds");
		}

		return str.toString();
	}

	public static ChatColor StringToChatColor(String str) {
		if(str.equalsIgnoreCase("aqua")) return ChatColor.AQUA;
		else if(str.equalsIgnoreCase("black")) return ChatColor.BLACK;
		else if(str.equalsIgnoreCase("blue")) return ChatColor.BLUE;
		else if(str.equalsIgnoreCase("dark_blue")) return ChatColor.DARK_BLUE;
		else if(str.equalsIgnoreCase("dark_gray")) return ChatColor.DARK_GRAY;
		else if(str.equalsIgnoreCase("dark_green")) return ChatColor.DARK_GREEN;
		else if(str.equalsIgnoreCase("dark_purple")) return ChatColor.DARK_PURPLE;
		else if(str.equalsIgnoreCase("dark_red")) return ChatColor.DARK_RED;
		else if(str.equalsIgnoreCase("gold")) return ChatColor.GOLD;
		else if(str.equalsIgnoreCase("gray")) return ChatColor.GRAY;
		else if(str.equalsIgnoreCase("green")) return ChatColor.GREEN;
		else if(str.equalsIgnoreCase("light_purple")) return ChatColor.LIGHT_PURPLE;
		else if(str.equalsIgnoreCase("red")) return ChatColor.RED;
		else if(str.equalsIgnoreCase("white")) return ChatColor.WHITE;
		else if(str.equalsIgnoreCase("yellow")) return ChatColor.YELLOW;
		return ChatColor.WHITE;
	}
	
	public static String getOrdinalFor(int number){
		if(number >= 10 && number <= 20) return "th";
		int remainder = number % 10;
		if(remainder == 1){
			return "st";
		}else if(remainder == 2){
			return "nd";
		}else if(remainder == 3){
			return "rd";
		}else{
			return "th";
		}
	}
	
	public static boolean equals(Location loc1, Location loc2) {
		if(loc1.getBlockX() == loc2.getBlockX()
				&& loc1.getBlockY() == loc2.getBlockY()
				&& loc1.getBlockZ() == loc2.getBlockZ()) return true;
		return false;
	}
	
	public static Color ChatColorToColor(ChatColor color) {
		if(color.equals(ChatColor.AQUA)) return Color.AQUA;
		else if(color.equals(ChatColor.BLACK)) return Color.BLACK;
		else if(color.equals(ChatColor.BLUE)) return Color.NAVY;
		else if(color.equals(ChatColor.DARK_BLUE)) return Color.BLUE;
		else if(color.equals(ChatColor.DARK_GRAY)) return Color.SILVER;
		else if(color.equals(ChatColor.DARK_GREEN)) return Color.GREEN;
		else if(color.equals(ChatColor.DARK_PURPLE)) return Color.PURPLE;
		else if(color.equals(ChatColor.DARK_RED)) return Color.MAROON;
		else if(color.equals(ChatColor.GOLD)) return Color.ORANGE;
		else if(color.equals(ChatColor.GRAY)) return Color.GRAY;
		else if(color.equals(ChatColor.GREEN)) return Color.LIME;
		else if(color.equals(ChatColor.LIGHT_PURPLE)) return Color.FUCHSIA;
		else if(color.equals(ChatColor.RED)) return Color.RED;
		else if(color.equals(ChatColor.WHITE)) return Color.WHITE;
		else if(color.equals(ChatColor.YELLOW)) return Color.YELLOW;
		return Color.GRAY;
	}
	
	public static int ChatColorToInt(ChatColor color) {
		if(color.equals(ChatColor.AQUA)) return 9;
		if(color.equals(ChatColor.BLACK)) return 15;
		else if(color.equals(ChatColor.BLUE)) return 3;
		else if(color.equals(ChatColor.DARK_BLUE)) return 11;
		else if(color.equals(ChatColor.DARK_GRAY)) return 7;
		else if(color.equals(ChatColor.DARK_GREEN)) return 13;
		else if(color.equals(ChatColor.DARK_PURPLE)) return 10;
		else if(color.equals(ChatColor.DARK_RED)) return 14;
		else if(color.equals(ChatColor.GOLD)) return 1;
		else if(color.equals(ChatColor.GRAY)) return 8;
		else if(color.equals(ChatColor.GREEN)) return 5;
		else if(color.equals(ChatColor.LIGHT_PURPLE)) return 2;
		else if(color.equals(ChatColor.RED)) return 6;
		else if(color.equals(ChatColor.WHITE)) return 0;
		else if(color.equals(ChatColor.YELLOW)) return 4;
		return 0;
	}
	
	public static ChatColor IntToCharColor(int color) {
		if(color == 9) return ChatColor.AQUA;
		else if(color == 15) return ChatColor.BLACK;
		else if(color == 11) return ChatColor.DARK_BLUE;
		else if(color == 7) return ChatColor.DARK_GRAY;
		else if(color == 13) return ChatColor.DARK_GREEN;
		else if(color == 10) return ChatColor.DARK_PURPLE;
		else if(color == 14) return ChatColor.DARK_RED;
		else if(color == 1) return ChatColor.GOLD;
		else if(color == 8) return ChatColor.GRAY;
		else if(color == 5) return ChatColor.GREEN;
		else if(color == 2) return ChatColor.LIGHT_PURPLE;
		else if(color == 6) return ChatColor.RED;
		else if(color == 0) return ChatColor.WHITE;
		else if(color == 4) return ChatColor.YELLOW;
		return ChatColor.WHITE;
	}
}