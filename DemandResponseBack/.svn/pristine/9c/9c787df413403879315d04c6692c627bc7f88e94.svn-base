package com.xqxy.dr.modular.event.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.event.entity.OrgExecute;
import com.xqxy.dr.modular.event.param.EventParam;
import com.xqxy.dr.modular.event.param.OrgExecuteParam;
import com.xqxy.dr.modular.event.service.OrgExecuteService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 组织机构执行信息 前端控制器
 * </p>
 *
 * @author liqirui
 * @since 2022-03-01
 */
@RestController
@RequestMapping("/orgExecute")
public class OrgExecuteController {

    @Autowired
    private OrgExecuteService orgExecuteService;

    /**
     * 执行监测 -- 组织机构监测
     *
     * @author lqr
     * @date 2022-03-01 8:49
     */
    @ApiOperation(value = "执行监测 -- 组织机构监测", notes = "执行监测 -- 组织机构监测", produces = "application/json")
    @PostMapping("/pageOrgExecute")
    public ResponseData pageOrgExecute(@RequestBody OrgExecuteParam orgExecuteParam) {
        return ResponseData.success(orgExecuteService.pageOrgExecute(orgExecuteParam));
    }


}

