package com.xqxy.dr.modular.evaluation.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.evaluation.entity.ConsEvaluationImmediate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.dr.modular.evaluation.entity.CustEvaluationImmediate;
import com.xqxy.dr.modular.evaluation.entity.EvaluationImmediate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 今日响应效果评估 Mapper 接口
 * </p>
 *
 * @author Peng
 * @since 2021-10-29
 */
public interface ConsEvaluationImmediateMapper extends BaseMapper<ConsEvaluationImmediate> {

    Page<ConsEvaluationImmediate> selectPageVo(Page<Object> defaultPage, @Param(Constants.WRAPPER) QueryWrapper<Object> queryWrapper);

    Page<CustEvaluationImmediate> pageProxy(Page<Object> defaultPage,@Param(Constants.WRAPPER) QueryWrapper<Object> queryWrapper);

    List<ConsEvaluationImmediate> immediateList(@Param(Constants.WRAPPER) QueryWrapper<Object> queryWrapper);

}
