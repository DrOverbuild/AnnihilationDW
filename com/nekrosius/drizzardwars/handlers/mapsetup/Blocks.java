package com.nekrosius.drizzardwars.handlers.mapsetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.nekrosius.drizzardwars.Main;
import com.nekrosius.drizzardwars.files.ConfigFile;
import com.nekrosius.drizzardwars.files.MapFile;
import com.nekrosius.drizzardwars.handlers.Maps;
import com.nekrosius.drizzardwars.handlers.Team;
import com.nekrosius.drizzardwars.managers.MapManager;
import com.nekrosius.drizzardwars.managers.TeamManager;
import com.nekrosius.drizzardwars.utils.Convert;

public class Blocks {
	
	//REGENERATING -- MAP SETUP
	static Map<String, Integer> regeneratingBlockStatus = new HashMap<String, Integer>();
	static Map<String, Block> regeneratingBlock = new HashMap<String, Block>();
	static Map<String, List<String>> regeneratingBlockInfo = new HashMap<String, List<String>>();
	
	//UNPLACEABLE -- MAP SETUP
	static Map<String, Integer> unplaceableBlockStatus = new HashMap<String, Integer>();
	static Map<String, Block> unplaceableBlock = new HashMap<String, Block>();
	private static Map<Material, Boolean> unplaceable = new HashMap<Material, Boolean>();
	private static Map<Material, Integer> unplaceableData = new HashMap<Material, Integer>();
	
	//DIAMONDS -- MAP SETUP
	private static List<Location> diamondSpawns = new ArrayList<Location>();
	
	//TEAM AMOUNT -- MAP SETUP
	static Map<String, Integer> teamAmountStatus = new HashMap<String, Integer>();
	
	//INFO
	private static Map<Material, Integer> data = new HashMap<Material, Integer>();
	private static Map<Material, String> dropType = new HashMap<Material, String>();
	private static Map<Material, Material> brokeBlock = new HashMap<Material, Material>();
	private static Map<Material, ItemStack> reward = new HashMap<Material, ItemStack>();
	private static Map<Material, String> rewardAmount = new HashMap<Material, String>();
	private static Map<Material, Integer> timer = new HashMap<Material, Integer>();
	private static Map<Material, Integer> xpReward = new HashMap<Material, Integer>();
	
	public static void setupBlocks(Maps map) {
		MapFile.createConfig(MapManager.mapsPath + map.getName());
		// REGENERATING
		List<String> blocks;
		String args[];
		if(MapFile.config.getStringList("blocks.regenerating") != null) {
			blocks = MapFile.config.getStringList("blocks.regenerating");
			int id = 0;
			for(String str : blocks) {
				args = str.split(",");
				Material material = Material.getMaterial(args[0]);
				int data = 0;
				boolean toFix = false;
				try{
					data = Integer.parseInt(args[1]);
				}catch(NumberFormatException e) {
					data = Integer.parseInt(args[1].substring(0, Main.getLastIndex(args[1])));
					toFix = true;
					if(toFix){
						String regeneratingBlock = "";
						regeneratingBlock += args[0] + "," + data + "," + args[1].substring(Main.getLastIndex(args[1])) + ",";
						for(int i = 2; i < 7; i++){
							regeneratingBlock += args[i];
							if(i != 6)
								regeneratingBlock += ",";
						}
						blocks.set(id, regeneratingBlock);
						MapFile.set("blocks.regenerating", blocks);
						MapFile.saveConfig();
						args = regeneratingBlock.split(",");
					}
				}
				String dropType = args[2];
				int timer = Integer.parseInt(args[3]);
				Material destroyedBlock = Material.getMaterial(args[4]);
				String[] rew = args[5].split(":");
				String rewardAmount = args[6];
				setRewardAmount(material, rewardAmount);
				@SuppressWarnings("deprecation")
				ItemStack reward = new ItemStack(Material.getMaterial(Integer.parseInt(rew[0])), getRewardAmount(material), Short.parseShort(rew[1]));
				int xpReward = Integer.parseInt(args[7]);
				setData(material, data);
				setDropType(material, dropType);
				setBrokeBlock(material, destroyedBlock);
				setReward(material, reward);
				setTimer(material, timer);
				setXpReward(material, xpReward);
				id++;
			}
		}
		// UNBREAKALBE
		if(MapFile.config.getStringList("blocks.unbreakable") != null) {
			blocks = MapFile.config.getStringList("blocks.unbreakable");
			for(String str : blocks) {
				args = str.split(",");
				Material m = Material.getMaterial(args[0]);
				int data = Integer.parseInt(args[1]);
				setData(m, data);
			}
		}
		// UNPLACEABLE
		if(MapFile.config.getStringList("blocks.unplaceable") != null) {
			blocks = MapFile.config.getStringList("blocks.unplaceable");
			for(String str : blocks) {
				args = str.split(",");
				Material m = Material.getMaterial(args[0]);
				int data = Integer.parseInt(args[1]);
				setUnplaceable(m, true);
				setUnplaceableData(m, data);
			}
		}
		// DIAMOND SPAWNS
		if(MapFile.config.getStringList("blocks.diamonds") != null) {
			blocks = MapFile.config.getStringList("blocks.diamonds");
			for(String str : blocks) {
				Location loc = Convert.StringToLocation(str, false, true);
				addDiamondSpawn(loc);
			}
			despawnDiamonds();
		}
		// NEXUS
		for(Team team : TeamManager.getTeams()) {
			if(MapFile.config.getString("team." + team.getCodeName() + ".nexus") == null) continue;
			Location nexusLoc = Convert.StringToLocation(MapFile.config.getString("team." + team.getCodeName() + ".nexus"), false, true);
			nexusLoc.getWorld().getBlockAt(nexusLoc).setType(Material.ENDER_STONE);
			team.setNexusLocation(nexusLoc);
			team.setNexusHealth(ConfigFile.config.getInt("nexus-health"));
		}
	}
	
