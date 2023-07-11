package com.xqxy.dr.modular.evaluation.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.evaluation.entity.ConsEvaluation;
import com.xqxy.dr.modular.evaluation.entity.CustEvaluation;
import com.xqxy.dr.modular.evaluation.entity.CustEvaluationImmediate;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.evaluation.param.CustEvaluationImmediateParam;
import com.xqxy.dr.modular.evaluation.param.CustEvaluationParam;
import com.xqxy.dr.modular.evaluation.param.EvaluationImmediateParam;

import java.util.List;

/**
 * <p>
 * 客户当日执行效果评估 服务类
 * </p>
 *
 * @author Peng
 * @since 2021-10-29
 */
public interface CustEvaluationImmediateService extends IService<CustEvaluationImmediate> {

    Page<CustEvaluationImmediate> page(CustEvaluationImmediateParam custEvaluationImmediateParam);

    Page<CustEvaluationImmediate> pageProxy(CustEvaluationImmediateParam custEvaluationImmediateParam);

    List<CustEvaluationImmediate> list(EvaluationImmediateParam evaluationImmediateParam);

}
