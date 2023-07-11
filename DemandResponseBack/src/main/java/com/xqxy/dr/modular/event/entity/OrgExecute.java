package com.xqxy.dr.modular.event.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 组织机构执行信息
 * </p>
 *
 * @author liqirui
 * @since 2022-03-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("dr_org_execute")
public class OrgExecute implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 本实体记录的唯一标识，产生规则为流水号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联事件ID
     */
    private Long eventId;

    /**
     * 关联组织机构ID
     */
    private String orgId;

    /**
     * 组织机构名称
     */
    private String orgName;

    /**
     * 基线负荷
     */
    private BigDecimal baselineCap;

    /**
     * 基线最大负荷
     */
    private BigDecimal maxLoadBaseline;

    /**
     * 响应负荷确认值
     */
    private BigDecimal replyCap;

    /**
     * 实时负荷
     */
    private BigDecimal realTimeCap;

    /**
     * 执行时间（yyyy-MM-dd HH:mm:ss）
     */
    private Date executeTime;

    /**
     * 实时执行负荷:基线负荷与实时负荷差值
     */
    private BigDecimal executeCap;

    /**
     * 实时执行率:执行负荷/响应负荷确认值计算百分比
     */
    private BigDecimal executeRate;

    /**
     * 是否越界:(是：Y，否：N)
     */
    private String isOut;

    /**
     * 是否达标:(是：Y，否：N)
     */
    private String isQualified;

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
     * 实时执行负荷:基线负荷与实时负荷差值
     */
    private BigDecimal maxExecuteCap;


}
