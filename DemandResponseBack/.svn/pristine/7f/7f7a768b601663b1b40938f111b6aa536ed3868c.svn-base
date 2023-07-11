package com.xqxy.dr.modular.dispatch.controller;

import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.dispatch.entity.OrgDemand;
import com.xqxy.dr.modular.dispatch.param.OrgDemandGetParam;
import com.xqxy.dr.modular.dispatch.param.OrgDemandParam;
import com.xqxy.dr.modular.dispatch.service.OrgDemandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("orgDemand/orgDemand")
@Api(tags = "调度指令分解")
public class OrgDemandController {

    @Resource
    private OrgDemandService orgDemandService;

    @ApiOperation(value = "根据指令id获取调度指令分解详情")
    @RequestMapping(method = RequestMethod.POST, value = "/getByRegulateId")
    public ResponseData<List<OrgDemand>> getRegulateId(@RequestBody OrgDemandGetParam orgDemandGetParam) {
        return ResponseData.success(orgDemandService.getByRegulateId(orgDemandGetParam.getRegulateId()));
    }

    @ApiOperation(value = "保存调度指令分解")
    @RequestMapping(method = RequestMethod.POST, value = "/saveOrgDemand")
    public ResponseData saveOrgDemand(@RequestBody OrgDemandParam orgDemandParam) {
        orgDemandService.saveOrgDemand(orgDemandParam);
        return ResponseData.success();
    }

}
