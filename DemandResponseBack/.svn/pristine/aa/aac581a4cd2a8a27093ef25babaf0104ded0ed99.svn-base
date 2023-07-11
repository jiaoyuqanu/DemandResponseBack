package com.xqxy.core.util;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
     * @Title: DesensitizedUtils
     */
    public class DesensitizedUtils {
        /**
         * 【中文姓名】只显示第一个汉字，其他隐藏为2个星号，比如：李**
         *
         * @param fullName
         * @return
         */
        public static String fullname(String fullName) {
            if (StringUtils.isBlank(fullName)) {
                return "";
            }
            String name = StringUtils.left(fullName, 1);
            return StringUtils.rightPad(name, StringUtils.length(fullName), "*");
        }
        /**
         * 【生日】只显示第一个字，其他隐藏为2个星号，比如：李**
         *
         * @param birthday
         * @return
         */
        public static String birthday(Date birthday) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String birth=sdf.format(birthday);
            if (StringUtils.isBlank(birth)) {
                return "";
            }
            String name = StringUtils.left(birth, 0);
            return StringUtils.rightPad(name, StringUtils.length(birth), "*");
        }

        /**
         * 【固定电话 后四位，其他隐藏，比如1234
         *
         * @param num
         * @return
         */
        public static String tel(String num) {
            if (StringUtils.isBlank(num)) {
                return "";
            }
            return StringUtils.left(num, 3).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(num, 2), StringUtils.length(num), "*"), "***"));
        }

        /**
         * 【手机号码】前三位，后四位，其他隐藏，比如135******10
         *
         * @param num
         * @return
         */
        public static String phone(String num) {
            if (StringUtils.isBlank(num)) {
                return "";
            }
            return StringUtils.left(num, 3).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(num, 2), StringUtils.length(num), "*"), "***"));
        }

        /**
         * 【电子邮箱 邮箱前缀仅显示3字母，前缀其他隐藏，用星号代替，@及后面的地址显示，比如：d**@126.com>
         *
         * @param email
         * @return
         */
        public static String email(String email) {
            if (StringUtils.isBlank(email)) {
                return email;
            }
            int index = StringUtils.indexOf(email, "@");
            if(index <= 1) {
                return email;
            } else {
                return StringUtils.rightPad(StringUtils.left(email, 3), index, "*").concat(StringUtils.mid(email, index, StringUtils.length(email)));
            }

        }


    }
