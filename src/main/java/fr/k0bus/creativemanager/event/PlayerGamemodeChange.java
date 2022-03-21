package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.manager.InventoryManager;
import fr.k0bus.k0buslib.utils.Messages;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

import java.util.HashMap;

/**
 * Player gamemode change event.
 */
public class PlayerGamemodeChange implements Listener {
	private final CreativeManager plugin;

	/**
	 * Instantiates a new Player gamemode change.
	 *
	 * @param instance the instance.
	 */
	public PlayerGamemodeChange(CreativeManager instance) {
		plugin = instance;
	}

	/**
	 * On gm change.
	 *
	 * @param e the event.
	 */
	@EventHandler(ignoreCancelled = true)
	public void onGMChange(PlayerGameModeChangeEvent e) {
		Player p = e.getPlayer();
		if (!p.hasPermission("creativemanager.bypass.inventory")) {
			InventoryManager im = new InventoryManager(p, plugin);
			if (!plugin.getSettings().adventureInvEnable()) {
				if (e.getNewGameMode().equals(GameMode.ADVENTURE)) {
					im.saveInventory(p.getGameMode());
					return;
				} else if (p.getGameMode().equals(GameMode.ADVENTURE)) {
					im.saveInventory(GameMode.SURVIVAL);
					return;
				}
			}
			if (!plugin.getSettings().creativeInvEnable()) {
				if (e.getNewGameMode().equals(GameMode.CREATIVE)) {
					im.saveInventory(p.getGameMode());
					return;
				} else if (p.getGameMode().equals(GameMode.CREATIVE)) {
					im.saveInventory(GameMode.SURVIVAL);
					return;
				}
			}
			if (!plugin.getSettings().spectatorInvEnable()) {
				if (e.getNewGameMode().equals(GameMode.SPECTATOR)) {
					im.saveInventory(p.getGameMode());
					return;
				} else if (p.getGameMode().equals(GameMode.SPECTATOR)) {
					im.saveInventory(GameMode.SURVIVAL);
					return;
				}
			}
			im.saveInventory(p.getGameMode());
			im.loadInventory(e.getNewGameMode());
			HashMap<String, String> replaceMap = new HashMap<>();
			replaceMap.put("{GAMEMODE}", e.getNewGameMode().name());
			if(plugin.getSettings().getBoolean("send-player-messages"))
				Messages.sendMessage(plugin.getMessageManager(), p, "inventory.change", replaceMap);
		}
	}
}
