package com.xqxy.dr.modular.adjustable.DTO;


import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@ApiModel(description = "用户可调节潜力  参数")
public class DrConsAdjustablePotentialDTO extends BaseParam {

    /**
     * 主键
     */
    @Excel(name = "主键",width = 10)
    private String id;
    /**
     * 用户名称
     */
    private String consName;

    /**
     * 用户id
     */
    private String consId;

    /**
     * 普查年度
     */
    private String year;

    /**
     * 市码
     */
    private String cityCode;

    /**
     * 供电单位
     */
    private List<String> orgIds;

    private String orgId;

}
