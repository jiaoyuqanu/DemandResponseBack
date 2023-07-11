package com.xqxy.dr.modular.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 客户签约文件表
 * </p>
 *
 * @author liqirui
 * @since 2022-03-30
 */
@Data
@TableName("dr_cust_contract_file")
public class CustContractFile extends BaseEntity implements Serializable{

private static final long serialVersionUID=1L;

    /**
     * 标识
     */
    private Long id;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 本实体记录的唯一标识，产生规则为流水号
     */
    @TableField("CUST_ID")
    private Long custId;

    /**
     * 关联文件ID
     */
    private Long fileId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件类型
     */
    private String fileType;



}
