package com.jay.blog.controller.admin;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jay.blog.converter.TagConverter;
import com.jay.blog.entity.Blog;
import com.jay.blog.entity.Comment;
import com.jay.blog.service.BlogService;
import com.jay.blog.service.Imp.BlogServiceImp;
import com.jay.blog.service.Imp.CommentServiceImp;
import com.jay.blog.service.Imp.TagServiceImp;
import com.jay.blog.service.Imp.TypeServiceImp;
import com.jay.blog.vo.BlogVO;
import com.jay.blog.vo.CommentVO;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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


    @Test
    public void test1(){
        //typeService.listType().forEach(System.out::println);
        //tagService.listTag().forEach(System.out::println);

       //  String tagIds = TagConverter.listTagToStringTagIds( tagService.listTag((long)2));
         //System.out.println(tagIds);

         //TagConverter.StringTagIdsToListTag(tagIds).forEach(System.out::println);
         //
//        Page<Blog> page = new Page<>();
//        List<OrderItem> orderItems = new ArrayList<>();
//        // 按照创建时间来排序，最新的在前面。
//        orderItems.add(new OrderItem().setColumn("update_time").setAsc(false));
//        page.setOrders(orderItems);
//        page.setCurrent(1);
//
//        blogService.listBlog(page).getRecords().forEach(System.out::println);

        //System.out.println(blogService.listRecommendBlogTop(3).size());
        //System.out.println(blogService.listRecommendBlogTop(3).size());
        //blogService.listRecommendBlogTop(3).forEach(System.out::println);
        Page<Blog> page = new Page<>();
        page.setSize(6);
        page.setCurrent(1);
        List<OrderItem> orderItems = new ArrayList<>();
        // 设置排序的列，降序
        orderItems.add(new OrderItem().setColumn("create_time").setAsc(false));
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd:hh-mm-ss");
//        Page<BlogVO> result = blogService.listBlog(page);
//
//        result.getRecords().stream().map(e->format.format(e.getCreateTime())).forEach(System.out::println);
//

        blogService.listBlogByTypeId((long) 4, page).getRecords().stream()
                .map(e->e.getTitle()).forEach(System.out::println);
    }

    @Test
    public void test02() {
        Map<String, List<Blog>> map = blogService.archiveBlogs();
        map.keySet().stream().flatMap(e -> map.get(e).stream().map(i -> i.getCreateTime()))
                .forEach(System.out::println);

        Lock lock = new ReentrantLock();
    }
}
