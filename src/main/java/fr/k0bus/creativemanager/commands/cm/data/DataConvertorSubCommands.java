package fr.k0bus.creativemanager.commands.cm.data;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.commands.SubCommands;
import fr.k0bus.creativemanager.manager.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataConvertorSubCommands extends SubCommands {

    public DataConvertorSubCommands(CreativeManager instance) {
        super(instance, "creativemanager.admin", true);
    }

    @Override
    protected void run(CommandSender sender, String[] args) {
        sender.sendMessage(CreativeManager.TAG + ChatColor.RED + "Not available yet.");
        return;
        /*if(sender instanceof Conversable)
        {

            if(GameModeInventories.plugin.isEnabled()) {
                try {
                    PreparedStatement statement = GameModeInventories.plugin.getDatabaseConnection().prepareStatement("SELECT * FROM " + GameModeInventories.plugin.getPrefix() + "inventories");
                    statement.execute();
                    ResultSet rs = statement.getResultSet();
                    while (rs.next())
                    {
                        InventoryManager im = new InventoryManager(null, plugin);
                    }

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            sender.sendMessage("§§l§7CM DEBUG §8>>§r §6" + plugin.getDescription().getName() + " version §7:§b " + plugin.getDescription().getVersion());
            sender.sendMessage("§§l§7CM DEBUG §8>>§r §6Server version §7:§b " + Bukkit.getVersion());
            sender.sendMessage("§§l§7CM DEBUG §8>>§r §6MC version §7:§b " + Bukkit.getBukkitVersion());
        }*/
    }
}
