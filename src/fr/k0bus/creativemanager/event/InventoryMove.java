package fr.k0bus.creativemanager.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
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

	HashMap<UUID, Long> cdtime = new HashMap<UUID, Long>();

	public InventoryMove(Main instance) {
		plugin = instance;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onInventoryClick(InventoryCreativeEvent e) {
		if (e != null) {
			try {

				if(e.getWhoClicked() instanceof Player)
				{
					Player p = (Player) e.getWhoClicked();
					if(e.getClick().equals(ClickType.DROP) || e.getClick().equals(ClickType.CONTROL_DROP) ||
					e.getClick().equals(ClickType.WINDOW_BORDER_LEFT) || e.getClick().equals(ClickType.WINDOW_BORDER_RIGHT) ||
					e.getClick().equals(ClickType.UNKNOWN))
					{
						if(plugin.getConfig().getBoolean("drop-protection") && !p.hasPermission("creativemanager.drop"))
						{
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("tag") + plugin.getLang().getString("permission.drop")));
							e.setCancelled(true);
						}
						return;
					}
					Bukkit.getLogger().info("Action : " + e.getClick());
					Bukkit.getLogger().info(e.getClick().name());
					ItemStack itemStack = null;
					if (e.getCurrentItem() != null) {
						itemStack = e.getCurrentItem();
					}
					if (e.getCursor() != null) {
						itemStack = e.getCursor();
					}
					if (plugin.getConfig().getList("blacklist.get")
							.contains(itemStack.getType().getKey().getKey())) {
						if (!p.hasPermission("creativemanager.bypass.blacklist.get")) {
							if (cdtime.get(p.getUniqueId()) == null
									|| (cdtime.get(p.getUniqueId()) + 1 * 1000) <= System.currentTimeMillis()) {
								if (cdtime.get(p.getUniqueId()) != null) {
									cdtime.remove(p.getUniqueId());
								}
								p.sendMessage(ChatColor.translateAlternateColorCodes('&',
										plugin.getConfig().getString("tag")
												+ plugin.getLang().getString("blacklist.get").replace("{ITEM}",
												itemStack.getType().getKey().getKey())));
								cdtime.put(p.getUniqueId(), (long) (System.currentTimeMillis()));
							}
							e.setCancelled(true);
							e.setCurrentItem(new ItemStack(Material.AIR));
						}
					}
					if (this.plugin.getConfig().getBoolean("add-lore") && !p.hasPermission("creativemanager.bypass.lore")) {
						if (e.getCurrentItem() != null) {
							e.setCurrentItem(this.addLore(itemStack, p));
						}
						e.getCursor();
						e.setCursor(this.addLore(itemStack, p));
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
		List<String> lore_t = new ArrayList<>();

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
