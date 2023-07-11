package com.xqxy.dr.modular.upload.controller;


import com.xqxy.dr.modular.upload.entity.Event;
import com.xqxy.dr.modular.upload.service.EvluateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 执行评价 前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/evluate")
public class EvluateController {

    @Autowired
    private EvluateService evluateService;



    @RequestMapping("/getevluate")
    public List<Event> getEvluate(){
        return evluateService.getEvluate();
    }
}
