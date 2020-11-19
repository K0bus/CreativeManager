package fr.k0bus.creativemanager.gui.settings;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.gui.Gui;
import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.creativemanager.utils.AkuraHeads;
import fr.k0bus.creativemanager.utils.LoreUtils;
import fr.k0bus.creativemanager.utils.MenuUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class ProtectionSettingGui extends Gui {

    CreativeManager plugin;
    HashMap<Integer, Protections> menuMap = new HashMap<>();

    public ProtectionSettingGui(Player player, CreativeManager plugin) {
        super(player, plugin);
        this.plugin = plugin;
        setInv(Bukkit.createInventory(null, 3*9, plugin.getInvTag() + "Protection"));
        initItem();
    }

    @Override
    public void initItem() {
        int i = 0;
        MenuUtils.addSeparator(getInv(), 2);
        for (Protections prot: Protections.values()) {
            ItemStack itemStack = prot.getIconItem(this.plugin.getSettings().getProtection(prot));
            getInv().setItem(i, itemStack);
            menuMap.put(i, prot);
            i++;
        }
        getInv().setItem(getInv().getSize() -1 ,AkuraHeads.CLOSE.getHead());
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if(!e.getInventory().equals(this.getInv())) return;
        e.setCancelled(true);

        if(e.getSlot() == getInv().getSize() -1)
        {
            getPlayer().closeInventory();
        }

        if(!getPlayer().hasPermission("creativemanager.admin")) return;
        if(menuMap.containsKey(e.getSlot()))
        {
            Protections prot = menuMap.get(e.getSlot());
            boolean newValue = !this.plugin.getSettings().getProtection(prot);
            if(this.plugin.getSettings().isLogged())
                this.plugin.getSettings().set("protections." + prot.getName(), newValue);
            this.plugin.getLogger().info(ChatColor.GOLD + e.getWhoClicked().getName() + " change " + prot.getName() + " to " + LoreUtils.getStatusString(newValue));
            initItem();
        }
    }
}
