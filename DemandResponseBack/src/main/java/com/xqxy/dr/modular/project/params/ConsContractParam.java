package com.xqxy.dr.modular.project.params;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xqxy.core.pojo.base.param.BaseParam;
import com.xqxy.dr.modular.project.entity.ConsContractDetail;
import com.xqxy.sys.modular.cust.entity.Cons;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * User Declare Project Param
 * </p>
 *
 * @author Caoj
 * @since 2021-10-15 15:27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ConsContractParam  extends BaseParam {
    private static final long serialVersionUID = 8144420388930294368L;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "用户标识")
    private String consId;

    @ApiModelProperty(value = "项目标识")
    private Long projectId;

    @ApiModelProperty(value = "1直接参与，2代理参与")
    private String particType;

    @ApiModelProperty(value = "分成比例")
    private BigDecimal extractRatio;

    @ApiModelProperty(value = "第一联系人")
    private String firstContactName;

    @ApiModelProperty(value = "第一联系人联系方式")
    private String firstContactInfo;

    @ApiModelProperty(value = "第二联系人姓名")
    private String secondContactName;

    @ApiModelProperty(value = "第二联系人联系方式")
    private String secondContactInifo;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private Long createUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    private Long updateUser;

    @ApiModelProperty(value = "用户列表")
    private List<Cons> consList;

    @ApiModelProperty(value = "最大响应负荷")
    private BigDecimal responseCap;

    @ApiModelProperty(value = "签约文件名称")
    private String fileName;

    @ApiModelProperty(value = "签约关联文件ID")
    private Long fileId;

    @ApiModelProperty(value = "签约文件类型")
    private String fileType;

    @ApiModelProperty(value = "申报标识")
    private String contractId;

    @ApiModelProperty(value = "省码")
    private String provinceCode;

    @ApiModelProperty(value = "市码")
    private String cityCode;

    @ApiModelProperty(value = "区县码")
    private String countyCode;

    @ApiModelProperty(value = "开始时间")
    private String startDate;

    @ApiModelProperty(value = "结束时间")
    private String endDate;

    @ApiModelProperty(value = "签约详情")
    private List<ConsContractDetail> fillInDetailList;

    @ApiModelProperty(value = "审核意见")
    private String checkMess;

    @ApiModelProperty(value = "审核结果")
    private String checkResult;

    @ApiModelProperty(value = "明细标识")
    private String detailId;

    @ApiModelProperty(value = "签约id")
    private String contractDetailId;

    private List<String> orgIds;

    private String orgId;

    private String creditCode;

    /**
     * 空调容量
     */
    private BigDecimal AirconditionCap;

    /**
     * 响应负荷：1   备用容量：2
     */
    private Integer type;
}
