package pl.dreammc.dreammcapi.api.command.response;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomCommandResponse implements ICommandResponse{

    private final Component message;

    public CustomCommandResponse(@NotNull Component message) {
        this.message = message;
    }

    @Override
    public CommandResponse getResponse() {
        return CommandResponse.CUSTOM;
    }

    @Override
    public @Nullable Component getMessage() {
        return this.message;
    }
}
