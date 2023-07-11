package com.xqxy.dr.modular.baseline.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.baseline.entity.PlanBaseLine;
import com.xqxy.dr.modular.baseline.entity.PlanBaseLineAll;

import java.util.List;

/**
 * <p>
 * 事件基线计算任务 服务类
 * </p>
 *
 * @author chen zhi jun
 * @since 2021-10-18
 */
public interface PlanBaseLineService extends IService<PlanBaseLine> {
    List<PlanBaseLine> getPlanBaseLineList();
    List<PlanBaseLine> getPlanBaseLineListAll();


}
