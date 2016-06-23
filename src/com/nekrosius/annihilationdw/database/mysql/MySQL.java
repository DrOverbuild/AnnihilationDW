package com.nekrosius.annihilationdw.database.mysql;

import com.nekrosius.annihilationdw.database.Database;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySQL implements Database {

    public static Logger logger = Logger.getLogger("Annihilation-MySQL");
    private static final String playerPointsTable = "player_points";

    private Connection conn;

    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final String database;

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
                update("CREATE TABLE IF NOT EXISTS " + playerPointsTable + "(uuid CHAR(36), points BIGINT, PRIMARY KEY(uuid));");
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "SQL Exception", ex);
            }
        }
    }

    @Override
    public int getPoints(Player player) {
        return getPoints(player.getUniqueId());
    }

    @Override
    public int getPoints(UUID playerId) {
        try {
            QueryResult result = query("SELECT points FROM " + playerPointsTable + " WHERE uuuid=?", playerId.toString());
            if (result.size() > 0) {
                return result.first().asInt("points");
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "SQL Exception", ex);
        }
        return 0;
    }

    @Override
    public void setPoints(Player player, int points) {
        setPoints(player.getUniqueId(), points);
    }

    @Override
    public void setPoints(UUID playerId, int points) {
        try {
            update("INSERT INTO " + playerPointsTable + " (uuid, points) VALUES (?, ?) ON DUPLICATE KEY UPDATE points = ?;", playerId.toString(),
                    points, points);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "SQL Exception", ex);
        }
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
