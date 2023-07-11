package com.xqxy.dr.modular.dispatch.param;

import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel(description = "新增调度指令 参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class DispatchAndSoltParam extends BaseParam {

    @ApiModelProperty(value = "指令信息")
    @NotNull(message = "指令信息不能为空，请检查dispatch参数", groups = {add.class})
    @Valid
    private DispatchParam dispatchParam;

    @ApiModelProperty(value = "指令时段")
    @NotNull(message = "指令时段不能为空，请检查dispatch参数", groups = {add.class})
    @Valid
    private List<DispatchSoltParam> dispatchSolts;

}
