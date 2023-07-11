package com.xqxy.dr.modular.dispatch.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 * 需求调度分解实体
 * </p>
 *
 * @author chenzhijun
 * @since 2021-10-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_event")
public class DispatchEvent extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 事件标识id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long eventId;


    /**
     * 事件名称
     */
    @TableField("EVENT_NAME")
    private String eventName;


    /**
     * 调度日期
     */
    @TableField("REGULATE_DATE")
    private LocalDate regulateDate;


    /**
     * 事件状态 1:保存，2待执行，3执行中，4结束，5废弃
     */
    private String eventStatus;

    /**
     * 发起人
     */
    private String UserName;

}
