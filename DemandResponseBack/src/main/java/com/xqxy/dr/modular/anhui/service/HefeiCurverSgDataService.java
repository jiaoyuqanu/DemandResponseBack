package com.xqxy.dr.modular.anhui.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.anhui.entity.HefeiCurverSgData;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 合肥策略实时数据 服务类
 * </p>
 *
 * @author liuyu
 * @since 2021-12-01
 */
public interface HefeiCurverSgDataService extends IService<HefeiCurverSgData> {

    /**
     * 查询实时负荷
     * @param consNo 户号
     * @param dateTime 日期
     * @return 96点数据p-值
     */
    Map<String, BigDecimal> queryDataByConsDate(String consNo, String dateTime);


    /**
     * 查询历史负荷
     * @param elecConsNo 户号
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 96点历史负荷List
     */
    List<ConsCurve> queryListCurve(String elecConsNo, String startDate, String endDate);

}
