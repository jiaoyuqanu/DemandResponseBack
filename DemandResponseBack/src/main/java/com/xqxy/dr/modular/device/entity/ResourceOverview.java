package com.xqxy.dr.modular.device.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ResourceOverview {
    //用户总数
    private Integer consNum;
    //设备总数
    private Integer deviceNum;
    //接入用户容量
    private BigDecimal accessCapacity;
}
