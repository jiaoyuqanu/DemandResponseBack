package com.xqxy.dr.modular.bidding.controller;


import com.xqxy.core.annotion.BusinessLog;
import com.xqxy.core.enums.LogAnnotionOpTypeEnum;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.bidding.param.BiddingNoticeParam;
import com.xqxy.dr.modular.bidding.result.BiddingNoticeInfo;
import com.xqxy.dr.modular.bidding.service.BiddingNoticeService;
import com.xqxy.dr.modular.project.params.ExamineParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 竞价公告 前端控制器
 * </p>
 *
 * @author Shen
 * @since 2021-10-15
 */
@Api(tags = "竞价公告API接口")
@RestController
@RequestMapping("/bidding/bidding-notice")
public class BiddingNoticeController {

    @Resource
    BiddingNoticeService biddingNoticeService;

    /**
     * 查询竞价公告
     *
     * @author shen
     * @date 2021-10-15 15:49
     * @return
     */
    @BusinessLog(title = "竞价公告分页查询", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "竞价公告分页查询", notes = "竞价公告分页查询", produces = "application/json")
    @PostMapping("/page")
    public ResponseData page(@RequestBody BiddingNoticeParam biddingNoticeParam) {
        return ResponseData.success(biddingNoticeService.page(biddingNoticeParam));
    }

    /**
     * 竞价详情
     * @param biddingNoticeParam
     * @return
     *
     * @author shen
     * @date 2021-10-15 16:30
     */
    @BusinessLog(title = "竞价公告详情", opType = LogAnnotionOpTypeEnum.DETAIL)
    @ApiOperation(value = "竞价公告详情", notes = "竞价公告详情", produces = "application/json")
    @PostMapping("/detail")
    public ResponseData detail(@RequestBody BiddingNoticeParam biddingNoticeParam) {
        return ResponseData.success(biddingNoticeService.detail(biddingNoticeParam));
    }


    /**
     * 删除竞价信息
     * @param biddingNoticeParam
     * @return
     *
     * @author shen
     * @date 2021-10-18 10:46
     */
    @BusinessLog(title = "删除竞价信息", opType = LogAnnotionOpTypeEnum.DELETE)
    @ApiOperation(value = "删除竞价信息", notes = "删除竞价信息", produces = "application/json")
    @PostMapping("/delete")
    public ResponseData delete(@RequestBody BiddingNoticeParam biddingNoticeParam) {
        biddingNoticeService.delete(biddingNoticeParam.getNoticeId());
        return ResponseData.success();
    }

    /**
     * 新增竞价信息
     * @param biddingNoticeInfo
     * @return
     *
     * @author shen
     * @date 2021-10-18 14:09
     */
    @BusinessLog(title = "新增竞价信息", opType = LogAnnotionOpTypeEnum.ADD)
    @ApiOperation(value = "新增竞价信息", notes = "新增竞价信息", produces = "application/json")
    @PostMapping("/add")
    public ResponseData add(@RequestBody BiddingNoticeInfo biddingNoticeInfo) {
        biddingNoticeService.add(biddingNoticeInfo);
        return ResponseData.success();
    }

    /**
     * 编辑竞价信息
     * @param biddingNoticeInfo
     * @return
     * @author shen
     * @date 2021-10-18 18:50
     */
    @BusinessLog(title = "编辑竞价信息", opType = LogAnnotionOpTypeEnum.EDIT)
    @ApiOperation(value = "编辑竞价信息", notes = "编辑竞价信息", produces = "application/json")
    @PostMapping("/edit")
    public ResponseData edit(@RequestBody BiddingNoticeInfo biddingNoticeInfo) {
        biddingNoticeService.edit(biddingNoticeInfo);
        return ResponseData.success();
    }

    /**
     * 编辑竞价状态
     * @param biddingNoticeParam
     * @return
     * @author shen
     * @date 2021-10-18 18:53
     */
    @BusinessLog(title = "编辑竞价状态", opType = LogAnnotionOpTypeEnum.EDIT)
    @ApiOperation(value = "编辑竞价状态", notes = "编辑竞价状态", produces = "application/json")
    @PostMapping("/editStatus")
    public ResponseData editStatus(@RequestBody BiddingNoticeParam biddingNoticeParam) {
        biddingNoticeService.editStatus(biddingNoticeParam);
        return ResponseData.success();
    }

    /**
     * 竞价提交审核
     * @param biddingNoticeParam
     * @return
     *
     * @author shen
     * @date 2021-10-18 16:49
     */
    @BusinessLog(title = "竞价提交审核", opType = LogAnnotionOpTypeEnum.DETAIL)
    @ApiOperation(value = "竞价提交审核", notes = "竞价提交审核", produces = "application/json")
    @PostMapping("/submitCheck")
    public ResponseData submitCheck(@RequestBody BiddingNoticeParam biddingNoticeParam) {
        biddingNoticeService.submitCheck(biddingNoticeParam);
        return ResponseData.success();
    }

    /**
     * 竞价审核
     * @param examineParam
     * @return
     *
     * @author shen
     * @date 2021-10-18 17:10
     */
    @BusinessLog(title = "竞价审核", opType = LogAnnotionOpTypeEnum.DETAIL)
    @ApiOperation(value = "竞价审核", notes = "竞价审核", produces = "application/json")
    @PostMapping("/examine")
    public ResponseData examine(@RequestBody @Validated(ExamineParam.add.class) ExamineParam examineParam) {
        biddingNoticeService.examine(examineParam);
        return ResponseData.success();
    }
}

