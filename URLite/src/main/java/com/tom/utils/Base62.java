package com.tom.utils;

public class Base62 {
    private static final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int BASE = 62;
    public static final String BASE_URL = "http://localhost:8080/";

    public static String encode(int num) {
        StringBuilder sb = new StringBuilder();

        while (num > 0) {
            sb.append(CHARS.charAt(num % BASE));
            num /= BASE;
        }

        return sb.reverse().toString();
    }

    public static int decode(String str) {
        int num = 0;

        for (char c : str.toCharArray()) {
            num = num * BASE + CHARS.indexOf(c);
        }

        return num;
    }
}
