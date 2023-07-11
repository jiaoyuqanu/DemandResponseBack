package com.xqxy.dr.modular.adjustable.DTO;

import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
@ApiModel(description = "设备可调节潜力  参数")
public class DrDeviceAdjustablePotentialDTO extends BaseParam {

    /**
     * 普查年度
     */
    private String year;

    /**
     * 市码
     */
    private String cityCode;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备类型名称
     */
    private String deviceTypeName;

    /**
     * 设备类型编码
     */
    private String deviceTypeCode;

    /**
     * 用户编号
     */
    private String consId;

    /**
     * 供电编码
     */
    private List<String> orgIds;
}
