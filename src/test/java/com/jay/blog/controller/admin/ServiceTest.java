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
    public void test02() {
    }
}
