package com.xqxy.dr.modular.evaluation.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.baseline.entity.BaseLineDetail;
import com.xqxy.dr.modular.evaluation.entity.CustEvaluationImmediate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.dr.modular.evaluation.param.EvaluCustTaskParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 客户当日执行效果评估 Mapper 接口
 * </p>
 *
 * @author Peng
 * @since 2021-10-29
 */
public interface CustEvaluationImmediateMapper extends BaseMapper<CustEvaluationImmediate> {

    Page<CustEvaluationImmediate> selectPageVo(Page<Object> defaultPage, @Param(Constants.WRAPPER) QueryWrapper<Object> queryWrapper);

    List<CustEvaluationImmediate> immediateCustList( @Param(Constants.WRAPPER) QueryWrapper<Object> queryWrapper);

    /**
     * 查询客户参与事件所有用户的基线
     * @param evaluCustTaskParam
     * @return
     */
    BaseLineDetail getCustBaseLine(EvaluCustTaskParam evaluCustTaskParam);

    /**
     * 获取客户绑定的用户
     * @param evaluCustTaskParam
     * @return
     */
    List<String> getConsByCustAndEvent(EvaluCustTaskParam evaluCustTaskParam);
}
