package com.jay.blog.search;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jay.blog.service.SearchService;
import com.jay.blog.vo.BlogVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.io.IOException;

/**
 * @Author: xiejie
 * @Date: 2020/11/16 15:59
 */
@SpringBootTest
public class SearchServiceTest {
    @Autowired
    SearchService searchService;

    @Test
    public void fullSearch() throws IOException {
        Page<BlogVO> page = searchService.search(0, 10, "java");
        for (BlogVO blogVO : page.getRecords()){
            System.out.println(blogVO.getTitle());
        }
        System.out.println("total : " + page.getTotal());
    }

    @Test
    void createDoc() {
    }

    @Test
    void deleteDoc() {
    }

}
