package pl.dreammc.dreammcapi.paper.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.paper.command.response.CommandResponse;
import pl.dreammc.dreammcapi.paper.command.response.ICommandResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class PaperSubcommand implements ICommandBase {

    protected final String name;
    protected final String description;
    protected final String usage;
    protected final List<String> aliases;
    @Nullable protected final String permission;
    protected final boolean isHidden;
    protected final boolean playerOnly;
    protected final Map<String, PaperSubcommand> subcommands;


    public PaperSubcommand(@NotNull String name, @NotNull String description, @NotNull String usage, @NotNull List<String> aliases, @Nullable String permission, boolean isHidden, boolean playerOnly, PaperSubcommand... subcommands) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.aliases = aliases;
        this.permission = permission;
        this.isHidden = isHidden;
        this.playerOnly = playerOnly;
        this.subcommands = new HashMap<>();
        for (PaperSubcommand subcommand : subcommands) {
            this.subcommands.put(subcommand.name, subcommand);
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
        return this.execute0(sender, commandLabel, nextArgs);
    }
}
