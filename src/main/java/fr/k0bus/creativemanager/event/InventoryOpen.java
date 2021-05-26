package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.k0buslib.utils.Messages;
import org.bukkit.GameMode;
import org.bukkit.block.EnderChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

public class InventoryOpen implements Listener {
    CreativeManager plugin;

    public InventoryOpen(CreativeManager instance) {
        plugin = instance;
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent e) {
        if(e.getPlayer() instanceof Player)
        {
            Player p = (Player) e.getPlayer();
            if(plugin.getSettings().getProtection(Protections.CONTAINER) &&
                    p.getGameMode().equals(GameMode.CREATIVE) &&
                    e.getInventory().getType().equals(InventoryType.CHEST))
            {
                if (!p.hasPermission("creativemanager.bypass.container")) {
                    if(plugin.getSettings().getBoolean("send-player-messages"))
                        Messages.sendMessage(plugin.getMessageManager(), p, "permission.container");
                    e.setCancelled(true);
                }
            }
            if(e.getInventory().equals(e.getPlayer().getEnderChest()))
            {
                if (!p.hasPermission("creativemanager.bypass.container")) {
                    if(plugin.getSettings().getBoolean("send-player-messages"))
                        Messages.sendMessage(plugin.getMessageManager(), p, "permission.container");
                    e.setCancelled(true);
                }
            }
        }
    }
}
