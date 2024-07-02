package fr.k0bus.creativemanager.utils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Piston;
import org.bukkit.block.data.type.PistonHead;

import java.util.List;

public class BlockUtils {
    public static List<Block> getBlockStructure(Block block) {
        try {
            BlockData blockData = block.getBlockData();
            if (blockData instanceof Piston piston) {
                Block pistonHead = block.getRelative(piston.getFacing());
                if (pistonHead.getType() == Material.PISTON_HEAD) {
                    return List.of(block, pistonHead);
                }
            }
            if (blockData instanceof PistonHead pistonHead) {
                Block piston = block.getRelative(pistonHead.getFacing().getOppositeFace());
                if (piston.getType() == Material.PISTON || piston.getType() == Material.STICKY_PISTON) {
                    return List.of(block, piston);
                }
            }
            if (blockData instanceof Bed bed) {
                BlockFace relative = bed.getPart() == Bed.Part.HEAD ? bed.getFacing() : bed.getFacing().getOppositeFace();
                Block otherPart = block.getRelative(relative);
                if (otherPart.getBlockData() instanceof Bed otherBedPart && otherBedPart.getPart() != bed.getPart()
                        && otherBedPart.getFacing() == bed.getFacing()) {
                    return List.of(block, otherPart);
                }
            }
            if (blockData instanceof Door door) {
                Block otherPart = door.getHalf() == Bisected.Half.BOTTOM ? block.getRelative(BlockFace.UP) : block.getRelative(BlockFace.DOWN);
                if (otherPart.getType() == blockData.getMaterial()) {
                    return List.of(block, otherPart);
                }
            }
        } catch (NoSuchMethodError ignored) {}
        return List.of(block);
    }
    public static List<Block> getAdjacentBlocks(Block block) {
        return List.of(block.getRelative(BlockFace.NORTH),
                block.getRelative(BlockFace.EAST),
                block.getRelative(BlockFace.SOUTH),
                block.getRelative(BlockFace.WEST));
    }
}
