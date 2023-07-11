package com.xqxy.dr.modular.grsg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 绿色国网业务申请记录表
 * </p>
 *
 * @author liqirui
 * @since 2021-11-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("dr_apply_rec")
public class DrApplyRec implements Serializable {

private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
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
     * 工单状态，1 申请，2 通过 3 不通过,4已推送,5推送失败
     */
    private String state;
}
