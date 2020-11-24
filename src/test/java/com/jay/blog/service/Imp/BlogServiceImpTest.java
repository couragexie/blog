package com.jay.blog.service.Imp;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jay.blog.vo.BlogVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BlogServiceImpTest {

    @Autowired
    BlogServiceImp blogServiceImp;

    @Test
    public void test(){
        //BlogVO blogVO = blogServiceImp.getBlogVObyIdToView(52l);
        //System.out.println(blogVO);
       // blogServiceImp.searchListBlog("java", 1);

        //Page<BlogContent> page = PageUtil.generatePage(1);
        //blogContentDao.listBlogContentByQuery("java", page).getRecords().forEach(System.out::println);

    }
    @Test
    public void searchList(){
        String query = "java";
        int pageNo = 1;
        Page<BlogVO> blogVOPage = blogServiceImp.searchListBlog(query, pageNo);
        for (BlogVO blogVO : blogVOPage.getRecords()){
            System.out.println(blogVO.getTitle());
        }
    }

}