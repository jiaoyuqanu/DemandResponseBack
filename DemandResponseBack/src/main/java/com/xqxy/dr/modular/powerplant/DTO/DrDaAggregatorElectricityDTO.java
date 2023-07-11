package com.xqxy.dr.modular.powerplant.DTO;

import com.xqxy.core.pojo.base.param.BaseParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrDaAggregatorElectricityDTO extends BaseParam {

    /**
     * 电力市场id
     */
    private Long electricMarketId;

    /**
     *
     */
    private List<Long> aggregatorIds;
}
