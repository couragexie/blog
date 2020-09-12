package com.jay.blog.cache;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.concurrent.TimeUnit;


@Component
public class RedisHandler {
    Logger logger = LoggerFactory.getLogger(RedisHandler.class);

    @Autowired
    StringRedisTemplate stringRedisTemplate;



    @PostConstruct
    StringRedisTemplate init() {
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
        stringRedisTemplate.setDefaultSerializer(serializer);
        return stringRedisTemplate;
    }


    public <T> void saveCache(String key, T t){
        String value = JSON.toJSONString(t);
        stringRedisTemplate.opsForValue().set(key, value);
        logger.info("====== 存入缓存 key:{} ======", key);
    }

    public <T> void saveCache(String key, T t, Long expire, TimeUnit unit){
        String value = JSON.toJSONString(t);
        stringRedisTemplate.opsForValue().set(key,value, expire,unit);
        logger.info("====== 存入缓冲 key:{}, expire:{}", key, expire);
    }

    public <T> void removeCache(String key){
        if (!key.contains("*")) {
            stringRedisTemplate.delete(key);
            logger.info("====== 删除缓存 key:{}", key);
        }else {
            removeAllKeysByCacheName(key);
        }
    }

    public String getCache(String key){
        // redisTemplate 获取到的 Object 的类型是 LinkedHashMap
        String cache =  stringRedisTemplate.opsForValue().get(key);
        logger.info("====== 从缓存中查找数据 key:{}", key);
        return cache;
    }
    public void removeAllKeysByCacheName(String key){
        Set<String> keys = stringRedisTemplate.keys(key);
        stringRedisTemplate.delete(keys);
        logger.info("====== 批量缓存 keys:{}", keys);
    }
}
