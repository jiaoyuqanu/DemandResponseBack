package com.xqxy.sys.modular.log.param;

import com.xqxy.core.pojo.base.param.BaseParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 中台日志操作记录参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DrRequestLogParam extends BaseParam {

    /**
     * 本实体记录的唯一标识，产生规则为流水号
     */
    private Long id;

    /**
     * 请求地址
     */
    private String requestUrl;

    /**
     * 请求参数
     */
    private String requestParam;

    /**
     * 返回结果
     */
    private String responseData;

}
