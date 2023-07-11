package com.xqxy.dr.modular.event.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.dr.modular.event.entity.EventPowerBase;
import com.xqxy.dr.modular.event.mapper.EventPowerBaseMapper;
import com.xqxy.dr.modular.event.param.ChangePowerBaseLineParam;
import com.xqxy.dr.modular.event.service.EventPowerBaseService;
import lombok.extern.log4j.Log4j2;
import org.docx4j.wml.P;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 事件执行曲线 服务实现类
 * </p>
 *
 * @author Shen
 * @since 2021-10-26
 */
@Log4j2
@Service
public class EventPowerBaseServiceImpl extends ServiceImpl<EventPowerBaseMapper, EventPowerBase> implements EventPowerBaseService {

    private static final Map<String, Method> methodCache = new ConcurrentHashMap<>();

    static {
        for (int i = 1; i <= 96; i++) {
            try {
                Method getMethod = EventPowerBase.class.getMethod("getP" + i);
                getMethod.setAccessible(true);
                Method setMethod = EventPowerBase.class.getMethod("setP" + i, BigDecimal.class);
                setMethod.setAccessible(true);
                methodCache.put("getP" + i, getMethod);
                methodCache.put("setP" + i, setMethod);
            } catch (Exception e) {

            }

        }
    }

    @Override
    public void changePowerBaseLine(ChangePowerBaseLineParam changePowerBaseLineParam) {
        Assert.isTrue(ObjectUtil.isNotEmpty(changePowerBaseLineParam.getEventId()), "事件id不能为空");
        Assert.isTrue(ObjectUtil.isNotEmpty(changePowerBaseLineParam.getOrgId()), "组织机构id不能为空");
        Map<String, BigDecimal> pValueMap = new HashMap<>();
        if (changePowerBaseLineParam.getPValueMap() != null) {
            pValueMap = changePowerBaseLineParam.getPValueMap();
        }

        LambdaQueryWrapper<EventPowerBase> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(EventPowerBase::getEventId, changePowerBaseLineParam.getEventId());
        lambdaQueryWrapper.eq(EventPowerBase::getOrgNo, changePowerBaseLineParam.getOrgId());
        List<EventPowerBase> eventPowerBases = getBaseMapper().selectList(lambdaQueryWrapper);
        if (ObjectUtil.isEmpty(eventPowerBases)) {
            throw new ServiceException(10, "无对应的基线");
        }
        EventPowerBase eventPowerBase = eventPowerBases.get(0);
        for (int i = 1; i <= 96; i++) {
            try {
                BigDecimal value = (BigDecimal) methodCache.get("getP" + i).invoke(eventPowerBase);
                if (value != null) {
                    BigDecimal bigDecimal = pValueMap.get("p" + i);
                    if (bigDecimal != null) {
                        methodCache.get("setP" + i).invoke(eventPowerBase, bigDecimal);
                    } else if (changePowerBaseLineParam.getAllPFixValue() != null) {
                        methodCache.get("setP" + i).invoke(eventPowerBase, value.add(changePowerBaseLineParam.getAllPFixValue()));
                    }
                }
            } catch (Exception e) {

            }
        }
        this.saveOrUpdate(eventPowerBase);
    }
}
