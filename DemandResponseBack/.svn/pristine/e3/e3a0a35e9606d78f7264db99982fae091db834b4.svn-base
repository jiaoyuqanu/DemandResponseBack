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
 * 虚拟电厂-用户档案
 * @author lixiaojun
 * @since 2021-10-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_da_user")
public class DaUser extends Param implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("代理机组ID")
    private Integer jzId;

    @ApiModelProperty("代理机组名称")
    private String jzName;

    @ApiModelProperty("所属电厂id")
    private Integer powerplantId;

    @ApiModelProperty("所属电厂名称")
    private String powerplantName;

    @ApiModelProperty("用户名")
    private String name;

    @ApiModelProperty("电话")
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("开户银行")
    private String bank;

    @ApiModelProperty("开户人姓名")
    private String accountName;

    @ApiModelProperty("银行账号")
    private String account;

    @ApiModelProperty("供电电压")
    private String voltage;

    @ApiModelProperty("供电线路")
    private String line;

    private Date createTime;

    private Date updateTime;

    private String createUser;


}
