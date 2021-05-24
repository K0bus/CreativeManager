package fr.k0bus.creativemanager.log;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.k0buslib.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class DataManager {

    Connection conn;
    String dbname;
    HashMap<Location, BlockLog> blockLogHashMap;
    CreativeManager plugin;

    public DataManager(String dbname, CreativeManager plugin)
    {
        this.blockLogHashMap = new HashMap<>();
        this.dbname = dbname;
        this.plugin = plugin;
        this.conn = startConnection();
        init();
        load();
    }

    public BlockLog getBlockFrom(Location location)
    {
        return blockLogHashMap.get(location);
    }
    public void moveBlock(Location from, Location to)
    {
        if(blockLogHashMap.containsKey(from))
        {
            BlockLog blockLog = blockLogHashMap.get(from);
            blockLog.setLocation(to);
            blockLogHashMap.put(to, blockLog);
            blockLogHashMap.remove(from);
        }
    }
    public void removeBlock(Location location)
    {
        BlockLog blockLog = blockLogHashMap.get(location);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> delete(blockLog));
        blockLogHashMap.remove(location);
    }

    public void addBlock(BlockLog log)
    {
        blockLogHashMap.put(log.getLocation(), log);
    }

    public void save()
    {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            int n = 0;
            HashMap<Location, BlockLog> cloned = new HashMap<>(blockLogHashMap);
            for (BlockLog log: cloned.values()) {
                save(log);
                n++;
            }
            if(plugin.getSettings().getBoolean("save-log"))
                Messages.log(plugin,
                        "&2Log saved to database ! &7[" + n + "]");
        });
    }
    public void saveSync()
    {
        int n = 0;
        HashMap<Location, BlockLog> cloned = new HashMap<>(blockLogHashMap);
        for (BlockLog log: cloned.values()) {
            save(log);
            n++;
        }
        Messages.log(plugin,
                "&2Log saved to database ! &7[" + n + "]");
    }

    public void delete(BlockLog log)
    {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM block_log WHERE uuid=?");
            ps.setString(1, log.getUuid().toString());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Unable to retrieve connection", ex);
        }
    }
    public void save(BlockLog log)
    {
        if(log.getLocation().getWorld() == null){
            delete(log);
            return;
        }
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("REPLACE INTO block_log (uuid,world,x,y, z, player) VALUES(?,?,?,?,?,?)");
            ps.setString(1, log.getUuid().toString());
            ps.setString(2, log.getLocation().getWorld().getName());
            ps.setInt(3, log.getLocation().getBlockX());
            ps.setInt(4, log.getLocation().getBlockY());
            ps.setInt(5, log.getLocation().getBlockZ());
            ps.setString(6, log.getPlayer().getUniqueId().toString());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Bukkit.getLogger().log(Level.SEVERE, ex.toString());
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException ex) {
                Bukkit.getLogger().log(Level.SEVERE, ex.toString());
            }
        }
    }

    public Connection startConnection() {
        if (this.conn != null)
            return this.conn;
        File dataFolder = new File(plugin.getDataFolder(), this.dbname + ".db");
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

    private void load()
    {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM block_log");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("player")));
                Location location = new Location(Bukkit.getWorld(rs.getString("world")),
                        rs.getInt("x"),
                        rs.getInt("y"),
                        rs.getInt("z"));
                UUID uuid = UUID.fromString(rs.getString("uuid"));
                blockLogHashMap.put(location, new BlockLog(location, player, uuid));
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Unable to retrieve connection", ex);
        }
    }

    public HashMap<Location, BlockLog> getBlockLogHashMap() {
        return blockLogHashMap;
    }
}
