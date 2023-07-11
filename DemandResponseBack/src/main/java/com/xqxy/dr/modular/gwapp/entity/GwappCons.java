package com.xqxy.dr.modular.gwapp.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 电力用户信息表
 * </p>
 *
 * @author Yechs
 * @since 2022-05-20
 */
@Data
@TableName("dr_gwapp_cons")
@ApiModel(value = "GwappCons对象", description = "电力用户信息表")
public class GwappCons extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("电力户号")
    @TableId("ID")
    private String id;

    @ApiModelProperty("本实体记录的唯一标识，产生规则为流水号")
    @TableField("CUST_ID")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long custId;

    @ApiModelProperty("用户名称")
    @TableField("CONS_NAME")
    private String consName;

    @ApiModelProperty("用电地址")
    @TableField("ELEC_ADDR")
    private String elecAddr;

    @ApiModelProperty("行业类别名称")
    @TableField("BIG_TRADE_NAME")
    private String bigTradeName;

    @ApiModelProperty("行业类别编码")
    @TableField("BIG_TRADE_CODE")
    private String bigTradeCode;

    @ApiModelProperty("行业分类名称")
    @TableField("TRADE_NAME")
    private String tradeName;

    @ApiModelProperty("行业分类编码")
    @TableField("TRADE_CODE")
    private String tradeCode;

    @ApiModelProperty("日最大负荷（本月~去年同月）")
    @TableField("DAY_MAX_POWER")
    private BigDecimal dayMaxPower;

    @ApiModelProperty("合同容量")
    @TableField("CONTRACT_CAP")
    private BigDecimal contractCap;

    @ApiModelProperty("运行容量")
    @TableField("RUN_CAP")
    private BigDecimal runCap;

    @ApiModelProperty("电源类型（专线、专变、公变）")
    @TableField("TYPE_CODE")
    private String typeCode;

    @ApiModelProperty("供电单位名称")
    @TableField("ORG_NAME")
    private String orgName;

    @ApiModelProperty("供电单位编码")
    @TableField("ORG_NO")
    private String orgNo;

    /**
     * 省 供电单位
     */
    @TableField("PROVINCE_ORG_NO")
    private String provinceOrgNo;

    /**
     * 市 供电单位
     */
    @TableField("CITY_ORG_NO")
    private String cityOrgNo;

    /**
     * 区/县 供电单位
     */
    @TableField("AREA_ORG_NO")
    private String areaOrgNo;

    /**
     * 乡/镇/街道 供电单位
     */
    @TableField("STREET_ORG_NO")
    private String streetOrgNo;

    @ApiModelProperty("省码")
    @TableField("PROVINCE_CODE")
    private String provinceCode;

    @ApiModelProperty("市码")
    @TableField("CITY_CODE")
    private String cityCode;

    @ApiModelProperty("区县码")
    @TableField("COUNTY_CODE")
    private String countyCode;

    @ApiModelProperty("街道码（乡镇）")
    @TableField("STREET_CODE")
    private String streetCode;

    @ApiModelProperty("变电站名称")
    @TableField("SUBS_NAME")
    private String subsName;

    @ApiModelProperty("变电站编码")
    @TableField("SUBS_NO")
    private String subsNo;

    @ApiModelProperty("线路名称")
    @TableField("LINE_NAME")
    private String lineName;

    @ApiModelProperty("线路编号")
    @TableField("LINE_NO")
    private String lineNo;

    @ApiModelProperty("台区名称")
    @TableField("TG_NAME")
    private String tgName;

    @ApiModelProperty("台区编号")
    @TableField("TG_NO")
    private String tgNo;

    @ApiModelProperty("第一联系人姓名")
    @TableField("first_contact_name")
    private String firstContactName;

    @ApiModelProperty("第一联系人联系方式")
    @TableField("first_contact_info")
    private String firstContactInfo;

    @ApiModelProperty("第二联系人姓名")
    @TableField("second_contact_name")
    private String secondContactName;

    @ApiModelProperty("第二联系人联系方式")
    @TableField("second_contact_inifo")
    private String secondContactInifo;

    @ApiModelProperty("1:正常，2：撤销")
    @TableField("STATE")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String state;

    @ApiModelProperty("电压等级")
    @TableField("VOLTCODE")
    private String voltcode;

    @ApiModelProperty("保安负荷")
    @TableField("SAFETY_LOAD")
    private BigDecimal safetyLoad;


}
