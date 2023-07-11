package com.xqxy.dr.modular.powerplant.param;

import com.xqxy.core.pojo.base.param.BaseParam;
import lombok.Data;

/**
 * <p>
 * 用户执行情况
 * </p>
 *
 * @author shi
 * @date 2021-12-15 9:07
 */
@Data
public class DaConsExecuteParam extends BaseParam{

        /**
         * 用户编号
         */
        private String consId;

        /**
         * 用户名称
         */
        private String consName;

        /**
         * 供电单位编码
         */
        private String orgNo;

        /**
         * 行业分类编码
         */
        private String tradeCode;



}
