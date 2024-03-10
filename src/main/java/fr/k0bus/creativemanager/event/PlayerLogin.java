package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.manager.InventoryManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * The type Player login.
 */
public class PlayerLogin implements Listener {
    private final CreativeManager plugin;

    /**
     * Instantiates a new Player login.
     *
     * @param plugin the plugin.
     */
    public PlayerLogin(CreativeManager plugin) {
        this.plugin = plugin;
    }

    /**
     * On player login/join.
     *
     * @param e the event.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onLogin(PlayerJoinEvent e) {
        if(e.getPlayer().hasPermission("creativemanager.admin.update"))
        {
            if(CreativeManager.getSettings().getBoolean("send-admin-update-message"))
            {
                if(!CreativeManager.getUpdateChecker().isUpToDate())
                {
                    BaseComponent[] tag = TextComponent.fromLegacyText(CreativeManager.TAG + " ");
                    TextComponent message = new TextComponent("CreativeManager updated on Spigot ");
                    TextComponent version = new TextComponent("v" + CreativeManager.getUpdateChecker().getVersion() + " ");
                    BaseComponent[] button = TextComponent.fromLegacyText("§7§l>> §r§5Click here to go on Spigot Page");

                    message.setColor(ChatColor.GOLD);
                    version.setColor(ChatColor.GREEN);
                    for(BaseComponent buttonComponent: button)
                    {
                        buttonComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/75097"));
                    }
                    BaseComponent[] component = new ComponentBuilder()
                            .append(tag)
                            .append(message)
                            .append(version)
                            .create();
                    e.getPlayer().spigot().sendMessage(component);
                    e.getPlayer().spigot().sendMessage(new ComponentBuilder()
                            .append(button)
                            .create());
                }
            }
        }

        boolean forceGamemode = false;

        try {
            BufferedReader is = new BufferedReader(new FileReader("server.properties"));
            Properties props = new Properties();
            props.load(is);
            is.close();
            forceGamemode = Boolean.parseBoolean(props.getProperty("force-gamemode"));
        } catch (IOException exception) {
            //
        }
        if (forceGamemode) {
            if (!e.getPlayer().hasPermission("creativemanager.bypass.inventory")) {
                InventoryManager im = new InventoryManager(e.getPlayer(), plugin);
                if(im.hasContent())
                    im.loadInventory(plugin.getServer().getDefaultGameMode());
                else
                    im.saveInventory(e.getPlayer().getGameMode());
            }
        }
    }
}
