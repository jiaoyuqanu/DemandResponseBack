package com.xqxy.dr.modular.event.param;

import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;

@ApiModel(description = "执行监测 -- 组织机构监测传参")
@EqualsAndHashCode()
@Data
public class OrgExecuteParam extends BaseParam {

    /**
     * 事件标识
     */
    private Long eventId;

    /**
     * 调控日期
     */
    private LocalDate regulateDate;


    /**
     * 供电单位 id
     */
    private String orgNo;

    /**
     * 下属 供电单位 集合
     */
    private ArrayList<String> orgNos;

}
