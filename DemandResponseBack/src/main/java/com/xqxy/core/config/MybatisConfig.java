
package com.xqxy.core.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import com.xqxy.config.NdsDialect;
import com.xqxy.core.mybatis.CustomMetaObjectHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis扩展插件配置
 *
 * @author xiao jun, fengshuonan
 * @date 2020/3/18 10:49
 */
@Configuration
@MapperScan(basePackages = {"com.xqxy.**.mapper"})
public class MybatisConfig {

    /**
     * mybatis-plus分页插件
     *
     * @author xiao jun
     * @date 2020/3/31 15:42
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        //解除分页只能每页查询500条的限制
        paginationInterceptor.setLimit(-1);
        paginationInterceptor.setDialect(new NdsDialect());
        paginationInterceptor.setDialectType("mysql");
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
        return paginationInterceptor;
    }

//    /**
//     * 自定义公共字段自动注入
//     *
//     * @author xiao jun
//     * @date 2020/3/31 15:42
//     */
//    @Bean
//    public MetaObjectHandler metaObjectHandler() {
//        return new CustomMetaObjectHandler();
//    }


}
