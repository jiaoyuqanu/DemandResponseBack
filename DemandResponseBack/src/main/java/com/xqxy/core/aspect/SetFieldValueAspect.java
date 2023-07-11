package com.xqxy.core.aspect;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.core.pojo.page.PageResult;
import com.xqxy.core.util.BeanUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @Description
 * @ClassName SetFieldValueAspect
 * @Author User
 * @date 2021.03.02 12:40
 */
@Component
@Aspect
public class SetFieldValueAspect {

    @Autowired
    BeanUtil beanUtil;

    @Around("@annotation(com.xqxy.core.annotion.NeedSetValueField)")
    public Object doSetFieldValue(ProceedingJoinPoint pjp) throws Throwable {
        Object ret = pjp.proceed();
        if (ret.getClass().equals(Page.class)) {
            if (((Page) ret).getRecords().size() > 0) {
                this.beanUtil.setFieldValueForCol(((Page) ret).getRecords());
            }

        } else if (ret.getClass().equals(ArrayList.class)) {
            if (((Collection) ret).size() > 0) {
                this.beanUtil.setFieldValueForCol((Collection) ret);
            }

        } else if (ObjectUtil.isNotNull(ret)) {
            this.beanUtil.setFieldValue(ret);
        }
//        this.beanUtil.setFieldValueForCol((Collection) ret);
        return ret;

    }

}
