package com.jay.blog.controller.admin;

import com.alibaba.fastjson.JSON;
import com.jay.blog.cache.RedisCache;
import com.jay.blog.cache.RedisHandler;
import com.jay.blog.entity.Type;
import com.jay.blog.service.Imp.BlogServiceImp;
import com.jay.blog.service.Imp.CommentServiceImp;
import com.jay.blog.service.Imp.TagServiceImp;
import com.jay.blog.service.Imp.TypeServiceImp;
import com.jay.blog.service.TestService;
import org.apache.ibatis.javassist.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

/**
 * @program: blog
 * @description:
 * @author: Jay
 * @create: 2020-04-04 17:06
 **/
@SpringBootTest
public class ServiceTest {
    @Autowired
    BlogServiceImp blogService;
    @Autowired
    TypeServiceImp typeService;
    @Autowired
    TagServiceImp tagService;
    @Autowired
    CommentServiceImp commentService;

    @Autowired
    TestService testService;

    @Autowired
    RedisHandler redisHandler;

    @Test
    public void test02() throws NotFoundException {
        //blogService.getAndConvertById(2l);
        //redisHandler.removeCache();
       // testService.remove(123l);
        //blogService.getAndConvertById(2l);
        List<Type> types = typeService.listType();
        System.out.println(types.get(0).getClass());
        //String json = "[{\"id\":1,\"name\":\"数据结构\"},{\"id\":2,\"name\":\"Java\"},{\"id\":3,\"name\":\"操作系统\"},{\"id\":4,\"name\":\"数据库\"},{\"id\":5,\"name\":\"计算机网络\"},{\"id\":6,\"name\":\"Spring\"},{\"id\":7,\"name\":\"C++\"},{\"id\":8,\"name\":\"Linux\"},{\"id\":16,\"name\":\"剑指offer\"},{\"id\":17,\"name\":\"LeetCode\"},{\"id\":18,\"name\":\"JavaWeb\"}]";
        //JSON.parseArray(json,Type.class);

    }

    @RedisCache
    public void  hello(){
        System.out.println( "hello ");
    }

}
