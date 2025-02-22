package com.banquito.marca.compartido.utilidades;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UtilidadHash {
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String hashString(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    public static boolean verificarString(String plainPassword, String hashedPassword) {
        return passwordEncoder.matches(plainPassword, hashedPassword);
    }
}
