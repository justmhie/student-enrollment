package com.orangeandbronze;

public class ValidationUtils {
    public static boolean isAlphanumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        return str.matches("^[a-zA-Z0-9]+$");
    }
}