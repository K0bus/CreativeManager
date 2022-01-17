package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.k0buslib.utils.Messages;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.projectiles.ProjectileSource;

/**
 * Player hit event listener.
 */
public class PlayerHitEvent implements Listener {
	private final CreativeManager plugin;
	private final boolean ensablePorjectile;

	/**
	 * Instantiates a new Player hit event.
	 *
	 * @param instance the instance.
	 */
	public PlayerHitEvent(CreativeManager instance, boolean ensablePorjectile) {
		plugin = instance;
		this.ensablePorjectile = ensablePorjectile;
	}

	/**
	 * On entity hit.
	 *
	 * @param e the event.
	 */
	@EventHandler(ignoreCancelled = true)
	public void onEntityHit(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			Player attacker = (Player) e.getDamager();
			if (attacker.getGameMode().equals(GameMode.CREATIVE)) {
				if (e.getEntity() instanceof Player) {
					if (!attacker.hasPermission("creativemanager.bypass.pvp") && plugin.getSettings().getProtection(Protections.PVP)) {
						if (plugin.getSettings().getBoolean("send-player-messages"))
							Messages.sendMessage(plugin.getMessageManager(), attacker, "permission.hit.player");
						e.setCancelled(true);
					}
				} else {
					if(e.getEntity().getType().equals(EntityType.ARMOR_STAND)) return;
					if (!attacker.hasPermission("creativemanager.bypass.pve") && plugin.getSettings().getProtection(Protections.PVE)) {
						if (plugin.getSettings().getBoolean("send-player-messages"))
							Messages.sendMessage(plugin.getMessageManager(), attacker, "permission.hit.monster");
						e.setCancelled(true);
					}
				}
			}
		}
	}

	/**
	 * On projectile hit.
	 *
	 * @param e the event.
	 */
	@EventHandler(ignoreCancelled = true)
	public void onProjectileHit(ProjectileHitEvent e) {
		if(!ensablePorjectile) return;
		ProjectileSource source = e.getEntity().getShooter();
		if (source instanceof Player) {
			Player attacker = (Player) source;
			if (attacker.getGameMode().equals(GameMode.CREATIVE)) {
				if (e.getHitEntity() instanceof Player) {
					if (!attacker.hasPermission("creativemanager.bypass.pvp") && plugin.getSettings().getProtection(Protections.PVP)) {
						if (plugin.getSettings().getBoolean("send-player-messages"))
							Messages.sendMessage(plugin.getMessageManager(), attacker, "permission.hit.player");
						e.setCancelled(true);
					}
				} else {
					if (!attacker.hasPermission("creativemanager.bypass.pve") && plugin.getSettings().getProtection(Protections.PVE)) {
						if (plugin.getSettings().getBoolean("send-player-messages"))
							Messages.sendMessage(plugin.getMessageManager(), attacker, "permission.hit.monster");
						e.setCancelled(true);
					}
				}
			}
		}
	}
}
