package com.xqxy.core.util;

import cn.hutool.core.util.StrUtil;
import com.xqxy.core.annotion.NeedSetValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @ClassName BeanUtil
 * @Author User
 * @date 2021.03.02 13:12
 */
@Component
public class BeanUtil implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setFieldValueForCol(Collection col) throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Class<?> clazz = col.iterator().next().getClass();
        Field[] fields = clazz.getDeclaredFields();
        Map<String, Object> cache = new HashMap<>();
        for (Field needField : fields) {
            NeedSetValue sv = needField.getAnnotation(NeedSetValue.class);
            if (sv == null) {
                continue;
            }
            needField.setAccessible(true);
            Object bean = this.applicationContext.getBean(sv.beanClass());

            String[] params = sv.params();
            Class<?>[] parameterTypes = new Class<?>[params.length];

            for (int i = 0; i < params.length; i++) {
                try {
                    parameterTypes[i] = clazz.getDeclaredField(params[i]).getType();
                } catch (NoSuchFieldException e) {
                    parameterTypes[i] = clazz.getSuperclass().getDeclaredField(params[i]).getType();
                }
                //parameterTypes[i] = clazz.getDeclaredField(params[i]).getType();
            }
            Method method = sv.beanClass().getMethod(sv.method(), parameterTypes);

            Field[] paramFields = new Field[params.length];
            for (int i = 0; i < params.length; i++) {
                try {
                    paramFields[i] = clazz.getDeclaredField(params[i]);
                } catch (NoSuchFieldException e) {
                    paramFields[i] = clazz.getSuperclass().getDeclaredField(params[i]);
                }
                // paramFields[i] = clazz.getDeclaredField(params[i]);
                paramFields[i].setAccessible(true);
            }
//            Field paramField = clazz.getDeclaredField(sv.param());
//            paramField.setAccessible(true);

            Field targetField = null;
            Boolean needInnerField = StrUtil.isEmpty(sv.targetField());

            String keyPrefix = sv.beanClass() + "-" + sv.method() + "-" + sv.targetField() + "-";

            for (Object obj : col) {
//                Object paramValue = paramField.get(obj);
                Object[] paramValues = new Object[params.length];
                for (int i = 0; i < params.length; i++) {
                    paramValues[i] = paramFields[i].get(obj);
                }
                if (paramValues == null)
                    continue;
                Object value = null;
                String key = keyPrefix + StringUtils.join(paramValues, ",");
                ;
                if (cache.containsKey(key)) {
                    value = cache.get(key);
                } else {
                    value = method.invoke(bean, paramValues);
                    if (needInnerField) {
                        if (value != null) {
                            if (targetField == null) {
                                targetField = value.getClass().getDeclaredField(sv.targetField());
                                targetField.setAccessible(true);
                            }
                            value = targetField.get(value);
                        }

                    }
                    cache.put(key, value);
                }
                needField.set(obj, value);

            }


        }


    }


    public void setFieldValue(Object obj) throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Map<String, Object> cache = new HashMap<>();
        for (Field needField : fields) {
            NeedSetValue sv = needField.getAnnotation(NeedSetValue.class);
            if (sv == null) {
                continue;
            }
            needField.setAccessible(true);
            Object bean = this.applicationContext.getBean(sv.beanClass());

            String[] params = sv.params();
            Class<?>[] parameterTypes = new Class<?>[params.length];

            for (int i = 0; i < params.length; i++) {
                try {
                    parameterTypes[i] = clazz.getDeclaredField(params[i]).getType();
                } catch (NoSuchFieldException e) {
                    parameterTypes[i] = clazz.getSuperclass().getDeclaredField(params[i]).getType();
                }
                //parameterTypes[i] = clazz.getDeclaredField(params[i]).getType();
            }
            Method method = sv.beanClass().getMethod(sv.method(), parameterTypes);

            Field[] paramFields = new Field[params.length];
            for (int i = 0; i < params.length; i++) {
                try {
                    paramFields[i] = clazz.getDeclaredField(params[i]);
                } catch (NoSuchFieldException e) {
                    paramFields[i] = clazz.getSuperclass().getDeclaredField(params[i]);
                }
                // paramFields[i] = clazz.getDeclaredField(params[i]);
                paramFields[i].setAccessible(true);
            }
//            Field paramField = clazz.getDeclaredField(sv.param());
//            paramField.setAccessible(true);

            Field targetField = null;
            Boolean needInnerField = StrUtil.isEmpty(sv.targetField());

            String keyPrefix = sv.beanClass() + "-" + sv.method() + "-" + sv.targetField() + "-";

            Object[] paramValues = new Object[params.length];
            for (int i = 0; i < params.length; i++) {
                paramValues[i] = paramFields[i].get(obj);
            }
            if (paramValues == null)
                continue;
            Object value = null;
            String key = keyPrefix + StringUtils.join(paramValues, ",");
            ;
            if (cache.containsKey(key)) {
                value = cache.get(key);
            } else {
                value = method.invoke(bean, paramValues);
                if (needInnerField) {
                    if (value != null) {
                        if (targetField == null) {
                            targetField = value.getClass().getDeclaredField(sv.targetField());
                            targetField.setAccessible(true);
                        }
                        value = targetField.get(value);
                    }

                }
                cache.put(key, value);
            }
            needField.set(obj, value);


        }
    }
}
