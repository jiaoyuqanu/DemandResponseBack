package com.xqxy.dr.modular.data.result.centerResult;

import lombok.Data;

import java.util.List;

@Data
public class DataAccessHistoryData {

    private Integer total;
//    private Integer pageSize;
    private List<DataAccessHistoryCurve> list;
//    private Integer pageNum;
}
