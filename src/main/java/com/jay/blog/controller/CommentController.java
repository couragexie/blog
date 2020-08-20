package com.jay.blog.controller;

import com.jay.blog.entity.Comment;
import com.jay.blog.entity.User;
import com.jay.blog.service.Imp.CommentServiceImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

/**
 * @program: blog
 * @description:
 * @author: Jay
 * @create: 2020-04-07 14:19
 **/
@Slf4j
@Controller
public class CommentController {

    @Autowired
    private CommentServiceImp commentService;

    // 从配置文件 .yaml 中取值
    @Value("${comment.avatar}")
    private String avatar;

    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, ModelMap model){
        model.addAttribute("comments", commentService.listCommentByBlogId(blogId));
        return "blog::commentList";
    }

    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session){
        log.info("【post()】 comment={}",comment);
        Long blogId = comment.getBlogId();
        User user = (User)session.getAttribute("user");
        if(user != null)
            comment.setAvatar(user.getAvatar());
        else
            comment.setAvatar(avatar);

        commentService.saveComment(comment);

        return "redirect:/comments/" + blogId;
    }

}
