package pl.dreammc.dreammcapi.api.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class FormatUtil {

    public static String formatWallet(double price) {
        return new DecimalFormat("#0.00").format(price).replace('.', ',') + " zł";
    }

    public static String formatTimeFromTicks(long totalTicks) {

        long seconds = totalTicks / 20;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        seconds = seconds % 60;
        minutes = minutes % 60;

        return String.format("%d godzin, %d minut, %d sekund", hours, minutes, seconds);
    }


    public static String formatFixedTimeFromTicks(long totalTicks) {

        long seconds = totalTicks / 20;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        seconds = seconds % 60;
        minutes = minutes % 60;
        hours = hours % 24;

        if (days > 0) {
            return String.format("%d dni, %d godzin, %d minut, %d sekund", days, hours, minutes, seconds);
        } else if (hours > 0) {
            return String.format("%d godzin, %d minut, %d sekund", hours, minutes, seconds);
        } else if (minutes > 0) {
            return String.format("%d minut, %d sekund", minutes, seconds);
        } else {
            return String.format("%d sekund", seconds);
        }
    }

    public static String formatTimeFromMillis(long millis) {
        return formatTimeFromTicks(millis / 50);
    }

    public static String formatFixedTimeFromMillis(long millis) {
        return formatFixedTimeFromTicks(millis / 50);
    }

    public static String formatTimeLeftFromMillis(long millis) {
        StringBuilder timeLeft = new StringBuilder();

        long cooldown = millis - System.currentTimeMillis();

        int hoursLeft = (int) (cooldown / (1000 * 60 * 60));
        int minutesLeft = (int) ((cooldown / (1000 * 60)) % 60);
        int secondsLeft = (int) ((cooldown / 1000) % 60);


        if (hoursLeft > 0) {
            timeLeft.append(hoursLeft).append(" godzin");
            if (hoursLeft == 1) {
                timeLeft.append("ę ");
            } else if (2 <= (hoursLeft % 10) && (hoursLeft % 10) <= 4) {
                timeLeft.append("y ");
            } else {
                timeLeft.append(" ");
            }
        }

        if (minutesLeft > 0 || hoursLeft > 0) {
            timeLeft.append(minutesLeft).append(" minut");
            if (minutesLeft == 1) {
                timeLeft.append("ę ");
            } else if (2 <= (minutesLeft % 10) && (minutesLeft % 10) <= 4) {
                timeLeft.append("y ");
            } else {
                timeLeft.append(" ");
            }
        }

        if (secondsLeft > 0 || (hoursLeft == 0 && minutesLeft == 0)) {
            timeLeft.append(secondsLeft).append(" sekund");
            if (secondsLeft == 1) {
                timeLeft.append("ę ");
            } else if (2 <= (secondsLeft % 10) && (secondsLeft % 10) <= 4) {
                timeLeft.append("y ");
            } else {
                timeLeft.append(" ");
            }
        }

        return timeLeft.toString();
    }

    public static double roundToTwoDecimalPlaces(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        return bd.setScale(2, RoundingMode.FLOOR).doubleValue();
    }

}
