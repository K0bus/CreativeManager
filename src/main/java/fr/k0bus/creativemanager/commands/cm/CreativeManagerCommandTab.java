package fr.k0bus.creativemanager.commands.cm;

import fr.k0bus.creativemanager.commands.CustomCommands;
import fr.k0bus.creativemanager.commands.SubCommands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CreativeManagerCommandTab implements TabCompleter {

    SubCommands commands;

    public CreativeManagerCommandTab(SubCommands commands)
    {
        this.commands = commands;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List cmdList = new ArrayList();
        if(args.length == 1)
            for (Map.Entry<String, SubCommands> entry:commands.getSubCommands().entrySet()) {
                if(entry.getValue().canUse(sender, false))
                    cmdList.add(entry.getKey());
            }
        return cmdList;
    }
}
