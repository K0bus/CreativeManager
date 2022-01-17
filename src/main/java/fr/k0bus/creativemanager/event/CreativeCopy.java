package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.settings.Protections;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class CreativeCopy implements Listener {

    final CreativeManager plugin;

    public CreativeCopy(CreativeManager plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockCopy(final InventoryClickEvent e)
    {
        if(!e.getAction().equals(InventoryAction.CLONE_STACK)) return;
        if(!plugin.getSettings().getProtection(Protections.COPY)) return;
        Player p = (Player) e.getWhoClicked();
        if(!p.getGameMode().equals(GameMode.CREATIVE)) return;
        if(p.hasPermission("creativemanager.bypass.blockcopy")) return;
        e.setCurrentItem(new ItemStack(e.getCurrentItem().getType()));
    }
}
