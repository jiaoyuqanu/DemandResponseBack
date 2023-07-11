package com.xqxy.core.log.factory;

import cn.hutool.log.Log;
import com.xqxy.core.annotion.BusinessLog;
import com.xqxy.core.context.requestno.RequestNoContext;
import com.xqxy.sys.modular.log.entity.SysOpLog;
import com.xqxy.sys.modular.log.service.SysOpLogService;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;

import java.util.TimerTask;


/**
 * 日志操作任务创建工厂
 *
 * @author xuyuxiang
 * @date 2020/3/12 14:18
 */
@Component
public class LogTaskFactory {

    private static final Log log = Log.get();

    //private static final SysOpLogService sysOpLogService = SpringUtil.getBean(SysOpLogService.class);

    private final SysOpLogService sysOpLogService;

    public LogTaskFactory(SysOpLogService sysOpLogService) {
        this.sysOpLogService = sysOpLogService;
    }

    /**
     * 操作日志
     *
     * @author xuyuxiang
     * @date 2020/3/12 15:21
     */
    public TimerTask operationLog(SysOpLog sysOpLog, String account, BusinessLog businessLog, JoinPoint joinPoint, String result) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    LogFactory.createSysOperationLog(sysOpLog, account, businessLog, joinPoint, result);
                    sysOpLogService.save(sysOpLog);
                } catch (Exception e) {
                    log.error(">>> 创建操作日志异常，请求号为：{}，具体信息为：{}", RequestNoContext.get(), e.getMessage());
                }
            }
        };
    }

    /**
     * 异常日志
     *
     * @author xuyuxiang
     * @date 2020/3/12 15:21
     */
    public TimerTask exceptionLog(SysOpLog sysOpLog, String account, BusinessLog businessLog, JoinPoint joinPoint, Exception exception) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    LogFactory.createSysExceptionLog(sysOpLog, account, businessLog, joinPoint, exception);
                    sysOpLogService.save(sysOpLog);
                } catch (Exception e) {
                    log.error(">>> 创建异常日志异常，请求号为：{}，具体信息为：{}", RequestNoContext.get(), e.getMessage());
                }
            }
        };
    }
}
