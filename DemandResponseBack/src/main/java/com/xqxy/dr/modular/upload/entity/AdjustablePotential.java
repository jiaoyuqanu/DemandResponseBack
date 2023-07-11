package com.xqxy.dr.modular.upload.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 用户级可调节潜力信息
 * </p>
 *
 * @author liqirui
 * @since 2021-11-06
 */
public class AdjustablePotential {
    private Long id;//标识

    private String consId;//用户标识

    private String bigClassName;//用户大类

    private String bigClassCode;//用户大类编码

    private String className;//用户小类

    private String classCode;//用户小类编码

    private Integer desPrepareTime;//削峰响应准备时间

    private Integer desPrepareTimeUnit;//削峰响应准备时间单位1、小时，2、分钟，3、秒

    private Integer risPrepareTime;//填谷响应准备时间

    private Integer risPrepareTimeUnit;//填谷响应准备时间单位1、小时，2、分钟，3、秒

    private Date prodStartTime;//用户生产经营开始时间

    private Integer prodStartTimeUnit;//用户生产经营开始时间单位1、小时，2、分钟，3、秒

    private Date prodEndTime;//用户生产经营结束时间

    private Integer prodEndTimeUnit;//用户生产经营结束时间单位1、小时，2、分钟，3、秒

    private BigDecimal safetyLoad;//保安负荷

    private BigDecimal reportDesMaxPower;//申报可削峰响应最大负荷  可调节设备潜力*数量

    private Integer partakeDesNum;//年可参与削峰响应次数

    private BigDecimal reportRisMaxPower;//申报可填谷最大负荷

    private Integer partakeRisNum;//年可参与填谷响应次数

    private Float onceReponseTime;//单次响应时长

    private Integer onceReponseTimeUnit;//单次响应时长单位：1、小时；2、分钟

    private BigDecimal expectDesPrice;//期望削峰电价

    private BigDecimal expectRisPrice;//期望填谷电价

    private BigDecimal lastYearRespPower;//上年实际响应最大负荷

    private Integer lastPartDesNum;//去年参与削峰响应次数

    private BigDecimal lastDesSumPower;//去年削峰累计削减负荷

    private Integer lastPartRisNum;//去年参与填谷响应次数

    private BigDecimal lastRisSumEnergy;//去年填谷累计消纳电量

    private BigDecimal consDesPower;//户级削峰可调节潜力  该户所有的设备潜力*数量的总和

    private BigDecimal consRisPower;//户级填谷可调节潜力  该户所有的设备潜力*数量的总和

    private BigDecimal year; //普查年度

    private String PROVINCE_ELE_CODE;//省电力公司码

    private String PROVINCE_ELE_NAME;//省电力公司名称

    private String CITY_ELE_NAME;//市电力公司名称

    private String CITY_ELE_CODE;//市电力公司码

    public AdjustablePotential() {
    }

