package com.xqxy.dr.modular.event.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;


@ApiModel(description = "方案执行用户 参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class ExecutePlanConsParam extends BaseParam {
    /**
     * 方案关联的事件id
     */
    @ApiModelProperty(value = "方案关联的事件id")
    @NotNull(message = "eventId不能为空，请检查eventId参数", groups = {edit.class,detail.class,page.class})
    private Long eventId;


    /**
     * 是否集成商1是0否
     */
    @ApiModelProperty(value = "是否集成商1是0否")
    @NotNull(message = "integrator不能为空，请检查integrator参数", groups = {page.class})
    private String integrator;

}
