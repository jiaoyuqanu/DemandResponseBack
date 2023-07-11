package com.xqxy.dr.modular.strategy;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description
 * @ClassName DataAccessStrategyContext
 * @Author User
 * @date 2021.03.31 21:36
 */
@Component
public class DataAccessStrategyContext {

    private final Map<String, DataAccessStrategy> strategyMap = new ConcurrentHashMap<>();

    @Autowired
    public void stragegyInteface(Map<String, DataAccessStrategy> strategyMap) {
        this.strategyMap.clear();
        strategyMap.forEach(this.strategyMap::put);
        System.out.println(this.strategyMap);
    }


//    @Autowired
//    public void stragegyInteface2(List<DataAccessStrategy> strategyMap) {
//        strategyMap.forEach(System.out::println);
//    }

    public DataAccessStrategy strategySelect(String mode) {
        Preconditions.checkArgument(!StringUtils.isEmpty(mode), "不允许输入空字符串");
        return this.strategyMap.get(mode);
    }
}
