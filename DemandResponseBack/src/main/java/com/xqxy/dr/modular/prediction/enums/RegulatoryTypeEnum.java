package com.xqxy.dr.modular.prediction.enums;

import com.alibaba.fastjson.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonFormat;

@JSONType(serializeEnumAsJavaBean = true)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RegulatoryTypeEnum {
    Regulatory_TypeEnum_1("下调","1"),
    Regulatory_TypeEnum_2("上调","0");
    // 成员变量
    private final String name;
    private final String code;

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
    // 构造方法
    RegulatoryTypeEnum(String name, String code) {
        this.name = name;
        this.code = code;
    }

    // 普通方法
    public static String getNameByCode(String code) {
        for (RegulatoryTypeEnum c : RegulatoryTypeEnum.values()) {
            if (code.equals(c.getCode())) {
                return c.name;
            }
        }
        return null;
    }
}
