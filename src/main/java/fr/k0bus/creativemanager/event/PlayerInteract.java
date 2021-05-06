package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.creativemanager.utils.Messages;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.meta.SpawnEggMeta;

import fr.k0bus.creativemanager.CreativeManager;

import java.util.HashMap;

public class PlayerInteract implements Listener {

    CreativeManager plugin;

    public PlayerInteract(CreativeManager instance) {
        plugin = instance;
    }

    @EventHandler(ignoreCancelled = true)
    public void onUse(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && p.getGameMode().equals(GameMode.CREATIVE)) {
            if (e.getClickedBlock().getState() instanceof InventoryHolder || e.getClickedBlock().getType().equals(Material.ENDER_CHEST)) {
                if (!p.hasPermission("creativemanager.container") && plugin.getSettings().getProtection(Protections.CONTAINER)) {
                    Messages.sendMessage(plugin, p, "permission.container");
                    e.setCancelled(true);
                }
            } else if (e.getItem() instanceof SpawnEggMeta) {
                if (e.getItem().getItemMeta() instanceof SpawnEggMeta) {
                    if (!p.hasPermission("creativemanager.bypass.spawn_egg") && plugin.getSettings().getProtection(Protections.SPAWN)) {
                        Messages.sendMessage(plugin, p, "permission.spawn");
                        e.setCancelled(true);
                    }
                }
            } else if (e.getItem() != null) {
                if(plugin.getSettings().getUseBL().contains(e.getItem().getType().name()))
                    if (!p.hasPermission("creativemanager.bypass.blacklist.use")) {
                        HashMap<String, String> replaceMap = new HashMap<>();
                        replaceMap.put("{ITEM}", e.getItem().getType().name());
                        Messages.sendMessage(plugin, p, "blacklist.use", replaceMap);
                        e.setCancelled(true);
                    }
            }
        }
    }
}
