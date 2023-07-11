package com.xqxy.dr.modular.powerplant.param;

import com.xqxy.core.pojo.base.param.BaseParam;
import com.xqxy.dr.modular.powerplant.entity.DrDaAggregatorPrice;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 负荷聚合商报价信息
 * </p>
 *
 * @author czj
 * @date 2021-12-10 9:07
 */
@Data
public class DrDaAggregatorPriceParam extends BaseParam {


    /**
     * 电力市场竞价公告id
     */
    @ApiModelProperty(value = "电力市场竞价公告id")
    @NotNull(message = "electricBidNoticeId不能为空，请检查electricBidNoticeId参数", groups = {page.class})
    private Long electricBidNoticeId;

    /**
     * 申报人
     */
    @ApiModelProperty(value = "申报人")
    private String declareName;

    /**
     * 负荷聚合商报价数组
     */
    @ApiModelProperty(value = "负荷聚合商报价数组")
    private List<DrDaAggregatorPrice> drDaAggregatorPriceList;

    /**
     * 提交状态1已提交0未提交
     */
    private String status;
    
}
