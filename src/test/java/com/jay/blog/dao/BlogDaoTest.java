package com.jay.blog.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jay.blog.converter.TagConverter;
import com.jay.blog.entity.Blog;
import com.jay.blog.entity.BlogAndTag;
import com.jay.blog.service.BlogService;
import com.jay.blog.service.Imp.BlogServiceImp;
import com.jay.blog.vo.BlogVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@SpringBootTest
class BlogDaoTest {
    @Autowired
    BlogDao blogDao;

    @Autowired
    BlogServiceImp blogService;

    @Test
    public void test(){
        Page<Blog> page = new Page<>();
        page.setCurrent(1);
        page.setSize(50);
            blogDao.selectPageOrderByUpdateTime(page);
        page.getRecords().stream().forEach(BlogDaoTest::print);

        System.out.println();
        Page<Blog> page2 = new Page<>();
        page2.setCurrent(1);
        page2.setSize(10);
        blogDao.selectPageOrderByUpdateTime(page2);
        page2.getRecords().stream().forEach(BlogDaoTest::print);

        System.out.println();
        Page<Blog> page3 = new Page<>();
        page3.setCurrent(2);
        page3.setSize(10);
        blogDao.selectPageOrderByUpdateTime(page3);
        page3.getRecords().stream().forEach(BlogDaoTest::print);

    }
    public static void print(Blog  blog){
        System.out.println(blog.getUpdateTime() + "   " + blog.getTitle());
    }

    @Test
    public void test3(){
       // List<Long> blogIds = Arrays.asList(new Long[]{23l,32l,1l,5l});
        //blogDao.listBlogByBlogIds(blogIds).stream().forEach(System.out::println);

        List<BlogAndTag> blogAndTags = TagConverter.StringTagIdsToListTag("")
                .stream()
                .map(e-> new BlogAndTag(1l, e))
                .collect(Collectors.toList());

    }

}