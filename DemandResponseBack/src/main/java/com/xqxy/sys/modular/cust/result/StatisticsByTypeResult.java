package com.xqxy.sys.modular.cust.result;
import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class StatisticsByTypeResult {
    /**
     * 地区码
     */
    private String regionCode;

    /**
     * 用户签约数量
     */
    private Integer contractCount;

    /**
     * 用户签约比率
     */
    private BigDecimal contractRatio;

    /**
     * 地区名称
     */
    @Excel(name = "地区名称",width = 25)
    private String regionName;

    /**
     * 其他用户数量
     */
    @Excel(name = "总数 (单位：户)",width = 25)
    private Integer totalCount;

    /**
     * 工业用户数量
     */
    @Excel(name = "工业用户数量 (单位：户)",width = 25)
    private Integer industrialCount;

    /**
     * 居民用户数量
     */
    @Excel(name = "居民用户数量 (单位：户)",width = 25)
    private Integer residentCount;

    /**
     * 楼宇用户数量
     */
    @Excel(name = "楼宇用户数量 (单位：户)",width = 25)
    private Integer buildingCount;

    /**
     * 新兴负荷用户数量
     */
    @Excel(name = "新兴负荷用户数量 (单位：户)",width = 25)
    private Integer emergingLoadUser;

    /**
     * 农林牧渔用户用户数量
     */
    @Excel(name = "农林牧渔用户用户数量 (单位：户)",width = 25)
    private Integer agriculCount;

    /**
     * 其他用户数量
     */
    @Excel(name = "其他用户数量 (单位：户)",width = 25)
    private Integer otherCount;


}
