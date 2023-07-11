package com.xqxy.dr.modular.event.service.serviceFactory;

import com.xqxy.dr.modular.event.service.IAppealExamineService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * <p></p>
 *
 * @author djj Create on 2022/8/23
 * @version 1.0
 */
@Component
public class AppealExamineServiceBeanFactory implements BeanPostProcessor {

    private static final String USER = "user";
    private static final String CITY = "city";
    private static final String PROVINCE = "province";
    private static final String ENERGY = "energy";


    private static final Map<String,IAppealExamineService> examineAppealServiceMap = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        if (bean instanceof IAppealExamineService) {
            if ("cityAppealExamineService".equals(beanName)) {
                beanName = CITY;
            }
            if ("userAppealExamineService".equals(beanName)) {
                beanName = USER;
            }
            if ("provinceAppealExamineService".equals(beanName)) {
                beanName = PROVINCE;
            }
            if ("energyAppealExamineService".equals(beanName)) {
                beanName = ENERGY;
            }
            examineAppealServiceMap.put(beanName, (IAppealExamineService) bean);
        }
        return bean;
    }

    public static Map<String,IAppealExamineService> getAppealServiceMap(){
        return examineAppealServiceMap;
    }

}
