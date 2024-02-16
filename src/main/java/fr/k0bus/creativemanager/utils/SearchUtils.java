package fr.k0bus.creativemanager.utils;

import fr.k0bus.creativemanager.CreativeManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

public class SearchUtils {
    public static boolean inList(List<String> stringList, String string)
    {
        string = string.toLowerCase();
        for (String s:stringList) {
            s = s.toLowerCase();
            if(s.equals("*")) return true;
            if(s.isEmpty()) continue;
            if(s.startsWith("*") && s.endsWith("*"))
            {
                if(string.contains(s.substring(1, s.length()-1))){
                    return true;
                }
            }
            if(s.startsWith("*"))
            {
                if(string.endsWith(s.substring(1))){
                    return true;
                }
            }
            if(s.endsWith("*"))
            {
                if(string.startsWith(s.substring(0, s.length()-1))){
                    return true;
                }
            }
            if(string.equals(s))
                return true;
        }
        return false;
    }
    public static boolean inList(List<String> stringList, Material material)
    {
        if(inList(stringList, material.name()))
            return true;
        else if(!material.equals(Material.AIR))
        {
            for (String s:stringList) {
                if(s.startsWith("#"))
                {
                    Set<Material> set = CreativeManager.getTagMap().get(s.substring(1).toUpperCase());
                    if(set != null)
                    {
                        if(set.contains(material)) return true;
                    }
                    else
                    {
                        CreativeManager.getLog().log("Unable to find " + s.substring(1) + " tags");
                    }
                }
            }
        }
        return false;
    }
    public static boolean inList(List<String> stringList, ItemStack itemStack)
    {
        return inList(stringList, itemStack.getType());
    }
    public static boolean inList(List<String> stringList, Block block)
    {
        return inList(stringList, block.getType());
    }
}
