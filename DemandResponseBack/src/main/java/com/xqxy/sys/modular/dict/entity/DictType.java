package com.xqxy.sys.modular.dict.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 字典类型
 * </p>
 *
 * @author xiao jun
 * @since 2021-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_sys_dict_type")
public class DictType {

    /**
     * 本实体记录的唯一标识，产生规则为流水号
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long typeId;

    /**
     * 名称
     */
    private String name;

    /**
     * 编码
     */
    private String code;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态（字典 0正常 1停用 2删除）
     */
    private Integer status;


}
