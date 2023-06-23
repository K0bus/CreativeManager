package fr.k0bus.creativemanager.event.plugin;

import de.tr7zw.changeme.nbtapi.NBTItem;
import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.settings.Protections;
import io.github.thebusybiscuit.slimefun4.api.events.MultiBlockInteractEvent;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;

public class SlimeFun implements Listener {

    final CreativeManager plugin;

    public SlimeFun(CreativeManager plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onMultiBlockInteract(MultiBlockInteractEvent e)
    {
        if(!CreativeManager.getSettings().getProtection(Protections.PL_SLIMEFUN)) return;
        if(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
        if(e.getPlayer().hasPermission("creativemanager.bypass.slimefun")) return;
        HashMap<String, String> replaceMap = new HashMap<>();
        replaceMap.put("{PLUGIN}", "SlimeFun");
        CreativeManager.sendMessage(e.getPlayer(), CreativeManager.TAG + CreativeManager.getLang().getString("permission.plugin", replaceMap));
        e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void protectBreakWithSlimefun(BlockBreakEvent e)
    {
        if(!CreativeManager.getSettings().getProtection(Protections.PL_SLIMEFUN)) return;
        if(SlimefunItem.getByItem(e.getPlayer().getItemInUse()) == null) return;
        if(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
        if(e.getPlayer().hasPermission("creativemanager.bypass.slimefun")) return;
        HashMap<String, String> replaceMap = new HashMap<>();
        replaceMap.put("{PLUGIN}", "SlimeFun");
        CreativeManager.sendMessage(e.getPlayer(), CreativeManager.TAG + CreativeManager.getLang().getString("permission.plugin", replaceMap));
        e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void protectSlimefunItemInventory(final InventoryClickEvent e)
    {
        Player p = (Player) e.getWhoClicked();
        if(!CreativeManager.getSettings().getProtection(Protections.PL_SLIMEFUN)) return;
        if(!p.getGameMode().equals(GameMode.CREATIVE)) return;
        if(p.hasPermission("creativemanager.bypass.slimefun")) return;
        if(SlimefunItem.getByItem(e.getCurrentItem()) != null)
        {
            HashMap<String, String> replaceMap = new HashMap<>();
            replaceMap.put("{PLUGIN}", "SlimeFun");
            CreativeManager.sendMessage(e.getWhoClicked(), CreativeManager.TAG + CreativeManager.getLang().getString("permission.plugin", replaceMap));
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
            CreativeManager.sendMessage(e.getWhoClicked(), CreativeManager.TAG + CreativeManager.getLang().getString("permission.plugin", replaceMap));
            e.getWhoClicked().setItemOnCursor(null);
            e.setCancelled(true);
        }
    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void protectSlimefunItemInteract(final PlayerRightClickEvent e)
    {
        if(!CreativeManager.getSettings().getProtection(Protections.PL_SLIMEFUN)) return;
        if(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
        if(e.getPlayer().hasPermission("creativemanager.bypass.slimefun")) return;
        if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)) return;
        NBTItem tmp = new NBTItem(e.getPlayer().getInventory().getItemInMainHand());
        if (tmp.hasNBTData() && tmp.hasKey("PublicBukkitValues")
                && tmp.getCompound("PublicBukkitValues").hasKey("slimefun:slimefun_item"))
        {
            HashMap<String, String> replaceMap = new HashMap<>();
            replaceMap.put("{PLUGIN}", "SlimeFun");
            CreativeManager.sendMessage(e.getPlayer(), CreativeManager.TAG + CreativeManager.getLang().getString("permission.plugin", replaceMap));
            e.setUseBlock(Event.Result.DENY);
            e.setUseItem(Event.Result.DENY);
            e.cancel();
        }
    }
}
