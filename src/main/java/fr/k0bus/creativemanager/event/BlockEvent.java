package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.log.BlockLog;
import fr.k0bus.creativemanager.utils.BlockUtils;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.world.StructureGrowEvent;

public class BlockEvent implements Listener {

    private final CreativeManager plugin;

    public BlockEvent(CreativeManager cm)
    {
        plugin = cm;
    }

    /*@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    void onBlockPhysicsEvent(BlockPhysicsEvent event){
        try {
            Block block = event.getBlock();
            if(block.getType().equals(Material.AIR)) return;
            if(block.getBlockData().isSupported(block)) return;
            BlockLog blockLog = plugin.getDataManager().getBlockFrom(block.getLocation());
            if(blockLog != null && blockLog.isCreative())
            {
                event.setCancelled(true);
                block.setType(Material.AIR);
            }
        } catch (NoSuchMethodError ignored) {}
    }*/
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    void onBlockGrow(BlockGrowEvent event)
    {
        Block block = event.getBlock();
        switch (event.getBlock().getType())
        {
            case CACTUS:
            case SUGAR_CANE:
                BlockLog blockLog = plugin.getDataManager().getBlockFrom(block.getRelative(BlockFace.DOWN).getLocation());
                if(blockLog != null && blockLog.isCreative())
                {
                    plugin.getDataManager().addBlock(
                            new BlockLog(block, blockLog.getPlayer())
                    );
                }
                break;
            case PUMPKIN:
            case MELON:
                for(Block b: BlockUtils.getAdjacentBlocks(block))
                {
                    if(Tag.CROPS.isTagged(b.getType()))
                    {
                        BlockLog bLog = plugin.getDataManager().getBlockFrom(b.getLocation());
                        if(bLog != null && bLog.isCreative())
                        {
                            plugin.getDataManager().addBlock(
                                    new BlockLog(block, bLog.getPlayer())
                            );
                        }
                    }
                }
                break;
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    void onStructureGrow(StructureGrowEvent event){
        BlockLog blockLog = plugin.getDataManager().getBlockFrom(event.getLocation());
        if(blockLog != null && blockLog.isCreative())
        {
            for(BlockState block: event.getBlocks())
            {
                plugin.getDataManager().addBlock(
                        new BlockLog(block.getBlock(), blockLog.getPlayer())
                );
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    void onBlockSpread(BlockSpreadEvent event)
    {
        BlockLog blockLog = plugin.getDataManager().getBlockFrom(event.getSource().getLocation());
        if(blockLog != null && blockLog.isCreative())
        {
            plugin.getDataManager().addBlock(
                    new BlockLog(event.getBlock(), blockLog.getPlayer())
            );
        }
    }
}
