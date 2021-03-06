package com.drizzard.annihilationdw.managers;

import com.drizzard.annihilationdw.Main;
import com.drizzard.annihilationdw.files.ConfigFile;
import com.drizzard.annihilationdw.files.MapFile;
import com.drizzard.annihilationdw.handlers.GameMap;
import com.drizzard.annihilationdw.utils.Convert;
import com.drizzard.annihilationdw.utils.CopyWorld;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MapManager {

    public static String mapName = null;
    public static String mapsPath = null;
    public static String path = null;
    static List<GameMap> maps = new ArrayList<GameMap>();
    static List<GameMap> votableMaps = new ArrayList<GameMap>();
    private static GameMap activeMap = null;
    private static Main pl;

    public MapManager(Main plugin) {
        pl = plugin;
        setMaps();
    }

    private static void setMaps() {
        setMapName();
        File file = new File(mapsPath);
        String[] files = file.list();
        Bukkit.unloadWorld(mapName, false);
        deleteWorld(mapName);

        if (files.length == 0) {
            new WorldCreator(mapsPath + "Default Map").createWorld();
            files = file.list();
        }

        int id = 0;
        for (String str : files) {
            if ((new File(mapsPath + str + File.separator).isDirectory())) {
                Main.unloadWorld(mapsPath + str, true);
                MapFile.createConfig(mapsPath + str);
                maps.add(new GameMap(id, str));
                id++;
            }
        }

        pickRandomVotableMaps();
    }

    public static List<GameMap> getMaps() {
        return maps;
    }

    public static GameMap getMap(int id) {
        for (GameMap map : getMaps()) {
            if (map.getId() == id) {
                return map;
            }
        }
        return null;
    }

    public static GameMap getMap(String worldName) {
        for (GameMap map : getMaps()) {
            if (worldName.contains(map.getName())) {
                return map;
            }
        }
        return null;
    }

    public static GameMap getMapOfName(String mapName) {
        for (GameMap map : maps) {
            if (map.getName().equalsIgnoreCase(mapName)) {
                return map;
            }
        }
        return null;
    }

    public static GameMap chooseMap() {
        List<GameMap> mostVotes = new ArrayList<GameMap>();
        int max = maxVotes();
        for (GameMap map : getVotableMaps()) {
            if (map.getVotes() == max) {
                mostVotes.add(map);
            }
        }
        if (mostVotes.size() == 1) {
            setActiveMap(mostVotes.get(0));
            return mostVotes.get(0);
        } else {
            int id = Main.getRandom(0, mostVotes.size() - 1);
            setActiveMap(mostVotes.get(id));
            return mostVotes.get(id);
        }
    }

    public static void createWorld(GameMap map) {
        if (Bukkit.getWorld(mapName) != null)
            deleteWorld(mapName);

        Location loc;
        if (ConfigFile.config.getString("spawn-location") == null) {
            loc = Bukkit.getWorlds().get(0).getSpawnLocation();
        } else {
            loc = Convert.StringToLocation(ConfigFile.config.getString("spawn-location"), true, false);
        }

        if (Bukkit.getWorld("plugins/AnnihilationDW/Maps/" + map.getName()) != null) {
            if (Bukkit.getWorld("plugins/AnnihilationDW/Maps/" + map.getName()).getPlayers().size() > 0) {
                for (Player p : Bukkit.getWorld("plugins/AnnihilationDW/Maps/" + map.getName()).getPlayers()) {
                    p.teleport(loc);
                }
            }
        }

        if (Bukkit.getWorld("plugins/AnnihilationDW/Maps/" + map.getName()) != null) {
            Bukkit.getServer().unloadWorld("plugins/AnnihilationDW/Maps/" + map.getName(), true);
        }

        CopyWorld.copyWorld(new File("plugins/AnnihilationDW/Maps/" + map.getName()),
                new File(mapName));
        Bukkit.createWorld(new WorldCreator(mapName));
    }

    public static void deleteWorld(String map) {
        File file = new File(map);
        if (file.exists()) {
            if (Bukkit.getWorld(map) != null && Bukkit.getWorld(map).getPlayers() != null && Bukkit.getWorld(map).getPlayers().size() > 0) {
                Location loc;
                if (ConfigFile.config.getString("spawn-location") == null) {
                    loc = Bukkit.getWorlds().get(0).getSpawnLocation();
                } else {
                    loc = Convert.StringToLocation(ConfigFile.config.getString("spawn-location"), true, false);
                }
                for (Player p : Bukkit.getWorld(map).getPlayers()) {
                    p.teleport(loc);
                }
            }

            if (Bukkit.unloadWorld(map, false)) {
                Main.println("World unload successful.");
            } else {
                Main.println("World unload failed.");
            }

            for (int i = 0; i < 10; i++) {
                Main.println("Attempting to delete directory (" + (i + 1) + "/10).");
                if (Main.deleteDirectory(file)) {
                    Main.println("Directory deletion successful.");
                    break;
                } else {
                    Main.println("Directory deletion failed. Trying again...");
                }
            }
        }
    }

    public static int maxVotes() {
        int max = -1;
        for (GameMap map : getMaps()) {
            if (map.getVotes() > max) {
                max = map.getVotes();
            }
        }
        return max;
    }

    public static int amountOfMaps() {
        int amount = 0;
        File file = new File("plugins" + File.separator + "AnnihilationDW" + File.separator
                + "Maps" + File.separator);
        String[] files = file.list();
        for (String str : files) {
            if ((new File(path + "Maps" + File.separator + str + File.separator).isDirectory())) {
                amount++;
            }
        }

        return amount;
    }

    public static GameMap getActiveMap() {
        return activeMap;
    }

    public static void setActiveMap(GameMap activeMap) {
        MapManager.activeMap = activeMap;
    }

    public static void setMapName() {
        path = pl.getDataFolder() + File.separator;
        mapsPath = path + "Maps" + File.separator;
        mapName = path + "CurrentMap";
    }

    public static List<GameMap> getVotableMaps() {
        return votableMaps;
    }

    public static void pickRandomVotableMaps() {
        votableMaps.clear();

        Collections.shuffle(maps);

        if (maps.size() > 5) {
            votableMaps.addAll(maps.subList(0, 5));
        } else {
            votableMaps.addAll(maps);
        }
    }

    public Main getMainClass() {
        return pl;
    }
}