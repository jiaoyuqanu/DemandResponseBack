package com.xqxy.dr.modular.data.param;

import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 用户功率曲线
 * </p>
 *
 * @author xiao jun
 * @since 2021-03-11
 */
@ApiModel(description = "用户功率曲线 参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class ConsCurveConsParam extends BaseParam {
    private List<Long> consNoList;

    private LocalDate dataDate;

}
