package com.xqxy.core.client;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "file-service")
@Component
public interface FileClient {

    @PostMapping("/file/file-info/download")
    Response download(@RequestBody FileReq fileReq);

}
