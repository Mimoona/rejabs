package com.rejabsbackend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FallbackController {

    @GetMapping(value = {
            "/", "/signup","/signin","/home", "/profile", "/boards/**"
    })
    public String forward() {
        return "forward:/index.html";
    }
}