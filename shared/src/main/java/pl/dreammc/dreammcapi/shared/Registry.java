package pl.dreammc.dreammcapi.shared;

import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.shared.logger.ILogger;
import pl.dreammc.dreammcapi.shared.player.IMessageSender;

public class Registry {

    @Nullable public static IMessageSender messageSender;
    @Nullable public static ILogger logger;

}
