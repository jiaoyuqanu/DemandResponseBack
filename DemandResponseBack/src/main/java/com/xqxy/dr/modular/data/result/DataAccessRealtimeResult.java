package com.xqxy.dr.modular.data.result;

import lombok.Data;

@Data
public class DataAccessRealtimeResult {

    private String requestId;
    private Integer errCode;
    private String errMsg;
    private DataAccessRealtimeData Data;
    private String apiLog;

}
