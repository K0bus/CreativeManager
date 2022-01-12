package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerDeath implements Listener {

    @EventHandler
    public void onPlayerDeath(org.bukkit.event.entity.PlayerDeathEvent e)
    {
        Player p = e.getEntity();
        if(p.getGameMode().equals(GameMode.CREATIVE))
        {
            if(!p.hasPermission("creativemanager.bypass.deathdrop"))
            {
                e.setDroppedExp(0);
                e.getDrops().clear();
            }
        }
    }
}
