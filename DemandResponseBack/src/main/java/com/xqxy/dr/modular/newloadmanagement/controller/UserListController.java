package com.xqxy.dr.modular.newloadmanagement.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.core.client.SystemClientService;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.newloadmanagement.param.OrgIdParam;
import com.xqxy.dr.modular.newloadmanagement.param.UserListParam;
import com.xqxy.dr.modular.newloadmanagement.service.UserListService;
import com.xqxy.dr.modular.newloadmanagement.util.PagedFormat;
import com.xqxy.dr.modular.newloadmanagement.util.ResponseResult;
import com.xqxy.dr.modular.newloadmanagement.vo.EventLoadVo;
import com.xqxy.dr.modular.newloadmanagement.vo.UserListVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description 未到位用户清单controller && 需求响当前时段执行事件及实时执行情况controller
 * @Author Rabbit
 * @Date 2022/6/10 9:55
 */
@RestController
@RequestMapping("/sea-opu/remoteAppCenter")
@Api(tags = "未到位用户清单")
@Slf4j
public class UserListController {
    @Resource
    private UserListService userListService;
    @Resource
    private SystemClientService systemClient;

    /**
     * 查询未到位用户列表
     *
     * @param userListParam 参数
     * @return ResponseResult 响应结果
     * @author Rabbit
     */
    @ApiOperation(value = "未到位用户清单", notes = "未到位用户清单", produces = "application/json")
    @PostMapping("/unExcuteCons")
    public ResponseResult userLastExecInfo(@RequestBody UserListParam userListParam) {
        log.info("接收到负控参数：" + userListParam);
        String orgNo = userListParam.getOrgNo();
        ResponseData<List<String>> allNextOrgId = systemClient.getAllNextOrgId(orgNo);
        log.info("跟据负控传来的orgNo查询出来的本级以及下级" + allNextOrgId);
        List<String> data = allNextOrgId.getData();
        log.info("data里面的数据：" + data);

        Page<UserListVo> page = new Page<>(userListParam.getPage(), userListParam.getLimit());
        List<UserListVo> userListVos = userListService.userInfo(page, data);
        log.info("未到位用户清单数据1111111" + userListVos);
        PagedFormat pagedFormat = new PagedFormat(page.getTotal(), page.getCurrent(), page.getSize(), userListVos);
        log.info("未到位用户清单数据2222222" + pagedFormat);
        return new ResponseResult(200, "", pagedFormat, true);
    }

    /**
     * 获取需求响当前时段执行事件及实时执行情况
     *
     * @param orgIdParam 接参类 包含机构id 不传则返回省级加16个地市数据
     * @return ResponseResult 响应结果
     * @author Rabbit
     */
    @ApiOperation(value = "获取需求响当前时段执行事件及实时执行情况", notes = "获取需求响当前时段执行事件及实时执行情况", produces = "application/json")
    @PostMapping("/timeInterval")
    public ResponseData timeInterval(@RequestBody OrgIdParam orgIdParam) {
        List<EventLoadVo> list = userListService.timeIntervalList(orgIdParam.getOrgId());
        log.info("Controller中的获取需求响当前时段执行事件及实时执行情况数据" + list);
        return ResponseData.success(list);
    }
}
