package com.xqxy.sys.modular.dict.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 字典值
 * </p>
 *
 * @author xiao jun
 * @since 2021-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_sys_dict_data")
public class DictData {

    /**
     * 本实体记录的唯一标识，产生规则为流水号
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long dataId;

    /**
     * 本实体记录的唯一标识，产生规则为流水号
     */
    private Long typeId;

    /**
     * 值
     */
    private String value;

    /**
     * 编码
     */
    private String code;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 排序
     */
    private String remark;

    /**
     * 状态（字典 0正常 1停用 2删除）
     */
    private Integer status;

    /**
     * 父级标识
     */
    private String parentId;

    /**
     * 等级
     */
    private String level;

    /**
     * 子集
     */
    @TableField(exist = false)
    private List children;


}
