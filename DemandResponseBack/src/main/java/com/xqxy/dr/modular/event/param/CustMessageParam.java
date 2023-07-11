package com.xqxy.dr.modular.event.param;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

@ApiModel(description = "客户邀约 参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class CustMessageParam extends BaseParam {

    /**
     * 事件标识
     */
    @ApiModelProperty(value = "事件标识 ")
    private String eventId;

    /**
     * 业务关联:业务编码_业务编码
     */
    @ApiModelProperty(value = "业务关联:业务编码_业务编码")
    private String businessCode;

    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名称")
    private String custName;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String custId;


    /**
     * 标记状态
     */
    @ApiModelProperty(value = "标记状态")
    private String delRule;

    /**
     * 统一信用编码
     */
    @ApiModelProperty(value = "统一信用编码")
    private String creditCode;

    private String state;

    private String phone;

    private List<String> orgs;

    private List<String> joinUserTypes;

    /**
     * 用户id集合
     */
    private List<String> consIds;


}
