package pl.dreammc.dreammcapi.velocity.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import pl.dreammc.dreammcapi.api.util.BaseColor;

@AllArgsConstructor
public enum ProfileTransferKickMessage {

    CONNECTION_ERROR("Wystąpił problem podczas nawiązywania połączenia z serwerem. Spróbuj ponownie."),
    PROFILE_NOT_LOADED("Serwer nie mógł wczytać Twojego profilu. Spróbuj ponownie później."),
    PROFILE_NOT_TRANSFERED("Wystąpił błąd podczas przesyłania danych. Spróbuj ponownie później.");


    final String message;

    public Component getMessage() {
        return Component.text(this.message).color(TextColor.fromHexString(BaseColor.redPrimary));
    }
}
