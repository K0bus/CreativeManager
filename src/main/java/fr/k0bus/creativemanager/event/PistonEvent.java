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
	 * @param e the event.
	 */
	@EventHandler(ignoreCancelled = true)
	public void onExtend(BlockPistonExtendEvent e) {
		BlockFace pistonDirection = e.getDirection();
		for (Block toMoveBlock : e.getBlocks()) {
			BlockLog blockLog = plugin.getDataManager().getBlockFrom(toMoveBlock.getLocation());
			if (blockLog != null) {
				if (blockLog.isCreative()) {
					Location movedBlock = toMoveBlock.getLocation().add(pistonDirection.getModX(),
							pistonDirection.getModY(),
							pistonDirection.getModZ());
					plugin.getDataManager().moveBlock(toMoveBlock.getLocation(), movedBlock);
				}
			}
		}
	}

	/**
	 * On retract.
	 *
	 * @param e the event.
	 */
	@EventHandler(ignoreCancelled = true)
	public void onRetract(BlockPistonRetractEvent e) {
		BlockFace pistonDirection = e.getDirection();
		for (Block toMoveBlock : e.getBlocks()) {
			BlockLog blockLog = plugin.getDataManager().getBlockFrom(toMoveBlock.getLocation());
			if (blockLog != null) {
				if (blockLog.isCreative()) {
					Location movedBlock = toMoveBlock.getLocation().add(pistonDirection.getModX(),
							pistonDirection.getModY(),
							pistonDirection.getModZ());
					plugin.getDataManager().moveBlock(toMoveBlock.getLocation(), movedBlock);
				}
			}
		}
	}
}
