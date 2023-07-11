package com.xqxy.dr.modular.newloadmanagement.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author Rabbit
 * @Date 2022/6/10 20:29
 */
@ApiModel(description = "rest请求的返回模型，所有rest正常都返回该类的对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResult<T> {
    @ApiModelProperty(value = "处理结果code,200 成功,", required = true)
    private Integer code;
    @ApiModelProperty(value = "处理结果描述信息")
    private String msg;
    @ApiModelProperty(value = "处理结果数据信息")
    private T data;
    @ApiModelProperty(value = "true 成功,false 失败")
    private boolean success;
}
