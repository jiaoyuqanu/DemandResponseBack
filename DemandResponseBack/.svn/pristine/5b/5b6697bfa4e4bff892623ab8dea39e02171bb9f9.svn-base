package com.xqxy.dr.modular.strategy.Utils;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.xqxy.dr.modular.baseline.entity.CustBaseLineDetail;
import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.evaluation.entity.*;
import com.xqxy.dr.modular.event.entity.Event;
import com.xqxy.dr.modular.eventEvaluation.entity.EventEvaluation;
import com.xqxy.dr.modular.project.enums.ProjectTargetEnums;
import com.xqxy.dr.modular.subsidy.enums.CalRuleEnum;
import lombok.Data;
import org.apache.commons.lang.RandomStringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Data
public class StrategyUtils {
    //自动生成id
    public synchronized static String getId() {
        StringBuilder rs = new StringBuilder(30);
        rs.append(1500000000000000000L+System.currentTimeMillis());
        rs.append(RandomStringUtils.randomAlphabetic(10));
        return rs.toString();
    }

    //自动生成id
   /* public synchronized static String getId() {
        StringBuilder rs = new StringBuilder(30);
        rs.append(1500000000000000000L+System.currentTimeMillis());
        rs.append(RandomStringUtils.randomAlphabetic(10));
        return rs.toString();
    }*/

    public synchronized static Long getIdLong() {
        Random r = new Random();
        StringBuilder rs = new StringBuilder(30);
        rs.append(System.currentTimeMillis());
        for (int i = 0; i < 6; i++) {
                 rs.append(r.nextInt(10));
        }
        return Long.valueOf(rs.toString());
    }

