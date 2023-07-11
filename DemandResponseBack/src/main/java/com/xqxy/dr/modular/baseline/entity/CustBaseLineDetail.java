package com.xqxy.dr.modular.baseline.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 * 基线管理
 * </p>
 *
 * @author chen zhi jun
 * @since 2021-10-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_cust_baseline")
public class CustBaseLineDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 本实体记录的唯一标识，基线负荷主键id
     */
    @ApiModelProperty(value = "基线负荷主键id")
    @TableId(type = IdType.ASSIGN_ID)
    private Long baselineId;

    /**
     * 基线库id
     */
    @ApiModelProperty(value = "基线库id")
    private Long baselineLibId;

    /**
     * 用户标识
     */
    @ApiModelProperty(value = "用户标识")
    private Long custId;

    /**
     * 用户户号
     */
    @ApiModelProperty(value = "用户户号")
    @TableField(exist = false)
    private String consNo;

    /**
     * 用户名称
     */
    @ApiModelProperty(value = "客户法人名称")
    @TableField("cust_name")
    private String name;

    /**
     * 是否集成商
     */
    @ApiModelProperty(value = "是否集成商")
    @TableField(exist = false)
    private String integrator;

    /**
     * 基线日期
     */
    @ApiModelProperty(value = "基线日期")
    private LocalDate baselineDate;

    /**
     * 日期,逗号分开
     */
    @ApiModelProperty(value = "日期,逗号分开")
    private String simplesDate;

    /**
     * 基线最大负荷
     */
    @ApiModelProperty(value = "基线最大负荷")
    private BigDecimal maxLoadBaseline;

    /**
     * 基线最小负荷
     */
    @ApiModelProperty(value = "基线最小负荷")
    private BigDecimal minLoadBaseline;

    /**
     * 基线平均负荷
     */
    @ApiModelProperty(value = "基线平均负荷")
    private BigDecimal avgLoadBaseline;

    /**
     * 异常描述
     */
    @ApiModelProperty(value = "异常描述")
    private String exceptionRemark;

    /**
     * 是否异常,数据字典获取
     */
    @ApiModelProperty(value = "是否异常,数据字典获取")
    private String normal;

    /**
     * 计算规则1、2、3,数据字典获取
     */
    @ApiModelProperty(value = "计算规则1、2、3,数据字典获取")
    private String calRule;

    /**
     * 标识，逗号分开
     */
    @ApiModelProperty(value = "标识，逗号分开")
    private String simplesId;

    /**
     * p1
     */
    @ApiModelProperty(value = "p1")
    private BigDecimal p1;

    /**
     * p2
     */
    @ApiModelProperty(value = "p2")
    private BigDecimal p2;

    /**
     * p3
     */
    @ApiModelProperty(value = "p3")
    private BigDecimal p3;

    /**
     * p4
     */
    @ApiModelProperty(value = "p4")
    private BigDecimal p4;

    /**
     * p5
     */
    @ApiModelProperty(value = "p5")
    private BigDecimal p5;

    /**
     * p6
     */
    @ApiModelProperty(value = "p6")
    private BigDecimal p6;

    /**
     * p7
     */
    @ApiModelProperty(value = "p7")
    private BigDecimal p7;

    /**
     * p1
     */
    @ApiModelProperty(value = "p8")
    private BigDecimal p8;

    /**
     * p9
     */
    @ApiModelProperty(value = "p9")
    private BigDecimal p9;

    /**
     * p10
     */
    @ApiModelProperty(value = "p10")
    private BigDecimal p10;

    /**
     * p11
     */
    @ApiModelProperty(value = "p11")
    private BigDecimal p11;

    /**
     * p12
     */
    @ApiModelProperty(value = "p12")
    private BigDecimal p12;

    /**
     * p13
     */
    @ApiModelProperty(value = "p13")
    private BigDecimal p13;

    /**
     * p14
     */
    @ApiModelProperty(value = "p14")
    private BigDecimal p14;

    /**
     * p15
     */
    @ApiModelProperty(value = "p15")
    private BigDecimal p15;

    /**
     * p16
     */
    @ApiModelProperty(value = "p16")
    private BigDecimal p16;

    /**
     * p17
     */
    @ApiModelProperty(value = "p17")
    private BigDecimal p17;

    /**
     * p18
     */
    @ApiModelProperty(value = "p18")
    private BigDecimal p18;

    /**
     * p19
     */
    @ApiModelProperty(value = "p19")
    private BigDecimal p19;

    /**
     * p20
     */
    @ApiModelProperty(value = "p20")
    private BigDecimal p20;

    /**
     * p21
     */
    @ApiModelProperty(value = "p21")
    private BigDecimal p21;

    /**
     * p22
     */
    @ApiModelProperty(value = "p22")
    private BigDecimal p22;

    /**
     * p23
     */
    @ApiModelProperty(value = "p23")
    private BigDecimal p23;

    /**
     * p24
     */
    @ApiModelProperty(value = "p24")
    private BigDecimal p24;

    /**
     * p25
     */
    @ApiModelProperty(value = "p25")
    private BigDecimal p25;

    /**
     * p26
     */
    @ApiModelProperty(value = "p26")
    private BigDecimal p26;

    /**
     * p27
     */
    @ApiModelProperty(value = "p27")
    private BigDecimal p27;

    /**
     * p28
     */
    @ApiModelProperty(value = "p28")
    private BigDecimal p28;

    /**
     * p29
     */
    @ApiModelProperty(value = "p29")
    private BigDecimal p29;

    /**
     * p30
     */
    @ApiModelProperty(value = "p30")
    private BigDecimal p30;

    /**
     * p31
     */
    @ApiModelProperty(value = "p31")
    private BigDecimal p31;

    /**
     * p32
     */
    @ApiModelProperty(value = "p32")
    private BigDecimal p32;

    /**
     * p33
     */
    @ApiModelProperty(value = "p33")
    private BigDecimal p33;

    /**
     * p34
     */
    @ApiModelProperty(value = "p34")
    private BigDecimal p34;

    /**
     * p35
     */
    @ApiModelProperty(value = "p35")
    private BigDecimal p35;

    /**
     * p36
     */
    @ApiModelProperty(value = "p36")
    private BigDecimal p36;

    /**
     * p37
     */
    @ApiModelProperty(value = "p37")
    private BigDecimal p37;

    /**
     * p38
     */
    @ApiModelProperty(value = "p38")
    private BigDecimal p38;

    /**
     * p39
     */
    @ApiModelProperty(value = "p39")
    private BigDecimal p39;

    /**
     * p40
     */
    @ApiModelProperty(value = "p40")
    private BigDecimal p40;

    /**
     * p41
     */
    @ApiModelProperty(value = "p41")
    private BigDecimal p41;

    /**
     * p42
     */
    @ApiModelProperty(value = "p42")
    private BigDecimal p42;

    /**
     * p43
     */
    @ApiModelProperty(value = "p43")
    private BigDecimal p43;

    /**
     * p44
     */
    @ApiModelProperty(value = "p44")
    private BigDecimal p44;

    /**
     * p45
     */
    @ApiModelProperty(value = "p45")
    private BigDecimal p45;

    /**
     * p46
     */
    @ApiModelProperty(value = "p46")
    private BigDecimal p46;

    /**
     * p47
     */
    @ApiModelProperty(value = "p47")
    private BigDecimal p47;

    /**
     * p48
     */
    @ApiModelProperty(value = "p48")
    private BigDecimal p48;

    /**
     * p49
     */
    @ApiModelProperty(value = "p49")
    private BigDecimal p49;

    /**
     * p50
     */
    @ApiModelProperty(value = "p50")
    private BigDecimal p50;

    /**
     * p51
     */
    @ApiModelProperty(value = "p51")
    private BigDecimal p51;

    /**
     * p52
     */
    @ApiModelProperty(value = "p52")
    private BigDecimal p52;

    /**
     * p53
     */
    @ApiModelProperty(value = "p53")
    private BigDecimal p53;

    /**
     * p54
     */
    @ApiModelProperty(value = "p54")
    private BigDecimal p54;

    /**
     * p55
     */
    @ApiModelProperty(value = "p55")
    private BigDecimal p55;

    /**
     * p56
     */
    @ApiModelProperty(value = "p56")
    private BigDecimal p56;

    /**
     * p57
     */
    @ApiModelProperty(value = "p57")
    private BigDecimal p57;

    /**
     * p58
     */
    @ApiModelProperty(value = "p58")
    private BigDecimal p58;

    /**
     * p59
     */
    @ApiModelProperty(value = "p59")
    private BigDecimal p59;

    /**
     * p60
     */
    @ApiModelProperty(value = "p60")
    private BigDecimal p60;

    /**
     * p61
     */
    @ApiModelProperty(value = "p61")
    private BigDecimal p61;

    /**
     * p62
     */
    @ApiModelProperty(value = "p62")
    private BigDecimal p62;

    /**
     * p63
     */
    @ApiModelProperty(value = "p63")
    private BigDecimal p63;

    /**
     * p64
     */
    @ApiModelProperty(value = "p64")
    private BigDecimal p64;

    /**
     * p65
     */
    @ApiModelProperty(value = "p65")
    private BigDecimal p65;

    /**
     * p66
     */
    @ApiModelProperty(value = "p66")
    private BigDecimal p66;

    /**
     * p67
     */
    @ApiModelProperty(value = "p67")
    private BigDecimal p67;

    /**
     * p68
     */
    @ApiModelProperty(value = "p68")
    private BigDecimal p68;

    /**
     * p69
     */
    @ApiModelProperty(value = "p69")
    private BigDecimal p69;

    /**
     * p70
     */
    @ApiModelProperty(value = "p70")
    private BigDecimal p70;

    /**
     * p71
     */
    @ApiModelProperty(value = "p71")
    private BigDecimal p71;

    /**
     * p72
     */
    @ApiModelProperty(value = "p72")
    private BigDecimal p72;

    /**
     * p73
     */
    @ApiModelProperty(value = "p73")
    private BigDecimal p73;

    /**
     * p74
     */
    @ApiModelProperty(value = "p74")
    private BigDecimal p74;

    /**
     * p75
     */
    @ApiModelProperty(value = "p75")
    private BigDecimal p75;

    /**
     * p76
     */
    @ApiModelProperty(value = "p76")
    private BigDecimal p76;

    /**
     * p77
     */
    @ApiModelProperty(value = "p77")
    private BigDecimal p77;

    /**
     * p78
     */
    @ApiModelProperty(value = "p78")
    private BigDecimal p78;

    /**
     * p79
     */
    @ApiModelProperty(value = "p79")
    private BigDecimal p79;

    /**
     * p80
     */
    @ApiModelProperty(value = "p80")
    private BigDecimal p80;

    /**
     * p81
     */
    @ApiModelProperty(value = "p81")
    private BigDecimal p81;

    /**
     * p82
     */
    @ApiModelProperty(value = "p82")
    private BigDecimal p82;

    /**
     * p83
     */
    @ApiModelProperty(value = "p83")
    private BigDecimal p83;

    /**
     * p84
     */
    @ApiModelProperty(value = "p84")
    private BigDecimal p84;

    /**
     * p85
     */
    @ApiModelProperty(value = "p85")
    private BigDecimal p85;

    /**
     * p86
     */
    @ApiModelProperty(value = "p86")
    private BigDecimal p86;

    /**
     * p87
     */
    @ApiModelProperty(value = "p87")
    private BigDecimal p87;

    /**
     * p88
     */
    @ApiModelProperty(value = "p88")
    private BigDecimal p88;

    /**
     * p89
     */
    @ApiModelProperty(value = "p89")
    private BigDecimal p89;

    /**
     * p90
     */
    @ApiModelProperty(value = "p90")
    private BigDecimal p90;

    /**
     * p91
     */
    @ApiModelProperty(value = "p91")
    private BigDecimal p91;

    /**
     * p92
     */
    @ApiModelProperty(value = "p92")
    private BigDecimal p92;

    /**
     * p93
     */
    @ApiModelProperty(value = "p93")
    private BigDecimal p93;

    /**
     * p94
     */
    @ApiModelProperty(value = "p94")
    private BigDecimal p94;

    /**
     * p95
     */
    @ApiModelProperty(value = "p95")
    private BigDecimal p95;

    /**
     * p96
     */
    @ApiModelProperty(value = "p96")
    private BigDecimal p96;

}
