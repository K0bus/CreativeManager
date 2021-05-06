package fr.k0bus.creativemanager.utils;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Formater {
    public static String formatColor(String text)
    {
        Pattern p = Pattern.compile("#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})");
        Matcher m = p.matcher(text);

        while (m.find())
        {
            text = text.replace("#" + m.group(1), ChatColor.of("#" + m.group(1)).toString());
        }
        return ChatColor.translateAlternateColorCodes('&',text);
    }
}
