package fr.k0bus.creativemanager.commands.cm;

import fr.k0bus.creativemanager.commands.Commands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CreativeManagerCommandTab implements TabCompleter {

    final Commands commands;

    public CreativeManagerCommandTab(Commands commands)
    {
        this.commands = commands;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> cmdList = new ArrayList<>();
        if(args.length == 1)
            for (Map.Entry<String, Commands> entry:commands.getSubCommands().entrySet()) {
                if(entry.getValue().canUse(sender, false))
                    cmdList.add(entry.getKey());
            }
        return cmdList;
    }
}
