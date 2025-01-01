package pl.dreammc.dreammcapi.paper.command.test;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.api.command.response.CommandResponse;
import pl.dreammc.dreammcapi.api.command.response.ICommandResponse;
import pl.dreammc.dreammcapi.api.model.ProfileModel;
import pl.dreammc.dreammcapi.paper.command.PaperCommand;
import pl.dreammc.dreammcapi.paper.command.PaperSubcommand;
import pl.dreammc.dreammcapi.paper.manager.PaperProfileManager;

import java.util.List;

public class ProfileTestCommand extends PaperCommand {

    public ProfileTestCommand() {
        super("profile", "", "", List.of("pv"), null, true, false);
    }

    @Override
    public @NotNull ICommandResponse execute0(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            return CommandResponse.ALLRIGHT;
        }

        ProfileModel model = PaperProfileManager.getInstance().getProfile(player.getUniqueId());
        sender.sendMessage(model == null ? "null" : model.getUuid().toString() + " " + model.getCurrentRank().name());
        return CommandResponse.ALLRIGHT;
    }
}
