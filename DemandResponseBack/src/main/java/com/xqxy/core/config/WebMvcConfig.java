
package com.xqxy.core.config;

import cn.hutool.log.Log;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * web配置
 *
 * @author stylefeng
 * @date 2020/4/11 10:23
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private static final Log log = Log.get();

    /**
     * DateTime格式化字符串
     */
    private String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * Date格式化字符串
     */
    private String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

    /**
     * Time格式化字符串
     */
    private String DEFAULT_TIME_PATTERN = "HH:mm:ss";

    /**
     * 请求唯一编号生成器
     *
     * @author stylefeng
     * @date 2020/6/21 10:30
     */
//    @Bean
//    public FilterRegistrationBean<RequestNoFilter> requestNoFilterFilterRegistrationBean() {
//        FilterRegistrationBean<RequestNoFilter> registration = new FilterRegistrationBean<>(new RequestNoFilter());
//        registration.addUrlPatterns("/*");
//        return registration;
//    }


    /**
     * json自定义序列化工具,long转string
     *
     * @author stylefeng
     * @date 2020/5/28 14:48
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATETIME_PATTERN);
        return jacksonObjectMapperBuilder ->
                jacksonObjectMapperBuilder
                        .serializerByType(Long.class, ToStringSerializer.instance)
                        .serializerByType(Long.TYPE, ToStringSerializer.instance)
                        .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATETIME_PATTERN)))
                        .serializerByType(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN)))
                        .serializerByType(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_PATTERN)))
                        .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATETIME_PATTERN)))
                        .deserializerByType(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN)))
                        .deserializerByType(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_PATTERN)));
//                ;
//                        .serializerByType(LocalDate.class, new LocalDateSerializer(formatter))
//                        .deserializerByType(LocalDate.class, new LocalDateDeserializer(formatter));
    }

}
