package com.xqxy.dr.modular.bidding.controller;


import com.xqxy.core.annotion.BusinessLog;
import com.xqxy.core.enums.LogAnnotionOpTypeEnum;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.bidding.entity.ConsBiddingDeclare;
import com.xqxy.dr.modular.bidding.service.ConsBiddingDeclareService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户竞价申报记录 前端控制器
 * </p>
 *
 * @author Shen
 * @since 2021-10-15
 */
@Api(tags = "用户竞价API接口")
@RestController
@RequestMapping("/bidding/cons-bidding-declare")
public class ConsBiddingDeclareController {

    @Resource
    ConsBiddingDeclareService consBiddingDeclareService;

    /**
     * 批量添加用户竞价
     * @param consBiddingDeclareList
     * @return
     *
     * @author shen
     * @date 2021-10-20 15:40
     */
    @BusinessLog(title = "批量添加用户竞价", opType = LogAnnotionOpTypeEnum.ADD)
    @ApiOperation(value = "批量添加用户竞价", notes = "批量添加用户竞价", produces = "application/json")
    @PostMapping("/addBatch")
    public ResponseData addBatch(@RequestBody List<ConsBiddingDeclare> consBiddingDeclareList) {
        consBiddingDeclareService.addBatch(consBiddingDeclareList);
        return ResponseData.success();
    }

    /**
     * 批量修改用户竞价
     * @param consBiddingDeclareList
     * @return
     *
     * @author shen
     * @date 2021-10-20 15:43
     */
    @BusinessLog(title = "批量修改用户竞价", opType = LogAnnotionOpTypeEnum.EDIT)
    @ApiOperation(value = "批量修改用户竞价", notes = "批量修改用户竞价", produces = "application/json")
    @PostMapping("/editBatch")
    public ResponseData editBatch(@RequestBody List<ConsBiddingDeclare> consBiddingDeclareList) {
        consBiddingDeclareService.editBatch(consBiddingDeclareList);
        return ResponseData.success();
    }



}

