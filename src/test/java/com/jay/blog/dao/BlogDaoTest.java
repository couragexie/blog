package com.jay.blog.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jay.blog.entity.Blog;
import com.jay.blog.service.BlogService;
import com.jay.blog.service.Imp.BlogServiceImp;
import com.jay.blog.vo.BlogVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class BlogDaoTest {
    @Autowired
    BlogDao blogDao;

    @Autowired
    BlogServiceImp blogService;

    @Test
    void listBlogByQuery() {
        Page<Blog> page = new Page<Blog>();
        page.setCurrent(1);
        page.setSize(100);

        Page<BlogVO> res = blogService.listBlog("linux", page);

        System.out.println(res.getTotal());
    }
}