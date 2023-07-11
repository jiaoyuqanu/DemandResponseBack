package com.xqxy.core.config;

import com.xqxy.core.aop.BusinessLogAop;
import com.xqxy.core.aop.WrapperAop;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 切面配置
 *
 * @author xuyuxiang
 * @date 2020/3/18 11:25
 */
@Configuration
public class AopConfig {

    /**
     * 日志切面
     *
     * @author xuyuxiang
     * @date 2020/3/20 14:10
     */
    @Bean
    public BusinessLogAop businessLogAop() {
        return new BusinessLogAop();
    }

    /**
     * 结果包装的aop
     *
     * @author fengshuonan
     * @date 2020/7/24 22:18
     */
    @Bean
    public WrapperAop wrapperAop() {
        return new WrapperAop();
    }
}
