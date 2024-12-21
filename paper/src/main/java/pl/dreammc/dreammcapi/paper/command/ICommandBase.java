package pl.dreammc.dreammcapi.paper.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.paper.command.response.ICommandResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ICommandBase {

    @NotNull ICommandResponse execute0(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args);

    @NotNull
    default Map<Integer, List<String>> additionalCompletions(Player player) {
        return new HashMap<>();
    }

}
