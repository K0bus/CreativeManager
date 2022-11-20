package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.creativemanager.utils.SearchUtils;
import fr.k0bus.k0buscore.utils.StringUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;

import java.util.HashMap;
import java.util.List;

/**
 * Player interact event listener.
 */
public class PlayerInteract implements Listener {

    /**
     * Instantiates a new Player interact.
     *
     */
    public PlayerInteract() {
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
        if(p.hasPermission("creativemanager.bypass.blacklist.use." + itemName)) return;
        if(p.hasPermission("creativemanager.bypass.blacklist.use" + itemName)) return; // TODO: To remove
        List<String> blacklist = CreativeManager.getSettings().getUseBL();
        if(SearchUtils.inList(blacklist, itemName))
        {
            HashMap<String, String> replaceMap = new HashMap<>();
            replaceMap.put("{ITEM}", StringUtils.proper(itemName));
            CreativeManager.sendMessage(p, CreativeManager.TAG + CreativeManager.getLang().getString("blacklist.use", replaceMap));
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void checkBlacklistUseBlock(PlayerInteractEvent e)
    {
        Player p = e.getPlayer();
        List<String> blacklist = CreativeManager.getSettings().getUseBlockBL();
        if (!p.getGameMode().equals(GameMode.CREATIVE)) return;
        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if(e.getClickedBlock() == null) return;
        String itemName = e.getClickedBlock().getType().name().toLowerCase();
        if(blacklist.isEmpty()) return;
        if(p.hasPermission("creativemanager.bypass.blacklist.useblock")) return;
        if(SearchUtils.inList(blacklist, itemName))
        {
            if (CreativeManager.getSettings().getBoolean("send-player-messages"))
                CreativeManager.sendMessage(p, CreativeManager.TAG + CreativeManager.getLang().getString("blacklist.useblock"));
            e.setCancelled(true);
        }
    }
    @EventHandler(priority=EventPriority.HIGHEST)
    public void checkSpawnEgg(PlayerInteractEvent e)
    {
        Player p = e.getPlayer();
        ItemStack itemStack = e.getItem();
        if (!p.getGameMode().equals(GameMode.CREATIVE)) return;
        if (itemStack == null) return;
        if(p.hasPermission("creativemanager.bypass.spawn_egg")) return;
        if(!CreativeManager.getSettings().getProtection(Protections.SPAWN)) return;
        try {
            Class.forName( "org.bukkit.inventory.meta.SpawnEggMeta" );
            if (itemStack.getItemMeta() instanceof SpawnEggMeta) {
                if (CreativeManager.getSettings().getBoolean("send-player-messages"))
                    CreativeManager.sendMessage(p, CreativeManager.TAG + CreativeManager.getLang().getString("permission.spawn"));
                e.setCancelled(true);
            }
        }catch (ClassNotFoundException ignored){}
    }
}
