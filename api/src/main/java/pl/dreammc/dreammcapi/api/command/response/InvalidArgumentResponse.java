package pl.dreammc.dreammcapi.api.command.response;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.api.util.BaseColor;

public class InvalidArgumentResponse implements ICommandResponse{

    private final String usage;

    public InvalidArgumentResponse(String usage) {
        this.usage = usage;
    }

    @Override
    public CommandResponse getResponse() {
        return CommandResponse.INVALID_ARGUMENTS;
    }

    @Override
    public @Nullable Component getMessage() {
        return this.getResponse().getMessage().append(Component.text(this.usage).color(TextColor.fromHexString(BaseColor.redPrimary)));
    }
}
