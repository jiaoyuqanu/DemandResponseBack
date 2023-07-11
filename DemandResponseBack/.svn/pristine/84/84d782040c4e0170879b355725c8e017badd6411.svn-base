package com.xqxy.dr.modular.upload.controller;


import com.xqxy.dr.modular.upload.entity.Drcons;
import com.xqxy.dr.modular.upload.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 需求响应用户 前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/users")
public class UserController {


    @Autowired
    private UserService userService;

    @RequestMapping("/getuser")
    public List<Drcons>getUser(){
        return userService.getUser();
    }


    @RequestMapping("/getability")
    public List<Drcons>getAbility(){
        return userService.getAbility();
    }
}
