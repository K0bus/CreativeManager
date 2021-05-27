package fr.k0bus.creativemanager.settings;

import fr.k0bus.k0buslib.utils.LoreUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * The Protections.
 */
public enum Protections {
    CONTAINER("container", Material.CHEST, "Protect container usage", "Container"),
    SPAWN("spawn", Material.SKELETON_SPAWN_EGG, "Protect spawn egg usage", "Spawn Egg"),
    DROP("drop", Material.DROPPER, "Protect player drop usage", "Drop"),
    PICKUP("pickup", Material.TRIPWIRE_HOOK, "Protect pickup item on ground", "PickUp"),
    BUILD("build", Material.OAK_PLANKS, "Protect build in creative", "Build"),
    ENTITY("entity", Material.ITEM_FRAME, "Protect entity usage (ItemFrame / ArmorStand ...)", "Entity"),
    PVP("pvp", Material.PLAYER_HEAD, "Disable PVP for Creative", "PVP"),
    PVE("pve", Material.SKELETON_SKULL, "Disable PVE for Creative", "PVE"),
    LORE("lore", Material.NAME_TAG, "Add lore to Creative Items", "Lore"),
    LOOT("loot", Material.NAME_TAG, "Deny looting from Creative placed block", "Lootable"),
    SPAWN_BUILD("spawn-build", Material.WITHER_SKELETON_SKULL, "Deny spawn monster with build", "Spawn Build"),
    THROW("throw", Material.ARROW, "Deny throw projectile", "Throw"),
    COMMANDS("commands", Material.COMMAND_BLOCK, "Deny using blacklisted commands", "Commands");

    private final String name;
    private final Material icon;
    private final String desc;
    private final String displayName;

    /**
     * Instantiates a new Protections.
     *
     * @param name        the name.
     * @param icon        the icon.
     * @param desc        the desc.
     * @param displayName the display name.
     */
    Protections(String name, Material icon, String desc, String displayName) {
        this.name = name;
        this.icon = icon;
        this.desc = desc;
        this.displayName = displayName;
    }

    /**
     * Gets name.
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets icon item.
     *
     * @param value the value.
     * @return the icon item.
     */
    public ItemStack getIconItem(boolean value) {
        ItemStack item = new ItemStack(this.icon);
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null) {
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.DARK_GRAY + "------");
            lore.addAll(LoreUtils.formatLoreString(ChatColor.RESET + this.desc));
            lore.add(ChatColor.DARK_GRAY + "------");
            if (value)
                lore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "Status : Enabled");
            else
                lore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "Status : Disabled");
            itemMeta.setLore(lore);
            itemMeta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + ChatColor.GOLD + this.displayName);
            if(value)
                itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(itemMeta);
        }
        return item;
    }
}
