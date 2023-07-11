package com.xqxy.dr.modular.notice.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.notice.param.NoticeFileParam;
import com.xqxy.dr.modular.notice.service.NoticeFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 公示附件 前端控制器
 * </p>
 *
 * @author xiao jun
 * @since 2021-04-27
 */
@Api(tags = "公告文件管理")
@RestController
@RequestMapping("/notice/notice-file")
public class NoticeFileController {

    @Resource
    private NoticeFileService noticeFileService;

    /**
     * 用户档案列表
     *
     * @author xiao jun
     * @date 2021-03-11 15:49
     */
    @ApiOperation(value = "用户档案列表", notes = "用户档案列表", produces = "application/json")
    @PostMapping("/list")
    public ResponseData list(@RequestBody NoticeFileParam noticeFileParam) {
        return ResponseData.success(noticeFileService.list(noticeFileParam));
    }

    /**
     * 公示附件列表
     *
     * @param noticeId 查询参数
     * @return 公示附件列表
     * @author xiao jun
     * @date 2021-03-11 15:49
     */
    @ApiOperation(value = "公示附件查询", notes = "公示附件查询", produces = "application/json")
    @PostMapping("/listByNoticeId")
    public ResponseData listByNoticeId(@RequestParam("noticeId") Long noticeId) {
        return ResponseData.success(noticeFileService.listByNoticeId(noticeId));
    }

    /**
     * 公示附件删除
     *
     * @param noticeFileParam
     * @return
     * @author xiao jun
     * @date 2021-03-11 15:49
     */
    @ApiOperation(value = "公示附件删除", notes = "公示附件删除", produces = "application/json")
    @PostMapping("/delete")
    public ResponseData delete(@RequestBody NoticeFileParam noticeFileParam) {
        noticeFileService.delete(noticeFileParam.getId());
        return ResponseData.success();
    }
}

