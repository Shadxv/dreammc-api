package org.sproject.sprojectapi.shared;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import org.sproject.sprojectapi.shared.logger.ILogger;
import org.sproject.sprojectapi.shared.player.IMessageSender;

public class Registry {

    @Nullable public static IMessageSender messageSender;
    @Nullable public static ILogger logger;

}
