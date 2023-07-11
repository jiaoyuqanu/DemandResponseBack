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
 * 市场准入审核表
 * @author lixiaojun
 * @since 2021-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_da_market")
public class DaMarket extends Param implements Serializable {

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

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;


}
