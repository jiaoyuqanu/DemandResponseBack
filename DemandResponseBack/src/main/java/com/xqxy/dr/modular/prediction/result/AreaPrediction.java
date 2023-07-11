package com.xqxy.dr.modular.prediction.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xqxy.dr.modular.prediction.param.AreaCurve;
import com.xqxy.dr.modular.prediction.param.AreaCurveBase;
import com.xqxy.dr.modular.prediction.param.AreaCurveLower;
import com.xqxy.dr.modular.prediction.param.AreaCurveUpper;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.util.List;

@Data
public class AreaPrediction {

    private String areaId;      //区域id

    @ApiModelProperty(value = "PROVINCE_CODE 省码CITY_CODE 市码COUNTY_CODE  区县码STREET_CODE  街道码/乡镇")
    private String areaType;       //区域类型  province_code city_code county_code street_code

    private long electricalAreaId;

    private List<String> areaIds; //下級区域id集合

    private String statDate;

    private AreaCurveBase areaCurveBase;

    private AreaCurveLower areaCurveLower;

    private AreaCurveUpper areaCurveUpper;

    private AreaCurve areaCurve;

    @JsonIgnore
    private String ignore;

}
