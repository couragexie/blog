package com.jay.blog.cache;

import java.lang.annotation.*;

@Repeatable(RedisCachesRemove.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RedisCacheRemove {
    String cacheName() default "";

    String key() default "";

}
