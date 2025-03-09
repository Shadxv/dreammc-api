package pl.dreammc.dreammcapi.api.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

@Getter @AllArgsConstructor
public enum Symbol {
    CROSS("✗"),
    DOT("•"),
    ARROW("➥"),
    LINE("|");

    final String symbolChar;

    public Component setPrefixTo(String text, String colorHex, TextDecoration... decorations) {
        return Component.text(this.symbolChar + " ").color(TextColor.fromHexString(colorHex)).decorate(decorations).append(TextUtil.deserializeText(text));
    }

    public Component setPrefixTo(Component text, String colorHex, TextDecoration... decorations) {
        return Component.text(this.symbolChar + " ").color(TextColor.fromHexString(colorHex)).decorate(decorations).append(text);
    }

    public Component setSuffixTo(String text, String colorHex, TextDecoration... decorations) {
        return TextUtil.deserializeText(text).append(Component.text(this.symbolChar + " ").color(TextColor.fromHexString(colorHex)).decorate(decorations));
    }

    public Component setSuffixTo(Component text, String colorHex, TextDecoration... decorations) {
        return text.append(Component.text(this.symbolChar + " ").color(TextColor.fromHexString(colorHex)).decorate(decorations));
    }
}
