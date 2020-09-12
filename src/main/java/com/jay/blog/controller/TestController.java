package com.jay.blog.controller;

import com.jay.blog.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
    @Autowired
    private TestService testService;

    @GetMapping("/test")
    public void test(){
        testService.hello("test");
    }

}
