package com.xqxy.dr.modular.event.enums;

import lombok.Getter;

    @Getter
    public enum  NoReplyReason {

        /**
         * WEB
         */
        TIME_DEAD("0", "邀约时间截止"),

        /**
         * APP
         */
        CAP_FULL("1", "超出调控停止响应量");

        private final String code;

        private final String message;

        NoReplyReason(String code, String message) {
            this.code = code;
            this.message = message;
        }


}
