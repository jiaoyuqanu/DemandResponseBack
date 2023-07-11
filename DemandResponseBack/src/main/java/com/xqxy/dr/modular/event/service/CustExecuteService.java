package com.xqxy.dr.modular.event.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.event.entity.*;
import com.xqxy.dr.modular.event.entity.CustExecute;
import com.xqxy.dr.modular.event.param.CustExecuteParam;
import com.xqxy.dr.modular.event.result.CustMonitorCurve;

import java.util.List;

/**
 * <p>
 * 电力用户执行信息 服务类
 * </p>
 *
 * @author Shen
 * @since 2021-10-27
 */
public interface CustExecuteService extends IService<CustExecute> {

    /**
     * @description:
     * @param: 
     * @return: 
     * @author: PengChuqing
     * @date: 2021/10/27 17:07
     */
    Page<CustExecute> eventMonitoring( CustExecuteParam CustExecuteParam);

    /**
     * @description: 用户执行率列表
     * @param: 
     * @return: 
     * @author: PengChuqing
     * @date: 2021/10/27 17:05
     */
    List<CustExecute> list (CustExecuteParam CustExecuteParam);

    CustBaselineAll getCustBaseByEventId (CustExecuteParam CustExecuteParam);

    CustMonitorCurve curveOfBaseAndTarget(CustExecuteParam CustExecuteParam);
}
