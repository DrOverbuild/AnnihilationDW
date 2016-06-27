package com.nekrosius.annihilationdw;


import com.nekrosius.annihilationdw.abilities.Archer;
import com.nekrosius.annihilationdw.abilities.Assassin;
import com.nekrosius.annihilationdw.abilities.Berserker;
import com.nekrosius.annihilationdw.abilities.Defender;
import com.nekrosius.annihilationdw.abilities.Enchanter;
import com.nekrosius.annihilationdw.database.Database;
import com.nekrosius.annihilationdw.database.flatfile.FlatFile;
import com.nekrosius.annihilationdw.database.mysql.MySQL;
import com.nekrosius.annihilationdw.files.ConfigFile;
import com.nekrosius.annihilationdw.handlers.Game;
import com.nekrosius.annihilationdw.handlers.Lobby;
import com.nekrosius.annihilationdw.handlers.MessageHandler;
import com.nekrosius.annihilationdw.handlers.PlayerHandler;
import com.nekrosius.annihilationdw.handlers.TabHandler;
import com.nekrosius.annihilationdw.managers.BarManager;
import com.nekrosius.annihilationdw.managers.CommandManager;
import com.nekrosius.annihilationdw.managers.FileManager;
import com.nekrosius.annihilationdw.managers.ListenerManager;
import com.nekrosius.annihilationdw.managers.MapManager;
import com.nekrosius.annihilationdw.managers.PartyManager;
import com.nekrosius.annihilationdw.managers.TeamManager;
import com.nekrosius.annihilationdw.statsigns.StatSignManager;
import com.nekrosius.annihilationdw.utils.AsyncUtil;
import com.nekrosius.annihilationdw.utils.Convert;
import de.slikey.effectlib.EffectLib;
import de.slikey.effectlib.EffectManager;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main extends JavaPlugin {

	// RECONNECT AND CAN VOTE AGAIN ON MAP
	// COLORED NAMES MUST

	private FileManager fm;
	private ListenerManager lm;
	private TeamManager tm;
	private MapManager mm;
	private CommandManager cm;
	private PartyManager pm;
	//	private TitleManager tlm;
	private static Database database;
	public static EffectManager em;

	public static void println(String s) {
		Bukkit.getPluginManager().getPlugin("AnnihilationDW").getLogger().info(s);
	}

	public void onEnable() {
		load();

		loadDefaultAbilities();
		new BukkitRunnable() {

			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					database.loadStats(p);
					Lobby.setupLobby(p);
					TabHandler.update(p);
					BarManager.removeBar(p);
				}
			}
		}.runTaskLater(this, 10L);

		switch (ConfigFile.config.getString("database.type", "MySQL").toLowerCase()) {
			case "flatfile":
			case "file":
				database = new FlatFile();
				break;
			default:
				database = new MySQL(ConfigFile.config.getString("database.mysql.host", "localhost"), ConfigFile.config.getInt("database.mysql"
						+ ".port", 3306), ConfigFile.config.getString("database.mysql.username", "user"), ConfigFile.config.getString("database"
						+ ".mysql.password", "password"), ConfigFile.config.getString("database.mysql.database", "database"));
				break;
		}
		database.connect();
		database.prepare();
	}

	public void onDisable() {
		if (Bukkit.getPluginManager().isPluginEnabled(this)) {
			if (Game.isGameStarted()) {
				Game.finish(TeamManager.getMostKills());
			}
		}
		database.disconnect();
		database = null;
		AsyncUtil.stop();
	}

	private void load() {
		fm = new FileManager(this);
		lm = new ListenerManager(this);
		mm = new MapManager(this);
		cm = new CommandManager(this);
		pm = new PartyManager(this);
		EffectLib lib = EffectLib.instance();
		em = new EffectManager(lib);
		new StatSignManager(this);
//		tlm = new TitleManager(this);
		MessageHandler.loadMessages();
		registerMultiplierPermissions();

		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
	}

	/**
	 * Adds default abilities
	 */
	private void loadDefaultAbilities() {
		new Archer();
		new Assassin();
		new Berserker();
		new Defender();
		new Enchanter();
	}

	public FileManager getFileManager() {
		return fm;
	}

	public ListenerManager getListenerManager() {
		return lm;
	}

	public TeamManager getTeamManager() {
		return tm;
	}

	public MapManager getMapManager() {
		return mm;
	}

	public CommandManager getCommandManager() {
		return cm;
	}

	public PartyManager getPartyManager() {
		return pm;
	}

	// Can't name it getDatabase because it's an existing method in the JavaPlugin class (or it's super classes) T_T
	public static Database getDatabaseImpl() {
		return database;
	}

	//	public TitleManager getTitleManager() {
