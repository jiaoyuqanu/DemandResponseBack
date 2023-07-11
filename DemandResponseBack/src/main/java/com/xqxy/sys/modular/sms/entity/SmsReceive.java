package com.xqxy.sys.modular.sms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 短信接收记录
 * </p>
 *
 * @author xiao jun
 * @since 2021-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_sys_sms_receive")
public class SmsReceive implements Serializable {

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
     * 短信内容
     */
    private String content;

    /**
     * 用户上行服务号码，服务号码包括了短信运营商服务号
     */
    private String serviceNumber;

    /**
     * 接收时间
     */
    private LocalDateTime receiveTime;


}
