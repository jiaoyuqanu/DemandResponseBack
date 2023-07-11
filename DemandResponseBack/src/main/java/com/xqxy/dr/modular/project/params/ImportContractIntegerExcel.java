package com.xqxy.dr.modular.project.params;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ImportContractIntegerExcel extends ImportContractExcel{

    @Excel(name = "分成比例(%)（同一个用户的分成比例必须相同，如果不同，默认存储第一个）", width = 20, orderNum = "100")
    private BigDecimal extractRatio;


}
