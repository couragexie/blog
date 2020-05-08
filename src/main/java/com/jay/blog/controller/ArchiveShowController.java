package com.jay.blog.controller;

import com.jay.blog.service.BlogService;
import com.jay.blog.service.Imp.BlogServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @program: blog
 * @description: 归档查询
 * @author: Jay
 * @create: 2020-04-06 17:55
 **/
@Controller
public class ArchiveShowController {
    @Autowired
    private BlogServiceImp blogService;

    @GetMapping("/archives")
    public String archives(ModelMap model){
        model.addAttribute("archiveMap", blogService.archiveBlogs());
        model.addAttribute("blogCount", blogService.countBlog());

        return "archives";
    }
}
