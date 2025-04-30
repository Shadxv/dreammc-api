package pl.dreammc.dreammcapi.shared;

import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.shared.language.ILanguageManager;
import pl.dreammc.dreammcapi.shared.logger.ILogger;
import pl.dreammc.dreammcapi.shared.player.IMessageSender;
import pl.dreammc.dreammcapi.shared.service.BaseService;

public class Registry {

    @Nullable public static IMessageSender messageSender;
    @Nullable public static ILogger logger;
    @Nullable public static BaseService service;
    @Nullable public static ILanguageManager languageManager;

}
