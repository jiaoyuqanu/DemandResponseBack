package com.xqxy.dr.modular.event.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel(description = "事件 参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class PlanParam  extends BaseParam {

    @ApiModelProperty(value = "主键标识")
    private Long id;

    @NotNull(message = "planId不能为空，请检查planId参数", groups = {detail.class})
    private Long planId;
    /**
     * 事件标识
     */
    @ApiModelProperty(value = "事件标识")
    @NotNull(message = "eventId不能为空，请检查eventId参数", groups = {add.class,autoDeleted.class,manualDeleted.class,batchInvation.class,deleteInvation.class})
    private Long eventId;

    /**
     * 基线剔除规则
     */
    // @NotNull(message = "deleteRule不能为空，请检查deleteRule参数", groups = {autoDeleted.class,})
    @ApiModelProperty(value = "剔除规则")
    private String deleteRule;

    @NotNull(message = "deleteRuleList不能为空，请检查deleteRuleList参数", groups = {autoDeleted.class,})
    private List<String> deleteRuleList;

    private List<Long> ids;

    /**
     * 剔除用户id集合
     */
    private List<String> consIdList;

    private List<String> custIdList;

    private List<String> custIds;


    /**
     * 调控范围类别
     */
    private String rangeType;

    /**
     * 基线库id
     */
//    @NotNull(message = "baselineCapId不能为空，请检查baselineCapId参数", groups = {add.class,})
    private Long baselineCapId;

    /**
     * 调控范围
     */
    private String regulateRange;

    /**
     * 行政区域等级
     */
    private String regionLevel;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String consId;

    /**
     * 客户id
     */
    @ApiModelProperty(value = "客户id")
    private Long custId;

    /**
     * 自动剔除分组
     */
    public @interface autoDeleted {
    }

    /**
     * 手动剔除分组
     */
    public @interface manualDeleted {
    }

    /**
     * 自动邀约分组
     */
    public @interface batchInvation {
    }

    /**
     * 删除邀约分组
     */
    public @interface deleteInvation {
    }

    @ApiModelProperty("组织机构id")
    @TableField(exist = false)
    private String orgId;

}
