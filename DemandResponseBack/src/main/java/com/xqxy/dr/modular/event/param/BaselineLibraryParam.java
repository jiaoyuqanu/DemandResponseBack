package com.xqxy.dr.modular.event.param;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-18
 */
@ApiModel(description = "基线库 参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class BaselineLibraryParam extends BaseParam implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "基线标识")
      private Long baselinId;

    @ApiModelProperty(value = "描述")
    private String descr;

    @ApiModelProperty(value = "基线生成日：即首次需求响应日")
    private LocalDate generateDate;

    @ApiModelProperty(value = "响应开始时段:hh:mm:ss")
    private String startPeriod;

    @ApiModelProperty(value = "响应结束时段:hh:mm:ss")
    private String endPeriod;


    @ApiModelProperty(value = "日期逗号隔开，20210101，20210102，20210103")
    private String simplesDate;

    @ApiModelProperty(value = "计算规则，1，2，3")
    private String calRule;

    @ApiModelProperty(value = "基线类型，1：时间段基线，2：全天基线")
    private String baselineType;

    @ApiModelProperty(value = "生成方式，1：自动生成，2：手动创建")
    private String genType;


}
