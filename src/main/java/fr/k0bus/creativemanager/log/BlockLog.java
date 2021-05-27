package fr.k0bus.creativemanager.log;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;

import java.util.UUID;

/**
 * Block log class.
 */
public class BlockLog {
    private final UUID uuid;
    private final OfflinePlayer player;
    private Location location;

    /**
     * Instantiates a new Block log.
     *
     * @param block  the block.
     * @param player the player.
     */
    public BlockLog(Block block, OfflinePlayer player) {
        this.location = block.getLocation();
        this.player = player;
        this.uuid = UUID.randomUUID();
    }

    /**
     * Instantiates a new Block log.
     *
     * @param location the location.
     * @param player   the player.
     * @param uuid     the uuid.
     */
    public BlockLog(Location location, OfflinePlayer player, UUID uuid) {
        this.location = location;
        this.player = player;
        this.uuid = uuid;
    }

    /**
     * Is creative.
     *
     * @return True if yes, otherwise false.
     */
    public boolean isCreative() {
        return this.player != null;
    }

    /**
     * Gets uuid.
     *
     * @return the uuid.
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Gets location.
     *
     * @return the location.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets location.
     *
     * @param location the location.
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Gets player.
     *
     * @return the player.
     */
    public OfflinePlayer getPlayer() {
        return player;
    }
}
