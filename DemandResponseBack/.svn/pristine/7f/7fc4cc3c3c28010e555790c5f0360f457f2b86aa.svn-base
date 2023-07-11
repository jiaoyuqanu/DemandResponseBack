package com.xqxy.executor.service.jobhandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xqxy.core.client.GrsgOrder;
import com.xqxy.core.client.SyncOrderStatusClient;
import com.xqxy.core.util.DateUtil;
import com.xqxy.dr.modular.grsg.entity.DrApplyRec;
import com.xqxy.dr.modular.grsg.service.DrApplyRecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: zgy
 * Date: 2021/11/30 11:38
 * Content: 申请工单进度推送服务，该功能只在外网部署才起作用，配置不开启
 */
@Service
public class OrderProPushJob {
    //工单进度是否推送，默认0不推送。只有外网服务才推送
    @Value("${order_push: 0}")
    private int orderPush;

    @Autowired
    private DrApplyRecService drApplyRecService;

    @Autowired
    private SyncOrderStatusClient syncOrderStatusClient;

    @Scheduled(initialDelay=6000,fixedDelay = 300000)//每5分钟执行一次
    public void OrderProPush() {
        if(orderPush==0){
            return;
        }

        QueryWrapper<DrApplyRec> qw = new QueryWrapper<>();
        qw.eq("state", "2").or().eq("state", "3");

        List<DrApplyRec> sendRecs = drApplyRecService.list(qw);
        int i = 0;
        for(DrApplyRec drApplyRec : sendRecs){
            i ++;
            if(i>30){
                //防止五分钟发不完
                break;
            }
            GrsgOrder grsgOrder = new GrsgOrder();
            grsgOrder.setOrderNum(drApplyRec.getOrderNum());
            grsgOrder.setAccessDate(DateUtil.getCurrentDate());

            if("2".equals(drApplyRec.getState())){
                grsgOrder.setOrderResult("PASS");
                grsgOrder.setStepCode("FINISH");
                grsgOrder.setOrderResultDesc("业务开通完成");
            }else if("3".equals(drApplyRec.getState())){
                grsgOrder.setOrderResult("FAIL");
                grsgOrder.setStepCode("START");
                grsgOrder.setOrderResultDesc("拒绝受理");
            }

            try{
                String result = syncOrderStatusClient.syncOrderStatus(grsgOrder);
                if(result==null || result.length()==0){
                    drApplyRec.setState("5");//推送失败
                }else{
                    JSONObject resObj = JSON.parseObject(result);
                    if(resObj.containsKey("state") && "true".equals(resObj.getString("state"))){
                        drApplyRec.setState("4");
                    }else{
                        drApplyRec.setState("5");//推送失败
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                drApplyRec.setState("5");//推送失败
            }finally{
                drApplyRecService.updateById(drApplyRec);
            }


            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
