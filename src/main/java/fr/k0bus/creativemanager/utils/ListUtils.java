package fr.k0bus.creativemanager.utils;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.type.ListType;
import org.bukkit.Material;

import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ListUtils {

    private static final String SEMILICON = "*";

    public static boolean inList(String search, List<String> list) {
        return inList(search, list, ListType.WHITELIST);
    }

    public static boolean inList(String search, List<String> list, ListType listType) {
        List<String> lowerList = TextUtils.listToLowerCase(list);
        for (String s : lowerList) {
            if (SEMILICON.equals(s)) return !listType.isBlacklistMode();
            if (s.isEmpty()) continue;
            if (s.startsWith(SEMILICON) && s.endsWith(SEMILICON) && search.contains(s.substring(1, s.length() - 1)))
                return !listType.isBlacklistMode();
            if (s.startsWith(SEMILICON) && search.endsWith(s.substring(1))) return !listType.isBlacklistMode();
            if (s.endsWith(SEMILICON) && search.startsWith(s.substring(0, s.length() - 1)))
                return !listType.isBlacklistMode();
            if (s.equals(search)) return !listType.isBlacklistMode();
            if (s.startsWith("#")) {
                Set<Material> set =
                        CreativeManager.getTagMap().get(s.substring(1).toUpperCase(Locale.getDefault()));
                if (set != null) {
                    if (set.contains(Material.valueOf(search.toUpperCase(Locale.getDefault()))))
                        return !listType.isBlacklistMode();
                } else {
                    CreativeManager.getInstance().getLogger().warning("Unable to find " + s + " tags");
                }
            }
        }
        return listType.isBlacklistMode();
    }
}
