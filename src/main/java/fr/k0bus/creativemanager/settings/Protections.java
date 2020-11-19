package fr.k0bus.creativemanager.settings;

import fr.k0bus.creativemanager.utils.LoreUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public enum Protections {
    CONTAINER("container", Material.CHEST, "Protect container usage"),
    SPAWN("spawn", Material.SKELETON_SPAWN_EGG, "Protect spawn egg usage"),
    DROP("drop", Material.DROPPER, "Protect player drop usage"),
    PICKUP("pickup", Material.TRIPWIRE_HOOK, "Protect pickup item on ground"),
    BUILD("build", Material.OAK_PLANKS, "Protect build in creative"),
    ENTITY("entity", Material.ITEM_FRAME, "Protect entity usage (ItemFrame / ArmorStand ...)"),
    PVP("pvp", Material.PLAYER_HEAD, "Disable PVP for Creative"),
    PVE("pve", Material.SKELETON_SKULL, "Disable PVE for Creative"),
    LORE("lore", Material.NAME_TAG, "Add lore to Creative Items");

    private final String name;
    private final Material icon;
    private final String desc;

    Protections(String name, Material icon, String desc) {
        this.name = name;
        this.icon = icon;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public Material getIcon() {
        return icon;
    }
    public ItemStack getIconItem(boolean value) {
        ItemStack item = new ItemStack(this.icon);
        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta != null)
        {
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.DARK_GRAY + "------");
            lore.addAll(LoreUtils.formatLoreString(this.desc));
            lore.add(ChatColor.DARK_GRAY + "------");
            lore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "Status : " + LoreUtils.getStatusString(value));
            itemMeta.setLore(lore);
            itemMeta.setDisplayName(this.name.substring(0, 1).toUpperCase() + this.name.substring(1));
            if(value)
                itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(itemMeta);
        }
        return item;
    }
}
