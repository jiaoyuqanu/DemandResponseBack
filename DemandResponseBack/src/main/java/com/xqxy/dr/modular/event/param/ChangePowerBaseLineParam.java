package com.xqxy.dr.modular.event.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class ChangePowerBaseLineParam {

    private String eventId;

    private String orgId;

    private BigDecimal allPFixValue;

    @JsonProperty(value = "pValueMap")
    private Map<String, BigDecimal> pValueMap;

}
