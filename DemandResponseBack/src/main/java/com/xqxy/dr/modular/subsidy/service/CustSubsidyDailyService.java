package com.xqxy.dr.modular.subsidy.service;

import com.xqxy.dr.modular.subsidy.entity.CustSubsidyDaily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDate;

/**
 * <p>
 * 日补贴 服务类
 * </p>
 *
 * @author Shen
 * @since 2021-10-21
 */
public interface CustSubsidyDailyService extends IService<CustSubsidyDaily> {

    /**
     * 根据用户标识和日期进行用户日补贴查询
     *
     * @param
     * @return
     * @author hu xingxing
     * @date 2021-10-25 15:20
     */
    CustSubsidyDaily getCustDailySubsidy(long custId, LocalDate subsidyDate);

}
