package com.joe.springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author maqiao
 * @create create on 2019-05-24 9:53
 * @desc
 **/
@RestController
@RequestMapping("user")
public class UserController {

    @GetMapping("test")
    public String test() {
        return "ok";
    }
}
