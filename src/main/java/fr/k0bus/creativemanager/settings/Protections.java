package fr.k0bus.creativemanager.settings;

import fr.k0bus.k0buscore.utils.StringUtils;
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
    SPAWN("spawn", Material.BONE, "Protect spawn egg usage", "Spawn Egg"),
    DROP("drop", Material.DROPPER, "Protect player drop usage", "Drop"),
    PICKUP("pickup", Material.TRIPWIRE_HOOK, "Protect pickup item on ground", "PickUp"),
    BUILD("build", Material.BRICK, "Protect build in creative", "Build"),
    ENTITY("entity", Material.ITEM_FRAME, "Protect entity usage (ItemFrame / ArmorStand ...)", "Entity"),
    PVP("pvp", Material.DIAMOND_SWORD, "Disable PVP for Creative", "PVP"),
    PVE("pve", Material.BONE, "Disable PVE for Creative", "PVE"),
    LORE("lore", Material.NAME_TAG, "Add lore to Creative Items", "Lore"),
    LOOT("loot", Material.NAME_TAG, "Deny looting from Creative placed block", "Lootable"),
    SPAWN_BUILD("spawn-build", Material.PUMPKIN, "Deny spawn monster with build", "Spawn Build"),
    THROW("throw", Material.ARROW, "Deny throw projectile", "Throw"),
    COMMANDS("commands", Material.PAPER, "Deny using blacklisted commands", "Commands"),
    BLOCK_USE("blockuse", Material.FURNACE, "Deny using blacklisted blocks", "Use block"),
    PL_SLIMEFUN("plugins.slimefun", Material.SLIME_BALL, "Deny using SlimeFun", "Plugins - SlimeFun"),
    PL_CHESTSHOP("plugins.chestshop", Material.GOLD_NUGGET, "Deny using ChestShop", "Plugins - ChestShop"),
    ENCHANT_AND_POTION("enchant-and-potion", Material.SHEARS, "Deny player to get Enchanted items / Potions with effects", "Enchant / Potion"),
    GUI("gui", Material.ENDER_CHEST, "Deny player to open any GUI (Including vanilla container)", "GUI"),
    PL_CITIZENS("plugins.citizens", Material.NAME_TAG, "Deny player to interact with Citizens", "Plugins - Citizens"),
    CUSTOM_NBT("custom-nbt", Material.NAME_TAG, "Deny player to get items with custom NBT", "Custom NBT"),
    ARMOR("armor", Material.CHAINMAIL_HELMET, "Set a defined player armor set", "Creative Armor"),
    PL_ITEMSADDER("itemsadder", Material.REDSTONE_BLOCK, "Deny player to use itemsadder features", "ItemsAdder"),
    BUILD_CONTAINER("build-container", Material.CHEST, "Deny player to place block with items in inventory", "Place container"),
    REMOVE_EFFECTS("remove-effects", Material.POTION, "Remove all potion effect when player switch gamemode", "Remove effects");

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
            lore.add(StringUtils.translateColor(ChatColor.RESET + this.desc));
            lore.add(ChatColor.DARK_GRAY + "------");
            if (value)
                lore.add(ChatColor.GOLD + String.valueOf(ChatColor.BOLD) + "Status : " + ChatColor.GREEN + "Enabled");
            else
                lore.add(ChatColor.GOLD + String.valueOf(ChatColor.BOLD) + "Status : " + ChatColor.RED + "Disabled");
            itemMeta.setLore(lore);
            itemMeta.setDisplayName(ChatColor.RESET + String.valueOf(ChatColor.BOLD) + ChatColor.GOLD + this.displayName);
            if(value)
                itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(itemMeta);
        }
        return item;
    }
}
