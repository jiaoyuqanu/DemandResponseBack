package com.xqxy.dr.modular.project.params;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ImportContractExcel {


    @Excel(name = "项目编号", width = 20)
    private String projectNo;
    @Excel(name = "项目名称", width = 20)
    private String projectName;
    @Excel(name = "电力户号", width = 20)
    private String consCode;
    @Excel(name = "用户名称", width = 25)
    private String consName;
    @Excel(name = "供电单位", width = 30)
    private String orgName;
    @Excel(name = "合同容量(KVA)", width = 15)
    private BigDecimal consContractCap;
    @Excel(name = "运行容量(KVA)", width = 15)
    private BigDecimal runCap;
    @Excel(name = "联系人姓名", width = 15)
    private String firstContactName;
    @Excel(name = "联系人电话", width = 15)
    private String firstContactInfo;
    @Excel(name = "响应类型代码", width = 15, isColumnHidden = true)
    private String responseType;
    @Excel(name = "事件类型代码", width = 15, isColumnHidden = true)
    private String timeType;
    @Excel(name = "提前通知代码", width = 15, isColumnHidden = true)
    private Integer advanceNoticeTime;
    @Excel(name = "响应类型", width = 15)
    private String responseTypeName;
    @Excel(name = "时间类型", width = 15)
    private String timeTypeName;
    @Excel(name = "提前通知时间", width = 15)
    private String advanceNoticeTimeName;
    @Excel(name = "新型负荷管理可控负荷值(kW)", width = 15, isColumnHidden = true)
    private BigDecimal controlCap;
    @Excel(name = "响应负荷(kW)", width = 15)
    private BigDecimal contractCap;

    @Excel(name = "最小响应时长(分)", width = 15)
    private Integer minTimes;

//    @Excel(name = "备用容量最小响应时长(分)", width = 15)
//    private Integer spareMinTimes;
//
//    @Excel(name = "备用容量(kW) (备用容量不能大于设备容量)", width = 15)
//    private BigDecimal spareCap;
//
//    @Excel(name = "空调容量(kW) (空调容量不能大于设备容量)", width = 15)
//    private BigDecimal airconditionCap;

    @Excel(name = "projectDetailId", isColumnHidden = true)
    private Long projectDetailId;
    @Excel(name = "项目id", width = 20, isColumnHidden = true)
    private String projectId;
}
