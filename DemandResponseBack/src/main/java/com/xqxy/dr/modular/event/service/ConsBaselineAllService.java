package com.xqxy.dr.modular.event.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.event.VO.ConsBaseLineExcelVo;
import com.xqxy.dr.modular.event.entity.ConsBaseline;
import com.xqxy.dr.modular.event.entity.ConsBaselineAll;
import com.xqxy.dr.modular.event.param.ConsBaselineParam;
import com.xqxy.dr.modular.event.param.PlanConsParam;

import java.util.List;

/**
 * <p>
 * 基线96点全量计算 服务类
 * </p>
 *
 * @author liqirui
 * @since 2022-06-15
 */
public interface ConsBaselineAllService extends IService<ConsBaselineAll> {

    /**
     * 根据事件id 和 用户id  查询基线
     * @param eventId 事件id
     * @param consId 用户id
     * @return
     */
    List<ConsBaselineAll> queryBaseLineByEventAndCons(Long eventId,String consId);

    List<ConsBaseLineExcelVo> exportListToExcel(ConsBaselineParam consBaselineParam);
}
