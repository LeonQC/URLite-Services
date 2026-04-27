package com.tom.utils;

import com.tom.pojo.Url;
import com.tom.service.redis.GlobalCounterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class Base62 {
    private final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final int BASE = 62;
    public final String BASE_URL = "http://localhost:8080/";

    private final GlobalCounterService globalCounterService;

    // for bulk upload
    public Url generate(String longUrl) {
        long id = globalCounterService.nextId("url:id");
        System.out.println("=====base62=====>: " + id);
        String shortCode = encode(id);
        String shortUrl = BASE_URL + shortCode;
        return new Url(id, longUrl, shortCode, shortUrl, null, 0, null, LocalDateTime.now());
    }

    // for singular | bulk upload
    public String encode(long num) {
        StringBuilder sb = new StringBuilder();

        while (num > 0) {
            sb.append(CHARS.charAt((int) (num % BASE)));
            num /= BASE;
        }

        return sb.reverse().toString();
    }

    public long decode(String str) {
        long num = 0L;

        for (char c : str.toCharArray()) {
            num = num * BASE + CHARS.indexOf(c);
        }

        return num;
    }
}
