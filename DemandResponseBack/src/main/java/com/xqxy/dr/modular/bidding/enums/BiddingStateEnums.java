package com.xqxy.dr.modular.bidding.enums;


import lombok.Getter;

@Getter
public enum BiddingStateEnums {

    SAVE("1", "保存"),

    ISSUED("2", "已发布");

    private final String code;

    private final String message;

    BiddingStateEnums(String code, String message) {
        this.code = code;
        this.message = message;
    }


}