	//---------------------REGENERATING----------------------------//
	
	public static int getRegeneratingBlockStatus(Player player) {
		if(regeneratingBlockStatus.get(player.getName()) != null) return regeneratingBlockStatus.get(player.getName());
		return 0;
	}

	public static void setRegeneratingBlockStatus(Player player, Integer status) {
		if(status == null) regeneratingBlockStatus.remove(player.getName());
		else regeneratingBlockStatus.put(player.getName(), status);
	}
	
	public static Block getRegeneratingBlock(Player player) {
		if(regeneratingBlock.get(player.getName()) != null) return regeneratingBlock.get(player.getName());
		return null;
	}
	
	public static void setRegeneratingBlock(Player player, Block block) {
		if(block == null) regeneratingBlock.remove(player.getName());
		else regeneratingBlock.put(player.getName(), block);
	}
	
	public static void addRegeneratingBlockInfo(Player player, String info){
		List<String> infoList = regeneratingBlockInfo.get(player.getName());
		infoList.add(info);
		regeneratingBlockInfo.put(player.getName(), infoList);
	}
	
	public static String getRegeneratingBlockInfo(Player player, int index){
		return regeneratingBlockInfo.get(player.getName()).get(index);
	}
	
	public static List<String> getRegeneratingBlockInfoList(Player player){
		return regeneratingBlockInfo.get(player.getName());
	}
	
	public static void removeRegeneratingBlockInfo(Player player, int index){
		List<String> infoList = regeneratingBlockInfo.get(player.getName());
		infoList.remove(index - 1);
		regeneratingBlockInfo.put(player.getName(), infoList);
	}
	
	public static void deleteRegeneratingBlockInfo(Player player){
		regeneratingBlockInfo.put(player.getName(), new ArrayList<String>());
	}
	
	//---------------------UNPLACEABLE-----------------------------//
	
	public static void setUnplaceableBlockStatus(Player player, int status) {
		if(status == 0) unplaceableBlockStatus.remove(player.getName());
		unplaceableBlockStatus.put(player.getName(), status);
	}
	
	public static int getUnplaceableBlockStatus(Player player) {
		if(unplaceableBlockStatus.get(player.getName()) == null) return 0;
		return unplaceableBlockStatus.get(player.getName());
	}
	
	public static Block getUnplaceableBlock(Player player) {
		if(unplaceableBlock.get(player.getName()) != null) return unplaceableBlock.get(player.getName());
		return null;
	}
	
	public static void setUnplaceableBlock(Player player, Block block) {
		if(block == null) unplaceableBlock.remove(player.getName());
		else unplaceableBlock.put(player.getName(), block);
	}
	
	public static Boolean isUnplaceable(Material material) {
		return unplaceable.get(material);
	}
	
	public static void setUnplaceable(Material material, boolean bool) {
		unplaceable.put(material, bool);
	}
	
