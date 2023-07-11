package com.xqxy.sys.modular.cust.entity;

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
 * 用户拓扑图表
 * </p>
 *
 * @author liqirui
 * @since 2022-05-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_cons_topology_file")
public class ConsTopologyFile extends BaseEntity implements Serializable{

    private static final long serialVersionUID=1L;

    /**
     * 标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户标识
     */
    @TableField("CONS_ID")
    private String consId;

    /**
     * 关联文件ID
     */
    @TableField("file_id")
    private Long fileId;

    /**
     * 文件类型
     */
    @TableField("file_name")
    private String fileName;

    /**
     * 文件类型
     */
    @TableField("file_type")
    private String fileType;
}
