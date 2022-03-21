package fr.k0bus.creativemanager.event.plugin;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.k0buslib.utils.Messages;
import io.github.thebusybiscuit.slimefun4.api.events.MultiBlockInteractEvent;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;

import java.util.HashMap;
import java.util.Set;

public class SlimeFun implements Listener {

    final CreativeManager plugin;

    public SlimeFun(CreativeManager plugin)
    {
        this.plugin = plugin;
        PluginManager pm = plugin.getServer().getPluginManager();
        Set<Permission> permissions = pm.getPermissions();
        Permission perm = new Permission("creativemanager.bypass.slimefun");
        if (!permissions.contains(perm)) {
            pm.addPermission(perm);
        }
        Messages.log(plugin, "&2Slimefun permissions registered ! &7[1]");
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onMultiBlockInteract(MultiBlockInteractEvent e)
    {
        if(!plugin.getSettings().getProtection(Protections.SLIMEFUN)) return;
        if(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
        if(e.getPlayer().hasPermission("creativemanager.bypass.slimefun")) return;
        HashMap<String, String> replaceMap = new HashMap<>();
        replaceMap.put("{PLUGIN}", "SlimeFun");
        Messages.sendMessage(plugin.getMessageManager(), e.getPlayer(), "permission.plugins", replaceMap);
        e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void protectBreakWithSlimefun(BlockBreakEvent e)
    {
        if(!plugin.getSettings().getProtection(Protections.SLIMEFUN)) return;
        if(SlimefunItem.getByItem(e.getPlayer().getItemInUse()) == null) return;
        if(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
        if(e.getPlayer().hasPermission("creativemanager.bypass.slimefun")) return;
        HashMap<String, String> replaceMap = new HashMap<>();
        replaceMap.put("{PLUGIN}", "SlimeFun");
        Messages.sendMessage(plugin.getMessageManager(), e.getPlayer(), "permission.plugins", replaceMap);
        e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void protectSlimefunItemInventory(final InventoryClickEvent e)
    {
        Player p = (Player) e.getWhoClicked();
        if(!plugin.getSettings().getProtection(Protections.SLIMEFUN)) return;
        if(!p.getGameMode().equals(GameMode.CREATIVE)) return;
        if(p.hasPermission("creativemanager.bypass.slimefun")) return;
        if(SlimefunItem.getByItem(e.getCurrentItem()) != null)
        {
            HashMap<String, String> replaceMap = new HashMap<>();
            replaceMap.put("{PLUGIN}", "SlimeFun");
            Messages.sendMessage(plugin.getMessageManager(), (Player)e.getWhoClicked(), "permission.plugins", replaceMap);
            e.setCancelled(true);
            e.setCurrentItem(null);
            p.setItemOnCursor(null);
            p.updateInventory();
            return;
        }
        if(SlimefunItem.getByItem(e.getCursor()) != null)
        {
            HashMap<String, String> replaceMap = new HashMap<>();
            replaceMap.put("{PLUGIN}", "SlimeFun");
            Messages.sendMessage(plugin.getMessageManager(), (Player) e.getWhoClicked(), "permission.plugins", replaceMap);
            e.getWhoClicked().setItemOnCursor(null);
            e.setCancelled(true);
            return;
        }
    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void protectSlimefunItemInteract(final PlayerRightClickEvent e)
    {
        if(!plugin.getSettings().getProtection(Protections.SLIMEFUN)) return;
        if(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
        if(e.getPlayer().hasPermission("creativemanager.bypass.slimefun")) return;
        if(SlimefunItem.getByItem(e.getPlayer().getInventory().getItemInMainHand()) != null)
        {
            HashMap<String, String> replaceMap = new HashMap<>();
            replaceMap.put("{PLUGIN}", "SlimeFun");
            Messages.sendMessage(plugin.getMessageManager(), e.getPlayer(), "permission.plugins", replaceMap);
            e.setUseBlock(Event.Result.DENY);
            e.setUseItem(Event.Result.DENY);
            e.cancel();
        }
    }
}
