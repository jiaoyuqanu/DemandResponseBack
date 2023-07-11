package com.xqxy.dr.modular.pcdata.param;

import lombok.Data;

import java.util.List;

/**
 * Author: zgy
 * Date: 2021/11/26 16:45
 * Content:
 */
@Data
public class PcDevice {
    /**
     * 设备id
     */
    private String GUID;

    /**
     * 用户id（用户表guid）
     */
    private String USERID;

    /**
     * 设备名称
     */
    private String SBMC;

    /**
     * 设备类型名称
     */
    private String SBLXMC;

    /**
     * 设备类型编码
     */
    private String SBLX;

    /**
     * 设备用途
     */
    private String SBYT;

    /**
     * 设备用途编码
     */
    private String SBYTBM;

    /**
     * 日平均利用小时数
     */
    private String RPJLYXSS;

    /**
     * 详细用途
     */
    private String XXYT;

    /**
     * 详细用途编码
     */
    private String XXYTBM;

    /**
     * 设备数量
     */
    private String SBSL;

    /**
     * 响应设备容量
     */
    private String XYSBRL;

    /**
     * 是否可监测
     */
    private String SFKJC;

    /**
     * 额定电压
     */
    private String EDDY;

    /**
     * 额定功率
     */
    private String EDGL;

    /**
     * 普查日期
     */
    private String PCRQ;

    /**
     * 削峰响应可持续时间（单位:时/分/秒）
     */
    private String XFXYKCXSJ;

    /**
     * 填谷响应可持续时间（单位:时/分/秒）
     */
    private String TGXYKCXSJ;

    /**
     * 设备爬坡时间（单位:时/分/秒）
     */
    private String SBPPSJ;

    /**
     * 扩展信息(典型设备字段)（json格式）
     */
    private String KZXX;

    /**
     * 设备图片信息
     */
//    private List<PcDevicePic> pictures;

    private PcDeviceAblity pcDeviceAblity;
}
