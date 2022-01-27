package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.k0buslib.utils.Messages;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
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

	private final CreativeManager plugin;
	private final HashMap<UUID, Long> cdtime = new HashMap<>();

	/**
	 * Instantiates a new Inventory move.
	 *
	 * @param instance the instance.
	 */
	public InventoryMove(CreativeManager instance) {
		plugin = instance;
	}

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
			if (plugin.getSettings().getProtection(Protections.DROP) && !player.hasPermission("creativemanager.bypass.drop")) {
				if (plugin.getSettings().getBoolean("send-player-messages"))
					Messages.sendMessage(plugin.getMessageManager(), player, "permission.drop");
				e.setCancelled(true);
			}
			return;
		}
		if (!player.hasPermission("creativemanager.bypass.lore") && plugin.getSettings().getProtection(Protections.LORE)) {
			e.setCurrentItem(addLore(e.getCurrentItem(), player));
			e.setCursor(addLore(e.getCursor(), player));
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void checkEnchantAndPotion(final InventoryClickEvent e)
	{
		Player p = (Player) e.getWhoClicked();
		if(!plugin.getSettings().getProtection(Protections.ENCHANTANDPOTION)) return;
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
		List<String> blacklist = plugin.getSettings().getGetBL();
		if(e.getCursor() != null){
			if (blacklist.size() > 0)
				if (blacklist.stream().anyMatch(e.getCursor().getType().name()::equalsIgnoreCase)) {
					HashMap<String, String> replaceMap = new HashMap<>();
					replaceMap.put("{ITEM}", e.getCursor().getType().name());
					Messages.sendMessage(plugin.getMessageManager(), p, "blacklist.get", replaceMap);
					e.setCancelled(true);
					p.updateInventory();
					return;
				}
		}
		if(e.getCurrentItem() != null) {
			if (blacklist.size() > 0)
				if (blacklist.stream().anyMatch(e.getCurrentItem().getType().name()::equalsIgnoreCase)) {
					HashMap<String, String> replaceMap = new HashMap<>();
					replaceMap.put("{ITEM}", e.getCursor().getType().name());
					Messages.sendMessage(plugin.getMessageManager(), p, "blacklist.get", replaceMap);
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
		List<?> lore = this.plugin.getSettings().getLore();
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
