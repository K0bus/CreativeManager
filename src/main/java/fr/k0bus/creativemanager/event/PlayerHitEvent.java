package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.creativemanager.utils.CMUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.projectiles.ProjectileSource;

/**
 * Player hit event listener.
 */
public class PlayerHitEvent implements Listener {
	private final boolean enableProjectile;
	CreativeManager plugin;

	/**
	 * Instantiates a new Player hit event.
	 *
	 */
	public PlayerHitEvent(boolean enableProjectile, CreativeManager plugin) {
		this.enableProjectile = enableProjectile;
		this.plugin = plugin;
	}

	/**
	 * On entity hit.
	 *
	 * @param e the event.
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityHit(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player attacker) {
			if (attacker.getGameMode().equals(GameMode.CREATIVE)) {
				if (e.getEntity() instanceof Player) {
					if (!attacker.hasPermission("creativemanager.bypass.pvp") && CreativeManager.getSettings().getProtection(Protections.PVP)) {
						if (CreativeManager.getSettings().getBoolean("send-player-messages"))
							CMUtils.sendMessage(attacker, "permission.hit.player");
						e.setCancelled(true);
					}
				} else {
					if(e.getEntity().getType().equals(EntityType.ARMOR_STAND)) return;
					if (!attacker.hasPermission("creativemanager.bypass.pve") && CreativeManager.getSettings().getProtection(Protections.PVE)) {
						if (CreativeManager.getSettings().getBoolean("send-player-messages"))
							CMUtils.sendMessage(attacker, "permission.hit.monster");
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
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onProjectileHit(ProjectileHitEvent e) {
		if(!enableProjectile) return;
		ProjectileSource source = e.getEntity().getShooter();
		if (source instanceof Player attacker) {
			if (attacker.getGameMode().equals(GameMode.CREATIVE)) {
				if (e.getHitEntity() instanceof Player) {
					if (!attacker.hasPermission("creativemanager.bypass.pvp") && CreativeManager.getSettings().getProtection(Protections.PVP)) {
						if (CreativeManager.getSettings().getBoolean("send-player-messages"))
							CMUtils.sendMessage(attacker, "permission.hit.player");
						e.setCancelled(true);
					}
				} else {
					if (!attacker.hasPermission("creativemanager.bypass.pve") && CreativeManager.getSettings().getProtection(Protections.PVE)) {
						if(e.getHitEntity() == null) return;
						if (CreativeManager.getSettings().getBoolean("send-player-messages"))
							CMUtils.sendMessage(attacker, "permission.hit.monster");
						e.setCancelled(true);
					}
				}
			}
		}
	}
}
