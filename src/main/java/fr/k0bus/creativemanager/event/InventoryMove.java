package fr.k0bus.creativemanager.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.k0bus.creativemanager.Main;

public class InventoryMove implements Listener {

	Main plugin;

	HashMap<UUID, Long> cdtime = new HashMap<>();

	public InventoryMove(Main instance) {
		plugin = instance;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClick(InventoryCreativeEvent e) {
		Player player = (Player) e.getWhoClicked();
		ItemStack itemStack = e.getCurrentItem();
		if(itemStack == null)
			itemStack = e.getCursor();
		else
			if(itemStack.getType().equals(Material.AIR))
				itemStack = e.getCursor();
		if(e.getClick().equals(ClickType.DROP) || e.getClick().equals(ClickType.CONTROL_DROP) ||
				e.getClick().equals(ClickType.WINDOW_BORDER_LEFT) || e.getClick().equals(ClickType.WINDOW_BORDER_RIGHT) ||
				e.getClick().equals(ClickType.UNKNOWN))
		{
			if(plugin.getConfig().getBoolean("drop-protection") && !player.hasPermission("creativemanager.drop"))
			{
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("tag") + plugin.getLang().getString("permission.drop")));
				e.setCancelled(true);
			}
			return;
		}
		List<String> blacklist = plugin.getConfig().getStringList("blacklist.get");
		if(blacklist.size() > 0)
			if (blacklist.contains(itemStack.getType().getKey().getKey())) {
				if (!player.hasPermission("creativemanager.bypass.blacklist.get")) {
					if (cdtime.get(player.getUniqueId()) == null || (cdtime.get(player.getUniqueId()) + 1000) <= System.currentTimeMillis()) {
						if (cdtime.get(player.getUniqueId()) != null) {
							cdtime.remove(player.getUniqueId());
						}
						String blget = plugin.getLang().getString("blacklist.get");
						if(blget != null)
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("tag") + blget.replace("{ITEM}", itemStack.getType().getKey().getKey())));
						cdtime.put(player.getUniqueId(), System.currentTimeMillis());
					}
					e.setCancelled(true);
				}
			}
		if(!player.hasPermission("creativemanager.bypass.lore"))
		{
			e.setCurrentItem(addLore(e.getCurrentItem(), player));
			e.setCursor(addLore(e.getCursor(), player));
		}
	}

	private ItemStack addLore(ItemStack item, Player p) {
		if(item == null)
			return null;
		if(p == null)
			return null;
		ItemMeta meta = item.getItemMeta();
		if(meta == null)
		{
			return item;
		}
		List<?> lore = this.plugin.getConfig().getList("creative-lore");
		List<String> lore_t = new ArrayList<>();

		if (lore != null) {
			for (Object obj : lore) {
				if(obj instanceof String)
				{
					String string = (String) obj;
					string = string.replace("{PLAYER}", p.getName())
						.replace("{UUID}", p.getUniqueId().toString())
						.replace("{ITEM}", item.getType().getKey().getKey());
					lore_t.add(ChatColor.translateAlternateColorCodes('&',string));
				}
			}
		}
		meta.setLore(lore_t);
		item.setItemMeta(meta);
		return item;
	}
}
