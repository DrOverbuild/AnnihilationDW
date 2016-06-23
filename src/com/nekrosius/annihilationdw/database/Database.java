package com.nekrosius.annihilationdw.database;

import org.bukkit.entity.Player;

import java.util.UUID;

public interface Database {

    void connect();

    boolean isConnected();

    void disconnect();

    void prepare();

    int getPoints(Player player);

    int getPoints(UUID playerId);

    void setPoints(Player player, int points);

    void setPoints(UUID playerId, int points);

}
