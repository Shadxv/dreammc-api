package pl.dreammc.dreammcapi.api.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentIteratorType;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TextUtil {

    private static final Map<Character, Character> charMap = new HashMap<>();
    private static final Map<Character, Integer> charWidthMap = new HashMap<>();
    private static final LegacyComponentSerializer legacyComponentSerializer = LegacyComponentSerializer.legacy('&');

    static {
        // Alternative Characters
        charMap.put('a', 'ᴀ');
        charMap.put('b', 'ʙ');
        charMap.put('c', 'ᴄ');
        charMap.put('d', 'ᴅ');
        charMap.put('e', 'ᴇ');
        charMap.put('f', 'ғ');
        charMap.put('g', 'ɢ');
        charMap.put('h', 'ʜ');
        charMap.put('i', 'ɪ');
        charMap.put('j', 'ᴊ');
        charMap.put('k', 'ᴋ');
        charMap.put('l', 'ʟ');
        charMap.put('m', 'ᴍ');
        charMap.put('n', 'ɴ');
        charMap.put('o', 'ᴏ');
        charMap.put('p', 'ᴘ');
        charMap.put('q', 'ǫ');
        charMap.put('r', 'ʀ');
        charMap.put('s', 'ѕ');
        charMap.put('t', 'ᴛ');
        charMap.put('u', 'ᴜ');
        charMap.put('v', 'ᴠ');
        charMap.put('w', 'ᴡ');
        charMap.put('x', 'х');
        charMap.put('y', 'ʏ');
        charMap.put('z', 'ᴢ');

        // Character widths
        charWidthMap.put(' ', 4);
        charWidthMap.put('!', 2);
        charWidthMap.put('"', 5);
        charWidthMap.put('#', 6);
        charWidthMap.put('$', 6);
        charWidthMap.put('%', 6);
        charWidthMap.put('&', 6);
        charWidthMap.put('\'', 3);
        charWidthMap.put('(', 5);
        charWidthMap.put(')', 5);
        charWidthMap.put('*', 5);
        charWidthMap.put('+', 6);
        charWidthMap.put(',', 2);
        charWidthMap.put('-', 6);
        charWidthMap.put('.', 2);
        charWidthMap.put('/', 6);
        charWidthMap.put('0', 6);
        charWidthMap.put('1', 6);
        charWidthMap.put('2', 6);
        charWidthMap.put('3', 6);
        charWidthMap.put('4', 6);
        charWidthMap.put('5', 6);
        charWidthMap.put('6', 6);
        charWidthMap.put('7', 6);
        charWidthMap.put('8', 6);
        charWidthMap.put('9', 6);
        charWidthMap.put(':', 2);
        charWidthMap.put(';', 2);
        charWidthMap.put('<', 5);
        charWidthMap.put('=', 6);
        charWidthMap.put('>', 5);
        charWidthMap.put('?', 6);
        charWidthMap.put('@', 7);
        charWidthMap.put('A', 6);
        charWidthMap.put('B', 6);
        charWidthMap.put('C', 6);
        charWidthMap.put('D', 6);
        charWidthMap.put('E', 6);
        charWidthMap.put('F', 6);
        charWidthMap.put('G', 6);
        charWidthMap.put('H', 6);
        charWidthMap.put('I', 4);
        charWidthMap.put('J', 6);
        charWidthMap.put('K', 6);
        charWidthMap.put('L', 6);
        charWidthMap.put('M', 6);
        charWidthMap.put('N', 6);
        charWidthMap.put('O', 6);
        charWidthMap.put('P', 6);
        charWidthMap.put('Q', 6);
        charWidthMap.put('R', 6);
        charWidthMap.put('S', 6);
        charWidthMap.put('T', 6);
        charWidthMap.put('U', 6);
        charWidthMap.put('V', 6);
        charWidthMap.put('W', 6);
        charWidthMap.put('X', 6);
        charWidthMap.put('Y', 6);
        charWidthMap.put('Z', 6);
        charWidthMap.put('[', 4);
        charWidthMap.put('\\', 6);
        charWidthMap.put(']', 4);
        charWidthMap.put('^', 6);
        charWidthMap.put('_', 6);
        charWidthMap.put('a', 6);
        charWidthMap.put('b', 6);
        charWidthMap.put('c', 6);
        charWidthMap.put('d', 6);
        charWidthMap.put('e', 6);
        charWidthMap.put('f', 5);
        charWidthMap.put('g', 6);
        charWidthMap.put('h', 6);
        charWidthMap.put('i', 2);
        charWidthMap.put('j', 6);
        charWidthMap.put('k', 5);
        charWidthMap.put('l', 3);
        charWidthMap.put('m', 6);
        charWidthMap.put('n', 6);
        charWidthMap.put('o', 6);
        charWidthMap.put('p', 6);
        charWidthMap.put('q', 6);
        charWidthMap.put('r', 6);
        charWidthMap.put('s', 6);
        charWidthMap.put('t', 4);
        charWidthMap.put('u', 6);
        charWidthMap.put('v', 6);
        charWidthMap.put('w', 6);
        charWidthMap.put('x', 6);
        charWidthMap.put('y', 6);
        charWidthMap.put('z', 6);
        charWidthMap.put('{', 5);
        charWidthMap.put('|', 2);
        charWidthMap.put('}', 5);
        charWidthMap.put('~', 7);
        charWidthMap.put('⌂', 6);
        charWidthMap.put('Ç', 6);
        charWidthMap.put('ü', 6);
        charWidthMap.put('é', 6);
        charWidthMap.put('â', 6);
        charWidthMap.put('ä', 6);
        charWidthMap.put('à', 6);
        charWidthMap.put('å', 6);
        charWidthMap.put('ç', 6);
        charWidthMap.put('ê', 6);
        charWidthMap.put('ë', 6);
        charWidthMap.put('è', 6);
        charWidthMap.put('ï', 4);
        charWidthMap.put('î', 6);
        charWidthMap.put('ì', 3);
        charWidthMap.put('Ä', 6);
        charWidthMap.put('Å', 6);
        charWidthMap.put('É', 6);
        charWidthMap.put('æ', 6);
        charWidthMap.put('Æ', 6);
        charWidthMap.put('ô', 6);
        charWidthMap.put('ö', 6);
        charWidthMap.put('ò', 6);
        charWidthMap.put('û', 6);
        charWidthMap.put('ù', 6);
        charWidthMap.put('ÿ', 6);
        charWidthMap.put('Ö', 6);
        charWidthMap.put('Ü', 6);
        charWidthMap.put('ø', 6);
        charWidthMap.put('£', 6);
        charWidthMap.put('Ø', 6);
        charWidthMap.put('×', 4);
        charWidthMap.put('ƒ', 6);
        charWidthMap.put('á', 6);
        charWidthMap.put('í', 3);
        charWidthMap.put('ó', 6);
        charWidthMap.put('ú', 6);
        charWidthMap.put('ñ', 6);
        charWidthMap.put('Ñ', 6);
        charWidthMap.put('ª', 6);
        charWidthMap.put('º', 6);
        charWidthMap.put('¿', 6);
        charWidthMap.put('®', 7);
        charWidthMap.put('¬', 6);
        charWidthMap.put('½', 6);
        charWidthMap.put('¼', 6);
        charWidthMap.put('¡', 2);
        charWidthMap.put('«', 6);
        charWidthMap.put('»', 6);
        charWidthMap.put('★', 14);
        charWidthMap.put('꩜', 10);
        charWidthMap.put('ᴀ', 6);
        charWidthMap.put('ʙ', 6);
        charWidthMap.put('ᴄ', 6);
        charWidthMap.put('ᴅ', 6);
        charWidthMap.put('ᴇ', 6);
        charWidthMap.put('ғ', 5);
        charWidthMap.put('ɢ', 6);
        charWidthMap.put('ʜ', 6);
        charWidthMap.put('ɪ', 2);
        charWidthMap.put('ᴊ', 6);
        charWidthMap.put('ᴋ', 5);
        charWidthMap.put('ʟ', 3);
        charWidthMap.put('ᴍ', 6);
        charWidthMap.put('ɴ', 6);
        charWidthMap.put('ᴏ', 6);
        charWidthMap.put('ᴘ', 6);
        charWidthMap.put('ǫ', 6);
        charWidthMap.put('ʀ', 6);
        charWidthMap.put('ѕ', 6);
        charWidthMap.put('ᴛ', 4);
        charWidthMap.put('ᴜ', 6);
        charWidthMap.put('ᴠ', 6);
        charWidthMap.put('ᴡ', 6);
        charWidthMap.put('х', 6);
        charWidthMap.put('ʏ', 6);
        charWidthMap.put('ᴢ', 6);
    }

    public static int getCharWidth(char c) {
        return charWidthMap.get(c);
    }

    public static int getBoldCharWidth(char c) {
        return charWidthMap.get(c) + 1;
    }

    public static String stylizeText(String text) {
        StringBuilder result = new StringBuilder();
        String input = text.toLowerCase();
        boolean skipMode = false;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (skipMode) {
                skipMode = false;
                result.append(c);
            } else if (c == '&') {
                if (i + 7 <= input.length() && input.charAt(i + 1) == '#') {
                    String hexColor = input.substring(i + 1, i + 8);
                    result.append('&').append(hexColor);
                    i += 7;
                } else {
                    result.append(c);
                    skipMode = true;
                }
            } else {
                result.append(charMap.getOrDefault(c, c));
            }
        }
        return result.toString();
    }

    public static int countLines(Component textComponent, int maxWidth) {
        int lines = 1;
        Iterator<Component> components = textComponent.iterable(ComponentIteratorType.BREADTH_FIRST).iterator();
        while (components.hasNext()) {
            int width = 0;
            TextComponent component = (TextComponent) components.next();
            boolean isBold = component.hasDecoration(TextDecoration.BOLD);
            for(String word : PlainTextComponentSerializer.plainText().serialize(component).replace(" ", "\u0007 ").split("\u0007")) {
                int wordWidth = 0;
                for(char c : word.toCharArray()) {
                    if(c == '\n') {
                        lines++;
                        width = 0;
                        wordWidth = 0;
                        continue;
                    }
                    if(charWidthMap.containsKey(c)) {
                        if (isBold) wordWidth += getBoldCharWidth(c);
                        else wordWidth += getCharWidth(c);
                    } else {
                        wordWidth += 4;
                    }
                }

                if(width + wordWidth > maxWidth) {
                    lines++;
                    width = wordWidth - getCharWidth(' ');
                } else {
                    width += wordWidth;
                    wordWidth = 0;
                }
            }
        }
        return lines;
    }

    public static Component deserializeText(String text) {
        return legacyComponentSerializer.deserialize(text);
    }

    public static String serializeComponent(Component textComponent) {
        return legacyComponentSerializer.serialize(textComponent);
    }
}