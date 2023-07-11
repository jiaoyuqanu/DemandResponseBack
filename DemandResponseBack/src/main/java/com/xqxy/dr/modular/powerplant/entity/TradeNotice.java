package com.xqxy.dr.modular.powerplant.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.xqxy.core.pojo.base.param.BaseParam;
import com.xqxy.dr.modular.powerplant.param.Param;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 交易公示
 * </p>
 *
 * @author xiao jun
 * @since 2021-10-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_trade_notice")
public class TradeNotice extends Param implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标题&名称
     */
    private String title;

    /**
     * 调控类型
     */
    private String type;

    /**
     * 状态
     */
    private String status;

    /**
     * 发布时间
     */
    private LocalDateTime publishTime;

    /**
     * 生效时间
     */
    private LocalDateTime enableTime;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
