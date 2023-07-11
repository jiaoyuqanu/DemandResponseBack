package com.xqxy.dr.modular.evaluation.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.evaluation.entity.ConsEvaluation;
import com.xqxy.dr.modular.evaluation.entity.ConsEvaluationImmediate;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.evaluation.entity.CustEvaluationImmediate;
import com.xqxy.dr.modular.evaluation.entity.EvaluationImmediate;
import com.xqxy.dr.modular.evaluation.param.ConsEvaluationImmediateParam;
import com.xqxy.dr.modular.evaluation.param.ConsEvaluationParam;
import com.xqxy.dr.modular.evaluation.param.CustEvaluationImmediateParam;
import com.xqxy.dr.modular.evaluation.param.EvaluationImmediateParam;

import java.util.List;

/**
 * <p>
 * 今日响应效果评估 服务类
 * </p>
 *
 * @author Peng
 * @since 2021-10-29
 */
public interface ConsEvaluationImmediateService extends IService<ConsEvaluationImmediate> {

    /**
     * @description: 用户当日效果评估分页查询
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/29 10:01
     */
    Page<ConsEvaluationImmediate> page(ConsEvaluationImmediateParam consEvaluationImmediateParam);

    Page<CustEvaluationImmediate> pageProxy(CustEvaluationImmediateParam custEvaluationImmediateParam);

    /**
     * @description: 当日效果评估导出
     * @param: 
     * @return: 
     * @author: PengChuqing
     * @date: 2021/11/1 14:48
     */
    void exportEvaluationImmediate(EvaluationImmediateParam evaluationImmediateParam);

    /**
     * 效果评估清单
     * @param evaluationImmediateParam
     * @return
     */
    List<ConsEvaluationImmediate> list(EvaluationImmediateParam evaluationImmediateParam);



}
