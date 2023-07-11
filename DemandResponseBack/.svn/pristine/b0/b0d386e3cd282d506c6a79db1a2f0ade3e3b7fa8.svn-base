package com.xqxy.dr.modular.event.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import com.xqxy.sys.modular.cust.result.ConsResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 方案参与用户
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ConsExecuteStatistic extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String orgId;

    @Excel(name = "供电单位", width = 16)
    private String orgName;

    private String regName;

    @Excel(name = "认约户数", width = 16)
    private Integer commitConsSize; //认约用户数量

    @Excel(name = "执行户数", width = 16)
    private Integer count;//执行用户数量

    @Excel(name = "剔除户数", width = 16)
    private Integer noExecConsSize; //剔除用户数量：实时显示执行方案中不参与状态的电力户号数

    @Excel(name = "认约负荷(万千瓦)", width = 16)
    private BigDecimal commitConsCap; //认约用户数量

//    @Excel(name = "执行目标负荷", width = 16)
    private BigDecimal targetCap;//执行目标负荷

    @Excel(name = "执行认约负荷(万千瓦)", width = 16)
    private BigDecimal execConsCap; //执行用户目标负荷

    @Excel(name = "剔除认约负荷(万千瓦)", width = 16)
    private BigDecimal noExecConsCap; //不参与用户目标负荷

    @Excel(name = "直接参与用户数", width = 16)
    private Integer consCount;//独立用户数量

    @Excel(name = "负荷聚合商数", width = 16)
    private Integer custCount;//聚合负荷商数量

    @ApiModelProperty(name = "聚合商目标执行负荷")
    private BigDecimal custCap;//聚合商目标执行负荷

    @ApiModelProperty(name = "独立用户目标执行负荷")
    private BigDecimal consCap;//独立用户目标执行负荷

    @ApiModelProperty(name = "实时显示执行方案中参与状态的电力户号数")
    private Integer execConsSize; //实时显示执行方案中参与状态的电力户号数


}
