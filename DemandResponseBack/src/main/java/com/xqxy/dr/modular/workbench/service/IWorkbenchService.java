package com.xqxy.dr.modular.workbench.service;

import com.xqxy.dr.modular.workbench.entity.Contracts;
import com.xqxy.dr.modular.workbench.entity.LoadCurve;
import com.xqxy.dr.modular.workbench.entity.ReserveSubsidy;
import com.xqxy.dr.modular.workbench.param.WorkbenchParam;

import java.util.List;
import java.util.Map;

public interface IWorkbenchService {

    /**
     * 工作台容量补偿（省级）
     */
    ReserveSubsidy reserveSubsidyWorkbench(WorkbenchParam param);

    /**
     * 工作台容量补偿(地市)
     *
     * @param param
     * @return
     */
    ReserveSubsidy reserveSubsidyCity(WorkbenchParam param);

    /**
     * 签约用户资源
     * @param param
     * @return
     */
    List<Contracts> contracts(WorkbenchParam param);

    /**
     * 用电负荷曲线
     * @param param
     * @return
     */
    LoadCurve loadCurve(WorkbenchParam param);
}
