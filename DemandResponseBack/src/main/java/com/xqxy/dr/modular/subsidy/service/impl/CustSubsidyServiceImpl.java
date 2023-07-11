package com.xqxy.dr.modular.subsidy.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.core.enums.YesOrNotEnum;
import com.xqxy.core.pojo.login.CurrenUserInfo;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.dr.modular.subsidy.entity.CustSubsidy;
import com.xqxy.dr.modular.subsidy.mapper.CustSubsidyMapper;
import com.xqxy.dr.modular.subsidy.model.ConsSubsidyModel;
import com.xqxy.dr.modular.subsidy.model.CustSubsidyModel;
import com.xqxy.dr.modular.subsidy.param.CustSubsidyDailyParam;
import com.xqxy.dr.modular.subsidy.param.CustSubsidyParam;
import com.xqxy.dr.modular.subsidy.service.CustSubsidyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.sys.modular.cust.enums.OrgTitleEnum;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户事件激励费用 服务实现类
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-20
 */
@Service
public class CustSubsidyServiceImpl extends ServiceImpl<CustSubsidyMapper, CustSubsidy> implements CustSubsidyService {

    @Resource
    private CustSubsidyMapper custSubsidyMapper;

    @Override
    public CustSubsidy getCustSubsidyByEventIdAndCustId(long eventId, long custId) {

        LambdaQueryWrapper<CustSubsidy> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(eventId)) {
            queryWrapper.eq(CustSubsidy::getEventId, eventId);
        }

        if (ObjectUtil.isNotNull(custId)) {
            queryWrapper.eq(CustSubsidy::getCustId, custId);
        }

        return this.getOne(queryWrapper);
    }

    @Override
    public Page<CustSubsidy> page(CustSubsidyDailyParam custSubsidyDailyParam) {

        Map<String, Object> map = new HashMap<>();
        if (ObjectUtil.isNotNull(custSubsidyDailyParam)) {
            // 客户标识
            if (ObjectUtil.isNotEmpty(custSubsidyDailyParam.getCustId())) {
                map.put("custId", custSubsidyDailyParam.getCustId());
            }
            // 事件标识字符串
            if (ObjectUtil.isNotEmpty(custSubsidyDailyParam.getEventIds())) {
                String[] split = custSubsidyDailyParam.getEventIds().split(",");
                map.put("eventIds", split);
            }
        }

        return custSubsidyMapper.custSubsidyPage(custSubsidyDailyParam.getPage(), map);
    }

    @Override
    public List<CustSubsidy> list(CustSubsidyDailyParam custSubsidyDailyParam) {
        LambdaQueryWrapper<CustSubsidy> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(custSubsidyDailyParam)) {
            // 客户标识
            if (ObjectUtil.isNotEmpty(custSubsidyDailyParam.getCustId())) {
                queryWrapper.eq(CustSubsidy::getCustId,custSubsidyDailyParam.getCustId());
            }
            // 事件标识字符串
            if (ObjectUtil.isNotEmpty(custSubsidyDailyParam.getEventIds())) {
                String[] split = custSubsidyDailyParam.getEventIds().split(",");
                queryWrapper.in(CustSubsidy::getEventId);
            }
        }
        queryWrapper.orderByDesc(CustSubsidy::getCreateTime);
        return this.list(queryWrapper);
    }

    @Override
    public Page<CustSubsidy> pageCustByEventId(CustSubsidyParam custSubsidyParam) {

        CurrenUserInfo currentUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if (currentUserInfo != null) {
            String orgTitle = currentUserInfo.getOrgTitle();
            if (orgTitle != null && orgTitle.equals(OrgTitleEnum.PROVINCE.getCode())) {
                Map<String, Object> map = new HashMap<>();
                if (ObjectUtil.isNotNull(custSubsidyParam.getEventId())) {
                    map.put("eventId", custSubsidyParam.getEventId());
                }

                if (ObjectUtil.isNotNull(custSubsidyParam.getCreditCode())) {
                    map.put("creditCode", custSubsidyParam.getCreditCode());
                }

                if (ObjectUtil.isNotNull(custSubsidyParam.getLegalName())) {
                    map.put("legalName", custSubsidyParam.getLegalName());
                }

                if (ObjectUtil.isNotNull(custSubsidyParam.getIntegrator())) {
                    map.put("integrator", custSubsidyParam.getIntegrator());
                }

                if (ObjectUtil.isNotNull(custSubsidyParam.getIsEffective())) {
                    map.put("isEffective", custSubsidyParam.getIsEffective());
                }

                Map<String, String> sortKeyMap = new HashMap<>();
                sortKeyMap.put("confirmCap", "confirm_cap");
                sortKeyMap.put("actualCap", "actual_cap");
                sortKeyMap.put("subsidyAmount", "subsidy_amount");
                sortKeyMap.put("settledAmount", "settled_amount");
                String sortColumn = sortKeyMap.getOrDefault(custSubsidyParam.getSortColumn(), "create_time");
                List<OrderItem> orderItems = new ArrayList<>();
                if ("asc".equals(custSubsidyParam.getOrder())) {
                    orderItems.add(OrderItem.asc(sortColumn));
                } else {
                    orderItems.add(OrderItem.desc(sortColumn));
                }
                Page page = custSubsidyParam.getPage();
                page.setOrders(orderItems);
                return custSubsidyMapper.custSubsidyByEventIdPage(page, map);
            }
        }

        return new Page<>();
    }


    /**
     * 导出 根据事件标识进行客户事件补贴分页查询
     * @param custSubsidyParam
     * @return
     */
    @Override
    public List<CustSubsidyModel> exportCustByEventId(CustSubsidyParam custSubsidyParam) {


        CurrenUserInfo currentUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if (currentUserInfo != null) {
            String orgTitle = currentUserInfo.getOrgTitle();
            if (orgTitle != null && orgTitle.equals(OrgTitleEnum.PROVINCE.getCode())) {
                Map<String, Object> map = new HashMap<>();
                if (ObjectUtil.isNotNull(custSubsidyParam.getEventId())) {
                    map.put("eventId", custSubsidyParam.getEventId());
                }

                if (ObjectUtil.isNotNull(custSubsidyParam.getCreditCode())) {
                    map.put("creditCode", custSubsidyParam.getCreditCode());
                }

                if (ObjectUtil.isNotNull(custSubsidyParam.getLegalName())) {
                    map.put("legalName", custSubsidyParam.getLegalName());
                }

                if (ObjectUtil.isNotNull(custSubsidyParam.getIntegrator())) {
                    map.put("integrator", custSubsidyParam.getIntegrator());
                }

                if (ObjectUtil.isNotNull(custSubsidyParam.getIsEffective())) {
                    map.put("isEffective", custSubsidyParam.getIsEffective());
                }

                List<CustSubsidyModel> modelList = custSubsidyMapper.exportCustByEventId(map);
                if(!CollectionUtils.isEmpty(modelList)){
                    for (CustSubsidyModel custSubsidyModel : modelList) {
                        if(YesOrNotEnum.Y.getCode().equals(custSubsidyModel.getIsEffective())){
                            custSubsidyModel.setIsEffective(YesOrNotEnum.Y.getMessage());
                        }else {
                            custSubsidyModel.setIsEffective(YesOrNotEnum.N.getMessage());
                        }

                    }
                }
                return modelList;
            }
        }

        return new ArrayList<>();
    }
}
