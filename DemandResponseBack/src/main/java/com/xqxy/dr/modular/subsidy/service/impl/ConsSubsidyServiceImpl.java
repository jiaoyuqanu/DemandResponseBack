package com.xqxy.dr.modular.subsidy.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.client.SystemClient;
import com.xqxy.core.enums.YesOrNotEnum;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.pojo.login.CurrenUserInfo;
import com.xqxy.core.util.OrganizationUtil;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.dr.modular.subsidy.entity.ConsSubsidy;
import com.xqxy.dr.modular.subsidy.mapper.ConsSubsidyMapper;
import com.xqxy.dr.modular.subsidy.model.ConsSubsidyModel;
import com.xqxy.dr.modular.subsidy.param.ConsSubsidyDailyParam;
import com.xqxy.dr.modular.subsidy.param.ConsSubsidyParam;
import com.xqxy.dr.modular.subsidy.result.ConsNoSubsidy;
import com.xqxy.dr.modular.subsidy.result.DailySubsidyInfo;
import com.xqxy.dr.modular.subsidy.service.ConsSubsidyService;
import com.xqxy.sys.modular.cust.entity.Cust;
import com.xqxy.sys.modular.cust.entity.UserConsRela;
import com.xqxy.sys.modular.cust.enums.ConsExceptionEnum;
import com.xqxy.sys.modular.cust.service.ConsService;
import com.xqxy.sys.modular.cust.service.CustService;
import com.xqxy.sys.modular.utils.CityAndCountyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户事件激励费用 服务实现类
 * </p>
 *
 * @author hu xingxing
 * @since 2021-10-20
 */
@Service
public class ConsSubsidyServiceImpl extends ServiceImpl<ConsSubsidyMapper, ConsSubsidy> implements ConsSubsidyService {

    @Resource
    private ConsSubsidyMapper consSubsidyMapper;

    @Resource
    private CustService custService;

    @Resource
    private ConsService consService;


    @Resource
    private SystemClient systemClient;

    @Resource
    private CityAndCountyUtils cityAndCountyUtils;

    @Override
    public ConsSubsidy getConsSubsidyByEventIdAndConsId(long eventId, String consId) {

        LambdaQueryWrapper<ConsSubsidy> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(eventId)) {
            queryWrapper.eq(ConsSubsidy::getEventId, eventId);
        }

        if (ObjectUtil.isNotNull(consId)) {
            queryWrapper.eq(ConsSubsidy::getConsId, consId);
        }

