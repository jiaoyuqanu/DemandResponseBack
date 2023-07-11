package com.xqxy.core.util;

import java.util.Map;
import java.util.Set;

public class SmsUtils {

    public static String getSmsText(Map<String, String> map, String htmlTemplate) {
        String htmlText = htmlTemplate;
        // 遍历Map中的所有Key，将得到的value值替换模板字符串中的变量值
        Set<String> keys = map.keySet();
        for (String key : keys) {
            if (htmlText.contains("${" + key + "}")) {
                htmlText = htmlText.replace("${" + key + "}", map.get(key));
            }
        }
        return htmlText;
    }

}
