package com.xqxy.sys.modular.sms.param;

import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
@ApiModel(description = "认证审核短信管理")
@Data
public class SmsParam extends BaseParam {
    private String pageNo;
    private String pageSize;
    /**
     * 用户地址省码，说明用户地址属于哪个省，引用国家标准GB T 2260-2002
     */
    @ApiModelProperty(value = "省码")
    private String provinceCode;//省码
    /**
     * 市码:用户地址市码，说明用户地址属于哪个市(地区),,引用国家标准GB T 2260-2002
     */
    @ApiModelProperty(value = "市码")
    private String cityCode;//市码
    /**
     * 用户地址区县码，说明用户地址属于哪个市直辖区或市直属县，引用国家标准GB T 2260-2002
     */
    @ApiModelProperty(value = "区县码")
    private String countyCode;
    /**
     * 用户类型(负荷集成商：1, 直接参与用户：2 代理参与用户：3
     */
    private String userType;
    /**
     * 发送状态（字典 0 未发送，1 待发送，2 已发送，3 发送成功，4 发送失败，5 失效）
     */
    private String status;

    /**
     * 账号
     */
    private String account;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 短信类型
     */
    private String templateType;
    /**
     * 注册时间
     */
    private String startTime;

    /**
     * 注册时间
     */
    private String endTime;

    /**
     * 审核结果
     */
    private String approvalResult;

    private String firstContactInfo;
    /**
     * 用户Id的集合
     */
    private List<String>userIds;
    /**
     * 是否存在短信 （ 0：未生成 1：已生成）
     */
    private String isExist;
    /**
     * 短信Id的集合
     */
    private List<Long>smsIds;

    /**
     * 签约Id的集合
     */
    private List<String> declareIds;

}
