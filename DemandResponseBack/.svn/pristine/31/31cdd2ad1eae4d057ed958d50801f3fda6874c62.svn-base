package com.xqxy.dr.modular.data.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.data.entity.ConsEnergyCurve;
import com.xqxy.dr.modular.data.param.ConsAndDate;
import com.xqxy.sys.modular.cust.entity.Cons;

import java.util.List;

/**
 * <p>
 * 用户总电能量曲线 服务类
 * </p>
 *
 * @author xiao jun
 * @since 2021-05-13
 */
public interface ConsEnergyCurveService extends IService<ConsEnergyCurve> {

    /**
     * @description: 通过consId和日期获取电量
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/5/14 11:06
     */
    ConsEnergyCurve getCurveByConsIdAndDate(String consId, String dataDate);

    /**
     * @description: 获取实时负荷
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/5/17 8:56
     */
    ConsEnergyCurve getRealtimeCurve(ConsAndDate consAndDate);

    /**
     * @description: 获取历史负荷
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/5/17 9:34
     */
    ConsEnergyCurve getHistoryCurve(ConsAndDate consAndDate);

    /**
     * @description: 采集监测历史电量监测
     * @param:
     * @return:
     * @author: shi
     * @date: 2021/11/13 14:36
     */
    Page<Cons> energyMonitorList(ConsAndDate consAndDate);
}
