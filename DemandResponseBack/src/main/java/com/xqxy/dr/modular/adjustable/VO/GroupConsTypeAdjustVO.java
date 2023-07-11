package com.xqxy.dr.modular.adjustable.VO;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

@Data
public class GroupConsTypeAdjustVO implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 用户类型 BIG_CLASS_NAME
     */
    private String bigClassName;

    /**
     * 削峰人数
     */
    private String desCount;

    /**
     * 削峰总负荷
     */
    private String desCapacitySum ;

    /**
     * 填谷人数
     */
    private String risCount;

    /**
     * 填谷总负荷
     */
    private String risCapacitySum ;

}
