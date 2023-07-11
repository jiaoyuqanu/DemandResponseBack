package com.xqxy.dr.modular.grsg.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrApplyRecVO {

    private Long id;

    /**
     * 绿色国网传过来的工单编号
     */
    private String orderNum;

    /**
     * 绿色国网传过来的应用编码
     */
    private String appCode;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 联系人电话
     */
    private String contactPhone;

    /**
     * 登录账号
     */
    private String loginName;

    /**
     * 企业名称
     */
    private String organName;

    /**
     * 统一社会信用代码
     */
    private String creditCode;

    /**
     * 工单描述
     */
    private String orderDesc;

    /**
     * 工单开始日期
     */
    private String beginDate;

    /**
     * 绿色国网凭证
     */
    private String ai;

    /**
     * 用户所在企业户号列表,逗号隔开
     */
    private String consNoList;

    /**
     * 工单状态，1 申请，2 通过 3 不通过
     */
    private String state;
}
