package fr.k0bus.creativemanager.event.plugin;

import com.Acrobot.ChestShop.Events.PreShopCreationEvent;
import com.Acrobot.ChestShop.Events.PreTransactionEvent;
import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.settings.Protections;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;

public class ChestShop implements Listener {


    @EventHandler
    public void onShopCreation(PreShopCreationEvent e)
    {
        if(!CreativeManager.getSettings().getProtection(Protections.PL_CHESTSHOP)) return;
        if(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
        if(e.getPlayer().hasPermission("creativemanager.bypass.chestshop")) return;
        HashMap<String, String> replaceMap = new HashMap<>();
        replaceMap.put("{PLUGIN}", "ChestShop");
        CreativeManager.sendMessage(e.getPlayer(),CreativeManager.TAG + CreativeManager.getLang().getString("permission.plugins", replaceMap));
        e.setCancelled(true);
    }
    @EventHandler
    public void onShopTransaction(PreTransactionEvent e)
    {
        if(!CreativeManager.getSettings().getProtection(Protections.PL_CHESTSHOP)) return;
        if(!e.getClient().getGameMode().equals(GameMode.CREATIVE)) return;
        if(e.getClient().hasPermission("creativemanager.bypass.chestshop")) return;
        HashMap<String, String> replaceMap = new HashMap<>();
        replaceMap.put("{PLUGIN}", "ChestShop");
        CreativeManager.sendMessage(e.getClient(), CreativeManager.TAG + CreativeManager.getLang().getString("permission.plugins", replaceMap));
        e.setCancelled(true);
    }
}
