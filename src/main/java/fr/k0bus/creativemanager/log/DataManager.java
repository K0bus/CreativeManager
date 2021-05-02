package fr.k0bus.creativemanager.log;

import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public class DataManager {

    Connection conn;
    String dbname;
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

    public Connection getConn() {
        return conn;
    }
}
