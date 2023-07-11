package com.xqxy.dr.modular.powerplant.param;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author czj
 * @since 2021-12-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DrDaVoidanceParam extends BaseParam implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "出清结果id")
    @NotNull(message = "id不能为空，请检查id参数", groups = {detail.class})
    private Long id;

    /**
     * 调峰日期
     */
    @ApiModelProperty(value = "调峰日期")
    private LocalDate peakDate;

    /**
     * 调峰时段
     */
    @ApiModelProperty(value = "调峰时段")
    private String peakTimeInterval;

    /**
     * 负荷聚合商no
     */
    @ApiModelProperty(value = "负荷聚合商no")
    private String aggregatorNo;

}
