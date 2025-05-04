package fr.k0bus.creativemanager.commands.cm;

import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.commands.Commands;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Set;

public class ItemsSubCommands extends Commands {

    public ItemsSubCommands(CreativeManager instance) {
        super(instance, "creativemanager.admin", true);
    }

    @Override
    protected void run(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        StringBuilder tags = new StringBuilder();
        for(Map.Entry<String, Set<Material>> entry: CreativeManager.getTagMap().entrySet())
        {
            if(entry.getValue().contains(itemStack.getType()))
            {
                if(!tags.toString().isEmpty()) tags.append(", ");
                tags.append("§r#").append(entry.getKey()).append("§6");
            }
        }
        ReadWriteNBT tmp = NBT.itemStackToNBT(itemStack);
        StringBuilder nbtKey = new StringBuilder();
        for(String k: tmp.getKeys())
        {
            if(!nbtKey.toString().isEmpty()) nbtKey.append(", ");
            nbtKey.append(k).append("§6");
        }

        sender.sendMessage(CreativeManager.TAG + "§r");
        sender.sendMessage(CreativeManager.TAG + "§6You've requested items informations below");
        sender.sendMessage(CreativeManager.TAG + "§r");
        sender.sendMessage(CreativeManager.TAG + "§l§7Name §8>> §r§6" + itemStack.getType().name());
        sender.sendMessage(CreativeManager.TAG + "§l§7Tags §8>> §r§6[" + tags + "]");
        sender.sendMessage(CreativeManager.TAG + "§l§7NBT Keys §8>> §r§6[" + nbtKey + "]");
    }
}
