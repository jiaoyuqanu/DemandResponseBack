package com.xqxy.core.log.factory;

import cn.hutool.log.Log;
import com.xqxy.core.context.requestno.RequestNoContext;
import com.xqxy.sys.modular.log.entity.DcRequestLog;
import com.xqxy.sys.modular.log.service.DcRequestLogService;

import java.util.TimerTask;


/**
 * 日志操作任务创建工厂
 *
 * @author xuyuxiang
 * @date 2020/3/12 14:18
 */
public class RequestLogTaskFactory {

    private static final Log log = Log.get();

    private static final DcRequestLogService dcRequestLogService = AppContextHolder
            .<DcRequestLogService>getBean(DcRequestLogService.class);


    /**
     * 操作日志
     *
     * @author xuyuxiang
     * @date 2020/3/12 15:21
     */
    public static TimerTask requestLog(DcRequestLog dcRequestLog) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    dcRequestLogService.save(dcRequestLog);
                } catch (Exception e) {
                    log.error(">>> 创建请求日志异常，请求号为：{}，具体信息为：{}", RequestNoContext.get(), e.getMessage());
                }
            }
        };
    }

}
