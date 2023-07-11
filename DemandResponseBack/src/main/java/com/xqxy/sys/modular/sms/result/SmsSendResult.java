package com.xqxy.sys.modular.sms.result;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class SmsSendResult implements Serializable {
    /**
     *用户Id
     */
    private Long userId;
    /**
     *短信Id
     */
    private Long smsId;
    /**
     *省码
     */
    private String  provinceCode;
    /**
     *市码
     */
    private String cityCode;
    /**
     *区县码
     */
    private String countyCode;
    /**
     *用户名称
     */
    private String userName;
    /**
     * 用户类型
     */
    private String userType;
    /**
     * 用户注册时间
     */
    private LocalDate registerTime;
    /**
     * 短信创建时间
     */
    private LocalDate createTime;
    /**
     * 短信内容
     */
    private String content;
    /**
     * 短信发送状态
     */
    private String status;
    /**
     * 用户账号
     */
    private String account;

}
