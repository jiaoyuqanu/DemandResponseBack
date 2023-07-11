package com.xqxy.dr.modular.powerplant.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.powerplant.param.DrDaElectricBidNoticeParam;
import com.xqxy.dr.modular.powerplant.service.DrDaElectricBidNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 电力市场竞价公告信息 前端控制器
 * </p>
 *
 * @author czj
 * @since 2021-12-09
 */
@Api(tags="电力市场竞价公告信息")
@RestController
@RequestMapping("/drDaElectricBidNotice")
public class DrDaElectricBidNoticeController {
    @Autowired
    private DrDaElectricBidNoticeService drDaElectricBidNoticeService;

    @ApiOperation(value = "电力市场竞价公告信息", notes = "电力市场竞价公告信息")
    @PostMapping("/page")
    public ResponseData page(@RequestBody DrDaElectricBidNoticeParam param) {
        return ResponseData.success(drDaElectricBidNoticeService.page(param));
    }


}

