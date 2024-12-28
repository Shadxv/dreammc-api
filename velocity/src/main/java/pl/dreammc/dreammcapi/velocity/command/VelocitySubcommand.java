package pl.dreammc.dreammcapi.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.api.command.response.CommandResponse;
import pl.dreammc.dreammcapi.api.command.response.ICommandResponse;

import java.util.*;

@Getter
public abstract class VelocitySubcommand implements ICommandBase{

    protected final String name;
    protected final String description;
    protected final String usage;
    protected final List<String> aliases;
    @Nullable protected final String permission;
    protected final boolean isHidden;
    protected final boolean playerOnly;
    protected final Map<String, VelocitySubcommand> subcommands;
    protected int indexOfSubcommandInArgsArray;


    protected VelocitySubcommand(@NotNull String name, @NotNull String description, @NotNull String usage, @NotNull List<String> aliases, @Nullable String permission, boolean isHidden, boolean playerOnly, VelocitySubcommand... subcommands) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.aliases = aliases;
        this.permission = permission;
        this.isHidden = isHidden;
        this.playerOnly = playerOnly;
        this.subcommands = new HashMap<>();
        for (VelocitySubcommand subcommand : subcommands) {
            this.subcommands.put(subcommand.name, subcommand);
            for(String alias : subcommand.aliases) {
                this.subcommands.put(alias, subcommand);
            }
        }
        this.indexOfSubcommandInArgsArray = 0;
    }

    protected VelocitySubcommand(@NotNull String name, @NotNull String description, @NotNull String usage, @NotNull List<String> aliases, @Nullable String permission, int indexOfSubcommandInArgsArray, boolean isHidden, boolean playerOnly, VelocitySubcommand... subcommands) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.aliases = aliases;
        this.permission = permission;
        this.isHidden = isHidden;
        this.playerOnly = playerOnly;
        this.subcommands = new HashMap<>();
        for (VelocitySubcommand subcommand : subcommands) {
            this.subcommands.put(subcommand.name, subcommand);
            subcommand.incrementIndex(this.indexOfSubcommandInArgsArray + 1);
            for(String alias : subcommand.aliases) {
                this.subcommands.put(alias, subcommand);
            }
        }
        this.indexOfSubcommandInArgsArray = indexOfSubcommandInArgsArray;
    }

    public void incrementIndex(int delta) {
        this.indexOfSubcommandInArgsArray += delta;
        Set<VelocitySubcommand> subcommandSet = new HashSet<>(this.subcommands.values());
        for(VelocitySubcommand subcommand : subcommandSet) {
            subcommand.incrementIndex(delta + 1);
        }
    }

    private ICommandResponse canExecute(CommandSource source) {
        if(source instanceof Player) {
            if(this.permission != null && !source.hasPermission(this.permission)) {
                if(this.isHidden) return CommandResponse.NOT_EXISTS;
                return CommandResponse.NO_PERMISSION;
            }
        } else {
            if(this.playerOnly) return CommandResponse.ONLY_PLAYER;
        }
        return CommandResponse.ALLRIGHT;
    }

    public ICommandResponse execute(@NotNull CommandSource source, @NotNull String commandLabel, @NotNull String[] args) {
        ICommandResponse checkResult = this.canExecute(source);
        if(checkResult != CommandResponse.ALLRIGHT) return checkResult;
        String[] nextArgs;
        if(args.length > 1) {
            nextArgs = new String[args.length - 1];
            System.arraycopy(args, 1, nextArgs, 0, nextArgs.length);
        } else {
            nextArgs = new String[0];
        }
        ICommandResponse response;
        if(args.length == 0 || (response = SubcommandHandler.handleSubcommand(source, commandLabel, args, this.usage, this.subcommands)) == CommandResponse.SUBCOMMAND_NOT_FOUND)
            response = this.execute0(source, commandLabel, nextArgs);
        return response;
    }

}
