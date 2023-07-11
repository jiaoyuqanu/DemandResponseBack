package com.xqxy.sys.modular.cust.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 用户户号关联表
 * </p>
 *
 * @author Caoj
 * @date 2021-10-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_user_cons_rela")
public class UserConsRela implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long custId;

    /**
     * 关联户号
     */
    private String consNo;

    /**
     * 1:所属关系，2代理关系
     */
    private String relaType;

    /**
     * 用户名称
     */
    @TableField(exist = false)
    private String consName;


}
