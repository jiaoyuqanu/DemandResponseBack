package com.xqxy.core.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * @Description
 * @ClassName LocalDateTimeUtil
 * @Author User
 * @date 2021.04.05 00:05
 */
public class LocalDateTimeUtil {

    //month: yyyy-MM
    public static LocalDateTime firstDayOfMonth(String month) {
        LocalDate localDate = LocalDate.parse(month + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTime localDateTime = localDate.atStartOfDay();
        //获取月的第一天0时0分0秒
        LocalDateTime firstDay = localDateTime.with(TemporalAdjusters.firstDayOfMonth())
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        return firstDay;
    }

    //month: yyyy-MM
    public static LocalDateTime lastDayOfMonth(String month) {
        LocalDate localDate = LocalDate.parse(month + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTime localDateTime = localDate.atStartOfDay();
        //获取月的最后一天的23点59分59秒
        LocalDateTime lastDay = localDateTime.with(TemporalAdjusters.lastDayOfMonth())
                .withHour(23)
                .withMinute(59)
                .withSecond(59);
        return lastDay;
    }



    /**
     * Date转换成LocalDate
     * @param date
     * @return
     */
    public static LocalDate dateToLocalDate(Date date) {
        if(null == date) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * LocalDate转换成Date
     * @param localDate
     * @return
     */
    public static Date localDateToDate(LocalDate localDate) {
        if(null == localDate) {
            return null;
        }
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }
}
