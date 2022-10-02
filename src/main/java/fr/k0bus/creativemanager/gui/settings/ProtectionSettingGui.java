package fr.k0bus.creativemanager.gui.settings;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.k0buscore.menu.MenuItems;
import fr.k0bus.k0buscore.menu.PagedMenu;
import fr.k0bus.k0buscore.utils.Serializer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Protection setting class.
 */
public class ProtectionSettingGui extends PagedMenu {

    /**
     * Instantiates a new Protection setting gui.
     *
     * @param plugin the plugin.
     */
    public ProtectionSettingGui(CreativeManager plugin) {
        super(3, plugin.getInvTag() + "Protection");
        setSlots(Serializer.readIntArray(Collections.singletonList("0-17")));
        init();
    }

    /**
     * Init item.
     */
    @Override
    public void init() {
        clearContent();
        for (Protections protection : Protections.values()) {
            ItemStack itemStack = protection.getIconItem(CreativeManager.getSettings().getProtection(protection));
            MenuItems items = new MenuItems(itemStack, (e) -> {
                boolean value = CreativeManager.getSettings().getProtection(protection);
                CreativeManager.getSettings().setProtection(protection, !value);
                init();
            });
            add(items);
        }
        if(hasPreviousPage())
        {
            MenuItems pagePrevious = new MenuItems(Material.ARROW, (e)-> {
                previous();
                init();
            });
            pagePrevious.setDisplayname(ChatColor.GOLD + "Previous page");
            setItem(20, pagePrevious);
        }
        if(hasNextPage())
        {
            MenuItems pageNext = new MenuItems(Material.ARROW, (e)-> {
                next();
                init();
            });
            pageNext.setDisplayname(ChatColor.GOLD + "Next page");
            setItem(24, pageNext);
        }

        MenuItems closeItem = new MenuItems(Material.BARRIER, (e) -> {
            for (HumanEntity entity:new ArrayList<>(this.getInventory().getViewers())) {
                entity.closeInventory();
            }
        });
        closeItem.setDisplayname(ChatColor.RED + "Close menu");
        setItem(getInventory().getSize() -1, closeItem);
        drawContent();
    }
}
