package com.xqxy.dr.modular.event.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@ApiModel(description = "方案参与用户 参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class PlanConsParam extends BaseParam {

    /**
     * 参与标识
     */
    private Long particId;

    /**
     * 用户标识
     */
    private String consId;

    private String consName;

    /**
     * 方案标识
     */
    private Long planId;

    /**
     * 基线负荷标识
     */
    private Long baselineCapId;

    @ApiModelProperty(value = "管理单位")
    private String orgName;

    @ApiModelProperty(value = "管理单位编号")
    private String orgId;

    /**
     * 可响应负荷
     */
    @TableField("DEMAND_CAP")
    private BigDecimal demandCap;

    /**
     * 响应价格
     */
    private BigDecimal replyPrice;

    /**
     * 方案序位
     */
    private Integer sequenceNo;

    /**
     * 是否剔除
     */
    private String deleted;

    /**
     * 被剔除使用的规则
     */
    private String delRule;

    /**
     * 方案关联的事件id
     */
    private Long eventId;


    private List<String> consIdList;

    private String integrator;

    private String involvedIn;

    private String provinceCode;

    private String cityCode;

    private String countryCode;

    /**
     * 执行时间
     */
    private String executeTime;

    /**
     * 是否越界
     */
    private String isOut;

    /**
     * 是否达标
     */
    private String isQualified;

    /**
     * 组织id集合
     */
    private List<String> orgNos;
}
