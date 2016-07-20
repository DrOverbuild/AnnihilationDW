package com.drizzard.annihilationdw.handlers;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import com.drizzard.annihilationdw.Main;
import com.drizzard.annihilationdw.api.events.JoinGameEvent;
import com.drizzard.annihilationdw.files.ConfigFile;
import com.drizzard.annihilationdw.files.MapFile;
import com.drizzard.annihilationdw.files.MessageFile;
import com.drizzard.annihilationdw.handlers.mapsetup.Blocks;
import com.drizzard.annihilationdw.handlers.mapsetup.Phase;
import com.drizzard.annihilationdw.handlers.mapsetup.Protect;
import com.drizzard.annihilationdw.handlers.mapsetup.Signs;
import com.drizzard.annihilationdw.managers.BarManager;
import com.drizzard.annihilationdw.managers.MapManager;
import com.drizzard.annihilationdw.managers.PartyManager;
import com.drizzard.annihilationdw.managers.ProtectedChestManager;
import com.drizzard.annihilationdw.managers.SoundManager;
import com.drizzard.annihilationdw.managers.TeamManager;
import com.drizzard.annihilationdw.managers.TitleManager;
import com.drizzard.annihilationdw.utils.Convert;
import com.drizzard.annihilationdw.utils.ItemStackGenerator;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

//import net.playerforcehd.nametags.TagManager.TagManager;
//import net.playerforcehd.nametags.TagManager.TagPlayer;

public class Game {

    private static boolean canVote;
    private static int countdown = getDefaultCountdown();

    private static boolean countdownStarted;
    private static GameState gameState = GameState.LOBBY;
    private static GameMap map;
    private static int phase = 0;
    private static int phaseTime;
    private static Main plugin = (Main) Bukkit.getPluginManager().getPlugin("AnnihilationDW");
    private static int respawnTimer = 0;

    public static void start(GameMap map) {
        setCountdownStarted(false);
        setGameState(GameState.IN_GAME);
        setRespawnTimer(ConfigFile.config.getInt("respawn.time"));
        PartyManager.setAddedParties(new ArrayList<Party>());
        setPhase(0);

        Main.println("Loading world " + map.getName() + " to start game.");
        long milliseconds = System.currentTimeMillis();
        MapManager.createWorld(map);
        MapFile.createConfig("plugins/AnnihilationDW/Maps/" + map.getName());
        Main.println("World load completed in " + (System.currentTimeMillis() - milliseconds) / 1000 + " seconds.");

        new TeamManager(plugin, MapFile.config.getInt("team.amount"));
        Blocks.setupBlocks(map);
        Protect.setupAreas(map);
        ProtectedChestManager.clearProtectedBlocks();
        Stats.setupPoints();
        Signs.setupShops();

        // Adding players to the teams

        // Add all parties first.

        for (Party party : PartyManager.getParties()) {
            Team toJoin = TeamManager.getTeamToJoin();
            for (Player player : party.getPlayers()) {
                setupPlayer(player, toJoin);
            }
            setupPlayer(party.getLeader(), toJoin);
            PartyManager.addAddedParty(party);
        }

        // Now add rest of players

        for (Player p : Bukkit.getOnlinePlayers()) {

//			// Taking care of parties
//
//			if(PartyManager.hasParty(p)) {
//				Party party = PartyManager.getParty(p);
//				if(!PartyManager.isPartyAdded(party)){
//					Team toJoin = TeamManager.getTeamToJoin();
//
//					for(Player partyPlayer : party.getPlayers()){
//						setupPlayer(partyPlayer, toJoin);
//					}
//
//					Player leader = party.getLeader();
//					setupPlayer(leader, toJoin);
//
//					PartyManager.addAddedParty(party);
//				}
//			}

            // Sole players

            if (!PartyManager.hasParty(p)) {
                setupPlayer(p, TeamManager.getTeamToJoin());
            }
        }
        Game.startPhases(map);

        for (Team team : TeamManager.getTeams()) {
            for (Player player : team.getAlivePlayers()) {
                Stats.getStats(player).addGame();
            }
        }
    }

    /**
     * Prepares player for the game
     *
     * @param team which will be joined
     */
    public static void setupPlayer(Player player, Team team) {
        PlayerHandler.unhidePlayer(player);
        Main.println("Setting " + player.getName() + " spectating to " + (getPhase() > 3));
        PlayerHandler.setSpectating(player, getPhase() > 3);
        PlayerHandler.setTeamOfPlayer(player, team);
        player.teleport((getPhase() > 3) ? team.getSpectatorSpawnpoint() : team.getSpawnpoint());
        player.getInventory().clear();
        setupGameInventory(player);
        ScoreboardHandler.update(player);
        Bukkit.getPluginManager().callEvent(new JoinGameEvent(player, team));
    }

