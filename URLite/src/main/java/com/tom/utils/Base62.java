package com.tom.utils;

import java.util.concurrent.atomic.AtomicLong;

public class Base62 {
    private static final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int BASE = 62;
    public static final String BASE_URL = "http://localhost:8080/";

    // for bulk upload
    private static final AtomicLong counter = new AtomicLong(0);

    // for bulk upload
    public static String generate(String longUrl) {
        long id = counter.incrementAndGet();
        System.out.println("=====base62=====>: " + id);
        return BASE_URL + encode(id);
    }

    // for bulk upload
    public static String encode(long num) {
        StringBuilder sb = new StringBuilder();

        while (num > 0) {
            sb.append(CHARS.charAt((int) (num % BASE)));
            num /= BASE;
        }

        return sb.reverse().toString();
    }

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
