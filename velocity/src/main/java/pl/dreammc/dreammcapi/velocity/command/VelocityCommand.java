package pl.dreammc.dreammcapi.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.api.command.response.CommandResponse;
import pl.dreammc.dreammcapi.api.command.response.ICommandResponse;
import pl.dreammc.dreammcapi.api.command.response.InvalidArgumentResponse;
import pl.dreammc.dreammcapi.velocity.util.StringUtil;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public abstract class VelocityCommand implements SimpleCommand, ICommandBase {

    @Getter protected final String name;
    @Getter protected final String description;
    @Getter protected final String usageMessage;
    @Getter protected final List<String> aliases;
    @Nullable @Getter protected final String permission;
    @Getter protected final boolean playerOnly;
    @Getter protected final boolean isHidden;
    @Getter protected final Map<String, VelocitySubcommand> subcommands;

    protected VelocityCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases, @Nullable String permission, boolean playerOnly, boolean isHidden, VelocitySubcommand... subcommands) {
        this.name = name;
        this.description = description;
        this.usageMessage = usageMessage;
        this.aliases = aliases;
        this.permission = permission;
        this.playerOnly = playerOnly;
        this.isHidden = isHidden;
        this.subcommands = new HashMap<>();
        for (VelocitySubcommand subcommand : subcommands) {
            this.subcommands.put(subcommand.name, subcommand);
            for(String alias : subcommand.aliases) {
                this.subcommands.put(alias, subcommand);
            }
        }
    }

    private ICommandResponse canExecute(CommandSource commandSource) {
        if(commandSource instanceof Player) {
            if(this.permission != null && !commandSource.hasPermission(this.permission)) {
                if(this.isHidden) return CommandResponse.NOT_EXISTS;
                return CommandResponse.NO_PERMISSION;
            }
        } else {
            if(this.playerOnly) return CommandResponse.ONLY_PLAYER;
        }
        return CommandResponse.ALLRIGHT;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        String commandLabel = invocation.alias();
        Optional.ofNullable(this.canExecute(source).getMessage()).ifPresentOrElse(source::sendMessage, () ->  {
            ICommandResponse response;
            if(args.length == 0 || (response = SubcommandHandler.handleSubcommand(source, commandLabel, args, this.usageMessage, this.subcommands)) == CommandResponse.SUBCOMMAND_NOT_FOUND)
                response = this.execute0(source, commandLabel, args);
            if(response == CommandResponse.INVALID_ARGUMENTS)
                response = new InvalidArgumentResponse(this.usageMessage);
            switch (response.getResponse()) {
                default -> {
                    Optional.ofNullable(response.getMessage()).ifPresent(source::sendMessage);
                }
            }
        });
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        return CompletableFuture.supplyAsync(() -> {
            CommandSource source = invocation.source();
            String[] args = invocation.arguments();

            if(!(source instanceof Player player)) return List.of();
            List<String> matchedCompletions = new ArrayList<>();

            int lastIndex = args.length - 1;
            VelocitySubcommand lastSubcommand = null;
            Set<VelocitySubcommand> subcommands;
            List<String> possibleArgs;
            if(lastIndex != 0) {
                for (int i = 0; i < lastIndex; i++) {
                    if (i == 0 || lastSubcommand == null) {
                        VelocitySubcommand possibleLastSubcommand = this.subcommands.get(args[i].toLowerCase());
                        if (possibleLastSubcommand == null) continue;
                        if (possibleLastSubcommand.permission != null && !player.hasPermission(possibleLastSubcommand.permission) && possibleLastSubcommand.isHidden) continue;
                        if (possibleLastSubcommand.getIndexOfSubcommandInArgsArray() == i)
                            lastSubcommand = possibleLastSubcommand;
                    } else {
                        VelocitySubcommand possibleLastSubcommand = lastSubcommand.getSubcommands().get(args[i].toLowerCase());
                        if (possibleLastSubcommand == null) continue;
                        if (possibleLastSubcommand.permission != null && !player.hasPermission(possibleLastSubcommand.permission) && possibleLastSubcommand.isHidden) continue;
                        if (possibleLastSubcommand.getIndexOfSubcommandInArgsArray() == i)
                            lastSubcommand = possibleLastSubcommand;
                    }
                }
                if(lastSubcommand == null) return matchedCompletions;
                subcommands = new HashSet<>(lastSubcommand.getSubcommands().values());
                possibleArgs = lastSubcommand.additionalCompletions(player).get(lastIndex - 1 - lastSubcommand.getIndexOfSubcommandInArgsArray());
            } else {
                subcommands = new HashSet<>(this.subcommands.values());
                possibleArgs = this.additionalCompletions(player).get(lastIndex);
            }

            for(VelocitySubcommand subcommand : subcommands) {
                if(subcommand.permission != null && !player.hasPermission(subcommand.permission) && subcommand.isHidden) continue;
                if(subcommand.getIndexOfSubcommandInArgsArray() != (lastIndex - (lastSubcommand != null ? lastSubcommand.getIndexOfSubcommandInArgsArray() + 1 : 0))) continue;
                if(StringUtil.startsWithIgnoreCase(subcommand.name, args[lastIndex]))
                    matchedCompletions.add(subcommand.name);
                // Removed aliases
                // for(String subAlias : subcommand.aliases) {
                //     if(StringUtil.startsWithIgnoreCase(subAlias, args[lastIndex]))
                //         matchedCompletions.add(subAlias);
                // }
            }


            if(possibleArgs == null) {
                matchedCompletions.sort(String.CASE_INSENSITIVE_ORDER);
                return matchedCompletions;
            }

            for(String arg : possibleArgs) {
                if(StringUtil.startsWithIgnoreCase(arg, args[lastIndex]))
                    matchedCompletions.add(arg);
            }

            matchedCompletions.sort(String.CASE_INSENSITIVE_ORDER);
            return matchedCompletions;
        });
    }
}
