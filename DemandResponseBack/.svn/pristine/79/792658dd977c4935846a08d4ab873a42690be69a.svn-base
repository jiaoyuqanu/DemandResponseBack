package com.xqxy.dr.modular.powerplant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xqxy.dr.modular.powerplant.entity.DaUserCurve;
import com.xqxy.dr.modular.powerplant.mapper.DaUserCurveMapper;
import com.xqxy.dr.modular.powerplant.service.DaUserCurveService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.netty.util.internal.StringUtil;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户功率曲线 服务实现类
 * </p>
 *
 * @author shi
 * @since 2021-12-09
 */
@Service
public class DaUserCurveServiceImpl extends ServiceImpl<DaUserCurveMapper, DaUserCurve> implements DaUserCurveService {

    @Override
    public  DaUserCurve getByConsNo(String consNo)
    {
        DaUserCurve daUserCurve=new DaUserCurve();
        LambdaQueryWrapper<DaUserCurve> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        if(StringUtil.isNullOrEmpty(consNo))
        {

        }
        else
        {
            lambdaQueryWrapper.eq(DaUserCurve::getConsNo,consNo);
            if(this.list(lambdaQueryWrapper).size()>0)
            {
                daUserCurve =this.list(lambdaQueryWrapper).get(0);
            }

        }
        return daUserCurve;
    }

}
