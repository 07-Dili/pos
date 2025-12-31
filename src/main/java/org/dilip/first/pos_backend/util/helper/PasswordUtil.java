package org.dilip.first.pos_backend.util.helper;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String hash(String password) {
        return encoder.encode(password);
    }

    public static boolean verify(String password, String password2) {
        return encoder.matches(password, password2);
    }
}

