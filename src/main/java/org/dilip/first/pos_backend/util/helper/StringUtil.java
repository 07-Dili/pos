package org.dilip.first.pos_backend.util.helper;

public class StringUtil {

    public static String normalize(String input) {
        return input == null ? null : input.trim().toLowerCase();
    }
}

