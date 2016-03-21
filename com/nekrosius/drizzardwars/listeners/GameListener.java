package com.nekrosius.drizzardwars.listeners;

import com.nekrosius.drizzardwars.files.ConfigFile;
import com.nekrosius.drizzardwars.managers.ProtectedChestManager;
import de.slikey.effectlib.util.DynamicLocation;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.nekrosius.drizzardwars.Main;
import com.nekrosius.drizzardwars.files.MessageFile;
import com.nekrosius.drizzardwars.files.ShopFile;
import com.nekrosius.drizzardwars.handlers.Game;
import com.nekrosius.drizzardwars.handlers.MessageHandler;
import com.nekrosius.drizzardwars.handlers.PlayerHandler;
import com.nekrosius.drizzardwars.handlers.Points;
import com.nekrosius.drizzardwars.handlers.ScoreboardHandler;
import com.nekrosius.drizzardwars.handlers.TabHandler;
import com.nekrosius.drizzardwars.handlers.Team;
import com.nekrosius.drizzardwars.handlers.mapsetup.Blocks;
import com.nekrosius.drizzardwars.handlers.mapsetup.Protect;
import com.nekrosius.drizzardwars.handlers.mapsetup.Signs;
import com.nekrosius.drizzardwars.managers.TeamManager;
import com.nekrosius.drizzardwars.utils.Convert;
import com.nekrosius.drizzardwars.utils.ExperienceManager;

import de.slikey.effectlib.effect.ExplodeEffect;

public class GameListener implements Listener{
	
