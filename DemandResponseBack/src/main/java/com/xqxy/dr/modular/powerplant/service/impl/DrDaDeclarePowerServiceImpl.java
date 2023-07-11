package com.xqxy.dr.modular.powerplant.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.dr.modular.powerplant.entity.DrDaDeclarePower;
import com.xqxy.dr.modular.powerplant.entity.DrDaVoidancePower;
import com.xqxy.dr.modular.powerplant.mapper.DrDaDeclarePowerMapper;
import com.xqxy.dr.modular.powerplant.param.DrDaVoidanceParam;
import com.xqxy.dr.modular.powerplant.service.DrDaDeclarePowerService;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 申报功率曲线 服务实现类
 * </p>
 *
 * @author liqirui
 * @since 2021-12-09
 */
@Service
public class DrDaDeclarePowerServiceImpl extends ServiceImpl<DrDaDeclarePowerMapper, DrDaDeclarePower> implements DrDaDeclarePowerService {

    @Override
    public Map<String, Object> getDeclarePower(DrDaVoidanceParam param) {
        LambdaQueryWrapper<DrDaDeclarePower> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotEmpty(param.getId())) {
            queryWrapper.eq(DrDaDeclarePower::getVoidanceId, param.getId());
        }
        DrDaDeclarePower baseLineDetailData = null;
        List<DrDaDeclarePower> baseLineDetailDatas = this.list(queryWrapper);
        if(null!=baseLineDetailDatas && baseLineDetailDatas.size()>0) {
            baseLineDetailData = baseLineDetailDatas.get(0);
        }
        Map<String, Object> data = new HashMap<>();
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
                    if (!"serialVersionUID".equals(name) && !"id".equals(name) && !"voidanceId".equals(name)) {
                        list.add(map);
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        } else {
            for(int i=1;i<=96;i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("p"+i,"");
                list.add(map);
            }
        }
        data.put("list",list);
        return data;
    }
}
