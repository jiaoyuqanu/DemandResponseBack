package com.xqxy.sys.modular.cust.param;

import com.xqxy.core.pojo.base.param.BaseParam;
import lombok.Data;

import java.util.List;

@Data
public class BlackNameParam extends BaseParam {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户名称
     */
    private String consName;

    /**
     * 用户户号
     */
    private String consId;

    /**
     * 年度
     */
    private String year;

    private Long[] ids;
}
