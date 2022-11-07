package fr.k0bus.creativemanager.utils;

import java.util.List;

public class SearchUtils {
    public static boolean inList(List<String> stringList, String string)
    {
        string = string.toLowerCase();
        for (String s:stringList) {
            s = s.toLowerCase();
            if(s.equals("*")) return true;
            if(s.equals("")) continue;
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
}
