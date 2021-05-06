package fr.k0bus.creativemanager.utils;

import fr.k0bus.creativemanager.CreativeManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class Messages {
    public static void sendMessage(CreativeManager plugin, Player player, String string)
    {
        sendMessageText(plugin, player, plugin.getLang().getString(string));
    }
    public static void sendMessage(CreativeManager plugin, Player player, String string, HashMap<String, String> replace)
    {
        String text = plugin.getLang().getString(string);
        for (Map.Entry<String, String> entry : replace.entrySet()) {
            text = text.replace(entry.getKey(), entry.getValue());
        }
        sendMessageText(plugin, player, text);
    }
    public static void sendMessageText(CreativeManager plugin, Player player, String text)
    {
        if(plugin.getAntiSpam().containsKey(player.getUniqueId())) {
            if (plugin.getAntiSpam().get(player.getUniqueId()) < System.currentTimeMillis()) {
                plugin.getAntiSpam().remove(player.getUniqueId());
                plugin.getAntiSpam().put(player.getUniqueId(), System.currentTimeMillis() + 100);
                player.sendMessage(Formater.formatColor( plugin.getSettings().getTag() + text));
            }
        }
        else {
            plugin.getAntiSpam().put(player.getUniqueId(), System.currentTimeMillis() + 100);
            player.sendMessage(Formater.formatColor(plugin.getSettings().getTag() + text));
        }
    }
    public static void sendMessage(CreativeManager plugin, CommandSender sender, String string)
    {
        sendMessageText(plugin, sender, plugin.getLang().getString(string));
    }
    public static void sendMessage(CreativeManager plugin, CommandSender sender, String string, HashMap<String, String> replace)
    {
        String text = plugin.getLang().getString(string);
        for (Map.Entry<String, String> entry : replace.entrySet()) {
            text = text.replace(entry.getKey(), entry.getValue());
        }
        sendMessageText(plugin, sender, text);
    }
    public static void sendMessageText(CreativeManager plugin, CommandSender sender, String text)
    {
        sender.sendMessage(Formater.formatColor( plugin.getSettings().getTag() + text));
    }

    public static void log(CreativeManager plugin, String text)
    {
        log(plugin, text, Level.INFO);
    }
    public static void log(CreativeManager plugin, String text, Level level)
    {
        plugin.getLogger().log(level, Formater.formatColor(text));
    }
}
