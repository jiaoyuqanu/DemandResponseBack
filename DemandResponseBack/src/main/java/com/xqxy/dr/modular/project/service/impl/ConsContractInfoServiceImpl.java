package com.xqxy.dr.modular.project.service.impl;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.client.SystemClient;
import com.xqxy.core.client.SystemClientService;
import com.xqxy.core.client.SystemLoadbusiClient;
import com.xqxy.core.enums.ApprovalCodeEnum;
import com.xqxy.core.enums.BusTypeEnum;
import com.xqxy.core.enums.CurrenUserInfoExceptionEnum;
import com.xqxy.core.enums.DrSysDictDataEnum;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import com.xqxy.core.pojo.login.CurrenUserInfo;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.core.util.OrganizationUtil;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.dr.modular.device.entity.DeviceAdjustableBase;
import com.xqxy.dr.modular.device.service.DeviceAdjustableBaseService;
import com.xqxy.dr.modular.event.entity.Event;
import com.xqxy.dr.modular.event.param.ConsInvitationParam;
import com.xqxy.dr.modular.event.utils.OrgUtils;
import com.xqxy.dr.modular.project.VO.DrConsContractDetailsVO;
import com.xqxy.dr.modular.project.entity.*;
import com.xqxy.dr.modular.project.enums.*;
import com.xqxy.dr.modular.project.mapper.ConsContractDetailMapper;
import com.xqxy.dr.modular.project.mapper.ConsContractInfoMapper;
import com.xqxy.dr.modular.project.params.*;
import com.xqxy.dr.modular.project.service.*;
import com.xqxy.dr.modular.workbench.VO.ContractDetailVO;
import com.xqxy.sys.modular.cust.entity.Cons;
import com.xqxy.sys.modular.cust.entity.Cust;
import com.xqxy.sys.modular.cust.entity.UserConsRela;
import com.xqxy.sys.modular.cust.enums.IsAggregatorEnum;
import com.xqxy.sys.modular.cust.param.BusConfigParam;
import com.xqxy.sys.modular.cust.result.Result;
import com.xqxy.sys.modular.cust.service.ConsService;
import com.xqxy.sys.modular.cust.service.CustService;
import com.xqxy.sys.modular.cust.service.UserConsRelaService;
import com.xqxy.sys.modular.dict.param.DictTypeParam;
import com.xqxy.sys.modular.dict.service.DictTypeService;
import com.xqxy.sys.modular.sms.enums.SmsTemplateTypeEnum;
import com.xqxy.sys.modular.sms.service.SysSmsSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户项目申报基本信息 服务实现类
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-13
 */
@Service
@Slf4j
public class ConsContractInfoServiceImpl extends ServiceImpl<ConsContractInfoMapper, ConsContractInfo> implements ConsContractInfoService {
    @Resource
    private CustService custService;
    @Resource
    private ConsService consService;
    @Resource
    private CustContractInfoService custContractInfoService;
    @Resource
    private ProjectDetailService projectDetailService;
    @Resource
    private CustContractDetailService custContractDetailService;
    @Resource
    private ConsContractDetailService consContractDetailService;
    @Resource
    private ConsContractInfoMapper consContractInfoMapper;
    @Resource
    private ConsContractDetailMapper consContractDetailMapper;
    @Resource
    private SystemClient systemClient;
    @Resource
    private SystemClientService systemClientService;
    @Resource
    private DictTypeService dictTypeService;
    @Resource
    private UserConsRelaService userConsRelaService;
    @Resource
    private SysSmsSendService smsSendService;
    @Resource
    private SpareContractDeviceService spareContractDeviceService;
    @Resource
    private DeviceAdjustableBaseService deviceAdjustableBaseService;
    @Resource
    private ProjectService projectService;

    @Resource
    private CustContractFileService custContractFileService;

    // 全局
    private List<String> listPart = new ArrayList<>();

    @Override
    public List<ConsContractInfo> listByProjectId(Long projectId) {
        LambdaQueryWrapper<ConsContractInfo> queryWrapper = new LambdaQueryWrapper<>();
       /* if (ObjectUtil.isNotNull(eventParam)) {
            // 根据事件编号模糊查询
            if (ObjectUtil.isNotEmpty(eventParam.getEventNo())) {
                queryWrapper.like(Event::getEventNo, eventParam.getEventNo());
            }
            // 根据事件名称模糊查询
            if (ObjectUtil.isNotEmpty(eventParam.getEventName())) {
                queryWrapper.like(Event::getEventName, eventParam.getEventName());
            }

        }*/

        // 根据排序升序排列，序号越小越在前
        queryWrapper.orderByAsc(ConsContractInfo::getCreateTime);
        return this.list(queryWrapper);
    }

    @Override
    public List<ConsContractInfo> list(ConsInvitationParam consInvitationParam) {
        LambdaQueryWrapper<ConsContractInfo> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(consInvitationParam)) {
            // 根据事件编号模糊查询
            if (CollectionUtil.isNotEmpty(consInvitationParam.getConsIdList())) {
                queryWrapper.in(ConsContractInfo::getConsId, consInvitationParam.getConsIdList());
            }

        }

