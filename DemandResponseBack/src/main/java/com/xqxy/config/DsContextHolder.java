package com.xqxy.config;

import com.xqxy.eum.DataSourceEnum;

/**
 * @Description
 * @ClassName DsContextHolder
 * @Author dongdawei
 * @date 2021.04.13 17:49
 */
public class DsContextHolder {
    private static final ThreadLocal<DataSourceEnum> contextHolder = new ThreadLocal<>();

    private DsContextHolder() throws Exception {
        throw new InstantiationException("无法实例化");
    }

    /**
     * 设置数据源标识
     */
    public static void setDataSourceType(DataSourceEnum dsEnum) {
        contextHolder.set(dsEnum);
    }

    /**
     * 获取当前数据源的标识
     */
    public static DataSourceEnum getDataSourceType() {
        return contextHolder.get();
    }

    /**
     * 清空数据源标识
     */
    public static void clearDataSourceType() {
        contextHolder.remove();
    }
}

