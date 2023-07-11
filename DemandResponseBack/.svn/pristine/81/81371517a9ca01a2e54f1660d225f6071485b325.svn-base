package com.xqxy.core.log;

import cn.hutool.core.util.ObjectUtil;
import com.xqxy.core.annotion.BusinessLog;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.exception.enums.ServerExceptionEnum;
import com.xqxy.core.log.factory.LogFactory;
import com.xqxy.core.log.factory.LogTaskFactory;
import com.xqxy.core.util.HttpServletUtil;
import com.xqxy.core.util.UaUtil;
import com.xqxy.sys.modular.log.entity.SysOpLog;
import org.aspectj.lang.JoinPoint;
import org.springframework.scheduling.concurrent.ScheduledExecutorFactoryBean;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 日志管理器
 *
 * @author xuyuxiang
 * @date 2020/3/12 14:13
 */
@Component
public class LogManager {

    private final LogTaskFactory logTaskFactory;

    /**
     * 异步操作记录日志的线程池
     */
    private static final ScheduledThreadPoolExecutor EXECUTOR = new ScheduledThreadPoolExecutor(10, new ScheduledExecutorFactoryBean());

    private LogManager(LogTaskFactory logTaskFactory) {
        this.logTaskFactory = logTaskFactory;
    }

    /*private static final LogManager LOG_MANAGER = new LogManager(logTaskFactory);

    public static LogManager me() {
        return LOG_MANAGER;
    }*/

    /**
     * 异步执行日志的方法
     *
     * @author xuyuxiang
     * @date 2020/4/8 19:19
     */
    private void executeLog(TimerTask task) {

        //日志记录操作延时
        int operateDelayTime = 10;
        EXECUTOR.schedule(task, operateDelayTime, TimeUnit.MILLISECONDS);
    }

    /**
     * 操作日志
     *
     * @author xuyuxiang
     * @date 2020/3/18 20:01
     */
    public void executeOperationLog(BusinessLog businessLog, final String account, JoinPoint joinPoint, final String result) {
        SysOpLog sysOpLog = this.genBaseSysOpLog();
        TimerTask timerTask = logTaskFactory.operationLog(sysOpLog, account, businessLog, joinPoint, result);
        executeLog(timerTask);
    }

    /**
     * 异常日志
     *
     * @author xuyuxiang
     * @date 2020/3/18 20:01
     */
    public void executeExceptionLog(BusinessLog businessLog, final String account, JoinPoint joinPoint, Exception exception) {
        SysOpLog sysOpLog = this.genBaseSysOpLog();
        TimerTask timerTask = logTaskFactory.exceptionLog(sysOpLog, account, businessLog, joinPoint, exception);
        executeLog(timerTask);
    }

    /**
     * 构建基础访问日志
     *
     * @author xuyuxiang
     * @date 2020/3/19 14:44
     */
    /*private SysVisLog genBaseSysVisLog() {
        HttpServletRequest request = HttpServletUtil.getRequest();
        if (ObjectUtil.isNotNull(request)) {
            String ip = IpAddressUtil.getIp(request);
            String address = IpAddressUtil.getAddress(request);
            String browser = UaUtil.getBrowser(request);
            String os = UaUtil.getOs(request);
            return LogFactory.genBaseSysVisLog(ip, address, browser, os);
        } else {
            throw new ServiceException(ServerExceptionEnum.REQUEST_EMPTY);
        }
    }*/

    /**
     * 构建基础操作日志
     *
     * @author xuyuxiang
     * @date 2020/3/19 14:44
     */
    private SysOpLog genBaseSysOpLog() {
        HttpServletRequest request = HttpServletUtil.getRequest();
        if (ObjectUtil.isNotNull(request)) {
            String ip = "";//IpAddressUtil.getIp(request);
            String address = "";//IpAddressUtil.getAddress(request);
            String browser = UaUtil.getBrowser(request);
            String os = UaUtil.getOs(request);
            String url = request.getRequestURI();
            String method = request.getMethod();
            return LogFactory.genBaseSysOpLog(ip, address, browser, os, url, method);
        } else {
            throw new ServiceException(ServerExceptionEnum.REQUEST_EMPTY);
        }
    }

}
