package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.k0buslib.utils.Messages;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

/**
 * Inventory open listener.
 */
public class InventoryOpen implements Listener {
    private final CreativeManager plugin;

    /**
     * Instantiates a new Inventory open.
     *
     * @param instance the instance
     */
    public InventoryOpen(CreativeManager instance) {
        plugin = instance;
    }

    /**
     * On inventory open.
     *
     * @param e the event.
     */
    @EventHandler(ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent e) {
        if (e.getPlayer() instanceof Player) {
            Player p = (Player) e.getPlayer();
            if (p.getGameMode().equals(GameMode.CREATIVE) && plugin.getSettings().getProtection(Protections.CONTAINER)) {
                if (isProtectedChest(e.getInventory())) {
                    if (!p.hasPermission("creativemanager.bypass.container")) {
                        if (plugin.getSettings().getBoolean("send-player-messages"))
                            Messages.sendMessage(plugin.getMessageManager(), p, "permission.container");
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    /**
     * Whether or not chest is protected.
     *
     * @param inventory the inventory.
     * @return True if yes, otherwise false.
     */
    public boolean isProtectedChest(Inventory inventory) {
        if (inventory.getType().equals(InventoryType.ENDER_CHEST)) return true;
        if (getProtectedType().contains(inventory.getType())) {
            if (inventory.getHolder() != null)
                return inventory.getHolder().getClass().toString().contains("org.bukkit");
        }
        return false;
    }

    /**
     * Gets protected type.
     *
     * @return the protected type.
     */
    public List<InventoryType> getProtectedType() {
        List<InventoryType> typeList = new ArrayList<>();
        typeList.add(InventoryType.CHEST);
        typeList.add(InventoryType.FURNACE);
        typeList.add(InventoryType.BLAST_FURNACE);
        typeList.add(InventoryType.BARREL);
        typeList.add(InventoryType.BEACON);
        typeList.add(InventoryType.BREWING);
        typeList.add(InventoryType.DISPENSER);
        typeList.add(InventoryType.DROPPER);
        typeList.add(InventoryType.HOPPER);
        typeList.add(InventoryType.SHULKER_BOX);
        typeList.add(InventoryType.LECTERN);
        return typeList;
    }
}
