package com.xqxy.dr.modular.powerplant.param;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 负荷聚合商报价信息
 * </p>
 *
 * @author czj
 * @since 2021-12-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DrDaAggregatorPriceAddParam extends BaseParam implements Serializable {

private static final long serialVersionUID=1L;

    /**
     * 负荷聚合商报价id
     */
    @ApiModelProperty(value = "负荷聚合商报价id")
    @NotNull(message = "id不能为空，请检查id参数", groups = {BaseParam.edit.class})
    private Long id;

    /**
     * 电力市场竞价公告id
     */
    @ApiModelProperty(value = "电力市场竞价公告id")
    @NotNull(message = "electricBidNoticeId不能为空，请检查electricBidNoticeId参数", groups = {BaseParam.add.class})
    private Long electricBidNoticeId;

    /**
     * 负荷聚合商组号
     */
    @ApiModelProperty(value = "负荷聚合商组号")
    @NotNull(message = "aggregatorNo不能为空，请检查aggregatorNo参数", groups = {BaseParam.add.class})
    private Long aggregatorNo;

    /**
     * 开始时间（时分秒）
     */
    @ApiModelProperty(value = "开始时间")
    @NotNull(message = "startTime不能为空，请检查startTime参数", groups = {BaseParam.add.class})
    private String startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @NotNull(message = "endTime不能为空，请检查endTime参数", groups = {BaseParam.add.class})
    private String endTime;

    /**
     * 响应容量
     */
    @ApiModelProperty(value = "响应容量")
    @NotNull(message = "endTime不能为空，请检查endTime参数", groups = {BaseParam.add.class})
    private Integer responseCapacity;

    /**
     * 响应速率
     */
    @ApiModelProperty(value = "响应速率")
    @NotNull(message = "responseRate不能为空，请检查responseRate参数", groups = {BaseParam.add.class})
    private Integer responseRate;

    /**
     * 响应单价
     */
    @ApiModelProperty(value = "响应单价")
    @NotNull(message = "responsePrice不能为空，请检查responsePrice参数", groups = {BaseParam.add.class})
    private Integer responsePrice;

    /**
     * 申报人
     */
    @ApiModelProperty(value = "申报人")
    @NotNull(message = "declareName不能为空，请检查declareName参数", groups = {BaseParam.add.class})
    private String declareName;

    /**
     * 调节容量
     */
    @ApiModelProperty(value = "调节容量")
    @NotNull(message = "adjustCapacity不能为空，请检查adjustCapacity参数", groups = {BaseParam.add.class})
    private Integer adjustCapacity;

    /**
     * 申报单价
     */
    @ApiModelProperty(value = "申报单价")
    @NotNull(message = "declarePrice不能为空，请检查declarePrice参数", groups = {BaseParam.add.class})
    private Long declarePrice;


    /**
     * 提交状态1已提交0未提交
     */
    private String status;


}
