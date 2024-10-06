package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

public class WorldEvent implements Listener {
    /**
     * Instantiates a new Inventory open.
     *
     */
    CreativeManager plugin;
    public WorldEvent(CreativeManager plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMapInit(WorldLoadEvent event)
    {
        plugin.getDataManager().load(event.getWorld());
    }
}
