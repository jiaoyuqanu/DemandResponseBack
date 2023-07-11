package com.xqxy.dr.modular.data.result.energyResult;

import lombok.Data;

@Data
public class DataCenterEnergyResult {

    private String code;
    private String msg;
    private DataCenterEnergyInnerResult data;
    private Long execMillis;
    private String resp_by;
}
