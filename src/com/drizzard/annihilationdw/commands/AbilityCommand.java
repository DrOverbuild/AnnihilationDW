package com.drizzard.annihilationdw.commands;

import com.drizzard.annihilationdw.Main;
import com.drizzard.annihilationdw.inventories.AbilitiesMenu;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AbilityCommand implements CommandExecutor {

    Main pl;

    public AbilityCommand(Main pl) {
        this.pl = pl;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        AbilitiesMenu.setup((Player) sender);

        return true;
    }

}
