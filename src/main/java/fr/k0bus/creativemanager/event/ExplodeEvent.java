package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.log.BlockLog;
import fr.k0bus.creativemanager.settings.Protections;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class ExplodeEvent implements Listener {

    private final CreativeManager plugin;

    public ExplodeEvent(CreativeManager cm)
    {
        plugin = cm;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockBreak(BlockExplodeEvent e) {
        if (CreativeManager.getSettings().getProtection(Protections.LOOT)) {
            for(Block block: e.blockList())
            {
                BlockLog blockLog = plugin.getDataManager().getBlockFrom(block.getLocation());
                if (blockLog != null) {
                    if (blockLog.isCreative()) {
                        block.setType(Material.AIR);
                        plugin.getDataManager().removeBlock(blockLog.getLocation());
                    }
                }
            }
        }
    }
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockBreak(EntityExplodeEvent e) {
        if (CreativeManager.getSettings().getProtection(Protections.LOOT)) {
            for(Block block: e.blockList())
            {
                BlockLog blockLog = plugin.getDataManager().getBlockFrom(block.getLocation());
                if (blockLog != null) {
                    if (blockLog.isCreative()) {
                        block.setType(Material.AIR);
                        plugin.getDataManager().removeBlock(blockLog.getLocation());
                    }
                }
            }
        }
    }
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockChange(EntityChangeBlockEvent e)
    {
        BlockLog blockLog = plugin.getDataManager().getBlockFrom(e.getBlock().getLocation());
        if (blockLog != null) {
            if (blockLog.isCreative()) {
                e.setCancelled(true);
            }
        }
    }
}
