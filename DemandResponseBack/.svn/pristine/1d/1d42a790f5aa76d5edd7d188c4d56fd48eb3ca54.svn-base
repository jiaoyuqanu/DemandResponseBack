package com.xqxy.dr.modular.event.param;

import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ConsMessageParam extends BaseParam {

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
    private String consName;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String consId;

    /**
     * 机构名称
     */
    @ApiModelProperty(value = "管理单位")
    private String orgName;

    /**
     * 标记状态
     */
    @ApiModelProperty(value = "标记状态")
    private String delRule;

    private List<String> orgs;

    private String joinUserType;

    private List<String> joinUserTypes;

    private String state;

    private String phone;


    /**
     * 用户id集合
     */
    private List<String> consIds;


}
