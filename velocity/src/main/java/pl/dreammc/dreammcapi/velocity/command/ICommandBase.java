package pl.dreammc.dreammcapi.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import org.jetbrains.annotations.NotNull;
import pl.dreammc.dreammcapi.api.command.response.ICommandResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ICommandBase {

    @NotNull
    ICommandResponse execute0(@NotNull CommandSource sender, @NotNull String commandLabel, @NotNull String[] args);

    @NotNull
    default Map<Integer, List<String>> additionalCompletions(Player player) {
        return new HashMap<>();
    }

}
