package com.xqxy.dr.modular.event.service;

import com.xqxy.dr.modular.event.VO.ConsBaseLineExcelVo;
import com.xqxy.dr.modular.event.entity.ConsBaseline;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.event.entity.ConsBaselineAll;
import com.xqxy.dr.modular.event.param.ConsBaselineParam;
import org.apache.xpath.operations.Bool;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-18
 */
public interface ConsBaselineService extends IService<ConsBaseline> {

    /**
     * @description: 用户基线列表
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/18 14:50
     */
    List<ConsBaseline> list(ConsBaselineParam consBaselineParam);

    List<ConsBaseLineExcelVo> exportListToExcel(ConsBaselineParam consBaselineParam);

    /**
     * 获取详情
     *
     * @param consId
     * @param dataDate
     * @return
     */
    ConsBaseline detail(String consId, LocalDate dataDate);

    /**
     * 获取详情
     *
     * @param consId
     * @param dataDate
     * @param baseLineId
     * @return
     */
    ConsBaseline detail(String consId, LocalDate dataDate,Long baseLineId);

    /**
     * @description: 通过用户id和基线库标识
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/26 14:06
     */
    ConsBaseline getConsBaseByLibId(String consId, Long baselinId);

    /**
     * @description: 执行监测——用户基线明细
     * @param: 
     * @return: 
     * @author: PengChuqing
     * @date: 2021/10/27 14:15
     */
    List<ConsBaseline> getConsBaseByEventId(Long eventId, String consId);

    /**
     * @description: 执行监测——用户基线明细
     * @param:
     * @return:
     * @author: liqirui
     * @date: 2022/10/27 14:15
     */
    List<ConsBaselineAll> getBaselineAllByEventId(Long eventId, String consId);


}
