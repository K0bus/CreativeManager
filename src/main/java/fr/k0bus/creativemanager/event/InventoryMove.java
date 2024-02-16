package fr.k0bus.creativemanager.event;

import de.tr7zw.changeme.nbtapi.NBTItem;
import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.creativemanager.utils.SearchUtils;
import fr.k0bus.k0buscore.utils.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

import java.util.*;

/**
 * Inventory move listener.
 */
public class InventoryMove implements Listener {

	boolean nbt_enabled = true;

	/**
	 * Instantiates a new Inventory move.
	 *
	 */
	public InventoryMove() {}
	public InventoryMove(boolean nbt_enabled) {
		this.nbt_enabled = nbt_enabled;
	}

	/**
	 * On inventory click.
	 *
	 * @param e the event.
	 */
	@EventHandler(priority = EventPriority.LOWEST)
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
			boolean changeLore = !e.getSlotType().equals(InventoryType.SlotType.ARMOR);
			if(!changeLore)
				changeLore = !CreativeManager.getSettings().getProtection(Protections.ARMOR) || player.hasPermission("creativemanager.bypass.armor");
			if(changeLore)
			{
				e.setCurrentItem(addLore(e.getCurrentItem(), player));
				e.setCursor(addLore(e.getCursor(), player));
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void checkNBT(final InventoryClickEvent e)
	{
		if(!nbt_enabled) return;
		Player p = (Player) e.getWhoClicked();
		if (!CreativeManager.getSettings().getProtection(Protections.CUSTOM_NBT)) return;
		if (!p.getGameMode().equals(GameMode.CREATIVE)) return;
		if (p.hasPermission("creativemanager.bypass.custom_nbt")) return;

		List<ItemStack> itemStackList = new ArrayList<>();
		if (e.getCursor() != null) itemStackList.add(e.getCursor());
		if (e.getCurrentItem() != null) itemStackList.add(e.getCurrentItem());
		for (ItemStack item : itemStackList) {
			if(item.getType().equals(Material.AIR)) continue;

			NBTItem tmp = new NBTItem(item);
			ItemMeta itemMeta = item.getItemMeta();

			if(tmp.hasNBTData() || tmp.hasCustomNbtData())
				for (String k:tmp.getKeys()) {
					tmp.removeKey(k);
					tmp.applyNBT(item);
				}

			if (itemMeta != null)
				if (!itemMeta.getPersistentDataContainer().isEmpty()) {
					for (NamespacedKey key : itemMeta.getPersistentDataContainer().getKeys()) {
						itemMeta.getPersistentDataContainer().remove(key);
					}
					item.setItemMeta(itemMeta);
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
	public void checkEnchantAndPotion(final InventoryClickEvent e)
	{
		Player p = (Player) e.getWhoClicked();
		if(!CreativeManager.getSettings().getProtection(Protections.ENCHANT_AND_POTION)) return;
		if(!p.getGameMode().equals(GameMode.CREATIVE)) return;
		if(p.hasPermission("creativemanager.bypass.enchants-and-potions")) return;

		List<ItemStack> itemStackList = new ArrayList<>();
		if(e.getCursor() != null) itemStackList.add(e.getCursor());
		if(e.getCurrentItem() != null) itemStackList.add(e.getCurrentItem());
		for (ItemStack item:itemStackList) {
			if(!item.getEnchantments().isEmpty())
			{
				for (Map.Entry<Enchantment, Integer> enc : item.getEnchantments().entrySet()) {
					item.removeEnchantment(enc.getKey());
				}
			}
			ItemMeta meta = item.getItemMeta();
			if(meta instanceof PotionMeta potionMeta)
			{
				for (PotionEffect effect:potionMeta.getCustomEffects()) {
					potionMeta.removeCustomEffect(effect.getType());
				}
				item.setItemMeta(potionMeta);
			}
		}
	}
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void checkBlackList(final InventoryClickEvent e)
	{
		Player p = (Player) e.getWhoClicked();
		if(!p.getGameMode().equals(GameMode.CREATIVE)) return;
		if(p.hasPermission("creativemanager.bypass.blacklist.get")) return;
		List<String> blacklist = CreativeManager.getSettings().getGetBL();

		List<ItemStack> itemStackList = new ArrayList<>();
		if(e.getCursor() != null) itemStackList.add(e.getCursor());
		if(e.getCurrentItem() != null) itemStackList.add(e.getCurrentItem());
		for (ItemStack item:itemStackList) {
			String itemName = item.getType().name().toLowerCase();
			if(p.hasPermission("creativemanager.bypass.blacklist.get." + itemName)) return;
			if((CreativeManager.getSettings().getString("list.mode.get").equals("whitelist") && !SearchUtils.inList(blacklist, item)) ||
					(!CreativeManager.getSettings().getString("list.mode.get").equals("whitelist") && SearchUtils.inList(blacklist, item))){
				HashMap<String, String> replaceMap = new HashMap<>();
				replaceMap.put("{ITEM}", StringUtils.proper(item.getType().name()));
				CreativeManager.sendMessage(p, CreativeManager.TAG + CreativeManager.getLang().getString("blacklist.get", replaceMap));
				e.setCancelled(true);
				return;
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
				if (obj instanceof String string) {
					string = string.replace("{PLAYER}", p.getName())
							.replace("{UUID}", p.getUniqueId().toString())
							.replace("{ITEM}", StringUtils.proper(item.getType().name()));
					lore_t.add(ChatColor.translateAlternateColorCodes('&',string));
				}
			}
		}
		meta.setLore(lore_t);
		item.setItemMeta(meta);
		return item;
	}
}
