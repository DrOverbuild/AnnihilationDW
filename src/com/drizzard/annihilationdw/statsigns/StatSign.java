package com.drizzard.annihilationdw.statsigns;

import com.drizzard.annihilationdw.files.StatSignFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Sign;

import java.util.List;

public class StatSign {

	private final Location location;
	private final int top;
	private final StatSignFile.StatType statType;

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
