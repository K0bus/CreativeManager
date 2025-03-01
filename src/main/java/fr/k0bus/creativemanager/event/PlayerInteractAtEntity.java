package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.creativemanager.utils.CMUtils;
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
    CreativeManager plugin;
    public PlayerInteractAtEntity(CreativeManager plugin) {
        this.plugin = plugin;
    }

    /**
     * On use.
     *
     * @param e the event.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
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
                    CMUtils.sendMessage(p, "permission.plugins", replaceMap);
                    e.setCancelled(true);
                }
                return;
            }
        if (CreativeManager.getSettings().getProtection(Protections.ENTITY) && !p.hasPermission("creativemanager.bypass.entity")) {
            if (!p.hasPermission("creativemanager.bypass.entity") && !p.hasPermission("creativemanager.bypass.entity." + e.getRightClicked().getType().name().toLowerCase())) {
                if (CreativeManager.getSettings().getConfiguration().getBoolean("send-player-messages"))
                    CMUtils.sendMessage(p, "permission.entity");
                e.setCancelled(true);
            }
        }
    }
}
