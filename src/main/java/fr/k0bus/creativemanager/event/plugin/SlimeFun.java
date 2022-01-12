package fr.k0bus.creativemanager.event.plugin;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.k0buslib.utils.Messages;
import io.github.thebusybiscuit.slimefun4.api.events.MultiBlockInteractEvent;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
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

    @EventHandler
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

    @EventHandler
    public void onToolUsed(BlockBreakEvent e)
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
}
