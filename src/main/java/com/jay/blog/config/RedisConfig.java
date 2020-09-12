package com.jay.blog.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    /**
     *
     * 配置 RedisTemplate
     */
//    @Bean
//    public RedisTemplate<String, Object> genRedisTemplate(){
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        // 注入数据源
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        // 使用 Jackson2JsonRedisSerializer 替换掉原生的序列化器
//        //Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = jackson2JsonRedisSerializer();
//        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
//
//        // key-value 数据结构的序列化
//        redisTemplate.setKeySerializer(stringRedisSerializer);
//        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//
//        // hash 数据结构
//        redisTemplate.setHashKeySerializer(stringRedisSerializer);
//        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
//
//        // 启动默认序列化的方式，注入用 Jackson2JsonRedisSerializer 替换
//        redisTemplate.setEnableDefaultSerializer(true);
//        redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
//
//        return redisTemplate;
//    }

//    /**
//     * Jackson2JsonRedisSerializer 默认使用的 ObjectMapper 是使用 new ObjectMapper() 创建的
//     * 这样的 ObjectMapper 会将 redis 中的字符反序列化为 LinkedHashMap()
//     */
//    private Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer(){
//
//        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =
//
//                new Jackson2JsonRedisSerializer<>(Object.class);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//
//        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
//
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//
//        // 此项必须配置，否则会报java.lang.ClassCastException: java.util.LinkedHashMap cannot be cast to XXX
//
//        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
//
//        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//
//        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
//
//        return jackson2JsonRedisSerializer;
//    }

//    @Bean
//    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory){
//        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
//
//        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
//
//        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig();
//
//        RedisCacheConfiguration redisCacheConfiguration = defaultCacheConfig
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(stringRedisSerializer))
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(genericJackson2JsonRedisSerializer));
//        return RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(redisCacheConfiguration).build();
//    }

}
