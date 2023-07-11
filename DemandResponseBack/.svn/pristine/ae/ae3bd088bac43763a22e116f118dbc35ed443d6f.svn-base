package com.xqxy.dr.modular.powerplant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 负荷聚合商报价信息
 * </p>
 *
 * @author liqirui
 * @since 2021-12-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DrDaAggregatorPrice  implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 电力市场竞价公告id
     */
    private Long electricBidNoticeId;

    /**
     * 负荷聚合商组号
     */
    private Long aggregatorNo;

    /**
     * 开始时间（时分秒）
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 响应容量
     */
    private Integer responseCapacity;

    /**
     * 响应速率
     */
    private Integer responseRate;

    /**
     * 响应单价
     */
    private Integer responsePrice;

    /**
     * 申报人
     */
    private String declareName;

    /**
     * 调节容量
     */
    private Integer adjustCapacity;

    /**
     * 申报单价
     */
    private Long declarePrice;


    /**
     * 提交状态1已提交0未提交
     */
    private String status;


}
