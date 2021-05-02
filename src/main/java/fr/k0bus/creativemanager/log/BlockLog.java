package fr.k0bus.creativemanager.log;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

public class BlockLog {

    Block block;
    UUID uuid;
    OfflinePlayer player;

    public BlockLog(Block block, OfflinePlayer player)
    {
        this.block = block;
        this.player = player;
        this.uuid = UUID.randomUUID();
    }
    public BlockLog(Block block)
    {
        this.block = block;
        load();
    }
    public BlockLog(World world, Location location)
    {
        this.block = world.getBlockAt(location);
        load();
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public boolean isCreative()
    {
        return this.player != null;
    }

    private void load()
    {
        Connection conn = new DataManager("data").getConn();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM block_log WHERE world=? AND x=? AND y=? AND z=?");
            ps.setString(1, block.getWorld().getName());
            ps.setInt(2, block.getX());
            ps.setInt(3, block.getY());
            ps.setInt(4, block.getZ());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                uuid = UUID.fromString(rs.getString("uuid"));
                block = Bukkit.getWorld(rs.getString("world"))
                        .getBlockAt(rs.getInt("x"), rs.getInt("y"), rs.getInt("z"));
                player = Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("player")));
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Unable to retrieve connection", ex);
        }
    }
    public void save()
    {
        Connection conn = new DataManager("data").getConn();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("REPLACE INTO block_log (uuid,world,x,y, z, player) VALUES(?,?,?,?,?,?)");
            ps.setString(1, uuid.toString());
            ps.setString(2, block.getWorld().getName());
            ps.setInt(3, block.getX());
            ps.setInt(4, block.getY());
            ps.setInt(5, block.getZ());
            ps.setString(6, player.getUniqueId().toString());
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
    public void delete()
    {
        Connection conn = new DataManager("data").getConn();
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM block_log WHERE uuid=?");
            ps.setString(1, uuid.toString());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Unable to retrieve connection", ex);
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public Block getBlock() {
        return block;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }
}
