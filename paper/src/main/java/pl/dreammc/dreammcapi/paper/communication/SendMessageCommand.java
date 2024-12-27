package pl.dreammc.dreammcapi.paper.communication;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.api.util.BaseColor;
import pl.dreammc.dreammcapi.api.util.MessageSender;
import pl.dreammc.dreammcapi.paper.PaperDreamMCAPI;
import pl.dreammc.dreammcapi.paper.command.PaperCommand;
import pl.dreammc.dreammcapi.paper.command.PaperSubcommand;
import pl.dreammc.dreammcapi.paper.command.response.CommandResponse;
import pl.dreammc.dreammcapi.paper.command.response.CustomCommandResponse;
import pl.dreammc.dreammcapi.paper.command.response.ICommandResponse;

import java.util.List;

public class SendMessageCommand extends PaperCommand {
    public SendMessageCommand() {
        super("sendmsg",
                "test command",
                "/sendmsg <channel> <message>",
                List.of("sm"),
                "*",
                false,
                false);
    }

    @Override
    public @NotNull ICommandResponse execute0(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {

        if(args.length < 2) return CommandResponse.INVALID_ARGUMENTS;

        String channel = args[0];
        StringBuilder message = new StringBuilder();

        for(int i = 1; i < args.length; i++) {
            message.append(args[i]).append(" ");
        }

        PaperDreamMCAPI.getInstance().getRedisConnector().publish(channel, new SendMessagePacket(sender.getName(), message.toString()));

        MessageSender.sendInfoMessage(sender, "Wiadomosc wyslana poprawnie");

        return CommandResponse.ALLRIGHT;
    }
}
