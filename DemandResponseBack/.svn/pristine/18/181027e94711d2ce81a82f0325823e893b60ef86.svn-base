package com.xqxy.dr.modular.device.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户组表
 * </p>
 *
 * @author liqirui
 * @since 2022-03-28
 */
@Data
public class SysOrgs implements Serializable {

private static final long serialVersionUID=1L;

    /**
     * id
     */
    @Excel(name = "组织机构编码", width = 25)
    private String id;

    /**
     * 用户组父id
     */
    private String parentId;

    /**
     * 是否页节点，0:false,1ture
     */
    private Integer isLeaf;

    /**
     * 组织名称
     */
    @Excel(name = "组织名称", width = 25)
    private String name;

    /**
     * 简称
     */
    private String simpleName;

    /**
     * 地址ID,关联region表
     */
    private String regionId;

    /**
     * 描述
     */
    private String description;

    /**
     * 系统标题，多余字段，用来控制供电公司的层级。1是省级，2是市级，3县级
     */
    private String orgTitle;

    /**
     * 关联厂家信息，仅仅厂家管理类型的组织机构有效
     */
    private String factId;

    /**
     * 单位类型:字典类型104
     */
    private Integer unitType;

    /**
     * 业务分类，字典类型103
     */
    private Integer serviceType;

    /**
     * 组织类型，字典类型101
     */
    private Integer type;

    /**
     * 照片
     */
    private String picUrl;

    /**
     * 联系人
     */
    private String linkName;

    /**
     * 联系电话
     */
    private String linkPhone;

    /**
     * 详情地址
     */
    private String addresDesc;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 是否已删除Y：已删除，N：未删除
     */
    private String deleted;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 管辖区域
     */
    private String manRegId;

    /**
     * 排序字段
     */
    private Integer orderNum;

    /**
     * 状态，字典102
     */
    private Integer status;

    /**
     * 税号
     */
    private String taxNo;

    /**
     * 银行开户行
     */
    private String bankNo;

    /**
     * 备用电话
     */
    private String alterTel;

    /**
     * 传真
     */
    private String fax;

    /**
     * 组织机构编码
     */
    private String orgNo;


}
