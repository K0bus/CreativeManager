package fr.k0bus.creativemanager.services;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.utils.CMUtils;
import fr.k0bus.creativemanager.utils.SearchUtils;
import fr.k0bus.k0buscore.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;

public class ItemBlacklist {
    public static void asyncCheck(Player player) {
        if(player.hasPermission("creativemanager.bypass.blacklist.get")) return;
        new BukkitRunnable() {
            @Override
            public void run() {
                for (ItemStack content : player.getInventory().getContents()) {
                    checkBlacklist(
                            content,
                            player,
                            CreativeManager.getSettings().getGetBL()
                    );
                }
            }
        }.runTaskLaterAsynchronously(CreativeManager.getInstance(), 2L);
    }

    public static void checkBlacklist(ItemStack itemStack, Player player, List<String> blacklist) {
        if (isBlackListed(itemStack, player, blacklist)) {
            itemStack.setAmount(0);
        }
    }

    private static boolean isBlackListed(ItemStack item, Player player, List<String> blacklist) {
        if (item == null) {
            return false;
        }
        String itemName = item.getType().name().toLowerCase();
        if(player.hasPermission("creativemanager.bypass.blacklist.get." + itemName)) return false;
        if(item.getType().equals(Material.AIR)) return false;
        if((CreativeManager.getSettings().getConfiguration().getString("list.mode.get").equals("whitelist") && !SearchUtils.inList(blacklist, item)) ||
                (!CreativeManager.getSettings().getConfiguration().getString("list.mode.get").equals("whitelist") && SearchUtils.inList(blacklist, item))){
            HashMap<String, String> replaceMap = new HashMap<>();
            replaceMap.put("{ITEM}", StringUtils.proper(item.getType().name()));
            CMUtils.sendMessage(player, "blacklist.get", replaceMap);
            return true;
        }
        return false;
    }
}
