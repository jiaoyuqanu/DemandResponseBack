package com.xqxy.dr.modular.powerplant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.dr.modular.powerplant.entity.DaOrgCurve;
import com.xqxy.dr.modular.powerplant.mapper.DaOrgCurveMapper;
import com.xqxy.dr.modular.powerplant.service.DaOrgCurveService;
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
 * 用户功率曲线 服务实现类
 * </p>
 *
 * @author shi
 * @since 2021-12-08
 */
@Service
public class DaOrgCurveServiceImpl extends ServiceImpl<DaOrgCurveMapper, DaOrgCurve> implements DaOrgCurveService {

    public Map<String, List<DaOrgCurve>>getCurve()
    {
        Map<String,List<DaOrgCurve>> map=new HashMap<>();
        String orgId = Objects.requireNonNull(SecurityUtils.getCurrentUserInfoUTF8()).getOrgId();
        LocalDate today=LocalDate.now();
        LocalDate yesToday=today.plusDays(-1);
        LambdaQueryWrapper<DaOrgCurve> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        if(StringUtil.isNullOrEmpty(orgId))
        {
            throw new ServiceException(ConsExceptionEnum.ORG_NOT_EXIST);
        }
        else {
//            lambdaQueryWrapper.eq(DaOrgCurve::getOrgNo,orgId);
            lambdaQueryWrapper.eq(DaOrgCurve::getDataDate,today);
            map.put("todayCurve",this.list(lambdaQueryWrapper));
            lambdaQueryWrapper=new LambdaQueryWrapper<>();
//            lambdaQueryWrapper.eq(DaOrgCurve::getOrgNo,orgId);
            lambdaQueryWrapper.eq(DaOrgCurve::getDataDate,yesToday);
            map.put("yesTodayCurve",this.list(lambdaQueryWrapper));
        }
        return  map;
    }

}
