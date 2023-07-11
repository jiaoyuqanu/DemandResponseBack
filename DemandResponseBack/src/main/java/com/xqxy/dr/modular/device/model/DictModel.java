package com.xqxy.dr.modular.device.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictModel {

    @Excel(name = "编码")
    private String code;

    @Excel(name = "值")
    private String value;

}
