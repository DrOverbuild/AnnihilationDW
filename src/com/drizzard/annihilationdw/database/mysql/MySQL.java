package com.drizzard.annihilationdw.database.mysql;

import com.drizzard.annihilationdw.database.Database;
import com.drizzard.annihilationdw.handlers.Stats;

import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySQL implements Database {

    private static final String playerKitsTable = "player_kits";
    private static final String playerStatsTable = "player_stats";
    public static Logger logger = Logger.getLogger("Annihilation-MySQL");
    private final String database;
    private final String host;
    private final String password;
    private final int port;
    private final String username;
    private Connection conn;

    public MySQL(String host, int port, String username, String password, String database) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
    }

    @Override
    public boolean isConnected() {
        try {
            if (conn != null) {
                if (conn.isClosed()) {
                    conn = null;
                    return false;
                }
                return true;
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Java Exception", ex);
        }
        return false;
    }

    @Override
    public void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + host + ":" + port + (database != null ? "/" + database : ""), username, password);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Java Exception", ex);
        }
    }

    @Override
    public void disconnect() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "SQL Exception", ex);
            }
            conn = null;
        }
    }

    @Override
    public void prepare() {
        if (isConnected()) {
            try {
                update("CREATE TABLE IF NOT EXISTS " + playerStatsTable + "(uuid CHAR(36), points BIGINT DEFAULT 0, kills INT DEfAULT 0, games "
                        + "INT DEFAULT 0, wins INT DEFAULT 0, PRIMARY KEY" + "(uuid));");
                update("CREATE TABLE IF NOT EXISTS " + playerKitsTable + "(uuid CHAR(36), kit VARCHAR(255), PRIMARY KEY(uuid, kit));");
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "SQL Exception", ex);
            }
        }
    }

    @Override
    public void setPoints(Player player, int points) {
        setPoints(player.getUniqueId(), points);
    }

    @Override
    public void setPoints(UUID playerId, int points) {
        try {
            update("INSERT INTO " + playerStatsTable + " (uuid, points) VALUES (?, ?) ON DUPLICATE KEY UPDATE points = ?;", playerId.toString(),
                    points, points);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "SQL Exception", ex);
        }
    }

    @Override
    public void addKit(Player player, String kit) {
        addKit(player.getUniqueId(), kit);
    }

    @Override
    public void addKit(UUID playerId, String kit) {
        try {
            update("INSERT IGNORE INTO " + playerKitsTable + "(uuid, kit) VALUES(?, ?);", playerId.toString(), kit);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "SQL Exception", ex);
        }
    }

    @Override
    public void clearKits(Player player) {
        clearKits(player.getUniqueId());
    }

    @Override
    public void clearKits(UUID playerId) {
        try {
            update("DELETE FROM " + playerKitsTable + " WHERE uuid=?;", playerId.toString());
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "SQL Exception", ex);
        }
    }

    @Override
    public List<String> getKits(Player player) {
        return getKits(player.getUniqueId());
    }

    @Override
    public List<String> getKits(UUID playerId) {
        List<String> kits = new ArrayList<String>();
        try {
            QueryResult result = query("SELECT kit FROM " + playerKitsTable + " WHERE uuid=?", playerId.toString());
            for (QueryResult.ResultData data : result) {
                kits.add(data.asString("kit"));
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "SQL Exception", ex);
        }
        return kits;
    }

    @Override
    public void setKills(Player player, int kills) {
        setKills(player.getUniqueId(), kills);
    }

    @Override
    public void setKills(UUID playerId, int kills) {
        try {
            update("INSERT INTO " + playerStatsTable + " (uuid, kills) VALUES (?, ?) ON DUPLICATE KEY UPDATE kills = ?;", playerId.toString(),
                    kills, kills);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "SQL Exception", ex);
        }
    }

    @Override
    public void setGames(Player player, int games) {
        setGames(player.getUniqueId(), games);
    }

    @Override
    public void setGames(UUID playerId, int games) {
        try {
            update("INSERT INTO " + playerStatsTable + " (uuid, games) VALUES (?, ?) ON DUPLICATE KEY UPDATE games = ?;", playerId.toString(),
                    games, games);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "SQL Exception", ex);
        }
    }

    @Override
    public void setWins(Player player, int wins) {
        setWins(player.getUniqueId(), wins);
    }

    @Override
    public void setWins(UUID playerId, int wins) {
        try {
            update("INSERT INTO " + playerStatsTable + " (uuid, wins) VALUES (?, ?) ON DUPLICATE KEY UPDATE wins = ?;", playerId.toString(),
                    wins, wins);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "SQL Exception", ex);
        }
    }

    @Override
    public Stats loadStats(Player player) {
        return loadStats(player.getUniqueId());
    }

    @Override
    public Stats loadStats(UUID playerId) {
        try {
            QueryResult result = query("SELECT * FROM " + playerStatsTable + " WHERE uuid=?", playerId.toString());
            if (result.size() > 0) {
                QueryResult.ResultData data = result.first();
                return new Stats(playerId, data.asInt("points"), data.asInt("kills"), data.asInt("games"), data.asInt("wins"));
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "SQL Exception", ex);
        }
        return new Stats(playerId, 0, 0, 0, 0);
    }


    private int update(String query, Object... args) throws SQLException {
        if (isConnected()) {
            PreparedStatement st = prepare(query, args);
            if (st != null) {
                int result = st.executeUpdate();
                st.close();
                return result;
            }
        }
        return -1;
    }

    private QueryResult query(String query, Object... args) throws SQLException {
        if (isConnected()) {
            PreparedStatement st = prepare(query, args);

            if (st != null) {
                ResultSet set = st.executeQuery();
                QueryResult result = new QueryResult(set);
                set.close();
                st.close();
                return result;
            }
        }
        return new QueryResult(null);
    }

    private PreparedStatement prepare(String query, Object... args) {
        try {
            PreparedStatement st = conn.prepareStatement(query);
            for (int i = 0; i < args.length; i++) {
                st.setObject(i + 1, args[i]);
            }
            return st;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "SQL Exception", ex);
            return null;
        }
    }

}
