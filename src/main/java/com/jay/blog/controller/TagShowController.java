package com.jay.blog.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jay.blog.dao.TagDao;
import com.jay.blog.entity.Blog;
import com.jay.blog.entity.Tag;
import com.jay.blog.entity.Type;
import com.jay.blog.entity.User;
import com.jay.blog.service.Imp.BlogServiceImp;
import com.jay.blog.service.Imp.TagServiceImp;
import com.jay.blog.service.Imp.TypeServiceImp;
import com.jay.blog.service.Imp.UserServiceImp;
import com.jay.blog.vo.BlogVO;
import com.jay.blog.vo.TagVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
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
 * @create: 2020-04-06 19:10
 **/
@Controller
public class TagShowController {
    @Autowired
    private TagServiceImp tagService;
    @Autowired
    private BlogServiceImp blogService;
    @Autowired
    private TypeServiceImp typeService;
    @Autowired
    private UserServiceImp userService;

    @GetMapping("/tags/{id}")
    public String tags(@RequestParam(defaultValue = "1") Integer pageNo,
            @PathVariable Long id, Model model) {
        Long size = new Long(6);
        Page<Blog> page = new Page<>(pageNo, size);
        page.setCurrent(pageNo);
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(new OrderItem().setColumn("create_time").setAsc(false));
        page.setOrders(orderItems);

        List<TagVO> tags = tagService.listTagTop(100);
        if (id == -1) {
            id = tags.get(0).getId();
        }


        Page<BlogVO> resultPage =  blogService.listBlogByTagId(id,page);
        // TODO 需要优化
        // 设置类型
        setBlogVOTypeName(resultPage.getRecords());
        // 设置用户
        setUser(resultPage.getRecords());
        // 设置tags
        setBlogTags(resultPage.getRecords());


        model.addAttribute("tags", tags);
        model.addAttribute("page", resultPage);
        model.addAttribute("activeTagId", id);
        return "tags";
    }

    private void setBlogTags(List<BlogVO> blogVOS){
       for(BlogVO blogVO : blogVOS)
           blogVO.setTags(tagService.listTag(blogVO.getId()));
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
