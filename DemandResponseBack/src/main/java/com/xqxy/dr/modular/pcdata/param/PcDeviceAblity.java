package com.xqxy.dr.modular.pcdata.param;

import lombok.Data;

/**
 * Author: zgy
 * Date: 2021/11/26 16:59
 * Content: 设备普查潜力信息
 */
@Data
public class PcDeviceAblity {
    /**
     * 响应guid
     */
    private String GUID;

    /**
     * 设备guid（设备表guid）
     */
    private String DEVICEGUID;

    /**
     * 设备工况名称
     */
    private String SBGKMC;

    /**
     * 设备工况编码
     */
    private String SBGKBM;

    /**
     * 响应参与方式
     */
    private String XYCYFSMC;

    /**
     * 响应参与方式编码
     */
    private String XYCYFS;

    /**
     * 响应负荷
     */
    private String XYFH;

    /**
     * 响应类型（削峰/填谷）
     */
    private String XYLX;

    /**
     * 时间类型（约时/实时）
     */
    private String SJLX;

    /**
     * 响应开始时间
     */
    private String STARTTIME;

    /**
     * 响应结束时间
     */
    private String ENDTIME;

    /**
     * 更新时间
     */
    private String UPDATE_TIME;
}
