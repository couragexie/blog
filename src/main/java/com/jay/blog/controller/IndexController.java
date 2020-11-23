package com.jay.blog.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jay.blog.entity.Blog;
import com.jay.blog.entity.Type;
import com.jay.blog.entity.User;
import com.jay.blog.service.Imp.*;
import com.jay.blog.utils.CollectionsUtil;
import com.jay.blog.utils.CommonUtils;
import com.jay.blog.utils.PageUtils;
import com.jay.blog.vo.BlogVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: blog
 * @description:
 * @author: Jay
 * @create: 2020-04-04 15:58
 **/
@Slf4j
@Controller
public class IndexController {
    private final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private BlogServiceImp blogService;
    @Autowired
    private TypeServiceImp typeService;
    @Autowired
    private TagServiceImp tagService;
    @Autowired
    private UserServiceImp userService;
    @Autowired
    private SearchServiceImp searchService;

    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "1") Integer pageNo, ModelMap model) {
        Page<Blog> page = PageUtils.generatePage(pageNo);
        List<OrderItem> orderItems = new ArrayList<>();
        // 设置排序的列，降序
        orderItems.add(new OrderItem().setColumn("create_time").setAsc(false));

        Page<BlogVO> result = blogService.listBlog(page);

        // TODO 优化
        // 设置user
        setUser(result.getRecords());
        // 设置博客的类型名称
        setBlogVOTypeName(result.getRecords());
        model.addAttribute("page", result);
        model.addAttribute("types", typeService.listTypeTop(6));
        model.addAttribute("tags", tagService.listTagTop(10));
        model.addAttribute("recommendBlogs", blogService.listRecommendBlogTop(8));

        log.info("【访问主页】pageTotal={}, pages={}", result.getTotal(), result.getPages());
        return "index";
    }

    private void setBlogVOTypeName(List<BlogVO> blogVOS) {
        if (CollectionsUtil.checkListIsNull(blogVOS)) {
            return;
        }
        List<Type> types = typeService.listType();
        // 获取所有的分类信息
        for (BlogVO blogVO : blogVOS) {
            for (Type type : types) {
                if (blogVO.getType().getId().equals(type.getId())) {
                    blogVO.getType().setName(type.getName());
                    break;
                }
            }
        }
    }

    // TODO：单用户情况下可以这样，多用户情况下就会出现问题
    private void setUser(List<BlogVO> blogVOS) {
        if (CollectionsUtil.checkListIsNull(blogVOS)) {
            return ;
        }
        User user = userService.getOneById(blogVOS.get(0).getUser().getId());
        user.setPassword(null);
        for (BlogVO blogVO : blogVOS) {
            blogVO.setUser(user);
        }
    }

    @GetMapping("/footer/newblog")
    public String newblogs(ModelMap model) {
        //log.info("【访问 newblogs】");
        model.addAttribute("newblogs", blogService.listRecommendBlogTop(3));
        return "_fragments :: newblogList";
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, ModelMap model) throws NotFoundException {
        BlogVO blogVO = blogService.getBlogVObyIdToView(id);
        User user = userService.getOneById(blogVO.getUser().getId());
        user.setPassword(null);
        blogVO.setUser(user);
        blogVO.setTags(tagService.listTag(blogVO.getId()));
        model.addAttribute("blog", blogVO);
        return "blog";
    }

/*
    @GetMapping("/search")
    public String search(@RequestParam(defaultValue = "1") Integer pageNo,
                         @RequestParam String query, Model model) {

        // 获取到结果
        Page<BlogVO> resultPage = blogService.searchListBlog(query, pageNo);

        if (resultPage != null) {
            // 设置 user；
            setUser(resultPage.getRecords());
            List<Type> types = typeService.listType();
            // 设置 result 中 record 中所有 blogVO 的 type。
            setBlogVOTypeName(resultPage.getRecords());
        }else{
            resultPage = PageUtil.generatePage(pageNo);
        }

        System.out.println("resultPage1" + resultPage);
        model.addAttribute("page", resultPage);
        model.addAttribute("query", query);

        return "search";
    }
*/
    @GetMapping("/search")
    public String search(@RequestParam(defaultValue = "1") Integer pageNo,
                         @RequestParam String query, Model model){
        Integer pageSize = CommonUtils.DEFAULT_PAGE_SIZE;
        Page<BlogVO> resultPage = null;
        try {
            throw new IOException();
//           resultPage = searchService.search(pageNo, pageSize, query);
        }catch (Exception e){
            logger.error("es 搜索失败...走补偿机制");
            resultPage = blogService.searchListBlog(query, pageNo);
        }

        if (resultPage != null) {
            // 设置 user；
            setUser(resultPage.getRecords());
            List<Type> types = typeService.listType();
            // 设置 result 中 record 中所有 blogVO 的 type。
            setBlogVOTypeName(resultPage.getRecords());
        }else{
            resultPage = PageUtils.generatePage(pageNo);
        }

        model.addAttribute("page", resultPage);
        model.addAttribute("query", query);

        return "search";
    }

}
