package fr.k0bus.creativemanager.event;

import de.tr7zw.nbtapi.NBT;
import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.creativemanager.utils.SearchUtils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/** Inventory move NBT listener. */
public class InventoryMoveNBT implements Listener {

  private final CreativeManager plugin;

  public InventoryMoveNBT(CreativeManager plugin) {
    this.plugin = plugin;
  }

  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void checkNBT(final InventoryClickEvent e) {
    Player p = (Player) e.getWhoClicked();
    if (!CreativeManager.getSettings().getProtection(Protections.CUSTOM_NBT)) return;
    if (!p.getGameMode().equals(GameMode.CREATIVE)) return;
    if (p.hasPermission("creativemanager.bypass.custom_nbt")) return;

    List<ItemStack> itemStackList = new ArrayList<>();
    if (e.getCursor() != null) itemStackList.add(e.getCursor());
    if (e.getCurrentItem() != null) itemStackList.add(e.getCurrentItem());
    for (ItemStack item : itemStackList) {
      if (item.getType().equals(Material.AIR)) continue;

      NBT.modify(
          item,
          (tmp) -> {
            for (String k : tmp.getKeys()) {
              if (!SearchUtils.inList(CreativeManager.getSettings().getNBTWhitelist(), k)) {
                tmp.removeKey(k);
              }
            }
          });

      ItemMeta itemMeta = item.getItemMeta();
      if (itemMeta != null) {
        if (!itemMeta.getPersistentDataContainer().isEmpty()) {
          for (NamespacedKey key : itemMeta.getPersistentDataContainer().getKeys()) {
            itemMeta.getPersistentDataContainer().remove(key);
          }
          item.setItemMeta(itemMeta);
        }
      }
    }
  }
}
