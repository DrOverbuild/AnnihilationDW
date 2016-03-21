package com.nekrosius.drizzardwars.managers;

import com.nekrosius.drizzardwars.Main;
import com.nekrosius.drizzardwars.commands.*;

public class CommandManager {
	
	private Main pl;
	public CommandManager(Main plugin) {
		pl = plugin;
		pl.getCommand("drizzardwars").setExecutor(new DWCommand(pl));
		VoteCommand voteCommand = new VoteCommand(pl);
		pl.getCommand("vote").setExecutor(voteCommand);
		pl.getCommand("vote").setTabCompleter(voteCommand);
		pl.getCommand("party").setExecutor(new PartyCommand(pl));
//		pl.getCommand("points").setExecutor(new PointsCommand(pl));
		pl.getCommand("help").setExecutor(new HelpCommand());
//		pl.getCommand("ability").setExecutor(new AbilityCommand(pl));
	}

	public Main getMainClass() {
		return pl;
	}
}