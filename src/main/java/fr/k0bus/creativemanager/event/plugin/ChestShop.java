package fr.k0bus.creativemanager.event.plugin;

import com.Acrobot.ChestShop.Events.PreShopCreationEvent;
import com.Acrobot.ChestShop.Events.PreTransactionEvent;
import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.k0buslib.utils.Messages;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;

import java.util.HashMap;
import java.util.Set;

public class ChestShop implements Listener {

    final CreativeManager plugin;

    public ChestShop(CreativeManager plugin)
    {
        this.plugin = plugin;
        PluginManager pm = plugin.getServer().getPluginManager();
        Set<Permission> permissions = pm.getPermissions();
        Permission perm = new Permission("creativemanager.bypass.chestshop");
        if (!permissions.contains(perm)) {
            pm.addPermission(perm);
        }
        Messages.log(plugin, "&2ChestShop permissions registered ! &7[1]");
    }

    @EventHandler
    public void onShopCreation(PreShopCreationEvent e)
    {
        if(!plugin.getSettings().getProtection(Protections.CHESTSHOP)) return;
        if(e.getPlayer().hasPermission("creativemanager.bypass.chestshop")) return;
        HashMap<String, String> replaceMap = new HashMap<>();
        replaceMap.put("{PLUGIN}", "ChestShop");
        Messages.sendMessage(plugin.getMessageManager(), e.getPlayer(), "permission.plugins", replaceMap);
        e.setCancelled(true);
    }
    @EventHandler
    public void onShopTransaction(PreTransactionEvent e)
    {
        if(!plugin.getSettings().getProtection(Protections.CHESTSHOP)) return;
        if(e.getClient().hasPermission("creativemanager.bypass.chestshop")) return;
        HashMap<String, String> replaceMap = new HashMap<>();
        replaceMap.put("{PLUGIN}", "ChestShop");
        Messages.sendMessage(plugin.getMessageManager(), e.getClient(), "permission.plugins", replaceMap);
        e.setCancelled(true);
    }
}
