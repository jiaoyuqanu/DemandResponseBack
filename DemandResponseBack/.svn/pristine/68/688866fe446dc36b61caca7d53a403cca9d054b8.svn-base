package com.xqxy.dr.modular.project.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.client.SystemClient;
import com.xqxy.core.client.SystemClientService;
import com.xqxy.core.enums.DrSysDictDataEnum;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.pojo.login.CurrenUserInfo;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.dr.modular.event.entity.Event;
import com.xqxy.dr.modular.project.VO.DrConsContractDetailsVO;
import com.xqxy.dr.modular.project.entity.*;
import com.xqxy.dr.modular.project.enums.DeclareProjectCheckEnums;
import com.xqxy.dr.modular.project.enums.DeclareProjectEnums;
import com.xqxy.dr.modular.project.enums.ParticipateTypeEnums;
import com.xqxy.dr.modular.project.mapper.CustContractInfoMapper;
import com.xqxy.dr.modular.project.params.ConsContractParam;
import com.xqxy.dr.modular.project.params.CustContractParam;
import com.xqxy.dr.modular.project.params.ProjectParam;
import com.xqxy.dr.modular.project.result.Region;
import com.xqxy.dr.modular.project.service.*;
import com.xqxy.sys.modular.cust.entity.Cons;
import com.xqxy.sys.modular.cust.entity.Cust;
import com.xqxy.sys.modular.cust.entity.UserConsRela;
import com.xqxy.sys.modular.cust.enums.IsAggregatorEnum;
import com.xqxy.sys.modular.cust.enums.OrgTitleEnum;
import com.xqxy.sys.modular.cust.service.ConsService;
import com.xqxy.sys.modular.cust.service.CustService;
import com.xqxy.sys.modular.cust.service.UserConsRelaService;
import com.xqxy.sys.modular.dict.param.DictTypeParam;
import com.xqxy.sys.modular.dict.service.DictTypeService;
import io.netty.util.internal.StringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 客户项目申报基本信息 服务实现类
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-13
 */
@Service
public class CustContractInfoServiceImpl extends ServiceImpl<CustContractInfoMapper, CustContractInfo> implements CustContractInfoService {

    @Resource
    private CustContractInfoMapper custContractInfoMapper;
    @Resource
    private SystemClient systemClient;
    @Resource
    private SystemClientService systemClientService;
    @Resource
    private DictTypeService dictTypeService;

    @Resource
    private UserConsRelaService userConsRelaService;

    @Resource
    private ConsContractInfoService consContractInfoService;

    @Resource
    private ConsContractDetailService consContractDetailService;

    @Resource
    private CustContractDetailService custContractDetailService;

    @Resource
    private ConsService consService;

    @Resource
    private CustContractInfoService custContractInfoService;

    @Resource
    private CustService custService;

    @Resource
    private SpareContractDeviceService spareContractDeviceService;

    @Resource
    private ProjectDetailService projectDetailService;