        return this.getOne(queryWrapper);
    }

    @Override
    public Page<ConsSubsidy> page(ConsSubsidyDailyParam consSubsidyDailyParam) {

        Map<String, Object> map = new HashMap<>();
        if (ObjectUtil.isNotNull(consSubsidyDailyParam)) {
            // 用户标识
            if (ObjectUtil.isNotEmpty(consSubsidyDailyParam.getConsId())) {
                map.put("consId", consSubsidyDailyParam.getConsId());
            }
            // 事件标识字符串
            if (ObjectUtil.isNotEmpty(consSubsidyDailyParam.getEventIds())) {
                String[] split = consSubsidyDailyParam.getEventIds().split(",");
                map.put("eventIds", split);
            }
        }

        return consSubsidyMapper.consSubsidyPage(consSubsidyDailyParam.getPage(), map);
    }

    @Override
    public Page<DailySubsidyInfo> dailySubsidyConsDetail(ConsSubsidyDailyParam consSubsidyDailyParam) {
        Map<String, Object> map = new HashMap<>();
        if (ObjectUtil.isNotNull(consSubsidyDailyParam)) {
            // 用户标识
            if (ObjectUtil.isNotEmpty(consSubsidyDailyParam.getConsId())) {
                map.put("consId", consSubsidyDailyParam.getConsId());
            }
            // 事件标识字符串
            if (ObjectUtil.isNotEmpty(consSubsidyDailyParam.getSubsidyDate())) {
                map.put("subsidyDate", consSubsidyDailyParam.getSubsidyDate());
            }
        }
        return consSubsidyMapper.dailySubsidyConsDetail(consSubsidyDailyParam.getPage(), map);
    }

    @Override
    public Page<DailySubsidyInfo> dailySubsidyCustDetail(ConsSubsidyDailyParam consSubsidyDailyParam) {
        Map<String, Object> map = new HashMap<>();
        if (ObjectUtil.isNotNull(consSubsidyDailyParam)) {
            // 用户标识
            if (ObjectUtil.isNotEmpty(consSubsidyDailyParam.getCustId())) {
                map.put("custId", consSubsidyDailyParam.getCustId());
            }
            // 事件标识字符串
            if (ObjectUtil.isNotEmpty(consSubsidyDailyParam.getSubsidyDate())) {
                map.put("subsidyDate", consSubsidyDailyParam.getSubsidyDate());
            }
        }
        return consSubsidyMapper.dailySubsidyCustDetail(consSubsidyDailyParam.getPage(), map);
    }

    @Override
    public BigDecimal dailySubsidyConsTotal(ConsSubsidyDailyParam consSubsidyDailyParam) {
        Map<String, Object> map = new HashMap<>();
        if (ObjectUtil.isNotNull(consSubsidyDailyParam)) {
            // 用户标识
            if (ObjectUtil.isNotEmpty(consSubsidyDailyParam.getConsId())) {
                map.put("consId", consSubsidyDailyParam.getConsId());
            }
            // 事件标识字符串
            if (ObjectUtil.isNotEmpty(consSubsidyDailyParam.getSubsidyDate())) {
                map.put("subsidyDate", consSubsidyDailyParam.getSubsidyDate());
            }
        }
        BigDecimal total = consSubsidyMapper.dailySubsidyConsTotal(map);
        return total;
    }

    @Override
    public BigDecimal dailySubsidyCustTotal(ConsSubsidyDailyParam consSubsidyDailyParam) {
        Map<String, Object> map = new HashMap<>();
        if (ObjectUtil.isNotNull(consSubsidyDailyParam)) {
            // 用户标识
            if (ObjectUtil.isNotEmpty(consSubsidyDailyParam.getCustId())) {
                map.put("custId", consSubsidyDailyParam.getCustId());
            }
            // 事件标识字符串
            if (ObjectUtil.isNotEmpty(consSubsidyDailyParam.getSubsidyDate())) {
                map.put("subsidyDate", consSubsidyDailyParam.getSubsidyDate());
            }
        }
        BigDecimal total = consSubsidyMapper.dailySubsidyCustTotal(map);
        return total;
    }

    @Override
    public List<ConsSubsidy> list(ConsSubsidyDailyParam consSubsidyDailyParam) {
        LambdaQueryWrapper<ConsSubsidy> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(consSubsidyDailyParam)) {
            // 用户标识
            if (ObjectUtil.isNotEmpty(consSubsidyDailyParam.getConsId())) {
                queryWrapper.eq(ConsSubsidy::getConsId, consSubsidyDailyParam.getConsId());
            }
            // 事件标识字符串
            if (ObjectUtil.isNotEmpty(consSubsidyDailyParam.getEventIds())) {
                String[] split = consSubsidyDailyParam.getEventIds().split(",");
                queryWrapper.in(ConsSubsidy::getEventId, split);
            }
            // 用户标识集合
            if (ObjectUtil.isNotEmpty(consSubsidyDailyParam.getConsIdList())) {
                queryWrapper.in(ConsSubsidy::getConsId, consSubsidyDailyParam.getConsIdList());
            }
        }
        queryWrapper.orderByDesc(ConsSubsidy::getCreateTime);
        return this.list(queryWrapper);
    }

    @Override
    public Page<ConsSubsidy> pageConsByEventId(ConsSubsidyParam consSubsidyParam) {
        //数据权限
        CurrenUserInfo currenUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if (null == currenUserInfo) {
            return new Page<>();
        }
        Map<String, Object> map = new HashMap<>();
        String orgNo = currenUserInfo.getOrgId();
        String orgTitle = currenUserInfo.getOrgTitle();
        //机构子集
        List<String> list = new ArrayList<>();
        List<String> list1 = null;
        List<String> list2 = null;
        if (null != orgNo && !"".equals(orgNo)) {
            if (!"1".equals(orgTitle)) {
                //获取用户权限内所有组织机构集合
                list1 = OrganizationUtil.getAllOrgByOrgNo();
                if (null == list1 || list1.size() == 0) {
                    return new Page<>();
                }
                //根据参数查询其所有子集
                if (null != consSubsidyParam.getOrgNo()) {
                    list2 = OrganizationUtil.getAllOrgByOrgNoPamarm(consSubsidyParam.getOrgNo());
                    //筛选用户机构集合中包含参数的机构
                    if (null != list1 && null != list2) {
                        for (String s : list2) {
                            if (list1.contains(s)) {
                                list.add(s);
                            }
                        }
                    }
                    if (CollectionUtil.isEmpty(list)) {
                        return new Page<>();
                    }
                } else {
                    list = list1;
                }
            } else {
                if (null != consSubsidyParam.getOrgNo()) {
                    list = OrganizationUtil.getAllOrgByOrgNoPamarm(consSubsidyParam.getOrgNo());
                }
            }
        } else {
            return new Page<>();
        }
        // 机构等级
        if (ObjectUtil.isNotEmpty(list)) {
            map.put("orgIds", list);
        }

        if (ObjectUtil.isNotNull(consSubsidyParam.getConsId())) {
            map.put("consId", consSubsidyParam.getConsId());
        }

        if (ObjectUtil.isNotNull(consSubsidyParam.getConsName())) {
            map.put("consName", consSubsidyParam.getConsName());
        }

        if (ObjectUtil.isNotNull(consSubsidyParam.getEventId())) {
            map.put("eventId", consSubsidyParam.getEventId());
        }

        if (ObjectUtil.isNotNull(consSubsidyParam.getIsEffective())) {
            map.put("isEffective", consSubsidyParam.getIsEffective());
        }
        if (ObjectUtil.isNotNull(orgTitle) && "1".equals(orgTitle)) {
            map.put("joinUserType", "1");
        }
        Map<String, String> sortKeyMap = new HashMap<>();
        sortKeyMap.put("actualCap", "actual_cap");
        sortKeyMap.put("subsidyAmount", "subsidy_amount");
        sortKeyMap.put("settledAmount", "settled_amount");
        String sortColumn = sortKeyMap.getOrDefault(consSubsidyParam.getSortColumn(), "create_time");
        List<OrderItem> orderItems = new ArrayList<>();
        if ("asc".equals(consSubsidyParam.getOrder())) {
            orderItems.add(OrderItem.asc(sortColumn));
        } else {
            orderItems.add(OrderItem.desc(sortColumn));
        }
        Page page = consSubsidyParam.getPage();
        page.setOrders(orderItems);
        return consSubsidyMapper.consSubsidyByEventIdPage(page, map);
    }

    @Override
    public Page<ConsSubsidy> pageConsByEventIdAndCustId(ConsSubsidyParam consSubsidyParam) {
        //数据权限
        CurrenUserInfo currenUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if (null == currenUserInfo) {
            return new Page<>();
        }
        String orgNo = currenUserInfo.getOrgId();
        String orgTitle = currenUserInfo.getOrgTitle();
        //机构子集
        List<String> list = new ArrayList<>();
        List<String> list1 = null;
        List<String> list2 = null;
        if (null != orgNo && !"".equals(orgNo)) {
            if (!"1".equals(orgTitle)) {
                //获取用户权限内所有组织机构集合
                list1 = OrganizationUtil.getAllOrgByOrgNo();
                if (null == list1 || list1.size() == 0) {
                    return new Page<>();
                }
                //根据参数查询其所有子集
                if (null != consSubsidyParam.getOrgNo()) {
                    list2 = OrganizationUtil.getAllOrgByOrgNoPamarm(consSubsidyParam.getOrgNo());
                    //筛选用户机构集合中包含参数的机构
                    if (null != list1 && null != list2) {
                        for (String s : list2) {
                            if (list1.contains(s)) {
                                list.add(s);
                            }
                        }
                    }
                    if (CollectionUtil.isEmpty(list)) {
                        return new Page<>();
                    }
                } else {
                    list = list1;
                }
            } else {
                if (null != consSubsidyParam.getOrgNo()) {
                    list = OrganizationUtil.getAllOrgByOrgNoPamarm(consSubsidyParam.getOrgNo());
                }
            }
        } else {
            return new Page<>();
        }
        Map<String, Object> map = new HashMap<>();
        List<String> consIds = null;
        List<UserConsRela> userConsRelies = null;
        if (ObjectUtil.isNotNull(consSubsidyParam.getCustId())) {
            userConsRelies = consSubsidyMapper.userConsRelies(consSubsidyParam.getCustId());
        }
        // 机构等级
        if (ObjectUtil.isNotEmpty(list)) {
            map.put("orgIds", list);
        }
        if (userConsRelies != null) {
            consIds = userConsRelies.stream().map(UserConsRela::getConsNo).collect(Collectors.toList());
            map.put("consIds", consIds);
        }

        if (ObjectUtil.isNotNull(consSubsidyParam.getEventId())) {
            map.put("eventId", consSubsidyParam.getEventId());
        }

        return consSubsidyMapper.consSubsidyByEventIdAndConsIdPage(consSubsidyParam.getPage(), map);
    }

    @Override
    public Page<ConsNoSubsidy> consNoSubsidy(ConsSubsidyParam consSubsidyParam) {

        CurrenUserInfo currentUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        Map<String, Object> map = new HashMap<>();
        String custId = null;

        if (currentUserInfo != null) {
            custId = currentUserInfo.getId();
        }
        if (custId == null || "".equals(custId)) {
            throw new ServiceException(ConsExceptionEnum.CUST_NOT_EXIST);
        }

        Cust cust = custService.getById(custId);
        Integer integrator = cust.getIntegrator();
        map.put("consId", consSubsidyParam.getConsId());
        map.put("eventId", consSubsidyParam.getEventId());
        map.put("startTime", consSubsidyParam.getStartTime());
        map.put("endTime", consSubsidyParam.getEndTime());

        List<String> consIds = consService.getConsIdByCust();
        if (consIds != null) {
            map.put("consIds", consIds);
            return consSubsidyMapper.consNoSubsidy(consSubsidyParam.getPage(), map);
        }

        return new Page<>();
    }

    @Override
    public Object consNoSubsidyInfo(ConsSubsidyParam consSubsidyParam) {
        CurrenUserInfo currentUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        Map<String, Object> map = new HashMap<>();
        String custId = null;

        if (currentUserInfo != null) {
            custId = currentUserInfo.getId();
        }
        if (custId == null || "".equals(custId)) {
            throw new ServiceException(ConsExceptionEnum.CUST_NOT_EXIST);
        }

        Cust cust = custService.getById(custId);
        Integer integrator = cust.getIntegrator();
        map.put("consId", consSubsidyParam.getConsId());
        map.put("eventId", consSubsidyParam.getEventId());
        map.put("startTime", consSubsidyParam.getStartTime());
        map.put("endTime", consSubsidyParam.getEndTime());
        return consSubsidyMapper.consNoSubsidyInfo(map);
    }


    /**
     * 导出 -- 根据事件标识进行用户事件补贴分页查询
     *
     * @param consSubsidyParam
     * @return
     * @author lqr
     * @date 2022-6-18 11:00
     */
    @Override
    public List<ConsSubsidyModel> exportConsByEventId(ConsSubsidyParam consSubsidyParam) {
        //数据权限
        CurrenUserInfo currenUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if (null == currenUserInfo) {
            return new ArrayList<>();
        }
        Map<String, Object> map = new HashMap<>();
        String orgNo = currenUserInfo.getOrgId();
        String orgTitle = currenUserInfo.getOrgTitle();
        //机构子集
        List<String> list = new ArrayList<>();
        List<String> list1 = null;
        List<String> list2 = null;
        if (null != orgNo && !"".equals(orgNo)) {
            if (!"1".equals(orgTitle)) {
                //获取用户权限内所有组织机构集合
                list1 = OrganizationUtil.getAllOrgByOrgNo();
                if (null == list1 || list1.size() == 0) {
                    return new ArrayList<>();
                }
                //根据参数查询其所有子集
                if (null != consSubsidyParam.getOrgNo()) {
                    list2 = OrganizationUtil.getAllOrgByOrgNoPamarm(consSubsidyParam.getOrgNo());
                    //筛选用户机构集合中包含参数的机构
                    if (null != list1 && null != list2) {
                        for (String s : list2) {
                            if (list1.contains(s)) {
                                list.add(s);
                            }
                        }
                    }
                    if (CollectionUtil.isEmpty(list)) {
                        return new ArrayList<>();
                    }
                } else {
                    list = list1;
                }
            } else {
                if (null != consSubsidyParam.getOrgNo()) {
                    list = OrganizationUtil.getAllOrgByOrgNoPamarm(consSubsidyParam.getOrgNo());
                }
            }
        } else {
            return new ArrayList<>();
        }
        // 机构等级
        if (ObjectUtil.isNotEmpty(list)) {
            map.put("orgIds", list);
        }

        if (ObjectUtil.isNotNull(consSubsidyParam.getConsId())) {
            map.put("consId", consSubsidyParam.getConsId());
        }

        if (ObjectUtil.isNotNull(consSubsidyParam.getConsName())) {
            map.put("consName", consSubsidyParam.getConsName());
        }

        if (ObjectUtil.isNotNull(consSubsidyParam.getEventId())) {
            map.put("eventId", consSubsidyParam.getEventId());
        }

        if (ObjectUtil.isNotNull(consSubsidyParam.getIsEffective())) {
            map.put("isEffective", consSubsidyParam.getIsEffective());
        }
        if (ObjectUtil.isNotNull(orgTitle) && "1".equals(orgTitle)) {
            map.put("joinUserType", "1");
        }

        JSONObject jsonObject = systemClient.queryAllOrg();
        JSONArray data = jsonObject.getJSONArray("data");

        List<ConsSubsidyModel> modelList = consSubsidyMapper.exportConsByEventId(map);
        if (!CollectionUtils.isEmpty(modelList)) {
            for (ConsSubsidyModel consSubsidyModel : modelList) {
                String province = "";
                String city = "";
                String county = "";
                String place = "";
                try {
                    if (ObjectUtil.isNotEmpty(consSubsidyModel.getOrgNo())) {
                        Map<String, Object> cityAndCountyMap = cityAndCountyUtils.cityAndCounty(consSubsidyModel.getOrgNo(), jsonObject);
                        province = (String) cityAndCountyMap.get("province");
                        city = (String) cityAndCountyMap.get("city");
                        county = (String) cityAndCountyMap.get("county");
                        place = (String) cityAndCountyMap.get("place");
                    }
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
                JSONArray jsonArray = data.stream().filter(item -> ((Map<String, Object>) item).get("id").equals(consSubsidyModel.getOrgNo())).collect(Collectors.toCollection(JSONArray::new));
                if (ObjectUtil.isNotEmpty(jsonArray) && jsonArray.size() > 0) {
                    Map<String, Object> o = (Map<String, Object>) jsonArray.get(0);
                    consSubsidyModel.setOrgName((String) o.get("name"));
                }

                consSubsidyModel.setCityOrgName(city);
                consSubsidyModel.setCountyOrgName(county);

                if (YesOrNotEnum.Y.getCode().equals(consSubsidyModel.getIsEffective())) {
                    consSubsidyModel.setIsEffective(YesOrNotEnum.Y.getMessage());
                } else {
                    consSubsidyModel.setIsEffective(YesOrNotEnum.N.getMessage());
                }

            }
        }
        return modelList;
    }

}
