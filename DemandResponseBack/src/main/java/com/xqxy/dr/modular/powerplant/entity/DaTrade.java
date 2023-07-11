package com.xqxy.dr.modular.powerplant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xqxy.core.pojo.base.param.BaseParam;
import com.xqxy.dr.modular.powerplant.param.Param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 交易提交表
 * @author lixiaojun
 * @since 2021-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_da_trade")
public class DaTrade extends Param implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("编号")
    private String num;

    @ApiModelProperty("1-虚拟电厂运营商,2-负荷聚合商")
    private String userType;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("参与类型")
    private String joinType;

    @ApiModelProperty("可调负荷")
    private String fuhe;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("参与方式")
    private String joinWay;

    @ApiModelProperty("开始日期")
    private String startDate;

    @ApiModelProperty("结束日期")
    private String endDate;

    @ApiModelProperty("状态: 1-待审核，2-审核通过，3-审核不通过")
    private String status;

    @ApiModelProperty("申报单价")
    private String unitPrice;

    @ApiModelProperty("可调节容量")
    private String volume;

    @ApiModelProperty("服务日期")
    private String serviceDate;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;

    public DaTrade(){}

    public DaTrade(DaMarket entity) {
        this.num = entity.getNum();
        this.userType = entity.getUserType();
        this.name = entity.getName();
        this.joinType = entity.getJoinType();
        this.fuhe = entity.getFuhe();
        this.type = entity.getType();
        this.joinWay = entity.getJoinWay();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.status = entity.getStatus();
    }

}
