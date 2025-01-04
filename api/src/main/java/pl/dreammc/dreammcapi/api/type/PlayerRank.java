package pl.dreammc.dreammcapi.api.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
@Getter
public enum PlayerRank {

    PLAYER(Component.empty(), "#aaaaaa", "#aaaaaa"),
    VIP(Component.text("VIP").style(Style.style(TextColor.fromHexString("#55ff55"), TextDecoration.BOLD)), "#55ff55", "#9cff9c"),
    VIP_PLUS(Component.text("VIP").style(Style.style(TextColor.fromHexString("#55ff55"), TextDecoration.BOLD)).append(Component.text("+").color(TextColor.fromHexString("#ffaa00")).decoration(TextDecoration.BOLD, false)), "#55ff55", "#9cff9c"),
    MVP(Component.text("MVP").style(Style.style(TextColor.fromHexString("#63ffff"), TextDecoration.BOLD)), "#63ffff", "#b0ffff"),
    MVP_PLUS(Component.text("MVP").style(Style.style(TextColor.fromHexString("#63ffff"), TextDecoration.BOLD)).append(Component.text("+").color(TextColor.fromHexString("#ff413e")).decoration(TextDecoration.BOLD, false)), "#63ffff", "#b0ffff"),
    MVP_PLUS_PLUS(Component.text("MVP").style(Style.style(TextColor.fromHexString("#ffaa00"), TextDecoration.BOLD)).append(Component.text("++").color(TextColor.fromHexString("#ff413e")).decoration(TextDecoration.BOLD, false)), "#ffaa00", "#ffd69c"),
    HELPER(Component.text("HELPER").style(Style.style(TextColor.fromHexString("#6385ff"), TextDecoration.BOLD)), "#6385ff", "#859fff"), // USELESS RANK
    MOD(Component.text("MOD").style(Style.style(TextColor.fromHexString("#ff413e"), TextDecoration.BOLD)), "#ff413e", "#ff7a78"),
    ADMIN(Component.text("ADMIN").style(Style.style(TextColor.fromHexString("#ff413e"), TextDecoration.BOLD)), "#ff413e", "#ffb14a");

    @Nullable final Component prefix;
    final String nicknameColor;
    final String messageName;

}
