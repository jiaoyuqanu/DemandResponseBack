package com.xqxy.dr.modular.statistics.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author liqirui
 * @since 2022-10-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("dr_event_statistics")
public class EventStatistics implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 事件id
     */
    @TableField("event_id")
    private Long eventId;


    /**
     * 项目id
     */
    @TableField("project_id")
    private Long projectId;


    /**
     * 组织机构编码
     */
    @TableField("org_no")
    private String orgNo;

    /**
     * 电力缺口
     */
    @TableField("goal")
    private BigDecimal goal;

    /**
     * 户次
     */
    @TableField("cons_count")
    private Integer consCount;

    /**
     * 调度日期
     */
    @TableField("regulate_date")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date regulateDate;

    /**
     * 实际响应负荷 (平均压降负荷 )单位：万千瓦
     */
    @TableField("actual_cap")
    private BigDecimal actualCap;

    /**
     * 日影响电量 单位：万千瓦
     */
    @TableField("actual_energy")
    private BigDecimal actualEnergy;


}
