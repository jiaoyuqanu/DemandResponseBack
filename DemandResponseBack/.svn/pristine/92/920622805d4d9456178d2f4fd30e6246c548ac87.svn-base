package com.xqxy.core.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


/**
 * @Description
 * @ClassName TimeUtil
 * @Author User
 * @date 2021.04.01 15:04
 */
public class TimeUtil {

    /**
     * @param targetTime HH:mm:ss
     * @param startTime  HH:mm:ss
     * @param endTime    HH:mm:ss
     * @return
     */
    public static boolean isBetween(String targetTime, String startTime, String endTime) {
        DateTime start = DateUtil.parse(startTime);
        DateTime end = DateUtil.parse(endTime);
        DateTime target = DateUtil.parse(targetTime);
        boolean in;
        if (end.getTime() >= start.getTime()) {
            in = target.getTime() >= start.getTime() && target.getTime() <= end.getTime();
        } else {
            in = (target.getTime() >= start.getTime() && target.getTime() <= DateUtil.parse("24:00:00").getTime()) || (target.getTime() >= DateUtil.parse("00:00:00").getTime() && target.getTime() <= end.getTime());
        }
        return in;

    }


    /**
     * 获取前几天的日期集合
     * @param baseDate
     * @param num
     * @return
     */
    public static List<String> lastSevenDays(LocalDate baseDate,Integer num){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<String> resultDate = new ArrayList<>();
        while(num > 0) {
            LocalDate localDate = baseDate.minusDays(num);
            resultDate.add(dateTimeFormatter.format(localDate));
            num--;
        }
        return resultDate;
    }

}
