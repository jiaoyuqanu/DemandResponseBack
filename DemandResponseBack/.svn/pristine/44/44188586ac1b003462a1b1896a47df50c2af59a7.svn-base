package com.xqxy.dr.modular.evaluation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import com.xqxy.sys.modular.cust.entity.CustCertifyFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 用户执行效果评估
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_cons_evaluation_appeal")
@ApiModel(value = "ConsEvaluation对象", description = "用户执行效果评估")
public class ConsEvaluationAppeal extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "评估标识")
    @TableId(type = IdType.ASSIGN_ID)
    @TableField("EVALUATION_ID")
    private Long evaluationId;

    @ApiModelProperty(value = "事件标识")
    @TableField("EVENT_ID")
    private Long eventId;

    @ApiModelProperty(value = "用户标识")
    @TableField("CONS_ID")
    private String consId;

    @ApiModelProperty(value = "邀约响应量")
    @TableField("INVITATION_CAP")
    private BigDecimal invitationCap;

    @ApiModelProperty(value = "反馈响应量")
    @TableField("REPLY_CAP")
    private BigDecimal replyCap;

    @ApiModelProperty(value = "基线最大负荷")
    @TableField("MAX_LOAD_BASELINE")
    private BigDecimal maxLoadBaseline;

    @ApiModelProperty(value = "基线最小负荷")
    @TableField("MIN_LOAD_BASELINE")
    private BigDecimal minLoadBaseline;

    @ApiModelProperty(value = "基线平均负荷")
    @TableField("AVG_LOAD_BASELINE")
    private BigDecimal avgLoadBaseline;

    @ApiModelProperty(value = "实际最大负荷")
    @TableField("MAX_LOAD_ACTUAL")
    private BigDecimal maxLoadActual;

    @ApiModelProperty(value = "实际最小负荷")
    @TableField("MIN_LOAD_ACTUAL")
    private BigDecimal minLoadActual;

    @ApiModelProperty(value = "实际平均负荷")
    @TableField("AVG_LOAD_ACTUAL")
    private BigDecimal avgLoadActual;

    @ApiModelProperty(value = "实际响应负荷")
    @TableField("ACTUAL_CAP")
    private BigDecimal actualCap;

    @ApiModelProperty(value = "实际响应电量")
    @TableField("ACTUAL_ENERGY")
    private BigDecimal actualEnergy;

    @ApiModelProperty(value = "核定响应负荷")
    @TableField("CONFIRM_CAP")
    private BigDecimal confirmCap;

    @ApiModelProperty(value = "是否有效:N 无效；Y 有效")
    @TableField("IS_EFFECTIVE")
    private String isEffective;

    @ApiModelProperty(value = "有效响应时长(**分钟)")
    @TableField("EFFECTIVE_TIME")
    private Integer effectiveTime;

    @ApiModelProperty(value = "是否合格(平均负荷)")
    @TableField("IS_QUALIFIED")
    private String isQualified;

    @ApiModelProperty(value = "是否越界(最大负荷)")
    @TableField("IS_OUT")
    private String isOut;


    /**
     * 客户主键
     */
    @TableField(exist = false)
    private Long custId;

    /**
     * 事件信息
     */
    @TableField(exist = false)
    private String consName;

    /**
     * 用电地址
     */
    @TableField(exist = false)
    private String elecAddr;

    /**
     * 行业类别名称
     */
    @TableField(exist = false)
    private String bigTradeName;

    /**
     * 行业类别编码
     */
    @TableField(exist = false)
    private String bigTradeCode;

    /**
     * 行业分类名称
     */
    @TableField(exist = false)
    private String tradeName;

    /**
     * 行业分类编码
     */
    @TableField(exist = false)
    private String tradeCode;

    /**
     * 日最大负荷（本月~去年同月）
     */
    @TableField(exist = false)
    private BigDecimal dayMaxPower;

    /**
     * 最大容量
     */
    @TableField(exist = false)
    private BigDecimal contractCap;

    /**
     * 运行容量
     */
    @TableField(exist = false)
    private BigDecimal runCap;

    /**
     * 电源类型（专线、专变、公变）
     */
    @TableField(exist = false)
    private String typeCode;

    /**
     * 供电单位名称
     */
    @TableField(exist = false)
    private String orgName;

    /**
     * 供电单位编码
     */
    @TableField(exist = false)
    private String orgNo;

    /**
     * 省码
     */
    @TableField(exist = false)
    private String provinceCode;

    /**
     * 市码
     */
    @TableField(exist = false)
    private String cityCode;

    /**
     * 区县码
     */
    @TableField(exist = false)
    private String countyCode;

    /**
     * 街道码（乡镇）
     */
    @TableField(exist = false)
    private String streetCode;

    /**
     * 变电站名称
     */
    @TableField(exist = false)
    private String subsName;

    /**
     * 变电站编码
     */
    @TableField(exist = false)
    private String subsNo;

    /**
     * 线路名称
     */
    @TableField(exist = false)
    private String lineName;

    /**
     * 线路编号
     */
    @TableField(exist = false)
    private String lineNo;

    /**
     * 台区名称
     */
    @TableField(exist = false)
    private String tgName;

    /**
     * 台区编号
     */
    @TableField(exist = false)
    private String tgNo;

    /**
     * 第一联系人姓名
     */
    @TableField(exist = false)
    private String firstContactName;

    /**
     * 第一联系人联系方式
     */
    @TableField(exist = false)
    private String firstContactInfo;

    /**
     * 第二联系人姓名
     */
    @TableField(exist = false)
    private String secondContactName;

    /**
     * 第二联系人联系方式
     */
    @TableField(exist = false)
    private String secondContactInifo;

    /**
     * 1:正常，2：撤销
     */
    @TableField(exist = false)
    private String state;

    /**
     * 项目有效性判断
     */
    @TableField(exist = false)
    private List<String> validityJudgment;

    private String joinUserType;

    @ApiModelProperty(value = "描述")
    @TableField("remark")
    private String remark;

    @ApiModelProperty(value = "负荷响应率")
    private BigDecimal executeRate;

    @TableField(exist = false)
    @ApiModelProperty(value = "受理公司")
    private String acceptingCompany;

    @TableField(exist = false)
    @ApiModelProperty(value = "核定负荷")
    private BigDecimal approvedLoad;

    @TableField(exist = false)
    @ApiModelProperty(value = "原因类型")
    private String appealReason;

    @TableField(exist = false)
    @ApiModelProperty(value = "申述文件上传")
    private String filIeds;

    @TableField(exist = false)
    @ApiModelProperty(value = "核对文件上传")
    private String checkIds;

    @ApiModelProperty(value = "申诉标识")
    @TableField("subsidy_appeal_id")
    private Long subsidyAppealId;


    @TableField(exist = false)
    @ApiModelProperty(value = "关联文件名称")
    private String filesName;

    //核对文件id
    @TableField(exist = false)
    @ApiModelProperty(value = "核对文件名称")
    private String checkName;

    @TableField(exist = false)
    @ApiModelProperty(value = "市专责审批意见")
    private String examineSuggestionCity;

    @TableField(exist = false)
    @ApiModelProperty(value = "省机构审批意见")
    private String examineSuggestionProvince;

    @TableField(exist = false)
    @ApiModelProperty(value = "能源局审批意见")
    private String examineSuggestionEnergy;


    @TableField(exist = false)
    @ApiModelProperty(value = "户号")
    private String orgId;


    @ApiModelProperty(value = "旧表——是否有效:N 无效；Y 有效")
    @TableField(exist = false)
    private String isEffectiveOldTable;


    @ApiModelProperty(value = "旧表——核定响应负荷")
    @TableField(exist = false)
    private BigDecimal confirmCapOldTable;


    @ApiModelProperty(value = "旧表——邀约响应量")
    @TableField(exist = false)
    private BigDecimal invitationCapOldTable;


    @ApiModelProperty(value = "事件编号")
    @TableField(exist = false)
    private String eventNo;


    @ApiModelProperty(value = "事件信息")
    @TableField(exist = false)
    private String eventName;

    //状态
    @TableField(exist = false)
    @ApiModelProperty(value = "状态(1 保存  2受理中 3撤回 4驳回(市级) 5申诉成功(能源局) 6申诉失败(省级或能源局))")
    private String status;

    @TableField(exist = false)
    @ApiModelProperty(value = "状态(1 未审批（展示处理按钮）  2审批中(二级页面提交后) 3撤回(提交后展示撤回按钮) 4修正完成(数据修正后)  5审批通过(能源局) 6审批失败(省级或能源局))")
    private String statusCity;

    @TableField(exist = false)
    @ApiModelProperty(value = "状态(1 未审批  2审批中 3审批失不通过省级或能源局) 4审批通过(能源局))")
    private String statusProvince;

    @TableField(exist = false)
    @ApiModelProperty(value = "状态(1 未审批  2审批不通过(能源局) 3审批通过(能源局))")
    private String statusEnergy;

    @TableField(exist = false)
    @ApiModelProperty(value = "申诉失败的原因")
    private String reasonOfFailure;
}
