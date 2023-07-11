package com.xqxy.dr.modular.custConfig.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author liqirui
 * @since 2021-11-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DrCustSysConfig implements Serializable {

private static final long serialVersionUID=1L;

    /**
     * 标识
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 本实体记录的唯一标识，产生规则为流水号
     */
    @TableField("CUST_ID")
    private Long custId;

    /**
     * 客户名称
     */
    @TableField("CUST_NAME")
    private String custName;

    /**
     * 接口地址，对方系统的完整路径，如：http://192.168.1.1/api
     */
    @TableField("API_URL")
    private String apiUrl;

    /**
     * 接口业务类型:1：邀约下发，2事件下发
     */
    @TableField("API_TYPE")
    private String apiType;

    /**
     * http请求方式,get，post,update,delete
     */
    @TableField("METHOD")
    private String method;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 创建人
     */
    @TableField("CREATE_USER")
    private Long createUser;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 更新人
     */
    @TableField("UPDATE_USER")
    private Long updateUser;


}
