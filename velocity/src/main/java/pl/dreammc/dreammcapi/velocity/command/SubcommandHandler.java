package pl.dreammc.dreammcapi.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import pl.dreammc.dreammcapi.api.command.response.CommandResponse;
import pl.dreammc.dreammcapi.api.command.response.ICommandResponse;
import pl.dreammc.dreammcapi.api.command.response.InvalidArgumentResponse;

import java.util.Map;

public class SubcommandHandler {

    public static ICommandResponse handleSubcommand(CommandSource source, String label, String[] args, String usage, Map<String, VelocitySubcommand> subcommands) {
        if(args.length == 0) return new InvalidArgumentResponse(usage);
        String nextArg = args[0].toLowerCase();
        if(!subcommands.containsKey(nextArg)) return CommandResponse.SUBCOMMAND_NOT_FOUND;
        VelocitySubcommand subcommand = subcommands.get(nextArg);
        ICommandResponse response = subcommand.execute(source, label, args);
        return (response instanceof CommandResponse enumResponse && enumResponse == CommandResponse.INVALID_ARGUMENTS) ? new InvalidArgumentResponse(subcommand.usage) : response;
    }

}
