package pl.dreammc.dreammcapi.velocity.command.proxy;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.api.command.response.CommandResponse;
import pl.dreammc.dreammcapi.api.command.response.ICommandResponse;
import pl.dreammc.dreammcapi.api.communication.packet.server.TransferPlayerPacket;
import pl.dreammc.dreammcapi.api.communication.packet.shared.TransferPlayerProfilePacket;
import pl.dreammc.dreammcapi.api.logger.Logger;
import pl.dreammc.dreammcapi.api.util.MessageSender;
import pl.dreammc.dreammcapi.shared.Registry;
import pl.dreammc.dreammcapi.velocity.VelocityDreamMCAPI;
import pl.dreammc.dreammcapi.velocity.command.VelocityCommand;
import pl.dreammc.dreammcapi.velocity.command.VelocitySubcommand;
import pl.dreammc.dreammcapi.velocity.event.TransferRequestEvent;
import pl.dreammc.dreammcapi.velocity.listener.TransferRequestListener;
import pl.dreammc.dreammcapi.velocity.manager.ConnectionManager;
import pl.dreammc.dreammcapi.velocity.manager.VelocityProfileManager;
import pl.dreammc.dreammcapi.velocity.model.TransferRequestModel;
import pl.dreammc.dreammcapi.velocity.type.PlayerTransferStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServerCommand extends VelocityCommand {
    public ServerCommand() {
        super("server",
                "Transfers sender to other server",
                "/server <name>",
                List.of(),
                "pl.dreammc.proxy.server",
                true,
                true);
    }

    @Override
    public @NotNull ICommandResponse execute0(@NotNull CommandSource sender, @NotNull String commandLabel, @NotNull String[] args) {
        if(args.length != 1) return CommandResponse.INVALID_ARGUMENTS;

        Player player = (Player) sender;

        VelocityDreamMCAPI.getInstance().getServer().getScheduler().buildTask(VelocityDreamMCAPI.getInstance(), scheduledTask -> {
            TransferRequestModel transferRequestModel = ConnectionManager.getInstance().transferPlayer(player, args[0]);
            if(transferRequestModel.getStatus() != PlayerTransferStatus.TARGET_FOUND) return;
            TransferRequestListener.handleTransferRequest(new TransferRequestEvent(player, transferRequestModel));
        }).schedule();

        return CommandResponse.ALLRIGHT;
    }

    @Override
    public @NotNull Map<Integer, List<String>> additionalCompletions(Player player) {
        List<String> availableServers = new ArrayList<>();
        for(RegisteredServer server : VelocityDreamMCAPI.getInstance().getServer().getAllServers()) {
            availableServers.add(server.getServerInfo().getName());
        }
        return Map.of(0, availableServers);
    }
}
