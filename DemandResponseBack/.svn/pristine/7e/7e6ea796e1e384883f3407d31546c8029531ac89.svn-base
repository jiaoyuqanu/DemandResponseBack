package com.xqxy.dr.modular.gwapp.param;

import com.xqxy.core.pojo.base.param.BaseParam;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class GwappCustPageQuery extends BaseParam {

    private String legalName;

    private String legalPhone;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date applyStartTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date applyEndTime;

    private Integer checkStatus;

}
