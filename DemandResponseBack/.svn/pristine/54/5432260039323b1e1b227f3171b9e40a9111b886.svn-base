package com.xqxy.dr.modular.powerplant.param;

import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 负荷聚合商报价信息
 * </p>
 *
 * @author czj
 * @since 2021-12-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DrDaAggregatorBaseLineParam extends BaseParam implements Serializable {

private static final long serialVersionUID=1L;

    /**
     * 负荷聚合商报价id
     */
    @ApiModelProperty(value = "负荷聚合商报价id")
    @NotNull(message = "id不能为空，请检查id参数", groups = {edit.class})
    private Long id;


    /**
     * 负荷聚合商组号
     */
    @ApiModelProperty(value = "负荷聚合商组号")
    @NotNull(message = "aggregatorNo不能为空，请检查aggregatorNo参数", groups = {detail.class})
    private Long aggregatorNo;


    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @NotNull(message = "measureDate不能为空，请检查measureDate参数", groups = {detail.class})
    private String measureDate;


    /**
     * 提交状态1已提交0未提交
     */
    private String status;


}
