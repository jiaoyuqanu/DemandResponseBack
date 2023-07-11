package com.xqxy.dr.modular.upload.jonhandler;


import com.xqxy.dr.modular.subsidy.entity.ConsSubsidyPay;
import com.xqxy.dr.modular.upload.entity.ContractInfo;
import com.xqxy.dr.modular.upload.entity.Drcons;
import com.xqxy.dr.modular.upload.entity.Event;
import com.xqxy.dr.modular.upload.mapper.EvluateMapper;
import com.xqxy.dr.modular.upload.mapper.IncidentMapper;
import com.xqxy.dr.modular.upload.mapper.SubsidyPayMapper;
import com.xqxy.dr.modular.upload.mapper.UserMapper;
import com.xqxy.dr.modular.upload.service.EvluateService;
import com.xqxy.dr.modular.upload.service.IncidentService;
import com.xqxy.dr.modular.upload.service.SubsidyPayService;
import com.xqxy.dr.modular.upload.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class FinalUploadStrategy implements UploadStrategy {

    @Resource
    private SubsidyPayService payService;

    @Resource
    private UserService userService;

    @Resource
    private IncidentService incidentService;

    @Resource
    private EvluateService evluateService;

    @Override
    public void generateConSubsidy() {
        List<ConsSubsidyPay> subsidyPays = payService.getConSubsidy();
        for (ConsSubsidyPay subsidyPay : subsidyPays) {
            String consId = subsidyPay.getConsId();//用户标识
            BigDecimal subsidyAmount = subsidyPay.getSubsidyAmount();//支付金额
            long payId = subsidyPay.getPayId();//支付标识
            //String createTime = subsidyPay.getCreateTime();//支付年份
        }
    }

    @Override
    public void generateUser() {
        List<Drcons> drcons = userService.getUser();
        for (Drcons drcon : drcons) {
            String id = drcon.getId();//所属聚合商
            String cityCode = drcon.getCityCode();//市码
            String countyCode = drcon.getCountyCode();//县码
            long custId = drcon.getCustId();//用户标识;
            String bigTradeName = drcon.getBigTradeName();//用户名称
            String bidTradeCode = drcon.getBidTradeCode();//用户标识
            BigDecimal contractCap = drcon.getContractCap();//合同容量
            BigDecimal dayMaxPower = drcon.getDayMaxPower();//最大需量
            BigDecimal consDesPower = drcon.getConsDesPower();//可响应容量
            String createTime = drcon.getCreateTime();//注册时间
            BigDecimal runCap = drcon.getRunCap();//供电容量
            String CREDIT_CODE = drcon.getCREDIT_CODE();//税号
            String voltCode = drcon.getVoltCode();//电压等级
        }
 
    }

    @Override
    public void generateAbility() {
        List<Drcons> drconsList = userService.getAbility();
        for (Drcons drcons : drconsList) {
            String id = drcons.getId();//用户标识
            long contractId = drcons.getContractId();//申报标识
            String createTime = drcons.getCreateTime();//申报年份;
            long planId = drcons.getPlanId();//合同附件标识
            BigDecimal demandCap = drcons.getDemandCap();//约定响应能力
        }
    }

    @Override
    public void generateIncident() {
        List<ContractInfo> contractInfos = incidentService.getIncident();
        for (ContractInfo contractInfo : contractInfos) {
            String updateTime = contractInfo.getUpdateTime();//审核时间
            long updateUser = contractInfo.getUpdateUser();//审核人编号
            String startTime = contractInfo.getStartTime();//响应开始时间
            BigDecimal contractCap = contractInfo.getContractCap();
            long drDays = 1;//响应执行天数
            String endTime = contractInfo.getEndTime();
            String createTime = contractInfo.getCreateTime();
            long contractId = contractInfo.getContractId();
            long projectDetailId = contractInfo.getProjectDetailId();
            String responseType = contractInfo.getResponseType();
            String timeType = contractInfo.getTimeType();
            String reviewcode = "通过";
            String checkStatus = contractInfo.getCheckStatus();

        }
    }

    @Override
    public void generateEvluate() {
        List<Event> events = evluateService.getEvluate();
        for (Event event : events) {
            BigDecimal actualcosts = event.getContractPrice().add(event.getSubsidyAmount());//实际支出成本
            BigDecimal actualCap = event.getActualCap();//实际响应容量
            BigDecimal invitationCap = event.getInvitationCap();//聚合商用户数量
            long eventId = event.getEventId();//需求响应事件标识
            long evaluationId = event.getEvaluationId();//评价标识
        }

    }
}
