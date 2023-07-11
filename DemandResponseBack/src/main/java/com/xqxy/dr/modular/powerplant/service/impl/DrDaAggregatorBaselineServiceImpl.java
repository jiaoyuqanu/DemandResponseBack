package com.xqxy.dr.modular.powerplant.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.dr.modular.powerplant.entity.DrDaAggregatorBaseline;
import com.xqxy.dr.modular.powerplant.entity.DrDaAggregatorPrice;
import com.xqxy.dr.modular.powerplant.mapper.DrDaAggregatorBaselineMapper;
import com.xqxy.dr.modular.powerplant.param.DrDaAggregatorBaseLineParam;
import com.xqxy.dr.modular.powerplant.param.DrDaAggregatorPriceAddParam;
import com.xqxy.dr.modular.powerplant.service.DrDaAggregatorBaselineService;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 负荷聚合商次日负荷基线 服务实现类
 * </p>
 *
 * @author czj
 * @since 2021-12-09
 */
@Service
public class DrDaAggregatorBaselineServiceImpl extends ServiceImpl<DrDaAggregatorBaselineMapper, DrDaAggregatorBaseline> implements DrDaAggregatorBaselineService {

    @Override
    public Map<String,Object> getDrDaAggregatorBaseline(DrDaAggregatorBaseLineParam param) {
        LambdaQueryWrapper<DrDaAggregatorBaseline> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotEmpty(param.getAggregatorNo())) {
            queryWrapper.eq(DrDaAggregatorBaseline::getAggregatorNo, param.getAggregatorNo());
        }

        if (ObjectUtil.isNotEmpty(param.getMeasureDate())) {
            queryWrapper.like(DrDaAggregatorBaseline::getMeasureDate, param.getMeasureDate());
        }
        DrDaAggregatorBaseline baseLineDetailData = this.getOne(queryWrapper);
        Map<String,Object> data = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        //通过反射获取类中所有属性
        if(null!=baseLineDetailData) {
            Field[] fields = baseLineDetailData.getClass().getDeclaredFields();
            for (Field field : fields) {
                //设置允许反射访问私有变量
                field.setAccessible(true);
                try {
                    //获取属性值
                    Object value = field.get(baseLineDetailData);
                    //获取属性名
                    String name = field.getName();
                    if (null == value) {
                        value = "";
                    } else {
                        value=value.toString();
                    }
                    Map<String, Object> map = new HashMap<>();
                    map.put("key", name);
                    map.put("value", value);
                    if (!"serialVersionUID".equals(name) && !"id".equals(name) && !"measureDate".equals(name)
                            && !"aggregatorNo".equals(name) && !"status".equals(name)) {
                        list.add(map);
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            }
            data.put("list",list);
            data.put("id",baseLineDetailData.getId());
            data.put("status",baseLineDetailData.getStatus());
            data.put("aggregatorNo",baseLineDetailData.getAggregatorNo());
            data.put("measureDate",baseLineDetailData.getMeasureDate());
        } else {
            for(int i=1;i<=96;i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("p"+i,"");
                list.add(map);
            }
            data.put("list",list);
            data.put("id","");
            data.put("status","");
            data.put("aggregatorNo","");
            data.put("measureDate","");
        }
        return data;
    }

    @Override
    public void submitBaseLine(DrDaAggregatorPriceAddParam param) {
        DrDaAggregatorBaseline drDaAggregatorBaseline = new DrDaAggregatorBaseline();
        drDaAggregatorBaseline.setId(param.getId());
        if(null!=param.getId()) {
            drDaAggregatorBaseline.setStatus("1");
            this.updateById(drDaAggregatorBaseline);
        }
    }
}
