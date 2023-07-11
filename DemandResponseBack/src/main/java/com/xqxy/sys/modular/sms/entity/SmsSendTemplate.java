package com.xqxy.sys.modular.sms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 短信发送模板
 * </p>
 *
 * @author xiao jun
 * @since 2021-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_sys_sms_send_template")
public class SmsSendTemplate extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 模板标识
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long templateId;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板类型
     */
    private String templateType;

    /**
     * 模板内容
     */
    private String templateContent;

    private String isValid;

    private String isExec;

    /**
     * 创建人名称
     */
    @TableField(exist = false)
    private String createUsername;

}