//		return tlm;
//	}

	/**
	 * Not sure if this is really required, but could be helpful to some people... maybe...
	 */
	public void registerMultiplierPermissions() {
		int max = ConfigFile.config.getInt("max-points-multiplier", 10);
		for (int i = 1; i <= max; i++) {
			getServer().getPluginManager().addPermission(new Permission("dw.multiplier." + i, PermissionDefault.FALSE));
		}
	}

	public static int getRandom(int min, int max) {
		Random random = new Random();
		return random.nextInt((max - min) + 1) + min;
	}

	public static void unloadWorld(String world, boolean save) {
		if (Bukkit.getWorld(world) == null) {
			Bukkit.unloadWorld(world, save);
		} else if (Bukkit.getWorld(world).getPlayers().size() > 0) {
			Location loc;
			if (ConfigFile.config.getString("spawn-location") == null) {
				loc = Bukkit.getWorlds().get(0).getSpawnLocation();
			} else {
				loc = Convert.StringToLocation(ConfigFile.config.getString("spawn-location"), true, false);
			}
			for (Player p : Bukkit.getWorld(world).getPlayers()) {
				p.teleport(loc);
			}
		}
		Bukkit.unloadWorld(world, save);
	}

	public static boolean deleteDirectory(File path) {
		try {
			FileUtils.deleteDirectory(path);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

//		if( path.exists() ) {
//			File[] files = path.listFiles();
//			for(int i=0; i<files.length; i++) {
//			if(files[i].isDirectory()) {
//				deleteDirectory(files[i]);
//			}
//			else {
//				files[i].delete();
//				}
//			}
//		}
//		return( path.delete() );
	}

	public static boolean contains(List<String> list, String str) {
		for (String st : list) {
			if (st.startsWith(str)) {
				return true;
			}
		}
		return false;
	}

	public static int containsId(List<String> list, String str) {
		int i = 0;
		for (String st : list) {
			if (st.startsWith(str)) {
				return i;
			}
			i++;
		}
		return -1;
	}

	public static BlockFace getPlayerDirection(Player player) {
		BlockFace dir = null;
		float y = player.getLocation().getYaw();
		if (y < 0) {
			y += 360;
		}
		y %= 360;
		int i = (int) ((y + 8) / 22.5);
		if (i == 0) {
			dir = BlockFace.SOUTH;
		} else if (i == 1) {
			dir = BlockFace.SOUTH_SOUTH_WEST;
		} else if (i == 2) {
			dir = BlockFace.SOUTH_WEST;
		} else if (i == 3) {
			dir = BlockFace.NORTH_NORTH_WEST;
		} else if (i == 4) {
			dir = BlockFace.WEST;
		} else if (i == 5) {
			dir = BlockFace.WEST_NORTH_WEST;
		} else if (i == 6) {
			dir = BlockFace.NORTH_WEST;
		} else if (i == 7) {
			dir = BlockFace.NORTH_NORTH_WEST;
		} else if (i == 8) {
			dir = BlockFace.NORTH;
		} else if (i == 9) {
			dir = BlockFace.NORTH_NORTH_EAST;
		} else if (i == 10) {
			dir = BlockFace.NORTH_EAST;
		} else if (i == 11) {
			dir = BlockFace.EAST_NORTH_EAST;
		} else if (i == 12) {
			dir = BlockFace.EAST;
		} else if (i == 13) {
			dir = BlockFace.EAST_SOUTH_EAST;
		} else if (i == 14) {
			dir = BlockFace.SOUTH_EAST;
		} else if (i == 15) {
			dir = BlockFace.SOUTH_SOUTH_EAST;
		} else {
			dir = BlockFace.SOUTH;
		}
		return dir;
	}

	public static int getLastIndex(String str) {
		for (int i = 0; i < str.length() - 1; i++) {
			if (!isNumber(str.charAt(i))) {
				return i;
			}
		}
		return 0;
	}

	/**
	 * @param symbol
	 * @return returns if given symbol is a number
	 */
	private static boolean isNumber(char symbol) {
		if (symbol == '0') {
			return true;
		} else if (symbol == '1') {
			return true;
		} else if (symbol == '2') {
			return true;
		} else if (symbol == '3') {
			return true;
		} else if (symbol == '5') {
			return true;
		} else if (symbol == '6') {
			return true;
		} else if (symbol == '7') {
			return true;
		} else if (symbol == '8') {
			return true;
		} else if (symbol == '9') {
			return true;
		}
		return false;
	}

	/**
	 * Returns all players on the server who is not in spectator mode.
	 *
	 * @return
	 */
	public static List<Player> getAlivePlayers() {
		List<Player> alivePlayers = new ArrayList<>();
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (!PlayerHandler.isSpectating(p)) {
				alivePlayers.add(p);
			}
		}
		return alivePlayers;
	}
}