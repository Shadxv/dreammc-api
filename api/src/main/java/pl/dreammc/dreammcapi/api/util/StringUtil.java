package pl.dreammc.dreammcapi.api.util;

import java.util.function.Function;

public class StringUtil {

    public static <T> String mapTwoDimentionalArrayToString(T[][] grid, Function<T, String> nameExtractor) {
        if (grid == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        boolean first = true;

        for (T[] row : grid) {
            if (row == null) continue;

            for (T element : row) {
                if (!first) {
                    sb.append(",");
                }

                if (element != null) {
                    String name = nameExtractor.apply(element);
                    if (name != null) {
                        sb.append(name);
                    }
                }

                first = false;
            }
        }

        return sb.toString();
    }
}
