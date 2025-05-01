package pl.dreammc.dreammcapi.paper.command.language;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.api.command.response.CommandResponse;
import pl.dreammc.dreammcapi.api.command.response.CustomCommandResponse;
import pl.dreammc.dreammcapi.api.command.response.ICommandResponse;
import pl.dreammc.dreammcapi.api.language.LanguagePack;
import pl.dreammc.dreammcapi.api.manager.LanguageManager;
import pl.dreammc.dreammcapi.api.util.BaseColor;
import pl.dreammc.dreammcapi.paper.command.PaperCommand;
import pl.dreammc.dreammcapi.paper.command.PaperSubcommand;

import java.util.List;
import java.util.Map;

public class LangReloadCommand extends PaperCommand {

    public LangReloadCommand() {
        super("langreload",
                "Reloads language pack",
                "/langreload <plugin-name>",
                List.of("lr"),
                "pl.dreammc.api.lang",
                false,
                true);
    }

    @Override
    public @NotNull ICommandResponse execute0(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
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
