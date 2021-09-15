package cn.jzcscw.commons.cache;

public interface CacheManager {
    void expire(String key, long seconds);

    boolean exists(String key);

    void del(String key);

    //普通键值对

    Object get(String key);

    void set(String key, Object value, int expireSeconds);

    void set(String key, Object value);

    //哈希

    Object hget(String key, String field);

    void hset(String key, String field, Object value);

    void hdel(String key, String field);

    boolean hexists(String key, String field);
}
