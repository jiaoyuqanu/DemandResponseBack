package com.xqxy.dr.modular.anhui.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 国泉实时负荷数据表映射类
 * @author liuyu
 * @since 2021-12-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sca_sg_data")
public class HefeiCurverSgData implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 产生该记录唯一流水号
     */
    private String id;
    /**
     *营销户号
     */
    private String consNo;
    /**
     *计量点标识
     */
    private String mpId;
    /**
     *计量点编号
     */
    private String mpNo;
    /**
     *电能表标识
     */
    private String meterId;
    /**
     *数据项编号
     */
    private String cpDataNo;
    /**
     *倍率
     */
    private String tFactor;
    /**
     *采集时间
     */
    private String dataTime;
    /**
     *采集值
     */
    private BigDecimal dataValue;
    /**
     *数据来源，01=用采，02=调控
     */
    private String dataSrc;
    /**
     *入库时间
     */
    private String createTime;

}
