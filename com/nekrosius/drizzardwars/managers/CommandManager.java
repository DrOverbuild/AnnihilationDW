package com.nekrosius.drizzardwars.managers;

import com.nekrosius.drizzardwars.Main;
import com.nekrosius.drizzardwars.commands.*;

public class CommandManager {
	
	private Main pl;
	public CommandManager(Main plugin) {
		pl = plugin;
		pl.getCommand("drizzardwars").setExecutor(new DWCommand(pl));
		pl.getCommand("vote").setExecutor(new VoteCommand(pl));
		pl.getCommand("party").setExecutor(new PartyCommand(pl));
		pl.getCommand("points").setExecutor(new PointsCommand(pl));
		pl.getCommand("help").setExecutor(new HelpCommand());
	}

	public Main getMainClass() {
		return pl;
	}
}