package org.sproject.sprojectapi.api.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.sproject.sprojectapi.shared.Registry;

public class MessageSender {

    private static void sendMessage(Object player, Component message) {
        if(Registry.messageSender == null) throw new RuntimeException("Message sender is not initialized");
        Registry.messageSender.sendMessage(player, message);
    }

    public static void sendErrorMessage(Object player, String message) {
        sendMessage(player, Component.text(message).color(TextColor.fromHexString(BaseColor.redPrimary)));
    }

    public static void sendInfoMessage(Object player, String message) {
        sendMessage(player, Component.text(message).color(TextColor.fromHexString(BaseColor.grayPrimary)));
    }

    public static void sendSuccessMessage(Object player, String message) {
        sendMessage(player, Component.text(message).color(TextColor.fromHexString(BaseColor.greenPrimary)));
    }

}
