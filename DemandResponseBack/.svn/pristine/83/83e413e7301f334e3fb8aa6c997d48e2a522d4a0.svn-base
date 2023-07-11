package com.xqxy.dr.modular.event.param;

import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
public class BuinessOperationParam extends BaseParam {

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

    /**
     * 事件类型
     */
    @ApiModelProperty(value = "事件类型")
    private String eventType;

    /**
     * 省份编码
     */
    @ApiModelProperty(value = "省份编码")
    @NotNull(message = "provinceCode不能为空，请检查provinceCode参数", groups = {page.class})
    private Long provinceCode;

    /**
     * 市编码
     */
    @ApiModelProperty(value = "市编码")
    private Long cityCode;


}
