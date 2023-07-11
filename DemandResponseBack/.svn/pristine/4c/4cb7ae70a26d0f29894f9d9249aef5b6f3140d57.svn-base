package com.xqxy.dr.modular.subsidy.param;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xqxy.core.annotion.NeedSetValue;
import com.xqxy.core.pojo.base.param.BaseParam;
import com.xqxy.sys.modular.cust.entity.Cons;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@ApiModel(description = "用户日补贴 参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class ConsSubsidyDailyParam extends BaseParam {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "关联用户ID")
    private String consId;

    @ApiModelProperty(value = "用户名称")
    private String consName;

    @ApiModelProperty(value = "客户名称")
    private String custName;

    @ApiModelProperty(value = "统一信用代码")
    private String creditCode;

    @ApiModelProperty(value = "补贴日期")
    private LocalDate subsidyDate;

    private String eventNo;

    // 是否集成商
    private int integrator;

    // 开始日期
    private LocalDate subsidyStartDate;

    // 结束日期
    private LocalDate subsidyEndDate;

    @ApiModelProperty(value = "结算金额")
    private BigDecimal settledAmount;

    @ApiModelProperty(value = "状态(字典 0 待确认 1 复核中 2 已确认)")
    private String status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private Long createUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    private Long updateUser;

    @ApiModelProperty(value = "计算时间")
    private LocalDateTime subTime;

    @ApiModelProperty(value = "计算状态")
    private String subStatus;

    @ApiModelProperty(value = "计算异常 描述")
    private String subException;

    @ApiModelProperty(value = "计算规则, 1:湖南老版规则，2：新版规则")
    private String cacRule;

    @ApiModelProperty(value = "事件id,多个逗号隔开")
    private String eventIds;

    @ApiModelProperty(value = "结算批号")
    private String settlementNo;

    @ApiModelProperty(value = "事件次数")
    private int eventNum;

    // 用户标识集合
    private List<Long> consIdList;

    private Long projectId;

    private Long custId;


}
