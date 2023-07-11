package com.xqxy.dr.modular.event.entity;

import com.xqxy.core.pojo.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 方案参与用户
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CustMessage extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    private String custId;//集成商id

    private String custName;//集成商名称

    private String creditCode;//统一信用编码

    private String phone;//手机号

    private String content;//短信内容

    private String state;//短信状态

    private String updateLatsTime;//最后更新时间


}
