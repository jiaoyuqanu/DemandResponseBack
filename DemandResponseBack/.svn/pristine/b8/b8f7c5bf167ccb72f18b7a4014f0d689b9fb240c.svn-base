package com.xqxy.dr.modular.notice.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.notice.param.NoticeParam;
import com.xqxy.dr.modular.notice.service.NoticeService;
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
 * 公示表 前端控制器
 * </p>
 *
 * @author xiao jun
 * @since 2021-04-27
 */
@Api(tags = "公告管理")
@RestController
@RequestMapping("/notice/notice")
public class NoticeController {
    @Resource
    private NoticeService noticeService;

    /**
     * 查询公式
     *
     * @author shi
     * @date 2021-08-19 15:49
     */
    @ApiOperation(value = "公告分页查询", notes = "公告分页查询", produces = "application/json")
    @PostMapping("/page")
    public ResponseData page(@RequestBody NoticeParam noticeParam) {
        return ResponseData.success(noticeService.page(noticeParam));
    }

    /**
     * 添加公式
     *
     * @author xiao jun
     * @date 2020/3/25 14:44
     */
    @ApiOperation(value = "新增公告", notes = "新增公告", produces = "application/json")
    @PostMapping("/add")
    public ResponseData add(@RequestBody @Validated(NoticeParam.add.class) NoticeParam noticeParam) {
        noticeService.add(noticeParam);
        return ResponseData.success();
    }

    /**
     * 编辑公式
     *
     * @author xiao jun
     * @date 2020/3/25 14:54
     */
    @ApiOperation(value = "公式编辑", notes = "公式编辑", produces = "application/json")
    @PostMapping("/edit")
    public ResponseData edit(@RequestBody @Validated(NoticeParam.edit.class) NoticeParam noticeParam) {
        noticeService.edit(noticeParam);
        return ResponseData.success();
    }

    /**
     * 查看公式
     *
     * @author xiao jun
     * @date 2020/3/26 9:49
     */
    @ApiOperation(value = "公式查看", notes = "公式查看", produces = "application/json")
    @PostMapping("/detail")
    public ResponseData detail(@RequestBody @Validated(NoticeParam.detail.class) NoticeParam noticeParam) {
        return ResponseData.success(noticeService.detail(noticeParam));
    }
}

