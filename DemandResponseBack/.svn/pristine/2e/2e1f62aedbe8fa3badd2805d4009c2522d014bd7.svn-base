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

/**
 * 公布出清计划表
 * @author lixiaojun
 * @since 2021-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_da_plan")
public class DaPlan extends Param implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("编号")
    private String num;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("调峰日期")
    private String tfDate;

    @ApiModelProperty("调峰时段")
    private String tfTime;

    @ApiModelProperty("类型")
    private String type;


}