    public AdjustablePotential(Long id, String consId, String bigClassName, String bigClassCode, String className, String classCode, Integer desPrepareTime, Integer desPrepareTimeUnit, Integer risPrepareTime, Integer risPrepareTimeUnit, Date prodStartTime, Integer prodStartTimeUnit, Date prodEndTime, Integer prodEndTimeUnit, BigDecimal safetyLoad, BigDecimal reportDesMaxPower, Integer partakeDesNum, BigDecimal reportRisMaxPower, Integer partakeRisNum, Float onceReponseTime, Integer onceReponseTimeUnit, BigDecimal expectDesPrice, BigDecimal expectRisPrice, BigDecimal lastYearRespPower, Integer lastPartDesNum, BigDecimal lastDesSumPower, Integer lastPartRisNum, BigDecimal lastRisSumEnergy, BigDecimal consDesPower, BigDecimal consRisPower, BigDecimal year, String PROVINCE_ELE_CODE, String PROVINCE_ELE_NAME, String CITY_ELE_NAME, String CITY_ELE_CODE) {
        this.id = id;
        this.consId = consId;
        this.bigClassName = bigClassName;
        this.bigClassCode = bigClassCode;
        this.className = className;
        this.classCode = classCode;
        this.desPrepareTime = desPrepareTime;
        this.desPrepareTimeUnit = desPrepareTimeUnit;
        this.risPrepareTime = risPrepareTime;
        this.risPrepareTimeUnit = risPrepareTimeUnit;
        this.prodStartTime = prodStartTime;
        this.prodStartTimeUnit = prodStartTimeUnit;
        this.prodEndTime = prodEndTime;
        this.prodEndTimeUnit = prodEndTimeUnit;
        this.safetyLoad = safetyLoad;
        this.reportDesMaxPower = reportDesMaxPower;
        this.partakeDesNum = partakeDesNum;
        this.reportRisMaxPower = reportRisMaxPower;
        this.partakeRisNum = partakeRisNum;
        this.onceReponseTime = onceReponseTime;
        this.onceReponseTimeUnit = onceReponseTimeUnit;
        this.expectDesPrice = expectDesPrice;
        this.expectRisPrice = expectRisPrice;
        this.lastYearRespPower = lastYearRespPower;
        this.lastPartDesNum = lastPartDesNum;
        this.lastDesSumPower = lastDesSumPower;
        this.lastPartRisNum = lastPartRisNum;
        this.lastRisSumEnergy = lastRisSumEnergy;
        this.consDesPower = consDesPower;
        this.consRisPower = consRisPower;
        this.year = year;
        this.PROVINCE_ELE_CODE = PROVINCE_ELE_CODE;
        this.PROVINCE_ELE_NAME = PROVINCE_ELE_NAME;
        this.CITY_ELE_NAME = CITY_ELE_NAME;
        this.CITY_ELE_CODE = CITY_ELE_CODE;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConsId() {
        return consId;
    }

    public void setConsId(String consId) {
        this.consId = consId;
    }

    public String getBigClassName() {
        return bigClassName;
    }

    public void setBigClassName(String bigClassName) {
        this.bigClassName = bigClassName;
    }

    public String getBigClassCode() {
        return bigClassCode;
    }

    public void setBigClassCode(String bigClassCode) {
        this.bigClassCode = bigClassCode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public Integer getDesPrepareTime() {
        return desPrepareTime;
    }

    public void setDesPrepareTime(Integer desPrepareTime) {
        this.desPrepareTime = desPrepareTime;
    }

    public Integer getDesPrepareTimeUnit() {
        return desPrepareTimeUnit;
    }

    public void setDesPrepareTimeUnit(Integer desPrepareTimeUnit) {
        this.desPrepareTimeUnit = desPrepareTimeUnit;
    }

    public Integer getRisPrepareTime() {
        return risPrepareTime;
    }

    public void setRisPrepareTime(Integer risPrepareTime) {
        this.risPrepareTime = risPrepareTime;
    }

    public Integer getRisPrepareTimeUnit() {
        return risPrepareTimeUnit;
    }

    public void setRisPrepareTimeUnit(Integer risPrepareTimeUnit) {
        this.risPrepareTimeUnit = risPrepareTimeUnit;
    }

    public Date getProdStartTime() {
        return prodStartTime;
    }

    public void setProdStartTime(Date prodStartTime) {
        this.prodStartTime = prodStartTime;
    }

    public Integer getProdStartTimeUnit() {
        return prodStartTimeUnit;
    }

    public void setProdStartTimeUnit(Integer prodStartTimeUnit) {
        this.prodStartTimeUnit = prodStartTimeUnit;
    }

    public Date getProdEndTime() {
        return prodEndTime;
    }

    public void setProdEndTime(Date prodEndTime) {
        this.prodEndTime = prodEndTime;
    }

    public Integer getProdEndTimeUnit() {
        return prodEndTimeUnit;
    }

    public void setProdEndTimeUnit(Integer prodEndTimeUnit) {
        this.prodEndTimeUnit = prodEndTimeUnit;
    }

    public BigDecimal getSafetyLoad() {
        return safetyLoad;
    }

    public void setSafetyLoad(BigDecimal safetyLoad) {
        this.safetyLoad = safetyLoad;
    }

    public BigDecimal getReportDesMaxPower() {
        return reportDesMaxPower;
    }

    public void setReportDesMaxPower(BigDecimal reportDesMaxPower) {
        this.reportDesMaxPower = reportDesMaxPower;
    }

    public Integer getPartakeDesNum() {
        return partakeDesNum;
    }

    public void setPartakeDesNum(Integer partakeDesNum) {
        this.partakeDesNum = partakeDesNum;
    }

    public BigDecimal getReportRisMaxPower() {
        return reportRisMaxPower;
    }

    public void setReportRisMaxPower(BigDecimal reportRisMaxPower) {
        this.reportRisMaxPower = reportRisMaxPower;
    }

    public Integer getPartakeRisNum() {
        return partakeRisNum;
    }

    public void setPartakeRisNum(Integer partakeRisNum) {
        this.partakeRisNum = partakeRisNum;
    }

    public Float getOnceReponseTime() {
        return onceReponseTime;
    }

    public void setOnceReponseTime(Float onceReponseTime) {
        this.onceReponseTime = onceReponseTime;
    }

    public Integer getOnceReponseTimeUnit() {
        return onceReponseTimeUnit;
    }

    public void setOnceReponseTimeUnit(Integer onceReponseTimeUnit) {
        this.onceReponseTimeUnit = onceReponseTimeUnit;
    }

    public BigDecimal getExpectDesPrice() {
        return expectDesPrice;
    }

    public void setExpectDesPrice(BigDecimal expectDesPrice) {
        this.expectDesPrice = expectDesPrice;
    }

    public BigDecimal getExpectRisPrice() {
        return expectRisPrice;
    }

    public void setExpectRisPrice(BigDecimal expectRisPrice) {
        this.expectRisPrice = expectRisPrice;
    }

    public BigDecimal getLastYearRespPower() {
        return lastYearRespPower;
    }

    public void setLastYearRespPower(BigDecimal lastYearRespPower) {
        this.lastYearRespPower = lastYearRespPower;
    }

    public Integer getLastPartDesNum() {
        return lastPartDesNum;
    }

    public void setLastPartDesNum(Integer lastPartDesNum) {
        this.lastPartDesNum = lastPartDesNum;
    }

    public BigDecimal getLastDesSumPower() {
        return lastDesSumPower;
    }

    public void setLastDesSumPower(BigDecimal lastDesSumPower) {
        this.lastDesSumPower = lastDesSumPower;
    }

    public Integer getLastPartRisNum() {
        return lastPartRisNum;
    }

    public void setLastPartRisNum(Integer lastPartRisNum) {
        this.lastPartRisNum = lastPartRisNum;
    }

    public BigDecimal getLastRisSumEnergy() {
        return lastRisSumEnergy;
    }

    public void setLastRisSumEnergy(BigDecimal lastRisSumEnergy) {
        this.lastRisSumEnergy = lastRisSumEnergy;
    }

    public BigDecimal getConsDesPower() {
        return consDesPower;
    }

    public void setConsDesPower(BigDecimal consDesPower) {
        this.consDesPower = consDesPower;
    }

    public BigDecimal getConsRisPower() {
        return consRisPower;
    }

    public void setConsRisPower(BigDecimal consRisPower) {
        this.consRisPower = consRisPower;
    }

    public BigDecimal getYear() {
        return year;
    }

    public void setYear(BigDecimal year) {
        this.year = year;
    }

    public String getPROVINCE_ELE_CODE() {
        return PROVINCE_ELE_CODE;
    }

    public void setPROVINCE_ELE_CODE(String PROVINCE_ELE_CODE) {
        this.PROVINCE_ELE_CODE = PROVINCE_ELE_CODE;
    }

    public String getPROVINCE_ELE_NAME() {
        return PROVINCE_ELE_NAME;
    }

    public void setPROVINCE_ELE_NAME(String PROVINCE_ELE_NAME) {
        this.PROVINCE_ELE_NAME = PROVINCE_ELE_NAME;
    }

    public String getCITY_ELE_NAME() {
        return CITY_ELE_NAME;
    }

    public void setCITY_ELE_NAME(String CITY_ELE_NAME) {
        this.CITY_ELE_NAME = CITY_ELE_NAME;
    }

    public String getCITY_ELE_CODE() {
        return CITY_ELE_CODE;
    }

    public void setCITY_ELE_CODE(String CITY_ELE_CODE) {
        this.CITY_ELE_CODE = CITY_ELE_CODE;
    }
}
