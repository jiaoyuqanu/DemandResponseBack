package com.xqxy.sys.modular.cust.entity.export;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p></p>
 *
 * @author zhengzheng Create on 2022/8/6
 * @version 1.0
 */
@Data
public class ExportCons extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 3489862119374427325L;
    /**
     * 电力户号
     */
    @Excel(name = "户号",width = 10)
    private String id;

    /**
     * 用户名称
     */
    @Excel(name = "用户名称",width = 10)
    private String consName;

    /**
     * 省码
     */
    @Excel(name = "省",width = 10)
    private String provinceCode;

    /**
     * 市码
     */
    @Excel(name = "市",width = 10)
    private String cityCode;

    /**
     * 区县码
     */
    @Excel(name = "区",width = 10)
    private String countyCode;

    /**
     * 省供电公司
     */
    @Excel(name = "省供电公司",width = 20)
    private String provinceOrgNo;

    /**
     * 市供电公司
     */
    @Excel(name = "市供电公司",width = 20)
    private String cityOrgNo;

    /**
     * 区供电公司
     */
    @Excel(name = "区供电公司",width = 20)
    private String areaOrgNo;

    /**
     * 用电地址
     */
    @Excel(name = "用电地址",width = 10)
    private String elecAddr;

    /**
     * 电压等级
     */
    @Excel(name = "负荷等级",width = 20)
    private String voltCode;

    /**
     * 行业分类名称(由bigTradeCode 行业类别编码 翻译)
     */
    @Excel(name = "行业分类名称",width = 10)
    private String tradeName;

    /**
     * 最大容量
     */
    @Excel(name = "合同容量(KVA)",width = 10)
    private BigDecimal contractCap;

    /**
     * 运行容量
     */
    @Excel(name = "运行容量(KVA)",width = 10)
    private BigDecimal runCap;

    /**
     * 电源类型（1：专线、0：专变、2：公变）
     */
    @Excel(name = "电源类型",width = 10)
    private String typeCode;

    /**
     * 供电单位名称
     */
    @Excel(name = "供电单位名称",width = 10)
    private String orgName;

    /**
     * 第一联系人姓名
     */
    @Excel(name = "第一联系人姓名",width = 10)
    private String firstContactName;

    /**
     * 第一联系人联系方式
     */
    @Excel(name = "第一联系人联系方式",width = 10)
    private String firstContactInfo;

    /**
     * 第二联系人姓名
     */
    @Excel(name = "第二联系人姓名",width = 10)
    private String secondContactName;

    /**
     * 第二联系人联系方式
     */
    @Excel(name = "第二联系人联系方式",width = 10)
    private String secondContactInifo;



}
