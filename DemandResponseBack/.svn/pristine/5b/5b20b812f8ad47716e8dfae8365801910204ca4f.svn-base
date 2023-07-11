package com.xqxy.dr.modular.baseline.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Calendar;
import java.util.Date;

/**
 * @Description
 * @ClassName CurveUtil
 * @Author User
 * @date 2020.07.02 16:05
 */
public class CurveUtil {

    public static String covDateTimeToCurvePoint(Date dateTime) {
        Calendar time = Calendar.getInstance();
        time.setTime(dateTime);
        int n = (time.get(Calendar.HOUR_OF_DAY) * 4 + time.get(Calendar.MINUTE) / 15);
        return "p" + (n + 1);
    }

    public static int covDateTimeToPoint(Date dateTime) {
        Calendar time = Calendar.getInstance();
        time.setTime(dateTime);
        int n = (time.get(Calendar.HOUR_OF_DAY) * 4 + time.get(Calendar.MINUTE) / 15);
        return n + 1;
    }

    public static String covDateTimeToCurvePoint(String dateTime) {
        Calendar time = Calendar.getInstance();
        time.setTime(DateUtil.parse(dateTime));
        int n = (time.get(Calendar.HOUR_OF_DAY) * 4 + time.get(Calendar.MINUTE) / 15);
        return "p" + (n + 1);
    }

    public static int covDateTimeToPoint(String dateTime) {
        Calendar time = Calendar.getInstance();
        time.setTime(DateUtil.parse(dateTime));
        int n = (time.get(Calendar.HOUR_OF_DAY) * 4 + time.get(Calendar.MINUTE) / 15);
        return n + 1;
    }

    public static String covCurvePointToDateTime(String curvePoint) {
        int point = Integer.parseInt(curvePoint.replace("p", ""));
        point--;
        int HH = point / 4;
        int mm = point % 4 * 15;

        return StrUtil.format("{}:{}", HH < 10 ? "0" + HH : HH, mm == 0 ? "00" : mm);
    }

   /* public static String covCurvePointToDateTime(String curvePoint) {
        int point = Integer.parseInt(curvePoint.replace("p", ""));
        point--;
        int HH = point / 4;
        int mm = point % 4 * 15;
        return StrUtil.format("{}:{}", HH == 0 ? "00" : HH, mm == 0 ? "00" : mm);
    }*/

    public static String covPointToDateTime(int point) {
        point--;
        int HH = point / 4;
        int mm = point % 4 * 15;
        return StrUtil.format("{}:{}", HH == 0 ? "00" : HH, mm == 0 ? "00" : mm);
    }

//    public static String covDateTimeToCurvePoint(String dateTime) {
//        Calendar time = Calendar.getInstance();
//        time.setTime(DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", dateTime));
//        int n = (time.get(Calendar.HOUR_OF_DAY) * 4 + time.get(Calendar.MINUTE) / 15 + 1) * 3 - 2;
//        System.out.println(n);
//        return "p" + n;
//    }
//
//    public static int covDateTimeToPoint(String dateTime) {
//        Calendar time = Calendar.getInstance();
//        time.setTime(DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", dateTime));
//        int n = (time.get(Calendar.HOUR_OF_DAY) * 4 + time.get(Calendar.MINUTE) / 15 + 1) * 3 - 2;
//        System.out.println(n);
//        return n;
//    }

    public static void main(String[] args) {
//        System.out.println(CurveUtil.covDateTimeToCurvePoint(DateUtil.parse("23:55:00")));
        System.out.println(CurveUtil.covDateTimeToPoint(new Date()));
//        System.out.println(CurveUtil.covDateTimeToCurvePoint("23:55:00"));
//        System.out.println(CurveUtil.covDateTimeToPoint("23:55:00"));
        System.out.println(CurveUtil.covCurvePointToDateTime("p65"));
//        System.out.println(CurveUtil.covPointToDateTime(96));
    }
}
