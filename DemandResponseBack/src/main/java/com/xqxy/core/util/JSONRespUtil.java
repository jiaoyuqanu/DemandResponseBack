package com.xqxy.core.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Set;
import java.util.regex.Pattern;

public class JSONRespUtil {

    public static JSONObject removeP(JSONObject jsonObject, Integer type) {
        Pattern pattern = Pattern.compile("^p\\d+$");
        JSONObject jsonObject2 = new JSONObject();
        Set<String> keySet = jsonObject.keySet();
        for (String key : keySet) {
            Object o = jsonObject.get(key);
            if (o instanceof JSONObject) {
                jsonObject2.put(key, removeP((JSONObject) o, type));
            } else if (o instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) o;
                jsonObject2.put(key, removePArray(jsonArray, type));
            } else if (type == 1 && !pattern.matcher(key).find()) {
                jsonObject2.put(key, o);
            } else if (type == 2 && pattern.matcher(key).find()) {
                jsonObject2.put(key, o);
            }
        }
        return jsonObject2;
    }

    public static JSONArray removePArray(JSONArray jsonArray, Integer type) {
        JSONArray jsonArray2 = new JSONArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            Object o2 = jsonArray.get(i);
            if (o2 instanceof JSONObject) {
                jsonArray2.add(removeP((JSONObject) o2, type));
            } else if (o2 instanceof JSONArray) {
                jsonArray2.add(removePArray((JSONArray) o2, type));
            } else {
                jsonArray2.add(o2);
            }
        }
        return jsonArray2;
    }

}