    @Override
    public List<CustContractInfo> listCustTractInfo(Event event) {
        JSONArray jsonArray = new JSONArray();
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


    /**
     * @description: 查询符合条件的 用户签约详情
     * @param:
     * @return:
     * @author: liqirui
     * @date: 2021/11/02 16:54
     */
    @Override
    public List<DrConsContractDetailsVO> queryDrCustContractDetails(Page<DrConsContractDetailsVO> page, ConsContractParam consContractParam) {
        List<Region> result = systemClientService.queryAll();
        Map<String, String> map = result.stream().collect(Collectors.toMap(Region::getId, Region::getName));

        List<DrConsContractDetailsVO> consList = new ArrayList<>();
        CurrenUserInfo currentUser = SecurityUtils.getCurrentUserInfoUTF8();
        if (currentUser != null) {
            String orgTitle = currentUser.getOrgTitle();
            if (orgTitle != null && orgTitle.equals(OrgTitleEnum.PROVINCE.getCode())) {
                consList = custContractInfoMapper.queryDrCustContractDetails(page, consContractParam);
            }
        }

        //请求字典接口
        DictTypeParam dictTypeParam = new DictTypeParam();
        dictTypeParam.setCode(DrSysDictDataEnum.CHECK_STATUS.getCode());

        List<Dict> dicts = dictTypeService.dropDown(dictTypeParam);

        for (DrConsContractDetailsVO cons : consList) {
            dicts.forEach(n -> {
                if (n.get("code").toString().equals(cons.getCheckStatus())) {
                    cons.setCheckStatusDesc(n.get("value").toString());
                }
            });

            String cityCodeDesc = map.get(cons.getCityCode()) == null ? cons.getCityCode() : map.get(cons.getCityCode());
            String countyCodeDesc = map.get(cons.getCountyCode()) == null ? cons.getCountyCode() : map.get(cons.getCountyCode());
            cons.setCityCodeDesc(cityCodeDesc);
            cons.setCountyCode(countyCodeDesc);
        }
        return consList;
    }


    /**
     * 查询客户签约详情导出
     *
     * @param consContractParam
     * @return
     */
    @Override
    public List<DrConsContractDetailsVO> exportDrConsDetails(ConsContractParam consContractParam) {
        List<Region> result = systemClientService.queryAll();
        Map<String, String> map = result.stream().collect(Collectors.toMap(Region::getId, Region::getName));
        List<DrConsContractDetailsVO> consList = custContractInfoMapper.exportDrConsDetails(consContractParam);

        //请求字典接口
        DictTypeParam dictTypeParam = new DictTypeParam();
        dictTypeParam.setCode(DrSysDictDataEnum.CHECK_STATUS.getCode());

        List<Dict> dicts = dictTypeService.dropDown(dictTypeParam);

        for (DrConsContractDetailsVO cons : consList) {
            dicts.forEach(n -> {
                if (n.get("code").toString().equals(cons.getCheckStatus())) {
                    cons.setCheckStatusDesc(n.get("value").toString());
                }
            });

            String cityCodeDesc = map.get(cons.getCityCode()) == null ? cons.getCityCode() : map.get(cons.getCityCode());
            String countyCodeDesc = map.get(cons.getCountyCode()) == null ? cons.getCountyCode() : map.get(cons.getCountyCode());
            cons.setCityCodeDesc(cityCodeDesc);
            cons.setCountyCode(countyCodeDesc);
        }
        return consList;
    }

    @Override
    public Page<CustContractInfo> pageDeclareProject(ProjectParam projectParam) {
        String projectName = Optional.ofNullable(projectParam.getProjectName()).orElse("");
        String custId = Objects.requireNonNull(SecurityUtils.getCurrentUserInfoUTF8()).getId();
        Cust cust = custService.getById(custId);

        List<String> consIds;
        Cust aggregator = custService.selectAggregator();
        Page<CustContractInfo> custContractInfoPage;
        if (null != cust && IsAggregatorEnum.AGGREGATOR.getCode().equals(cust.getIntegrator())) {
            // 集成商
            List<UserConsRela> userConsRela = userConsRelaService.list(Wrappers.<UserConsRela>lambdaQuery()
                    .eq(UserConsRela::getCustId, custId));
            consIds = userConsRela.stream().map(UserConsRela::getConsNo).collect(Collectors.toList());

            custContractInfoPage = getBaseMapper().pageDeclareProject(projectParam.getPage(), custId, projectName, projectParam.getConsId());
            custContractInfoPage.getRecords().forEach(custContractInfo -> {
                custContractInfo.setCountCons(consService.listByIds(consIds).size());
                LambdaQueryWrapper<ConsContractInfo> contractInfoLambdaQueryWrapper = Wrappers.lambdaQuery();
                if (CollectionUtils.isEmpty(consIds)) {
                    contractInfoLambdaQueryWrapper.in(ConsContractInfo::getConsId, -1);
                } else {
                    contractInfoLambdaQueryWrapper.in(ConsContractInfo::getConsId, consIds);
                }
                contractInfoLambdaQueryWrapper.eq(ConsContractInfo::getProjectId, custContractInfo.getProjectId())
                        .eq(ConsContractInfo::getStatus, DeclareProjectEnums.SIGNED.getCode());
                custContractInfo.setContractCons(consContractInfoService.list(contractInfoLambdaQueryWrapper).size());
            });
        } else if (ObjectUtil.isEmpty(aggregator)) {
            // 直接参与用户
            List<Cons> consList = consService.list(Wrappers.<Cons>lambdaQuery().eq(Cons::getCustId, custId));
            consIds = consList.stream().map(Cons::getId).collect(Collectors.toList());

            custContractInfoPage = getBaseMapper().pageDeclareProject(projectParam.getPage(), custId, projectName, projectParam.getConsId());
            custContractInfoPage.getRecords().forEach(custContractInfo -> {
                custContractInfo.setCountCons(consList.size());
                LambdaQueryWrapper<ConsContractInfo> contractInfoLambdaQueryWrapper = Wrappers.lambdaQuery();
                if (CollectionUtils.isEmpty(consIds)) {
                    contractInfoLambdaQueryWrapper.in(ConsContractInfo::getConsId, -1);
                } else {
                    contractInfoLambdaQueryWrapper.in(ConsContractInfo::getConsId, consIds);
                }
                contractInfoLambdaQueryWrapper.eq(ConsContractInfo::getProjectId, custContractInfo.getProjectId())
                        .eq(ConsContractInfo::getStatus, DeclareProjectEnums.SIGNED.getCode());
                custContractInfo.setContractCons(consContractInfoService.list(contractInfoLambdaQueryWrapper).size());
            });
        } else {
            // 代理参与用户
            List<Cons> consList = consService.list(Wrappers.<Cons>lambdaQuery().eq(Cons::getCustId, custId));
            consIds = consList.stream().map(Cons::getId).collect(Collectors.toList());

            custContractInfoPage = getBaseMapper().pageAgentContractProject(projectParam.getPage(),
                    aggregator.getId().toString(), projectName, custId);
            custContractInfoPage.getRecords().forEach(custContractInfo -> {
                custContractInfo.setCountCons(consList.size());
                LambdaQueryWrapper<ConsContractInfo> contractInfoLambdaQueryWrapper = Wrappers.lambdaQuery();
                if (CollectionUtils.isEmpty(consIds)) {
                    contractInfoLambdaQueryWrapper.in(ConsContractInfo::getConsId, -1);
                } else {
                    contractInfoLambdaQueryWrapper.in(ConsContractInfo::getConsId, consIds);
                }
                contractInfoLambdaQueryWrapper.eq(ConsContractInfo::getProjectId, custContractInfo.getProjectId())
                        .eq(ConsContractInfo::getStatus, DeclareProjectEnums.SIGNED.getCode());
                custContractInfo.setContractCons(consContractInfoService.list(contractInfoLambdaQueryWrapper).size());
            });
        }
        return custContractInfoPage;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteCustContract(CustContractParam custContractParam) {
        //查询客户签约
        CustContractInfo custContractInfo = getById(custContractParam.getContractId());

        if (custContractInfo != null) {
            Cust cust = custService.getCustById(custContractInfo.getCustId());
            if (ObjectUtil.isNotEmpty(cust)) {
                //客户 是否集成商   拿到直接参与用户 或者 负荷聚合商 对应的用户id 集合
                List<String> consIdListByCust = consService.getConsIdListByCust(cust.getId());
                if (CollectionUtils.isNotEmpty(consIdListByCust)) {
                    //根据项目id 和 关联用户id集合  查出关联 用户签约
                    LambdaQueryWrapper<ConsContractInfo> consContractInfoWrapper = new LambdaQueryWrapper<>();
                    consContractInfoWrapper.eq(ConsContractInfo::getProjectId, custContractInfo.getProjectId());
                    consContractInfoWrapper.in(ConsContractInfo::getConsId, consIdListByCust);
                    List<ConsContractInfo> consContractInfos = consContractInfoService.list(consContractInfoWrapper);

                    if (CollectionUtils.isNotEmpty(consContractInfos)) {
                        List<Long> consContractInfoIds = consContractInfos.stream().map(ConsContractInfo::getContractId).collect(Collectors.toList());
                        //根据用户签约id集合  查出关联 用户签约明细
                        LambdaQueryWrapper<ConsContractDetail> consContractDetailWrapper = new LambdaQueryWrapper<>();
                        consContractDetailWrapper.in(ConsContractDetail::getContractId, consContractInfoIds);
                        List<ConsContractDetail> consContractDetails = consContractDetailService.list(consContractDetailWrapper);

                        if (CollectionUtils.isNotEmpty(consContractDetails)) {
                            //用户签约明细id集合
                            List<Long> consContractDetailIds = consContractDetails.stream().map(ConsContractDetail::getDetailId).collect(Collectors.toList());
                            //用户签约明细  对应 的  项目明细id集合
                            List<Long> consContractDetailProjectDetailIds = consContractDetails.stream().map(ConsContractDetail::getProjectDetailId).collect(Collectors.toList());
                            //根据 用户id集合 和 consContractDetailProjectDetailIds删除签约设备
                            LambdaQueryWrapper<SpareContractDevice> spareContractDeviceWrapper = new LambdaQueryWrapper<>();
                            spareContractDeviceWrapper.in(SpareContractDevice::getConsId, consIdListByCust);
                            spareContractDeviceWrapper.in(SpareContractDevice::getProjectDetailId, consContractDetailProjectDetailIds);
                            spareContractDeviceService.remove(spareContractDeviceWrapper);

                            //删除用户签约明细集合
                            consContractDetailService.removeByIds(consContractDetailIds);
                        }
                        //删除用户签约集合
                        consContractInfoService.removeByIds(consContractInfoIds);
                    }

                    //查询项目对应项目明细
                    List<ProjectDetail> projectDetails = projectDetailService.listByProjectId(custContractInfo.getProjectId());
                    if (CollectionUtils.isNotEmpty(projectDetails)) {
                        List<Long> projectDetailIds = projectDetails.stream().map(ProjectDetail::getDetailId).collect(Collectors.toList());

                        //根据项目id 获取项目明细ids   通过项目明细ids和关联用户id 去签约设备表
                        LambdaQueryWrapper<SpareContractDevice> spareContractDeviceWrapper = new LambdaQueryWrapper<>();
                        spareContractDeviceWrapper.in(SpareContractDevice::getConsId, consIdListByCust);
                        spareContractDeviceWrapper.in(SpareContractDevice::getProjectDetailId, projectDetailIds);
                        spareContractDeviceService.remove(spareContractDeviceWrapper);
                    }
                }
            }
            //删除客户签约
            this.removeById(custContractInfo.getContractId());
        }
    }

    @Override
    public void deleteConsContract(ConsContractParam consContractParam) {
        ConsContractInfo consContractInfo = consContractInfoService.getById(consContractParam.getContractId());
        if (consContractInfo != null) {
            //查询所有 签约明细
            List<ConsContractDetail> consContractDetails = consContractDetailService.list(Wrappers.<ConsContractDetail>lambdaQuery().eq(ConsContractDetail::getContractId, consContractInfo.getContractId()));
            if (CollectionUtils.isNotEmpty(consContractDetails)) {
                List<Long> projectDetailIds = consContractDetails.stream().map(n -> n.getProjectDetailId()).collect(Collectors.toList());

                LambdaQueryWrapper<SpareContractDevice> spareQuery = new LambdaQueryWrapper<>();
                spareQuery.eq(SpareContractDevice::getConsId, consContractInfo.getConsId());
                spareQuery.in(SpareContractDevice::getProjectDetailId, projectDetailIds);
                spareContractDeviceService.remove(spareQuery);

                // 直接用户删除签约明细
                consContractDetailService.remove(Wrappers.<ConsContractDetail>lambdaQuery().eq(ConsContractDetail::getContractId, consContractInfo.getContractId()));
                if (consContractInfo.getParticType().equals(ParticipateTypeEnums.AGENT_PARTICIPATE.getCode())) {
                    // 代理参与删除集成商签约明细
                    custContractDetailService.remove(Wrappers.<CustContractDetail>lambdaQuery().eq(CustContractDetail::getContractId, consContractInfo.getContractId()));
                }
            }

            //查询项目对应项目明细
            List<ProjectDetail> projectDetails = projectDetailService.listByProjectId(consContractInfo.getProjectId());
            if (CollectionUtils.isNotEmpty(projectDetails)) {
                List<Long> projectDetailIds = projectDetails.stream().map(ProjectDetail::getDetailId).collect(Collectors.toList());

                //根据项目id 获取项目明细ids   通过项目明细ids和关联用户id 去签约设备表
                LambdaQueryWrapper<SpareContractDevice> spareContractDeviceWrapper = new LambdaQueryWrapper<>();
                spareContractDeviceWrapper.in(SpareContractDevice::getConsId, consContractInfo.getConsId());
                spareContractDeviceWrapper.in(SpareContractDevice::getProjectDetailId, projectDetailIds);
                spareContractDeviceService.remove(spareContractDeviceWrapper);
            }

            //删除 用户签约
            consContractInfoService.removeById(consContractInfo.getContractId());
        }
    }

    @Override
    public void deleteContractDetail(ConsContractParam consContractParam) {
        if (StringUtil.isNullOrEmpty(consContractParam.getContractDetailId())) {
            throw new ServiceException(-1, "该时段用户没有签约");
        }
        //查询 签约和 签约详情
        ConsContractDetail contractDetail = consContractDetailService.getById(consContractParam.getContractDetailId());
        ConsContractInfo consContractInfo = consContractInfoService.getById(contractDetail.getContractId());

        if (consContractParam.getType() == 1) {
            // 1.来自  响应负荷删除   删除本身 及 关联签约设备 删除
            consContractDetailService.removeById(consContractParam.getContractDetailId());
        } else {
            // 2.来自  签约容量删除
            //判断响应负荷  为空    删除本身 及 关联签约设备 删除
            // 不为空  修改本身 备用容量 空调容量 最小响应时长 （置空）关联签约设备 删除
            BigDecimal contractCap = contractDetail.getContractCap();
            if (contractCap == null) {
                consContractDetailService.removeById(consContractParam.getContractDetailId());
            } else {
                contractDetail.setSpareCap(BigDecimal.ZERO);
                contractDetail.setAirconditionCap(BigDecimal.ZERO);
                contractDetail.setSpareMinTimes(0);
                consContractDetailService.updateByDetailIdToNull(contractDetail);
            }
        }

        LambdaQueryWrapper<SpareContractDevice> spareQuery = new LambdaQueryWrapper<>();
        spareQuery.eq(SpareContractDevice::getConsId, consContractInfo.getConsId());
        spareQuery.in(SpareContractDevice::getProjectDetailId, contractDetail.getProjectDetailId());
        spareContractDeviceService.remove(spareQuery);
        if (consContractParam.getParticType().equals(ParticipateTypeEnums.AGENT_PARTICIPATE.getCode())) {
            custContractDetailService.removeById(consContractParam.getContractDetailId());
        }
    }

    @Transactional
    @Override
    public void recallSigning(Long contractId) {
        CustContractInfo custContractInfo = getById(contractId);
        // 撤销签约：修改客户以及用户待审核的记录为保存
        if (DeclareProjectCheckEnums.VERIFYING.getCode().equals(custContractInfo.getCheckStatus())) {
            ResponseData<?> responseData = systemClient.deleteBus(custContractInfo.getContractId().toString());
            if (ResponseData.SUCCESSFUL_CODE.equals(Objects.requireNonNull(responseData.getCode()))) {
                custContractInfo.setCheckStatus(DeclareProjectCheckEnums.UNSUBMIT.getCode());
                List<ConsContractInfo> consContractInfos = consContractInfoService.
                        listConsByCust(custContractInfo.getCustId().toString(), custContractInfo.getProjectId().toString());
                consContractInfos.forEach(consContractInfo -> {
                    if (DeclareProjectCheckEnums.VERIFYING.getCode().equals(consContractInfo.getCheckStatus())) {
                        consContractInfo.setCheckStatus(DeclareProjectCheckEnums.UNSUBMIT.getCode());
                    }
                });
                consContractInfoService.updateBatchById(consContractInfos);
                updateById(custContractInfo);
            } else {
                throw new ServiceException(-1, "代办事项不存在");
            }
        }
    }
}
