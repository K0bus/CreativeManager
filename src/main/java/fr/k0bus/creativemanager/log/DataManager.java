package fr.k0bus.creativemanager.log;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class DataManager {

    Connection conn;
    String dbname;
    HashMap<Location, BlockLog> blockBlockLogMap = new HashMap<>();
    List<UUID> toDelete = new ArrayList<>();
    public DataManager(String dbname)
    {
        this.dbname = dbname;
        this.conn = startConnection();
        init();
    }

    public Connection startConnection() {
        if (this.conn != null)
            return this.conn;
        File dataFolder = new File(Bukkit.getPluginManager().getPlugin("CreativeManager").getDataFolder(), this.dbname + ".db");
        if (!dataFolder.exists())
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                Bukkit.getLogger().log(Level.SEVERE, "File write error: " + this.dbname + ".db");
            }
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
        } catch (SQLException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "SQLite exception on initialize");
            Bukkit.getLogger().log(Level.SEVERE, ex.toString());
        } catch (ClassNotFoundException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
        }
        return null;
    }
    public void init() {
        try {
            this.conn = startConnection();
            Statement s = this.conn.createStatement();
            // Create table if not exist
            s.executeUpdate("CREATE TABLE IF NOT EXISTS block_log (`uuid` text NOT NULL,`world` text NOT NULL,`x` int NOT NULL,`y` int NOT NULL,`z` int NOT NULL,`player` text NOT NULL,PRIMARY KEY(`uuid`));");
            // Update table if exist
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadLog()
    {
        Connection conn = new DataManager("data").getConn();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM block_log");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Block block = Bukkit.getWorld(rs.getString("world")).getBlockAt(
                        rs.getInt("x"),
                        rs.getInt("y"),
                        rs.getInt("z")
                );
                OfflinePlayer player = Bukkit.getPlayer(UUID.fromString(rs.getString("player")));
                BlockLog blockLog = new BlockLog(block, player);
                blockLog.uuid = UUID.fromString(rs.getString("uuid"));
                blockBlockLogMap.put(block.getLocation(), blockLog);
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Unable to retrieve connection", ex);
        }
    }

    public HashMap<Location, BlockLog> getBlockBlockLogMap() {
        return blockBlockLogMap;
    }
    public void addLog(BlockLog log)
    {
        blockBlockLogMap.put(log.getBlock().getLocation(), log);
    }
    public void removeLog(Location loc)
    {
        blockBlockLogMap.remove(loc);
    }
    public void addToDelete(UUID id)
    {
        toDelete.add(id);
    }
    public List<UUID> getToDelete() {
        return toDelete;
    }
    public void resetToDelete()
    {
        toDelete = new ArrayList<>();
    }

    public Connection getConn() {
        return conn;
    }
}
