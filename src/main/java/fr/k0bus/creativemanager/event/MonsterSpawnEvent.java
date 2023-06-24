package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.log.BlockLog;
import fr.k0bus.creativemanager.settings.Protections;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Monster spawn event listener.
 */
public class MonsterSpawnEvent implements Listener {

    private final CreativeManager plugin;

    /**
     * Instantiates a new Monster spawn event.
     *
     * @param plugin the plugin.
     */
    public MonsterSpawnEvent(CreativeManager plugin) {
        this.plugin = plugin;
    }

    /**
     * On spawn.
     *
     * @param e the event.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSpawn(CreatureSpawnEvent e) {
        if (!CreativeManager.getSettings().getProtection(Protections.SPAWN_BUILD)) return;
        Block baseBlock = e.getLocation().getBlock();
        List<Block> blockList = new ArrayList<>();
        if (e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.BUILD_SNOWMAN)) {
            // Check body
            blockList.add(baseBlock);
            blockList.add(baseBlock.getRelative(0, 1, 0));
            blockList.add(baseBlock.getRelative(0, 2, 0));
        } else if (e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.BUILD_IRONGOLEM)) {
            // Check body
            blockList.add(baseBlock);
            blockList.add(baseBlock.getRelative(0, 1, 0));
            blockList.add(baseBlock.getRelative(0, 2, 0));
            // Check arms
            blockList.add(baseBlock.getRelative(0, 1, 1));
            blockList.add(baseBlock.getRelative(0, 1, -1));
            // Check arms
            blockList.add(baseBlock.getRelative(1, 1, 0));
            blockList.add(baseBlock.getRelative(-1, 1, 0));
        } else if (e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.BUILD_WITHER)) {
            // Check body
            blockList.add(baseBlock);
            blockList.add(baseBlock.getRelative(0, 1, 0));
            blockList.add(baseBlock.getRelative(0, 2, 0));
            // Check arms / head
            blockList.add(baseBlock.getRelative(0, 1, 1));
            blockList.add(baseBlock.getRelative(0, 2, 1));
            blockList.add(baseBlock.getRelative(0, 1, -1));
            blockList.add(baseBlock.getRelative(0, 2, -1));
            // Check arms / head
            blockList.add(baseBlock.getRelative(1,1,0));
            blockList.add(baseBlock.getRelative(1,2,0));
            blockList.add(baseBlock.getRelative(-1,1,0));
            blockList.add(baseBlock.getRelative(-1,2,0));
        }
        for (Block block:blockList) {
            BlockLog log = plugin.getDataManager().getBlockFrom(block.getLocation());
            if (log != null) {
                Player player = Bukkit.getPlayer(log.getPlayer().getUniqueId());
                if (player != null)
                    if (player.hasPermission("creativemanager.bypass.spawn_build"))
                        continue;
                e.setCancelled(true);
                return;
            }
        }
    }
}
