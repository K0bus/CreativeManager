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

    Location location;
    UUID uuid;
    OfflinePlayer player;

    public BlockLog(Block block, OfflinePlayer player)
    {
        this.location = block.getLocation();
        this.player = player;
        this.uuid = UUID.randomUUID();
    }
    public BlockLog(Location location, OfflinePlayer player, UUID uuid)
    {
        this.location = location;
        this.player = player;
        this.uuid = uuid;
    }
    public void setLocation(Location location) {
        this.location = location;
    }


    public boolean isCreative()
    {
        return this.player != null;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Location getLocation() {
        return location;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }
}
