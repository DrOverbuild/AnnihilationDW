package com.drizzard.annihilationdw.managers;

import com.drizzard.annihilationdw.Main;
import com.drizzard.annihilationdw.files.ConfigFile;
import com.drizzard.annihilationdw.files.KitsFile;
import com.drizzard.annihilationdw.files.MapFile;
import com.drizzard.annihilationdw.files.MessageFile;
import com.drizzard.annihilationdw.files.ShopFile;
import com.drizzard.annihilationdw.files.TeamsFile;

public class FileManager {


    private Main pl;

    public FileManager(Main plugin) {
        this.pl = plugin;

        new MessageFile(pl);
        new MapFile(pl);
        new ConfigFile(pl);
        new KitsFile(pl);
        new ShopFile(pl);
        new TeamsFile(pl);
        //TabHandler.pts = MessageFile.getMessage("general.points");
    }


    public Main getMainClass() {
        return pl;
    }
}