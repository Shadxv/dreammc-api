package pl.dreammc.dreammcapi.paper.command.test.npc;

import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.dreammc.dreammcapi.api.command.response.CommandResponse;
import pl.dreammc.dreammcapi.api.command.response.CustomCommandResponse;
import pl.dreammc.dreammcapi.api.command.response.ICommandResponse;
import pl.dreammc.dreammcapi.paper.command.PaperSubcommand;
import pl.dreammc.dreammcapi.paper.command.test.NPCTestCommand;
import pl.dreammc.dreammcapi.paper.entity.ai.MovementComponent;

import java.util.List;

public class NPCDespawnCommand  extends PaperSubcommand {

    private final NPCTestCommand npcTestCommand;

    public NPCDespawnCommand(NPCTestCommand npcTestCommand) {
        super("despawn",
                "",
                "",
                List.of(),
                "",
                true,
                false);
        this.npcTestCommand = npcTestCommand;
    }

    @Override
    public @NotNull ICommandResponse execute0(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            return CommandResponse.ALLRIGHT;
        }

        if (!this.npcTestCommand.getNpcs().containsKey(player)) return new CustomCommandResponse(Component.text("You don't have any NPCs."));
        this.npcTestCommand.getNpcs().get(player).despawn();
        this.npcTestCommand.getNpcs().remove(player);

        return CommandResponse.ALLRIGHT;
    }
}
