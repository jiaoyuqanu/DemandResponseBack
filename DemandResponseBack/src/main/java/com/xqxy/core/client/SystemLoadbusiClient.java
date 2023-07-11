package com.xqxy.core.client;

import com.alibaba.fastjson.JSONObject;
import com.xqxy.dr.modular.project.params.LoadbusiConsParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name="znyd-loadbusi-service")
@Component
public interface SystemLoadbusiClient {

    /**
     * 根据户号获取支路信息
     *
     * @param map
     * @return
     */
    @PostMapping(value = "/devBranch/getBranchByConNo")
    JSONObject getBranchByConNo(@RequestBody Map<String,Object> map);

    /**
     * 访问负控 服务接口  获取用户对应的 负荷
     * @param loadbusiConsParam
     * @return
     */
    @PostMapping(value = "/devBranch/getConsInfoByConsId")
    JSONObject getConsInfoByConsId(@RequestBody LoadbusiConsParam loadbusiConsParam);
}
