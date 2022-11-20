package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.settings.Protections;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.HashMap;

/**
 * Player interact at entity event listener.
 */
public class PlayerInteractAtEntity implements Listener {

    /**
     * Instantiates a new Player interact at entity.
     *
     */
    public PlayerInteractAtEntity() {
    }

    /**
     * On use.
     *
     * @param e the event.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onUse(PlayerInteractAtEntityEvent e) {
        Player p = e.getPlayer();
        if(!p.getGameMode().equals(GameMode.CREATIVE)) return;
        if(Bukkit.getServer().getPluginManager().isPluginEnabled("Citizens"))
            if(e.getRightClicked().hasMetadata("NPC"))
            {
                if(CreativeManager.getSettings().getProtection(Protections.PL_CITIZENS) && !p.hasPermission("creativemanager.bypass.entity"))
                {
                    HashMap<String, String> replaceMap = new HashMap<>();
                    replaceMap.put("{PLUGIN}", "Citizens");
                    CreativeManager.sendMessage(p, CreativeManager.TAG + CreativeManager.getLang().getString("permission.plugins", replaceMap));
                    e.setCancelled(true);
                }
                return;
            }
        if (CreativeManager.getSettings().getProtection(Protections.ENTITY) && !p.hasPermission("creativemanager.bypass.entity")) {
            if (!p.hasPermission("creativemanager.bypass.entity") && !p.hasPermission("creativemanager.bypass.entity." + e.getRightClicked().getType().name().toLowerCase())) {
                if (CreativeManager.getSettings().getBoolean("send-player-messages"))
                    CreativeManager.sendMessage(p, CreativeManager.TAG + CreativeManager.getLang().getString("permission.entity"));
                e.setCancelled(true);
            }
        }
    }
}
