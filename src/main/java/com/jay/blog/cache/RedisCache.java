package com.jay.blog.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisCache {

    long expire() default -1;

    String key() default "";

    String cacheNames() default "";

    TimeUnit unit() default TimeUnit.SECONDS;
}
