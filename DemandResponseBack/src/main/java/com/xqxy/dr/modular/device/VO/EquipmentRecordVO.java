package com.xqxy.dr.modular.device.VO;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 设备记录实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentRecordVO implements Serializable {

    @TableId(value="id",type = IdType.ASSIGN_ID)
    @TableField(fill = FieldFill.INSERT)
    private String id;

    /**
     * 用户编号
     */
    @TableField("equipment_no")
    private  String  equipmentNo;

    /**
     * 中台数据id
     */
    private  String  dataId;

    /**
     * 数据日期 格式为20211022
     */
    private String ymd;

    private	String	t0000	;

    private	String	t0015	;

    private	String	t0030	;

    private	String	t0045	;

    private	String	t0100	;

    private	String	t0115	;

    private	String	t0130	;

    private	String	t0145	;

    private	String	t0200	;

    private	String	t0215	;

    private	String	t0230	;

    private	String	t0245	;

    private	String	t0300	;

    private	String	t0315	;

    private	String	t0330	;

    private	String	t0345	;

    private	String	t0400	;

    private	String	t0415	;

    private	String	t0430	;

    private	String	t0445	;

    private	String	t0500	;

    private	String	t0515	;

    private	String	t0530	;

    private	String	t0545	;

    private	String	t0600	;

    private	String	t0615	;

    private	String	t0630	;

    private	String	t0645	;

    private	String	t0700	;

    private	String	t0715	;

    private	String	t0730	;

    private	String	t0745	;

    private	String	t0800	;

    private	String	t0815	;

    private	String	t0830	;

    private	String	t0845	;

    private	String	t0900	;

    private	String	t0915	;

    private	String	t0930	;

    private	String	t0945	;

    private	String	t1000	;

    private	String	t1015	;

    private	String	t1030	;

    private	String	t1045	;

    private	String	t1100	;

    private	String	t1115	;

    private	String	t1130	;

    private	String	t1145	;

    private	String	t1200	;

    private	String	t1215	;

    private	String	t1230	;

    private	String	t1245	;

    private	String	t1300	;

    private	String	t1315	;

    private	String	t1330	;

    private	String	t1345	;

    private	String	t1400	;

    private	String	t1415	;

    private	String	t1430	;

    private	String	t1445	;

    private	String	t1500	;

    private	String	t1515	;

    private	String	t1530	;

    private	String	t1545	;

    private	String	t1600	;

    private	String	t1615	;

    private	String	t1630	;

    private	String	t1645	;

    private	String	t1700	;

    private	String	t1715	;

    private	String	t1730	;

    private	String	t1745	;

    private	String	t1800	;

    private	String	t1815	;

    private	String	t1830	;

    private	String	t1845	;

    private	String	t1900	;

    private	String	t1915	;

    private	String	t1930	;

    private	String	t1945	;

    private	String	t2000	;

    private	String	t2015	;

    private	String	t2030	;

    private	String	t2045	;

    private	String	t2100	;

    private	String	t2115	;

    private	String	t2130	;

    private	String	t2145	;

    private	String	t2200	;

    private	String	t2215	;

    private	String	t2230	;

    private	String	t2245	;

    private	String	t2300	;

    private	String	t2315	;

    private	String	t2330	;

    private	String	t2345	;

}
