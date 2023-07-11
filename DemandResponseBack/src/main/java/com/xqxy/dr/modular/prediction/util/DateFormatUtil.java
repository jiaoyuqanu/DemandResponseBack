package com.xqxy.dr.modular.prediction.util;

import com.xqxy.core.util.DateUtil;

import java.time.LocalDate;
import java.time.ZoneId;

/**
 * 时间格式转换类
 * @author wang yunfei
 * @since 2021-11-06
 */
public class DateFormatUtil {

    /**
     * string 转 LocalDate 年-月-日
     * @param date
     * @return
     */
    public static LocalDate string2LocalDate(String date){

        return DateUtil.parseDate(date,"yyyy-MM-dd").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
