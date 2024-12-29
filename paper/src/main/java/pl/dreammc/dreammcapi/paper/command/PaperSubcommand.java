package pl.dreammc.dreammcapi.paper.command;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.api.command.response.CommandResponse;
import pl.dreammc.dreammcapi.api.command.response.ICommandResponse;

import java.util.*;

@Getter
public abstract class PaperSubcommand implements ICommandBase {

    protected final String name;
    protected final String description;
    protected final String usage;
    protected final List<String> aliases;
    @Nullable protected final String permission;
    protected final boolean isHidden;
    protected final boolean playerOnly;
    protected final Map<String, PaperSubcommand> subcommands;
    protected int indexOfSubcommandInArgsArray;


    protected PaperSubcommand(@NotNull String name, @NotNull String description, @NotNull String usage, @NotNull List<String> aliases, @Nullable String permission, boolean playerOnly, boolean isHidden, PaperSubcommand... subcommands) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.aliases = aliases;
        this.permission = permission;
        this.playerOnly = playerOnly;
        this.isHidden = isHidden;
        this.subcommands = new HashMap<>();
        for (PaperSubcommand subcommand : subcommands) {
            this.subcommands.put(subcommand.name, subcommand);
            for(String alias : subcommand.aliases) {
                this.subcommands.put(alias, subcommand);
            }
        }
        this.indexOfSubcommandInArgsArray = 0;
    }

    protected PaperSubcommand(@NotNull String name, @NotNull String description, @NotNull String usage, @NotNull List<String> aliases, @Nullable String permission, int indexOfSubcommandInArgsArray, boolean playerOnly, boolean isHidden, PaperSubcommand... subcommands) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.aliases = aliases;
        this.permission = permission;
        this.playerOnly = playerOnly;
        this.isHidden = isHidden;
        this.subcommands = new HashMap<>();
        for (PaperSubcommand subcommand : subcommands) {
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
        Set<PaperSubcommand> subcommandSet = new HashSet<>(this.subcommands.values());
        for(PaperSubcommand subcommand : subcommandSet) {
            subcommand.incrementIndex(delta + 1);
        }
    }

    private ICommandResponse canExecute(CommandSender commandSender) {
        if(commandSender instanceof Player) {
            if(this.permission != null && !commandSender.hasPermission(this.permission)) {
                if(this.isHidden) return CommandResponse.NOT_EXISTS;
                return CommandResponse.NO_PERMISSION;
            }
        } else {
            if(this.playerOnly) return CommandResponse.ONLY_PLAYER;
        }
        return CommandResponse.ALLRIGHT;
    }

    public ICommandResponse execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        ICommandResponse checkResult = this.canExecute(sender);
        if(checkResult != CommandResponse.ALLRIGHT) return checkResult;
        String[] nextArgs;
        if(args.length > 1) {
            nextArgs = new String[args.length - 1];
            System.arraycopy(args, 1, nextArgs, 0, nextArgs.length);
        } else {
            nextArgs = new String[0];
        }
        ICommandResponse response;
        if(args.length == 0 || (response = SubcommandHandler.handleSubcommand(sender, commandLabel, args, this.usage, this.subcommands)) == CommandResponse.SUBCOMMAND_NOT_FOUND)
            response = this.execute0(sender, commandLabel, nextArgs);
        return response;
    }
}
