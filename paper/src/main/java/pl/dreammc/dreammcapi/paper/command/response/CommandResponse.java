package pl.dreammc.dreammcapi.paper.command.response;

import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.api.util.BaseColor;

@AllArgsConstructor
public enum CommandResponse implements ICommandResponse{
    ALLRIGHT(null),
    NO_PERMISSION("Nie posiadasz uprawnień do użycia tej komendy"),
    NOT_EXISTS("Taka komenda nie istnieje"),
    ONLY_PLAYER("Console does not support this command"),
    INVALID_ARGUMENTS("Prawidlowe uzycie: "),
    SUBCOMMAND_NOT_FOUND(null),
    CUSTOM(null);

    final String message;

    @Override
    public CommandResponse getResponse() {
        return this;
    }

    @Override
    public @Nullable Component getMessage() {
        return this.message == null ? null : Component.text(message).color(TextColor.fromHexString(BaseColor.redPrimary));
    }
}
