package com.xqxy.dr.modular.powerplant.param;

import com.xqxy.core.pojo.base.param.BaseParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 通用参数
 * </p>
 *
 * @author Caoj
 * @date 2021-12-08 14:07
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DaParam extends BaseParam {

    private static final long serialVersionUID = -8927324963468786727L;

    /**
     * 调控开始日期
     */
    private String startTime;

    /**
     * 调控结束日期
     */
    private String endTime;

    /**
     * 集成商编号
     */
    private String custNo;

    /**
     * 客户标识
     */
    private String custId;

    /**
     * 调控日期
     */
    private String pickLoadDate;

    /**
     * 代理用户编号
     */
    private String id;
}
