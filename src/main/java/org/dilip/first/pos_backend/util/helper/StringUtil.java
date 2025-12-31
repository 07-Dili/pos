package org.dilip.first.pos_backend.util.helper;

public class StringUtil {

    private StringUtil() {}

    public static String normalizeToLowerCase(String input) {
        return input == null ? null : input.trim().toLowerCase();
    }
}


