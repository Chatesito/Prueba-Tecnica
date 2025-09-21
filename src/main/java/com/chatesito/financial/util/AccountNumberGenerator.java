package com.chatesito.financial.util;

import java.security.SecureRandom;

public final class AccountNumberGenerator {

    private static final SecureRandom RND = new SecureRandom();

    private AccountNumberGenerator() {}

    // 10 digit account number with a two digit prefix
    public static String generateWithPrefix(String prefix) {
        if (prefix == null || prefix.length() != 2 || !prefix.chars().allMatch(Character::isDigit)) {
            throw new IllegalArgumentException("El prefijo debe tener 2 dígitos numéricos");
        }
        StringBuilder sb = new StringBuilder(prefix);
        while (sb.length() < 10) {
            sb.append(RND.nextInt(10));
        }
        return sb.toString();
    }
}
