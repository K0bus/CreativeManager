package fr.k0bus.creativemanager.event;

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
                if (!p.hasPermission("creativemanager.container")) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("tag") + plugin.getLang().getString("permission.container")));
                    e.setCancelled(true);
                }
            } else if (e.getItem() instanceof SpawnEggMeta) {
                if (e.getItem().getItemMeta() instanceof SpawnEggMeta) {
                    if (!p.hasPermission("creativemanager.spawn")) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("tag") + plugin.getLang().getString("permission.spawn")));
                        e.setCancelled(true);
                    }
                } else if (plugin.getConfig().getList("blacklist.use").contains(e.getItem().getType().getKey().getKey())) {
                    if (!p.hasPermission("creativemanager.bypass.blacklist.use")) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("tag") + plugin.getLang().getString("blacklist.use").replace("{ITEM}", e.getItem().getType().getKey().getKey())));
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}
