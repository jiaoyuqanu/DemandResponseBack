package com.xqxy.rentation;


import com.xqxy.eum.DataSourceEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description
 * @ClassName DsSwitcher
 * @Author dongdawei
 * @date 2021.04.13 17:49
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface DsSwitcher {
    //默认数据源是mysql
    DataSourceEnum value() default DataSourceEnum.MYSQL;
}
