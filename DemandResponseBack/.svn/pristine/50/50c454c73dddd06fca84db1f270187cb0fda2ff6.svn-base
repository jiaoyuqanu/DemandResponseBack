package com.xqxy.dr.modular.data.param;

import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
public class ConsAndDate extends BaseParam {

    /**
     * 用户编号:与省级智慧能源服务平台用户档案编号一致
     */
    @ApiModelProperty(value = "用户编号")
    private String consNo;

    @ApiModelProperty(value = "用户名")
    private String consName;

    /**
     * 营销户号
     */
    @ApiModelProperty(value = "营销户号")
    private String elecConsNo;

    /**
     * 需求响应用户分类:大用户/ 聚合商/代理用户
     */
    @ApiModelProperty(value = "用户分类")
    private String consType;

    /**
     * 数据日期
     */
    @ApiModelProperty(value = "数据日期")
    private String dataDate;

    /**
     * 用户区域等级
     */
    @ApiModelProperty(value = "用户区域等级")
    private String regionLevel;

    /**
     * 用户所属区域区域区域编码
     */
    @ApiModelProperty(value = "用户区域等级")
    private String regionCode;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private String startDate;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private String endDate;

    public boolean isTodayData(){
       return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now()).equals(this.dataDate);
    }


}
