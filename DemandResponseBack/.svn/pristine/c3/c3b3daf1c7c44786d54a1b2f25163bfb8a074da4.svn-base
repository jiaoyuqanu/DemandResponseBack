package com.xqxy.dr.modular.project.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author Caoj
 * @since 2021-10-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_cust_contract_detail")
@Accessors(chain = true)
public class CustContractDetail extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 明细标识
     */
    @TableId("DETAIL_ID")
    private Long detailId;

    private Long contractId;

    /**
     * 响应类型1削峰，2填谷
     */
    @TableField("RESPONSE_TYPE")
    private String responseType;

    /**
     * 时间类型 1邀约，2实时
     */
    @TableField("TIME_TYPE")
    private String timeType;

    /**
     * 响应开始时间
     */
    @TableField("START_TIME")
    private String startTime;

    /**
     * 响应结束时间
     */
    @TableField("END_TIME")
    private String endTime;

    /**
     * 可响应负荷为该代理子用户设备可调
            节负荷的累加
     */
    @TableField("RESPONSE_CAP")
    private BigDecimal responseCap;

    /**
     * 合同容量
     */
    @TableField("CONTRACT_CAP")
    private BigDecimal contractCap;

    /**
     * 签约价格
     */
    @TableField("CONTRACT_PRICE")
    private BigDecimal contractPrice;

    /**
     * 分成比例
     */
    @TableField("EXTRACT_RATIO")
    private BigDecimal extractRatio;

    /**
     * 项目明细标识
     */
    @TableField("PROJECT_DETAIL_ID")
    private Long projectDetailId;

    @ApiModelProperty(value = "提前通知时间")
    private Integer advanceNoticeTime;

    @ApiModelProperty(value = "备用容量")
    private BigDecimal spareCap;

    @ApiModelProperty(value = "最小响应时长")
    private Integer minTimes;

    @ApiModelProperty(value = "备用容量最小响应时长 单位分钟")
    private Integer spareMinTimes;
}
