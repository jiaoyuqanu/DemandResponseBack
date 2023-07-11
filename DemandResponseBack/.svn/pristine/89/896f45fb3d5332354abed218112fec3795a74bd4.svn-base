
package com.xqxy.dr.modular.upload.jonhandler;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class UploadJob {

    private String uploadStrategyValue;

    @Resource
    private UploadStrategyContext uploadStrategyContext;


    /**
     * 补贴发放记录
     */
    @XxlJob("subsidyPayment")
    public ReturnT<String> SubsidyPayment(String param) throws Exception {

        XxlJobLogger.log("补贴发放记录");
        UploadStrategy uploadStrategy = uploadStrategyContext.strategySelect(uploadStrategyValue);
        uploadStrategy.generateConSubsidy();
        return ReturnT.SUCCESS;
    }

    /**
     * 需求响应用户
     */
    @XxlJob("user")
    public ReturnT<String> user(String param) throws Exception {

        XxlJobLogger.log("需求响应用户");
        UploadStrategy uploadStrategy = uploadStrategyContext.strategySelect(uploadStrategyValue);
        uploadStrategy.generateUser();
        return ReturnT.SUCCESS;
    }

    /**
     * 用户年度响应能力
     */
    @XxlJob("ability")
    public ReturnT<String> ability(String param) throws Exception {

        XxlJobLogger.log("用户年度响应能力");
        UploadStrategy uploadStrategy = uploadStrategyContext.strategySelect(uploadStrategyValue);
        uploadStrategy.generateAbility();
        return ReturnT.SUCCESS;
    }


    /**
     * 需求响应事件
     */
    @XxlJob("incident")
    public ReturnT<String> incident(String param) throws Exception {

        XxlJobLogger.log("需求响应事件");
        UploadStrategy uploadStrategy = uploadStrategyContext.strategySelect(uploadStrategyValue);
        uploadStrategy.generateIncident();
        return ReturnT.SUCCESS;
    }


    /**
     * 事件执行评价
     */
    @XxlJob("evluate")
    public ReturnT<String> evluate(String param) throws Exception {

        XxlJobLogger.log("事件执行评价");
        UploadStrategy uploadStrategy =  uploadStrategyContext.strategySelect(uploadStrategyValue);
        uploadStrategy.generateEvluate();
        return ReturnT.SUCCESS;
    }
}

