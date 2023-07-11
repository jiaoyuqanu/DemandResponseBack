package com.xqxy.dr.modular.statistics.mapper;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.dispatch.entity.DispatchEvent;
import com.xqxy.dr.modular.dispatch.param.DispatchParam;
import com.xqxy.dr.modular.statistics.param.AdjustParam;
import com.xqxy.dr.modular.statistics.result.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface StatisticsMapper {


    /**
     * 调节速度统计
     * @param page
     * @return
     */
    @Select("" +
            "SELECT\n" +
            "\ta.*,\n" +
            "\tIFNULL(b.CONS_DES_POWER2,0) AS CONS_DES_POWER2,\n" +
            "\tIFNULL(b.CONS_RIS_POWER2,0) AS CONS_RIS_POWER2,\n" +
            "\tIFNULL(c.CONS_DES_POWER3,0) AS CONS_DES_POWER3,\n" +
            "\tIFNULL(c.CONS_RIS_POWER3,0) AS CONS_RIS_POWER3,\n" +
            "\tIFNULL(d.CONS_DES_POWER4,0) AS CONS_DES_POWER4,\n" +
            "\tIFNULL(d.CONS_RIS_POWER4,0) AS CONS_RIS_POWER4\n" +
            "FROM\n" +
            "\t(\n" +
            "\tSELECT\n" +
            "\t\tdc.ID AS id,\n" +
            "\t\tdc.CONS_NAME AS name,\n" +
            "\tCASE\n" +
            "\t\t\tdab.DEVICE_RISE_TIME_CODE \n" +
            "\t\t\tWHEN '0508901' THEN\n" +
            "\t\t\tIFNULL( sum( dcap.CONS_DES_POWER ), 0 ) ELSE 0 \n" +
            "\t\tEND AS CONS_DES_POWER1,\n" +
            "\tCASE\n" +
            "\t\t\tdab.DEVICE_RISE_TIME_CODE \n" +
            "\t\t\tWHEN '0508901' THEN\n" +
            "\t\t\tIFNULL( sum( dcap.CONS_RIS_POWER ), 0 ) ELSE 0 \n" +
            "\t\tEND AS CONS_RIS_POWER1 \n" +
            "\tFROM\n" +
            "\t\tdr_cons dc\n" +
            "\t\tLEFT JOIN dr_device_adjustable_base dab ON dc.ID = dab.CONS_ID\n" +
            "\t\tLEFT JOIN dr_cons_adjustable_potential dcap ON dc.ID = dcap.CONS_ID \n" +
            "\tGROUP BY\n" +
            "\t\tdc.ID\n" +
            "\t) a\n" +
            "\tLEFT JOIN (\n" +
            "\tSELECT\n" +
            "\t\tdc.ID AS id,\n" +
            "\t\tdc.CONS_NAME,\n" +
            "\tCASE\n" +
            "\t\t\tdab.DEVICE_RISE_TIME_CODE \n" +
            "\t\t\tWHEN '0508902' THEN\n" +
            "\t\t\tIFNULL( sum( dcap.CONS_DES_POWER ), 0 ) ELSE 0 \n" +
            "\t\tEND AS CONS_DES_POWER2,\n" +
            "\tCASE\n" +
            "\t\t\tdab.DEVICE_RISE_TIME_CODE \n" +
            "\t\t\tWHEN '0508902' THEN\n" +
            "\t\t\tIFNULL( sum( dcap.CONS_RIS_POWER ), 0 ) ELSE 0 \n" +
            "\t\tEND AS CONS_RIS_POWER2 \n" +
            "\tFROM\n" +
            "\t\tdr_cons dc\n" +
            "\t\tLEFT JOIN dr_device_adjustable_base dab ON dc.ID = dab.CONS_ID\n" +
            "\t\tLEFT JOIN dr_cons_adjustable_potential dcap ON dc.ID = dcap.CONS_ID \n" +
            "\tGROUP BY\n" +
            "\t\tdc.ID\n" +
            "\t) b ON a.id = b.id\n" +
            "\tLEFT JOIN (\n" +
            "\tSELECT\n" +
            "\t\tdc.ID AS id,\n" +
            "\t\tdc.CONS_NAME,\n" +
            "\tCASE\n" +
            "\t\t\tdab.DEVICE_RISE_TIME_CODE \n" +
            "\t\t\tWHEN '0508903' THEN\n" +
            "\t\t\tIFNULL( sum( dcap.CONS_DES_POWER ), 0 ) ELSE 0 \n" +
            "\t\tEND AS CONS_DES_POWER3,\n" +
            "\tCASE\n" +
            "\t\t\tdab.DEVICE_RISE_TIME_CODE \n" +
            "\t\t\tWHEN '0508903' THEN\n" +
            "\t\t\tIFNULL( sum( dcap.CONS_RIS_POWER ), 0 ) ELSE 0 \n" +
            "\t\tEND AS CONS_RIS_POWER3 \n" +
            "\tFROM\n" +
            "\t\tdr_cons dc\n" +
            "\t\tLEFT JOIN dr_device_adjustable_base dab ON dc.ID = dab.CONS_ID\n" +
            "\t\tLEFT JOIN dr_cons_adjustable_potential dcap ON dc.ID = dcap.CONS_ID \n" +
            "\tGROUP BY\n" +
            "\t\tdc.ID\n" +
            "\t) c ON a.id = c.id\n" +
            "\tLEFT JOIN (\n" +
            "\tSELECT\n" +
            "\t\tdc.ID AS id,\n" +
            "\t\tdc.CONS_NAME,\n" +
            "\tCASE\n" +
            "\t\t\tdab.DEVICE_RISE_TIME_CODE \n" +
            "\t\t\tWHEN '0508904' THEN\n" +
            "\t\t\tIFNULL( sum( dcap.CONS_DES_POWER ), 0 ) ELSE 0 \n" +
            "\t\tEND AS CONS_DES_POWER4,\n" +
            "\tCASE\n" +
            "\t\t\tdab.DEVICE_RISE_TIME_CODE \n" +
            "\t\t\tWHEN '0508904' THEN\n" +
            "\t\t\tIFNULL( sum( dcap.CONS_RIS_POWER ), 0 ) ELSE 0 \n" +
            "\t\tEND AS CONS_RIS_POWER4\n" +
            "\tFROM\n" +
            "\t\tdr_cons dc\n" +
            "\t\tLEFT JOIN dr_device_adjustable_base dab ON dc.ID = dab.CONS_ID\n" +
            "\t\tLEFT JOIN dr_cons_adjustable_potential dcap ON dc.ID = dcap.CONS_ID \n" +
            "\tGROUP BY\n" +
            "\t\tdc.ID\n" +
            "\t) d ON a.id = d.id")
    List<AdjustSpeedResult> adjustSpeed(Page<AdjustSpeedResult> page);

    /**
     * 用户可调资源统计
     * @param page
     * @param startTime
     * @return
     */
    List<AdjustResourceResult> getUserAdjustRes(Page<AdjustResourceResult> page, @Param("startTime") String startTime,@Param("endTime") String endTime);

    /**
     * 设备可持续响应时间统计
     * @param page
     * @param startTime
     * @param endTime
     * @return
     */
    List<DeviceResponseTimeResult> getDeviceResponseTime(Page<DeviceResponseTimeResult> page, @Param("startTime") String startTime,@Param("endTime") String endTime);

    /**
     * 设备类型统计
     * @return
     */
    List<DeviceTypeResult> getDeviceType();

    /**
     * 各单位资源统计
     * @return
     */
    List<DeviceTypeResult> getCoResource();

    /**
     * 响应准备时间可调节资源统计
     * @return
     */
    ResponseTimeStatisticsResult responseTimeStatistics();

}
