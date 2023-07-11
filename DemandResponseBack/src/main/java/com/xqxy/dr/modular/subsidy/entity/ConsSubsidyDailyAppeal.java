package com.xqxy.dr.modular.subsidy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 日补贴
 * </p>
 *
 * @author Shen
 * @since 2021-10-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_cons_subsidy_daily_appeal")
@ApiModel(value="ConsSubsidyDaily对象", description="日补贴")
public class ConsSubsidyDailyAppeal extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;


}
