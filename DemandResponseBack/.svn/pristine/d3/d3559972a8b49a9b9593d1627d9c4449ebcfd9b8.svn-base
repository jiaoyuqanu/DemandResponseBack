package com.xqxy.dr.modular.baseline.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.core.pojo.page.PageResult;
import com.xqxy.dr.modular.baseline.entity.BaseLine;
import com.xqxy.dr.modular.baseline.entity.CustBaseLineDetail;
import com.xqxy.dr.modular.baseline.param.BaseLineParam;
import com.xqxy.dr.modular.event.entity.ConsBaseline;
import com.xqxy.dr.modular.event.param.EventParam;

import java.util.List;

/**
 * <p>
 * 基线管理 服务类
 * </p>
 *
 * @author chen zhi jun
 * @since 2021-10-18
 */
public interface BaseLineService extends IService<BaseLine> {

    /**
     * @description:
     * @param: 
     * @return: 
     * @author: chen zhi jun
     * @date: 2021/10/18 20:16
     */
    Long add(BaseLineParam baseLineParam);

    /**
     * @description:
     * @param:
     * @return:
     * @author: chen zhi jun
     * @date: 2021/10/19 13:44
     */
    Page<BaseLine> page(BaseLineParam baseLineParam);

    /**
     * 查询集成商基线
     * @return
     */
    List<CustBaseLineDetail> getCustBaseLine();

    /**
     * 查询集成商基线
     * @return
     */
    List<CustBaseLineDetail> getCustBaseLineAll();

    List<CustBaseLineDetail> getCustCurve(List<String> sampleDateList,List<String> cons);

    List<CustBaseLineDetail> getCustCurveAll(List<String> sampleDateList,List<String> cons);

    List<BaseLine> getBaseLineInfo();

    List<ConsBaseline> getConsBaseLineInfo();

    List<CustBaseLineDetail> getCustAndConsInfo();

    List<ConsBaseline> getConsBaseLineByBaseLineId(Long baselineId);
    List<ConsBaseline> getConsBaseLineByBaseLineIdAll(Long baselineId);
}
