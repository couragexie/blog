package com.jay.blog.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jay.blog.entity.Blog;
import com.jay.blog.entity.Type;
import com.jay.blog.entity.User;
import com.jay.blog.service.Imp.BlogServiceImp;
import com.jay.blog.service.Imp.TagServiceImp;
import com.jay.blog.service.Imp.TypeServiceImp;
import com.jay.blog.service.Imp.UserServiceImp;
import com.jay.blog.vo.BlogVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: blog
 * @description:
 * @author: Jay
 * @create: 2020-04-04 15:58
 **/
@Slf4j
@Controller
public class IndexController {
    @Autowired
    private BlogServiceImp blogService;
    @Autowired
    private TypeServiceImp typeService;
    @Autowired
    private TagServiceImp tagService;
    @Autowired
    private UserServiceImp userService;


    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "1") Integer pageNo, ModelMap model){
        Page<Blog> page = new Page<>();
        page.setSize(6);
        page.setCurrent(pageNo);
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

        log.info("【访问主页】pageTotal={}, pages={}",result.getTotal(), result.getPages());
        return "index";
    }

    private void setBlogVOTypeName(List<BlogVO> blogVOS){
        List<Type> types = typeService.listType();
        // 获取所有的分类信息
        for (BlogVO blogVO : blogVOS ) {
            for (Type type : types){
                if( blogVO.getType().getId() == type.getId() ){
                    blogVO.getType().setName(type.getName());
                    break;
                }
            }
        }
    }

    private void setUser(List<BlogVO> blogVOS){
        User user = userService.getOneById(blogVOS.get(0).getUser().getId());
        user.setPassword(null);
        for (BlogVO blogVO : blogVOS)
            blogVO.setUser(user);
    }


    @GetMapping("/footer/newblog")
    public String newblogs(ModelMap model) {
        //log.info("【访问 newblogs】");
        model.addAttribute("newblogs", blogService.listRecommendBlogTop(3));
        return "_fragments :: newblogList";
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, ModelMap model) throws NotFoundException {
        BlogVO blogVO = blogService.getAndConvertById(id);
        User user = userService.getOneById(blogVO.getUser().getId());
        user.setPassword(null);
        blogVO.setUser(user);
        blogVO.setTags(tagService.listTag(blogVO.getId()));
        model.addAttribute("blog", blogVO);
        return "blog";
    }

    @PostMapping("/search")
    public String search(@RequestParam(defaultValue = "1") Integer pageNO,
                         @RequestParam String query, Model model) {
        // 构建 page 查询
        Page<Blog> page = new Page<>();
        page.setCurrent(pageNO);
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(new OrderItem().setColumn("update_time").setAsc(false));
        // 获取到结果
        Page<BlogVO> resultPage = blogService.listBlog(query, page);
        // 设置 user；
        setUser(resultPage.getRecords());
        List<Type> types = typeService.listType();
        // 设置result 中 record 中所有 blogVO 的 type。
        setBlogVOTypeName(resultPage.getRecords());

        model.addAttribute("page", resultPage);
        model.addAttribute("query", query);
        return "search";
    }

}
