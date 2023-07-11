package com.xqxy.dr.modular.anhui.utils;


import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import com.alibaba.fastjson.JSON;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 安徽数据中台历史负荷接口服务实现类
 * </p>
 *
 * @author liuyu
 * @since 2021-12-01
 */

public class AnhuiAdsCstUtils {

    private static final Log log = Log.get();

    /*private static final ThreadLocal<List<Map<Object,Object>>> bachList=new ThreadLocal<>();
    static{
        //HTTP Client init
        HttpClientBuilderParams httpParam = new HttpClientBuilderParams();
        httpParam.setAppKey("1660039420735541");
        httpParam.setAppSecret("2e4fd2ca6dc34c5091460d0fbf9d315");
        HttpApiClientAPI.getInstance().init(httpParam);
    }

    public static String queryHistoryCurveToAnhuiApi(String pageNo,String pageSize,String consNo,String dataTime) {

        HttpApiClientAPI.getInstance().queryAdsCstZhnyHistoryCurve(pageNo , pageSize , consNo ,dataTime , new ApiCallback() {
            @Override
            public void onFailure(ApiRequest request, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(ApiRequest request, ApiResponse response) {
             *//*   try {
                    String resultString = getResultString(response);
                    System.out.println("jieguojji:"+resultString);
                    if(bachList.get()==null){
                        bachList.set(Lists.newArrayList());
                    }else {
                        bachList.get().clear();
                    }
                    if(resultString!=null){
                        bachList.get().add(resultString);
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }*//*
            }
        });
        return null;
    }*/
    public static String queryCons(String url,String appCode,int pageNo,int pageSize,String consNO){
        /*ApiResponse response = HttpApiClientAPI.getInstance().query_ads_cst_zhny_dr_cons_Mode(pageNo, pageSize , consNO);
        log.info("中台接口返回：" + JSON.toJSONString(response));
        try {
            Map<Object, Object> resultString = getResultString(response);
            if(bachList.get()==null){
                bachList.set(Lists.newArrayList());
            }else {
                bachList.get().clear();
            }
            if(resultString!=null){
                bachList.get().add(resultString);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        List<Map<Object, Object>> maps = bachList.get();
        if(null != maps && maps.size()>0){
            return maps.get(0);
        }
        return null;*/
        String path = url + "?appcode=" + appCode + "&pageNum=" + pageNo + "&pageSize=" + pageSize + "&consNo=" + consNO;
        log.info(path);

        long time = Calendar.getInstance().getTime().getTime();

        HttpResponse rest = HttpRequest.post(path).execute();

        long time2 = Calendar.getInstance().getTime().getTime();
        long haoshi = time2-time;
        log.info(">>> 请求中台接口总计耗时："+ haoshi);

        if(rest.getStatus()!=200){
            log.error(JSON.toJSONString(rest));
            return null;
        }

        String body = rest.body();
        log.info(body);


        return body;
    }

    /*private static   Map<Object,Object> getResultString(ApiResponse response) throws IOException {
        StringBuilder result = new StringBuilder();
        result.append(SdkConstant.CLOUDAPI_LF).append(new String(response.getBody() , SdkConstant.CLOUDAPI_ENCODING));
        log.info(">>> 中台返回数据：{}",result);
        Map<String,Object> cons = (Map<String, Object>) JSON.parse(String.valueOf(result));
       if(null!=cons && cons.size()>0){
           Map<Object,Object> data= (Map<Object, Object>) cons.get("data");
           if(null!=data && data.size()>0){
               List<Map<Object,Object>> rows= (List<Map<Object, Object>>) data.get("rows");
               if(rows!=null && rows.size()>0){
                   return rows.get(0);
               }
           }
       }
        return null;
    }*/
}
