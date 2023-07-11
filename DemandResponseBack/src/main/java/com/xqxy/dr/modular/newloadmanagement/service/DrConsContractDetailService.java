package com.xqxy.dr.modular.newloadmanagement.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.newloadmanagement.entity.DrConsContractDetail;
import com.xqxy.dr.modular.newloadmanagement.param.ComprehensiveIndicatorsParam;
import com.xqxy.dr.modular.newloadmanagement.param.DemandEvaluationParam;
import com.xqxy.dr.modular.newloadmanagement.vo.UserListVo;

import java.util.List;

/**
 * @Description 需求响应综合指标数据
 * @Author jyq
 * @Date 2022/6/20
 */
public interface DrConsContractDetailService {


    Integer numOfContractedHouseholds();


}
