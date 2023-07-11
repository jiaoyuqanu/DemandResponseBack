package com.xqxy.dr.modular.evaluation.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.evaluation.entity.CustEvaluation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 客户执行效果评估 Mapper 接口
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-22
 */
public interface CustEvaluationMapper extends BaseMapper<CustEvaluation> {

    Page<CustEvaluation> selectPageVo(Page<Object> defaultPage, @Param(Constants.WRAPPER) QueryWrapper<Object> queryWrapper);

    List<CustEvaluation> getCustList(@Param(Constants.WRAPPER) QueryWrapper<Object> queryWrapper);
}
