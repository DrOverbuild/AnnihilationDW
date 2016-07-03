package com.drizzard.annihilationdw.statsigns;

import com.drizzard.annihilationdw.files.StatSignFile;
import com.drizzard.annihilationdw.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class StatSignManager extends BukkitRunnable {

	private static StatSignManager instance;

	public static StatSignManager getInstance() {
		return instance;
	}

	private final List<StatSign> statSigns = new ArrayList<>();

	public StatSignManager(Main plugin) {
		if (instance != null) {
			instance.cancel();
		}
		instance = this;

		statSigns.addAll(StatSignFile.getStatSigns());

		runTaskTimer(plugin, 0, 200);
	}

	public void addStatSign(StatSign sign) {
		statSigns.add(sign);
		StatSignFile.setStatSigns(statSigns);
	}

	public void removeStatSign(Location loc) {
		loc = new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		Iterator<StatSign> it = statSigns.iterator();
		while (it.hasNext()) {
			StatSign sign = it.next();
			if (sign.getLocation().toString().equals(loc.toString())) {
				it.remove();
			}
		}
		StatSignFile.setStatSigns(statSigns);
	}

	@Override
	public void run() {
		Iterator<StatSign> it = statSigns.iterator();
		boolean save = false;
		Map<StatSignFile.StatType, List<StatSignFile.StatData>> map = new HashMap<>();
		for (StatSignFile.StatType statType : StatSignFile.StatType.values()) {
			map.put(statType, StatSignFile.getStatData(statType));
		}
		while (it.hasNext()) {
			StatSign next = it.next();
			if (next.getLocation() != null && next.getStatType() != null && next.getTop() >= 0) {
				Block block = next.getLocation().getBlock();
				if (block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST) {
					next.update(map.get(next.getStatType()));
				} else {
					it.remove();
					save = true;
				}
			} else {
				it.remove();
				save = true;
			}
		}
		if (save) {
			StatSignFile.setStatSigns(statSigns);
		}
	}
}
