package com.nekrosius.drizzardwars.managers;

import com.nekrosius.drizzardwars.Main;
import com.nekrosius.drizzardwars.handlers.MessageHandler;
import com.nekrosius.drizzardwars.objects.Title;

public class TitleManager {
	
	Title phase;
	
	Main pl;
	public TitleManager(Main plugin) {
		pl = plugin;
		initializeTitles();
	}
	
	private void initializeTitles() {
		phase = new Title("PHASE");
	}
	
	public Title getPhaseTitle(int phase) {
		this.phase.setTitle(MessageHandler.getPhaseMessage(phase));
		return this.phase;
	}

}
