package com.xqxy.dr.modular.event.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户参与情况统计表实体类
 *
 */
@Data
public class DrEventInvitationUser implements Serializable {

    private static final long serialVersionUID = -7841533926025452017L;

    /**
     * 用户id
     */
    private String consId;
    /**
     * 户名
     */
    private String consName;
    /**
     * 用户类型
     */
    private Integer consType;
    /**
     * 参与响应次数
     */
    private Integer responseTimes;
    /**
     * 削峰响应次数
     */
    private Integer peakClippingTimes;
    /**
     * 填谷响应次数
     */
    private Integer valleyFillingTimes;
    /**
     * 总响应负荷
     */
    private BigDecimal totalResponseLoad;
    /**
     * 总响应电量
     */
    private BigDecimal totalResponsePower;
    /**
     * 响应准确度
     */
    private BigDecimal responseAccuracy;
    /**
     * 激励金额
     */
    private BigDecimal incentiveAmount;
}
