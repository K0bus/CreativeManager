package fr.k0bus.creativemanager.utils;

import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class LoreUtils {
    public static String formatString(String text)
    {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static List<String> formatLoreString(String text)
    {
        List<String> tempList = new ArrayList<>();
        int maxLength = 25;
        boolean first = true;
        if(text.length() <= maxLength)
        {
            tempList.add(ChatColor.RESET + formatString(text));
            return tempList;
        }
        else
        {
            String color = "&r";
            while (text.length() > maxLength)
            {
                if(text.contains(" ") && text.indexOf(' ', maxLength) > 0)
                {
                    if(color.equals(""))
                        color = "&r";
                    String tempText = color + text.substring(0, text.indexOf(' ', maxLength));
                    if(first)
                        tempList.add(ChatColor.translateAlternateColorCodes('&',color + tempText));
                    else
                        tempList.add(ChatColor.translateAlternateColorCodes('&',color + " " + tempText));
                    if(tempText.lastIndexOf('&') > 0)
                        color = tempText.substring(tempText.lastIndexOf('&'), tempText.lastIndexOf('&')+2);
                    else
                    if(tempText.lastIndexOf('§') > 0)
                        color = tempText.substring(tempText.lastIndexOf('§'), tempText.lastIndexOf('§')+2);
                    text = text.substring(text.indexOf(' ',maxLength));
                    first = false;
                }
                else
                {
                    break;
                }
            }
            if(first)
                tempList.add(ChatColor.translateAlternateColorCodes('&',color + text));
            else
                tempList.add(ChatColor.translateAlternateColorCodes('&',color + " " + text));
        }
        return tempList;
    }
    public static String getStatusString(boolean value)
    {
        if(value) return ChatColor.GREEN + "Enabled";
        return ChatColor.RED + "Disabled";
    }
}
