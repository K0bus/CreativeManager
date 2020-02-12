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
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.k0bus.creativemanager.Main;

public class InventoryMove implements Listener {

	Main plugin;

	HashMap<UUID, Long> cdtime = new HashMap<UUID, Long>();

	public InventoryMove(Main instance) {
		plugin = instance;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onInventoryClick(InventoryCreativeEvent e) {
		if (!e.equals(null)) {
			try {
				Player p = (Player) e.getWhoClicked();
				if (plugin.getConfig().getList("blacklist.get")
						.contains(e.getCurrentItem().getType().getKey().getKey())) {
					if (!p.hasPermission("creativemanager.bypass.blacklist.get")) {
						if (cdtime.get(p.getUniqueId()) == null
								|| (cdtime.get(p.getUniqueId()) + 1 * 1000) <= System.currentTimeMillis()) {
							if (cdtime.get(p.getUniqueId()) != null) {
								cdtime.remove(p.getUniqueId());
							}
							p.sendMessage(ChatColor.translateAlternateColorCodes('&',
									plugin.getConfig().getString("tag")
											+ plugin.getConfig().getString("lang.blacklist.get").replace("{ITEM}",
													e.getCurrentItem().getType().getKey().getKey())));
							cdtime.put(p.getUniqueId(), (long) (System.currentTimeMillis()));
						}
						e.setCancelled(true);
						e.setCurrentItem(new ItemStack(Material.AIR));
					}
				} else if (plugin.getConfig().getList("blacklist.get")
						.contains(e.getCursor().getType().getKey().getKey())) {
					if (!p.hasPermission("creativemanager.bypass.blacklist.get")) {
						if (cdtime.get(p.getUniqueId()) == null
								|| (cdtime.get(p.getUniqueId()) + 1 * 1000) <= System.currentTimeMillis()) {
							if (cdtime.get(p.getUniqueId()) != null) {
								cdtime.remove(p.getUniqueId());
							}
							p.sendMessage(ChatColor.translateAlternateColorCodes('&',
									plugin.getConfig().getString("tag")
											+ plugin.getConfig().getString("lang.blacklist.get").replace("{ITEM}",
													e.getCursor().getType().getKey().getKey())));
							cdtime.put(p.getUniqueId(), (long) (System.currentTimeMillis()));
						}
						e.setCancelled(true);
						e.setCurrentItem(new ItemStack(Material.AIR));
					}
				}
				if (this.plugin.getConfig().getBoolean("add-lore")) {
					p.sendMessage("EVENT");
					if (!e.getCurrentItem().equals(null)) {
						p.sendMessage("ITEM");
						e.setCurrentItem(this.addLore(e.getCurrentItem(), p));
					}
					if (!e.getCursor().equals(null)) {
						p.sendMessage("CURSOR");
						e.setCursor(this.addLore(e.getCursor(), p));
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private ItemStack addLore(ItemStack item, Player p) {
		ItemMeta meta = item.getItemMeta();
		if(meta == null)
		{
			return item;
		}
		List<?> lore = this.plugin.getConfig().getList("creative-lore");
		List<String> lore_t = new ArrayList<String>();
		
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
		meta.setLore(lore_t);
		item.setItemMeta(meta);
		return item;
	}
}
