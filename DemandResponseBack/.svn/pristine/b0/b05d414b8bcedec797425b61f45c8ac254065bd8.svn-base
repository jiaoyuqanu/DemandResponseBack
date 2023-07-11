package com.xqxy.core.annotion;

import com.xqxy.core.enums.SensitiveTypeEnum;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Desensitized {
    //    脱敏类型(规则)
    SensitiveTypeEnum type();
    String isEffictiveMethod() default "";
}
