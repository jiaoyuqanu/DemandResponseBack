package com.xqxy.core.log.factory;

import cn.hutool.core.date.DateTime;
import com.xqxy.core.annotion.BusinessLog;
import com.xqxy.core.enums.LogSuccessStatusEnum;
import com.xqxy.core.util.JoinPointUtil;
import com.xqxy.sys.modular.log.entity.SysOpLog;
import org.aspectj.lang.JoinPoint;

import java.util.Arrays;

/**
 * 日志对象创建工厂
 *
 * @author xuyuxiang
 * @date 2020/3/12 14:31
 */
public class LogFactory {

    /**
     * 创建操作日志
     *
     * @author xuyuxiang
     * @date 2020/3/12 16:09
     */
    static void createSysOperationLog(SysOpLog sysOpLog, String account, BusinessLog businessLog, JoinPoint joinPoint, String result) {
        fillCommonSysOpLog(sysOpLog, account, businessLog, joinPoint);
        sysOpLog.setSuccess(LogSuccessStatusEnum.SUCCESS.getCode());
        sysOpLog.setResult(result);
        sysOpLog.setMessage(LogSuccessStatusEnum.SUCCESS.getMessage());
    }

    /**
     * 创建异常日志
     *
     * @author xuyuxiang
     * @date 2020/3/16 17:18
     */
    static void createSysExceptionLog(SysOpLog sysOpLog, String account, BusinessLog businessLog, JoinPoint joinPoint, Exception exception) {
        fillCommonSysOpLog(sysOpLog, account, businessLog, joinPoint);
        sysOpLog.setSuccess(LogSuccessStatusEnum.FAIL.getCode());
        sysOpLog.setMessage(Arrays.toString(exception.getStackTrace()));
    }

    /**
     * 生成通用操作日志字段
     *
     * @author xuyuxiang
     * @date 2020/3/16 17:20
     */
    private static void fillCommonSysOpLog(SysOpLog sysOpLog, String account, BusinessLog businessLog, JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getName();

        String methodName = joinPoint.getSignature().getName();

        String param = JoinPointUtil.getArgsJsonString(joinPoint);

        sysOpLog.setName(businessLog.title());
        sysOpLog.setOpType(businessLog.opType().ordinal());
        sysOpLog.setClassName(className);
        sysOpLog.setMethodName(methodName);
        sysOpLog.setParam(param);
        sysOpLog.setOpTime(DateTime.now());
        sysOpLog.setAccount(account);
    }

    /**
     * 构建基础操作日志
     *
     * @author xuyuxiang
     * @date 2020/3/19 14:36
     */
    public static SysOpLog genBaseSysOpLog(String ip, String location, String browser, String os, String url, String method) {
        SysOpLog sysOpLog = new SysOpLog();
        sysOpLog.setIp(ip);
        sysOpLog.setLocation(location);
        sysOpLog.setBrowser(browser);
        sysOpLog.setOs(os);
        sysOpLog.setUrl(url);
        sysOpLog.setReqMethod(method);
        return sysOpLog;
    }

}
