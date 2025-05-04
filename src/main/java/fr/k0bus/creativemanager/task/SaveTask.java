package fr.k0bus.creativemanager.task;

import fr.k0bus.creativemanager.CreativeManager;
import org.bukkit.Bukkit;

/**
 * Save task class.
 */
public class SaveTask {
    /**
     * Run interval.
     *
     * @param plugin the plugin.
     * @return the interval.
     */
    public static int run(CreativeManager plugin) {
        int interval = plugin.getConfig().getInt("save-interval");
        if (interval > 0) {
            return Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () ->
                            plugin.getDataManager().saveAsync(),
                    0L, interval * 20L);
        }
        return 0;
    }
}
