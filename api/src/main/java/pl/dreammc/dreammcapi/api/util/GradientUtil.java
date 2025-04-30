package pl.dreammc.dreammcapi.api.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GradientUtil {

    public static List<String> generateColorGradient(int letterCount, List<String> hexColors) {
        if (letterCount <= 0) return new ArrayList<>();
        if (hexColors == null || hexColors.isEmpty()) {
            throw new IllegalArgumentException("Hex colors list cannot be empty");
        }

        List<RGBColor> colors = new ArrayList<>();
        for (String hex : hexColors) {
            colors.add(hexToRgb(hex));
        }

        return calculateGradient(letterCount, colors);
    }

    private static List<String> calculateGradient(int letterCount, List<RGBColor> colors) {
        List<String> result = new ArrayList<>();
        if (colors.size() == 1) {
            String color = rgbToHex(colors.get(0)).replace("#", "&#");
            return Collections.nCopies(letterCount, color);
        }

        List<Double> stops = generateColorStops(colors.size());

        for (int i = 0; i < letterCount; i++) {
            double position = (letterCount == 1) ? 0.0 : i / (double) (letterCount - 1);
            GradientSegment segment = findGradientSegment(position, stops);
            RGBColor interpolated = interpolate(colors.get(segment.startIndex), colors.get(segment.endIndex),
                    (position - segment.startStop) / (segment.endStop - segment.startStop));
            result.add(rgbToHex(interpolated).replace("#", "&#"));
        }

        return result;
    }

    private static List<Double> generateColorStops(int colorCount) {
        List<Double> stops = new ArrayList<>();
        for (int i = 0; i < colorCount; i++) {
            stops.add(i / (double) (colorCount - 1));
        }
        return stops;
    }

    private static GradientSegment findGradientSegment(double position, List<Double> stops) {
        for (int i = 0; i < stops.size() - 1; i++) {
            if (position >= stops.get(i) && position <= stops.get(i + 1)) {
                return new GradientSegment(i, i + 1, stops.get(i), stops.get(i + 1));
            }
        }
        return new GradientSegment(stops.size() - 2, stops.size() - 1,
                stops.get(stops.size() - 2), stops.get(stops.size() - 1));
    }

    private static RGBColor interpolate(RGBColor start, RGBColor end, double t) {
        int r = clamp(Math.round(start.r + (end.r - start.r) * t));
        int g = clamp(Math.round(start.g + (end.g - start.g) * t));
        int b = clamp(Math.round(start.b + (end.b - start.b) * t));
        return new RGBColor(r, g, b);
    }

    private static int clamp(double value) {
        return (int) Math.max(0, Math.min(255, value));
    }

    private static RGBColor hexToRgb(String hex) {
        String cleanHex = hex.replace("#", "");
        if (cleanHex.length() != 6) throw new IllegalArgumentException("Invalid HEX color format");

        return new RGBColor(
                Integer.parseInt(cleanHex.substring(0, 2), 16),
                Integer.parseInt(cleanHex.substring(2, 4), 16),
                Integer.parseInt(cleanHex.substring(4, 6), 16)
        );
    }

    private static String rgbToHex(RGBColor color) {
        return String.format("#%02x%02x%02x", color.r, color.g, color.b);
    }

    private static class RGBColor {
        final int r, g, b;

        RGBColor(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }

    private static class GradientSegment {
        final int startIndex, endIndex;
        final double startStop, endStop;

        GradientSegment(int start, int end, double startS, double endS) {
            this.startIndex = start;
            this.endIndex = end;
            this.startStop = startS;
            this.endStop = endS;
        }
    }

}
