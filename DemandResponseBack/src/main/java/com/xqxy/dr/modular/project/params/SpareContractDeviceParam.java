package com.xqxy.dr.modular.project.params;

import com.xqxy.core.pojo.base.param.BaseParam;
import com.xqxy.dr.modular.project.entity.SpareContractDevice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * spare device contraction param
 * </p>
 *
 * @author Caoj
 * @date 2022-01-07 14:41
 */

@ApiModel(description = "需求响应项目明细 参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class SpareContractDeviceParam extends BaseParam {
    private static final long serialVersionUID = -4080891753731916852L;

    @ApiModelProperty(value = "用户户号")
    private String consId;

    @ApiModelProperty(value = "项目明细标识")
    private Long projectDetailId;

    @ApiModelProperty(value = "设备信息集合")
    private List<SpareContractDevice> deviceList;

    private String detailId;

    @ApiModelProperty(value = "备用容量")
    private BigDecimal spareCap;

    private Long contractDetailId;

    @ApiModelProperty(value = "签约标识")
    private Long contractId;
}
