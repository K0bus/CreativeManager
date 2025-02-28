package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.manager.InventoryManager;
import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.creativemanager.utils.CMUtils;
import fr.k0bus.k0buscore.utils.ItemsUtils;
import fr.k0bus.k0buscore.utils.StringUtils;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.potion.PotionEffect;

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
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onGMChange(PlayerGameModeChangeEvent e) {
		try{
			if(!e.getPlayer().getOpenInventory().getType().equals(InventoryType.CRAFTING))
				e.getPlayer().closeInventory();
		}catch (Error ignored) {
			e.getPlayer().closeInventory();
		}
		if(e.getNewGameMode().equals(e.getPlayer().getGameMode())) return;
		if(CreativeManager.getSettings().getBoolean("stop-inventory-save")) return;
		Player p = e.getPlayer();
		if (!p.hasPermission("creativemanager.bypass.inventory")) {
			InventoryManager im = new InventoryManager(p, plugin);

			GameMode gmFrom = getGamemodeFromSetting(p.getGameMode());
			GameMode gmTo = getGamemodeFromSetting(e.getNewGameMode());

			if(!gmFrom.equals(gmTo))
			{
				im.saveInventory(gmFrom);
				im.loadInventory(gmTo);
			}

			if(CreativeManager.getSettings().getProtection(Protections.ARMOR) && !p.hasPermission("creativemanager.bypass.armor"))
			{
				if(e.getNewGameMode().equals(GameMode.CREATIVE))
				{
					ConfigurationSection cs = CreativeManager.getSettings().getConfigurationSection("creative_armor");
					if(cs != null)
					{
						p.getInventory().setHelmet(ItemsUtils.fromConfiguration(cs.getConfigurationSection("helmet"), e.getPlayer()));
						p.getInventory().setChestplate(ItemsUtils.fromConfiguration(cs.getConfigurationSection("chestplate"), e.getPlayer()));
						p.getInventory().setLeggings(ItemsUtils.fromConfiguration(cs.getConfigurationSection("leggings"), e.getPlayer()));
						p.getInventory().setBoots(ItemsUtils.fromConfiguration(cs.getConfigurationSection("boots"), e.getPlayer()));
					}
				}
			}
			HashMap<String, String> replaceMap = new HashMap<>();
			replaceMap.put("{GAMEMODE}", StringUtils.proper(e.getNewGameMode().name()));
			if(CreativeManager.getSettings().getBoolean("send-player-messages"))
				CMUtils.sendMessage(p, "inventory.change", replaceMap);
		}
	}

	private GameMode getGamemodeFromSetting(GameMode gameMode)
	{
		switch (gameMode)
		{
			case CREATIVE -> {
				if(!CreativeManager.getSettings().creativeInvEnable())
				{
					gameMode = GameMode.SURVIVAL;
				}
			}
			case ADVENTURE -> {
				if(!CreativeManager.getSettings().adventureInvEnable())
				{
					gameMode = GameMode.SURVIVAL;
				}
			}
			case SPECTATOR -> {
				if(CreativeManager.getSettings().spectatorInvEnable())
				{
					gameMode = GameMode.SURVIVAL;
				}
			}
		}
		return gameMode;
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onGMChangeRemoveEffects(PlayerGameModeChangeEvent e) {
		if (!CreativeManager.getSettings().getProtection(Protections.REMOVE_EFFECTS)) return;
		Player p = e.getPlayer();
		if (!p.hasPermission("creativemanager.bypass.effects-cleaner")) {
			for (PotionEffect effect:p.getActivePotionEffects()) {
				p.removePotionEffect(effect.getType());
			}
		}
	}
}
