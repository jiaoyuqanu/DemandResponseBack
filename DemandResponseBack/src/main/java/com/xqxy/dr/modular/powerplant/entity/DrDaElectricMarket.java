package com.xqxy.dr.modular.powerplant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

/**
 * <p>
 * 电力市场需求信息表
 * </p>
 *
 * @author liqirui
 * @since 2021-12-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DrDaElectricMarket extends BaseEntity implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 服务类型名称
     */
    private String electricTypeName;

    /**
     * 服务类型code
     */
    private Long electricTypeCode;

    /**
     * 服务容量
     */
    private Long electricCapacity;

    /**
     * 服务日期
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date date;

    /**
     * 开始时间
     */
    private Time startTime;

    /**
     * 结束时间
     */
    private Time endTime;

    /**
     * 接收时间
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date receiveTime;
}
