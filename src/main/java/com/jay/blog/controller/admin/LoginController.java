package com.jay.blog.controller.admin;

import com.jay.blog.entity.User;
import com.jay.blog.service.Imp.UserServiceImp;
import com.jay.blog.utils.MD5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

/**
 * @program: blog
 * @description:
 * @author: Jay
 * @create: 2020-04-02 21:54
 **/
@Controller
@RequestMapping("/admin")
public class LoginController {
    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserServiceImp userService;

    @GetMapping
    public String logPage(){
        return "admin/login";
    }

    @GetMapping("/hello")
    public String hello(){
        return "/hello";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes,
                        HttpServletResponse response
    ){
        User user = userService.checkUser(username, MD5Utils.code(password));
        logger.info("获取到 User ：" + user);
        if(user != null){
            user.setPassword(null);
            session.setAttribute("user", user);
            return "admin/index";
        }else{
            attributes.addAttribute("message", "用户名和密码错误");
            return "redirect:/admin";
        }
    }


    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");

        return "redirect:/admin";
    }

}

