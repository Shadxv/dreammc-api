package pl.dreammc.dreammcapi.paper.command;

import org.bukkit.command.CommandSender;
import pl.dreammc.dreammcapi.paper.command.response.CommandResponse;
import pl.dreammc.dreammcapi.paper.command.response.ICommandResponse;

import java.util.Map;

public class SubcommandHandler {

    public static ICommandResponse handleSubcommand(CommandSender sender, String label, String[] args, Map<String, PaperSubcommand> subcommands) {
        if(args.length == 0) return CommandResponse.INVALID_ARGUMENTS;
        String nextArg = args[0].toLowerCase();
        if(!subcommands.containsKey(nextArg)) return CommandResponse.INVALID_ARGUMENTS;
        return subcommands.get(nextArg).execute(sender, label, args);
    }

}
