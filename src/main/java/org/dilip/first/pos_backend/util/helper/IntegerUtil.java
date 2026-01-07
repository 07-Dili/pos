package org.dilip.first.pos_backend.util.helper;

public class IntegerUtil {

    public static Long parseLong(String value) {
        if (value == null) return null;
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
