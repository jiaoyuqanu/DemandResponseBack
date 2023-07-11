package com.xqxy.dr.modular.event.VO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
public class PlanConsVO implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 参与标识
     */
    private Long particId;

    /**
     * 方案标识
     */
    private Long planId;

    /**
     * 用户标识
     */
    private String consId;

    /**
     * 关联客户标识
     */
    private Long custId;

    /**
     * 数据字典：1直接参与，2代理参与

     */
    private String participantType;

    /**
     * 基线负荷标识
     */
    private Long baselineCapId;

    /**
     * 可响应负荷
     */
    private BigDecimal demandCap;

    /**
     * 响应价格
     */
    private BigDecimal replyPrice;

    /**
     * 序位
     */
    private Integer sequenceNo;

    /**
     * 是否被剔除
     */
    private String deleted;

    /**
     * 被剔除使用的规则
     */
    private String delRule;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private Long updateUser;

    /**
     * 反馈是否参与事件
     */
    private String involvedIn;

    /**
     * 是否参与执行，反馈后方案编制最终确认参与用户标志，字典yes_or_no
     */
    private String implement;

    /**
     * 用户参与方式1：独立参与用户，2：代理参与用户，3：负荷聚合商
     */
    private String joinUserType;

    /**
     * 签约响应容量
     */
    private BigDecimal contractCap;

    /**
     * 参与日期
     */
    private LocalDate date;


}
