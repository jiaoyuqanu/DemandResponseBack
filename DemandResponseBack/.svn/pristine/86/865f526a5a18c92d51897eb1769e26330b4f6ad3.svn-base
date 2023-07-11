package com.xqxy.dr.modular.workbench.controller;

import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.dr.modular.workbench.entity.Contracts;
import com.xqxy.dr.modular.workbench.entity.LoadCurve;
import com.xqxy.dr.modular.workbench.entity.ReserveSubsidy;
import com.xqxy.dr.modular.workbench.param.WorkbenchParam;
import com.xqxy.dr.modular.workbench.service.IWorkbenchService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 工作台前端控制器
 */
@RestController
@RequestMapping("/workbench")
public class WorkbenchController {

    @Resource
    private IWorkbenchService service;

    /**
     * 工作台容量补偿统计
     */
    @PostMapping("/provincial/reserveSubsidy")
    public ResponseData<ReserveSubsidy> reserveSubsidy(@RequestBody WorkbenchParam param) {
        // 供电单位标识 1.省级 2.地市
        String orgTitle = SecurityUtils.getCurrentUserInfoUTF8().getOrgTitle();
        String orgId = SecurityUtils.getCurrentUserInfoUTF8().getOrgId();
        String orgNo = param.getOrgNo();
        // 省公司登录
        if ("1".equals(orgTitle)) {
            if (Objects.equals(orgId, orgNo)) {
                param.setOrgNo(null);
            }
            // 切换地区
            return ResponseData.success(service.reserveSubsidyWorkbench(param));
            // 地市公司登录
        } else {
            return ResponseData.success(service.reserveSubsidyCity(param));
        }
    }


    /**
     * 签约资源分布
     *
     * @param param
     * @return
     */
    @PostMapping("/contracts")
    public ResponseData<Contracts> Contracts(@RequestBody WorkbenchParam param) {
        return ResponseData.success(service.contracts(param));
    }

    /**
     * 签约用户用电负荷曲线
     *
     * @param param
     * @return
     */
    @PostMapping("/loadCurve")
    public ResponseData<LoadCurve> LoadCurve(@RequestBody WorkbenchParam param) {
        String orgTitle = SecurityUtils.getCurrentUserInfoUTF8().getOrgTitle();
        String orgId = SecurityUtils.getCurrentUserInfoUTF8().getOrgId();
        String orgNo = param.getOrgNo();
        // 省公司登录
        if ("1".equals(orgTitle)) {
            if (Objects.equals(orgId, orgNo)) {
                param.setOrgNo(null);
            }
        }
        return ResponseData.success(service.loadCurve(param));
    }
}
