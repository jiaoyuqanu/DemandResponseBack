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
 * 机组档案
 * @author lixiaojun
 * @since 2021-10-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_da_jz")
public class DaJz extends Param implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("所属电厂id")
    private Integer powerplantId;

    @ApiModelProperty("所属电厂名称")
    private String powerplantName;

    @ApiModelProperty("机组名称")
    private String name;

    @ApiModelProperty("机组编号")
    private String num;

    @ApiModelProperty("并网线路名称")
    private String line;

    @ApiModelProperty("并网电压等级")
    private String voltageLevel;

    @ApiModelProperty("机组类型（分布式发电 机组、负荷调控机组、双向调节机组）")
    private String type;

    @ApiModelProperty("额定容量")
    private String edrl;

    @ApiModelProperty("爬坡率")
    private String ppl;

    private Date createTime;

    private Date updateTime;

    private String createUser;


}
