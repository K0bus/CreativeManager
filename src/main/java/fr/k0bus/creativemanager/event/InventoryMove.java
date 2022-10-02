package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.settings.Protections;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

import java.util.*;

/**
 * Inventory move listener.
 */
public class InventoryMove implements Listener {

	/**
	 * Instantiates a new Inventory move.
	 *
	 */
	public InventoryMove() {}

	/**
	 * On inventory click.
	 *
	 * @param e the event.
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClick(InventoryCreativeEvent e) {
		Player player = (Player) e.getWhoClicked();
		if (e.getClick().equals(ClickType.DROP) || e.getClick().equals(ClickType.CONTROL_DROP) ||
				e.getClick().equals(ClickType.WINDOW_BORDER_LEFT) || e.getClick().equals(ClickType.WINDOW_BORDER_RIGHT) ||
				e.getClick().equals(ClickType.UNKNOWN)) {
			if (CreativeManager.getSettings().getProtection(Protections.DROP) && !player.hasPermission("creativemanager.bypass.drop")) {
				if (CreativeManager.getSettings().getBoolean("send-player-messages"))
					CreativeManager.sendMessage(player, CreativeManager.TAG + CreativeManager.getLang().getString("permission.drop"));
				e.setCancelled(true);
			}
			return;
		}
		if (!player.hasPermission("creativemanager.bypass.lore") && CreativeManager.getSettings().getProtection(Protections.LORE)) {
			e.setCurrentItem(addLore(e.getCurrentItem(), player));
			e.setCursor(addLore(e.getCursor(), player));
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void checkEnchantAndPotion(final InventoryClickEvent e)
	{
		Player p = (Player) e.getWhoClicked();
		if(!CreativeManager.getSettings().getProtection(Protections.ENCHANT_AND_POTION)) return;
		if(!p.getGameMode().equals(GameMode.CREATIVE)) return;
		if(p.hasPermission("creativemanager.bypass.enchants-and-potions")) return;
		if(e.getCursor() != null){
			if(!e.getCursor().getEnchantments().isEmpty())
			{
				for (Map.Entry<Enchantment, Integer> enc : e.getCursor().getEnchantments().entrySet()) {
					e.getCursor().removeEnchantment(enc.getKey());
				}
			}
			ItemMeta meta = e.getCursor().getItemMeta();
			if(meta instanceof PotionMeta)
			{
				PotionMeta potionMeta = (PotionMeta) meta;
				for (PotionEffect effect:potionMeta.getCustomEffects()) {
					potionMeta.removeCustomEffect(effect.getType());
				}
				e.getCursor().setItemMeta(potionMeta);
			}
		}
		if(e.getCurrentItem() != null){
			if(!e.getCurrentItem().getEnchantments().isEmpty())
			{
				for (Map.Entry<Enchantment, Integer> enc : e.getCurrentItem().getEnchantments().entrySet()) {
					e.getCurrentItem().removeEnchantment(enc.getKey());
				}
			}
			ItemMeta meta = e.getCurrentItem().getItemMeta();
			if(meta instanceof PotionMeta)
			{
				PotionMeta potionMeta = (PotionMeta) meta;
				for (PotionEffect effect:potionMeta.getCustomEffects()) {
					potionMeta.removeCustomEffect(effect.getType());
				}
				e.getCurrentItem().setItemMeta(potionMeta);
			}
		}
	}
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void checkBlackList(final InventoryClickEvent e)
	{
		Player p = (Player) e.getWhoClicked();
		if(!p.getGameMode().equals(GameMode.CREATIVE)) return;
		if(p.hasPermission("creativemanager.bypass.blacklist.get")) return;
		List<String> blacklist = CreativeManager.getSettings().getGetBL();
		if(e.getCursor() != null){
			String itemName = e.getCursor().getType().name().toLowerCase();
			if(p.hasPermission("creativemanager.bypass.blacklist.get" + itemName)) return;
			if (blacklist.size() > 0)
				if (blacklist.stream().anyMatch(e.getCursor().getType().name()::equalsIgnoreCase)) {
					HashMap<String, String> replaceMap = new HashMap<>();
					replaceMap.put("{ITEM}", e.getCursor().getType().name());
					CreativeManager.sendMessage(p, CreativeManager.TAG + CreativeManager.getLang().getString("blacklist.get", replaceMap));
					e.setCancelled(true);
					p.updateInventory();
					return;
				}
		}
		if(e.getCurrentItem() != null) {
			String itemName = e.getCurrentItem().getType().name().toLowerCase();
			if(p.hasPermission("creativemanager.bypass.blacklist.get" + itemName)) return;
			if (blacklist.size() > 0)
				if (blacklist.stream().anyMatch(e.getCurrentItem().getType().name()::equalsIgnoreCase)) {
					HashMap<String, String> replaceMap = new HashMap<>();
					replaceMap.put("{ITEM}", e.getCursor().getType().name());
					CreativeManager.sendMessage(p, CreativeManager.TAG + CreativeManager.getLang().getString("blacklist.get", replaceMap));
					e.setCancelled(true);
					p.updateInventory();
				}
		}
	}

	/**
	 * Add lore item stack.
	 *
	 * @param item the item.
	 * @param p    the player.
	 * @return the item stack.
	 */
	private ItemStack addLore(ItemStack item, Player p) {
		if (item == null)
			return null;
		if (p == null)
			return null;
		ItemMeta meta = item.getItemMeta();
		if (meta == null) {
			return item;
		}
		List<?> lore = CreativeManager.getSettings().getLore();
		List<String> lore_t = new ArrayList<>();

		if (lore != null) {
			for (Object obj : lore) {
				if (obj instanceof String) {
					String string = (String) obj;
					string = string.replace("{PLAYER}", p.getName())
							.replace("{UUID}", p.getUniqueId().toString())
							.replace("{ITEM}", item.getType().name());
					lore_t.add(ChatColor.translateAlternateColorCodes('&',string));
				}
			}
		}
		meta.setLore(lore_t);
		item.setItemMeta(meta);
		return item;
	}
}
