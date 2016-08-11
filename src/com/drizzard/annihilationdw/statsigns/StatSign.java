package com.drizzard.annihilationdw.statsigns;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;

import com.drizzard.annihilationdw.files.StatSignFile;

public class StatSign {

    private final Location location;
    private final StatSignFile.StatType statType;
    private final int top;

    public StatSign(Location location, int top, StatSignFile.StatType statType) {
        this.location = new Location(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
        this.top = top;
        this.statType = statType;
    }

    public Location getLocation() {
        return location;
    }

    public int getTop() {
        return top;
    }

    public StatSignFile.StatType getStatType() {
        return statType;
    }

    public void update(List<StatSignFile.StatData> dataList) {
        Sign sign = (Sign) location.getBlock().getState();
        StatSignFile.StatData statData = top < dataList.size() ? dataList.get(top) : null;

        sign.setLine(0, ChatColor.GREEN + "#" + (top + 1) + ChatColor.BLACK + " " + statType.name().toLowerCase());
        if (statData != null) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(statData.getPlayerId());
            sign.setLine(1, ChatColor.GREEN + offlinePlayer.getName());
            sign.setLine(2, ChatColor.GREEN.toString() + statData.getAmount() + " " + ChatColor.BLACK + statType.name().toLowerCase());
        } else {
            sign.setLine(1, ChatColor.DARK_RED + "N/A");
            sign.setLine(2, ChatColor.DARK_RED + "N/A");
        }
        sign.update();
        
        // Player heads -- added by Zach Sents
        org.bukkit.material.Sign materialSign = (org.bukkit.material.Sign) sign.getData();
        Block testPoint = sign.getBlock().getRelative(materialSign.getAttachedFace());
        
        for(int i = -1; i <= 1; i++) {
        	if(i == 0) continue;
        	
        	BlockState sample = testPoint.getRelative(0, i, 0).getState();
        	
        	if(sample instanceof Skull) {
        		Skull skull = (Skull) sample;
        		skull.setSkullType(SkullType.PLAYER);

        		if(statData != null) {
        			OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(statData.getPlayerId());
        			skull.setOwner(offlinePlayer.getName());
        		}
        		skull.update();
        	}
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        StatSign other = (StatSign) obj;
        return ((location == null && other.location == null) || (location != null && other.location != null && location.equals(other.location))) &&
                (top == other.top) && ((statType == other.statType));
    }


}
