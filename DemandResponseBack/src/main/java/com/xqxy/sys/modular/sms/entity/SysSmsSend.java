package com.xqxy.sys.modular.sms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 短信发送记录
 * </p>
 *
 * @author xiao jun
 * @since 2021-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_sys_sms_send")
public class SysSmsSend extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 手机号
     */
    private String phoneNumbers;

    /**
     * 短信验证码
     */
    private String validateCode;

    /**
     * 短信模板ID
     */
    private String templateCode;

    /**
     * 短信内容
     */
    private String content;

    /**
     * 回执id，可根据该id查询具体的发送状态
     */
    private String bizId;

    /**
     * 发送状态（字典 0 未发送，1 待发送，2 已发送，3 发送成功，4 发送失败，5 失效）
     */
    private Integer status;

    /**
     * 来源（字典 1 app， 2 pc， 3 其他）
     */
    private Integer source;

    /**
     * 业务关联：  业务编码_业务编码
     */
    private String businessRela;

    /**
     * 业务关联：  业务编码_业务标识
     */
    private String businessCode;

    /**
     * 预发送时间
     */
    private Date preSendTime;

    /**
     * 失效时间
     */
    private Date invalidTime;

    /**
     * 发送次数
     */
    private Integer sendTimes;

    /**
     * 发送时间
     */
    private Date sendTime;

    /**
     * 失败原因
     */
    private String failInfo;

    /**
    *  户号/社会信用代码
    */
    private String userNo;

    /**
     *  客户名称
     */
    private String userName;

    /**
     *  参与方式
     */
    private String joinUserType;

}
