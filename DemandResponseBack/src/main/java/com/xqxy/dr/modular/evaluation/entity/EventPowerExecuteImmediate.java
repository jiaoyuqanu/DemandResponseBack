package com.xqxy.dr.modular.evaluation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 * 用户执行曲线-实时
 * </p>
 *
 * @author xiao jun
 * @since 2021-07-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_event_power_execute_immediate")
public class EventPowerExecuteImmediate implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 本实体记录的唯一标识，产生规则为流水号
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long dataId;

    /**
     * 关联用户ID
     */
    private String consId;

    /**
     * 关联事件ID
     */
    private Long eventId;

    /**
     * 数据日期
     */
    private LocalDate dataDate;

    /**
     * 数据点数标志	(1：96点2:	48点3:	24点)		
     */
    private String dataPointFlag;

    private BigDecimal p1;

    private BigDecimal p2;

    private BigDecimal p3;

    private BigDecimal p4;

    private BigDecimal p5;

    private BigDecimal p6;

    private BigDecimal p7;

    private BigDecimal p8;

    private BigDecimal p9;

    private BigDecimal p10;

    private BigDecimal p11;

    private BigDecimal p12;

    private BigDecimal p13;

    private BigDecimal p14;

    private BigDecimal p15;

    private BigDecimal p16;

    private BigDecimal p17;

    private BigDecimal p18;

    private BigDecimal p19;

    private BigDecimal p20;

    private BigDecimal p21;

    private BigDecimal p22;

    private BigDecimal p23;

    private BigDecimal p24;

    private BigDecimal p25;

    private BigDecimal p26;

    private BigDecimal p27;

    private BigDecimal p28;

    private BigDecimal p29;

    private BigDecimal p30;

    private BigDecimal p31;

    private BigDecimal p32;

    private BigDecimal p33;

    private BigDecimal p34;

    private BigDecimal p35;

    private BigDecimal p36;

    private BigDecimal p37;

    private BigDecimal p38;

    private BigDecimal p39;

    private BigDecimal p40;

    private BigDecimal p41;

    private BigDecimal p42;

    private BigDecimal p43;

    private BigDecimal p44;

    private BigDecimal p45;

    private BigDecimal p46;

    private BigDecimal p47;

    private BigDecimal p48;

    private BigDecimal p49;

    private BigDecimal p50;

    private BigDecimal p51;

    private BigDecimal p52;

    private BigDecimal p53;

    private BigDecimal p54;

    private BigDecimal p55;

    private BigDecimal p56;

    private BigDecimal p57;

    private BigDecimal p58;

    private BigDecimal p59;

    private BigDecimal p60;

    private BigDecimal p61;

    private BigDecimal p62;

    private BigDecimal p63;

    private BigDecimal p64;

    private BigDecimal p65;

    private BigDecimal p66;

    private BigDecimal p67;

    private BigDecimal p68;

    private BigDecimal p69;

    private BigDecimal p70;

    private BigDecimal p71;

    private BigDecimal p72;

    private BigDecimal p73;

    private BigDecimal p74;

    private BigDecimal p75;

    private BigDecimal p76;

    private BigDecimal p77;

    private BigDecimal p78;

    private BigDecimal p79;

    private BigDecimal p80;

    private BigDecimal p81;

    private BigDecimal p82;

    private BigDecimal p83;

    private BigDecimal p84;

    private BigDecimal p85;

    private BigDecimal p86;

    private BigDecimal p87;

    private BigDecimal p88;

    private BigDecimal p89;

    private BigDecimal p90;

    private BigDecimal p91;

    private BigDecimal p92;

    private BigDecimal p93;

    private BigDecimal p94;

    private BigDecimal p95;

    private BigDecimal p96;


}
