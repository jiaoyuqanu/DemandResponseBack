package com.xqxy.dr.modular.data.result;

import lombok.Data;

import java.util.List;

@Data
public class DataAccessInnerResult {
    private String total;
    private List<DataAccessCurve> list;
    /*private Long execMillis;
    private String resp_by;*/
}
