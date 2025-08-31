package fr.k0bus.creativemanager.services;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.utils.CMUtils;
import fr.k0bus.creativemanager.utils.SpigotUtils;
import fr.k0bus.creativemanager.utils.TextUtils;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemLore {
    public static void asyncCheck(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (ItemStack content : player.getInventory().getContents()) {
                    addLore(
                            content,
                            player
                    );
                }
            }
        }.runTaskLaterAsynchronously(CreativeManager.getInstance(), 2L);
    }

    private static void addLore(ItemStack itemStack, HumanEntity p) {
        if (itemStack == null || p == null) return;
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return;

        List<String> lore = CreativeManager.getSettings().getLore();
        List<String> tempLore = new ArrayList<>();
        if (!lore.isEmpty()) {
            for (String line : lore) {
                tempLore.add(getFinalString(line, (Player) p, itemStack));
            }
            SpigotUtils.setItemMetaLore(meta, tempLore);
        }
        itemStack.setItemMeta(meta);
    }

    private static String getFinalString(String string, Player player, ItemStack itemStack) {
        return CMUtils.parse(TextUtils.replacePlaceholders(
                string,
                Map.of(
                        "PLAYER", player.getName(),
                        "UUID", player.getUniqueId().toString(),
                        "ITEM", itemStack.getType().name())));
    }
}
