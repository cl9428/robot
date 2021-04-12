package com.cl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {
    @RequestMapping("/")
    public String Test(){
        return "test";
    }
    @RequestMapping("/user_list")
    public String user_list(){
        return "user_list";
    }

}
