package pl.dreammc.dreammcapi.paper.command;

import org.bukkit.command.CommandSender;
import pl.dreammc.dreammcapi.paper.command.response.CommandResponse;
import pl.dreammc.dreammcapi.paper.command.response.ICommandResponse;
import pl.dreammc.dreammcapi.paper.command.response.InvalidArgumentResponse;

import java.util.Map;

public class SubcommandHandler {

    public static ICommandResponse handleSubcommand(CommandSender sender, String label, String[] args, String usage, Map<String, PaperSubcommand> subcommands) {
        if(args.length == 0) return new InvalidArgumentResponse(usage);
        String nextArg = args[0].toLowerCase();
        if(!subcommands.containsKey(nextArg)) return CommandResponse.SUBCOMMAND_NOT_FOUND;
        PaperSubcommand subcommand = subcommands.get(nextArg);
        ICommandResponse response = subcommand.execute(sender, label, args);
        return (response instanceof CommandResponse enumResponse && enumResponse == CommandResponse.INVALID_ARGUMENTS) ? new InvalidArgumentResponse(subcommand.usage) : response;
    }

}
