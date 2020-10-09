package com.jay.blog.service.Imp;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jay.blog.dao.BlogContentDao;
import com.jay.blog.entity.BlogContent;
import com.jay.blog.utils.PageUtil;
import com.jay.blog.vo.BlogVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BlogServiceImpTest {


    @Autowired
    BlogServiceImp blogServiceImp;
    @Autowired
    BlogContentDao blogContentDao;

    @Test
    public void test(){
        //BlogVO blogVO = blogServiceImp.getBlogVObyIdToView(52l);
        //System.out.println(blogVO);
       // blogServiceImp.searchListBlog("java", 1);

        //Page<BlogContent> page = PageUtil.generatePage(1);
        //blogContentDao.listBlogContentByQuery("java", page).getRecords().forEach(System.out::println);


    }
}