package com.xqxy.sys.modular.cust.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("dr_black_list")
public class BlackName extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 3489862119374427325L;

    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "户号")
    private String consId;

    @ApiModelProperty(value = "用户名称")
    private String consName;

    @ApiModelProperty(value = "过期时间")
    private String expireTime;

    @ApiModelProperty(value = "年度")
    private String year;

    @ApiModelProperty(value = "是否有效")
    private String isEffective;

    @ApiModelProperty(value = "关联事件")
    private String eventIds;

    @ApiModelProperty(value = "关联事件名称")
    private String eventNames;

    @ApiModelProperty(value = "关联事件名称")
    private String eventNos;

    @ApiModelProperty(value = "描述")
    @TableField("REASON")
    private String remark;

    @TableField(exist = false)
    private Integer count;

    @TableField(exist = false)
    private Long eventId;

    @TableField(exist = false)
    private String eventNo;

    @TableField(exist = false)
    private String eventName;

}
