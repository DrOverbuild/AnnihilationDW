package com.drizzard.annihilationdw.handlers;

import com.drizzard.annihilationdw.files.ConfigFile;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Party {

    private Player leader;
    private List<Player> players;

    public Party(Player leader, List<Player> members) {
        setLeader(leader);
        setPlayers(members);
    }

    public boolean isFull() {
        List<Player> playersInParty = new ArrayList<>(getPlayers());
        if (!playersInParty.contains(getLeader())) {
            playersInParty.add(getLeader());
        }

        return playersInParty.size() >= ConfigFile.getPartyPlayerLimit();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Player getLeader() {
        return leader;
    }

    public void setLeader(Player leader) {
        this.leader = leader;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public boolean isLeader(Player player) {
        return player.equals(leader);
    }

    public boolean isMember(Player player) {
        return players.contains(player);
    }
}