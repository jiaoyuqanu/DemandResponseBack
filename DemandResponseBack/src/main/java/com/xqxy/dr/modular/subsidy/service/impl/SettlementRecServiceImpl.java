package com.xqxy.dr.modular.subsidy.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.pojo.login.CurrenUserInfo;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.dr.modular.subsidy.entity.*;
import com.xqxy.dr.modular.subsidy.enums.PayStateEnum;
import com.xqxy.dr.modular.subsidy.enums.SubsidyExceptionEnum;
import com.xqxy.dr.modular.subsidy.mapper.ConsSubsidyDailyMapper;
import com.xqxy.dr.modular.subsidy.mapper.CustSubsidyDailyMapper;
import com.xqxy.dr.modular.subsidy.mapper.SettlementRecMapper;
import com.xqxy.dr.modular.subsidy.param.ConsSubsidyDailyParam;
import com.xqxy.dr.modular.subsidy.param.CustSubsidyDailyParam;
import com.xqxy.dr.modular.subsidy.param.SettlementRecParam;
import com.xqxy.dr.modular.subsidy.service.ConsSubsidyPayService;
import com.xqxy.dr.modular.subsidy.service.CustSubsidyPayService;
import com.xqxy.dr.modular.subsidy.service.SettlementRecService;
import com.xqxy.dr.modular.subsidy.util.DictUtil;
import com.xqxy.sys.modular.cust.service.CustService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 响应补贴结算记录表 服务实现类
 * </p>
 *
 * @author hu xingxing
 * @since 2022-01-07
 */
@Service
public class SettlementRecServiceImpl extends ServiceImpl<SettlementRecMapper, SettlementRec> implements SettlementRecService {

    @Resource
    private SettlementRecMapper settlementRecMapper;

    @Resource
    private ConsSubsidyDailyMapper consSubsidyDailyMapper;

    @Resource
    private CustSubsidyDailyMapper custSubsidyDailyMapper;

    @Resource
    private ConsSubsidyPayService consSubsidyPayService;

    @Resource
    private CustSubsidyPayService custSubsidyPayService;

    @Resource
    private CustService custService;

