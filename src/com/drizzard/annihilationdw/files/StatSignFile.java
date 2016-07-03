package com.drizzard.annihilationdw.files;

import com.drizzard.annihilationdw.statsigns.StatSign;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class StatSignFile {

	private static File file = new File("plugins" + File.separator + "AnnihilationDW" + File.separator + "statsigns.yml");
	private static FileConfiguration config = YamlConfiguration.loadConfiguration(file);

	static {
		saveConfig();
	}

	public static void setStatSigns(List<StatSign> signs) {
		config.set("signs", Lists.transform(signs, new Function<StatSign, String>() {

			@Override
			public String apply(StatSign statSign) {
				Location loc = statSign.getLocation();
				return loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + ";" + statSign
						.getStatType() + ";" + statSign.getTop();
			}

		}));
		saveConfig();
	}

	public static List<StatSign> getStatSigns() {
		return Lists.transform(config.getStringList("signs"), new Function<String, StatSign>() {

			@Override
			public StatSign apply(String s) {
				String[] split = s.split(";");
				String[] locSplit = split[0].split(",");
				Location loc = new Location(Bukkit.getWorld(locSplit[0]), Integer.parseInt(locSplit[1]), Integer.parseInt(locSplit[2]), Integer
						.parseInt(locSplit[3]));
				StatType signType = StatType.valueOf(split[1]);
				int top = Integer.parseInt(split[2]);
				return new StatSign(loc, top, signType);
			}

		});
	}

	public static List<StatData> getStatData(StatType statType) {
		List<StatData> list = new ArrayList<>();

		if (config.isSet(statType.name().toLowerCase()) && config.isConfigurationSection(statType.name().toLowerCase())) {
			ConfigurationSection section = config.getConfigurationSection(statType.name().toLowerCase());
			for (String key : section.getKeys(false)) {
				try {
					UUID playerId = UUID.fromString(key);
					int value = section.getInt(key);
					list.add(new StatData(playerId, value));
				} catch (Exception ex) {
					System.out.println("Error loading stats, key: " + key + ", value: " + section.get(key));
				}
			}
		}

		list.sort(new Comparator<StatData>() {

			@Override
			public int compare(StatData o1, StatData o2) {
				return Integer.compare(o2.getAmount(), o1.getAmount());
			}
		});
		return list;
	}

	public static void update(StatType statType, UUID playerId, int value) {
		config.set(statType.name().toLowerCase() + "." + playerId.toString(), value);
		saveConfig();
	}

	private static void saveConfig() {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static class StatData {

		private final UUID playerId;
		private final int amount;

		public StatData(UUID playerId, int amount) {
			this.playerId = playerId;
			this.amount = amount;
		}

		public UUID getPlayerId() {
			return playerId;
		}

		public int getAmount() {
			return amount;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null || !obj.getClass().equals(this.getClass())) {
				return false;
			}
			StatData data = (StatData) obj;
			return ((playerId == null && data.playerId == null) || (playerId != null && data.playerId != null && playerId.equals(data.playerId)))
					&& amount == data.amount;
		}
	}

	public enum StatType {

		POINTS,
		KILLS,
		GAMES,
		WINS;

		public static StatType fromName(String name) {
			for (StatType statType : values()) {
				if (statType.name().equalsIgnoreCase(name)) {
					return statType;
				}
			}
			return null;
		}

	}

}
