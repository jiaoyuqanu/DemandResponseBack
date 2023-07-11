package com.xqxy.sys.modular.utils;


import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Configuration
public class HttpClientUtil {

    private final static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    /**
     * 向指定URL发送POST方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求Map参数，请求参数应该是 {"name1":"value1","name2":"value2"}的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static JSONObject sendPost(String url, Map<String, Object> param) {
        //请求地址
        HttpPost httpPost = new HttpPost(url);


        //设置请求的header
        httpPost.addHeader("Content-Type", "application/json;charset=utf-8");

        //设置参数
        /*JSONObject jsonParam = new JSONObject();
        jsonParam.put("batchSMSContentBOs",batchSMSContentBOs);*/
        JSONObject jsonObject = JSONObject.fromObject(param);
       /* String  a="[{\"phone\":\"17373903348\",\"content\":\"测试\",\"businessId\":\"11\"},\n" +
                "        {\"phone\":\"17373903340\",\"content\":\"测试\",\"businessId\":\"11\"}]";*/
        StringEntity entity = new StringEntity(jsonObject.toString(), "utf-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);

        //创建通道
        HttpClient httpClient = HttpClientBuilder.create().build();

        // 执行请求
        JSONObject returnJson = null;
        try {
            logger.warn("正在请求中,请等待。。。。。。。StringEntity:{}", entity);
            HttpResponse response = httpClient.execute(httpPost);
            String json2 = EntityUtils.toString(response.getEntity(), "utf-8");
            returnJson = JSONObject.fromObject(json2);
            logger.info("已经请求完成。。。。。。。返回结果为：{}", returnJson);
        } catch (IOException e) {
            logger.error("请求失败。。。。。。。StringEntity:{}", entity);
            e.printStackTrace();
        }
        return returnJson;
    }

    /**
     * 向指定URL发送POST方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求Map参数，请求参数应该是 {"name1":"value1","name2":"value2"}的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static JSONObject sendPost(String url, String param) {
        //请求地址
        HttpPost httpPost = new HttpPost(url);


        //设置请求的header
        httpPost.addHeader("Content-Type", "application/json;charset=utf-8");

        //设置参数
        /*JSONObject jsonParam = new JSONObject();
        jsonParam.put("batchSMSContentBOs",batchSMSContentBOs);*/
       /* String  a="[{\"phone\":\"17373903348\",\"content\":\"测试\",\"businessId\":\"11\"},\n" +
                "        {\"phone\":\"17373903340\",\"content\":\"测试\",\"businessId\":\"11\"}]";*/
        StringEntity entity = new StringEntity(param, "utf-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);

        //创建通道
        HttpClient httpClient = HttpClientBuilder.create().build();

        // 执行请求
        JSONObject returnJson = null;
        try {
            logger.warn("正在请求中,请等待。。。。。。。StringEntity:{}", entity);
            HttpResponse response = httpClient.execute(httpPost);
            String json2 = EntityUtils.toString(response.getEntity(), "utf-8");
            returnJson = JSONObject.fromObject(json2);
            logger.info("已经请求完成。。。。。。。返回结果为：{}", returnJson);
        } catch (IOException e) {
            logger.error("请求失败。。。。。。。StringEntity:{}", entity);
            e.printStackTrace();
        }
        return returnJson;
    }

    /**
     * 向指定URL发送Get方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求Map参数，请求参数应该是 {"name1":"value1","name2":"value2"}的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static JSONObject sendGet(String url, String param) {
        //请求地址
        HttpGet httpGet = new HttpGet(url + param);


        //设置请求的header
        //httpGet.addHeader("Content-Type", "application/json;charset=utf-8");

        //设置参数
        /*JSONObject jsonParam = new JSONObject();
        jsonParam.put("batchSMSContentBOs",batchSMSContentBOs);*/
       /* String  a="[{\"phone\":\"17373903348\",\"content\":\"测试\",\"businessId\":\"11\"},\n" +
                "        {\"phone\":\"17373903340\",\"content\":\"测试\",\"businessId\":\"11\"}]";*/
        /*StringEntity entity = new StringEntity(param, "utf-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");*/

        //创建通道
        HttpClient httpClient = HttpClientBuilder.create().build();

        // 执行请求
        JSONObject returnJson = null;
        try {
            logger.warn("正在请求中,请等待。。。。。。。StringEntity:{}", param);
            HttpResponse response = httpClient.execute(httpGet);
            String json2 = EntityUtils.toString(response.getEntity(), "utf-8");
            returnJson = JSONObject.fromObject(json2);
            logger.info("已经请求完成。。。。。。。返回结果为：{}", returnJson);
        } catch (IOException e) {
            logger.error("请求失败。。。。。。。StringEntity:{}", param);
            e.printStackTrace();
        }
        return returnJson;
    }
}