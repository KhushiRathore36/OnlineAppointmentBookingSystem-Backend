package com.appointment.auth_service.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenBlacklistService {

    private final RedisTemplate<String, String> redisTemplate;

    public TokenBlacklistService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void blacklistToken(String token, long expirationTimeInMillis) {
        redisTemplate.opsForValue().set(
                "BLACKLIST_" + token,
                "blacklisted",
                expirationTimeInMillis,
                TimeUnit.MILLISECONDS
        );
    }

    public boolean isTokenBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("BLACKLIST_" + token));
    }
}
