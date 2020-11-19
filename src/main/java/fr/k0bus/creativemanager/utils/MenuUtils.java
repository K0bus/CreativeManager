package fr.k0bus.creativemanager.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuUtils {
    public static ItemStack createItems(Material material, String name, String description)
    {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        itemMeta.setLore(LoreUtils.formatLoreString(description));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    public static ItemStack createItems(Material material, String name)
    {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    public static ItemStack createItems(Material material)
    {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("");
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    public static void addSeparator(Inventory inventory, int row) {
        for(int i = 0; i<9; i++)
        {
            inventory.setItem((row-1)*9 + i, AkuraItems.FILL.getItemStack());
        }
    }
    public static void fillInventory(Inventory inventory)
    {
        for(int i = 0 ; i < inventory.getSize(); i++)
        {
            if(inventory.getItem(i) == null)
            {
                inventory.setItem(i, AkuraItems.FILL.getItemStack());
            }
        }
    }
}
