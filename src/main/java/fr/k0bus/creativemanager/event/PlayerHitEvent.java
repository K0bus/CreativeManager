package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.settings.Protections;
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
	private final boolean enableProjectile;

	/**
	 * Instantiates a new Player hit event.
	 *
	 */
	public PlayerHitEvent(boolean enableProjectile) {
		this.enableProjectile = enableProjectile;
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
					if (!attacker.hasPermission("creativemanager.bypass.pvp") && CreativeManager.getSettings().getProtection(Protections.PVP)) {
						if (CreativeManager.getSettings().getBoolean("send-player-messages"))
							CreativeManager.sendMessage(attacker, CreativeManager.TAG + CreativeManager.getLang().getString("permission.hit.player"));
						e.setCancelled(true);
					}
				} else {
					if(e.getEntity().getType().equals(EntityType.ARMOR_STAND)) return;
					if (!attacker.hasPermission("creativemanager.bypass.pve") && CreativeManager.getSettings().getProtection(Protections.PVE)) {
						if (CreativeManager.getSettings().getBoolean("send-player-messages"))
							CreativeManager.sendMessage(attacker, CreativeManager.TAG + CreativeManager.getLang().getString("permission.hit.monster"));
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
		if(!enableProjectile) return;
		ProjectileSource source = e.getEntity().getShooter();
		if (source instanceof Player) {
			Player attacker = (Player) source;
			if (attacker.getGameMode().equals(GameMode.CREATIVE)) {
				if (e.getHitEntity() instanceof Player) {
					if (!attacker.hasPermission("creativemanager.bypass.pvp") && CreativeManager.getSettings().getProtection(Protections.PVP)) {
						if (CreativeManager.getSettings().getBoolean("send-player-messages"))
							CreativeManager.sendMessage(attacker, CreativeManager.TAG + CreativeManager.getLang().getString("permission.hit.player"));

						e.setCancelled(true);
					}
				} else {
					if (!attacker.hasPermission("creativemanager.bypass.pve") && CreativeManager.getSettings().getProtection(Protections.PVE)) {
						if(e.getHitEntity() == null) return;
						if (CreativeManager.getSettings().getBoolean("send-player-messages"))
							CreativeManager.sendMessage(attacker, CreativeManager.TAG + CreativeManager.getLang().getString("permission.hit.monster"));
						e.setCancelled(true);
					}
				}
			}
		}
	}
}
