package com.xqxy.dr.modular.powerplant.param;

import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 电力市场竞价公告信息参数
 * </p>
 *
 * @author czj
 * @date 2021-12-10 9:07
 */
@Data
public class DrDaElectricBidNoticeParam extends BaseParam implements Serializable {

    /**
     *
     */
    @ApiModelProperty(value = "服务类型code")
    private String electricTypeCode;

    /**
     * 发布时间（年月日时分秒）
     */
    @ApiModelProperty(value = "发布时间")
    private String releaseTime;

}
