package com.xqxy.dr.modular.powerplant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * <p>
 * 电力市场竞价公告信息
 * </p>
 *
 * @author liqirui
 * @since 2021-12-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DrDaElectricBidNotice extends BaseEntity implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 服务类型code
     */
    private String electricTypeCode;

    /**
     * 服务类型名称
     */
    private String electricTypeName;

    /**
     * 服务容量
     */
    private Integer electricCapacity;

    /**
     * 服务日期
     */
    private LocalDate date;

    /**
     * 开始时间（时分秒）
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 发布时间（年月日时分秒）
     */
    private String releaseTime;

    /**
     * 申报截止日期
     */
    private LocalDate declareTime;

    /**
     * 执行日期（年月日）
     */
    private LocalDate effectDate;


}
