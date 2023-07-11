package com.xqxy.dr.modular.event.param;

import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 业务运行统计
 * </p>
 *
 * @author chen zhi jun
 * @since 2021-03-11
 */
@ApiModel(description = "业务运行统计")
@EqualsAndHashCode(callSuper = true)
@Data
public class EventUserParam extends BaseParam {

    /**
     * 事件id
     */
    @ApiModelProperty(value = "事件id")
    private Long eventId;

    /**
     * 开始时间
     */
    @NotNull(message = "startDate不能为空，请检查startDate参数", groups = {page.class})
    @ApiModelProperty(value = "开始时间")
    private String startDate;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @NotNull(message = "endDate不能为空，请检查endDate参数", groups = {page.class})
    private String endDate;

    @ApiModelProperty(value = "年份")
    private String year;



}
