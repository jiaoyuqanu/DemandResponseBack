package com.xqxy.dr.modular.pcdata.param;

import lombok.Data;

/**
 * Author: zgy
 * Date: 2021/11/26 16:25
 * Content: 用户潜力普查信息
 */
@Data
public class PcConsAblity {
    /**
     * 所属地区
     */
    private String YHDZ;

    /**
     * 用户大类名称
     */
    private String YHDLMC;

    /**
     * 用户大类编码
     */
    private String YHDLBM;

    /**
     * 用户小类名称
     */
    private String YHXLMC;

    /**
     * 用户小类编码
     */
    private String YHXLBM;

    /**
     * 削峰响应准备时间（单位:时/分/秒）
     */
    private String XFXYZBSJ;

    /**
     * 填谷响应准备时间（单位:时/分/秒）
     */
    private String TGXYZBSJ;

    /**
     * 企业生产经营开始时间
     */
    private String YXKSSJ;

    /**
     * 企业生产经营结束时间
     */
    private String YXJSSJ;

    /**
     * 保安容量
     */
    private String BARL;

    /**
     * 上年实际响应最大负荷
     */
    private String SNSJXYZDFH;

    /**
     * 去年参与削峰响应次数
     */
    private String QNCYXFXYCS;

    /**
     * 去年累计削减负荷
     */
    private String QNLJXJFH;

    /**
     * 去年参与填谷响应次数
     */
    private String QNCYTGXYCS;

    /**
     * 去年累计消纳电量
     */
    private String QNLJXNDL;

    /**
     * 期望削峰电价
     */
    private String QWXFDJ;

    /**
     * 普查日期
     */
    private String PCRQ;

    /**
     * 期望填谷电价
     */
    private String QWTGDJ;

    /**
     * 创建人
     */
    private String CJR;
}
