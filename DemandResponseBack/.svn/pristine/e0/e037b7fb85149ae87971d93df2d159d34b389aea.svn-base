package com.xqxy.sys.modular.cust.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xqxy.core.annotion.NeedSetValue;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import com.xqxy.dr.modular.adjustable.service.DrConsAdjustablePotentialService;
import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.data.entity.ConsEnergyCurve;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("dr_cons")
public class Cons extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 3489862119374427325L;
    /**
     * 电力户号
     */
    @Excel(name = "户号",width = 10)
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 客户主键
     */
    private Long custId;

    /**
     * 用户名称
     */
    @Excel(name = "用户名称",width = 10)
    private String consName;

    /**
     * 用电地址
     */
    @Excel(name = "用电地址",width = 10)
    private String elecAddr;

    /**
     * 行业类别名称
     */
    private String bigTradeName;

    /**
     * 行业类别编码
     */
    private String bigTradeCode;

    /**
     * 行业分类名称
     */
    @Excel(name = "行业分类名称",width = 10)
    private String tradeName;

    /**
     * 行业分类编码
     */

    private String tradeCode;

    /**
     * 日最大负荷（本月~去年同月）
     */
    private BigDecimal dayMaxPower;

    /**
     * 最大容量
     */
    @Excel(name = "合同容量",width = 10)
    private BigDecimal contractCap;

    /**
     * 运行容量
     */
    @Excel(name = "运行容量",width = 10)
    private BigDecimal runCap;

    /**
     * 电源类型（1：专线、0：专变、2：公变）
     */
    private String typeCode;

    /**
     * 供电单位名称
     */
    @Excel(name = "供电单位名称",width = 10)
    private String orgName;

    /**
     * 供电单位编码
     */
    private String orgNo;

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
     * 街道码（乡镇）
     */
    private String streetCode;

    /**
     * 变电站名称
     */
    private String subsName;

    /**
     * 变电站编码
     */
    private String subsNo;

    /**
     * 线路名称
     */
    private String lineName;

    /**
     * 线路编号
     */

    private String lineNo;

    /**
     * 台区名称
     */
    private String tgName;

    /**
     * 台区编号
     */
    private String tgNo;

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

    /**
     * 1:正常，2：撤销
     */
    private String state;

    /**
     * 电压等级
     */
    @TableField("VOLTCODE")
    private String voltCode;

    /**
     * 省 供电单位
     */
    @TableField("PROVINCE_ORG_NO")
    private String provinceOrgNo;

    /**
     * 市 供电单位
     */
    @TableField("CITY_ORG_NO")
    private String cityOrgNo;

    /**
     * 区/县 供电单位
     */
    @TableField("AREA_ORG_NO")
    private String areaOrgNo;

    /**
     * 乡/镇/街道 供电单位
     */
    @TableField("STREET_ORG_NO")
    private String streetOrgNo;

    @TableField(exist = false)
    private String custNo;

    /**
     * 客户名称
     */
    @TableField(exist = false)
    private String custName;


    /**
     * 扩展属性：统一社会信用代码
     */
    @TableField(exist = false)
    @NeedSetValue(beanClass = com.xqxy.sys.modular.cust.service.CustService.class, method = "getCustById", params = {"custId"}, targetField = "cust")
    private Cust cust;

    /**
     * 用户大类编码
     */
    @TableField(exist = false)
    private String bigClassCode;

    /**
     * 分成比例
     */
    @TableField(exist = false)
    private BigDecimal extractRatio;

    /**
     * 签约文件名称
     */
    @TableField(exist = false)
    private String fileName;

    /**
     * 签约文件id
     */
    @TableField(exist = false)
    private String fileId;

    /**
     * 签约的文件类型
     */
    @TableField(exist = false)
    private String fileType;

    /**
     * 用户曲线
     */
    @TableField(exist = false)
    private ConsCurve consCurve;

    /**
     * 用户曲线中有效点数
     */
    @TableField(exist = false)
    private Integer getPointNum;

    /**
     * 用户电能势值曲线
     */
    @TableField(exist = false)
    private ConsEnergyCurve consEnergyCurve;

    /**
     * 邀约id
     */
    @TableField(exist = false)
    private Long invitationId;

    /**
     * 反馈截止日期
     */
    @TableField(exist = false)
    private LocalDateTime deadlineTime;

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
     * 最新反馈时间
     */
    @TableField(exist = false)
    private LocalDateTime latestFeedBackTime;

    /**
     * 项目id
     */
    @TableField(exist = false)
    private String projectId;

    /**
     * 项目名称
     */
    @TableField(exist = false)
    private String projectName;

    /**
     *  参与方式
     */
    @TableField(exist = false)
    private String joinUserType;

    /**
     * 保安负荷
     */
    @Excel(name = "保安负荷",width = 10)
    @NeedSetValue(beanClass = DrConsAdjustablePotentialService.class, method = "getSafetyLoadByConsId", params = {"id"}, targetField = "safetyLoad")
    private BigDecimal safetyLoad;


    /**
     * 参与类型
     */
    @TableField(exist = false)
    private String particType;

    @TableField(exist = false)
    private BigDecimal p1;
    @TableField(exist = false)
    private BigDecimal p2;
    @TableField(exist = false)
    private BigDecimal p3;
    @TableField(exist = false)
    private BigDecimal p4;
    @TableField(exist = false)
    private BigDecimal p5;
    @TableField(exist = false)
    private BigDecimal p6;
    @TableField(exist = false)
    private BigDecimal p7;
    @TableField(exist = false)
    private BigDecimal p8;
    @TableField(exist = false)
    private BigDecimal p9;
    @TableField(exist = false)
    private BigDecimal p10;
    @TableField(exist = false)
    private BigDecimal p11;
    @TableField(exist = false)
    private BigDecimal p12;
    @TableField(exist = false)
    private BigDecimal p13;
    @TableField(exist = false)
    private BigDecimal p14;
    @TableField(exist = false)
    private BigDecimal p15;
    @TableField(exist = false)
    private BigDecimal p16;
    @TableField(exist = false)
    private BigDecimal p17;
    @TableField(exist = false)
    private BigDecimal p18;
    @TableField(exist = false)
    private BigDecimal p19;
    @TableField(exist = false)
    private BigDecimal p20;
    @TableField(exist = false)
    private BigDecimal p21;
    @TableField(exist = false)
    private BigDecimal p22;
    @TableField(exist = false)
    private BigDecimal p23;
    @TableField(exist = false)
    private BigDecimal p24;
    @TableField(exist = false)
    private BigDecimal p25;
    @TableField(exist = false)
    private BigDecimal p26;
    @TableField(exist = false)
    private BigDecimal p27;
    @TableField(exist = false)
    private BigDecimal p28;
    @TableField(exist = false)
    private BigDecimal p29;
    @TableField(exist = false)
    private BigDecimal p30;
    @TableField(exist = false)
    private BigDecimal p31;
    @TableField(exist = false)
    private BigDecimal p32;
    @TableField(exist = false)
    private BigDecimal p33;
    @TableField(exist = false)
    private BigDecimal p34;
    @TableField(exist = false)
    private BigDecimal p35;
    @TableField(exist = false)
    private BigDecimal p36;
    @TableField(exist = false)
    private BigDecimal p37;
    @TableField(exist = false)
    private BigDecimal p38;
    @TableField(exist = false)
    private BigDecimal p39;
    @TableField(exist = false)
    private BigDecimal p40;
    @TableField(exist = false)
    private BigDecimal p41;
    @TableField(exist = false)
    private BigDecimal p42;
    @TableField(exist = false)
    private BigDecimal p43;
    @TableField(exist = false)
    private BigDecimal p44;
    @TableField(exist = false)
    private BigDecimal p45;
    @TableField(exist = false)
    private BigDecimal p46;
    @TableField(exist = false)
    private BigDecimal p47;
    @TableField(exist = false)
    private BigDecimal p48;
    @TableField(exist = false)
    private BigDecimal p49;
    @TableField(exist = false)
    private BigDecimal p50;
    @TableField(exist = false)
    private BigDecimal p51;
    @TableField(exist = false)
    private BigDecimal p52;
    @TableField(exist = false)
    private BigDecimal p53;
    @TableField(exist = false)
    private BigDecimal p54;
    @TableField(exist = false)
    private BigDecimal p55;
    @TableField(exist = false)
    private BigDecimal p56;
    @TableField(exist = false)
    private BigDecimal p57;
    @TableField(exist = false)
    private BigDecimal p58;
    @TableField(exist = false)
    private BigDecimal p59;
    @TableField(exist = false)
    private BigDecimal p60;
    @TableField(exist = false)
    private BigDecimal p61;
    @TableField(exist = false)
    private BigDecimal p62;
    @TableField(exist = false)
    private BigDecimal p63;
    @TableField(exist = false)
    private BigDecimal p64;
    @TableField(exist = false)
    private BigDecimal p65;
    @TableField(exist = false)
    private BigDecimal p66;
    @TableField(exist = false)
    private BigDecimal p67;
    @TableField(exist = false)
    private BigDecimal p68;
    @TableField(exist = false)
    private BigDecimal p69;
    @TableField(exist = false)
    private BigDecimal p70;
    @TableField(exist = false)
    private BigDecimal p71;
    @TableField(exist = false)
    private BigDecimal p72;
    @TableField(exist = false)
    private BigDecimal p73;
    @TableField(exist = false)
    private BigDecimal p74;
    @TableField(exist = false)
    private BigDecimal p75;
    @TableField(exist = false)
    private BigDecimal p76;
    @TableField(exist = false)
    private BigDecimal p77;
    @TableField(exist = false)
    private BigDecimal p78;
    @TableField(exist = false)
    private BigDecimal p79;
    @TableField(exist = false)
    private BigDecimal p80;
    @TableField(exist = false)
    private BigDecimal p81;
    @TableField(exist = false)
    private BigDecimal p82;
    @TableField(exist = false)
    private BigDecimal p83;
    @TableField(exist = false)
    private BigDecimal p84;
    @TableField(exist = false)
    private BigDecimal p85;
    @TableField(exist = false)
    private BigDecimal p86;
    @TableField(exist = false)
    private BigDecimal p87;
    @TableField(exist = false)
    private BigDecimal p88;
    @TableField(exist = false)
    private BigDecimal p89;
    @TableField(exist = false)
    private BigDecimal p90;
    @TableField(exist = false)
    private BigDecimal p91;
    @TableField(exist = false)
    private BigDecimal p92;
    @TableField(exist = false)
    private BigDecimal p93;
    @TableField(exist = false)
    private BigDecimal p94;
    @TableField(exist = false)
    private BigDecimal p95;
    @TableField(exist = false)
    private BigDecimal p96;


    /**
     * 是否响应邀约 0不参与 1参与
     */
    @TableField(exist = false)
    private String isParticipate;
    @TableField(exist = false)
    private BigDecimal e1;
    @TableField(exist = false)
    private BigDecimal e2;
    @TableField(exist = false)
    private BigDecimal e3;
    @TableField(exist = false)
    private BigDecimal e4;
    @TableField(exist = false)
    private BigDecimal e5;
    @TableField(exist = false)
    private BigDecimal e6;
    @TableField(exist = false)
    private BigDecimal e7;
    @TableField(exist = false)
    private BigDecimal e8;
    @TableField(exist = false)
    private BigDecimal e9;
    @TableField(exist = false)
    private BigDecimal e10;
    @TableField(exist = false)
    private BigDecimal e11;
    @TableField(exist = false)
    private BigDecimal e12;
    @TableField(exist = false)
    private BigDecimal e13;
    @TableField(exist = false)
    private BigDecimal e14;
    @TableField(exist = false)
    private BigDecimal e15;
    @TableField(exist = false)
    private BigDecimal e16;
    @TableField(exist = false)
    private BigDecimal e17;
    @TableField(exist = false)
    private BigDecimal e18;
    @TableField(exist = false)
    private BigDecimal e19;
    @TableField(exist = false)
    private BigDecimal e20;
    @TableField(exist = false)
    private BigDecimal e21;
    @TableField(exist = false)

    private BigDecimal e22;
    @TableField(exist = false)
    private BigDecimal e23;
    @TableField(exist = false)
    private BigDecimal e24;
    @TableField(exist = false)
    private BigDecimal e25;
    @TableField(exist = false)
    private BigDecimal e26;
    @TableField(exist = false)
    private BigDecimal e27;
    @TableField(exist = false)
    private BigDecimal e28;
    @TableField(exist = false)
    private BigDecimal e29;
    @TableField(exist = false)
    private BigDecimal e30;
    @TableField(exist = false)
    private BigDecimal e31;
    @TableField(exist = false)
    private BigDecimal e32;
    @TableField(exist = false)
    private BigDecimal e33;
    @TableField(exist = false)
    private BigDecimal e34;
    @TableField(exist = false)
    private BigDecimal e35;
    @TableField(exist = false)
    private BigDecimal e36;
    @TableField(exist = false)
    private BigDecimal e37;
    @TableField(exist = false)
    private BigDecimal e38;
    @TableField(exist = false)
    private BigDecimal e39;
    @TableField(exist = false)
    private BigDecimal e40;
    @TableField(exist = false)
    private BigDecimal e41;
    @TableField(exist = false)
    private BigDecimal e42;
    @TableField(exist = false)
    private BigDecimal e43;
    @TableField(exist = false)
    private BigDecimal e44;
    @TableField(exist = false)
    private BigDecimal e45;
    @TableField(exist = false)
    private BigDecimal e46;
    @TableField(exist = false)
    private BigDecimal e47;
    @TableField(exist = false)
    private BigDecimal e48;
    @TableField(exist = false)
    private BigDecimal e49;
    @TableField(exist = false)
    private BigDecimal e50;
    @TableField(exist = false)
    private BigDecimal e51;
    @TableField(exist = false)
    private BigDecimal e52;
    @TableField(exist = false)
    private BigDecimal e53;
    @TableField(exist = false)
    private BigDecimal e54;
    @TableField(exist = false)
    private BigDecimal e55;
    @TableField(exist = false)
    private BigDecimal e56;
    @TableField(exist = false)
    private BigDecimal e57;
    @TableField(exist = false)
    private BigDecimal e58;
    @TableField(exist = false)
    private BigDecimal e59;
    @TableField(exist = false)
    private BigDecimal e60;
    @TableField(exist = false)
    private BigDecimal e61;
    @TableField(exist = false)
    private BigDecimal e62;
    @TableField(exist = false)
    private BigDecimal e63;
    @TableField(exist = false)
    private BigDecimal e64;
    @TableField(exist = false)
    private BigDecimal e65;
    @TableField(exist = false)
    private BigDecimal e66;
    @TableField(exist = false)
    private BigDecimal e67;
    @TableField(exist = false)
    private BigDecimal e68;
    @TableField(exist = false)
    private BigDecimal e69;
    @TableField(exist = false)
    private BigDecimal e70;
    @TableField(exist = false)
    private BigDecimal e71;
    @TableField(exist = false)
    private BigDecimal e72;
    @TableField(exist = false)
    private BigDecimal e73;
    @TableField(exist = false)
    private BigDecimal e74;
    @TableField(exist = false)
    private BigDecimal e75;
    @TableField(exist = false)
    private BigDecimal e76;
    @TableField(exist = false)
    private BigDecimal e77;
    @TableField(exist = false)
    private BigDecimal e78;
    @TableField(exist = false)
    private BigDecimal e79;
    @TableField(exist = false)
    private BigDecimal e80;
    @TableField(exist = false)
    private BigDecimal e81;
    @TableField(exist = false)
    private BigDecimal e82;
    @TableField(exist = false)
    private BigDecimal e83;
    @TableField(exist = false)
    private BigDecimal e84;
    @TableField(exist = false)
    private BigDecimal e85;
    @TableField(exist = false)
    private BigDecimal e86;
    @TableField(exist = false)
    private BigDecimal e87;
    @TableField(exist = false)
    private BigDecimal e88;
    @TableField(exist = false)
    private BigDecimal e89;
    @TableField(exist = false)
    private BigDecimal e90;
    @TableField(exist = false)
    private BigDecimal e91;
    @TableField(exist = false)
    private BigDecimal e92;
    @TableField(exist = false)
    private BigDecimal e93;
    @TableField(exist = false)
    private BigDecimal e94;
    @TableField(exist = false)
    private BigDecimal e95;
    @TableField(exist = false)
    private BigDecimal e96;


    @TableField(exist = false)
    private List<ConsTopologyFile> consTopologyFileList;
}
