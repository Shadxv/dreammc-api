package pl.dreammc.dreammcapi.paper.command;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.api.util.BaseColor;
import pl.dreammc.dreammcapi.paper.command.response.CommandResponse;
import pl.dreammc.dreammcapi.paper.command.response.ICommandResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class PaperCommand extends BukkitCommand implements ICommandBase{

    @Nullable protected final String permission;
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
        if(this.permission != null) this.setPermission(this.permission);
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

            switch (response.getResponse()) {
                default -> {
                    Optional.ofNullable(response.getMessage()).ifPresent(sender::sendMessage);
                }
            }
        });
        return false;
    }
}
