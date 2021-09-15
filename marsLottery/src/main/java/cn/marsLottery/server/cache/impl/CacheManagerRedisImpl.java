package cn.marsLottery.server.cache.impl;


import cn.jzcscw.commons.cache.CacheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CacheManagerRedisImpl implements CacheManager {

    private static final int DEFAULT_EXPIRE_SECONDS = 60 * 60 * 24 * 7; // 默认过期时间，7天

    @Autowired
    private RedisTemplate<String, Object> coreRedisTemplate;

    @Override
    public void expire(String key, long seconds) {
        coreRedisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }

    @Override
    public boolean exists(String key) {
        return coreRedisTemplate.hasKey(key);
    }

    @Override
    public void del(String key) {
        coreRedisTemplate.delete(key);
    }

    @Override
    public Object get(String key) {
        return coreRedisTemplate.opsForValue().get(key);
    }

    @Override
    public void set(String key, Object value, int expireSeconds) {
        coreRedisTemplate.opsForValue().set(key, value, expireSeconds, TimeUnit.SECONDS);
    }

    @Override
    public void set(String key, Object value) {
        coreRedisTemplate.opsForValue().set(key, value);
    }

    @Override
    public Object hget(String key, String field) {
        return coreRedisTemplate.opsForHash().get(key, field);
    }

    @Override
    public void hset(String key, String field, Object value) {
        coreRedisTemplate.opsForHash().put(key, field, value);
    }

    @Override
    public void hdel(String key, String field) {
        coreRedisTemplate.opsForHash().delete(key, field);
    }

    @Override
    public boolean hexists(String key, String field) {
        return coreRedisTemplate.opsForHash().hasKey(key, field);
    }
}

