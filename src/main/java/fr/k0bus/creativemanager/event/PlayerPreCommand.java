package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.k0buslib.utils.Messages;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * Player pre command event listener.
 */
public class PlayerPreCommand implements Listener {
    private final CreativeManager plugin;

    /**
     * Instantiates a new Player pre command.
     *
     * @param plugin the plugin.
     */
    public PlayerPreCommand(CreativeManager plugin) {
        this.plugin = plugin;
    }

    /**
     * On player command.
     *
     * @param e the event.
     */
    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
        if (!plugin.getSettings().getProtection(Protections.COMMANDS)) return;
        if (!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
        String cmd = e.getMessage().toLowerCase().substring(1);
        for (String blCmd : plugin.getSettings().getCommandBL()) {
            if (blCmd.toLowerCase().startsWith(cmd)) {
                e.setCancelled(true);
                if (plugin.getSettings().getBoolean("send-player-messages"))
                    Messages.sendMessage(plugin.getMessageManager(), e.getPlayer(), "blacklist.commands");
            }
        }
    }
}
