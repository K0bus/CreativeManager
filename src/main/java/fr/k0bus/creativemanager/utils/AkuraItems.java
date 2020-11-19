package fr.k0bus.creativemanager.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum AkuraItems {
    AMULET(1, Material.GLASS_PANE),
    ARTIFACT(2, Material.GLASS_PANE),
    BACKPACK(3, Material.GLASS_PANE),
    BOOTS(4, Material.GLASS_PANE),
    BUYABLE(5, Material.GLASS_PANE),
    CHESTPLATE(6, Material.GLASS_PANE),
    ELYTRA(7, Material.GLASS_PANE),
    EMPTY(8, Material.GLASS_PANE),
    EXTENDABLE(9, Material.GLASS_PANE),
    FILL(10, Material.GLASS_PANE),
    GLOVES(11, Material.GLASS_PANE),
    HELMET(12, Material.GLASS_PANE),
    INFO(13, Material.GLASS_PANE),
    LEGGINGS(14, Material.GLASS_PANE),
    LOCKED(15, Material.GLASS_PANE),
    PET(16, Material.GLASS_PANE),
    RING(17, Material.GLASS_PANE),
    SHIELD(18, Material.GLASS_PANE),
    WEAPON(19, Material.GLASS_PANE),
    WORKBENCH(20, Material.GLASS_PANE);

    int customModel;
    Material material;
    ItemStack itemStack;

    AkuraItems(int customModel, Material material) {
        this.customModel = customModel;
        this.material = material;
        this.itemStack = new ItemStack(material);
        ItemMeta meta = this.itemStack.getItemMeta();
        if(meta != null)
        {
            meta.setDisplayName("");
            meta.setLore(null);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
            meta.addItemFlags(ItemFlag.HIDE_DYE);
            meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
            meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            meta.setCustomModelData(customModel);
            this.itemStack.setItemMeta(meta);
        }
    }

    public int getCustomModel() {
        return customModel;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public Material getMaterial() {
        return material;
    }
}