        // 根据排序升序排列，序号越小越在前
        queryWrapper.orderByAsc(ConsContractInfo::getCreateTime);
        return this.list(queryWrapper);
    }

    @Override
    public Page<ConsContractInfo> listConsContract(CustContractParam custContractParam) {
        Long custId = Long.valueOf(Objects.requireNonNull(SecurityUtils.getCurrentUserInfoUTF8()).getId());
        Cust cust = custService.getById(custId);
        Cust aggregator = custService.selectAggregator();
        Page<ConsContractInfo> consContractInfos;
        if (ObjectUtil.isNotEmpty(custContractParam.getConsId())) {
            String consId = custContractParam.getConsId().toString();
            Cons consInfo = consService.getById(consId);
            LambdaQueryWrapper<ConsContractInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(ConsContractInfo::getConsId, consId);
            lambdaQueryWrapper.eq(ObjectUtil.isNotNull(custContractParam.getProjectId()), ConsContractInfo::getProjectId, custContractParam.getProjectId());
            lambdaQueryWrapper.orderByDesc(BaseEntity::getCreateTime);
            consContractInfos = getBaseMapper().selectPage(custContractParam.getPage(), lambdaQueryWrapper);
            consContractInfos.getRecords().forEach(item -> {
                item.setConsName(consInfo.getConsName());
                item.setOrgName(consInfo.getOrgName());
                item.setRunCap(consInfo.getRunCap().toString());
                item.setContractCap(consInfo.getContractCap());
                item.setState(consInfo.getState());
                item.setFirstContactName(consInfo.getFirstContactName());
                item.setFirstContactInfo(consInfo.getFirstContactInfo());
                item.setSecondContactInifo(consInfo.getSecondContactInifo());
                item.setSecondContactName(consInfo.getSecondContactName());
            });
        } else if (IsAggregatorEnum.AGGREGATOR.getCode().equals(cust.getIntegrator())) {
            consContractInfos = getBaseMapper().listConsContractAgg(custContractParam.getPage(), custContractParam.getProjectId(), custId);
        } else if (ObjectUtil.isEmpty(aggregator)) {
            consContractInfos = getBaseMapper().listConsContract(custContractParam.getPage(), custContractParam.getProjectId(), custId);
        } else {
            consContractInfos = getBaseMapper().listConsContractProxy(custContractParam.getPage(), custContractParam.getProjectId(), custId, aggregator.getId());
        }

        if (consContractInfos.getRecords() != null && consContractInfos.getRecords().size() > 0) {
            List<Long> projectIds = consContractInfos.getRecords().stream().map(ConsContractInfo::getProjectId).collect(Collectors.toList());
            LambdaQueryWrapper<Project> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.in(Project::getProjectId, projectIds);
            List<Project> list = projectService.list(lambdaQueryWrapper);
            Map<Long, Project> projectMap = list.stream().collect(Collectors.toMap(Project::getProjectId, Function.identity()));
            consContractInfos.getRecords().forEach(item -> item.setProject(projectMap.get(item.getProjectId())));
        }
        return consContractInfos;
    }

    @Override
    public List<ConsContractInfo> listConsTractInfo(Event event) {
        JSONArray jsonArray = null;
        List<String> provinceList = new ArrayList<>();
        List<String> cityList = new ArrayList<>();
        List<String> countyList = new ArrayList<>();
        jsonArray = JSONArray.parseArray(event.getRegulateRange());
        if (null != jsonArray && jsonArray.size() > 0) {
            for (int j = 0; j < jsonArray.size(); j++) {
                JSONArray value = jsonArray.getJSONArray(j);
                List<String> list = new ArrayList<>();
                if (null != value && value.size() > 0) {
                    for (int i = 0; i < value.size(); i++) {
                        if (i == 0) {
                            provinceList.add(value.get(i).toString());
                        } else if (i == 1) {
                            cityList.add(value.get(i).toString());
                        } else {
                            countyList.add(value.get(i).toString());
                        }

                    }
                }
            }
        }
        return getBaseMapper().listConsTractInfoReigon(event.getProjectId(), event.getAdvanceNoticeTime(), event.getResponseType(), event.getTimeType(), provinceList, cityList, countyList);
    }

    @Override
    public List<ConsContractInfo> listConsTractInfo2(Event event, List orglist) {
        return getBaseMapper().listConsTractInfoReigon(event.getProjectId(), event.getAdvanceNoticeTime(), event.getResponseType(), event.getTimeType(), null, orglist, null);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void submitSigning(ConsContractParam consContractParam) {
        CurrenUserInfo currentUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if (ObjectUtil.isNull(currentUserInfo)) {
            throw new ServiceException(CurrenUserInfoExceptionEnum.CURRENT_USER_INFO_NOT_EXIST);
        }
        BusConfigParam busConfigParam = new BusConfigParam();
        busConfigParam.setBusId(consContractParam.getContractId());
        // busConfigParam.setApplyOrgId(Objects.requireNonNull(currentUserInfo).getOrgId());
        Cust cust = custService.getById(currentUserInfo.getId());
        if (cust == null) {
            busConfigParam.setApplyOrgId(Objects.requireNonNull(currentUserInfo).getOrgId());
        } else {
            // 不同客户审核单位不同
            if (cust.getOrgNo() == null || cust.getOrgNo() == "") {
                busConfigParam.setApplyOrgId(Objects.requireNonNull(currentUserInfo).getOrgId());
            } else {
                busConfigParam.setApplyOrgId(cust.getOrgNo());
            }
        }

        busConfigParam.setBusType(BusTypeEnum.SIGNING_PROCESS.getCode());
        busConfigParam.setApplyManId(Long.parseLong(currentUserInfo.getId()));
        busConfigParam.setApplyManName(cust.getApplyName());
        busConfigParam.setLevel(1);
        busConfigParam.setOperaManId(currentUserInfo.getId());
        Result result = null;
        try {
            result = systemClient.selectInfo(busConfigParam);
        } catch (Exception e) {
            log.error("该用户未找到审批流程");
            throw new ServiceException(500, "该用户未找到审批流程");
        }
        // assert result != null;
        JSONObject data = null;
        if (null != result) {
            data = result.getData();
        } else {
            log.error("签约审核接口返回为空，调用参数为{}，结果详情：{}", busConfigParam, result);
            throw new ServiceException(ExamineExceptionEnum.RESULT_FAIL);
        }
        if (result.getCode().equals(ApprovalCodeEnum.SUCCESS.getCode())) {
            if (data.getString("handleCode").equals(ApprovalCodeEnum.BUILD_SUCCESS.getCode())) {
                CustContractInfo custContractInfo = custContractInfoService.getById(consContractParam.getContractId());
                custContractInfo.setCheckStatus(DeclareProjectCheckEnums.VERIFYING.getCode());
                Cust aggregator = custService.selectAggregator();
                List<ConsContractInfo> consContractInfos;
                if (!ObjectUtil.isEmpty(aggregator)) {
                    consContractInfos = listConsByCust(currentUserInfo.getId(), custContractInfo.getProjectId()
                            .toString());
                } else {
                    consContractInfos = listConsByCust(custContractInfo.getCustId()
                            .toString(), custContractInfo.getProjectId().toString());
                }
                // 用户签约审核状态： 未提交 -> 审核中
                consContractInfos.forEach(consContractInfo -> {
                    if (DeclareProjectCheckEnums.UNSUBMIT.getCode()
                            .equals(consContractInfo.getCheckStatus()) || DeclareProjectCheckEnums.UNVERIFIED.getCode()
                            .equals(consContractInfo.getCheckStatus())) {
                        consContractInfo.setCheckStatus(DeclareProjectCheckEnums.VERIFYING.getCode());
                    }
                });
                updateBatchById(consContractInfos);
                custContractInfoService.updateById(custContractInfo);
            } else {
                // 创建失败，抛出异常
                throw new ServiceException(ExamineExceptionEnum.CREATION_FAILED);
            }
        }
    }

    @Override
    public List<ConsContractInfo> listConsTractInfoByOrg(Event event) {
        // 获取所有组织机构集合
        JSONObject result = systemClientService.queryAllOrg();
        JSONArray data = null;
        if (null != result) {
            data = result.getJSONArray("data");
        } else {
            throw new ServiceException(PlanExceptionEnum.NO_ORG_INFO);
        }
        JSONArray jsonArray = null;
        List<String> list = new ArrayList<>();
        jsonArray = JSONArray.parseArray(event.getRegulateRange());
        if (null != jsonArray && jsonArray.size() > 0) {
            for (int j = 0; j < jsonArray.size(); j++) {
                JSONArray value = jsonArray.getJSONArray(j);
                if (null != value && value.size() > 0) {
                    // 获取前端传的最后一级组织机构编码
                    String code = (String) value.get(value.size() - 1);
                    if (null != data && data.size() > 0) {
                        OrgUtils orgUtils = new OrgUtils();
                        List<String> part = orgUtils.getData(data, code, listPart);
                        // 将子集code合并至一个list
                        list.addAll(part);
                        list.add(code);
                        listPart = new ArrayList<>();
                    }
                }
            }
        }
        return getBaseMapper().listConsTractInfoOrg(event.getProjectId(), event.getAdvanceNoticeTime(), event.getResponseType(), event.getTimeType(), list);
        // return getBaseMapper().listConsTractInfoByOrg(projectId, startTime, endTime, regulateRange);
    }

    @Override
    public List<ConsContractInfo> listConsTractInfoByOrg2(Event event, List orgList) {
        return getBaseMapper().listConsTractInfoOrg(event.getProjectId(), event.getAdvanceNoticeTime(), event.getResponseType(), event.getTimeType(), orgList);
    }

    /**
     * @description: 查询符合条件的 用户签约详情
     * @param:
     * @return:
     * @author: liqirui
     * @date: 2021/11/02 16:54
     */
    @Override
    public List<DrConsContractDetailsVO> queryDrConsContractDetails(Page<DrConsContractDetailsVO> page, ConsContractParam consContractParam) {
        // List<Region> result = systemClient.queryAll();
        // Map<String, String> map = result.stream().collect(Collectors.toMap(Region::getId, Region::getName));
        // 数据权限
        CurrenUserInfo currenUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if (null == currenUserInfo) {
            return new ArrayList<>();
        }
        String orgNo = currenUserInfo.getOrgId();
        String orgTitle = currenUserInfo.getOrgTitle();
        // 机构子集
        List<String> list = new ArrayList<>();
        List<String> list1 = null;
        List<String> list2 = null;
        if (null != orgNo && !"".equals(orgNo)) {
            if (!"1".equals(orgTitle)) {
                // 获取用户权限内所有组织机构集合
                list1 = OrganizationUtil.getAllOrgByOrgNo();
                if (null == list1 || list1.size() == 0) {
                    return new ArrayList<>();
                }
                // 根据参数查询其所有子集
                if (null != consContractParam.getOrgId()) {
                    list2 = OrganizationUtil.getAllOrgByOrgNoPamarm(consContractParam.getOrgId());
                    // 筛选用户机构集合中包含参数的机构
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
                if (null != consContractParam.getOrgId()) {
                    list = OrganizationUtil.getAllOrgByOrgNoPamarm(consContractParam.getOrgId());
                }
            }
        } else {
            return new ArrayList<>();
        }
        consContractParam.setOrgIds(list);
        List<DrConsContractDetailsVO> listdata = consContractInfoMapper.queryDrConsContractDetails(page, consContractParam);

        // 请求字典接口
       /* DictTypeParam dictTypeParam = new DictTypeParam();
        dictTypeParam.setCode(DrSysDictDataEnum.CHECK_STATUS.getCode());

        List<Dict> dicts = dictTypeService.dropDown(dictTypeParam);

        for (DrConsContractDetailsVO cons : list) {
            dicts.forEach(n -> {
                if (n.get("code").toString().equals(cons.getCheckStatus())) {
                    cons.setCheckStatusDesc(n.get("value").toString());
                }
            });

            String cityCodeDesc = map.get(cons.getCityCode()) == null ? cons.getCityCode() : map.get(cons.getCityCode());
            String countyCodeDesc = map.get(cons.getCountyCode()) == null ? cons.getCountyCode() : map.get(cons.getCountyCode());
            cons.setCityCodeDesc(cityCodeDesc);
            cons.setCountyCodeDesc(countyCodeDesc);
        }*/
        return listdata;
    }


    /**
     * 查询用户签约详情导出
     *
     * @param consContractParam
     * @return
     */
    @Override
    public List<DrConsContractDetailsVO> exportDrConsDetails(ConsContractParam consContractParam) {
        // 数据权限
        CurrenUserInfo currenUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if (null == currenUserInfo) {
            return new ArrayList<>();
        }
        String orgNo = currenUserInfo.getOrgId();
        String orgTitle = currenUserInfo.getOrgTitle();
        log.info("获取权限中的orgNo：" + orgNo + ",orgTitle：" + orgTitle);
        // 获取权限中的orgNo：3440101,orgTitle：3
        // 机构子集
        List<String> list0 = new ArrayList<>();
        List<String> list1 = null;
        List<String> list2 = null;
        if (null != orgNo && !"".equals(orgNo)) {
            if (!"1".equals(orgTitle)) {
                // 获取用户权限内所有组织机构集合
                list1 = OrganizationUtil.getAllOrgByOrgNo();
                if (null == list1 || list1.size() == 0) {
                    return new ArrayList<>();
                }
                // 根据参数查询其所有子集
                if (null != consContractParam.getOrgId() && !"".equals(consContractParam.getOrgId())) {
                    list2 = OrganizationUtil.getAllOrgByOrgNoPamarm(consContractParam.getOrgId());
                    // 筛选用户机构集合中包含参数的机构
                    if (null != list1 && null != list2) {
                        for (String s : list2) {
                            if (list1.contains(s)) {
                                list0.add(s);
                            }
                        }
                    }
                    if (CollectionUtil.isEmpty(list0)) {
                        return new ArrayList<>();
                    }
                } else {
                    list0 = list1;
                }
            } else {
                if (null != consContractParam.getOrgId() && !"".equals(consContractParam.getOrgId())) {
                    list0 = OrganizationUtil.getAllOrgByOrgNoPamarm(consContractParam.getOrgId());
                }
            }
        } else {
            return new ArrayList<>();
        }
        consContractParam.setOrgIds(list0);
        // log.info("测试*********" + consContractParam.getOrgIds());
        // List<Region> result = systemClientService.queryAll();
        // Map<String, String> map = result.stream().collect(Collectors.toMap(Region::getId, Region::getName));
        List<DrConsContractDetailsVO> list = consContractInfoMapper.exportDrConsDetails(consContractParam);
        log.info("测试99999999" + list);
        // 供电单位 id 以及名称
        Result allOrgs = systemClientService.getAllOrgs();
        JSONObject jsonObject = null;
        if (ObjectUtil.isNotEmpty(allOrgs)) jsonObject = allOrgs.getData();
        // 请求字典接口
        DictTypeParam dictTypeParam = new DictTypeParam();
        dictTypeParam.setCode(DrSysDictDataEnum.CHECK_STATUS.getCode());

        List<Dict> dicts = dictTypeService.dropDown(dictTypeParam);

        for (DrConsContractDetailsVO cons : list) {
            dicts.forEach(n -> {
                if (n.get("code").toString().equals(cons.getCheckStatus())) {
                    cons.setCheckStatusDesc(n.get("value").toString());
                }
            });
            if (ObjectUtil.isNotEmpty(cons.getCityOrgNo())) {
                String names = "";
                if (jsonObject.containsKey(cons.getCityOrgNo())) {
                    Object data = jsonObject.get(cons.getCityOrgNo());
                    JSONObject datas = (JSONObject) JSON.toJSON(data);
                    names = (String) datas.get("name");
                }
                cons.setCityOrgNo(names);
            }
            if (ObjectUtil.isNotEmpty(cons.getAreaOrgNo())) {
                String names = "";
                if (jsonObject.containsKey(cons.getAreaOrgNo())) {
                    Object data = jsonObject.get(cons.getAreaOrgNo());
                    JSONObject datas = (JSONObject) JSON.toJSON(data);
                    names = (String) datas.get("name");
                }
                cons.setAreaOrgNo(names);
            }
            // if (jsonObject.containsKey(cons.getAreaOrgNo())) {
            //
            // }
            // String cityCodeDesc = map.get(cons.getCityCode()) == null ? cons.getCityCode() : map.get(cons.getCityCode());
            // String countyCodeDesc = map.get(cons.getCountyCode()) == null ? cons.getCountyCode() : map.get(cons.getCountyCode());
            // cons.setCityCodeDesc(cityCodeDesc);
            // cons.setCountyCodeDesc(countyCodeDesc);
        }
        Long projectId = consContractParam.getProjectId();
        List<DrConsContractDetailsVO> drConsContractDetails = consContractDetailMapper.selectInfoByProjectId(projectId);
        for (DrConsContractDetailsVO d : list) {
            for (DrConsContractDetailsVO o : drConsContractDetails) {
                if (d.getConsId().equals(o.getConsId())) {
                    if (o.getTimeType().equals("1") && o.getResponseType().equals("1") && o.getAdvanceNoticeTime().equals(1)) {
                        // 日前约时削峰
                        d.setDayInvtionPeakContractCap(o.getContractCap());
                        d.setDayInvtionPeakMinTimes(o.getMinTimes());
                    } else if (o.getTimeType().equals("1") && o.getResponseType().equals("1") && o.getAdvanceNoticeTime().equals(2)) {
                        // 小时级约时削峰
                        d.setHourInvtionPeakContractCap(o.getContractCap());
                        d.setHourInvtionPeakMinTimes(o.getMinTimes());
                    } else if (o.getTimeType().equals("2") && o.getResponseType().equals("1") && o.getAdvanceNoticeTime().equals(3)) {
                        // 分钟级实时削峰
                        d.setMinuteInvtionPeakContractCap(o.getContractCap());
                        d.setMinuteInvtionPeakMinTimes(o.getMinTimes());
                    } else if (o.getTimeType().equals("2") && o.getResponseType().equals("1") && o.getAdvanceNoticeTime().equals(4)) {
                        // 秒级实时削峰
                        d.setSecondInvtionPeakContractCap(o.getContractCap());
                        d.setSecondInvtionPeakMinTimes(o.getMinTimes());
                    } else if (o.getTimeType().equals("1") && o.getResponseType().equals("2") && o.getAdvanceNoticeTime().equals(1)) {
                        // 日前约时削谷
                        d.setDayInvtionValleyContractCap(o.getContractCap());
                        d.setDayInvtionValleyMinTimes(o.getMinTimes());
                    }
                }
            }
        }

        return list;
    }

    @Override
    public Page<ConsContractInfo> pageProxyContract(CustContractParam custContractParam) {
        return getBaseMapper().pageProxyContract(custContractParam.getPage(), custContractParam.getProjectId(), custContractParam.getCustId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseData consContract(ConsContractParam consContractParam) {
        if (consContractParam != null) {
            List<ConsContractDetail> fillInDetailList = consContractParam.getFillInDetailList();
            if (!CollectionUtils.isEmpty(fillInDetailList)) {
                for (ConsContractDetail contractDetail : fillInDetailList) {
                    if (ResponseTypeEnum.DES.getCode().equals(contractDetail.getResponseType())) {

                        // 从 负控取 新型负荷  20220621 更改需求  取合同容量   因此注释
                        //（1）削峰协议负荷值不能超过新型负荷管理可控负荷值；
//                        BigDecimal contractCap = contractDetail.getContractCap();
//                        BigDecimal controlCap = contractDetail.getControlCap();
//                        if (contractCap == null) {
//                            contractCap = BigDecimal.ZERO;
//                        }
//                        if (controlCap == null) {
//                            controlCap = BigDecimal.ZERO;
//                        }
//
//                        // 削峰协议负荷值不能超过新型负荷管理可控负荷值；
//                        if (contractCap.compareTo(controlCap) > 0) {
//                            return ResponseData.fail("500", "削峰协议负荷值不能超过新型负荷管理可控负荷值；", null);
//                        }

                        // 削峰协议负荷值不能超过该户号合同容量*50%；
                        BigDecimal contractCap = contractDetail.getContractCap();
                        BigDecimal contractInfnCap = contractDetail.getContractInfnCap();
                        if (contractCap == null) {
                            contractCap = BigDecimal.ZERO;
                        }
                        if (contractInfnCap == null) {
                            contractInfnCap = BigDecimal.ZERO;
                        }

                        // 削峰协议负荷值不能超过该户号合同容量*50%；
                        if (contractCap.compareTo(contractInfnCap) > 0) {
                            return ResponseData.fail("500", "削峰协议负荷值不能超过该户号合同容量", null);
                        }
                    } else {
                        //（2）填谷协议负荷值不能超过合同容量；
                        BigDecimal contractCap = contractDetail.getContractCap();
                        BigDecimal contractInfnCap = contractDetail.getContractInfnCap();
                        if (contractCap == null) {
                            contractCap = BigDecimal.ZERO;
                        }
                        if (contractInfnCap == null) {
                            contractInfnCap = BigDecimal.ZERO;
                        }

                        // 填谷协议负荷值不能超过合同容量；
                        if (contractCap.compareTo(contractInfnCap) > 0) {
                            return ResponseData.fail("500", "填谷协议负荷值不能超过合同容量", null);
                        }
                    }
                }
            }
        }


        // 用户签约基本信息保存或更新
        ConsContractInfo consContract = getOne(Wrappers.<ConsContractInfo>lambdaQuery()
                .eq(ConsContractInfo::getConsId, consContractParam.getConsId())
                .eq(ConsContractInfo::getProjectId, consContractParam.getProjectId())
                .last("LIMIT 1"));
        ConsContractInfo consContractInfo = ObjectUtil.isNull(consContract) ? new ConsContractInfo() : consContract;

        consContractInfo.setConsId(consContractParam.getConsId())
                .setProjectId(consContractParam.getProjectId())
                .setParticType(consContractParam.getParticType())
                .setFirstContactInfo(consContractParam.getFirstContactInfo())
                .setFirstContactName(consContractParam.getFirstContactName())
                .setCheckStatus(ProjectCheckStatusEnums.UNSUBMITTED.getCode())
                .setStatus(DeclareProjectEnums.SAVE.getCode());

        if (!ObjectUtil.isNull(consContractParam.getExtractRatio())) {
            consContractInfo.setExtractRatio(consContractParam.getExtractRatio());
        }

        // consContractInfo 对象塞值
        String consId = consContractParam.getConsId();
        Cons cons = consService.getById(consId);
        if (cons != null) {
            Long custId = cons.getCustId();
            if (custId != null) {
                consContractInfo.setCustId(custId);
            } else {
                LambdaQueryWrapper<UserConsRela> userConsRelaLambdaQueryWrapper = new LambdaQueryWrapper<>();
                userConsRelaLambdaQueryWrapper.eq(UserConsRela::getConsNo, consId);
                List<UserConsRela> userConsRelas = userConsRelaService.list(userConsRelaLambdaQueryWrapper);

                if (!CollectionUtils.isEmpty(userConsRelas)) {
                    consContractInfo.setCustId(userConsRelas.get(0).getCustId());
                }
            }
        }

        // 传递文件
        if (consContractParam.getFileId() != null) {
            consContractInfo.setFileId(consContractParam.getFileId());
            consContractInfo.setFileType(consContractParam.getFileType());
            consContractInfo.setFileName(consContractParam.getFileName());
        }

        saveOrUpdate(consContractInfo);

        // 用户申报明细

//        List<ConsContractDetail> detailList = consContractDetailService.list(Wrappers.<ConsContractDetail>lambdaQuery()
//                .eq(ConsContractDetail::getContractId, consContractInfo.getContractId()));
        LambdaQueryWrapper<ConsContractDetail> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ConsContractDetail::getContractId, consContractInfo.getContractId());
        consContractDetailService.remove(lambdaQueryWrapper);
        for (ConsContractDetail detail : consContractParam.getFillInDetailList()) {
            ConsContractDetail consContractDetail = null;
//            for (ConsContractDetail contractDetail : detailList) {
//                if (contractDetail.getProjectDetailId().equals(detail.getProjectDetailId())) {
//                    consContractDetail = contractDetail;
//                }
//            }
//            if (ObjectUtil.isNull(consContractDetail)) {
            consContractDetail = new ConsContractDetail();
            consContractDetail.setContractId(consContractInfo.getContractId());
            consContractDetail.setResponseType(detail.getResponseType());
            consContractDetail.setTimeType(detail.getTimeType());
            consContractDetail.setAdvanceNoticeTime(detail.getAdvanceNoticeTime());
            consContractDetail.setProjectDetailId(detail.getDetailId());
//            }
            consContractDetail.setContractCap(detail.getContractCap());
            consContractDetail.setSpareCap(detail.getSpareCap());
            consContractDetail.setExtractRatio(detail.getExtractRatio());
            consContractDetail.setMinTimes(detail.getMinTimes());
            consContractDetail.setSpareMinTimes(detail.getSpareMinTimes());
            consContractDetail.setAirconditionCap(detail.getAirconditionCap());
            consContractDetailService.saveOrUpdate(consContractDetail);
        }
        return ResponseData.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addSigning(ConsContractParam consContractParam) {
        String custId = Objects.requireNonNull(SecurityUtils.getCurrentUserInfoUTF8()).getId();
        Cust cust = custService.getById(custId);
        if (ObjectUtil.isNull(cust)) {
            throw new ServiceException(500, "无法查询到客户信息");
        }

        CustContractInfo custContractInfo;
        LambdaQueryWrapper<CustContractInfo> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(CustContractInfo::getProjectId, consContractParam.getProjectId());
        if (IsAggregatorEnum.AGGREGATOR.getCode().equals(cust.getIntegrator())) {
            // 集成商
            lambdaQueryWrapper.eq(CustContractInfo::getCustId, cust.getId());
            CustContractInfo contractInfo = custContractInfoService.getOne(lambdaQueryWrapper);
            custContractInfo = ObjectUtil.isNull(contractInfo) ? new CustContractInfo() : contractInfo;
            custContractInfo.setCustId(cust.getId());
            custContractInfo.setIntegrator(IsAggregatorEnum.AGGREGATOR.getCode().toString());
        } else {
            lambdaQueryWrapper.eq(CustContractInfo::getCustId, cust.getId());
            CustContractInfo contractInfo = custContractInfoService.getOne(lambdaQueryWrapper);
            custContractInfo = ObjectUtil.isNull(contractInfo) ? new CustContractInfo() : contractInfo;
            custContractInfo.setCustId(cust.getId());
            custContractInfo.setIntegrator(IsAggregatorEnum.NOT_AGGREGATOR.getCode().toString());
        }

        // 客户项目申报基本信息
        custContractInfo.setProjectId(consContractParam.getProjectId());
        custContractInfo.setStatus(DeclareProjectEnums.SAVE.getCode());
        custContractInfo.setCheckStatus(DeclareProjectCheckEnums.UNSUBMIT.getCode());
        custContractInfo.setFirstContactName(cust.getApplyName());
        custContractInfo.setFirstContactInfo(cust.getTel());

        // 查询是否存在签约用户，没有则抛出异常
        List<ConsContractInfo> consContractInfos = listConsByCust(custId, consContractParam.getProjectId().toString());
        if (CollectionUtils.isEmpty(consContractInfos)) {
            throw new ServiceException(-1, "请填报用户签约后再保存");
        }
        custContractInfoService.saveOrUpdate(custContractInfo);
        return custContractInfo.getContractId();

        // 保存 客户签约明细  2022.04.08 上午需要把 ConsContractDetail 数据 统计到 CustContractDetail表， 下午取消计划 此段代码待测试
        /*List<CustContractDetail> custContractDetailList = new ArrayList<>();
        List<Long> contractIds = consContractInfos.stream().map(ConsContractInfo::getContractId).collect(Collectors.toList());

        //根据用户签约ids  查询用户签约详情
        List<ConsContractDetail> consContractDetails = consContractDetailService.listByIds(contractIds);
        if(!CollectionUtils.isEmpty(consContractDetails)){
            Map<Long, List<ConsContractDetail>> map = consContractDetails.stream().collect(Collectors.groupingBy(ConsContractDetail::getProjectDetailId));

            for (Map.Entry<Long, List<ConsContractDetail>> entry : map.entrySet()) {
                CustContractDetail custContractDetail = new CustContractDetail();

                Long projectDetailId = entry.getKey();
                List<ConsContractDetail> details = entry.getValue();

                //塞值
                custContractDetail.setProjectDetailId(projectDetailId);
                custContractDetail.setContractId(custContractInfo.getContractId());

                //累加 备用容量
                BigDecimal spareCapSum = details.stream().map(ConsContractDetail::getSpareCap).reduce(BigDecimal.ZERO,BigDecimal::add);
                custContractDetail.setSpareCap(spareCapSum);

                //累加 签约容量（最大容量）
                BigDecimal contractCapSum = details.stream().map(ConsContractDetail::getContractCap).reduce(BigDecimal.ZERO,BigDecimal::add);
                custContractDetail.setContractCap(contractCapSum);
                custContractDetailList.add(custContractDetail);
            }
            // 客户签约详情 入库
            custContractDetailService.saveBatch(custContractDetailList);
        }*/

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void verifySigning(BusConfigParam busConfigParam) {
        String custId = Objects.requireNonNull(SecurityUtils.getCurrentUserInfoUTF8()).getId();
        busConfigParam.setBusType(BusTypeEnum.SIGNING_PROCESS.getCode());
        busConfigParam.setOperaManId(custId);

        Result result = systemClient.selectInfo(busConfigParam);
        JSONObject data = result.getData();
        if (result.getCode().equals(ApprovalCodeEnum.SUCCESS.getCode())) {
            CustContractInfo custContractInfo = custContractInfoService.getById(busConfigParam.getBusId());
            if (ObjectUtil.isNull(custContractInfo)) {
                throw new ServiceException(-1, "找不到签约客户");
            }
            List<ConsContractInfo> consContractInfos = listConsByCust(custContractInfo.getCustId()
                    .toString(), custContractInfo.getProjectId().toString());
            consContractInfos.removeIf(contractInfo -> !DeclareProjectCheckEnums.VERIFYING.getCode()
                    .equals(contractInfo.getCheckStatus()));

            List<CustContractFile> custContractFiles = busConfigParam.getCustContractFiles();
            for (CustContractFile custContractFile : custContractFiles) {
                custContractFile.setProjectId(custContractInfo.getProjectId());
                custContractFile.setCustId(custContractInfo.getCustId());
            }
            if (CollectionUtils.isEmpty(custContractFiles)) {
                throw new ServiceException(-1, "上传文件为空");
            }
            if ("1".equals(busConfigParam.getIntegrator())) {
                if (custContractFiles.size() < 2) {
                    throw new ServiceException(-1, "协议文件 或者 承诺书未上传,请核查");
                }
            } else {
                if (CollectionUtils.isEmpty(custContractFiles)) {
                    throw new ServiceException(-1, "上传文件为空");
                }
            }

            LambdaQueryWrapper<CustContractFile> custFileWrapper = new LambdaQueryWrapper<>();
            custFileWrapper.eq(CustContractFile::getCustId, custContractInfo.getCustId());
            custFileWrapper.eq(CustContractFile::getProjectId, custContractInfo.getProjectId());

            custFileWrapper.in(CustContractFile::getFileType, FileTypeEnum.CUST_CONTRACT_AGREEMENT.getCode(), FileTypeEnum.CUST_PROMISE_BOOK.getCode());
            custContractFileService.remove(custFileWrapper);
            // 保存 协议 承诺书
            boolean b = custContractFileService.saveBatch(custContractFiles);

            if (data.getString("handleCode").equals(ApprovalCodeEnum.PROCESS_SUCCESS.getCode())) {
                custContractInfo.setCheckStatus(DeclareProjectCheckEnums.VERIFIED.getCode());
                custContractInfo.setStatus(DeclareProjectEnums.SIGNED.getCode());
                consContractInfos.forEach(contractInfo -> {
                    contractInfo.setCheckStatus(DeclareProjectCheckEnums.VERIFIED.getCode());
                    contractInfo.setStatus(DeclareProjectEnums.SIGNED.getCode());
                    // 给用户发送签约成功短信
                    smsSendService.generateSms(contractInfo.getConsId(), SmsTemplateTypeEnum.DECLARE_RESULT_SUCCESS.getCode());
                });
            } else if (data.getString("handleCode").equals(ApprovalCodeEnum.APPROVAL_REJECT.getCode())) {
                custContractInfo.setCheckStatus(DeclareProjectCheckEnums.UNVERIFIED.getCode());
                consContractInfos.forEach(contractInfo -> {
                    contractInfo.setCheckStatus(DeclareProjectCheckEnums.UNVERIFIED.getCode());
                    smsSendService.generateSms(contractInfo.getConsId(), SmsTemplateTypeEnum.DECLARE_RESULT_FAILED.getCode());
                });
            }
            updateBatchById(consContractInfos);
            custContractInfoService.updateById(custContractInfo);
        } else {
            throw new ServiceException(ExamineExceptionEnum.CREATION_FAILED);
        }
    }

    @Override
    public List<ConsContractInfo> listConsByCust(String custId, String projectId) {
        Cust cust = custService.getById(custId);
        List<String> consIds;
        if (IsAggregatorEnum.AGGREGATOR.getCode().equals(cust.getIntegrator())) {
            List<UserConsRela> userConsRelas = userConsRelaService.list(Wrappers.<UserConsRela>lambdaQuery()
                    .eq(UserConsRela::getCustId, cust.getId()));
            consIds = userConsRelas.stream().map(UserConsRela::getConsNo).collect(Collectors.toList());
        } else {
            List<Cons> consList = consService.list(Wrappers.<Cons>lambdaQuery().eq(Cons::getCustId, cust.getId()));
            consIds = consList.stream().map(Cons::getId).collect(Collectors.toList());
        }
        LambdaQueryWrapper<ConsContractInfo> consLambdaQueryWrapper = Wrappers.lambdaQuery();
        if (CollectionUtils.isEmpty(consIds)) {
            consLambdaQueryWrapper.in(ConsContractInfo::getConsId, -1);
        } else {
            consLambdaQueryWrapper.in(ConsContractInfo::getConsId, consIds);
        }
        consLambdaQueryWrapper.eq(ConsContractInfo::getProjectId, projectId);
        return list(consLambdaQueryWrapper);
    }

    @Override
    public Page<ConsContractInfo> listVerifyContract(BusConfigParam busConfigParam) {
        CustContractInfo custContractInfo = custContractInfoService.getById(busConfigParam.getBusId());
        if (ObjectUtil.isEmpty(custContractInfo)) {
            throw new ServiceException(-1, "签约信息不存在");
        }

        List<ConsContractInfo> consContractInfo = listConsByCust(custContractInfo.getCustId()
                .toString(), custContractInfo.getProjectId().toString());
        List<String> consIds = consContractInfo.stream().map(ConsContractInfo::getConsId).collect(Collectors.toList());

        LambdaQueryWrapper<ConsContractInfo> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(ConsContractInfo::getProjectId, custContractInfo.getProjectId())
                .in(ConsContractInfo::getConsId, consIds)
                .ne(ConsContractInfo::getCheckStatus, DeclareProjectCheckEnums.UNSUBMIT.getCode());
        Page<ConsContractInfo> page = page(new Page<>(busConfigParam.getCurrent(), busConfigParam.getSize()), lambdaQueryWrapper);
        page.getRecords().forEach(contractInfo -> {
            Cons cons = consService.getById(contractInfo.getConsId());
            contractInfo.setConsName(cons.getConsName());
            contractInfo.setContractCap(cons.getContractCap());
            contractInfo.setRunCap(cons.getRunCap().toString());
        });
        return page;
    }

    @Override
    public List<ProjectDetail> listContractCap(CustContractParam custContractParam) {
        String custId = Objects.requireNonNull(SecurityUtils.getCurrentUserInfoUTF8()).getId();

        List<ConsContractInfo> consContractInfos = listConsByCust(custId, custContractParam.getProjectId().toString());
        List<Long> contractIds = consContractInfos.stream()
                .map(ConsContractInfo::getContractId)
                .collect(Collectors.toList());
        List<ProjectDetail> projectDetails = projectDetailService.listByProjectId(custContractParam.getProjectId());
        LambdaQueryWrapper<ConsContractDetail> lambdaQueryWrapper = Wrappers.lambdaQuery();
        if (CollectionUtils.isEmpty(contractIds)) {
            lambdaQueryWrapper.in(ConsContractDetail::getContractId, -1);
        } else {
            lambdaQueryWrapper.in(ConsContractDetail::getContractId, contractIds);
        }
        List<ConsContractDetail> list = consContractDetailService.list(lambdaQueryWrapper);
        for (ProjectDetail projectDetail : projectDetails) {
            projectDetail.setContractCap(BigDecimal.ZERO);
            for (ConsContractDetail consContractDetail : list) {
                if (projectDetail.getDetailId()
                        .equals(consContractDetail.getProjectDetailId()) && !ObjectUtil.isNull(consContractDetail.getContractCap())) {
                    projectDetail.setContractCap(projectDetail.getContractCap()
                            .add(consContractDetail.getContractCap()));
                }
            }
        }
        return projectDetails;
    }

    @Override
    public Page<ContractDetailVO> pageContractDetail(CustContractParam custContractParam) {
        Long contractId = custContractParam.getContractId();
        if (null == contractId) {
            return new Page<>();
        }
        Page<ContractDetailVO> page = new Page<>(custContractParam.getCurrent(), custContractParam.getSize());
        List<ContractDetailVO> list = consContractDetailMapper.pageContractDetailPage(page, contractId);
        if (null == list || list.size() == 0) {
            return new Page<>();
        }
        // 日前约时削峰
        Map<String, Object> map = new HashMap<>();
        map.put("contractId", contractId);
        map.put("timeType", "1");
        map.put("responseType", "1");
        map.put("advanceNoticeTime", 1);
        List<ContractDetailVO> list1 = consContractDetailMapper.pageContractDetail(map);
        // 小时级约时削峰
        map.put("timeType", "1");
        map.put("responseType", "1");
        map.put("advanceNoticeTime", 2);
        List<ContractDetailVO> list2 = consContractDetailMapper.pageContractDetail(map);
        // 分钟级实时削峰
        map.put("timeType", "2");
        map.put("responseType", "1");
        map.put("advanceNoticeTime", 3);
        List<ContractDetailVO> list3 = consContractDetailMapper.pageContractDetail(map);
        // 秒级实时削峰
        map.put("timeType", "2");
        map.put("responseType", "1");
        map.put("advanceNoticeTime", 4);
        List<ContractDetailVO> list4 = consContractDetailMapper.pageContractDetail(map);
        // 日前约时填谷
        map.put("timeType", "1");
        map.put("responseType", "2");
        map.put("advanceNoticeTime", 1);
        List<ContractDetailVO> list5 = consContractDetailMapper.pageContractDetail(map);
        for (ContractDetailVO contractDetailVO : list) {
            if (null != list1 && list1.size() > 0) {
                for (ContractDetailVO contractDetailVO1 : list1) {
                    if (contractDetailVO.getConsId().equals(contractDetailVO1.getConsId())) {
                        contractDetailVO.setDayInvtionPeakContractCap(contractDetailVO1.getDayInvtionPeakContractCap());
                        contractDetailVO.setDayInvtionPeakMinTimes(contractDetailVO1.getDayInvtionPeakMinTimes());
                    }
                }
            }

            if (null != list2 && list2.size() > 0) {
                for (ContractDetailVO contractDetailVO1 : list2) {
                    if (contractDetailVO.getConsId().equals(contractDetailVO1.getConsId())) {
                        contractDetailVO.setHourInvtionPeakContractCap(contractDetailVO1.getDayInvtionPeakContractCap());
                        contractDetailVO.setHourInvtionPeakMinTimes(contractDetailVO1.getDayInvtionPeakMinTimes());
                    }
                }
            }

            if (null != list3 && list3.size() > 0) {
                for (ContractDetailVO contractDetailVO1 : list3) {
                    if (contractDetailVO.getConsId().equals(contractDetailVO1.getConsId())) {
                        contractDetailVO.setMinuteInvtionPeakContractCap(contractDetailVO1.getDayInvtionPeakContractCap());
                        contractDetailVO.setMinuteInvtionPeakMinTimes(contractDetailVO1.getDayInvtionPeakMinTimes());
                    }
                }
            }

            if (null != list4 && list4.size() > 0) {
                for (ContractDetailVO contractDetailVO1 : list4) {
                    if (contractDetailVO.getConsId().equals(contractDetailVO1.getConsId())) {
                        contractDetailVO.setSecondInvtionPeakContractCap(contractDetailVO1.getDayInvtionPeakContractCap());
                        contractDetailVO.setSecondInvtionPeakMinTimes(contractDetailVO1.getDayInvtionPeakMinTimes());
                    }
                }
            }

            if (null != list5 && list5.size() > 0) {
                for (ContractDetailVO contractDetailVO1 : list5) {
                    if (contractDetailVO.getConsId().equals(contractDetailVO1.getConsId())) {
                        contractDetailVO.setDayInvtionValleyContractCap(contractDetailVO1.getDayInvtionPeakContractCap());
                        contractDetailVO.setDayInvtionValleyMinTimes(contractDetailVO1.getDayInvtionPeakMinTimes());
                    }
                }
            }
        }
        page.setRecords(list);
        return page;
    }

    /**
     * 查询用户签约 详情根据id
     *
     * @param contractId
     * @return
     */
    @Override
    public List<ConsContractDetail> queryDrConsDetailByInfoId(Long contractId) {
        QueryWrapper<ConsContractDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("contract_id", contractId);
        List<ConsContractDetail> list = consContractDetailService.list();
        return list;
    }

    @Override
    public ConsContractInfo queryByConsIdAndProjectId(String consId, Long projectId) {
        LambdaQueryWrapper<ConsContractInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotEmpty(consId) && ObjectUtil.isNotNull(consId)) {
            lambdaQueryWrapper.eq(ConsContractInfo::getConsId, consId);
        }
        if (ObjectUtil.isNotEmpty(projectId) && ObjectUtil.isNotNull(projectId)) {
            lambdaQueryWrapper.eq(ConsContractInfo::getProjectId, projectId);
        }
        List<ConsContractInfo> consContractInfos = this.list(lambdaQueryWrapper);
        if (CollectionUtil.isEmpty(consContractInfos) || consContractInfos.size() == 0) {
            return null;
        }
        return consContractInfos.get(0);

    }

    @Resource
    private SystemLoadbusiClient systemLoadbusiClient;

    @Override
    public void exportConsContractTemplate(HttpServletRequest request, HttpServletResponse response, String projectId) {
        CurrenUserInfo currentUserInfoUTF8 = SecurityUtils.getCurrentUserInfoUTF8();
        Cust cust = custService.getById(currentUserInfoUTF8.getId());
        List<String> consIdListByCust = consService.getConsIdListByCust(Long.valueOf(currentUserInfoUTF8.getId()));
        LambdaQueryWrapper<Cons> consLambdaQueryWrapper = new LambdaQueryWrapper<>();
        consLambdaQueryWrapper.in(Cons::getId, consIdListByCust);
//        consLambdaQueryWrapper.select(Cons::getId, Cons::getConsName);
        List<Cons> consList = consService.list(consLambdaQueryWrapper);
        Map<String, String> consIdNameMap = consList.stream().collect(Collectors.toMap(Cons::getId, Cons::getConsName));

        Project project = projectService.getById(projectId);
        if (project == null) {
            throw new ServiceException(2, "项目不存在");
        }
        List<ProjectDetail> projectDetails = projectDetailService.listByProjectId(Long.valueOf(projectId));
        List<ImportContractExcel> importContractExcels = new ArrayList<>();

        consList.forEach(cons -> {
//            BigDecimal controlCap = BigDecimal.ZERO;

//            LoadbusiConsParam loadbusiConsParam = new LoadbusiConsParam();
//            loadbusiConsParam.setConsNo(cons.getId());
//            JSONObject jsonObject = systemLoadbusiClient.getConsInfoByConsId(loadbusiConsParam);
//            if (jsonObject != null) {
//                if (jsonObject.get("code") != null) {
//                    JSONObject data = jsonObject.getJSONObject("data");
//                    if (data != null) {
//                        Object o = data.get("ratingLoad");
//                        if (o != null) {
//                            controlCap = new BigDecimal(o.toString());
//                        }
//                    }
//                }
//            }

//            BigDecimal finalControlCap = controlCap;
            BigDecimal finalControlCap = cons.getSafetyLoad();
            projectDetails.forEach(projectDetail -> {
                ImportContractExcel importContractExcel = new ImportContractIntegerExcel();
                importContractExcel.setProjectNo(project.getProjectNo());
                importContractExcel.setConsCode(cons.getId());
                importContractExcel.setConsName(cons.getConsName());
                importContractExcel.setOrgName(cons.getOrgName());
                importContractExcel.setConsContractCap(cons.getContractCap());
                importContractExcel.setRunCap(cons.getRunCap());
                importContractExcel.setFirstContactInfo(cons.getFirstContactInfo());
                importContractExcel.setFirstContactName(cons.getFirstContactName());
                importContractExcel.setProjectId(projectId);
                importContractExcel.setProjectName(project.getProjectName());
                importContractExcel.setResponseType(projectDetail.getResponseType());
                importContractExcel.setProjectDetailId(projectDetail.getDetailId());
                importContractExcel.setTimeType(projectDetail.getTimeType());
                importContractExcel.setAdvanceNoticeTime(projectDetail.getAdvanceNoticeTime());
                importContractExcel.setResponseTypeName(ProjectTargetEnums.getMessageByCode(importContractExcel.getResponseType()));
                importContractExcel.setTimeTypeName(ProjectTimeTypeEnum.getMessageByCode(importContractExcel.getTimeType()));
                importContractExcel.setAdvanceNoticeTimeName(AdvanceNoticeEnums.getMessageByCode(String.valueOf(importContractExcel.getAdvanceNoticeTime())));
//                if (ProjectTargetEnums.FILL.getCode().equals(importContractExcel.getResponseType())) {
                importContractExcel.setControlCap(finalControlCap);
//                }
                importContractExcels.add(importContractExcel);
            });
        });
        List<Map<String, Object>> sheetList = new ArrayList<>();
        Map<String, Object> sheet1 = new HashMap<>();
        sheet1.put(NormalExcelConstants.CLASS, Objects.equals(cust.getIntegrator(), 1) ? ImportContractIntegerExcel.class : ImportContractExcel.class);
        sheet1.put(NormalExcelConstants.PARAMS, new ExportParams("约时签约配置", "约时签约配置", ExcelType.XSSF));
        sheet1.put(NormalExcelConstants.DATA_LIST, importContractExcels);
        sheetList.add(sheet1);

        LambdaQueryWrapper<DeviceAdjustableBase> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(DeviceAdjustableBase::getConsId, consIdListByCust);
//        List<ImportContractDeviceExcel> importContractDeviceExcels = deviceAdjustableBaseService.list(lambdaQueryWrapper)
//                .stream()
//                .flatMap(item -> {
//                    Stream.Builder<ImportContractDeviceExcel> builder = Stream.builder();
//                    projectDetails.forEach(projectDetail -> {
//                        ImportContractDeviceExcel importContractDeviceExcel = new ImportContractDeviceExcel();
//                        BeanUtils.copyProperties(item, importContractDeviceExcel);
//                        importContractDeviceExcel.setConsName(consIdNameMap.get(item.getConsId()));
//                        importContractDeviceExcel.setIsChecked(0);
//                        importContractDeviceExcel.setResponseType(projectDetail.getResponseType());
//                        importContractDeviceExcel.setTimeType(projectDetail.getTimeType());
//                        importContractDeviceExcel.setAdvanceNoticeTime(projectDetail.getAdvanceNoticeTime());
//                        importContractDeviceExcel.setResponseTypeName(ProjectTargetEnums.getMessageByCode(importContractDeviceExcel.getResponseType()));
//                        importContractDeviceExcel.setTimeTypeName(ProjectTimeTypeEnum.getMessageByCode(importContractDeviceExcel.getTimeType()));
//                        importContractDeviceExcel.setAdvanceNoticeTimeName(AdvanceNoticeEnums.getMessageByCode(String.valueOf(importContractDeviceExcel.getAdvanceNoticeTime())));
//                        importContractDeviceExcel.setProjectDetailId(projectDetail.getDetailId());
//                        builder.add(importContractDeviceExcel);
//                    });
//                    return builder.build();
//                })
//                .collect(Collectors.toList());

//        Map<String, Object> sheet2 = new HashMap<>();
//        sheet2.put(NormalExcelConstants.CLASS, ImportContractDeviceExcel.class);
//        sheet2.put(NormalExcelConstants.PARAMS, new ExportParams("备用容量签约配置", "备用容量签约配置", ExcelType.XSSF));
//        sheet2.put(NormalExcelConstants.DATA_LIST, importContractDeviceExcels);

//        sheetList.add(sheet2);

        HashMap<String, Object> map = new HashMap<>();
        map.put(NormalExcelConstants.FILE_NAME, "签约导入模板");
//        map.put(NormalExcelConstants.MAP_LIST, sheetList);
        map.put(NormalExcelConstants.CLASS, Objects.equals(cust.getIntegrator(), 1) ? ImportContractIntegerExcel.class : ImportContractExcel.class);
        map.put(NormalExcelConstants.PARAMS, new ExportParams("约时签约配置", "约时签约配置", ExcelType.XSSF));
        map.put(NormalExcelConstants.DATA_LIST, importContractExcels);

        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long importConsContractByExcel(String pageProjectId, MultipartFile file) throws Exception {
        String custId = SecurityUtils.getCurrentUserInfoUTF8().getId();
        byte[] input = file.getBytes();
        ImportParams importParams = new ImportParams();
        importParams.setStartSheetIndex(0);
        importParams.setTitleRows(1);
        importParams.setHeadRows(1);
        List<ImportContractIntegerExcel> objectExcelImportResult;
        List<String> consIds = consService.getConsIdListByCust(Long.valueOf(custId));
//        List<ImportContractDeviceExcel> excelImportDeviceResult;
        try {
            objectExcelImportResult = ExcelImportUtil.importExcel(new ByteArrayInputStream(input), ImportContractIntegerExcel.class, importParams);
            importParams.setStartSheetIndex(1);
//            excelImportDeviceResult = ExcelImportUtil.importExcel(new ByteArrayInputStream(input), ImportContractDeviceExcel.class, importParams);
        } catch (Exception e) {
            throw new ServiceException(2, "文件模板错误");
        }
        if (CollectionUtils.isEmpty(objectExcelImportResult)
//                || excelImportDeviceResult == null
                || StringUtils.isEmpty(objectExcelImportResult.get(0).getProjectId())) {
            throw new ServiceException(2, "文件模板错误");
        }
        Map<String, List<ImportContractIntegerExcel>> consMap = objectExcelImportResult.stream()
                .collect(Collectors.groupingBy(ImportContractExcel::getConsCode));
        Iterator<Map.Entry<String, List<ImportContractIntegerExcel>>> iterator = consMap.entrySet().iterator();
//        Map<String, List<ImportContractDeviceExcel>> spareDeviceMap = excelImportDeviceResult.stream()
//                .filter(item -> item.getIsChecked() != null && item.getIsChecked() == 1)
//                .collect(Collectors.groupingBy(ImportContractDeviceExcel::getConsId));
//        List<SpareContractDeviceParam> list2 = new ArrayList<>();
        String projectId = objectExcelImportResult.get(0).getProjectId();
        if (StringUtils.hasText(pageProjectId) && !pageProjectId.equals(projectId)) {
            throw new ServiceException(4, "模板与所选项目不对应!");
        }
        LambdaQueryWrapper<CustContractInfo> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper2.eq(CustContractInfo::getProjectId, projectId);
        lambdaQueryWrapper2.eq(CustContractInfo::getCustId, custId);
//        lambdaQueryWrapper2.ne(CustContractInfo::getStatus, 2);
        if (custContractInfoService.count(lambdaQueryWrapper2) > 0) {
            throw new ServiceException(3, "项目已签约, 请重新选择模板导入!");
        }
//        List<SpareContractDeviceParam> spareContractDeviceParams = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<String, List<ImportContractIntegerExcel>> next = iterator.next();
            String consId = next.getKey();
            if (consIds.indexOf(consId) == -1) {
                throw new ServiceException(7, "签约失败, 户号: " + consId + " 您无权签约");
            }
            ImportContractIntegerExcel importContractIntegerExcel = next.getValue().get(0);
            ConsContractParam consContractParam = new ConsContractParam();
            consContractParam.setProjectId(Long.valueOf(importContractIntegerExcel.getProjectId()));
            consContractParam.setConsId(importContractIntegerExcel.getConsCode());
            consContractParam.setParticType(ParticipateTypeEnums.DIRECT_PARTICIPATE.getCode());
            consContractParam.setFirstContactName(importContractIntegerExcel.getFirstContactName());
            consContractParam.setFirstContactInfo(importContractIntegerExcel.getFirstContactInfo());
            consContractParam.setExtractRatio(importContractIntegerExcel.getExtractRatio());
            consContractParam.setDetailId(String.valueOf(importContractIntegerExcel.getProjectDetailId()));
            consContractParam.setParticType(custService.getById(custId).getIntegrator() == 1 ? "2" : "1");
            consContractParam.setFillInDetailList(new ArrayList<>());
            for (ImportContractIntegerExcel contractIntegerExcel : next.getValue()) {
//                if (contractIntegerExcel.getControlCap() == null || contractIntegerExcel.getControlCap()
//                        .compareTo(BigDecimal.ZERO) <= 0) {
//                    throw new ServiceException(10, "签约容量必须大于0");
//                }
//                if (contractIntegerExcel.getMinTimes() == null || contractIntegerExcel.getMinTimes() <= 0) {
//                    throw new ServiceException(10, "最小响应时长必须大于0");
//                }
                ConsContractDetail consContractDetail = new ConsContractDetail();
                consContractDetail.setResponseType(contractIntegerExcel.getResponseType());
                consContractDetail.setTimeType(contractIntegerExcel.getTimeType());
                consContractDetail.setAdvanceNoticeTime(contractIntegerExcel.getAdvanceNoticeTime());
                if (contractIntegerExcel.getContractCap().compareTo(contractIntegerExcel.getConsContractCap()) > 0) {
                    throw new ServiceException(8, "响应负荷不能大于合同容量");
                }
                consContractDetail.setContractCap(contractIntegerExcel.getContractCap());
                consContractDetail.setMinTimes(contractIntegerExcel.getMinTimes());
                consContractDetail.setControlCap(contractIntegerExcel.getControlCap());
                consContractDetail.setContractInfnCap(contractIntegerExcel.getConsContractCap());
//                consContractDetail.setSpareMinTimes(contractIntegerExcel.getSpareMinTimes());
                consContractDetail.setProjectDetailId(contractIntegerExcel.getProjectDetailId());
//                consContractDetail.setAirconditionCap(contractIntegerExcel.getAirconditionCap());
//                consContractDetail.setSpareCap(contractIntegerExcel.getSpareCap());
                consContractDetail.setDetailId(contractIntegerExcel.getProjectDetailId());
//                List<ImportContractDeviceExcel> importContractDeviceExcels = spareDeviceMap.getOrDefault(consId, new ArrayList<>())
//                        .stream()
//                        .filter(item -> contractIntegerExcel.getProjectDetailId().equals(item.getProjectDetailId()))
//                        .collect(Collectors.toList());
                consContractParam.getFillInDetailList().add(consContractDetail);
                // 设备处理
//                SpareContractDeviceParam spareContractDeviceParam = new SpareContractDeviceParam();
////                    spareContractDeviceParam.setContractId(consContractInfo.getContractId());
//                spareContractDeviceParam.setDetailId(String.valueOf(contractIntegerExcel.getProjectDetailId()));
//                BigDecimal sparePower = BigDecimal.ZERO;
//                List<SpareContractDevice> spareContractDevices = new ArrayList<>();
//                if (importContractDeviceExcels != null && importContractDeviceExcels.size() > 0) {
//                    sparePower = importContractDeviceExcels.stream()
//                            .filter(item -> item.getRatedPower() != null)
//                            .map(ImportContractDeviceExcel::getRatedPower)
//                            .map(BigDecimal::valueOf)
//                            .reduce(BigDecimal::add)
//                            .orElse(BigDecimal.ZERO);
//                    spareContractDevices.addAll(importContractDeviceExcels.stream().map(item -> {
//                        SpareContractDevice spareContractDevice = new SpareContractDevice();
//                        spareContractDevice.setConsId(consId);
//                        spareContractDevice.setDeviceId(item.getDeviceId());
//                        spareContractDevice.setProjectId(Long.valueOf(contractIntegerExcel.getProjectId()));
//                        spareContractDevice.setParticType(consContractParam.getParticType());
//                        spareContractDevice.setResponseType(consContractDetail.getResponseType());
//                        spareContractDevice.setTimeType(consContractDetail.getTimeType());
//                        spareContractDevice.setResponseCap(consContractDetail.getResponseCap());
//                        spareContractDevice.setContractCap(consContractDetail.getContractCap());
//                        spareContractDevice.setContractPrice(consContractDetail.getContractPrice());
//                        spareContractDevice.setProjectDetailId(consContractDetail.getProjectDetailId());
//                        spareContractDevice.setDeviceName(item.getDeviceName());
//                        spareContractDevice.setRatedPower(item.getRatedPower() != null ? Long.valueOf(item.getRatedPower()) : null);
//                        return spareContractDevice;
//                    }).collect(Collectors.toList()));
//                }
//
//                if (consContractDetail.getSpareCap() != null && consContractDetail.getSpareCap()
//                        .compareTo(sparePower) > 0) {
//                    throw new ServiceException(5, "签约失败, 备用容量不能大于设备容量!");
//                }
//
//                if (consContractDetail.getAirconditionCap() != null && consContractDetail.getAirconditionCap()
//                        .compareTo(sparePower) > 0) {
//                    throw new ServiceException(6, "签约失败, 空调容量不能大于设备容量!");
//                }
//
//                spareContractDeviceParam.setConsId(consContractParam.getConsId());
//                spareContractDeviceParam.setSpareCap(sparePower);
//                spareContractDeviceParam.setDeviceList(spareContractDevices);
//                spareContractDeviceParams.add(spareContractDeviceParam);
            }
            ResponseData responseData = this.consContract(consContractParam);
            if (!Objects.equals(responseData.getCode(), ResponseData.SUCCESSFUL_CODE)) {
                throw new ServiceException(999, responseData.getMesg());
            }
        }
//        spareContractDeviceParams.forEach(item -> {
//            spareContractDeviceService.deviceContract(item);
//        });
        return Long.valueOf(objectExcelImportResult.get(0).getProjectId());
    }

    @Override
    public Integer getApprovalConsCount(List<String> orgNo) {
        return baseMapper.getApprovalConsCount(orgNo);
    }

    @Override
    public BigDecimal getApprovalConstractCapSum(List<String> orgNo) {
        return baseMapper.getApprovalConstractCapSum(orgNo);
    }

    @Override
    public List<Long> getConsByContractInfo(String projectId, String responseType, String timeType, String advanceTimeType) {
        return baseMapper.getConsContractInfosByDetail(projectId, responseType, timeType, advanceTimeType);
    }


}
