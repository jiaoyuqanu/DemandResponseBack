package com.xqxy.dr.modular.subsidy.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.subsidy.entity.SettlementRec;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.dr.modular.subsidy.param.SettlementRecParam;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 响应补贴结算记录表 Mapper 接口
 * </p>
 *
 * @author hu xingxing
 * @since 2022-01-07
 */
public interface SettlementRecMapper extends BaseMapper<SettlementRec> {

    Page<SettlementRec> settlementRecPage(Page<Object> defaultPage, Map<String, Object> map);

    Page<SettlementRec> custSettlementRecPage(Page<Object> defaultPage, Map<String, Object> map);

    List<SettlementRec> isRepeatSettlementRecWeek(SettlementRecParam settlementRecParam);
 }
