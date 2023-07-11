package com.xqxy.dr.modular.project.utlis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xqxy.core.client.SystemClient;
import com.xqxy.dr.modular.dispatch.entity.Dispatch;
import com.xqxy.dr.modular.dispatch.param.DispatchParam;
import com.xqxy.dr.modular.project.result.Region;
import com.xqxy.sys.modular.cust.result.Result;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PageUtils {

    /**
     * 手动分页类
     * @param datas
     * @param pageSize
     * @param pageNum
     * @param <T>
     * @return
     */
    public static <T> TableInfo getPageSizeDataForRelations(List<T> datas, Integer pageSize, Integer pageNum){
        if(pageNum == null){
            pageNum = 1;
        }
        if(pageSize == null){
            pageSize = 10;
        }

        TableInfo tableInfo = new TableInfo();
        tableInfo.setPageNum(pageNum);
        tableInfo.setPageSize(pageSize);

        int total = datas.size();
        int pages;
        if(total % pageSize >0){
            pages = total / pageSize +1;
        }else {
            pages = total / pageSize;
        }

        tableInfo.setTotal(total);
        tableInfo.setPages(pages);
        List<T> res = new ArrayList<>();

        int startNum = (pageNum-1)* pageSize+1 ;                     //起始截取数据位置
        if(startNum > datas.size()){
            tableInfo.setRecords(res);
            return tableInfo;
        }

        int rum = datas.size() - startNum;
        if(rum < 0){
            tableInfo.setRecords(res);
            return tableInfo;
        }
        if(rum == 0){                                               //说明正好是最后一个了
            int index = datas.size() -1;
            res.add(datas.get(index));
            tableInfo.setRecords(res);
            return tableInfo;
        }
        if(rum / pageSize >= 1){                                    //剩下的数据还够1页，返回整页的数据
            for(int i=startNum;i<startNum + pageSize;i++){          //截取从startNum开始的数据
                res.add(datas.get(i-1));
            }
            tableInfo.setRecords(res);
            return tableInfo;
        }else if((rum / pageSize == 0) && rum > 0){                 //不够一页，直接返回剩下数据
            for(int j = startNum ;j<=datas.size();j++){
                res.add(datas.get(j-1));
            }
            tableInfo.setRecords(res);
            return tableInfo;
        }else{
            return null;
        }
    }

}
