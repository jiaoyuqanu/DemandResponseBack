package com.xqxy.core.aop;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.xqxy.core.annotion.BusinessLog;
import com.xqxy.core.consts.AopSortConstant;
import com.xqxy.core.consts.CommonConstant;
import com.xqxy.core.log.LogManager;
import com.xqxy.core.log.factory.AppContextHolder;
import com.xqxy.core.pojo.login.CurrenUserInfo;
import com.xqxy.core.util.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;

/**
 * 业务日志aop切面
 *
 * @author xuyuxiang
 * @date 2020/3/20 11:47
 */
@Aspect
@Order(AopSortConstant.BUSINESS_LOG_AOP)
public class BusinessLogAop {

    private static final LogManager logManager = AppContextHolder
            .<LogManager>getBean(LogManager.class);

    /**
     * 日志切入点
     *
     * @author xuyuxiang
     * @date 2020/3/23 17:10
     */
    @Pointcut("@annotation(com.xqxy.core.annotion.BusinessLog)")
    private void getLogPointCut() {
    }

    /**
     * 操作成功返回结果记录日志
     *
     * @author xuyuxiang
     * @date 2020/3/20 11:51
     */
    @AfterReturning(pointcut = "getLogPointCut()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        BusinessLog businessLog = method.getAnnotation(BusinessLog.class);
        CurrenUserInfo sysLoginUser = SecurityUtils.getCurrentUserInfo();
        String account = CommonConstant.UNKNOWN;
        if (ObjectUtil.isNotNull(sysLoginUser)) {
            account = sysLoginUser.getId();
        }
        //异步记录日志
        logManager.executeOperationLog(
                businessLog, account, joinPoint, JSON.toJSONString(result));
    }

    /**
     * 操作发生异常记录日志
     *
     * @author xuyuxiang
     * @date 2020/3/21 11:38
     */
    @AfterThrowing(pointcut = "getLogPointCut()", throwing = "exception")
    public void doAfterThrowing(JoinPoint joinPoint, Exception exception) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        BusinessLog businessLog = method.getAnnotation(BusinessLog.class);
        CurrenUserInfo sysLoginUser = SecurityUtils.getCurrentUserInfo();
        String account = CommonConstant.UNKNOWN;
        if (ObjectUtil.isNotNull(sysLoginUser)) {
            account = sysLoginUser.getId();
        }
        //异步记录日志
        logManager.executeExceptionLog(
                businessLog, account, joinPoint, exception);
    }
}