    public static void finish(Team winner) {
        setCountdownStarted(false);
        setGameState(GameState.END_OF_GAME);
        Signs.setupShops();
        for (final Player p : Bukkit.getOnlinePlayers()) {
            if (winner != null) {
                MessageHandler
                        .sendMessage(p, MessageHandler.formatString(MessageFile.getMessage("game.winner"), winner.getColor() + winner.getName()));

            }
            PlayerHandler.setSpectating(p, true);
            BarManager.removeBar(p);
            PlayerHandler.clearAbilities(p);
        }

        if (winner != null) {
            for (Player p : winner.getAlivePlayers()) {
                Stats.getStats(p).addPoints(Stats.winPoints);
                Stats.getStats(p).addWin();
            }
        }

        if (ConfigFile.getBungeeCordModeEnabled()) {
            new BukkitRunnable() {

                @Override
                public void run() {
                    stopServer();
                }
            }.runTaskLater(plugin, 10 * 20);
        } else {
            prepareForNextGame();
        }
    }

    public static void prepareForNextGame() {
        PlayerHandler.clearPlayerGold();
        ProtectedChestManager.clearProtectedBlocks();

        for (GameMap m : MapManager.getMaps()) {
            m.setVotes(0);
        }

        MapManager.pickRandomVotableMaps();

        new BukkitRunnable() {

            public void run() {
                MapManager.deleteWorld(MapManager.mapName);
                setGameState(GameState.LOBBY);
                for (final Player p : Bukkit.getOnlinePlayers()) {
                    BarManager.removeBar(p);
                    PlayerHandler.setSpectating(p, false);
                    PlayerHandler.unhidePlayer(p);
                    //PlayerHandler.setPlayerPlaying(p, false);
                    Lobby.setupLobby(p);
                    //TagAPI.refreshPlayer(p);
                }

                for (Team team : TeamManager.getTeams()) {
                    team.setPlayers(new ArrayList<>());
                }

                PlayerHandler.clearPlayersPlayingMap();


            }
        }.runTaskLater(plugin, 10 * 20);
    }

    public static void stopServer() {
        plugin.getLogger().info("Attempting to kick players and stop server...");

        final String fallbackServer = ConfigFile.getFallbackServer();
        for (Player p : Bukkit.getOnlinePlayers()) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(fallbackServer);
            p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
        }

