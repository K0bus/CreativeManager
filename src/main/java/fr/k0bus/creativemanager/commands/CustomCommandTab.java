package fr.k0bus.creativemanager.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomCommandTab implements TabCompleter {

    SubCommands commands;

    public CustomCommandTab(SubCommands commands)
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
