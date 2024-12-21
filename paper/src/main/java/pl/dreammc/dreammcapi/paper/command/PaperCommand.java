package pl.dreammc.dreammcapi.paper.command;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.api.util.BaseColor;
import pl.dreammc.dreammcapi.api.util.MessageSender;
import pl.dreammc.dreammcapi.paper.command.response.CommandResponse;
import pl.dreammc.dreammcapi.paper.command.response.ICommandResponse;
import pl.dreammc.dreammcapi.paper.command.response.InvalidArgumentResponse;

import java.util.*;

public abstract class PaperCommand extends BukkitCommand implements ICommandBase{

    @Nullable @Getter protected final String permission;
    @Getter protected final boolean playerOnly;
    @Getter protected final boolean isHidden;
    @Getter protected final Map<String, PaperSubcommand> subcommands;

    protected PaperCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases, @Nullable String permission, boolean playerOnly, boolean isHidden, PaperSubcommand... subcommands) {
        super(name, description, usageMessage, aliases);
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

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        Optional.ofNullable(this.canExecute(sender).getMessage()).ifPresentOrElse(sender::sendMessage, () ->  {
            ICommandResponse response;
            if(args.length == 0 || (response = SubcommandHandler.handleSubcommand(sender, commandLabel, args, this.usageMessage, this.subcommands)) == CommandResponse.SUBCOMMAND_NOT_FOUND)
                response = this.execute0(sender, commandLabel, args);
            if(response == CommandResponse.INVALID_ARGUMENTS)
                response = new InvalidArgumentResponse(this.usageMessage);
            switch (response.getResponse()) {
                default -> {
                    Optional.ofNullable(response.getMessage()).ifPresent(sender::sendMessage);
                }
            }
        });
        return false;
    }

    @Override @NotNull
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if(!(sender instanceof Player player)) return List.of();
        List<String> matchedCompletions = new ArrayList<>();

        int lastIndex = args.length - 1;
        PaperSubcommand lastSubcommand = null;
        Set<PaperSubcommand> subcommands;
        List<String> possibleArgs;
        if(lastIndex != 0) {
            for (int i = -1; i < lastIndex; i++) {
                if (i == -1 || lastSubcommand == null) {
                    PaperSubcommand possibleLastSubcommand = this.subcommands.get(args[lastIndex].toLowerCase());
                    if (possibleLastSubcommand == null) continue;
                    if(possibleLastSubcommand.permission != null && !player.hasPermission(possibleLastSubcommand.permission) && possibleLastSubcommand.isHidden) continue;
                    if (possibleLastSubcommand.getIndexOfSubcommandInArgsArray() == i)
                        lastSubcommand = possibleLastSubcommand;
                } else {
                    PaperSubcommand possibleLastSubcommand = lastSubcommand.getSubcommands().get(args[lastIndex].toLowerCase());
                    if (possibleLastSubcommand == null) continue;
                    if(possibleLastSubcommand.permission != null && !player.hasPermission(possibleLastSubcommand.permission) && possibleLastSubcommand.isHidden) continue;
                    if (possibleLastSubcommand.getIndexOfSubcommandInArgsArray() == i)
                        lastSubcommand = possibleLastSubcommand;
                }
            }
            if(lastSubcommand == null) return matchedCompletions;
            subcommands = new HashSet<>(lastSubcommand.getSubcommands().values());
            possibleArgs = lastSubcommand.additionalCompletions().get(lastIndex - lastSubcommand.getIndexOfSubcommandInArgsArray());
        } else {
            subcommands = new HashSet<>(this.subcommands.values());
            possibleArgs = this.additionalCompletions().get(lastIndex);
        }

        for(PaperSubcommand subcommand : subcommands) {
            if(subcommand.permission != null && !player.hasPermission(subcommand.permission) && subcommand.isHidden) continue;
            if(subcommand.getIndexOfSubcommandInArgsArray() != lastIndex) continue;
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
    }

}
