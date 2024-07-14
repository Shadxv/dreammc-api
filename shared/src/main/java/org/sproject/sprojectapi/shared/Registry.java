package org.sproject.sprojectapi.shared;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import org.sproject.sprojectapi.shared.player.IMessageSender;

import java.util.PrimitiveIterator;

public class Registry {

    @Nullable
    public static IMessageSender messageSender;

}
