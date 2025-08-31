package fr.k0bus.creativemanager.utils;

import fr.k0bus.creativemanager.CreativeManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class SpigotUtils {

    public static String getPlayerDisplayname(Player player) {
        return player.getDisplayName();
    }

    public static String getPluginName() {
        return CreativeManager.getInstance().getDescription().getName();
    }

    public static void setItemMetaLore(ItemMeta itemMeta, List<String> lore) {
        itemMeta.setLore(lore);
    }

    public static void setItemMetaDisplayname(ItemMeta itemMeta, String displayName) {
        itemMeta.setDisplayName(displayName);
    }

    public static boolean isPaper() {
        try {
            Class.forName("com.destroystokyo.paper.ParticleBuilder");
            return true;
        } catch (ClassNotFoundException ignored) {
        }
        return false;
    }

    public static Inventory createInventory(int row, String title) {
        return Bukkit.createInventory(null, 9 * row, title);
    }

    public static boolean isBrigadier() {
        try {
            Class.forName("io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents");
            return true;
        } catch (ClassNotFoundException ignored) {
        }
        return false;
    }
}
