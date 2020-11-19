package fr.k0bus.creativemanager.utils;

import dev.dbassett.skullcreator.SkullCreator;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum AkuraHeads {
    BACK("Back", ChatColor.RED, "http://textures.minecraft.net/texture/c49d271c5df84f8a3c8aa5d15427f62839341dab52c619a5987d38fbe18e464"),
    CLOSE("Close", ChatColor.RED, "http://textures.minecraft.net/texture/beb588b21a6f98ad1ff4e085c552dcb050efc9cab427f46048f18fc803475f7"),
    LOADING("Loading...", ChatColor.BLUE, "http://textures.minecraft.net/texture/6ba0c4a0fdce923a9048328d664147c5b924491f4ee5fea71f3e9ec314"),
    NEXT("Next", ChatColor.WHITE, "http://textures.minecraft.net/texture/be9ae7a4be65fcbaee65181389a2f7d47e2e326db59ea3eb789a92c85ea46"),
    PREVIOUS("Previous", ChatColor.WHITE, "http://textures.minecraft.net/texture/3f46abad924b22372bc966a6d517d2f1b8b57fdd262b4e04f48352e683fff92");

    String name;
    ChatColor color;
    String url;
    ItemStack head;

    AkuraHeads(String name, ChatColor color, String url) {
        this.name = name;
        this.color = color;
        this.url = url;
        ItemStack head = SkullCreator.itemFromUrl(this.url);
        ItemMeta headMeta = head.getItemMeta();
        headMeta.setDisplayName(this.color + this.name);
        head.setItemMeta(headMeta);
        this.head = head;
    }

    public ItemStack getHead() {
        return head;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
