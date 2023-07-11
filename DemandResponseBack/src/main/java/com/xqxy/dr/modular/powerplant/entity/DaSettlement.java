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
 * 费用结算
 * @author lixiaojun
 * @since 2021-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_da_settlement")
public class DaSettlement extends Param implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("编号")
    private String num;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("参与类型")
    private String joinType;

    @ApiModelProperty("负荷曲线")
    private String fuhe;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("调峰日期")
    private String tfDate;

    @ApiModelProperty("调峰时段")
    private String tfTime;

    @ApiModelProperty("出清价格")
    private String price;

    @ApiModelProperty("实际相应电量")
    private String power;


}
