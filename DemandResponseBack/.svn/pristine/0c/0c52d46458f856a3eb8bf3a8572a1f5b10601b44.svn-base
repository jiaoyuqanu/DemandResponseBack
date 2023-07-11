package com.xqxy.dr.modular.subsidy.param;

import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 用户事件补贴 参数
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-20 15:00
 */
@ApiModel(description = "用户事件补贴")
@EqualsAndHashCode(callSuper = true)
@Data
public class ConsSubsidyParam extends BaseParam {

    //补贴标识
    private Long subsidyId;

    //用户标识
    private String consId;

    //用户名称
    private String consName;

    //客户标识
    private Long custId;

    //事件标识
    private Long eventId;

    //实际响应负荷
    private BigDecimal actualCap;

    //实际响应电量
    private BigDecimal actualEnergy;

    //签约价格
    private BigDecimal contractPrice;

    //激励金额
    private BigDecimal subsidyAmount;

    //实际金额
    private BigDecimal settledAmount;

    //异常描述
    private String remark;

    //计算规则，1，2，3
    private String calRule;

    //是否有效
    private String isEffective;

    //  组织结构编码
    private String orgNo;

    private String startTime;

    private String endTime;

}
