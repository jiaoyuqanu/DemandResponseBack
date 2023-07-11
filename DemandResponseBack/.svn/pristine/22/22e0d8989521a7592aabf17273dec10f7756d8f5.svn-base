package com.xqxy.dr.modular.upload.service.impl;

import com.xqxy.dr.modular.subsidy.entity.ConsSubsidyPay;
import com.xqxy.dr.modular.upload.mapper.SubsidyPayMapper;
import com.xqxy.dr.modular.upload.service.SubsidyPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 补贴发放记录 服务实现类
 * </p>
 *
 */
@Service
public class SubsidyPayServiceImpl implements SubsidyPayService {

    @Autowired

    private SubsidyPayMapper consSubsidyPayMapper;

    @Override
    public List<ConsSubsidyPay> getConSubsidy() {
        return consSubsidyPayMapper.getConSubsidy();
    }
}
