package pl.dreammc.dreammcapi.paper.command.response;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

public interface ICommandResponse {

    CommandResponse getResponse();
    @Nullable Component getMessage();

}
