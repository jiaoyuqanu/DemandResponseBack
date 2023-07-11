package com.xqxy.sys.modular.cust.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 系统用户表
 *
 * @author xuyuxiang
 * @date 2021/10/8 10:25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("dr_cust")
public class Cust extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 7714761189300705959L;
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 注册手机号
     */
    @Excel(name = "联系方式",width = 13)
    private String tel;

    /**
     * 统一社会信用代码证就是三证合一。 三证合一,就是把营业执照、税务登记证和组织机构代码证这三个证件合三为一
     */
    @Excel(name = "统一社会信用代码",width = 20)
    private String creditCode;

    /**
     * 是否集成商  字典： 1是，0 否
     */
    private Integer integrator;

    /**
     * 法定代表人姓名
     */
    @Excel(name = "法定代表人姓名",width = 15)
    private String legalName;

    /**
     * 法定代表人号码
     */
    @Excel(name = "法定代表人证件号码",width = 20)
    private String legalNo;

    /**
     * 1身份证，2护照
     */
    @Excel(name = "法定代表人证件类型",width = 20)
    private String legalCardType;

    /**
     * 经办人证件号码
     */
    @Excel(name = "经办人证件号码",width = 20)
    private String applyNo;

    /**
     * 1身份证，2护照
     */
    @Excel(name = "经办人证件类型",width = 20)
    private String applyCardType;

    /**
     * 经办人
     */
    @Excel(name = "经办人姓名",width = 15)
    private String applyName;

    /**
     * 客户的编号（保留）
     */
    private String custNo;

    /**
     * 客户的名称（保留）
     */
    private String custName;

    /**
     * 省码（保留）
     */
    @Excel(name = "省",width = 10)
    private String provinceCode;

    /**
     * 市码（保留）
     */
    @Excel(name = "市",width = 10)
    private String cityCode;

    /**
     * 区县码（保留）
     */
    @Excel(name = "区/县",width = 10)
    private String countyCode;

    /**
     * 街道码（乡镇）（保留）
     */
    private String streetCode;

    /**
     * 客户地址（保留）
     */
    private String custAddr;

    /**
     * 1:未提交，2：审核中，3：审核通过，4：审核不通过
     */
    @Excel(name = "审核状态",width = 10)
    private String checkStatus;

    /**
     * 1:未认证，2:已认证，3：撤销，4：认证失败
     */
    private String state;

    /**
     * 身份信息是否允许修改  N 不允许
     */
    private String allowChange;

    /**
     * 归属供电单位编码
     */
    private String orgNo;

    /**
     * 邀约id
     */
    @TableField(exist = false)
    private Long invitationId;

    /**
     * 调控日期
     */
    @TableField(exist = false)
    private LocalDate regulateDate;

    /**
     * 响应开始时间
     */
    @TableField(exist = false)
    private String startTime;

    /**
     * 响应结束时间
     */
    @TableField(exist = false)
    private String endTime;

    /**
     * 是否响应邀约 0不参与 1参与
     */
    @TableField(exist = false)
    private String isParticipate;



}
