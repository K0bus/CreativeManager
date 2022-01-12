package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.settings.Protections;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class CreativeCopy implements Listener {

    CreativeManager plugin;

    public CreativeCopy(CreativeManager plugin)
    {
        this.plugin = plugin;
    }

    public void onBlockCopy(final InventoryClickEvent e)
    {
        if(!e.getClick().equals(ClickType.CREATIVE)) return;
        if(!plugin.getSettings().getProtection(Protections.COPY)) return;
        if(e.getCursor() == null) return;
        Player p = (Player) e.getWhoClicked();
        if(!p.getGameMode().equals(GameMode.CREATIVE)) return;
        if(p.hasPermission("creativemanager.bypass.blockcopy")) return;
        p.setItemOnCursor(new ItemStack(e.getCursor().getType()));
    }
}
