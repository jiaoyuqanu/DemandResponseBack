package com.xqxy.dr.modular.upload.jonhandler;

import com.google.common.base.Preconditions;
import com.xqxy.dr.modular.strategy.CalculateStrategy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description
 * @ClassName CalculateStrategyContext
 * @Author UserMapper
 * @date 2021.03.31 21:36
 */
@Component
public class UploadStrategyContext {

    private final Map<String, UploadStrategy> strategyMap = new ConcurrentHashMap<>();

    @Autowired
    public void stragegyInteface(Map<String, UploadStrategy> strategyMap) {
        this.strategyMap.clear();
        strategyMap.forEach(this.strategyMap::put);
    }


    public UploadStrategy strategySelect(String mode) {
        Preconditions.checkArgument(!StringUtils.isEmpty(mode), "不允许输入空字符串");
        return this.strategyMap.get(mode);
    }
}
