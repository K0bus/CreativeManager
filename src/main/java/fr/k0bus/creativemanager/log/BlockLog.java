package fr.k0bus.creativemanager.log;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;

import java.util.UUID;

public class BlockLog {

    private Location location;
    private UUID uuid;
    private OfflinePlayer player;
    private boolean saved;

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
        this.saved = false;
    }


    public boolean isCreative()
    {
        return this.player != null;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
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
