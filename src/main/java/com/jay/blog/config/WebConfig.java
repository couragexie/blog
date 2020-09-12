package com.jay.blog.config;

import com.jay.blog.interceptor.LoginInterceptor;
import com.jay.blog.interceptor.VisitedInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @program: blog
 * @description: 注册
 * @author: Jay
 * @create: 2020-04-03 15:47
 **/
@Configuration
public class WebConfig implements WebMvcConfigurer {
    /**
     * @Author: jay
     * @Description: 拦截器
     * @Date 2020/5/8 23:26
     **/
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/admin/**") // 指定拦截 /admin 路径下的任意请求， /** 任意请求
                .excludePathPatterns("/admin") // 排出拦截请求，指定范文 /admin, or /admin/login
                .excludePathPatterns("/admin/login");
        registry.addInterceptor(new VisitedInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/admin/**")
                .excludePathPatterns("/css/**")  // 不拦截静态资源
                .excludePathPatterns("/images/**")
                .excludePathPatterns("/lib/**");
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}
