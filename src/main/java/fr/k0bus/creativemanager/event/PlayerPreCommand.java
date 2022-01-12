package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.k0buslib.utils.Messages;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerPreCommand implements Listener {

    final CreativeManager plugin;

    public PlayerPreCommand(CreativeManager plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerCommand(PlayerCommandPreprocessEvent e)
    {
        if(!plugin.getSettings().getProtection(Protections.COMMANDS)) return;
        if(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
        if(e.getPlayer().hasPermission("creativemanager.bypass.blacklist.commands")) return;
        String cmd = e.getMessage().toLowerCase().substring(1);
        for (String blCmd:plugin.getSettings().getCommandBL()) {
            if(cmd.startsWith(blCmd.toLowerCase()))
            {
                e.setCancelled(true);
                if(plugin.getSettings().getBoolean("send-player-messages"))
                    Messages.sendMessage(plugin.getMessageManager(), e.getPlayer(), "blacklist.commands");
            }
        }
    }
}
