package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.log.BlockLog;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Piston event listener.
 */
public class PistonEvent implements Listener {
    private final CreativeManager plugin;

    /**
     * Instantiates a new Piston event.
     *
     * @param instance the instance.
     */
    public PistonEvent(CreativeManager instance) {
        plugin = instance;
    }

    /**
     * On extend.
     *
     * @param event the event.
     */
    @EventHandler(ignoreCancelled = true)
    public void onExtend(BlockPistonExtendEvent event) {
        BlockFace pistonDirection = event.getDirection();
        List<Block> blocks = new ArrayList<>(event.getBlocks());
        this.pistonCheck(pistonDirection, blocks);
    }

    /**
     * On retract.
     *
     * @param event the event.
     */
    @EventHandler(ignoreCancelled = true)
    public void onRetract(BlockPistonRetractEvent event) {
        BlockFace pistonDirection = event.getDirection();
        List<Block> blocks = new ArrayList<>(event.getBlocks());
        this.pistonCheck(pistonDirection, blocks);
    }

    private void pistonCheck(BlockFace blockFace, List<Block> blocks) {
        // Разработано для: ZDStudio(https://vk.com/zdstudio_ru)
        Collections.reverse(blocks);
        for (Block toMoveBlock : blocks) {
            BlockLog blockLog = plugin.getDataManager().getBlockFrom(toMoveBlock.getLocation());
            if (blockLog != null && blockLog.isCreative()) {
                Location movedBlock = toMoveBlock.getRelative(blockFace).getLocation();
                plugin.getDataManager().moveBlock(toMoveBlock.getLocation(), movedBlock);
            }
        }
    }
}