	private Main pl;
	public GameListener(Main plugin) {
		pl = plugin;
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onInteract(PlayerInteractEvent event){
		if(!Game.isGameStarted()) return;
		Player player = event.getPlayer();
		if(PlayerHandler.isSpectating(player)){ event.setCancelled(true); return; }
		if(player.getItemInHand().getType().equals(Material.COMPASS)) {
			if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
				PlayerHandler.setCompassStatus(player, PlayerHandler.nextCompassStatus(player));
				ItemMeta meta = player.getItemInHand().getItemMeta();
				Team team = TeamManager.getTeam(PlayerHandler.getCompassStatus(player));
				if(team == null) team = TeamManager.getTeam(0);
				player.setCompassTarget(team.getNexusLocation());
				meta.setDisplayName(MessageHandler.formatString(MessageFile.getMessage("compass.target"), team.getColor() + team.getName()));
				player.getItemInHand().setItemMeta(meta);
			}
		}
		else if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			// SHOPS
			if(!Game.isGameStarted()){ event.setCancelled(true); return; }
			if(event.getClickedBlock().getType().equals(Material.WALL_SIGN)
					|| event.getClickedBlock().getType().equals(Material.SIGN_POST)){
				Sign sign = (Sign) event.getClickedBlock().getState();
				if(!sign.getLine(0).equalsIgnoreCase(ChatColor.DARK_RED + "[" + ChatColor.DARK_PURPLE + "Shop" + ChatColor.DARK_RED + "]")) return;
				// BREWING SHOP
				if(sign.getLine(1).equalsIgnoreCase("brewing")){
					Signs.openBrewingShop(player);
				}
				// WEAPON SHOP
				else if(sign.getLine(1).equalsIgnoreCase("weapon")){
					Signs.openWeaponShop(player);
				}
			}
			// PROTECTED CHESTS/FURNACES
			if(!ProtectedChestManager.playerHasAccessToBlock(player,event.getClickedBlock())){
				player.sendMessage(MessageHandler.formatString(MessageFile.getMessage("protection.protected"), event.getClickedBlock().getType().toString()));
				event.setCancelled(true);
			}

//			if(ProtectedChestManager.blockIsProtectable(event.getClickedBlock())){
//				OfflinePlayer protector = ProtectedChestManager.getProtector(event.getClickedBlock());
//				if(protector != null){
//					if(protector.isOnline()){
//						Player p = protector.getPlayer();
//						if(!p.equals(player)) {
//							player.sendMessage(MessageHandler.formatString(MessageFile.getMessage("protection.protected"), event.getClickedBlock().toString()));
//							event.setCancelled(true);
//						}
//					}else{
//						player.sendMessage(MessageHandler.formatString(MessageFile.getMessage("protection.protected"), event.getClickedBlock().toString()));
//						event.setCancelled(true);
//					}
//				}
//			}
		}
	}

	@EventHandler
	public void onTabChat(PlayerChatTabCompleteEvent event) {
		if(!Game.isGameStarted()) return;
		Player player = event.getPlayer();
		event.getTabCompletions().clear();
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.sendMessage(ChatColor.GRAY + "(" + MessageHandler.all 
					+ ") [" + TeamManager.getTeam(player).getColor() + TeamManager.getTeam(player).getName() + ChatColor.GRAY + "] " 
					+ ChatColor.WHITE + player.getName() + ": " + event.getChatMessage());	
		}
		Inventory inv = Bukkit.createInventory(null, 9);
		player.openInventory(inv);
		player.closeInventory();
	}
	
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		String message = event.getMessage();
		String playerName = player.getName();
		if(player.hasPermission("drwars.vip")){
			playerName = MessageFile.formatMessage("vip-prefix") + " " + playerName;
		}
		if(PlayerHandler.isSpectating(player)){
			for(Player p : Bukkit.getOnlinePlayers()){
				if(PlayerHandler.isSpectating(p)){
					event.setFormat(ChatColor.GRAY + "(" + MessageHandler.all 
							+ ") [" + ChatColor.LIGHT_PURPLE + "Spectator" + ChatColor.GRAY + "] " 
							+ ChatColor.WHITE + playerName + ": " + message);
				}
			}
		}
		else if(!TeamManager.hasTeam(player)){
			event.setFormat(ChatColor.GRAY + "(" + MessageHandler.all 
					+ ") [" + ChatColor.DARK_PURPLE + "Lobby" + ChatColor.GRAY + "] "
					+ ChatColor.WHITE + playerName + ": " + message);
		}else if(event.getMessage().startsWith("!")){
			message = message.replace("!", "");
			event.setFormat(ChatColor.GRAY + "(" + MessageHandler.all 
					+ ") [" + TeamManager.getTeam(player).getColor() + TeamManager.getTeam(player).getName() + ChatColor.GRAY + "] "
					+ ChatColor.WHITE + playerName + ": " + message);
		}else{
			event.setCancelled(true);
			ChatColor color = TeamManager.getTeam(player).getColor();
			String name = TeamManager.getTeam(player).getName();
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(TeamManager.getTeam(p).equals(TeamManager.getTeam(player))){
					p.sendMessage(ChatColor.GRAY + "(" + MessageHandler.team 
							+ ") [" + color + name + ChatColor.GRAY 
							+ "] " + ChatColor.WHITE + playerName + ": " + message);
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockBreak(final BlockBreakEvent event) {
		if(!Game.isGameStarted()) return;
		if(PlayerHandler.isSpectating(event.getPlayer())){ event.setCancelled(true); return; }
		if(!event.getPlayer().isOp()){
			if(event.getPlayer().getWorld().getName().equalsIgnoreCase("lobby")){ event.setCancelled(true); return; }
		}
		if(event.getBlock().getType().equals(Material.SIGN_POST) || event.getBlock().getType().equals(Material.WALL_SIGN)){
			Sign sign = (Sign) event.getBlock().getState();
			if(sign.getLine(0).equals(ChatColor.DARK_RED + "[" + ChatColor.DARK_PURPLE + "Shop" + ChatColor.DARK_RED + "]")){
				event.setCancelled(true);
				return;
			}
		}

		// NEXUS DAMAGE
		else if(event.getBlock().getType().equals(Material.ENDER_STONE)) {
			event.setCancelled(true);
			if(Game.getPhase() == 1){
				return;
			}
			int dmg = 1;
			if(Game.getPhase() == 5){
				dmg = 2;
			}
			Team remove = null;
			for(Team team : TeamManager.getTeams()) {
				if(Convert.equals(event.getBlock().getLocation(), team.getNexusLocation())) {
					if(!team.getName().equalsIgnoreCase(TeamManager.getTeam(event.getPlayer()).getName())){
						team.setNexusHealth(team.getNexusHealth() - dmg);
						ExplodeEffect eff = new ExplodeEffect(Main.em);
						eff.setDynamicOrigin(new DynamicLocation(team.getNexusLocation()));
						eff.start();
						team.getNexusLocation().getWorld().playSound(team.getNexusLocation(), Sound.ITEM_BREAK, 1F, 1F);
						for(String p : team.getAllPlayers()){
							if(Bukkit.getPlayer(p) != null)
							MessageHandler.sendMessage(Bukkit.getPlayer(p), MessageHandler.nexusDmg);
						}
						if(team.getNexusHealth() <= 0) {
							remove = team;
						}
						ScoreboardHandler.updateAll();
					}
				}
			}
			if(remove != null) {
				TeamManager.destroyTeam(remove);
			}
		}

		// PROTECTED AREAS
		for(int i = 0; true; i++) {
			if(Protect.getFirstPoint(i) == null) break;
			Vector min = Vector.getMinimum(Protect.getFirstPoint(i).toVector(), Protect.getSecondPoint(i).toVector());
			Vector max = Vector.getMaximum(Protect.getFirstPoint(i).toVector(), Protect.getSecondPoint(i).toVector());
			if(event.getBlock().getLocation().toVector().isInAABB(min, max)){
				event.setCancelled(true);
				return;
			}
		}

		// REGENERATING BLOCKS
		if(Blocks.blockRegenerates(event.getBlock().getType())){
			final Material m = event.getBlock().getType();
			if(Blocks.getBrokeBlock(m).equals(Material.COBBLESTONE)) event.setCancelled(true);
			event.getBlock().setType(Blocks.getBrokeBlock(m));
			Player player = event.getPlayer();
			ExperienceManager expMan = new ExperienceManager(player);
			expMan.changeExp(Blocks.getXpReward(m));
			ItemStack reward = Blocks.getReward(m);
			reward.setAmount(Blocks.getRewardAmount(m));
			if(Blocks.getDropType(m).equals("natural")){
				event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), reward);
			}else{
				player.getInventory().addItem(reward);
			}
			new BukkitRunnable() {
				@Override
				public void run() {
					event.getBlock().setType(m);
				}
			}.runTaskLater(pl, Blocks.getTimer(m));
			return;
		}else if(Blocks.getData(event.getBlock().getType()) != null){
			final Material m = event.getBlock().getType();
			if(event.getBlock().getData() == Blocks.getData(m)){
				event.setCancelled(true);
				return;
			}
		}

		// PROTECTED CHESTS/FURNACES
		if(ProtectedChestManager.playerHasAccessToBlock(event.getPlayer(),event.getBlock())) {
			if (ProtectedChestManager.blockIsProtected(event.getBlock())) {
				event.getPlayer().sendMessage(MessageHandler.formatString(MessageFile.getMessage("protection.removed"), event.getBlock().getType().toString()));
				ProtectedChestManager.removeProtectedBlock(event.getBlock());
			}
		}else{
			event.getPlayer().sendMessage(MessageHandler.formatString(MessageFile.getMessage("protection.protected"), event.getBlock().getType().toString()));
			event.setCancelled(true);
		}

//		if(ProtectedChestManager.blockIsProtectable(event.getBlock())){
//			if(ProtectedChestManager.blockIsProtected(event.getBlock())){
//				OfflinePlayer offlinePlayer = ProtectedChestManager.getProtector(event.getBlock());
//				if(offlinePlayer.isOnline()){
//					Player p = offlinePlayer.getPlayer();
//					if(p.equals(event.getPlayer())){
//						ProtectedChestManager.removeProtectedBlock(event.getBlock());
//						p.sendMessage(MessageHandler.formatString(MessageFile.getMessage("protection.removed"),event.getBlock().toString()));
//					}else{
//						p.sendMessage(MessageHandler.formatString(MessageFile.getMessage("protection.protected"), event.getBlock().toString()));
//						event.setCancelled(true);
//					}
//				}else{
//					event.getPlayer().sendMessage(MessageHandler.formatString(MessageFile.getMessage("protection.protected"), event.getBlock().toString()));
//					event.setCancelled(true);
//				}
//			}
//		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if(!Game.isGameStarted()) return;
		if(PlayerHandler.isSpectating(event.getPlayer())){ event.setCancelled(true); return; }
		Block block = event.getBlock();
		for(int i = 0; true; i++) {
			if(Protect.getFirstPoint(i) == null) break;
			Vector min = Vector.getMinimum(Protect.getFirstPoint(i).toVector(), Protect.getSecondPoint(i).toVector());
			Vector max = Vector.getMaximum(Protect.getFirstPoint(i).toVector(), Protect.getSecondPoint(i).toVector());
			if(block.getLocation().toVector().isInAABB(min, max)){
				event.setCancelled(true);
				return;
			}
		}
		if(Blocks.isUnplaceable(block.getType()) != null){
			if(Blocks.isUnplaceable(block.getType())){
				event.setCancelled(true);
			}
		}

		// PROTECTED CHESTS/FURNACES
		Block placedBlock = event.getBlockPlaced();
		if(ProtectedChestManager.blockIsProtectable(placedBlock)){
			ProtectedChestManager.addProtectedBlock(placedBlock, event.getPlayer());
			event.getPlayer().sendMessage(MessageHandler.formatString(MessageFile.getMessage("protection.now-protected"),placedBlock.getType().toString()));
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if(PlayerHandler.isSpectating((Player)event.getWhoClicked())){ event.setCancelled(true); return; }
		// WEAPONS SHOP
		if(event.getInventory().getName().equalsIgnoreCase(MessageHandler.format(ShopFile.config.getString("weapons.inventory.name")))){
			if(event.getCurrentItem() == null){ event.setCancelled(true); return;}
			if(!event.getClickedInventory().getName().equalsIgnoreCase(MessageHandler.format(ShopFile.config.getString("weapons.inventory.name")))){
				event.setCancelled(true);
				return;
			}
			if(event.getCurrentItem().getType() == Material.AIR) return;
			if(!event.getSlotType().equals(SlotType.CONTAINER)){ 
				event.setCancelled(true);
				return;
			}
			event.setCancelled(true);
			String id = String.valueOf(event.getSlot());
			int price = ShopFile.config.getInt("weapons.items." + id + ".price");
			int remove = price;
			Player player = (Player) event.getWhoClicked();
			if(!player.getInventory().contains(Material.GOLD_INGOT, price)){
				MessageHandler.sendMessage(player, MessageFile.getMessage("shop.cant-afford"));
				return;
			}

			player.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, remove));
			ItemStack item = event.getCurrentItem().clone();
			ItemMeta meta = item.getItemMeta();
			meta.setLore(null);
			item.setItemMeta(meta);
			player.getInventory().addItem(item);
		}
		// BREWING SHOP
		else if(event.getInventory().getName().equalsIgnoreCase(MessageHandler.format(ShopFile.config.getString("brewing.inventory.name")))){
			if(event.getCurrentItem() == null){ event.setCancelled(true); return;}
			if(!event.getClickedInventory().getName().equalsIgnoreCase(MessageHandler.format(ShopFile.config.getString("brewing.inventory.name")))){
				event.setCancelled(true);
				return;
			}
			if(event.getCurrentItem().getType() == Material.AIR) return;
			if(!event.getSlotType().equals(SlotType.CONTAINER)){ 
				event.setCancelled(true);
				return;
			}
			event.setCancelled(true);
			String id = String.valueOf(event.getSlot());
			int price = ShopFile.config.getInt("brewing.items." + id + ".price");
			int remove = price;
			Player player = (Player) event.getWhoClicked();
			if(!player.getInventory().contains(Material.GOLD_INGOT, price)){
				MessageHandler.sendMessage(player, MessageFile.getMessage("shop.cant-afford"));
				return;
			}
			for(ItemStack item : player.getInventory()){
				if(item != null){
					if(!item.getType().equals(Material.AIR)){
						if(item.getType().equals(Material.GOLD_INGOT)){
							if(item.getAmount() >= remove){
								item.setAmount(item.getAmount() - remove);
							}
						}
					}
				}
			}
			ItemStack item = event.getCurrentItem();
			ItemMeta meta = item.getItemMeta();
			meta.setLore(null);
			item.setItemMeta(meta);
			player.getInventory().addItem(item);
		}
	}
	
	@EventHandler
	public void playerAttacksPlayer(EntityDamageByEntityEvent event) {
		if(event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
			if(!Game.isGameStarted()){
				event.setCancelled(true);
				return;
			}
			if(Game.getPhase()==1){
				event.setCancelled(true);
				return;
			}
			Player victim = (Player) event.getEntity();
			Player damager = (Player) event.getDamager();
			if(PlayerHandler.isSpectating(victim) || PlayerHandler.isSpectating(damager)) event.setCancelled(true);
			else if(TeamManager.getTeam(damager).equals(TeamManager.getTeam(victim))){
				event.setCancelled(true);
				return;
			}
			else if(victim.getHealth() - event.getDamage() <= 0){
				if(victim.isDead()) return;
				if(!victim.isValid()) return;
				Points.addPoints(damager, Points.getKillPoints());
				TabHandler.update(damager);
				for(Player p : Bukkit.getOnlinePlayers()){
					MessageHandler.sendMessage(p, MessageHandler.formatPlayer(MessageFile.getMessage("player.kill"), victim, damager));
				}
				TeamManager.getTeam(damager).addKill();
				for(Player p : TeamManager.getTeam(damager).getAlivePlayers()){
						ScoreboardHandler.update(p);
				}
				damager.getInventory().addItem(PlayerHandler.getKillReward());
				victim.setHealth(0);
			}
		}else if(event.getDamager() instanceof Arrow && event.getEntity() instanceof Player) {
			Arrow arrow = (Arrow) event.getDamager();
			if(arrow.getShooter() instanceof Player) {
				if(!Game.isGameStarted()){
					event.setCancelled(true);
					return;
				}
				if(Game.getPhase()==1){
					event.setCancelled(true);
					return;
				}
				Player victim = (Player) event.getEntity();
				Player damager = (Player) arrow.getShooter();
				if(PlayerHandler.isSpectating(victim) || PlayerHandler.isSpectating(damager)) event.setCancelled(true);
				else if(TeamManager.getTeam(damager).equals(TeamManager.getTeam(victim))){
					event.setCancelled(true);
					return;
				}
				else if(victim.getHealth() - event.getDamage() <= 0){
					if(victim.isDead()) return;
					if(!victim.isValid()) return;
					Points.addPoints(damager, Points.getKillPoints());
					TabHandler.update(damager);
					for(Player p : Bukkit.getOnlinePlayers()){
						MessageHandler.sendMessage(p, MessageHandler.formatPlayer(MessageFile.getMessage("player.kill"), victim, damager));
					}
					TeamManager.getTeam(damager).addKill();
					for(String p : TeamManager.getTeam(damager).getAllPlayers()){
						if(Bukkit.getPlayer(p) != null)
							ScoreboardHandler.update(Bukkit.getPlayer(p));
					}
					damager.getInventory().addItem(PlayerHandler.getKillReward());
					victim.setHealth(0);
				}
			}
		}
	}
	
	@EventHandler
	public void dropItem(PlayerDropItemEvent event) {
		if(PlayerHandler.isSpectating(event.getPlayer())){ event.setCancelled(true); return; }
	}
	
	@EventHandler
	public void pickupItem(PlayerPickupItemEvent event) {
		if(PlayerHandler.isSpectating(event.getPlayer())){ event.setCancelled(true); return; }
	}

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent e){
		if(e.toWeatherState()) {
			if (ConfigFile.config.getString("spawn-location") == null) {
				if (e.getWorld().getName().equalsIgnoreCase(Bukkit.getWorlds().get(0).getName())) {
					e.setCancelled(true);
				}
			} else {
				Location loc = Convert.StringToLocation(ConfigFile.config.getString("spawn-location"), true, false);
				if(loc.getWorld().getName().equals(e.getWorld().getName())){
					e.setCancelled(true);
				}
			}
		}
	}

	public Main getMainPlugin() {
		return pl;
	}
}