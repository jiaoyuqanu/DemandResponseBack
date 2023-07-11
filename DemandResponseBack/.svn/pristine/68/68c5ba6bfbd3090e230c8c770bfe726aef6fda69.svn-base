package com.xqxy.dr.modular.workbench.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkbenchParam {
    /**
     * 供电单位
     */
    private String orgNo;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 类型
     */
    private String type;

    /**
     * 响应类型1削峰，2填谷
     */
    private String responseType;

    /**
     * 时间类型 1邀约，2实时
     */
    private String timeType;

    /**
     * 提前通知时间 单位分钟
     */
    private Integer advanceNoticeTime;

    /**
     * 项目详情id
     */
    private String projectDetailId;

    /**
     * 本级和下级区域集合
     */
    private List<String> orgList;

    /**
     * 审核状态 1:未提交，2：审核中，3：审核通过，4：审核不通过
     */
    private String checkStatus;

    /**
     * 审核状态 1:未提交，2：审核中，3：审核通过，4：审核不通过
     */
    private String underReview;
}
