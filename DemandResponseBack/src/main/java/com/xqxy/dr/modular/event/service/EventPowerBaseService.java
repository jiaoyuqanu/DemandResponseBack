package com.xqxy.dr.modular.event.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.event.entity.Event;
import com.xqxy.dr.modular.event.entity.EventPower;
import com.xqxy.dr.modular.event.entity.EventPowerBase;
import com.xqxy.dr.modular.event.param.ChangePowerBaseLineParam;
import com.xqxy.dr.modular.event.param.EventParam;
import com.xqxy.dr.modular.event.result.EventPowerResult;

import java.util.Map;

/**
 * <p>
 * 事件执行曲线 服务类
 * </p>
 *
 * @author Shen
 * @since 2021-10-26
 */
public interface EventPowerBaseService extends IService<EventPowerBase> {


    void changePowerBaseLine(ChangePowerBaseLineParam changePowerBaseLineParam);
}
