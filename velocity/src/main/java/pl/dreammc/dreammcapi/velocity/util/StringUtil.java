package pl.dreammc.dreammcapi.velocity.util;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

public class StringUtil {

    public static boolean startsWithIgnoreCase(@NotNull String string, @NotNull String prefix) throws IllegalArgumentException, NullPointerException {
        return string.length() >= prefix.length() && string.regionMatches(true, 0, prefix, 0, prefix.length());
    }

}
