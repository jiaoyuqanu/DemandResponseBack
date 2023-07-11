package com.xqxy.sys.modular.utils;

import java.util.*;

public class StringUtils {
    /**
     * 判斷字符串是否纯数字
     * */
    public static boolean isNumeric(String str){
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isDigit(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }
    /**
     * 入参
     * value=t110
     * key=t
     * 判断字符串开头
     * value 为传入的参数
     * key 为模板
     * */
    public static boolean isString(String value ,String key){
        if(value==null||key==null){
            return false;
        }
        String substring = value.substring(0, 1);
        return key.equals(substring);
    }


    public static void main(String[] args) {

        Map<String,Object> map=new HashMap<String,Object>();
        Map<String,Object> mapParam=new HashMap<String,Object>();
        mapParam.put("t0000","0000");
        mapParam.put("t0034","0034");
        mapParam.put("t0040","0034");
        mapParam.put("p0040","0034");
        mapParam.put("p0t40","0034");
        List<String> strings =null;
        for(int i =0;i<=1;i++){
            if(strings!=null){
                break;
            }
            strings=mapToList( mapParam);
        }

        for(int i=0;strings.size()<96;i++){
            String  mapPar= mapParam.get(strings.get(i))==null?null:(String) mapParam.get(strings.get(i));
            map.put("p"+(i+1),strings.get(i));
        }

    }

    private static List<String > mapToList(Map<String,Object> mapParam){
        List<String> list=null;
        if(list==null || list.size()!=96){
            Set<Map.Entry<String, Object>> entries = mapParam.entrySet();
            list=new ArrayList<String>();
            for (Map.Entry<String, Object> entry:entries){
                System.out.println(entry.getKey()+"=="+entry.getValue());
                String key = entry.getKey();
                //获取
                if(key!=null&&key.length()==5  ){
                    String substring = key.substring(0, 1);
                    String substring1 = key.substring(1, 4);
                    if(isString(key,"t")&&isNumeric(substring1)){
                        list.add(key);
                    }
                }
            }
            Collections.sort(list);
        }
        return list;
    }

}
