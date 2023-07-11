package com.xqxy.dr.modular.powerplant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xqxy.core.pojo.base.param.BaseParam;
import com.xqxy.dr.modular.powerplant.param.Param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 设备档案
 * </p>
 *
 * @author xiao jun
 * @since 2021-10-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_da_device_child")
public class DaDeviceChild extends Param implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("父级设备编码")
    private String parentDeviceId;

    @ApiModelProperty("设备编码")
    private String deviceId;

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("设备类型")
    private String deviceType;

    @ApiModelProperty("所属聚合资源编号")
    private String jhzyId;

    @ApiModelProperty("所属业主编号")
    private String userId;

    @ApiModelProperty("安装地址")
    private String location;

    @ApiModelProperty("安装单位")
    private String azCompany;

    @ApiModelProperty("维护单位")
    private String whCompany;

    @ApiModelProperty("投运日期")
    private LocalDateTime tyDate;

    @ApiModelProperty("设备品牌")
    private String deviceBrand;

    @ApiModelProperty("设备型号")
    private String deviceModel;

    @ApiModelProperty("额定电压")
    private String eddy;

    @ApiModelProperty("额定电流")
    private String eddl;

    @ApiModelProperty("额定负荷")
    private String edfh;

    @ApiModelProperty("额定功率")
    private String edgl;

    @ApiModelProperty("额定频率")
    private String edpl;

    @ApiModelProperty("制冷量")
    private String zll;

    @ApiModelProperty("制冷消耗功率")
    private String zlxhgl;

    @ApiModelProperty("制热量")
    private String zrl;

    @ApiModelProperty("制热消耗功率")
    private String zrxhgl;

    @ApiModelProperty("热测最高工作压力")
    private String rczggzyl;

    @ApiModelProperty("冷测最高工作压力")
    private String lczggzyl;

    @ApiModelProperty("质量")
    private String zhiliang;

    @ApiModelProperty("防触电保护类别")
    private String fzdbhlb;

    @ApiModelProperty("防水等级")
    private String fsdj;

    @ApiModelProperty("制造日期")
    private LocalDateTime makeDate;

    @ApiModelProperty("出厂日期")
    private LocalDateTime ccDate;

    @ApiModelProperty("定值模板")
    private String templateId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
