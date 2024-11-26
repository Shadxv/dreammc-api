package pl.dreammc.dreammcapi.paper.command;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pl.dreammc.dreammcapi.paper.command.response.ICommandResponse;

import java.util.List;

public interface ICommandBase {

    @NotNull ICommandResponse execute0(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args);
    default List<String> additionalCompletions() {
        return List.of();
    }

}
