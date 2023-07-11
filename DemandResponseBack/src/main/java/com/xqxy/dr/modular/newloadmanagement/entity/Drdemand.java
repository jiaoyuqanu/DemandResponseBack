package com.xqxy.dr.modular.newloadmanagement.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("dr_demand") //表名 需求响应需求
public class Drdemand {

    private Long regulateId;
    private Long projectId;//'项目标识'
    private String regulateNo;//需求编号
    private String responseType;//响应类型  1削峰，2填谷，3新能源
    private String timeType;//'时间类型 1邀约，2实时'
    private String rangeType;//调控范围类别：地区/分区/变电站/线路/台区
    private String regulateRange;//调控范围
    private BigDecimal regulateCap;//调控目标
    private String regulateDate;//'调控日期'
    private String startTime;//响应开始时间
    private String endTime;//'响应结束时间'
    private int advanceNoticeTime;//提前通知时间(分钟)
    private String createUserName;//创建人姓名
    private String status;//需求状态01保存02事件已生成03指令已下发
    private String createTime;//'创建时间'
    private Long createUser;//'创建人'
    private String updateTime;//'更新时间'
    private Long updateUser;//'更新人'
    private String stregyId;//负控id

}
