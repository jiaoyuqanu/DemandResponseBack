package com.xqxy.core.log;

import com.xqxy.core.log.factory.RequestLogTaskFactory;
import com.xqxy.sys.modular.log.entity.DcRequestLog;
import org.springframework.scheduling.concurrent.ScheduledExecutorFactoryBean;

import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 请求日志管理器
 *
 * @author xiao jun
 * @date 2020/3/12 14:13
 */
public class RequestLogManager {

    /**
     * 异步操作记录日志的线程池
     */
    private static final ScheduledThreadPoolExecutor EXECUTOR = new ScheduledThreadPoolExecutor(10, new ScheduledExecutorFactoryBean());

    private RequestLogManager() {
    }

    private static final RequestLogManager REQUEST_LOG_MANAGER = new RequestLogManager();

    public static RequestLogManager me() {
        return REQUEST_LOG_MANAGER;
    }

    /**
     * 异步执行日志的方法
     *
     * @author xiao jun
     * @date 2021/4/8 19:19
     */
    private void executeLog(TimerTask task) {

        //日志记录操作延时
        int operateDelayTime = 10;
        EXECUTOR.schedule(task, operateDelayTime, TimeUnit.MILLISECONDS);
    }

    /**
     * 请求日志
     *
     * @author xiao jun
     * @date 2021/3/18 20:01
     */
    public void executeRequestLog(String requestUrl, String requestParam, String responseData) {
        DcRequestLog dcRequestLog = new DcRequestLog();
        dcRequestLog.setRequestUrl(requestUrl);
        dcRequestLog.setRequestParam(requestParam);
        dcRequestLog.setResponseData(responseData);
        TimerTask timerTask = RequestLogTaskFactory.requestLog(dcRequestLog);
        executeLog(timerTask);
    }


}
