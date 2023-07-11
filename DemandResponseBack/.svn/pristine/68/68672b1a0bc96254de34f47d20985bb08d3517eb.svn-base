package com.xqxy.dr.modular.adjustable.VO;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrConsAdjustablePotentialVO implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @Excel(name = "主键",width = 10)
    @ApiModelProperty(value = "主键")
    private String id;

    /**
     * 用户标识
     */
    @Excel(name = "用户标识",width = 10)
    @ApiModelProperty(value = "用户标识")
    private String consId;

    /**
     * 用户名称
     */
    @Excel(name = "用户名称",width = 10)
    @ApiModelProperty(value = "用户名称")
    private String consName;

    /**
     * 合同容量
     */
    @Excel(name = "合同容量",width = 10)
    @ApiModelProperty(value = "合同容量")
    private BigDecimal contractCap;

    /**
     * 用户大类
     */
    @Excel(name = "用户大类",width = 10)
    @ApiModelProperty(value = "用户大类")
    private String bigClassName;

    /**
     * 用户大类编码
     */
    @Excel(name = "用户大类编码",width = 10)
    @ApiModelProperty(value = "用户大类编码")
    private String bigClassCode;

    /**
     * 用户小类
     */
    @Excel(name = "用户小类",width = 10)
    @ApiModelProperty(value = "用户小类")
    private String className;

    /**
     * 用户小类编码
     */
    @Excel(name = "用户小类编码",width = 10)
    @ApiModelProperty(value = "用户小类编码")
    private String classCode;

    /**
     * 削峰响应准备时间
     */
    @Excel(name = "削峰响应准备时间",width = 10)
    @ApiModelProperty(value = "削峰响应准备时间")
    private Integer desPrepareTime;

    /**
     * 削峰响应准备时间单位1、小时，2、分钟，3、秒
     */
    @ApiModelProperty(value = "削峰响应准备时间单位1、小时，2、分钟，3、秒")
    private Integer desPrepareTimeUnit;

    /**
     * 填谷响应准备时间
     */
    @Excel(name = "填谷响应准备时间",width = 10)
    @ApiModelProperty(value = "填谷响应准备时间")
    private Integer risPrepareTime;

    /**
     * 填谷响应准备时间单位1、小时，2、分钟，3、秒
     */
    @ApiModelProperty(value = "填谷响应准备时间单位1、小时，2、分钟，3、秒")
    private Integer risPrepareTimeUnit;

    /**
     * 用户生产经营开始时间
     */
    @Excel(name = "用户生产经营开始时间",width = 10)
    @ApiModelProperty(value = "用户生产经营开始时间")
    private String prodStartTime;

    /**
     * 用户生产经营开始时间单位1、小时，2、分钟，3、秒
     */
    @Excel(name = "用户生产经营开始时间单位1、小时，2、分钟，3、秒",width = 10)
    @ApiModelProperty(value = "用户生产经营开始时间单位1、小时，2、分钟，3、秒")
    private Integer prodStartTimeUnit;

    /**
     * 用户生产经营结束时间
     */
    @Excel(name = "用户生产经营结束时间",width = 10)
    @ApiModelProperty(value = "用户生产经营结束时间")
    private String prodEndTime;

    /**
     * 用户生产经营结束时间单位1、小时，2、分钟，3、秒
     */
    @ApiModelProperty(value = "用户生产经营结束时间单位1、小时，2、分钟，3、秒")
    private Integer prodEndTimeUnit;

    /**
     * 保安负荷
     */
    @Excel(name = "保安负荷",width = 10)
    @ApiModelProperty(value = "保安负荷")
    private BigDecimal safetyLoad;

    /**
     * 日前约时申报可削峰响应最大负荷  可调节设备潜力*数量
     */
    @Excel(name = "日前约时申报可削峰响应最大负荷",width = 10)
    @ApiModelProperty(value = "日前约时申报可削峰响应最大负荷")
    private BigDecimal reportDesMaxPower;

    /**
     * 年可参与削峰响应次数
     */
    @Excel(name = "年可参与削峰响应次数",width = 10)
    @ApiModelProperty(value = "年可参与削峰响应次数")
    private Integer partakeDesNum;

    /**
     * 约时填谷可响应负荷
     */
    @Excel(name = "约时填谷可响应负荷",width = 10)
    @ApiModelProperty(value = "约时填谷可响应负荷")
    private BigDecimal reportRisMaxPower;

    /**
     * 年可参与填谷响应次数
     */
    @Excel(name = "年可参与填谷响应次数",width = 10)
    @ApiModelProperty(value = "年可参与填谷响应次数")
    private Integer partakeRisNum;

    /**
     * 约时削峰最小响应时长
     */
    @Excel(name = "约时削峰最小响应时长",width = 10)
    @ApiModelProperty(value = "约时削峰最小响应时长")
    private Float onceReponseTime;

    /**
     * 单次响应时长单位：1、小时；2、分钟
     */
    @TableField("ONCE_REPONSE_TIME_UNIT")
    @ApiModelProperty(value = "单次响应时长单位：1、小时；2、分钟")
    private Integer onceReponseTimeUnit;

    /**
     * 期望削峰电价
     */
    @Excel(name = "期望削峰电价",width = 10)
    @ApiModelProperty(value = "期望削峰电价")
    private BigDecimal expectDesPrice;

    /**
     * 期望填谷电价
     */
    @Excel(name = "期望填谷电价",width = 10)
    @ApiModelProperty(value = "期望填谷电价")
    private BigDecimal expectRisPrice;

    /**
     * 上年实际响应最大负荷
     */
    @Excel(name = "上年实际响应最大负荷",width = 10)
    @ApiModelProperty(value = "上年实际响应最大负荷")
    private BigDecimal lastYearRespPower;

    /**
     * 去年参与削峰响应次数
     */
    @Excel(name = "去年参与削峰响应次数",width = 10)
    @ApiModelProperty(value = "去年参与削峰响应次数")
    private Integer lastPartDesNum;

    /**
     * 去年削峰累计削减负荷
     */
    @Excel(name = "去年削峰累计削减负荷",width = 10)
    @ApiModelProperty(value = "去年削峰累计削减负荷")
    private BigDecimal lastDesSumPower;

    /**
     * 去年参与填谷响应次数
     */
    @Excel(name = "去年参与填谷响应次数",width = 10)
    @ApiModelProperty(value = "去年参与填谷响应次数")
    private Integer lastPartRisNum;

    /**
     * 去年填谷累计消纳电量
     */
    @Excel(name = "去年填谷累计消纳电量",width = 10)
    @ApiModelProperty(value = "去年填谷累计消纳电量")
    private BigDecimal lastRisSumEnergy;

    /**
     * 户级削峰可调节潜力  该户所有的设备潜力*数量的总和
     */
    @Excel(name = "户级削峰可调节潜力",width = 10)
    @ApiModelProperty(value = "户级削峰可调节潜力")
    private BigDecimal consDesPower;

    /**
     * 户级填谷可调节潜力  该户所有的设备潜力*数量的总和
     */
    @Excel(name = "户级填谷可调节潜力",width = 10)
    @ApiModelProperty(value = "户级填谷可调节潜力")
    private BigDecimal consRisPower;


    /**
     * 普查年度
     */
    @Excel(name = "普查年度",width = 10)
    @ApiModelProperty(value = "普查年度")
    private String year;

    /**
     * 小时级削峰申报可削峰响应最大负荷
     */
    @Excel(name = "小时级削峰申报可削峰响应最大负荷",width = 10)
    @ApiModelProperty(value = "小时级削峰申报可削峰响应最大负荷")
    private BigDecimal hourReportDesMaxPower;

    /**
     * 实时削峰分钟级申报可削峰响应最大负荷
     */
    @Excel(name = "实时削峰分钟级申报可削峰响应最大负荷",width = 10)
    @ApiModelProperty(value = "实时削峰分钟级申报可削峰响应最大负荷")
    private BigDecimal minReportDesMaxPower;

    /**
     * 实时削峰秒级申报可削峰响应最大负荷
     */
    @Excel(name = "实时削峰秒级申报可削峰响应最大负荷",width = 10)
    @ApiModelProperty(value = "实时削峰秒级申报可削峰响应最大负荷")
    private BigDecimal secReportDesMaxPower;

    /**
     * ,实时削峰最小响应时长
     */
    @Excel(name = "实时削峰最小响应时长",width = 10)
    @ApiModelProperty(value = "实时削峰最小响应时长")
    private Integer realOnceReponseTime;

    /**
     * 约时填谷最小响应时长
     */
    @Excel(name = "约时填谷最小响应时长",width = 10)
    @ApiModelProperty(value = "约时填谷最小响应时长")
    private Integer risReponseTime;

    /**
     * 省电力公司码
     */
    @Excel(name = "省电力公司码",width = 10)
    @ApiModelProperty(value = "省电力公司码")
    private String provinceEleCode;

    /**
     * 省电力公司名称
     */
    @Excel(name = "省电力公司名称",width = 10)
    @ApiModelProperty(value = "省电力公司名称")
    private String provinceEleName;

    /**
     * 市电力公司名称
     */
    @Excel(name = "市电力公司名称",width = 10)
    @ApiModelProperty(value = "市电力公司名称")
    private String cityEleName;

    /**
     * 市电力公司码
     */
    @Excel(name = "市电力公司码",width = 10)
    @ApiModelProperty(value = "市电力公司码")
    private String cityEleCode;
}
