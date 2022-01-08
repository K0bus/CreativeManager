package fr.k0bus.creativemanager.gui.settings;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.gui.Gui;
import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.k0buslib.utils.AkuraHeads;
import fr.k0bus.k0buslib.utils.AkuraItems;
import fr.k0bus.k0buslib.utils.MenuUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * Protection setting class.
 */
public class ProtectionSettingGui extends Gui {

    private final CreativeManager plugin;
    private final HashMap<Integer, Protections> menuMap = new HashMap<>();

    /**
     * Instantiates a new Protection setting gui.
     *
     * @param player the player.
     * @param plugin the plugin.
     */
    public ProtectionSettingGui(Player player, CreativeManager plugin) {
        super(player, plugin);
        this.plugin = plugin;
        setInv(Bukkit.createInventory(null, 3 * 9, plugin.getInvTag() + "Protection"));
        initItem();
    }

    /**
     * Init item.
     */
    @Override
    public void initItem() {
        int i = 0;
        addSeparator(2);
        addSeparator(3);
        for (Protections prot : Protections.values()) {
            ItemStack itemStack = prot.getIconItem(this.plugin.getSettings().getProtection(prot));
            getInv().setItem(i, itemStack);
            menuMap.put(i, prot);
            i++;
        }
        getInv().setItem(getInv().getSize() - 1, AkuraHeads.CLOSE.getHead());
    }

    /**
     * On inventory click.
     *
     * @param e the event.
     */
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!e.getInventory().equals(this.getInv())) return;
        e.setCancelled(true);

        if (e.getSlot() == getInv().getSize() - 1) {
            getPlayer().closeInventory();
        }

        if (!getPlayer().hasPermission("creativemanager.admin")) return;
        if (menuMap.containsKey(e.getSlot())) {
            Protections prot = menuMap.get(e.getSlot());
            boolean newValue = !this.plugin.getSettings().getProtection(prot);
            if (this.plugin.getSettings().isLogged())
                this.plugin.getSettings().set("protections." + prot.getName(), newValue);
            String status = "Enabled";
            if (!newValue)
                status = "Disabled";
            this.plugin.getLogger().info(
                    ChatColor.GOLD + e.getWhoClicked().getName() + " change " + prot.getName() + " to " + status);
            initItem();
        }
    }
    private void addSeparator(int row) {
        try {
            for (int i = 0; i < 9; i++) {
                getInv().setItem((row - 1) * 9 + i, new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE));
            }
        }
        catch (Error ignored){}
    }
}
