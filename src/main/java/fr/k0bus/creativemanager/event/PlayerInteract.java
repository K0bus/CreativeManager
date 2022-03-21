package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.k0buslib.utils.Messages;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;

import java.util.HashMap;
import java.util.List;

/**
 * Player interact event listener.
 */
public class PlayerInteract implements Listener {
    private final CreativeManager plugin;

    /**
     * Instantiates a new Player interact.
     *
     * @param instance the instance.
     */
    public PlayerInteract(CreativeManager instance) {
        plugin = instance;
    }

    @EventHandler
    public void checkBlacklistUse(PlayerInteractEvent e)
    {
        Player p = e.getPlayer();
        ItemStack itemStack = e.getItem();
        if (!p.getGameMode().equals(GameMode.CREATIVE)) return;
        if (itemStack == null) return;
        String itemName = itemStack.getType().name().toLowerCase();
        if(p.hasPermission("creativemanager.bypass.blacklist.use")) return;
        if(p.hasPermission("creativemanager.bypass.blacklist.use" + itemName)) return;
        List<String> blacklist = plugin.getSettings().getUseBL();
        if(blacklist.isEmpty()) return;
        if (blacklist.stream().anyMatch(itemName::equalsIgnoreCase))
        {
            HashMap<String, String> replaceMap = new HashMap<>();
            replaceMap.put("{ITEM}", itemName);
            Messages.sendMessage(plugin.getMessageManager(), p, "blacklist.use", replaceMap);
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void checkBlacklistUseBlock(PlayerInteractEvent e)
    {
        Player p = e.getPlayer();
        ItemStack itemStack = e.getItem();
        List<String> blacklist = plugin.getSettings().getUseBlockBL();
        if (!p.getGameMode().equals(GameMode.CREATIVE)) return;
        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (itemStack == null) return;
        String itemName = itemStack.getType().name().toLowerCase();
        if(blacklist.isEmpty()) return;
        if(p.hasPermission("creativemanager.bypass.blacklist.useblock")) return;
        if(blacklist.stream().anyMatch(itemName::equalsIgnoreCase))
        {
            if (plugin.getSettings().getBoolean("send-player-messages"))
                Messages.sendMessage(plugin.getMessageManager(), p, "blacklist.useblock");
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void checkSpawnEgg(PlayerInteractEvent e)
    {
        Player p = e.getPlayer();
        ItemStack itemStack = e.getItem();
        if (!p.getGameMode().equals(GameMode.CREATIVE)) return;
        if (itemStack == null) return;
        if(p.hasPermission("creativemanager.bypass.spawn_egg")) return;
        if(!plugin.getSettings().getProtection(Protections.SPAWN)) return;
        try {
            Class.forName( "org.bukkit.inventory.meta.SpawnEggMeta" );
            if (itemStack.getItemMeta() instanceof SpawnEggMeta) {
                if (plugin.getSettings().getBoolean("send-player-messages"))
                    Messages.sendMessage(plugin.getMessageManager(), p, "permission.spawn");
                e.setCancelled(true);
            }
        }catch (ClassNotFoundException ignored){}
    }
}
