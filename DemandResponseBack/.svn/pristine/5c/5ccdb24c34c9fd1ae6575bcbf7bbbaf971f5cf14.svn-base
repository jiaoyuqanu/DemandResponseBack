package com.xqxy.dr.modular.powerplant.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;

import com.xqxy.core.pojo.base.param.BaseParam;
import com.xqxy.dr.modular.powerplant.param.Param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 电厂档案
 * @author lixiaojun
 * @since 2021-10-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_da_powerplant")
public class DaPowerplant extends Param implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("电厂名称")
    private String name;

    @ApiModelProperty("法人姓名")
    private String leader;

    @ApiModelProperty("电话")
    private String phone;

    @ApiModelProperty("电厂编码")
    private String num;

    @ApiModelProperty("所属运营商")
    private String business;

    @ApiModelProperty("电压等级")
    private String voltageLevel;

    @ApiModelProperty("线路名称")
    private String line;

    @ApiModelProperty("电厂类型：分布式发电电厂、负荷调控电厂、双向调节电厂、混合型电厂")
    private String type;

    @ApiModelProperty("所在省")
    private String province;

    @ApiModelProperty("所在市")
    private String city;

    @ApiModelProperty("所在县（区）")
    private String district;

    @ApiModelProperty("通信地址")
    private String address;

    @ApiModelProperty("状态：0-草稿，1-提交审核，2-审核通过，3-审核不通过")
    private Integer status;

    private Date createTime;

    @ApiModelProperty("信息更新时间")
    private Date updateTime;

    private String createUser;


}
