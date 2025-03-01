package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.creativemanager.utils.CMUtils;
import fr.k0bus.creativemanager.utils.SearchUtils;
import fr.k0bus.k0buscore.utils.StringUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
    CreativeManager plugin;
    public PlayerInteract(CreativeManager plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void checkBlacklistUse(PlayerInteractEvent e)
    {
        Player p = e.getPlayer();
        ItemStack itemStack = e.getItem();
        if (!p.getGameMode().equals(GameMode.CREATIVE)) return;
        if (itemStack == null) return;

        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            if(e.getClickedBlock() != null)
                if(itemStack.getType().isBlock())
                    return;

        String itemName = itemStack.getType().name().toLowerCase();
        if(p.hasPermission("creativemanager.bypass.blacklist.use")) return;
        if(p.hasPermission("creativemanager.bypass.blacklist.use." + itemName)) return;
        List<String> blacklist = CreativeManager.getSettings().getUseBL();
        if((CreativeManager.getSettings().getConfiguration().getString("list.mode.use").equals("whitelist") && !SearchUtils.inList(blacklist, itemStack)) ||
                (!CreativeManager.getSettings().getConfiguration().getString("list.mode.use").equals("whitelist") && SearchUtils.inList(blacklist, itemStack))){
            HashMap<String, String> replaceMap = new HashMap<>();
            replaceMap.put("{ITEM}", StringUtils.proper(itemName));
            CMUtils.sendMessage(p, "blacklist.use", replaceMap);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void checkContainer(PlayerInteractEvent e)
    {
        Player p = e.getPlayer();
        Block block = e.getClickedBlock();
        if (!p.getGameMode().equals(GameMode.CREATIVE)) return;
        if(!CreativeManager.getSettings().getProtection(Protections.CONTAINER)) return;
        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if(block == null) return;
        if(p.isSneaking() && p.getInventory().getItemInMainHand().getType().isBlock()) return;

        try {
            if(block.getType().equals(Material.CAMPFIRE) || block.getType().equals(Material.SOUL_CAMPFIRE)) {
                if(!p.getInventory().getItemInMainHand().getType().name().contains("SHOVEL"))
                {
                    if (!p.hasPermission("creativemanager.bypass.container")) {
                        if (CreativeManager.getSettings().getConfiguration().getBoolean("send-player-messages"))
                            CMUtils.sendMessage(p, "permission.container");
                        e.setCancelled(true);
                    }
                }
            }
        } catch (NoSuchFieldError ignored) {}
    }

    @EventHandler
    public void checkBlacklistUseBlock(PlayerInteractEvent e)
    {
        Player p = e.getPlayer();
        List<String> blacklist = CreativeManager.getSettings().getUseBlockBL();
        if (!p.getGameMode().equals(GameMode.CREATIVE)) return;
        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if(e.getClickedBlock() == null) return;
        if (e.getPlayer().isSneaking() && e.getItem() != null) return;
        if(blacklist.isEmpty()) return;
        if(p.hasPermission("creativemanager.bypass.blacklist.useblock")) return;
        if((CreativeManager.getSettings().getConfiguration().getString("list.mode.useblock").equals("whitelist") && !SearchUtils.inList(blacklist, e.getClickedBlock())) ||
                (!CreativeManager.getSettings().getConfiguration().getString("list.mode.useblock").equals("whitelist") && SearchUtils.inList(blacklist, e.getClickedBlock()))){
            if (CreativeManager.getSettings().getConfiguration().getBoolean("send-player-messages"))
                CMUtils.sendMessage(p, "blacklist.useblock");
            e.setCancelled(true);
        }
    }
    @EventHandler(priority=EventPriority.LOWEST)
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
                if (CreativeManager.getSettings().getConfiguration().getBoolean("send-player-messages"))
                    CMUtils.sendMessage(p, "permission.spawn");
                e.setCancelled(true);
            }
        }catch (ClassNotFoundException ignored){}
    }
}
