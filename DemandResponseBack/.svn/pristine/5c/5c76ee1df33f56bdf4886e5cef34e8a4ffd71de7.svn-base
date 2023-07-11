package com.xqxy.core.client;

import com.xqxy.sys.modular.cust.param.BusConfigParam;
import com.xqxy.sys.modular.cust.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Author: zgy
 * Date: 2021/11/30 12:02
 * Content: 绿色国网交互服务接口
 */
@FeignClient(name="SyncOrderStatusService")
@Component
public interface SyncOrderStatusClient {
    @PostMapping(value="rest/workorder/province/syncOrderStatus")
    public String syncOrderStatus(@RequestBody GrsgOrder GrsgOrder);
}
