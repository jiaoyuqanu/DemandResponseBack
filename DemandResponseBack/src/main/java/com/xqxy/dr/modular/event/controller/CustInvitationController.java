package com.xqxy.dr.modular.event.controller;


import com.xqxy.core.annotion.BusinessLog;
import com.xqxy.core.enums.LogAnnotionOpTypeEnum;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.event.param.ConsInvitationParam;
import com.xqxy.dr.modular.event.param.CustInvitationParam;
import com.xqxy.dr.modular.event.service.ConsInvitationService;
import com.xqxy.dr.modular.event.service.CustInvitationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 客户邀约 前端控制器
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-09
 */
@Api(tags = "客户邀约API接口")
@RestController
@RequestMapping("/event/cust-invitation")
public class CustInvitationController {

    @Resource
    private CustInvitationService custInvitationService;

    /**
     * @description: 客户邀约信息分页查询
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/22 14:25
     */
//    @BusinessLog(title = "客户邀约信息分页查询", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "客户邀约信息分页查询", notes = "客户邀约信息分页查询", produces = "application/json")
    @PostMapping("/page")
    public ResponseData page(@RequestBody CustInvitationParam custInvitationParam) {
        return ResponseData.success(custInvitationService.page(custInvitationParam));
    }






}

