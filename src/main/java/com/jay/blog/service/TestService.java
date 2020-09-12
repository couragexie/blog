package com.jay.blog.service;

import com.jay.blog.cache.RedisCache;
import com.jay.blog.cache.RedisCacheRemove;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    @RedisCache(expire = 240, key = "#id" )
    public  void hello(String id){
        System.out.println("hello");
    }

    //@RedisCacheRemove(cacheName = "blog", key = "")
    @RedisCacheRemove(cacheName = "blog", key = "#id")
    public void remove(long id){
        System.out.println("hello");
    }

}
