package org.sproject.sprojectapi.api.util;

import java.util.HashMap;
import java.util.Map;

public class TextUtil {

    private static final Map<Character, Character> charMap = new HashMap<>();

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

        for (char c : input.toCharArray()) {
            if (skipMode) {
                skipMode = false;
                result.append(c);
            } else if (c == '&') {
                skipMode = true;
                result.append(c);
            } else {
                result.append(charMap.getOrDefault(c, c));
            }
        }

        return result.toString();
    }

}