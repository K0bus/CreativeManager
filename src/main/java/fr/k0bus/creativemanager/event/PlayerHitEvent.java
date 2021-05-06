package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.creativemanager.utils.Messages;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import fr.k0bus.creativemanager.CreativeManager;
import net.md_5.bungee.api.ChatColor;

public class PlayerHitEvent implements Listener {

	CreativeManager plugin;

	public PlayerHitEvent(CreativeManager instance) {
		plugin = instance;
	}

	@EventHandler(ignoreCancelled = true)
	public void onGMChange(EntityDamageByEntityEvent   e) {
		if(e.getDamager() instanceof Player)
		{
			Player attacker = (Player) e.getDamager();
			if(attacker.getGameMode().equals(GameMode.CREATIVE))
			{
				if(e.getEntity() instanceof Player)
				{
					if(!attacker.hasPermission("creativemanager.bypass.pvp") && plugin.getSettings().getProtection(Protections.PVP))
					{
						Messages.sendMessage(plugin, attacker, "permission.hit.player");
						e.setCancelled(true);
					}
				}
				else
				{
					if(!attacker.hasPermission("creativemanager.bypass.pve") && plugin.getSettings().getProtection(Protections.PVE))
					{
						Messages.sendMessage(plugin, attacker, "permission.hit.monster");
						e.setCancelled(true);
					}
				}
			}
		}
	}
}
