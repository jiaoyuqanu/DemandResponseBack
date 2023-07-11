package com.xqxy.dr.modular.data.entity;

import lombok.Data;

@Data
public class PointGotten {

    /**
     * 直接参与用户数
     */
    private Integer derectConsNum;

    /**
     * 某时刻直接参与用户曲线采集到的点数
     */
    private Integer directPointGotten;

    /**
     * 代理用户数
     */
    private Integer proxyConsNum;

    /**
     * 某时刻直接代理用户曲线采集到的点数
     */
    private Integer proxyPointGotten;

    /**
     * 某一点时间
     */
    private String pointName;
}
