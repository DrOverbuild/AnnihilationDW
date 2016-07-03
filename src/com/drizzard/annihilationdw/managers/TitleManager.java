package com.drizzard.annihilationdw.managers;

import com.connorlinfoot.titleapi.TitleAPI;
import com.drizzard.annihilationdw.handlers.MessageHandler;
import org.bukkit.entity.Player;

public class TitleManager {
	
//	Title phase;
//
//	Main pl;
//	public TitleManager(Main plugin) {
//		pl = plugin;
//		initializeTitles();
//	}
//
//	private void initializeTitles() {
//		phase = new Title("PHASE");
//	}
//
//	public Title getPhaseTitle(int phase) {
//		this.phase.setTitle(MessageHandler.getPhaseMessage(phase));
//		return this.phase;
//	}

	public static void sendTitle(Player p, int phase){
		if(p.getServer().getPluginManager().getPlugin("TitleAPI") != null){
			TitleAPI.sendTitle(p,10,60,10,MessageHandler.getPhaseMessage(phase),null);
		}
	}

}
