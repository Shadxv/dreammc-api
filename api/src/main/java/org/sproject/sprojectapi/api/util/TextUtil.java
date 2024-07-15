package org.sproject.sprojectapi.api.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.HashMap;
import java.util.Map;

public class TextUtil {

    private static final Map<Character, Character> charMap = new HashMap<>();
    private static final LegacyComponentSerializer legacyComponentSerializer = LegacyComponentSerializer.legacy('&');

    static {
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
    }

    public static String stylizeText(String input) {
        StringBuilder result = new StringBuilder();
        boolean skipMode = false;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (skipMode) {
                skipMode = false;
                result.append(c);
            } else if (c == '&') {
                if (i + 7 <= input.length() && input.charAt(i + 1) == '#') {
                    String hexColor = input.substring(i + 1, i + 7);
                    result.append('#').append(hexColor);
                    i += 7;
                } else {
                    result.append(c);
                }
            } else {
                result.append(charMap.getOrDefault(c, c));
            }
        }
        return result.toString();
    }

    public static Component deserializeText(String text) {
        return legacyComponentSerializer.deserialize(text);
    }

    public static String serializeComponent(Component textComponent) {
        return legacyComponentSerializer.serialize(textComponent);
    }
}