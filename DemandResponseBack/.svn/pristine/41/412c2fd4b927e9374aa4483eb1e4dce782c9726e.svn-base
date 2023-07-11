package com.xqxy.dr.modular.newloadmanagement.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.xqxy.dr.modular.upload.entity.AdjustablePotential;
import com.xqxy.dr.modular.upload.entity.ContractInfo;
import com.xqxy.dr.modular.upload.entity.PlanCons;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("dr_cons") //表名
public class Drcons {
    private String id;//电力户号
    private Long custId;//本实体记录的唯一标识，产生规则为流水号
    private String consName;//用户名称
    private String electAddr;//用电地址
    private String bigTradeName;//行业类别名称
    private String bidTradeCode;//行业类别编码
    private String tradeName;//行业分类名称
    private String tradeCode;//行业分类编码
    private BigDecimal dayMaxPower;//日最大负荷（本月~去年同月
    private BigDecimal contractCap;//合同容量
    private BigDecimal runCap;//运行容量
    private String typeCode;//电源类型（专线、专变、公变）
    private String orgName;//供电单位名称
    private String orgNo;//供电单位编码
    private String provinceCode;//省码
    private String cityCode;//市码
    private String countyCode;//区县码
    private String streetCode;//街道码（乡镇）
    private String subsName;//变电站名称
    private String subsNo;//变电站编码
    private String lineName;//线路名称
    private String lineNo;//线路编号
    private String tgName;//台区名称
    private String tgNo;//台区编号
    private String firstContactName;//第一联系人姓名
    private String firstContactInfo;//第一联系人联系方式第一联系人联系方式
    private String secondContactName;//第二联系人姓名
    private String secondContactInifo;//第二联系人联系方式
    private String state;//1:正常，2：撤销
    private String createTime;//创建时间
    private Long createUser;//创建人
    private String updateTime;//更新时间
    private Long updateUser;//更新人
    private String voltCode;//电压等级

    private DrUserConsRela drUserConsRela;

    private Drcust drcust;
    private String custName;//客户的名称（保留）
    private String  custAddr;//客户地址（保留）
    private String creditCode;//统一社会信用代码证就是三证合一。 三证合一,就是把营业执照、税务登记证和组织机构代码证这三个证件合三为一
    private String legalName;//法定代表人姓名
    private String legalNo;//法定代表人号码
}
