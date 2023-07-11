package com.xqxy.dr.modular.prediction.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.prediction.entity.ConsAbility;
import com.xqxy.dr.modular.prediction.result.AreaPrediction;
import com.xqxy.dr.modular.prediction.result.ConsPrediction;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangyunfei
 * @since 2021-11-04
 */
public interface ConsAbilityService extends IService<ConsAbility> {

    /**
     * 地区日前负荷预测
     * @param areaId
     * @param statDate
     * @return
     */
    AreaPrediction queryAreaPrediction(String areaId, String statDate, String areaType);


    /**
     * 用户响应能力分析&用户负荷预测
     * @param consId
     * @param statDate
     * @return
     */
    ConsPrediction queryConsPrediction(String consId, String statDate);


}
