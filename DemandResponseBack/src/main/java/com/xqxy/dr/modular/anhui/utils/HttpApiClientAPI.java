//
//  Created by  fred on 2017/1/12.
//  Copyright © 2016年 Alibaba. All rights reserved.
//

package com.xqxy.dr.modular.anhui.utils;

import cn.hutool.log.Log;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class HttpApiClientAPI {
/*
public class HttpApiClientAPI extends ApacheHttpClient{
    private static final Log log = Log.get();


    public final static String HOST = "725b83c34ece4985bab67cab7df16df9.apigateway.res.sgmc.sgcc.com.cn";

    static HttpApiClientAPI instance = new HttpApiClientAPI();

    public static HttpApiClientAPI getInstance(){
        return instance;
    }

    public void init(HttpClientBuilderParams httpClientBuilderParams){

        httpClientBuilderParams.setScheme(Scheme.HTTP);

        httpClientBuilderParams.setHost(HOST);

        super.init(httpClientBuilderParams);

    }


    */
/**
     * 初始化链接
     *//*

    @PostConstruct
    private void initHttp(){
        // bf7f28d080214b448629ace7d7105217.apigateway.res.sgmc.sgcc.com.cn 192.168.8.74:20090
        HttpClientBuilderParams httpParam = new HttpClientBuilderParams();
        httpParam.setScheme(Scheme.HTTP);
        httpParam.setHost(HOST);
        httpParam.setAppKey("1660039420735541");
        httpParam.setAppSecret("2e4fd2ca6dc34c5091460d0fbf9d315");
        super.init(httpParam);
    }

    */
/**
     * POST_智慧能源综合服务平台_负荷查询_历史负荷数据_ads_cst_zhny_history_curve_查询
     * @param pageNum
     * @param pageSize
     * @param consNo
     * @param dataTime
     * @param callback
     *//*

    public void queryAdsCstZhnyHistoryCurve(String pageNum , String pageSize , String consNo , String dataTime , ApiCallback callback) {
        String path = "/cst/zhny/rds/post_zhnyQueryConsHistoryLoadData_htlx";
        ApiRequest request = new ApiRequest(HttpMethod.POST_FORM , path);
        request.addParam("pageNum" , pageNum , ParamPosition.QUERY , false);
        request.addParam("pageSize" , pageSize , ParamPosition.QUERY , false);
        request.addParam("consNo" , consNo , ParamPosition.QUERY , true);
        request.addParam("dataTime" , dataTime , ParamPosition.QUERY , false);
        sendAsyncRequest(request , callback);
    }


    public ApiResponse queryAdsCstZhnyHistoryCurve(String pageNum , String pageSize , String consNo , String dataTime) {
        String path = "/cst/zhny/rds/post_zhnyQueryConsHistoryLoadData_htlx";
        ApiRequest request = new ApiRequest(HttpMethod.POST_FORM , path);
        request.addParam("pageNum" , pageNum , ParamPosition.QUERY , false);
        request.addParam("pageSize" , pageSize , ParamPosition.QUERY , false);
        request.addParam("consNo" , consNo , ParamPosition.QUERY , true);
        request.addParam("dataTime" , dataTime , ParamPosition.QUERY , false);
        return sendSyncRequest(request);
    }

    */
/**
     * POST_智慧能源综合服务平台_营销用户档案查询_电力用户信息表
     * @param pageNum
     * @param pageSize
     * @param consNo
     * @return
     *//*

    public ApiResponse query_ads_cst_zhny_dr_cons_Mode(String pageNum , String pageSize , String consNo) {
        String path = "/cst/zhny/rds/post_zhnyQueryConsData_htlx";
        ApiRequest request = new ApiRequest(HttpMethod.POST_FORM , path);
        request.addParam("pageNum" , pageNum , ParamPosition.QUERY , false);
        request.addParam("pageSize" , pageSize , ParamPosition.QUERY , false);
        request.addParam("consNo" , consNo , ParamPosition.QUERY , false);
        log.info("参数consNo："+ consNo);
        return sendSyncRequest(request);
    }
*/

}