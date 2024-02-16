package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.log.BlockLog;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

public class FlowEvent implements Listener {

    private final CreativeManager plugin;

    public FlowEvent(CreativeManager plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    void onBlockFromTo(BlockFromToEvent event) {
        BlockLog blockLog = plugin.getDataManager().getBlockFrom(event.getToBlock().getLocation());
        if(blockLog != null && blockLog.isCreative())
        {
            event.setCancelled(true);
            event.getToBlock().setType(Material.AIR);
            plugin.getDataManager().removeBlock(blockLog.getLocation());
        }
    }
}
