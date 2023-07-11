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
 * 评估表
 * @author lixiaojun
 * @since 2021-10-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_da_evaluate")
public class DaEvaluate extends Param implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 评估目标ID
     */
    @ApiModelProperty("评估目标ID")
    private Integer targetId;

    /**
     * 评估目标类型
     */
    @ApiModelProperty("评估目标类型：1-电厂，2-机组，3-代理用户")
    private Integer targetType;

    @ApiModelProperty("容量")
    private String volume;

    @ApiModelProperty("负荷")
    private String onus;

    @ApiModelProperty("电量")
    private String power;

    /**
     * 评估内容
     */
    @ApiModelProperty("评估内容")
    private String content;

    private Date createTime;

    @ApiModelProperty("创建人")
    private String createUser;


}
