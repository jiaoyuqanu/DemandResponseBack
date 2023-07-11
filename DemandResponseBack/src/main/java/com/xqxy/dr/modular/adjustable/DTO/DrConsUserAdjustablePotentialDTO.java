package com.xqxy.dr.modular.adjustable.DTO;


import cn.afterturn.easypoi.excel.annotation.Excel;
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
@ApiModel(description = "电力用户/用户可调节潜力  参数")
public class DrConsUserAdjustablePotentialDTO extends BaseParam {



    @ApiModelProperty("用户名称")
    private String consName;

    @ApiModelProperty("用户id")
    private String consId;

    @ApiModelProperty("普查年度")
    private String year;

    @ApiModelProperty("市码")
    private String cityCode;

    @ApiModelProperty("供电单位")
    private String orgId;

    @ApiModelProperty("行业类别")
    private String bigClassCode;

}
