package com.xqxy.dr.modular.anhui.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 国泉营销用户档案表映射类
 * @author liuyu
 * @since 2021-12-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class HefeiCons {

    /**
     * 服务区编码
     */
    private String areaNo;
    /**
     * 服务单位编码
     */
    private String orgNo;
    /**
     * 客户标识
     */
    private Long custId;
    /**
     * 客户编号
     */
    private String custNo;
    /**
     * 客户名称
     */
    private String custName;
    /**
     * 账号标识
     */
    private Long userId;
    /**
     * 所属集团标识
     */
    private String cliqueId;
    /**
     * 客户状态，01正常，02停用，03注销
     */
    private String custStatus;
    /**
     * 客户类型：10商业楼宇，20工业企业，30园区，40社区，50乡村
     */
    private String custType;
    /**
     * 营销用户编号
     */
    private String consNo;
    /**
     * 营销用户标识
     */
    private String consId;
    /**
     *运行容量
     */
    private BigDecimal runCap;
    /**
     * 电压等级
     */
    private String voltCode;
    /**
     * 行业类别
     */
    private String tradeCode;
    /**
     * 用电类别
     */
    private String elecTypeCode	;
    /**
     * 抄表列日
     */
    private String mrDay;
    /**
     * 邀请码
     */
    private String invitationCode;
    /**
     * 联系人名称
     */
    private String contactName;
    /**
     * 联系人电话
     */
    private String phoneNumber;
    /**
     * 联系地址
     */
    private String address;

    /**
     * 企业简介
     */
    private String introduction	;
    /**
     * cps服务商标识
     */
    private Long cpsProviderId;
    /**
     * 客户序列号
     */
    private Integer serialNumber;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 重点领域用户，01医院，02政府机构，03学习，04交通枢纽，05场馆，06高耗能企业
     */
    private String keyAreaUsers	;

}
