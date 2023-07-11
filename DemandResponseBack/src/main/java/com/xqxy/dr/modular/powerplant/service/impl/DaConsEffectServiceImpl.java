package com.xqxy.dr.modular.powerplant.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.poi.POIExcelUtil;
import com.xqxy.core.util.HttpServletUtil;
import com.xqxy.dr.modular.powerplant.entity.DaConsEffect;
import com.xqxy.dr.modular.powerplant.mapper.DaConsEffectMapper;
import com.xqxy.dr.modular.powerplant.param.DaParam;
import com.xqxy.dr.modular.powerplant.service.DaConsEffectService;
import com.xqxy.dr.modular.powerplant.service.DaUserCurveService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Caoj
 * @since 2021-12-08
 */
@Service
public class DaConsEffectServiceImpl extends ServiceImpl<DaConsEffectMapper, DaConsEffect> implements DaConsEffectService {

    @Resource
    DaUserCurveService daUserCurveService;
    @Override
    public Page<DaConsEffect> page(DaParam daParam) {
        LambdaQueryWrapper<DaConsEffect> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DaConsEffect::getCustId, daParam.getId());
        return page(new Page<>(daParam.getCurrent(), daParam.getSize()), lambdaQueryWrapper);
    }

    /**
     * 代理用户效果导出
     *
     * @param daParam 取custId客户标识
     * @date 12/21/2021 14:26
     * @author Caoj
     */
    @Override
    public void consEffectExport(DaParam daParam) {
        LambdaQueryChainWrapper<DaConsEffect> lambdaQueryChainWrapper = lambdaQuery();
        List<DaConsEffect> daConsEffects = lambdaQueryChainWrapper.eq(DaConsEffect::getCustId, daParam.getCustId()).list();
        String[] titleRow = {"用户编号", "用户名称", "所属供电单位", "响应电量(kWh)", "激励费用(元)"};
        String[][] dataRows = new String[daConsEffects.size()][titleRow.length];
        for (int i = 0; i < daConsEffects.size(); i++) {
            dataRows[i][0] = daConsEffects.get(i).getId().toString();
            dataRows[i][1] = daConsEffects.get(i).getConsName();
            dataRows[i][2] = daConsEffects.get(i).getOrgName();
            dataRows[i][3] = daConsEffects.get(i).getResponseElect().toString();
            dataRows[i][4] = daConsEffects.get(i).getUrgeExpense().toString();
        }
        POIExcelUtil.generatorExcel("收益信息清单", "代理用户效果信息清单导出", "sheet1",
                titleRow, dataRows, HttpServletUtil.getResponse());
    }

    @Override
    public DaConsEffect curve(DaParam daParam)
    {

        DaConsEffect daConsEffect=new DaConsEffect();
        daConsEffect=this.getById(daParam.getId());
        if(ObjectUtil.isNotNull(daConsEffect))
        {
            daConsEffect.setDaUserCurve(daUserCurveService.getByConsNo(daParam.getId()));
        }
        else
        {
            daConsEffect=new DaConsEffect();
        }
        return daConsEffect;
    }
}
