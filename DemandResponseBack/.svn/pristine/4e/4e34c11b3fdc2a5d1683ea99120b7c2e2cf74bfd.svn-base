package com.xqxy.dr.modular.event.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.event.param.PlanCustParam;
import com.xqxy.dr.modular.event.service.PlanCustService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 方案参与客户 前端控制器
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-09
 */
@Api(tags = "方案参与客户API接口")
@RestController
@RequestMapping("/event/plan-cust")
public class PlanCustController {
    @Autowired
    private PlanCustService planCustService;
    /**
     * @description: 执行监测-客户监测-分页
     * @param:
     * @return:
     * @author: czj
     * @date: 2021/10/27 9:46
     */

    // @BusinessLog(title = "执行监测-客户监测-分页", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "执行监测-客户监测-分页", notes = "执行监测-客户监测-分页", produces = "application/json")
    @PostMapping("/pageCustMonitor")
    public ResponseData pageCustMonitor(@RequestBody PlanCustParam planCustParam) {
        return ResponseData.success(planCustService.pageCustMonitor(planCustParam));
    }

}

