package pl.dreammc.dreammcapi.velocity.command.proxy;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.api.command.response.CommandResponse;
import pl.dreammc.dreammcapi.api.command.response.CustomCommandResponse;
import pl.dreammc.dreammcapi.api.command.response.ICommandResponse;
import pl.dreammc.dreammcapi.api.language.LanguagePack;
import pl.dreammc.dreammcapi.api.manager.LanguageManager;
import pl.dreammc.dreammcapi.api.util.BaseColor;
import pl.dreammc.dreammcapi.velocity.command.VelocityCommand;
import pl.dreammc.dreammcapi.velocity.command.VelocitySubcommand;

import java.util.List;
import java.util.Map;

public class LangReloadCommand extends VelocityCommand {
    public LangReloadCommand() {
        super("vlangreload",
                "Reloads language pack",
                "/vlangreload <plugin-name>",
                List.of("vlr"),
                "pl.dreammc.proxy.lang",
                false,
                true);
    }

    @Override
    public @NotNull ICommandResponse execute0(@NotNull CommandSource sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (args.length != 1) return CommandResponse.INVALID_ARGUMENTS;
        LanguagePack pack = LanguageManager.getInstance().getPack(args[0]);
        if (pack == null) return new CustomCommandResponse(Component.text("Pack not found!").color(TextColor.fromHexString(BaseColor.redPrimary)));
        pack.reload(LanguageManager.getInstance().getLanguage());
        return CommandResponse.ALLRIGHT;
    }

    @Override
    public @NotNull Map<Integer, List<String>> additionalCompletions(Player player) {
        return Map.of(0, LanguageManager.getInstance().getPlugins());
    }
}
