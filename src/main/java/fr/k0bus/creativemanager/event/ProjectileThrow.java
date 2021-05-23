package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.k0buslib.utils.Messages;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.projectiles.ProjectileSource;

public class ProjectileThrow implements Listener {

	CreativeManager plugin;

	public ProjectileThrow(CreativeManager instance) {
		plugin = instance;
	}

	@EventHandler(ignoreCancelled = true)
	public void onDrop(ProjectileLaunchEvent e) {
		ProjectileSource source = e.getEntity().getShooter();
		if(source instanceof Player)
		{
			Player p = (Player) source;
			if(plugin.getSettings().getProtection(Protections.THROW) && p.getGameMode().equals(GameMode.CREATIVE))
			{
				if (!p.hasPermission("creativemanager.bypass.throw")) {
					if(plugin.getSettings().getBoolean("send-player-messages"))
						Messages.sendMessage(plugin.getMessageManager(), p, "permission.throw");
					e.setCancelled(true);
				}
			}
		}
	}
}
