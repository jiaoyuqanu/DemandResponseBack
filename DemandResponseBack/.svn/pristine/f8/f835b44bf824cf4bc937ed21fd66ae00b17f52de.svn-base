package com.xqxy.core.client;

import com.alibaba.fastjson.JSONObject;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.project.result.Region;
import com.xqxy.sys.modular.cust.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author: zgy
 * Date: 2022/4/17 15:19
 * Content:
 */
@Service
public class SystemClientService {
    @Autowired
    private SystemClient systemClient;

    private static List<Region> allRegion = new ArrayList<>();
    private static long getRegionTime = System.currentTimeMillis();

    public List<Region> queryAll(){
        long aaa = System.currentTimeMillis();
        if((aaa-getRegionTime)>7200000){
            //超过一小时重新获取
            getRegionTime = System.currentTimeMillis();
            allRegion = systemClient.queryAll();
        }
        if(allRegion.isEmpty()){
            allRegion = systemClient.queryAll();
        }
        return allRegion;
    }


    private static Result result = null;
    private static long getResultTime = System.currentTimeMillis();
    public Result getAllOrgs(){
        if((System.currentTimeMillis()-getResultTime)>7200000){
            //超过一小时重新获取
            getResultTime = System.currentTimeMillis();
            result=systemClient.getAllOrgs();
        }
        if(result==null){
            result=systemClient.getAllOrgs();
        }
        return result;
    }

    private static JSONObject jsonObject = null;
    private static long getJsTime = System.currentTimeMillis();
    public JSONObject queryAllOrg(){
        if((System.currentTimeMillis()-getJsTime)>7200000){
            //超过一小时重新获取
            getJsTime = System.currentTimeMillis();
            jsonObject = systemClient.queryAllOrg();
        }
        if(jsonObject==null){
            jsonObject = systemClient.queryAllOrg();
        }
        return jsonObject;
    }


    private static ConcurrentHashMap<String,ResponseData<String>> norgsMap = new ConcurrentHashMap<>();
    private static long getnorgsMapTime = System.currentTimeMillis();
    public ResponseData<String> getNextOrgs(String id){
        if((System.currentTimeMillis()-getnorgsMapTime)>7200000){
            //超过一小时重新获取
            getnorgsMapTime = System.currentTimeMillis();
            ResponseData<String> res = systemClient.getNextOrgs(id);
            norgsMap.clear();
            norgsMap.put(id,res);
        }
        if(norgsMap.containsKey(id)){
            return norgsMap.get(id);
        }else{
            getnorgsMapTime = System.currentTimeMillis();
            ResponseData<String> res = systemClient.getNextOrgs(id);
            norgsMap.put(id,res);
            return res;
        }
    }


    private static ConcurrentHashMap<String,ResponseData<List<String>>> allnorgsMap = new ConcurrentHashMap<>();
    private static long getallnorgsMapTime = System.currentTimeMillis();
    public ResponseData<List<String>> getAllNextOrgId(String orgId){
        if((System.currentTimeMillis()-getallnorgsMapTime)>7200000){
            //超过一小时重新获取
            getallnorgsMapTime = System.currentTimeMillis();
            ResponseData<List<String>> res = systemClient.getAllNextOrgId(orgId);
            allnorgsMap.clear();
            allnorgsMap.put(orgId,res);
        }
        if(allnorgsMap.containsKey(orgId)){
            return allnorgsMap.get(orgId);
        }else{
            getallnorgsMapTime = System.currentTimeMillis();
            ResponseData<List<String>> res = systemClient.getAllNextOrgId(orgId);
            allnorgsMap.put(orgId,res);
            return res;
        }
    }

    private static ConcurrentHashMap<String,JSONObject> orgEntityMap = new ConcurrentHashMap<>();
    private static long getorgEntityMapTime = System.currentTimeMillis();
    public JSONObject parent(String id){
        if((System.currentTimeMillis()-getorgEntityMapTime)>7200000){
            //超过一小时重新获取
            getorgEntityMapTime = System.currentTimeMillis();
            JSONObject res = systemClient.parent(id);
            orgEntityMap.clear();
            orgEntityMap.put(id,res);
        }
        if(orgEntityMap.containsKey(id)){
            return orgEntityMap.get(id);
        }else{
            getorgEntityMapTime = System.currentTimeMillis();
            JSONObject res = systemClient.parent(id);
            orgEntityMap.put(id,res);
            return res;
        }
    }
}
