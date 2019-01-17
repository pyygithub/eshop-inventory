package com.wolf.eshop.inventory.service.impl;


import com.wolf.eshop.inventory.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    @Override
    public String get(String key) {
        return  stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public void del(String key) {
        stringRedisTemplate.delete(key);
    }

    @Override
    public void expire(String key, int timeout) {
        stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }
}