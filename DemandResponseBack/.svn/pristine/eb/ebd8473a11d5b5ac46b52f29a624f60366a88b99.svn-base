package com.xqxy.dr.modular.powerplant.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.xqxy.dr.modular.powerplant.param.Param;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 交易公示分段
 * </p>
 *
 * @author xiao jun
 * @since 2021-10-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_trade_notice_detail")
public class TradeNoticeDetail extends Param implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer noticeId;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 流量
     */
    private String kwv;


}
