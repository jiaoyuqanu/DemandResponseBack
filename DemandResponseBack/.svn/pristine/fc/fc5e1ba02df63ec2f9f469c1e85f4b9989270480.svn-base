package com.xqxy.dr.modular.subsidy.util;

import com.xqxy.sys.modular.dict.entity.DictData;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class DictUtil {

    public static BigDecimal getDictValue(List<DictData> dataList, BigDecimal val) {

        for (DictData data : dataList) {
            String value = data.getValue();
            String remark = data.getRemark();
            boolean leftFlag = true;
            boolean rightFlag = true;

            if (remark != null && ! remark.equals("")) {
                remark = remark.trim();
                int len = remark.length();

                String left = remark.substring(0, 1);
                String[] nums = remark.substring(1, len - 1).split("[,，]");
                String leftNum = nums[0].trim();
                String rightNum = nums[1].trim();
                String right = remark.substring(len - 1, len);

                if (!leftNum.equals("∞")) {
                    if (left.equals("(") || left.equals("（")) {
                        leftFlag = val.compareTo(new BigDecimal(leftNum)) > 0;
                    } else if (left.equals("[") || left.equals("【")) {
                        leftFlag = val.compareTo(new BigDecimal(leftNum)) >= 0;
                    }
                }

                if (!rightNum.equals("∞")) {
                    if (right.equals(")") || right.equals("）")) {
                        rightFlag = val.compareTo(new BigDecimal(rightNum)) < 0;
                    } else if (right.equals("]") || right.equals("】")) {
                        rightFlag = val.compareTo(new BigDecimal(rightNum)) <= 0;
                    }
                }
            }

            if (leftFlag && rightFlag) {
                return new BigDecimal(value);
            }
        }

        return BigDecimal.ZERO;
    }

    public static String getSettlementId() {

        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");

        return format.format(date);
    }

    public static String getSettlementWeek(LocalDate beginDate, LocalDate endDate) {

        String bYear = beginDate.getYear() + "年";
        String bMonth = beginDate.getMonthValue() + "月";
        String bDay = beginDate.getDayOfMonth() + "日";

        String eYear = endDate.getYear() + "年";
        String eMonth = endDate.getMonthValue() + "月";
        String eDay = endDate.getDayOfMonth() + "日";

        return bYear + bMonth + bDay + " 至 " + eYear + eMonth + eDay;
    }
}
