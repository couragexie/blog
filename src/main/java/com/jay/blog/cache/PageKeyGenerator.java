package com.jay.blog.cache;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


public class PageKeyGenerator implements KeyGenerator {
    @Override
    public Object generate(Object o, Method method, Object... objects) {
        Page page = null;
        for ( Object object : objects){
            if (object instanceof Page){
                page = (Page) object;
            }
        }
        String key = method.toGenericString() + page.getCurrent() + page.getSize();
        return key;
    }
}
