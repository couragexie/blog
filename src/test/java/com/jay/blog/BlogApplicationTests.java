package com.jay.blog;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jay.blog.dao.*;
import com.jay.blog.entity.Blog;
import com.jay.blog.entity.Tag;
import com.jay.blog.service.Imp.BlogServiceImp;
import org.apache.ibatis.javassist.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SpringBootTest
class BlogApplicationTests {

    @Test
    void contextLoads() {
        Scanner scanner = new Scanner(System.in);

    }

    @Autowired
    BlogDao blogDao;

    @Autowired
    CommentDao commentDao;

    @Autowired
    TagDao tagDao;

    @Autowired
    TypeDao typeDao;

    @Autowired
    UserDao userDao;

    @Autowired
    BlogServiceImp blogServiceImp;

    @Test
    void test() throws NotFoundException {
       // tagDao.listTagIdTop(5).forEach(System.out::println);
        //typeDao.listTypeIdTop(5).forEach(System.out::println);
        //typeDao.listTypeIdTop(5).forEach(System.out::println);
//        Page<Blog> page = new Page<>();
//        page.setCurrent(1);
//        List<OrderItem> orderItems = new ArrayList<>();
//        orderItems.add(new OrderItem().setColumn("update_time").setAsc(false));
//        page.setOrders(orderItems);
//        System.out.println(blogDao.selectPageByTagId((long)8, page).getRecords().size());
//

       // blogServiceImp.getAndConvertById(53l);
    }
}
