package com.xqxy.sys.modular.cust.result;


import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private String Code;
    private String mesg;
    private String time;
    private JSONObject data;

}
