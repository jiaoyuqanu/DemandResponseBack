package com.xqxy.sys.modular.cust.controller;

import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.sys.modular.cust.param.BlackNameParam;
import com.xqxy.sys.modular.cust.service.BlackNameService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统用户控制器
 *
 * @author czj
 * @date 2020/3/19 21:14
 */
@Api(tags = "黑名单管理")
@RestController
@RequestMapping("/dr/blackName")
public class BlackNameController {
    @Autowired
    BlackNameService blackNameService;


    /**
     * 黑名单分页查询
     * @param consParam
     * @return
     */
    @ApiOperation(value = "黑名单分页查询", notes = "黑名单分页查询", produces = "application/json")
    @PostMapping("/page")
    public ResponseData page(@RequestBody BlackNameParam consParam) {
        return ResponseData.success(blackNameService.page(consParam));
    }

    /**
     * 黑名单删除
     * @param consParam
     * @return
     */
    @ApiOperation(value = "黑名单删除", notes = "黑名单删除", produces = "application/json")
    @PostMapping("/deleteById")
    public ResponseData deleteById(@RequestBody BlackNameParam consParam) {
        blackNameService.deleteById(consParam);
        return ResponseData.success();
    }

}
