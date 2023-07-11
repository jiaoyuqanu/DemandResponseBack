package com.xqxy.dr.modular.powerplant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.dr.modular.powerplant.entity.DaOrgEnergyCurve;
import com.xqxy.dr.modular.powerplant.mapper.DaOrgEnergyCurveMapper;
import com.xqxy.dr.modular.powerplant.service.DaOrgEnergyCurveService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.sys.modular.cust.enums.ConsExceptionEnum;
import io.netty.util.internal.StringUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 用户总电能量曲线 服务实现类
 * </p>
 *
 * @author shi
 * @since 2021-12-08
 */
@Service
public class DaOrgEnergyCurveServiceImpl extends ServiceImpl<DaOrgEnergyCurveMapper, DaOrgEnergyCurve> implements DaOrgEnergyCurveService {

    public Map<String, List<DaOrgEnergyCurve>> getEnergyCurve()
    {
        Map<String,List<DaOrgEnergyCurve>> map=new HashMap<>();
        String orgId = Objects.requireNonNull(SecurityUtils.getCurrentUserInfoUTF8()).getOrgId();
        LocalDate today=LocalDate.now();
        LocalDate yesToday=today.plusDays(-1);
        LambdaQueryWrapper<DaOrgEnergyCurve> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        if(StringUtil.isNullOrEmpty(orgId))
        {
            throw new ServiceException(ConsExceptionEnum.ORG_NOT_EXIST);
        }
        else {
//            lambdaQueryWrapper.eq(DaOrgEnergyCurve::getOrgNo,orgId);
            lambdaQueryWrapper.eq(DaOrgEnergyCurve::getDataDate,today);
            map.put("todayCurve",this.list(lambdaQueryWrapper));
            lambdaQueryWrapper=new LambdaQueryWrapper<>();
//            lambdaQueryWrapper.eq(DaOrgEnergyCurve::getOrgNo,orgId);
            lambdaQueryWrapper.eq(DaOrgEnergyCurve::getDataDate,yesToday);
            map.put("yesTodayCurve",this.list(lambdaQueryWrapper));
        }
        return  map;
    }

}
