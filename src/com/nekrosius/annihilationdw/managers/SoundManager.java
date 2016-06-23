package com.nekrosius.annihilationdw.managers;

import com.nekrosius.annihilationdw.Main;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * Created by jasper on 4/22/16.
 */
public class SoundManager {

	public static void playDragonGrowl(Player p){
		try {
			p.playSound(p.getLocation(), Sound.valueOf("ENDERDRAGON_GROWL"), 1F, 1F);
		}catch (IllegalArgumentException e1){
			try {
				p.playSound(p.getLocation(), Sound.valueOf("ENTITY_ENDERDRAGON_GROWL"), 1F, 1F);
			}catch (IllegalArgumentException e2){

			}
		}
	}

	public static void playClick(Player p){
		try{
			p.playSound(p.getLocation(), Sound.valueOf("CLICK"), 1, 0);
		}catch (IllegalArgumentException e1){
			try{
				p.playSound(p.getLocation(), Sound.valueOf("BLOCK_LEVER_CLICK"), 1, 0);
			}catch (IllegalArgumentException e2){

			}
		}
	}

	public static void playItemBreak(World world, Location loc){
		try{
			world.playSound(loc,Sound.valueOf("ITEM_BREAK"), 1, 0);
		}catch (IllegalArgumentException e){
			try{
				world.playSound(loc, Sound.valueOf("ENTITY_ITEM_BREAK"), 1, 0);
			}catch (IllegalArgumentException e2){

			}
		}
	}
}
