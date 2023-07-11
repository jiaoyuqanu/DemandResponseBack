package com.xqxy.dr.modular.event.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.core.pojo.page.PageResult;
import com.xqxy.dr.modular.evaluation.param.EventPowerExecuteImmediateParam;
import com.xqxy.dr.modular.event.entity.EventExecute;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.event.param.EventExecuteParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 电力用户执行信息 服务类
 * </p>
 *
 * @author Shen
 * @since 2021-10-27
 */
public interface EventExecuteService extends IService<EventExecute> {

    /**
     * @description:
     * @param: 
     * @return: 
     * @author: PengChuqing
     * @date: 2021/10/27 17:07
     */
    Page<EventExecute> eventMonitoring( EventExecuteParam eventExecuteParam);

    /**
     * @description: 用户执行率列表
     * @param: 
     * @return: 
     * @author: PengChuqing
     * @date: 2021/10/27 17:05
     */
    List<EventExecute> list (EventExecuteParam eventExecuteParam);

    void downloadExecteReport(EventExecuteParam eventExecuteParam, HttpServletResponse response, HttpServletRequest request);

}