        new BukkitRunnable() {

            final int timeoutLimit = ConfigFile.getTimeout();
            int timeoutTimer = 0;

            @Override
            public void run() {
                if (Bukkit.getOnlinePlayers().size() <= 0) {
                    this.cancel();
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
                } else if (timeoutLimit > 0) {
                    timeoutTimer++;
                    if (timeoutTimer >= timeoutLimit) {
                        this.cancel();
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
                    }
                }
            }
        }.runTaskTimer(plugin, 20, 20);
    }

    private static void startPhases(final GameMap map) {
        Signs.setupShops();
//		Signs.setupWeaponsShop();
//		Signs.setupBrewingShop();
        if (!Game.isGameStarted()) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                BarManager.removeBar(p);
            }
            return;
        }
        setPhase(getPhase() + 1);
        MapFile.createConfig("plugins/AnnihilationDW/Maps/" + map.getName());
        phaseTime = MapFile.config.getInt("phase-time") / 20;
        ScoreboardHandler.updateAll();

        for (Team t : TeamManager.getTeams()) {
            for (Player p : t.getAlivePlayers()) {
//				plugin.getTitleManager().getPhaseTitle(getPhase()).send(p);
                TitleManager.sendTitle(p, getPhase());
                SoundManager.playDragonGrowl(p);
                Phase.sendPhaseMessage(p, getPhase());
                BarManager.setMessage(p, MessageHandler.getPhaseMessage(getPhase()));
                //BossBarAPI.setMessage(p, MessageHandler.getPhaseMessage(getPhase()));
                //BossBarAPI.setMessage(p, MessageHandler.getPhaseMessage(getPhase()), phaseTime / 20);
            }
        }

        List<Team> remove = new ArrayList<>();
        for (Team t : TeamManager.getTeams()) {
            if (t.getAlivePlayers().size() == 0 && getPhase() > 3) {
                remove.add(t);
            }
        }
        for (Team t : remove) {
            TeamManager.destroyTeam(t);
        }

        if (getPhase() == 3) {
            Blocks.spawnDiamonds();
        }


        new BukkitRunnable() {

            @Override
            public void run() {
                if (Game.isGameStarted()) {
                    phaseTime--;
                    if (phaseTime % 60 == 0 || phaseTime <= 60) {
                        ScoreboardHandler.updateAll();
                    }
                    if (phaseTime <= 0) {
                        this.cancel();
                        if (getPhase() < 4) {
                            startPhases(map);
                        } else {
                            startLastPhase(map);
                        }
                    }
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 20, 20);
    }

    private static void startLastPhase(GameMap map) {
        Signs.setupShops();
//		Signs.setupWeaponsShop();
//		Signs.setupBrewingShop();
        if (!Game.isGameStarted()) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                BarManager.removeBar(p);
            }
            return;
        }
        setPhase(getPhase() + 1);
        phaseTime = ConfigFile.config.getInt("gametime") * 1200;
        MapFile.createConfig("plugins/AnnihilationDW/Maps/" + map.getName());
        phaseTime -= (MapFile.config.getInt("phase-time") * 4);
        phaseTime /= 20;
        if (phaseTime <= 0) {
            Team team = TeamManager.getMostKills();
            Main.println("Finishing game because we're starting last phase and phaseTime is less than or equal to 0.");
            finish(team);
            return;
        }
        for (Team t : TeamManager.getTeams()) {
            for (Player p : t.getAlivePlayers()) {
                SoundManager.playDragonGrowl(p);
                Phase.sendPhaseMessage(p, getPhase());
                //BossBarAPI.setMessage(p, MessageHandler.getPhaseMessage(getPhase()), phaseTime / 20);
                BarManager.setMessage(p, MessageHandler.getPhaseMessage(getPhase()));
            }
        }
        new BukkitRunnable() {

            @Override
            public void run() {
                if (Game.isGameStarted()) {
                    phaseTime--;
                    ScoreboardHandler.updateAll();
                    if (phaseTime <= 0) {
                        this.cancel();
                        Team team = TeamManager.getMostKills();
                        Main.println("Finishing game because phaseTime reached 0.");
                        finish(team);
                    }
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 20, 20);
    }

    public static void startCountdown() {
        countdown = getDefaultCountdown();
        setGameState(GameState.LOBBY);
        setCountdownStarted(true);
        new BukkitRunnable() {

            @Override
            public void run() {
                if (isGameStarted()) {
                    countdown = 0;
                    this.cancel();
                    return;
                }
                if (countdown == getDefaultCountdown()) {
                    setCanVote(true);
                }
                if (Bukkit.getOnlinePlayers().size() == 0) {
                    setCountdownStarted(false);
                    this.cancel();
                    return;
                } else if (Bukkit.getOnlinePlayers().size() < ConfigFile.config.getInt("minimum-player-amount")) {
                    setCountdownStarted(false);
                    countdown = getDefaultCountdown();
                    ScoreboardHandler.updateAll();
                    this.cancel();
                    return;
                }
                if (countdown == 10) {
                    map = MapManager.chooseMap();
                    setCanVote(false);
                }
                for (Player p : Bukkit.getOnlinePlayers()) {
                    ScoreboardHandler.update(p);
                    if (countdown <= 10) {
                        SoundManager.playClick(p);
                        //p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 0);
                    }
                    if (countdown > 20 && countdown % 20 == 0) {
                        MessageHandler.sendMessage(p, MessageFile.getMessage("vote.reminder"));
                    }
                    if ((countdown % 30 == 0 || countdown == 10) && countdown != 0) {
                        String msg = MessageFile.getMessage("game.until-start");
                        msg = MessageHandler.formatInteger(msg, countdown);
                        MessageHandler.sendMessage(p, msg);
                        if (countdown == 10) {
                            msg = MessageFile.getMessage("vote.map-chosen");
                            msg = MessageHandler.formatString(msg, map.getName());
                            MessageHandler.sendMessage(p, msg);
                        }
                    } else if (countdown == 0) {
                        if (Bukkit.getOnlinePlayers().size() >= ConfigFile.config.getInt("minimum-player-amount")) {
                            String msg = MessageFile.getMessage("game.start");
                            msg = MessageHandler.format(msg);
                            MessageHandler.sendMessage(p, msg);
                        } else {
                            String msg = MessageFile.getMessage("game.not-enough-players");
                            msg = MessageHandler.formatInteger(msg, ConfigFile.config.getInt("minimum-player-amount"));
                            MessageHandler.sendMessage(p, msg);
                        }
                    }
                }
                if (countdown == 0) {
                    if (Bukkit.getOnlinePlayers().size() >= ConfigFile.config.getInt("minimum-player-amount")) {
                        start(map);
                        this.cancel();
                        return;
                    } else {
                        countdown = getDefaultCountdown();
                    }
                }
                --countdown;
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }

    public static void setupGameInventory(Player player) {
        player.setGameMode(GameMode.SURVIVAL);
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        Color color = Convert.ChatColorToColor(TeamManager.getTeam(player).getColor());
        ItemStack helmet = ItemStackGenerator.createItem(Material.LEATHER_HELMET, 0, 0, null, null);
        LeatherArmorMeta meta = (LeatherArmorMeta) helmet.getItemMeta();
        meta.setColor(color);
        helmet.setItemMeta(meta);
        player.getInventory().setHelmet(helmet);
        ItemStack armor = ItemStackGenerator.createItem(Material.LEATHER_CHESTPLATE, 0, 0, null, null);
        meta = (LeatherArmorMeta) armor.getItemMeta();
        meta.setColor(color);
        armor.setItemMeta(meta);
        player.getInventory().setChestplate(armor);
        ItemStack leggings = ItemStackGenerator.createItem(Material.LEATHER_LEGGINGS, 0, 0, null, null);
        meta = (LeatherArmorMeta) leggings.getItemMeta();
        meta.setColor(color);
        leggings.setItemMeta(meta);
        player.getInventory().setLeggings(leggings);
        ItemStack boots = ItemStackGenerator.createItem(Material.LEATHER_BOOTS, 0, 0, null, null);
        meta = (LeatherArmorMeta) boots.getItemMeta();
        meta.setColor(color);
        boots.setItemMeta(meta);
        player.getInventory().setBoots(boots);
        PlayerHandler.setCompassStatus(player, 0);
        player.getInventory().addItem(
                ItemStackGenerator.createItem(Material.COMPASS, 0, 0, MessageHandler.format(MessageFile.getMessage("compass.default")), null));
        player.getInventory().addItem(ItemStackGenerator.createItem(Material.WOOD_SWORD, 0, 0, null, null));
        player.getInventory().addItem(ItemStackGenerator.createItem(Material.WOOD_PICKAXE, 0, 0, null, null));
        player.getInventory().addItem(ItemStackGenerator.createItem(Material.WOOD_AXE, 0, 0, null, null));
        player.getInventory().addItem(ItemStackGenerator.createItem(Material.WORKBENCH, 0, 0, null, null));
        Integer gold = PlayerHandler.getPlayerGold(player);
        if (gold != null && gold > 0) {
            player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, gold));
        }
        PlayerHandler.setPlayerGold(player, null);
        Kits.setupKit(player);
    }

    public static boolean isCountdownStarted() {
        return countdownStarted;
    }

    public static void setCountdownStarted(boolean countdownStarted) {
        Game.countdownStarted = countdownStarted;
    }

    public static boolean isGameStarted() {
        return gameState.equals(GameState.IN_GAME);
    }

    public static GameState getGameState() {
        return gameState;
    }

    public static void setGameState(GameState state) {
        gameState = state;
    }

    public static void addPlayer(List<Player> players, Player p) {
        players.add(p);
    }

    public static void removePlayer(List<Player> players, Player p) {
        players.remove(p);
    }

    public static int getDefaultCountdown() {
        return ConfigFile.config.getInt("time-until-start");
    }

    public static int getCountdown() {
        return countdown;
    }

    public static boolean isCanVote() {
        return canVote;
    }

    public static void setCanVote(boolean canVote) {
        Game.canVote = canVote;
    }

    public static int getPhase() {
        return phase;
    }

    public static void setPhase(int phase) {
        Game.phase = phase;
    }

    public static int getPhaseTime() {
        return phaseTime;
    }

    public static int getRespawnTimer() {
        return respawnTimer;
    }

    public static void setRespawnTimer(int timer) {
        respawnTimer = timer;
    }

    public static boolean checkIfGameShouldBeFinished() {
        if (getPhase() > 3) {
            if (Bukkit.getOnlinePlayers().size() == 0) {
                return true;
            }
            List<Team> teamsAlive = TeamManager.getAliveTeams();
            if (teamsAlive.size() <= 1) {
                return true;
            }

        }
        return false;
    }
}