    @Override
    public Page<SettlementRec> settlementPage(SettlementRecParam settlementRecParam) {

        Map<String, Object> map = new HashMap<>();
        if (ObjectUtils.isNotNull(settlementRecParam)) {
            if (ObjectUtils.isNotEmpty(settlementRecParam.getYear())) {
                map.put("year", settlementRecParam.getYear());
            }

            if (ObjectUtils.isNotEmpty(settlementRecParam.getState())) {
                map.put("state", settlementRecParam.getState());
            }
        }

        return settlementRecMapper.settlementRecPage(settlementRecParam.getPage(), map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void createSettlementRec(SettlementRecParam settlementRecParam) {
        LambdaQueryWrapper<SettlementRec> queryWrapper = new LambdaQueryWrapper<>();
        String settlementId = DictUtil.getSettlementId();
        LocalDate beginDate = settlementRecParam.getBeginDate();
        LocalDate endDate = settlementRecParam.getEndDate();
        Long projectId = settlementRecParam.getProjectId();
        if(beginDate==null || endDate==null || projectId==null) {
            throw new ServiceException(SubsidyExceptionEnum.NO_PARAM_REC);
        }
        //String settlementWeek = DictUtil.getSettlementWeek(beginDate, endDate);
        //批次号为项目编号
        String settlementWeek = projectId.toString();
        queryWrapper.eq(SettlementRec::getSettlementId, settlementId);
        List<SettlementRec> settlementIdList = this.list(queryWrapper);
        if (settlementIdList != null && settlementIdList.size() > 0) {
            throw new ServiceException(SubsidyExceptionEnum.SETTLEMENT_ID_EXIST);
        }

        List<SettlementRec> settlementWeekList = settlementRecMapper.isRepeatSettlementRecWeek(settlementRecParam);
        if (settlementWeekList != null && settlementWeekList.size() > 0) {
            throw new ServiceException(SubsidyExceptionEnum.SETTLEMENT_WEEK_REPEAT);
        }

        ConsSubsidyDailyParam consSubsidyDailyParam = new ConsSubsidyDailyParam();
        consSubsidyDailyParam.setSubsidyStartDate(beginDate);
        consSubsidyDailyParam.setSubsidyEndDate(endDate);
        consSubsidyDailyParam.setSettlementNo(settlementId);
        consSubsidyDailyParam.setProjectId(projectId);
        ConsSubsidyDaily consSubsidyDaily = consSubsidyDailyMapper.consCountAmount(consSubsidyDailyParam);

        CustSubsidyDailyParam custSubsidyDailyParam = new CustSubsidyDailyParam();
        custSubsidyDailyParam.setSubsidyStartDate(beginDate);
        custSubsidyDailyParam.setSubsidyEndDate(endDate);
        custSubsidyDailyParam.setSettlementNo(settlementId);
        custSubsidyDailyParam.setProjectId(projectId);
        CustSubsidyDaily custSubsidyDaily = custSubsidyDailyMapper.custCountAmount(custSubsidyDailyParam);

        BigDecimal subsidyAmount = BigDecimal.ZERO;
        if (ObjectUtils.isNotNull(consSubsidyDaily) && ObjectUtils.isNotEmpty(consSubsidyDaily.getSettledAmount())) {
            subsidyAmount = NumberUtil.add(subsidyAmount, consSubsidyDaily.getSettledAmount());
        }

        if (ObjectUtils.isNotNull(custSubsidyDaily) && ObjectUtils.isNotEmpty(custSubsidyDaily.getSettledAmount())) {
            subsidyAmount = NumberUtil.add(subsidyAmount, custSubsidyDaily.getSettledAmount());
        }

        SettlementRec settleMentRec = new SettlementRec();
        settleMentRec.setSettlementId(settlementId);
        settleMentRec.setBeginDate(beginDate);
        settleMentRec.setEndDate(endDate);
        settleMentRec.setProjectId(projectId);
        settleMentRec.setSubsidyAmount(subsidyAmount);
        settleMentRec.setSettlementDesc(settlementWeek);
        settleMentRec.setState(PayStateEnum.WAIT_PAY.getCode());

        boolean flag = this.save(settleMentRec);
        if (flag) {
            consSubsidyDailyMapper.setConsSubsidyDailySettlementNo(consSubsidyDailyParam);
            custSubsidyDailyMapper.setCustSubsidyDailySettlementNo(custSubsidyDailyParam);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void settlementRecPay(SettlementRecParam settlementRecParam) {

        LambdaQueryWrapper<SettlementRec> queryWrapper = new LambdaQueryWrapper<>();
        String settlementId = settlementRecParam.getSettlementId();
        queryWrapper.eq(SettlementRec::getSettlementId, settlementId);

        SettlementRec settlementRec = this.getOne(queryWrapper);
        if (ObjectUtils.isNull(settlementRec)) {
            throw new ServiceException(SubsidyExceptionEnum.NO_SETTLEMENT_REC);
        }

        LocalDate beginDate = settlementRec.getBeginDate();
        LocalDate endDate = settlementRec.getEndDate();
        //Long projectId = settlementRecParam.getProjectId();

        if(beginDate==null || endDate==null) {
            throw new ServiceException(SubsidyExceptionEnum.NO_PARAM_REC);
        }

        List<ConsSubsidyPay> consSubsidyPayList = consSubsidyPayService.consSubsidyPayByPayNo(settlementId);
        List<CustSubsidyPay> custSubsidyPayList = custSubsidyPayService.custSubsidyPayByPayNo(settlementId);

        if ((consSubsidyPayList != null && consSubsidyPayList.size() > 0) || (custSubsidyPayList != null && custSubsidyPayList.size() > 0)) {
            throw new ServiceException(SubsidyExceptionEnum.PAY_RECORD_REPEAT);
        }

        ConsSubsidyDailyParam consSubsidyDailyParam = new ConsSubsidyDailyParam();
        consSubsidyDailyParam.setSettlementNo(settlementId);
        List<ConsSubsidyDaily> consSubsidyDailies = consSubsidyDailyMapper.getConsAmountBySettlementNo(consSubsidyDailyParam);

        CustSubsidyDailyParam custSubsidyDailyParam = new CustSubsidyDailyParam();
        custSubsidyDailyParam.setSettlementNo(settlementId);
        List<CustSubsidyDaily> custSubsidyDailies = custSubsidyDailyMapper.getCustAmountBySettlementNo(custSubsidyDailyParam);

        List<ConsSubsidyPay> consSubsidyPays = new ArrayList<>();
        List<CustSubsidyPay> custSubsidyPays = new ArrayList<>();

        boolean consFlag = false;
        boolean custFlag = false;
        if(!CollectionUtils.isEmpty(consSubsidyDailies)){
            for (ConsSubsidyDaily consSubsidyDaily : consSubsidyDailies) {
                ConsSubsidyPay consSubsidyPay = new ConsSubsidyPay();
                consSubsidyPay.setConsId(consSubsidyDaily.getConsId());
                consSubsidyPay.setSubsidyAmount(consSubsidyDaily.getSettledAmount());
                consSubsidyPay.setPayNo(consSubsidyDaily.getSettlementNo());
                consSubsidyPay.setBeginDate(beginDate);
                consSubsidyPay.setEndDate(endDate);
                consSubsidyPay.setParticNum(consSubsidyDaily.getEventNum());
                consSubsidyPay.setPayStatus("2");
                consSubsidyPays.add(consSubsidyPay);
            }
            consFlag = consSubsidyPayService.saveBatch(consSubsidyPays);
        }
        if(!CollectionUtils.isEmpty(custSubsidyDailies)){
            for (CustSubsidyDaily custSubsidyDaily : custSubsidyDailies) {
                CustSubsidyPay custSubsidyPay = new CustSubsidyPay();
                custSubsidyPay.setCustId(custSubsidyDaily.getCustId());
                custSubsidyPay.setIntegrator(custSubsidyDaily.getIntegrator());
                custSubsidyPay.setSubsidyAmount(custSubsidyDaily.getSettledAmount());
                custSubsidyPay.setPayNo(custSubsidyDaily.getSettlementNo());
                custSubsidyPay.setBeginDate(beginDate);
                custSubsidyPay.setEndDate(endDate);
                custSubsidyPay.setParticNum(custSubsidyDaily.getEventNum());
                custSubsidyPay.setPayStatus("2");
                custSubsidyPays.add(custSubsidyPay);
            }
            custFlag = custSubsidyPayService.saveBatch(custSubsidyPays);
        }
        if (consFlag && custFlag) {
            settlementRec.setState(PayStateEnum.PAY_COMPLETE.getCode());
            this.updateById(settlementRec);
        } else {
            throw new ServiceException(SubsidyExceptionEnum.NO_SETTLEMENT_DALLIY);
        }
    }

    @Override
    public Page<SettlementRec> getCustSettlementRecPage(SettlementRecParam settlementRecParam) {
        Map<String, Object> map = new HashMap<>();
        CurrenUserInfo currenUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if (currenUserInfo != null) {
            String custUserId = currenUserInfo.getId();
            map.put("custId", custUserId);
        }else {
            return new Page<>();
        }
        if (ObjectUtils.isNotNull(settlementRecParam)) {
            if (ObjectUtils.isNotEmpty(settlementRecParam.getYear())) {
                map.put("year", settlementRecParam.getYear());
            }

            if (ObjectUtils.isNotEmpty(settlementRecParam.getState())) {
                map.put("state", settlementRecParam.getState());
            }
        }
        return settlementRecMapper.custSettlementRecPage(settlementRecParam.getPage(), map);
    }
}
