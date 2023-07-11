package com.xqxy.dr.modular.subsidy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.subsidy.entity.SettlementRec;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.subsidy.param.SettlementRecParam;

/**
 * <p>
 * 响应补贴结算记录表 服务类
 * </p>
 *
 * @author hu xingxing
 * @since 2022-01-07
 */
public interface SettlementRecService extends IService<SettlementRec> {

    /**
     * 补贴结算记录分页查询
     * @param settlementRecParam
     * @return
     */
    Page<SettlementRec> settlementPage(SettlementRecParam settlementRecParam);

    /**
     * 补贴结算记录生成
     * @param settlementRecParam
     * @return
     */
    void createSettlementRec(SettlementRecParam settlementRecParam);

    /**
     * 补贴结算记录发放
     * @param settlementRecParam
     * @return
     */
    void settlementRecPay(SettlementRecParam settlementRecParam);

    Page<SettlementRec> getCustSettlementRecPage(SettlementRecParam settlementRecParam);
}
