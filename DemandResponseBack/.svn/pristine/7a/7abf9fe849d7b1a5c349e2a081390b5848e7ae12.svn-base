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
 * 执行情况统计
 * @author lixiaojun
 * @since 2021-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_da_execute")
public class DaExecute extends Param implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("所在省")
    private String province;

    @ApiModelProperty("所在市")
    private String city;

    @ApiModelProperty("所在县（区）")
    private String district;

    @ApiModelProperty("供电单位编号")
    private String powerNum;

    @ApiModelProperty("当日电网供电统调日负荷")
    private String fuheDay;

    @ApiModelProperty("当日电网供电统调日电量")
    private String powerDay;


}
