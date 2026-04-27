package com.tom.service.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GlobalCounterService {
    private final StringRedisTemplate stringRedisTemplate;

    public Long nextId(String key) {
        return stringRedisTemplate.opsForValue().increment(key);
    }
}
