package com.xqxy.dr.modular.upload.controller;


import com.xqxy.dr.modular.subsidy.entity.ConsSubsidyPay;
import com.xqxy.dr.modular.upload.service.SubsidyPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 补贴发放记录 前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/subsidy")
public class SubsidyPayController {

    @Autowired
    private SubsidyPayService consSubsidyPayService;


    @RequestMapping("/getconsubsidy")
    public List<ConsSubsidyPay> getConSubsidy() {
        return consSubsidyPayService.getConSubsidy();
    }
}