	public static Integer getUnplaceableData(Material material) {
		return unplaceableData.get(material);
	}
	
	public static void setUnplaceableData(Material material, int data) {
		unplaceableData.put(material, data);
	}
	
	//---------------------DIAMONDS--------------------------------//
	
	public static void addDiamondSpawn(Location loc) {
		diamondSpawns.add(loc);
	}
	
	public static void removeDiamondSpawn(Location loc) {
		if(diamondSpawns.contains(loc)) diamondSpawns.remove(loc);
	}
	
	public static boolean isDiamondSpawn(Location loc) {
		if(diamondSpawns.contains(loc)) return true;
		return false;
	}
	
	public static List<Location> getDiamondSpawns() {
		return diamondSpawns;
	}
	
	public static void spawnDiamonds() {
		for(Location loc : getDiamondSpawns()) {
			loc.getWorld().getBlockAt(loc).setType(Material.DIAMOND_ORE);
		}
	}
	
	public static void despawnDiamonds() {
		for(Location loc : getDiamondSpawns()) {
			loc.getWorld().getBlockAt(loc).setType(Material.AIR);
		}
	}
	
	//---------------------TEAM AMOUNT-----------------------------//
	
	public static void setTeamAmountStatus(Player player, int status) {
		if(status == 0) teamAmountStatus.remove(player.getName());
		else teamAmountStatus.put(player.getName(), status);
	}
	
	public static int getTeamAmountStatus(Player player) {
		if(teamAmountStatus.get(player.getName()) != null) return teamAmountStatus.get(player.getName());
		return 0;
	}
	
	
	//-------------------------------------------------------------//

	public static boolean blockRegenerates(Material material){
		return dropType.containsKey(material);
	}

	public static Integer getData(Material material) {
		return data.get(material);
	}
	
	public static void setData(Material material, int data) {
		Blocks.data.put(material, data);
	}
	
	public static String getDropType(Material material) {
		return dropType.get(material);
	}
	
	public static void setDropType(Material material, String type) {
		dropType.put(material, type);
	}
	
	public static Material getBrokeBlock(Material material) {
		return brokeBlock.get(material);
	}
	
	public static void setBrokeBlock(Material material, Material type) {
		brokeBlock.put(material, type);
	}
	
	public static ItemStack getReward(Material material) {
		return reward.get(material);
	}
	
	public static void setReward(Material material, ItemStack type) {
		reward.put(material, type);
	}
	
	public static int getRewardAmount(Material material) {
		String amount = rewardAmount.get(material);
		try{
			return Integer.parseInt(amount);
		}catch(NumberFormatException e){
			String[] numbers = amount.split(":");
			int min = Integer.parseInt(numbers[0]);
			int max = Integer.parseInt(numbers[1]);
			return Main.getRandom(min, max);
		}
	}
	
	public static void setRewardAmount(Material material, String amount) {
		rewardAmount.put(material, amount);
	}
	
	public static int getTimer(Material material) {
		return timer.get(material);
	}
	
	public static void setTimer(Material material, int type) {
		timer.put(material, type);
	}
	
	public static int getXpReward(Material material) {
		return xpReward.get(material);
	}
	
	public static void setXpReward(Material material, int type) {
		xpReward.put(material, type);
	}
	
	
	@SuppressWarnings("deprecation")
	public static void setBlockDiretion(Player player, Block block){
		// 2 - NORTH
		// 3 - SOUTH
		// 4 - WEST
		// 5 - EAST
        double rotation = (player.getLocation().getYaw() - 90) % 360;
        int data;
        if (rotation < 0) {
            rotation += 360.0;
        }
        if (0 <= rotation && rotation < 22.5) {
            data = 5;
        } else if (22.5 <= rotation && rotation < 67.5) {
            data = 3;
        } else if (67.5 <= rotation && rotation < 112.5) {
            data = 3;
        } else if (112.5 <= rotation && rotation < 157.5) {
            data = 3;
        } else if (157.5 <= rotation && rotation < 202.5) {
            data = 4;
        } else if (202.5 <= rotation && rotation < 247.5) {
            data = 2;
        } else if (247.5 <= rotation && rotation < 292.5) {
            data = 2;
        } else if (292.5 <= rotation && rotation < 337.5) {
            data = 2;
        } else if (337.5 <= rotation && rotation < 360.0) {
            data = 5;
        } else {
        	data = 2;
        }
        block.setData((byte) data);
	}
}