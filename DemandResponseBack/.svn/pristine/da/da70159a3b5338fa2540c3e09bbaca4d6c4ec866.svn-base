package com.xqxy.dr.modular.evaluation.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.core.pojo.page.PageResult;
import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.evaluation.entity.ConsEvaluation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.evaluation.entity.EventPowerExecute;
import com.xqxy.dr.modular.evaluation.param.ConsEvaluationParam;
import com.xqxy.dr.modular.evaluation.param.CustEvaluationParam;
import com.xqxy.dr.modular.evaluation.param.EvaluationImmediateParam;

import java.util.List;

/**
 * <p>
 * 用户执行效果评估 服务类
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-20
 */
public interface ConsEvaluationService extends IService<ConsEvaluation> {


    Page<ConsEvaluation> page(ConsEvaluationParam evaluationParam);

    /**
     * @description: 根据事件标识和用户标识查询用户效果评估
     * @param: evalTaskParam 查询参数
     * @return:
     * @author: hu xingxing
     * @date: 2021-10-20 10:00
     */
    ConsEvaluation getConsEvaluationByEventIdAndConsId(long eventId, String consId);

    /**
     * @description: 查询用户效果评估
     * @param: consEvaluationParam 查询参数
     * @return:
     * @author: hu xingxing
     * @date: 2021-10-22 10:00
     */
    Object list(ConsEvaluationParam consEvaluationParam);

    /**
     * 根据事件和用户获取效果评估
     * @param eventId
     * @param consId
     * @return
     */
    ConsEvaluation detailByEventIdAndConsId(Long eventId, String consId);

    /**
     * @description: 查询客户名下的代理用户效果评估
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/28 17:02
     */
    Page<ConsEvaluation> pageProxy(CustEvaluationParam custEvaluationParam);

    /**
     * @description: 次日效果评估报表导出
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/11/2 14:02
     */
    void exportEvaluationImmediate(ConsEvaluationParam consEvaluationParam);

    /**
     * @description: 用户次日效果评估列表（包含用户信息）
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/11/2 14:51
     */
    List<ConsEvaluation> listWithCons(ConsEvaluationParam consEvaluationParam);
    /**
     * @description: 用户执行曲线（包含用户信息）
     * @param:
     * @return:
     * @author: shi
     * @date: 2021/11/6 14:51
     */
    EventPowerExecute implementCurve(ConsEvaluationParam consEvaluationParam);

}
