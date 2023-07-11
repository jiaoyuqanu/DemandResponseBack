package com.xqxy.dr.modular.evaluation.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.evaluation.entity.ConsEvaluation;
import com.xqxy.dr.modular.evaluation.entity.CustEvaluation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.evaluation.param.ConsEvaluationParam;
import com.xqxy.dr.modular.evaluation.param.CustEvaluationParam;

import java.util.List;

/**
 * <p>
 * 客户执行效果评估 服务类
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-22
 */
public interface CustEvaluationService extends IService<CustEvaluation> {

    /**
     * @description: 根据事件标识和客户标识查询客户效果评估
     * @param: eventId,custId 查询参数
     * @return:
     * @author: hu xingxing
     * @date: 2021-10-22 10:00
     */
    CustEvaluation getCustEvaluationByEventIdAndCustId(long eventId, long custId);

    Page<CustEvaluation> page(CustEvaluationParam custEvaluationParam);

    Page<ConsEvaluation> pageProxy(CustEvaluationParam custEvaluationParam);

    /**
     * @description: 客户列表（包含客户信息）
     * @param: 
     * @return: 
     * @author: PengChuqing
     * @date: 2021/11/2 15:06
     */
    List<CustEvaluation> list(ConsEvaluationParam consEvaluationParam);
}
