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
import com.jay.blog.vo.BlogQuery;
import com.jay.blog.vo.BlogVO;
import com.jay.blog.vo.TypeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: blog
 * @description:
 * @author: Jay
 * @create: 2020-04-06 19:11
 **/
@Slf4j
@Controller
public class TypeShowController {
    @Autowired
    private TagServiceImp tagService;
    @Autowired
    private BlogServiceImp blogService;
    @Autowired
    private TypeServiceImp typeService;
    @Autowired
    private UserServiceImp userService;

    @GetMapping("/types/{id}")
    public String types(@RequestParam(defaultValue = "1") Integer pageNo,
                        @PathVariable Long id, Model model) {
        //设置页面大小
        Long size = new Long(6);
        Page<Blog> page = new Page<>(pageNo,size);
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(new OrderItem().setColumn("create_time").setAsc(false));
        page.setOrders(orderItems);

        List<TypeVO> types = typeService.listTypeTop(100);
        if (id == -1) {
            id = types.get(0).getId();
        }
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setTypeId(id);

        Page<BlogVO> resultPage = blogService.listBlogByTypeId(id, page);
        // 设置博客的分类
        setBlogVOTypeName(resultPage.getRecords());
        // 设置所属id
        setUser(resultPage.getRecords());

        model.addAttribute("types", types);
        model.addAttribute("page", resultPage);
        model.addAttribute("activeTypeId", id);
        return "types";
    }

    private void setBlogVOTypeName(List<BlogVO> blogVOS){
        List<Type> types = typeService.listType();
        // TODO 改用数据库多表查询来获取结果
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
        for(BlogVO blogVO : blogVOS)
            blogVO.setUser(user);
    }

    

}