    //校验参数是否日期
    public static boolean  isDate(String param) {
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dateFormat2.parse(param);
            return true;
        } catch (ParseException e) {
            e.getMessage();
            return false;
        }
    }

    /**
     * 判断当前曲线中的点是否全部为空
     *
     * @param consCurve
     * @param startP
     * @param endP
     * @return
     */
    public Boolean judgeIsAllNull(ConsCurve consCurve, int startP, int endP) {
        if (ObjectUtil.isNull(consCurve)) return true;
        for (int i = startP; i <= endP; i++) {
            Object fieldValue = ReflectUtil.getFieldValue(consCurve, "p" + i);
            if (ObjectUtil.isNotNull(fieldValue)) return false;
        }
        return true;
    }
    /**
     * 判断当前曲线中的点是否全部为空
     *
     * @param consCurve
     * @param startP
     * @param endP
     * @return
     */
    public Boolean judgeIsAllNull2(CustBaseLineDetail consCurve, int startP, int endP) {
        if (ObjectUtil.isNull(consCurve)) return true;
        for (int i = startP; i <= endP; i++) {
            Object fieldValue = ReflectUtil.getFieldValue(consCurve, "p" + i);
            if (ObjectUtil.isNotNull(fieldValue)) return false;
        }
        return true;
    }

    /**
     * 根据有效性判定规则来判定是否有效响应   当日用户
     * 规则一：
     *      * 1 基线最大负荷—响应时段最大负荷>用户申报负荷
     *      * 2 基线平均负荷—响应时段平均负荷>80%*用户申报负荷
     * 规则二：
     *      * 1 对于削峰需求响应，响应时段最大负荷低于基线最大负荷；对于填谷需求响应，响应时段最小负荷高于基线最小负荷
     *      * 2 对于削峰需求响应，响应时段平均负荷低于基线平均负荷，负荷响应率大于等于80%；对于填谷需求响应，响应时段平均负荷高于基线平均负荷，负荷响应率大于等于80%
     *      * 3 单次需求响应持续时长不低于1小时
     * @param evaluationImmediate
     * @param evaluTask
     * @return
     */
    public EvaluationImmediate judgeEffectiveOfRule(EvaluationImmediate evaluationImmediate, EvaluTask evaluTask,Event event) {
        String validityJudgment = evaluTask.getValidityJudgment();
        if (CalRuleEnum.ONE.getCode().equals(validityJudgment)) {
            return judgeEeffective(evaluationImmediate);
        } else if (CalRuleEnum.TWO.getCode().equals(validityJudgment) || validityJudgment == null) {
            return judgeEffectiveOfRuleTwo(evaluationImmediate,event);
        }
        return evaluationImmediate;
    }

    /**
     * 判定响应有效性  规则一
     * 1 基线最大负荷—响应时段最大负荷>用户申报负荷
     * 2 基线平均负荷—响应时段平均负荷>80%*用户申报负荷
     *
     * @return
     */
    public EvaluationImmediate judgeEeffective(EvaluationImmediate evaluationImmediate) {

        //基线最大负荷—响应时段最大负荷
        BigDecimal maxLoadDifference = NumberUtil.sub(evaluationImmediate.getMaxLoadBaseline(), evaluationImmediate.getMaxLoadActual());
        //基线平均负荷—响应时段平均负荷
        BigDecimal avgLoadDifference = NumberUtil.sub(evaluationImmediate.getAvgLoadBaseline(), evaluationImmediate.getAvgLoadActual());
        //80%*响应负荷确认值
        BigDecimal targetCap = NumberUtil.mul(evaluationImmediate.getReplyCap(), new BigDecimal("0.8"));

        if(null!=evaluationImmediate.getReplyCap()) {
            //1 (基线最大负荷—响应时段最大负荷)<响应负荷确认值 判定无效
            if (maxLoadDifference.compareTo(evaluationImmediate.getReplyCap()) == -1) {
                evaluationImmediate.setRemark("(基线最大负荷—响应时段最大负荷)<响应负荷确认值");
                evaluationImmediate.setIsEffective("N");
                return evaluationImmediate;
            }
            //2 (基线平均负荷—响应时段平均负荷)<80%*响应负荷确认值 判定无效
            if (avgLoadDifference.compareTo(targetCap) == -1) {
                evaluationImmediate.setRemark("(基线平均负荷—响应时段平均负荷)<80%*响应负荷确认值");
                evaluationImmediate.setIsEffective("N");
                return evaluationImmediate;
            }

        } else {
            evaluationImmediate.setRemark("无邀约反馈值");
            evaluationImmediate.setIsEffective("N");
            return evaluationImmediate;
        }
        evaluationImmediate.setIsEffective("Y");
        return evaluationImmediate;
    }

    /**
     * 判定响应有效性  规则二
     * 1 对于削峰需求响应，响应时段最大负荷低于基线最大负荷；对于填谷需求响应，响应时段最小负荷高于基线最小负荷
     * 2 对于削峰需求响应，响应时段平均负荷低于基线平均负荷，负荷响应率大于等于80%；对于填谷需求响应，响应时段平均负荷高于基线平均负荷，负荷响应率大于等于80%
     * 3 单次需求响应持续时长不低于1小时
     *
     * @return
     */
    public EvaluationImmediate judgeEffectiveOfRuleTwo(EvaluationImmediate evaluationImmediate,Event event) {
        String responseType = event.getResponseType();
        BigDecimal maxLoadActual = evaluationImmediate.getMaxLoadActual();
        BigDecimal avgLoadActual = evaluationImmediate.getAvgLoadActual();
        BigDecimal minLoadActual = evaluationImmediate.getMinLoadActual();
        BigDecimal actualCap = evaluationImmediate.getActualCap();
        BigDecimal maxLoadBaseline = evaluationImmediate.getMaxLoadBaseline();
        BigDecimal avgLoadBaseline = evaluationImmediate.getAvgLoadBaseline();
        BigDecimal minLoadBaseline = evaluationImmediate.getMinLoadBaseline();
        BigDecimal replyCap = evaluationImmediate.getReplyCap();
        Integer effectiveTime = evaluationImmediate.getEffectiveTime();

        BigDecimal baseRate = new BigDecimal("0.8");
        boolean responseLoadRate = actualCap.compareTo(NumberUtil.mul(replyCap, baseRate)) < 0;

        if (ProjectTargetEnums.PEEK.getCode().equals(responseType)) {
            if (maxLoadActual == null || maxLoadBaseline == null || maxLoadActual.compareTo(maxLoadBaseline) >= 0) {
                evaluationImmediate.setRemark("实际最大负荷大于等于基线最大负荷，或者为空");
                evaluationImmediate.setIsEffective("N");
                return evaluationImmediate;
            }
            if (avgLoadActual == null || avgLoadBaseline == null || avgLoadActual.compareTo(avgLoadBaseline) >= 0 || responseLoadRate) {
                evaluationImmediate.setRemark("负荷响应率低于百分80，或者实际平均负荷、基线平均负荷为空");
                evaluationImmediate.setIsEffective("N");
                return evaluationImmediate;
            }
        } else if (ProjectTargetEnums.FILL.getCode().equals(responseType)) {
            if (minLoadActual == null || minLoadBaseline == null || minLoadActual.compareTo(minLoadBaseline) <= 0) {
                evaluationImmediate.setRemark("实际最大负荷大于等于基线最大负荷，或者为空");
                evaluationImmediate.setIsEffective("N");
                return evaluationImmediate;
            }

            if (avgLoadActual == null || avgLoadBaseline == null || avgLoadActual.compareTo(avgLoadBaseline) <= 0 || responseLoadRate) {
                evaluationImmediate.setRemark("负荷响应率低于百分80，或者实际平均负荷、基线平均负荷为空");
                evaluationImmediate.setIsEffective("N");
                return evaluationImmediate;
            }
        }

        if (effectiveTime < 60) {
            evaluationImmediate.setRemark("有效时长不足一小时");
            evaluationImmediate.setIsEffective("N");
            return evaluationImmediate;
        }
        evaluationImmediate.setIsEffective("Y");
        return evaluationImmediate;
    }

    /**
     * 根据有效性判定规则来判定是否有效响应   当日客户
     * 规则一：
     *      * 1 基线最大负荷—响应时段最大负荷>用户申报负荷
     *      * 2 基线平均负荷—响应时段平均负荷>80%*用户申报负荷
     * 规则二：
     *      * 1 对于削峰需求响应，响应时段最大负荷低于基线最大负荷；对于填谷需求响应，响应时段最小负荷高于基线最小负荷
     *      * 2 对于削峰需求响应，响应时段平均负荷低于基线平均负荷，负荷响应率大于等于80%；对于填谷需求响应，响应时段平均负荷高于基线平均负荷，负荷响应率大于等于80%
     *      * 3 单次需求响应持续时长不低于1小时
     * @param evaluationImmediate
     * @param evaluTask
     * @return
     */
    public CustEvaluationImmediate judgeCustEffectiveOfRule(CustEvaluationImmediate evaluationImmediate, EvaluCustTask evaluTask,Event event) {

        String validityJudgment = evaluTask.getValidityJudgment();
        if (CalRuleEnum.ONE.getCode().equals(validityJudgment)) {
            return judgeCustEeffective(evaluationImmediate);
        } else if (CalRuleEnum.TWO.getCode().equals(validityJudgment) || validityJudgment == null) {
            return judgeCustEffectiveOfRuleTwo(evaluationImmediate,event);
        }
        return evaluationImmediate;
    }


    /**
     * 判定响应有效性  规则一
     * 1 基线最大负荷—响应时段最大负荷>用户申报负荷
     * 2 基线平均负荷—响应时段平均负荷>80%*用户申报负荷
     *
     * @return
     */
    public CustEvaluationImmediate judgeCustEeffective(CustEvaluationImmediate evaluationImmediate) {

        //基线最大负荷—响应时段最大负荷
        BigDecimal maxLoadDifference = NumberUtil.sub(evaluationImmediate.getMaxLoadBaseline(), evaluationImmediate.getMaxLoadActual());
        //基线平均负荷—响应时段平均负荷
        BigDecimal avgLoadDifference = NumberUtil.sub(evaluationImmediate.getAvgLoadBaseline(), evaluationImmediate.getAvgLoadActual());
        //80%*响应负荷确认值
        BigDecimal targetCap = NumberUtil.mul(evaluationImmediate.getReplyCap(), new BigDecimal("0.8"));
        if(null!=evaluationImmediate.getReplyCap()) {
            //1 (基线最大负荷—响应时段最大负荷)<响应负荷确认值 判定无效
            if (maxLoadDifference.compareTo(evaluationImmediate.getReplyCap()) == -1) {
                evaluationImmediate.setRemark("(基线最大负荷—响应时段最大负荷)<响应负荷确认值");
                evaluationImmediate.setIsEffective("N");
                return evaluationImmediate;
            }
            //2 (基线平均负荷—响应时段平均负荷)<80%*响应负荷确认值 判定无效
            if (avgLoadDifference.compareTo(targetCap) == -1) {
                evaluationImmediate.setRemark("(基线平均负荷—响应时段平均负荷)<80%*响应负荷确认值");
                evaluationImmediate.setIsEffective("N");
                return evaluationImmediate;
            }

        } else {
            evaluationImmediate.setRemark("无邀约反馈值");
            evaluationImmediate.setIsEffective("N");
            return evaluationImmediate;
        }
        evaluationImmediate.setIsEffective("Y");
        return evaluationImmediate;
    }

    /**
     * 判定响应有效性  规则二
     * 1 对于削峰需求响应，响应时段最大负荷低于基线最大负荷；对于填谷需求响应，响应时段最小负荷高于基线最小负荷
     * 2 对于削峰需求响应，响应时段平均负荷低于基线平均负荷，负荷响应率大于等于80%；对于填谷需求响应，响应时段平均负荷高于基线平均负荷，负荷响应率大于等于80%
     * 3 单次需求响应持续时长不低于1小时
     *
     * @return
     */
    public CustEvaluationImmediate judgeCustEffectiveOfRuleTwo(CustEvaluationImmediate evaluationImmediate,Event event) {
        String responseType = event.getResponseType();
        BigDecimal maxLoadActual = evaluationImmediate.getMaxLoadActual();
        BigDecimal avgLoadActual = evaluationImmediate.getAvgLoadActual();
        BigDecimal minLoadActual = evaluationImmediate.getMinLoadActual();
        BigDecimal actualCap = evaluationImmediate.getActualCap();
        BigDecimal maxLoadBaseline = evaluationImmediate.getMaxLoadBaseline();
        BigDecimal avgLoadBaseline = evaluationImmediate.getAvgLoadBaseline();
        BigDecimal minLoadBaseline = evaluationImmediate.getMinLoadBaseline();
        BigDecimal replyCap = evaluationImmediate.getReplyCap();
        Integer effectiveTime = evaluationImmediate.getEffectiveTime();

        BigDecimal baseRate = new BigDecimal("0.8");
        boolean responseLoadRate = actualCap.compareTo(NumberUtil.mul(replyCap, baseRate)) < 0;

        if (ProjectTargetEnums.PEEK.getCode().equals(responseType)) {
            if (maxLoadActual == null || maxLoadBaseline == null || maxLoadActual.compareTo(maxLoadBaseline) >= 0) {
                evaluationImmediate.setRemark("实际最大负荷大于等于基线最大负荷，或者为空");
                evaluationImmediate.setIsEffective("N");
                return evaluationImmediate;
            }

            if (avgLoadActual == null || avgLoadBaseline == null || avgLoadActual.compareTo(avgLoadBaseline) >= 0 || responseLoadRate) {
                evaluationImmediate.setRemark("负荷响应率低于百分80，或者实际平均负荷、基线平均负荷为空");
                evaluationImmediate.setIsEffective("N");
                return evaluationImmediate;
            }
        } else if (ProjectTargetEnums.FILL.getCode().equals(responseType)) {
            if (minLoadActual == null || minLoadBaseline == null || minLoadActual.compareTo(minLoadBaseline) <= 0) {
                evaluationImmediate.setRemark("实际最大负荷大于等于基线最大负荷，或者为空");
                evaluationImmediate.setIsEffective("N");
                return evaluationImmediate;
            }

            if (avgLoadActual == null || avgLoadBaseline == null || avgLoadActual.compareTo(avgLoadBaseline) <= 0 || responseLoadRate) {
                evaluationImmediate.setRemark("负荷响应率低于百分80，或者实际平均负荷、基线平均负荷为空");
                evaluationImmediate.setIsEffective("N");
                return evaluationImmediate;
            }
        }

        if (effectiveTime < 60) {
            evaluationImmediate.setRemark("有效时长不足一小时");
            evaluationImmediate.setIsEffective("N");
            return evaluationImmediate;
        }
        evaluationImmediate.setIsEffective("Y");
        return evaluationImmediate;
    }

    /**
     * 根据有效性判定规则来判定是否有效响应  次日用户
     * 规则一：
     *      * 1 基线最大负荷—响应时段最大负荷>用户申报负荷
     *      * 2 基线平均负荷—响应时段平均负荷>80%*用户申报负荷
     * 规则二：
     *      * 1 对于削峰需求响应，响应时段最大负荷低于基线最大负荷；对于填谷需求响应，响应时段最小负荷高于基线最小负荷
     *      * 2 对于削峰需求响应，响应时段平均负荷低于基线平均负荷，负荷响应率大于等于80%；对于填谷需求响应，响应时段平均负荷高于基线平均负荷，负荷响应率大于等于80%
     *      * 3 单次需求响应持续时长不低于1小时
     * @param evaluationImmediate
     * @param evaluTask
     * @return
     */
    public ConsEvaluation judgeEeffectiveNextOfRule(ConsEvaluation evaluationImmediate, EvaluTask evaluTask,Event event) {
        String validityJudgment = evaluTask.getValidityJudgment();
        if (CalRuleEnum.ONE.getCode().equals(validityJudgment)) {
            return judgeEeffectiveNext(evaluationImmediate);
        } else if (CalRuleEnum.TWO.getCode().equals(validityJudgment) || validityJudgment == null) {
            return judgeEffectiveNextOfRuleTwo(evaluationImmediate,event);
        }
        return evaluationImmediate;
    }

    /**
     * 判定响应有效性  规则一
     * 1 基线最大负荷—响应时段最大负荷>用户申报负荷
     * 2 基线平均负荷—响应时段平均负荷>80%*用户申报负荷
     *
     * @return
     */
    public ConsEvaluation judgeEeffectiveNext(ConsEvaluation evaluationImmediate) {

        //基线最大负荷—响应时段最大负荷
        BigDecimal maxLoadDifference = NumberUtil.sub(evaluationImmediate.getMaxLoadBaseline(), evaluationImmediate.getMaxLoadActual());
        //基线平均负荷—响应时段平均负荷
        BigDecimal avgLoadDifference = NumberUtil.sub(evaluationImmediate.getAvgLoadBaseline(), evaluationImmediate.getAvgLoadActual());
        //80%*响应负荷确认值
        BigDecimal targetCap = NumberUtil.mul(evaluationImmediate.getReplyCap(), new BigDecimal("0.8"));
        if(null!=evaluationImmediate.getReplyCap()) {
            //1 (基线最大负荷—响应时段最大负荷)<响应负荷确认值 判定无效
            if (maxLoadDifference.compareTo(evaluationImmediate.getReplyCap()) == -1) {
                evaluationImmediate.setRemark("(基线最大负荷—响应时段最大负荷)<响应负荷确认值");
                evaluationImmediate.setIsEffective("N");
                return evaluationImmediate;
            }
            //2 (基线平均负荷—响应时段平均负荷)<80%*响应负荷确认值 判定无效
            if (avgLoadDifference.compareTo(targetCap) == -1) {
                evaluationImmediate.setRemark("(基线平均负荷—响应时段平均负荷)<80%*响应负荷确认值");
                evaluationImmediate.setIsEffective("N");
                return evaluationImmediate;
            }

        } else {
            evaluationImmediate.setRemark("无邀约反馈值");
            evaluationImmediate.setIsEffective("N");
            return evaluationImmediate;
        }
        evaluationImmediate.setIsEffective("Y");
        return evaluationImmediate;
    }

    /**
     * 根据有效性判定规则来判定是否有效响应  次日用户
     * 规则一：
     *      * 1 基线最大负荷—响应时段最大负荷>用户申报负荷
     *      * 2 基线平均负荷—响应时段平均负荷>80%*用户申报负荷
     * 规则二：
     *      * 1 对于削峰需求响应，响应时段最大负荷低于基线最大负荷；对于填谷需求响应，响应时段最小负荷高于基线最小负荷
     *      * 2 对于削峰需求响应，响应时段平均负荷低于基线平均负荷，负荷响应率大于等于80%；对于填谷需求响应，响应时段平均负荷高于基线平均负荷，负荷响应率大于等于80%
     *      * 3 单次需求响应持续时长不低于1小时
     * @param evaluationImmediate
     * @return
     */
    public EventEvaluation judgeEeffectiveEventOfRule(EventEvaluation evaluationImmediate,String validityJudgment, Event event) {
        if (CalRuleEnum.ONE.getCode().equals(validityJudgment)) {
            return judgeEeffectiveEvent(evaluationImmediate);
        } else if (CalRuleEnum.TWO.getCode().equals(validityJudgment) || validityJudgment == null) {
            return judgeEffectiveEventOfRuleTwo(evaluationImmediate,event);
        }
        return evaluationImmediate;
    }

    /**
     * 判定响应有效性  规则一
     * 1 基线最大负荷—响应时段最大负荷>用户申报负荷
     * 2 基线平均负荷—响应时段平均负荷>80%*用户申报负荷
     *
     * @return
     */
    public EventEvaluation judgeEeffectiveEvent(EventEvaluation evaluationImmediate) {

        //基线最大负荷—响应时段最大负荷
        BigDecimal maxLoadDifference = NumberUtil.sub(evaluationImmediate.getMaxLoadBaseline(), evaluationImmediate.getMaxLoadActual());
        //基线平均负荷—响应时段平均负荷
        BigDecimal avgLoadDifference = NumberUtil.sub(evaluationImmediate.getAvgLoadBaseline(), evaluationImmediate.getAvgLoadActual());
        //80%*响应负荷确认值
        BigDecimal targetCap = NumberUtil.mul(evaluationImmediate.getReplyCap(), new BigDecimal("0.8"));
        if(null!=evaluationImmediate.getReplyCap()) {
            //1 (基线最大负荷—响应时段最大负荷)<响应负荷确认值 判定无效
            if (maxLoadDifference.compareTo(evaluationImmediate.getReplyCap()) == -1) {
                evaluationImmediate.setRemark("(基线最大负荷—响应时段最大负荷)<响应负荷确认值");
                evaluationImmediate.setIsEffective("N");
                return evaluationImmediate;
            }
            //2 (基线平均负荷—响应时段平均负荷)<80%*响应负荷确认值 判定无效
            if (avgLoadDifference.compareTo(targetCap) == -1) {
                evaluationImmediate.setRemark("(基线平均负荷—响应时段平均负荷)<80%*响应负荷确认值");
                evaluationImmediate.setIsEffective("N");
                return evaluationImmediate;
            }

        } else {
            evaluationImmediate.setRemark("无邀约反馈量");
            evaluationImmediate.setIsEffective("N");
            return evaluationImmediate;
        }
        evaluationImmediate.setIsEffective("Y");
        return evaluationImmediate;
    }

    /**
     * 判定响应有效性  规则二
     * 1 对于削峰需求响应，响应时段最大负荷低于基线最大负荷；对于填谷需求响应，响应时段最小负荷高于基线最小负荷
     * 2 对于削峰需求响应，响应时段平均负荷低于基线平均负荷，负荷响应率大于等于80%；对于填谷需求响应，响应时段平均负荷高于基线平均负荷，负荷响应率大于等于80%
     * 3 单次需求响应持续时长不低于1小时
     *
     * @return
     */
    public EventEvaluation judgeEffectiveEventOfRuleTwo(EventEvaluation evaluationImmediate,Event event) {
        String responseType = event.getResponseType();
        BigDecimal maxLoadActual = evaluationImmediate.getMaxLoadActual();
        BigDecimal avgLoadActual = evaluationImmediate.getAvgLoadActual();
        BigDecimal minLoadActual = evaluationImmediate.getMinLoadActual();
        BigDecimal actualCap = evaluationImmediate.getActualCap();
        BigDecimal maxLoadBaseline = evaluationImmediate.getMaxLoadBaseline();
        BigDecimal avgLoadBaseline = evaluationImmediate.getAvgLoadBaseline();
        BigDecimal minLoadBaseline = evaluationImmediate.getMinLoadBaseline();
        BigDecimal replyCap = evaluationImmediate.getReplyCap();
        Integer effectiveTime = evaluationImmediate.getEffectiveTime();

        BigDecimal baseRate = new BigDecimal("0.8");
        boolean responseLoadRate = actualCap.compareTo(NumberUtil.mul(replyCap, baseRate)) < 0;

        if (ProjectTargetEnums.PEEK.getCode().equals(responseType)) {
            if (maxLoadActual == null || maxLoadBaseline == null || maxLoadActual.compareTo(maxLoadBaseline) >= 0) {
                evaluationImmediate.setRemark("实际最大负荷大于等于基线最大负荷，或者为空");
                evaluationImmediate.setIsEffective("N");
                return evaluationImmediate;
            }

            if (avgLoadActual == null || avgLoadBaseline == null || avgLoadActual.compareTo(avgLoadBaseline) >= 0 || responseLoadRate) {
                evaluationImmediate.setRemark("负荷响应率低于百分80，或者实际平均负荷、基线平均负荷为空");
                evaluationImmediate.setIsEffective("N");
                return evaluationImmediate;
            }
        } else if (ProjectTargetEnums.FILL.getCode().equals(responseType)) {
            if (minLoadActual == null || minLoadBaseline == null || minLoadActual.compareTo(minLoadBaseline) <= 0) {
                evaluationImmediate.setRemark("实际最大负荷大于等于基线最大负荷，或者为空");
                evaluationImmediate.setIsEffective("N");
                return evaluationImmediate;
            }

            if (avgLoadActual == null || avgLoadBaseline == null || avgLoadActual.compareTo(avgLoadBaseline) <= 0 || responseLoadRate) {
                evaluationImmediate.setRemark("负荷响应率低于百分80，或者实际平均负荷、基线平均负荷为空");
                evaluationImmediate.setIsEffective("N");
                return evaluationImmediate;
            }
        }

        if (effectiveTime < 60) {
            evaluationImmediate.setRemark("响应时长低于一小时");
            evaluationImmediate.setIsEffective("N");
            return evaluationImmediate;
        }
        evaluationImmediate.setIsEffective("Y");
        return evaluationImmediate;
    }

    /**
     * 判定响应有效性  规则二
     * 1 对于削峰需求响应，响应时段最大负荷低于基线最大负荷；对于填谷需求响应，响应时段最小负荷高于基线最小负荷
     * 2 对于削峰需求响应，响应时段平均负荷低于基线平均负荷，负荷响应率大于等于80%；对于填谷需求响应，响应时段平均负荷高于基线平均负荷，负荷响应率大于等于80%
     * 3 单次需求响应持续时长不低于1小时
     *
     * @return
     */
    public ConsEvaluation judgeEffectiveNextOfRuleTwo(ConsEvaluation evaluationImmediate,Event event) {
        String responseType = event.getResponseType();
        BigDecimal maxLoadActual = evaluationImmediate.getMaxLoadActual();
        BigDecimal avgLoadActual = evaluationImmediate.getAvgLoadActual();
        BigDecimal minLoadActual = evaluationImmediate.getMinLoadActual();
        BigDecimal actualCap = evaluationImmediate.getActualCap();
        BigDecimal maxLoadBaseline = evaluationImmediate.getMaxLoadBaseline();
        BigDecimal avgLoadBaseline = evaluationImmediate.getAvgLoadBaseline();
        BigDecimal minLoadBaseline = evaluationImmediate.getMinLoadBaseline();
        BigDecimal replyCap = evaluationImmediate.getReplyCap();
        Integer effectiveTime = evaluationImmediate.getEffectiveTime();

        BigDecimal baseRate = new BigDecimal("0.8");
        boolean responseLoadRate = actualCap.compareTo(NumberUtil.mul(replyCap, baseRate)) < 0;

        if (ProjectTargetEnums.PEEK.getCode().equals(responseType)) {
            if (maxLoadActual == null || maxLoadBaseline == null || maxLoadActual.compareTo(maxLoadBaseline) >= 0) {
                evaluationImmediate.setRemark("实际最大负荷大于等于基线最大负荷，或者为空");
                evaluationImmediate.setIsEffective("N");
                return evaluationImmediate;
            }

            if (avgLoadActual == null || avgLoadBaseline == null || avgLoadActual.compareTo(avgLoadBaseline) >= 0 || responseLoadRate) {
                evaluationImmediate.setRemark("负荷响应率低于百分80，或者实际平均负荷、基线平均负荷为空");
                evaluationImmediate.setIsEffective("N");
                return evaluationImmediate;
            }
        } else if (ProjectTargetEnums.FILL.getCode().equals(responseType)) {
            if (minLoadActual == null || minLoadBaseline == null || minLoadActual.compareTo(minLoadBaseline) <= 0) {
                evaluationImmediate.setRemark("实际最大负荷大于等于基线最大负荷，或者为空");
                evaluationImmediate.setIsEffective("N");
                return evaluationImmediate;
            }

            if (avgLoadActual == null || avgLoadBaseline == null || avgLoadActual.compareTo(avgLoadBaseline) <= 0 || responseLoadRate) {
                evaluationImmediate.setRemark("负荷响应率低于百分80，或者实际平均负荷、基线平均负荷为空");
                evaluationImmediate.setIsEffective("N");
                return evaluationImmediate;
            }
        }

        if (effectiveTime < 60) {
            evaluationImmediate.setRemark("响应时长低于一小时");
            evaluationImmediate.setIsEffective("N");
            return evaluationImmediate;
        }
        evaluationImmediate.setIsEffective("Y");
        return evaluationImmediate;
    }

    /**
     * 根据有效性判定规则来判定是否有效响应  次日客户
     * 规则一：
     *      * 1 基线最大负荷—响应时段最大负荷>用户申报负荷
     *      * 2 基线平均负荷—响应时段平均负荷>80%*用户申报负荷
     * 规则二：
     *      * 1 对于削峰需求响应，响应时段最大负荷低于基线最大负荷；对于填谷需求响应，响应时段最小负荷高于基线最小负荷
     *      * 2 对于削峰需求响应，响应时段平均负荷低于基线平均负荷，负荷响应率大于等于80%；对于填谷需求响应，响应时段平均负荷高于基线平均负荷，负荷响应率大于等于80%
     *      * 3 单次需求响应持续时长不低于1小时
     * @param evaluationImmediate
     * @param evaluTask
     * @return
     */
    public CustEvaluation judgeCustEffectiveNextOfRule(CustEvaluation evaluationImmediate, EvaluCustTask evaluTask,Event event) {
        String validityJudgment = evaluTask.getValidityJudgment();
        if (CalRuleEnum.ONE.getCode().equals(validityJudgment)) {
            return judgeCustEeffectiveNext(evaluationImmediate);
        } else if (CalRuleEnum.TWO.getCode().equals(validityJudgment) || validityJudgment == null) {
            return judgeCustEffectiveNextOfRuleTwo(evaluationImmediate,event);
        }
        return evaluationImmediate;
    }

    /**
     * 判定响应有效性  规则一
     * 1 基线最大负荷—响应时段最大负荷>用户申报负荷
     * 2 基线平均负荷—响应时段平均负荷>80%*用户申报负荷
     *
     * @return
     */
    public CustEvaluation judgeCustEeffectiveNext(CustEvaluation evaluationImmediate) {

        //基线最大负荷—响应时段最大负荷
        BigDecimal maxLoadDifference = NumberUtil.sub(evaluationImmediate.getMaxLoadBaseline(), evaluationImmediate.getMaxLoadActual());
        //基线平均负荷—响应时段平均负荷
        BigDecimal avgLoadDifference = NumberUtil.sub(evaluationImmediate.getAvgLoadBaseline(), evaluationImmediate.getAvgLoadActual());
        //80%*响应负荷确认值
        BigDecimal targetCap = NumberUtil.mul(evaluationImmediate.getReplyCap(), new BigDecimal("0.8"));
        if(null!=evaluationImmediate.getReplyCap()) {
            //1 (基线最大负荷—响应时段最大负荷)<响应负荷确认值 判定无效
            if (maxLoadDifference.compareTo(evaluationImmediate.getReplyCap()) == -1) {
                evaluationImmediate.setIsEffective("N");
                evaluationImmediate.setRemark("((基线最大负荷—响应时段最大负荷)<响应负荷确认值");
                return evaluationImmediate;
            }
            //2 (基线平均负荷—响应时段平均负荷)<80%*响应负荷确认值 判定无效
            if (avgLoadDifference.compareTo(targetCap) == -1) {
                evaluationImmediate.setRemark("(基线平均负荷—响应时段平均负荷)<80%*响应负荷确认值");
                evaluationImmediate.setIsEffective("N");
                return evaluationImmediate;
            }

        } else {
            evaluationImmediate.setRemark("(无反馈响应量");
            evaluationImmediate.setIsEffective("N");
            return evaluationImmediate;
        }
        evaluationImmediate.setIsEffective("Y");
        return evaluationImmediate;
    }

    /**
     * 判定响应有效性  规则二
     * 1 对于削峰需求响应，响应时段最大负荷低于基线最大负荷；对于填谷需求响应，响应时段最小负荷高于基线最小负荷
     * 2 对于削峰需求响应，响应时段平均负荷低于基线平均负荷，负荷响应率大于等于80%；对于填谷需求响应，响应时段平均负荷高于基线平均负荷，负荷响应率大于等于80%
     * 3 单次需求响应持续时长不低于1小时
     *
     * @return
     */
    public CustEvaluation judgeCustEffectiveNextOfRuleTwo(CustEvaluation evaluationImmediate,Event event) {
        String responseType = event.getResponseType();
        BigDecimal maxLoadActual = evaluationImmediate.getMaxLoadActual();
        BigDecimal avgLoadActual = evaluationImmediate.getAvgLoadActual();
        BigDecimal minLoadActual = evaluationImmediate.getMinLoadActual();
        BigDecimal actualCap = evaluationImmediate.getActualCap();
        BigDecimal maxLoadBaseline = evaluationImmediate.getMaxLoadBaseline();
        BigDecimal avgLoadBaseline = evaluationImmediate.getAvgLoadBaseline();
        BigDecimal minLoadBaseline = evaluationImmediate.getMinLoadBaseline();
        BigDecimal replyCap = evaluationImmediate.getReplyCap();
        Integer effectiveTime = evaluationImmediate.getEffectiveTime();
        BigDecimal baseRate = new BigDecimal("0.8");
        boolean responseLoadRate = actualCap.compareTo(NumberUtil.mul(replyCap, baseRate)) < 0;
        if (ProjectTargetEnums.PEEK.getCode().equals(responseType)) {
            if (maxLoadActual == null || maxLoadBaseline == null || maxLoadActual.compareTo(maxLoadBaseline) >= 0) {
                evaluationImmediate.setRemark("实际最大负荷大于等于基线最大负荷，或者为空");
                evaluationImmediate.setIsEffective("N");
                return evaluationImmediate;
            }

            if (avgLoadActual == null || avgLoadBaseline == null || avgLoadActual.compareTo(avgLoadBaseline) >= 0 || responseLoadRate) {
                evaluationImmediate.setRemark("负荷响应率低于百分80，或者实际平均负荷、基线平均负荷为空");
                evaluationImmediate.setIsEffective("N");
                return evaluationImmediate;
            }
        } else if (ProjectTargetEnums.FILL.getCode().equals(responseType)) {
            if (minLoadActual == null || minLoadBaseline == null || minLoadActual.compareTo(minLoadBaseline) <= 0) {
                evaluationImmediate.setRemark("实际最大负荷大于等于基线最大负荷，或者为空");
                evaluationImmediate.setIsEffective("N");
                return evaluationImmediate;
            }

            if (avgLoadActual == null || avgLoadBaseline == null || avgLoadActual.compareTo(avgLoadBaseline) <= 0 || responseLoadRate) {
                evaluationImmediate.setRemark("负荷响应率低于百分80，或者实际平均负荷、基线平均负荷为空");
                evaluationImmediate.setIsEffective("N");
                return evaluationImmediate;
            }
        }
        if (effectiveTime < 60) {
            evaluationImmediate.setRemark("有效时长低于一小时");
            evaluationImmediate.setIsEffective("N");
            return evaluationImmediate;
        }
        evaluationImmediate.setIsEffective("Y");
        return evaluationImmediate;
    }

    /**
     *
     * @param actualList 历史曲线
     * @param forecastList  基线
     * @param target 反馈响应量
     * @return
     */
    public int calEeffectiveTime(List<BigDecimal> actualList, List<BigDecimal> forecastList, BigDecimal target) {
        int num = 0;
        List<Integer> nums = new ArrayList<>();
        if(actualList.size()>0) {
            for (int n = 0; n < actualList.size(); n++) {
                BigDecimal effectiveTarget = NumberUtil.mul(target, new BigDecimal("0.8"));
                BigDecimal actualValue = NumberUtil.sub(forecastList.get(n), actualList.get(n));
                if (actualValue.compareTo(effectiveTarget) >= 0) {
                    if (n > 0) {
                        num++;
                        nums.add(num);
                    }
                } else {
                    if (num != 0) {
                        nums.add(num);
                        num = 0;
                    }
                }
            }
        }
        if(nums.size()>0) {
            num =  nums.stream().filter(e -> e!=null).max(Comparator.naturalOrder()).orElse(0);
        }
        //System.out.println("数组："+nums);
        //System.out.println("最大值："+num);
        return num;
    }

    /**
     *
     * @param actualList 历史曲线
     * @param forecastList  基线
     * @param target 反馈响应量
     * @return
     */
    public int calEeffectiveTime2(List<BigDecimal> actualList, List<BigDecimal> forecastList, BigDecimal target) {
        int num = 0;
        List<Integer> nums = new ArrayList<>();
        if(actualList.size()>0) {
            for (int n = 0; n < actualList.size(); n++) {
                BigDecimal effectiveTarget = NumberUtil.mul(target, new BigDecimal("0.8"));
                BigDecimal actualValue = NumberUtil.sub(actualList.get(n), forecastList.get(n));
                if (actualValue.compareTo(effectiveTarget) >= 0) {
                    if (n > 0) {
                        num++;
                        nums.add(num);
                    }
                } else {
                    if (num != 0) {
                        nums.add(num);
                        num = 0;
                    }
                }
            }
        }
        if(nums.size()>0) {
            num =  nums.stream().filter(e -> e!=null).max(Comparator.naturalOrder()).orElse(0);
        }
        //System.out.println("数组："+nums);
        //System.out.println("最大值："+num);
        return num;
    }

   /* public static void main(String[] args) {
        List<BigDecimal> actualList =new ArrayList<>();
        List<BigDecimal> forecastList = new ArrayList<>();
        BigDecimal target = new BigDecimal(1.0);
        for(int i=0;i<10;i++) {
            int random=(int)(Math.random()*5+1);
            int random2=(int)(Math.random()*5+2);
            BigDecimal bigDecimal = new BigDecimal(random);
            BigDecimal bigDecimal2 = new BigDecimal(random2);
            forecastList.add(bigDecimal2);
            actualList.add(bigDecimal);
        }
        StrategyUtils strategyUtils = new StrategyUtils();
        Integer num = strategyUtils.calEeffectiveTime(actualList,forecastList,target);
        System.out.print(num*15);
    }
*/
    /**
     * 计算核定响应量
     * @param replyCap  反馈响应量
     * @param actualCap 实际响应量
     * @return
     */
    public BigDecimal getConfirmCap(BigDecimal replyCap,BigDecimal actualCap) {
        BigDecimal temp = BigDecimal.ZERO;
        if (null != replyCap) {
            temp = NumberUtil.mul(replyCap, 1.2);
        }
        if (temp.compareTo(actualCap) > 0) {
            temp = actualCap;
        }
        return temp;
    }

}
