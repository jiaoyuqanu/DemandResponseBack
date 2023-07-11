package com.xqxy.dr.modular.event.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 需求响应事件
 * </p>
 *
 * @author czj
 * @since 2022-01-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_plan_cons_rec")
public class DrPlanConsRec extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键标识
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 事件标识
     */
    @TableField("event_id")
    private Long eventId;

    /**
     * 方案执行标识
     */
    @TableField("implem_id")
    private Long implemId;

    /**
     * 用户标识
     */
    @TableField("cons_id")
    private String consId;

    /**
     * 是否参与执行
     */
    @TableField("IMPLEMENT")
    private String implement;
    

}
