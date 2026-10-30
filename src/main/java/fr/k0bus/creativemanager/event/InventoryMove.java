package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.services.ItemBlacklist;
import fr.k0bus.creativemanager.services.ItemLore;
import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.creativemanager.utils.CMUtils;
import java.util.*;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

/** Inventory move listener. */
public class InventoryMove implements Listener {

  /** Instantiates a new Inventory move. */
  CreativeManager plugin;

  public InventoryMove(CreativeManager plugin) {
    this.plugin = plugin;
  }

  @Deprecated
  public InventoryMove(CreativeManager plugin, boolean nbt_enabled) {
    this(plugin);
  }

  @EventHandler(ignoreCancelled = false)
  void onInventoryInteract(InventoryInteractEvent event) {
    if (event.getInventory().getHolder() instanceof Player p) {
      if (!p.getGameMode().equals(GameMode.CREATIVE)) return;
      ItemBlacklist.asyncCheck(p);
      ItemLore.asyncCheck(p);
    }
  }

  /**
   * On inventory click.
   *
   * @param e the event.
   */
  @EventHandler(priority = EventPriority.LOWEST)
  public void onInventoryClick(InventoryCreativeEvent e) {
    Player player = (Player) e.getWhoClicked();
    if (e.getClick().equals(ClickType.DROP)
        || e.getClick().equals(ClickType.CONTROL_DROP)
        || e.getClick().equals(ClickType.WINDOW_BORDER_LEFT)
        || e.getClick().equals(ClickType.WINDOW_BORDER_RIGHT)
        || e.getClick().equals(ClickType.UNKNOWN)) {
      if (CreativeManager.getSettings().getProtection(Protections.DROP)
          && !player.hasPermission("creativemanager.bypass.drop")) {
        if (CreativeManager.getSettings().getConfiguration().getBoolean("send-player-messages"))
          CMUtils.sendMessage(player, "permission.drop");
        e.setCancelled(true);
      }
    }
  }


  @EventHandler(priority = EventPriority.LOWEST)
  public void checkArmorClick(final InventoryCreativeEvent e) {
    Player p = (Player) e.getWhoClicked();
    if (!p.getGameMode().equals(GameMode.CREATIVE)) return;
    if (!CreativeManager.getSettings().getProtection(Protections.ARMOR)) return;
    if (p.hasPermission("creativemanager.bypass.armor")) return;
    if (e.getSlotType().equals(InventoryType.SlotType.ARMOR)) {
      e.setResult(Event.Result.DENY);
      e.setCursor(new ItemStack(Material.AIR));
      e.setCurrentItem(e.getCurrentItem());
      e.setCancelled(true);
    }
  }

  @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
  public void checkEnchantAndPotion(final InventoryClickEvent e) {
    Player p = (Player) e.getWhoClicked();
    if (!CreativeManager.getSettings().getProtection(Protections.ENCHANT_AND_POTION)) return;
    if (!p.getGameMode().equals(GameMode.CREATIVE)) return;
    if (p.hasPermission("creativemanager.bypass.enchants-and-potions")) return;

    List<ItemStack> itemStackList = new ArrayList<>();
    if (e.getCursor() != null) itemStackList.add(e.getCursor());
    if (e.getCurrentItem() != null) itemStackList.add(e.getCurrentItem());
    for (ItemStack item : itemStackList) {
      if (!item.getEnchantments().isEmpty()) {
        for (Map.Entry<Enchantment, Integer> enc : item.getEnchantments().entrySet()) {
          item.removeEnchantment(enc.getKey());
        }
      }
      ItemMeta meta = item.getItemMeta();
      if (meta instanceof PotionMeta potionMeta) {
        for (PotionEffect effect : potionMeta.getCustomEffects()) {
          potionMeta.removeCustomEffect(effect.getType());
        }
        item.setItemMeta(potionMeta);
      }
    }
  }
}
