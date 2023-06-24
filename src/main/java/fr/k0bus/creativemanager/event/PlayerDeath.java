package fr.k0bus.creativemanager.event;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e)
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
