package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.log.BlockLog;
import fr.k0bus.creativemanager.settings.Protections;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.util.UUID;

public class ExplodeEvent implements Listener {

    private final CreativeManager plugin;

    private static final String UUID_ID = "/UUID";
    private static final String DATE_ID = "/DATE";

    public ExplodeEvent(CreativeManager cm)
    {
        plugin = cm;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
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
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockChange(EntityChangeBlockEvent e)
    {
        if (e.getTo().equals(Material.AIR) && !((e.getEntity() instanceof FallingBlock))) {
            BlockLog blockLog = plugin.getDataManager().getBlockFrom(e.getBlock().getLocation());
            if (blockLog != null) {
                if (blockLog.isCreative()) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFallBlock(EntityChangeBlockEvent event) {
        BlockLog blockLog = plugin.getDataManager().getBlockFrom(event.getBlock().getLocation());
        if (blockLog != null) {
            UUID uuid = blockLog.getPlayer().getUniqueId();
            if(event.getEntity() instanceof FallingBlock fallingBlock)
            {
                register(fallingBlock, uuid);
                fallingBlock.setDropItem(false);
            }
            if(event.getTo().equals(Material.AIR))
            {
                plugin.getDataManager().removeBlock(blockLog.getLocation());
            }
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFallBlockStop(EntityChangeBlockEvent event) {
        if(event.getEntity() instanceof FallingBlock fallingBlock && !event.getTo().equals(Material.AIR)) {
            UUID uuid = findPlayer(fallingBlock);
            if(uuid == null) return;
            if(!event.getTo().equals(Material.AIR))
            {
                plugin.getDataManager().addBlock(
                        new BlockLog(event.getBlock(), Bukkit.getPlayer(uuid))
                );
            }
        }
    }

    public static void register(Entity entity, UUID uuid)
    {
        NamespacedKey namespacedKeyUuid = new NamespacedKey(CreativeManager.getInstance(), UUID_ID);
        entity.getPersistentDataContainer()
                .set(namespacedKeyUuid, PersistentDataType.STRING, uuid.toString());
        NamespacedKey namespacedKeyDate = new NamespacedKey(CreativeManager.getInstance(), DATE_ID);
        entity.getPersistentDataContainer()
                .set(namespacedKeyDate, PersistentDataType.LONG, System.currentTimeMillis());
    }
    public static void unregister(Entity entity)
    {
        NamespacedKey namespacedKeyUuid = new NamespacedKey(CreativeManager.getInstance(), UUID_ID);
        entity.getPersistentDataContainer().remove(namespacedKeyUuid);
        NamespacedKey namespacedKeyDate = new NamespacedKey(CreativeManager.getInstance(), DATE_ID);
        entity.getPersistentDataContainer().remove(namespacedKeyDate);
    }
    @Nullable
    public static UUID findPlayer(Entity entity)
    {
        NamespacedKey namespacedKeyUuid = new NamespacedKey(CreativeManager.getInstance(), UUID_ID);
        String UUIDText = entity.getPersistentDataContainer().get(namespacedKeyUuid, PersistentDataType.STRING);
        if(UUIDText == null)
            return null;
        return UUID.fromString(UUIDText);
    }
    public static long findDate(Entity entity)
    {
        if(entity == null) return 0;
        NamespacedKey namespacedKeyDate = new NamespacedKey(CreativeManager.getInstance(), DATE_ID);
        return entity.getPersistentDataContainer().get(namespacedKeyDate, PersistentDataType.LONG);
        //TODO: Check warning
    }

}
