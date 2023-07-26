package com.restaurant.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RestController {
    @GetMapping(value = "/admin/rest/new")
    public String itemForm(){
        return "/rest/restForm";
    }
}
