package com.xqxy.dr.modular.newloadmanagement.service.impl;

import cn.hutool.core.lang.Dict;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.client.SystemClient;
import com.xqxy.core.client.SystemClientService;
import com.xqxy.core.pojo.base.param.BaseParam;
import com.xqxy.core.pojo.login.CurrenUserInfo;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.device.entity.SysOrgs;
import com.xqxy.dr.modular.event.entity.ConsInvitation;
import com.xqxy.dr.modular.event.entity.Event;
import com.xqxy.dr.modular.event.enums.RangeTypeEnum;
import com.xqxy.dr.modular.event.mapper.ConsInvitationMapper;
import com.xqxy.dr.modular.event.param.ConsInvitationParam;
import com.xqxy.dr.modular.event.service.ConsInvitationService;
import com.xqxy.dr.modular.event.service.EventService;
import com.xqxy.dr.modular.newloadmanagement.entity.Drevent;
import com.xqxy.dr.modular.newloadmanagement.mapper.CitiesInvitedMapper;
import com.xqxy.dr.modular.newloadmanagement.mapper.DrConsMapper;
import com.xqxy.dr.modular.newloadmanagement.service.CitiesInvitedService;
import com.xqxy.dr.modular.newloadmanagement.vo.UserSignalDetailVo;
import com.xqxy.dr.modular.project.entity.ConsContractInfo;
import com.xqxy.dr.modular.project.mapper.ConsContractInfoMapper;
import com.xqxy.dr.modular.project.service.ConsContractInfoService;
import com.xqxy.dr.modular.project.service.CustContractInfoService;
import com.xqxy.sys.modular.cust.enums.OrgTitleEnum;
import com.xqxy.sys.modular.dict.param.DictTypeParam;
import com.xqxy.sys.modular.dict.service.DictTypeService;
import com.xqxy.sys.modular.utils.JSONObjectToEntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CitiesInvitedServiceImpl implements CitiesInvitedService {


    @Autowired
    private SystemClientService systemClientService;

    @Autowired
    private SystemClient systemClient;

    @Autowired
    private CitiesInvitedMapper citiesInvitedMapper;

    @Autowired
    private ConsInvitationService consInvitationService;

    @Autowired
    private ConsInvitationMapper consInvitationMapper;

    @Resource
    private EventService eventService;

    @Autowired
    private DrConsMapper drConsMapper;

    @Resource
    private DictTypeService dictTypeService;


    @Resource
    ConsContractInfoService consContractInfoService;


    @Override
    public Page queryCitiesInvited(ConsInvitationParam consInvitationParam) {
        CurrenUserInfo currenUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        List<String> orgIds = null;
        List<String> provinceOrgId = null;
        List resultList = new ArrayList<>();
        List listDatas = new ArrayList();
        String cityOrgId = null;

        List resultPage = new ArrayList<>();
        //登录用户为省级用户
        if (OrgTitleEnum.PROVINCE.getCode().equals(currenUserInfo.getOrgTitle())) {
            JSONObject result = systemClient.queryAllOrg();
            List<SysOrgs> list = new ArrayList<>();
            List<SysOrgs> listProvince = new ArrayList<>();

            if ("000000".equals(result.getString("code"))) {
                JSONArray datas = result.getJSONArray("data");
                if (null != datas && datas.size() > 0) {
                    for (Object obj : datas) {
                        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(obj);
                        SysOrgs sysOrgs = JSONObjectToEntityUtils.JSONObjectToSysOrg(jsonObject);
                        list.add(sysOrgs);
                        listProvince.add(sysOrgs);
                    }
                }
            }


            orgIds = list.stream().filter(s -> "2".equals(s.getOrgTitle())).map(SysOrgs::getId).collect(Collectors.toList());
//            provinceOrgId = list.stream().filter(s -> "1".equals(s.getOrgTitle())).map(SysOrgs::getId).collect(Collectors.toList());

//            for (String s : provinceOrgId) {
//                orgIds.add(s);
//            }

            for (String orgId : orgIds) {
                BigDecimal goal = citiesInvitedMapper.queryGoal(orgId, consInvitationParam.getEventId());


                ResponseData<List<String>> allNextOrgId = systemClientService.getAllNextOrgId(orgId);
                List<String> allNextOrgIdData = allNextOrgId.getData();
                Drevent drevent3Condition = citiesInvitedMapper.query3Condition(consInvitationParam.getEventId());


                String rangeType = citiesInvitedMapper.rangeType(consInvitationParam.getEventId());
                List<ConsContractInfo> consContractList = new ArrayList();

                Event event2 = citiesInvitedMapper.eventInfo(consInvitationParam.getEventId());


                ResponseData<List<String>> allNextOrgIds = systemClientService.getAllNextOrgId(orgId);
                List orgData = allNextOrgIds.getData();

                if (RangeTypeEnum.ADMINISTRATIVE_REGION.getCode().
                        equals(rangeType)) {
                    // 事件执行范围为行政区域时候
                    consContractList = consContractInfoService.listConsTractInfo2(event2,orgData);
                }
                if (RangeTypeEnum.ELECTRICIC_REGION.getCode().
                        equals(rangeType)) {
                    // 事件执行范围为供电单位编码的时候
                    consContractList = consContractInfoService.listConsTractInfoByOrg2(event2,orgData);
                }
                Integer userSignalCount = null;
                if(consContractList!=null && consContractList.size()>0){
                    userSignalCount = consContractList.size();
                }


//                List<Long> projectIds = citiesInvitedMapper.queryProjectId(drevent3Condition);
//                List<String> consIds = drConsMapper.consIds(allNextOrgIdData);
//                Integer userSignalCount = null;
//                if (consIds != null && consIds.size() > 0 && projectIds != null && projectIds.size() > 0) {
//                    userSignalCount = citiesInvitedMapper.userSignalCount(projectIds, consIds);
//                }


                Map map2 = new HashMap();
                map2.put("eventId", consInvitationParam.getEventId());
                map2.put("orgs", allNextOrgIdData);
                Map consCount = consInvitationMapper.getConsCount(map2);


                consCount.put("orgId", orgId);
                consCount.put("goal", goal);
                consCount.put("userSignalCount", userSignalCount);


                consInvitationParam.setOrgs(allNextOrgIdData);
                Event event = eventService.getById(consInvitationParam.getEventId());
                // 查询所有已反馈的邀约用户
                List<ConsInvitation> consInvitations = consInvitationMapper.getPartCons(consInvitationParam);
                // 2.返回结果/容量
                ConsCurve resultCurve = new ConsCurve();
                // 统计反馈容量总和和最新反馈时间
                BigDecimal feedbackCapacity = consInvitations.stream().map(ConsInvitation::getReplyCap).reduce(BigDecimal.ZERO, (d1, d2) -> {
                    return Optional.ofNullable(d1).orElse(BigDecimal.ZERO).add(Optional.ofNullable(d2).orElse(BigDecimal.ZERO));
                });
                resultCurve.setFeedbackCapacity(feedbackCapacity);

                consCount.put("resultCurve", resultCurve.getFeedbackCapacity());
                resultList.add(consCount);
            }
        } else {
            cityOrgId = currenUserInfo.getOrgId();

            BigDecimal goal = citiesInvitedMapper.queryGoal(cityOrgId, consInvitationParam.getEventId());


            ResponseData<List<String>> allNextOrgId = systemClientService.getAllNextOrgId(cityOrgId);
            List<String> allNextOrgIdData = allNextOrgId.getData();
            Drevent drevent3Condition = citiesInvitedMapper.query3Condition(consInvitationParam.getEventId());

            List<Long> projectIds = citiesInvitedMapper.queryProjectId(drevent3Condition);
            List<String> consIds = drConsMapper.consIds(allNextOrgIdData);

            Integer userSignalCount = null;
            if (consIds != null && consIds.size() > 0 && projectIds != null && projectIds.size() > 0) {
                userSignalCount = citiesInvitedMapper.userSignalCount(projectIds, consIds);
            }


            Map map2 = new HashMap();
            map2.put("eventId", consInvitationParam.getEventId());
            map2.put("orgs", allNextOrgIdData);
            Map consCount = consInvitationMapper.getConsCount(map2);


            consCount.put("orgId", cityOrgId);
            consCount.put("goal", goal);
            consCount.put("userSignalCount", userSignalCount);


            consInvitationParam.setOrgs(allNextOrgIdData);
            Event event = eventService.getById(consInvitationParam.getEventId());
            // 查询所有已反馈的邀约用户
            List<ConsInvitation> consInvitations = consInvitationMapper.getPartCons(consInvitationParam);
            // 2.返回结果/容量
            ConsCurve resultCurve = new ConsCurve();
            // 统计反馈容量总和和最新反馈时间
            BigDecimal feedbackCapacity = consInvitations.stream().map(ConsInvitation::getReplyCap).reduce(BigDecimal.ZERO, (d1, d2) -> {
                return Optional.ofNullable(d1).orElse(BigDecimal.ZERO).add(Optional.ofNullable(d2).orElse(BigDecimal.ZERO));
            });
            resultCurve.setFeedbackCapacity(feedbackCapacity);
            consCount.put("resultCurve", resultCurve.getFeedbackCapacity());


            resultList.add(consCount);

        }

        Long current = consInvitationParam.getCurrent();
        Long size = consInvitationParam.getSize();
        Page page = new Page();
        page.setCurrent(current);
        page.setSize(size);

        List pageMap = getPageMap(resultList, page);

        page.setTotal(resultList.size());
        page.setRecords(pageMap);


        return page;
    }

    @Override
    public List queryDrConsInvitation(ConsInvitationParam consInvitationParam) {
        ResponseData<List<String>> allNextOrgId = systemClientService.getAllNextOrgId(String.valueOf(consInvitationParam.getOrgId()));
        List<String> allNextOrgIdData = allNextOrgId.getData();
        Drevent drevent3Condition = citiesInvitedMapper.query3Condition(consInvitationParam.getEventId());
        List<Long> projectIds = citiesInvitedMapper.queryProjectId(drevent3Condition);
        List<String> consIds = drConsMapper.consIds(allNextOrgIdData);

        List<String> userSignal = null;
        List<UserSignalDetailVo> userSignalDetail = new ArrayList<>();
        if (consIds != null && consIds.size() > 0 && projectIds != null && projectIds.size() > 0) {
            userSignal = citiesInvitedMapper.userSignal(projectIds, consIds);
        }
        if (userSignal != null && userSignal.size() > 0) {
            userSignalDetail = citiesInvitedMapper.userSignalInfo(consInvitationParam.getEventId(), userSignal);
        }


        List<SysOrgs> provinceOrgsList = new ArrayList<>();
        List<SysOrgs> cityOrgsList = new ArrayList<>();
        List<SysOrgs> areaOrgsList = new ArrayList<>();
        List<SysOrgs> zOrgsList = new ArrayList<>();
        List<SysOrgs> listCity = new ArrayList<>();
        List<SysOrgs> listProvince = new ArrayList<>();
        List<SysOrgs> listArea = new ArrayList<>();
        List<SysOrgs> listZ = new ArrayList<>();
        JSONObject result = systemClient.queryAllOrg();
        if ("000000".equals(result.getString("code"))) {
            JSONArray datas = result.getJSONArray("data");
            if (null != datas && datas.size() > 0) {
                for (Object obj : datas) {
                    JSONObject jsonObject = (JSONObject) JSONObject.toJSON(obj);
                    SysOrgs sysOrgs = JSONObjectToEntityUtils.JSONObjectToSysOrg(jsonObject);
                    listCity.add(sysOrgs);
                    listProvince.add(sysOrgs);
                    listArea.add(sysOrgs);
                    listZ.add(sysOrgs);
                }
            }
            cityOrgsList = listCity.stream().filter(s -> "2".equals(s.getOrgTitle())).collect(Collectors.toList());
            provinceOrgsList = listProvince.stream().filter(s -> "1".equals(s.getOrgTitle())).collect(Collectors.toList());
            areaOrgsList = listArea.stream().filter(s -> "3".equals(s.getOrgTitle())).collect(Collectors.toList());
            zOrgsList = listArea.stream().filter(s -> "4".equals(s.getOrgTitle())).collect(Collectors.toList());
        }

        for (UserSignalDetailVo userSignalDetailVo : userSignalDetail) {
            for (SysOrgs sysOrgs : provinceOrgsList) {
                if (sysOrgs.getId().equals(userSignalDetailVo.getOrgNo())) {
                    userSignalDetailVo.setOrgName(sysOrgs.getName());
                }
            }
        }


        for (UserSignalDetailVo userSignalDetailVo : userSignalDetail) {
            for (SysOrgs sysOrgs : cityOrgsList) {
                if (sysOrgs.getId().equals(userSignalDetailVo.getOrgNo())) {
                    userSignalDetailVo.setOrgName(sysOrgs.getName());
                }
            }
        }

        for (UserSignalDetailVo userSignalDetailVo : userSignalDetail) {
            for (SysOrgs sysOrgs : zOrgsList) {
                if (sysOrgs.getId().equals(userSignalDetailVo.getOrgNo())) {
                    userSignalDetailVo.setOrgName(sysOrgs.getName());
                }
            }
        }


        for (UserSignalDetailVo userSignalDetailVo : userSignalDetail) {
            for (SysOrgs sysOrgs : areaOrgsList) {
                if (sysOrgs.getId().equals(userSignalDetailVo.getOrgNo())) {
                    userSignalDetailVo.setOrgName(sysOrgs.getName());
                }
            }
        }


        for (UserSignalDetailVo userSignalDetailVo : userSignalDetail) {
            if (getProvinceCode().equals(userSignalDetailVo.getOrgNo())) {
                String name = provinceOrgsList.get(0).getName();
                userSignalDetailVo.setProvinceOrgName(name);
            } else {
                for (SysOrgs sysOrgs : zOrgsList) {
                    if (sysOrgs.getId().equals(userSignalDetailVo.getOrgNo())) {
                        for (SysOrgs sysOrgs2 : areaOrgsList) {
                            if (sysOrgs2.getId().equals(sysOrgs.getParentId())) {
                                userSignalDetailVo.setAreaOrgName(sysOrgs2.getName());
                                for (SysOrgs sysOrgs3 : cityOrgsList) {
                                    if (sysOrgs3.getId().equals(sysOrgs2.getParentId())) {
                                        userSignalDetailVo.setCityOrgName(sysOrgs3.getName());
                                    }
                                }
                            }
                        }
                    }
                }


                for (SysOrgs sysOrgs : areaOrgsList) {
                    if (userSignalDetailVo.getOrgNo().equals(sysOrgs.getId())) {
                        userSignalDetailVo.setAreaOrgName(sysOrgs.getName());
                        for (SysOrgs sysOrgs2 : cityOrgsList) {
                            if (sysOrgs2.getId().equals(sysOrgs.getParentId())) {
                                userSignalDetailVo.setCityOrgName(sysOrgs2.getName());
                            }
                        }
                    }
                }

                for (SysOrgs sysOrgs : cityOrgsList) {
                    if (sysOrgs.getId().equals(userSignalDetailVo.getOrgNo())) {
                        userSignalDetailVo.setCityOrgName(sysOrgs.getName());
                    }
                }
            }
        }

        return userSignalDetail;
    }


    private String getProvinceCode() {
        String provinceCode = null;
        DictTypeParam dictTypeParam3 = new DictTypeParam();
        dictTypeParam3.setCode("province_code");
        List<Dict> list3 = dictTypeService.dropDown(dictTypeParam3);
        Object value2 = null;
        if (null != list3 && list3.size() > 0) {
            for (Dict dict : list3) {
                if ("anhui_org_code".equals(dict.get("code"))) {
                    value2 = dict.get("value");
                }
            }
            if (null != value2) {
                provinceCode = (String) value2;
            }
        } else {
            provinceCode = "34101";
        }
        return provinceCode;
    }


    public static List<Map<String, Object>> getPageMap(List<Map<String, Object>> transDetecMongos, Page page) {
        List<Map<String, Object>> pageList = new ArrayList();
        int current = (int) page.getCurrent();
        int rows = (int) page.getSize();
        int dataRows = transDetecMongos.size();
        if (dataRows == 0) {
            return pageList;
        }
        int totalPages = (dataRows % rows == 0) ? (dataRows / rows) : (dataRows / rows + 1); //总页数

        int pageStartRows;//起始行数
        int pageEndRows;//结束行数

        if (current * rows < dataRows) {
            pageEndRows = current * rows;
            pageStartRows = pageEndRows - rows;
        } else {
            pageEndRows = dataRows;
            pageStartRows = rows * (totalPages - 1);
        }
        for (int i = pageStartRows; i < pageEndRows; i++) {
            pageList.add(transDetecMongos.get(i));
        }
        return pageList;
    }

}