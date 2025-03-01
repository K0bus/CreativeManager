package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.creativemanager.utils.CMUtils;
import fr.k0bus.creativemanager.utils.SearchUtils;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class PlayerPreCommand implements Listener {

    CreativeManager plugin;
    public PlayerPreCommand(CreativeManager plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerCommand(PlayerCommandPreprocessEvent e)
    {
        if(!CreativeManager.getSettings().getProtection(Protections.COMMANDS)) return;
        if(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
        if(e.getPlayer().hasPermission("creativemanager.bypass.blacklist.commands")) return;
        String cmd = e.getMessage().toLowerCase().substring(1);
        List<String> list = CreativeManager.getSettings().getCommandBL();
        if((CreativeManager.getSettings().getConfiguration().getString("list.mode.commands").equals("whitelist") && !SearchUtils.inList(list, cmd)) ||
                (!CreativeManager.getSettings().getConfiguration().getString("list.mode.commands").equals("whitelist") && SearchUtils.inList(list, cmd))){
            e.setCancelled(true);
            if(CreativeManager.getSettings().getConfiguration().getBoolean("send-player-messages"))
                CMUtils.sendMessage(e.getPlayer(), "blacklist.commands");
        }
    }
}
