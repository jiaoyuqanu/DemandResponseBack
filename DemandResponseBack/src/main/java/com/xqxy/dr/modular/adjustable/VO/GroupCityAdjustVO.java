package com.xqxy.dr.modular.adjustable.VO;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

@Data
public class GroupCityAdjustVO implements Serializable {

    private static final long serialVersionUID=1L;


    /**
     * 主键
     */
    private String id;

    /**
     * 市码
     */
    private String cityCode;

    /**
     * 市名称
     */
    private String cityName;

    /**
     * 省码
     */
    private String provinceCode;

    /**
     * 省名称
     */
    private String provinceName;


    /**
     * 普查年度
     */
    private String year;


    /**
     * 上年实际响应最大负荷
     */
    private String lastYearRespPower;


    /**
     * 可调节资源库容量
     */
    private String capacitySum;


    /**
     * 可调节资源总户数
     */
    private String countHouse;

}
