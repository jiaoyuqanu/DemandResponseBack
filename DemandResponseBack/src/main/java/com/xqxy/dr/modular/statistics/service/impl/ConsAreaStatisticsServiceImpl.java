package com.xqxy.dr.modular.statistics.service.impl;

import cn.hutool.core.lang.Dict;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.core.client.SystemClient;
import com.xqxy.core.client.SystemClientService;
import com.xqxy.core.enums.DrSysDictDataEnum;
import com.xqxy.core.enums.PrvoinceOrgNoEnum;
import com.xqxy.core.enums.RegionLevelEnum;
import com.xqxy.dr.modular.device.entity.SysOrgs;
import com.xqxy.dr.modular.dispatch.entity.OrgDemand;
import com.xqxy.dr.modular.dispatch.mapper.OrgDemandMapper;
import com.xqxy.dr.modular.dispatch.service.OrgDemandService;
import com.xqxy.dr.modular.event.enums.CheckStatusEnum;
import com.xqxy.dr.modular.event.mapper.PlanConsMapper;
import com.xqxy.dr.modular.project.mapper.ConsContractInfoMapper;
import com.xqxy.dr.modular.project.result.Region;
import com.xqxy.dr.modular.statistics.VO.ConsAreaStatisticsVO;
import com.xqxy.dr.modular.statistics.VO.ConsBigClassCodeVO;
import com.xqxy.dr.modular.statistics.entity.EventStatistics;
import com.xqxy.dr.modular.statistics.param.StatisticalParam;
import com.xqxy.dr.modular.statistics.result.EventStatisticsResult;
import com.xqxy.dr.modular.statistics.service.ConsAreaStatisticsService;
import com.xqxy.dr.modular.statistics.service.EventStatisticsService;
import com.xqxy.sys.modular.cust.entity.Cons;
import com.xqxy.sys.modular.cust.enums.ConsBigClassEnum;
import com.xqxy.sys.modular.cust.enums.OrgTitleEnum;
import com.xqxy.sys.modular.cust.mapper.ConsMapper;
import com.xqxy.sys.modular.cust.result.ConsStatisticsMonthCountResult;
import com.xqxy.sys.modular.cust.result.ConsStatisticsResult;
import com.xqxy.sys.modular.cust.result.StatisticsByTypeResult;
import com.xqxy.sys.modular.cust.service.ConsService;
import com.xqxy.sys.modular.dict.param.DictTypeParam;
import com.xqxy.sys.modular.dict.service.DictTypeService;
import com.xqxy.sys.modular.utils.JSONObjectToEntityUtils;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ConsAreaStatisticsServiceImpl implements ConsAreaStatisticsService {

    @Autowired
    private ConsService consService;

    @Resource
    private ConsMapper consMapper;

    @Resource
    private ConsContractInfoMapper consContractInfoMapper;

    @Autowired
    private SystemClientService systemClientService;

    @Resource
    private EventStatisticsService eventStatisticsService;

    /**
     * 用户的地区统计
     *
     * @param projectId
     * @return
     */
    @Override
    public Map<String, Integer> consAreaStatistics(Long projectId) {
        Map<String, Integer> map = new HashMap<>();

        //1.全省统计
        //查询 全省用户注册数
        List<Cons> consList = consService.list();
        map.put("consSum", consList.size());

        //查询（审核通过 ）用户数量  按照用户大类分组
        List<ConsBigClassCodeVO> contractConsList = consContractInfoMapper.groupConsBigClassCode(projectId, CheckStatusEnum.PASS_THE_AUDIT.getCode());
        if (!CollectionUtils.isEmpty(contractConsList)) {
            Integer contractSum = contractConsList.stream().mapToInt(ConsBigClassCodeVO::getConsCount).sum();
            map.put("contractSum", contractSum);

            //2.签约用户行业组成
            Map<String, Integer> contractConsMap = contractConsList.stream().collect(Collectors.toMap(ConsBigClassCodeVO::getBigTradeCode, ConsBigClassCodeVO::getConsCount));

            //工业用户
            map.put("industrialUsers", contractConsMap.get(ConsBigClassEnum.INDUSTRIAL_USERS.getCode()));
            //楼宇用户
            map.put("buildingUsers", contractConsMap.get(ConsBigClassEnum.BUILDING_USERS.getCode()));
            //居民用户
            map.put("residentUsers", contractConsMap.get(ConsBigClassEnum.RESIDENT_USER.getCode()));
            //新兴负荷用户
            map.put("emergingLoadUsers", contractConsMap.get(ConsBigClassEnum.EMERGING_LOAD_USERS.getCode()));
            //农林牧渔用户
            map.put("agricultureUsers", contractConsMap.get(ConsBigClassEnum.AGRICULTURE_USER.getCode()));
            //其他用户
            map.put("otherUsers", contractConsMap.get(ConsBigClassEnum.OTHER_USERS.getCode()));

        } else {
            //签约总数
            map.put("contractSum", 0);
            //工业用户
            map.put("industrialUsers", 0);
            //楼宇用户
            map.put("buildingUsers", 0);
            //居民用户
            map.put("residentUsers", 0);
            //新兴负荷用户
            map.put("emergingLoadUsers", 0);
            //农林牧渔用户
            map.put("agricultureUsers", 0);
            //其他用户
            map.put("otherUsers", 0);
        }

        //查询所有用户 按照用户大类 分组
        //（未签约 + 审核中 + 审核不通过） 用户数据
        List<ConsBigClassCodeVO> consBigClassList = consMapper.groupConsBigClassCode();
        if (!CollectionUtils.isEmpty(consBigClassList)) {
            Map<String, Integer> consMap = consBigClassList.stream().collect(Collectors.toMap(ConsBigClassCodeVO::getBigTradeCode, ConsBigClassCodeVO::getConsCount));

            //工业用户
            Integer industrialUsersSum = consMap.get(ConsBigClassEnum.INDUSTRIAL_USERS.getCode());
            Integer industrialUsers = map.get("industrialUsers");
            if (industrialUsers != null && industrialUsersSum != null) {
                map.put("noIndustrialUsers", industrialUsersSum - industrialUsers);
            } else {
                map.put("noIndustrialUsers", industrialUsersSum);
            }

            //楼宇用户
            Integer buildingUsersSum = consMap.get(ConsBigClassEnum.BUILDING_USERS.getCode());
            Integer buildingUsers = map.get("buildingUsers");
            if (buildingUsers != null && buildingUsersSum != null) {
                map.put("noBuildingUsers", buildingUsersSum - buildingUsers);
            } else {
                map.put("noBuildingUsers", buildingUsersSum);
            }

            //居民用户
            Integer residentUsersSum = consMap.get(ConsBigClassEnum.RESIDENT_USER.getCode());
            Integer residentUsers = map.get("residentUsers");
            if (residentUsers != null && residentUsersSum != null) {
                map.put("noResidentUsers", residentUsersSum - residentUsers);
            } else {
                map.put("noResidentUsers", residentUsersSum);
            }

            //新兴负荷用户
            Integer emergingLoadUsersSum = consMap.get(ConsBigClassEnum.EMERGING_LOAD_USERS.getCode());
            Integer emergingLoadUsers = map.get("emergingLoadUsers");
            if (emergingLoadUsers != null && emergingLoadUsersSum != null) {
                map.put("noEmergingLoadUsers", emergingLoadUsersSum - emergingLoadUsers);
            } else {
                map.put("noEmergingLoadUsers", emergingLoadUsersSum);
            }

            //农林牧渔用户
            Integer agricultureUsersSum = consMap.get(ConsBigClassEnum.AGRICULTURE_USER.getCode());
            Integer agricultureUsers = map.get("agricultureUsers");
            if (agricultureUsers != null && agricultureUsersSum != null) {
                map.put("noAgricultureUsers", agricultureUsersSum - agricultureUsers);
            } else {
                map.put("noAgricultureUsers", agricultureUsersSum);
            }

            //楼宇用户
            Integer otherUsersSum = consMap.get(ConsBigClassEnum.OTHER_USERS.getCode());
            Integer otherUsers = map.get("otherUsers");
            if (otherUsers != null && otherUsersSum != null) {
                map.put("noOtherUsers", otherUsersSum - otherUsers);
            } else {
                map.put("noOtherUsers", otherUsersSum);
            }

        } else {
            map.put("noIndustrialUsers", 0);
            map.put("noBuildingUsers", 0);
            map.put("noResidentUsers", 0);
            map.put("noEmergingLoadUsers", 0);
            map.put("noAgricultureUsers", 0);
            map.put("noOtherUsers", 0);
        }
        return map;
    }


    /**
     * 各地市用户类型统计 --- 签约数据
     *
     * @return
     */
    @Override
    public List<ConsStatisticsResult> consCityStatistics(Long projectId) {
        //返回
        List<ConsStatisticsResult> list = new ArrayList<>();
        //查询各地市 用户类型统计
        List<StatisticsByTypeResult> allList = consMapper.statisticsByType();

        //查询各地市通过审核 用户类型统计
        List<ConsStatisticsResult> contractConsList = consMapper.contractStatistics(projectId, CheckStatusEnum.PASS_THE_AUDIT.getCode());

        //查询所有市
        List<Region> regions = systemClientService.queryAll();
        if (!CollectionUtils.isEmpty(regions)) {
            regions = regions.stream().filter( n -> RegionLevelEnum.LEVEL_TWO.getCode().equals(n.getLevel())).collect(Collectors.toList());
            for (Region region : regions) {
                ConsStatisticsResult result = new ConsStatisticsResult();

                result.setRegionName(region.getName());
                result.setRegionCode(region.getId());
                // 注册数总计
                if (!CollectionUtils.isEmpty(allList)) {
                    List<StatisticsByTypeResult> filterList = allList.stream().filter(n -> region.getId().equals(n.getRegionCode())).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(filterList)) {
                        StatisticsByTypeResult filterResult = filterList.get(0);
                        Integer totalCount = filterResult.getTotalCount();
                        result.setTotalCount(totalCount);
                    } else {
                        result.setTotalCount(0);
                    }
                } else {
                    result.setTotalCount(0);
                }

                //签约数总计 签约率
                if (!CollectionUtils.isEmpty(contractConsList)) {
                    List<ConsStatisticsResult> filterList = contractConsList.stream().filter(n -> region.getId().equals(n.getRegionCode())).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(filterList)) {
                        ConsStatisticsResult filterResult = filterList.get(0);
                        Integer contractCount = filterResult.getTotalCount();
                        // 签约数 塞入
                        result.setContractCount(contractCount);

                        //工业用户
                        result.setIndustrialCount(filterResult.getIndustrialCount());
                        //居民用户
                        result.setResidentCount(filterResult.getResidentCount());
                        //楼宇用户
                        result.setBuildingCount(filterResult.getBuildingCount());
                        //新兴负荷用户
                        result.setEmergingLoadUser(filterResult.getEmergingLoadUser());
                        //农林牧渔用户
                        result.setAgriculCount(filterResult.getAgriculCount());
                        //其他用户
                        result.setOtherCount(filterResult.getOtherCount());

                        if (result.getTotalCount() != null && contractCount != null && 0 != result.getTotalCount()) {
                            //用户总注册数 为0  则签约比例也为 0
                            result.setContractRatio(new BigDecimal(contractCount).divide(new BigDecimal(result.getTotalCount()), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
                        }
                    } else {
                        //工业用户
                        result.setIndustrialCount(0);
                        //居民用户
                        result.setResidentCount(0);
                        //楼宇用户
                        result.setBuildingCount(0);
                        //新兴负荷用户
                        result.setEmergingLoadUser(0);
                        //农林牧渔用户
                        result.setAgriculCount(0);
                        //其他用户
                        result.setOtherCount(0);

                        result.setContractCount(0);
                        result.setContractRatio(BigDecimal.ZERO);
                    }
                } else {
                    //工业用户
                    result.setIndustrialCount(0);
                    //居民用户
                    result.setResidentCount(0);
                    //楼宇用户
                    result.setBuildingCount(0);
                    //新兴负荷用户
                    result.setEmergingLoadUser(0);
                    //农林牧渔用户
                    result.setAgriculCount(0);
                    //其他用户
                    result.setOtherCount(0);

                    result.setContractCount(0);
                    result.setContractRatio(BigDecimal.ZERO);
                }
                list.add(result);
            }
        }

        return list;
    }


    /**
     *  各地市用户类型统计 --- 所有 审核 数据
     * @return
     */
    @Override
    public List<ConsStatisticsResult> consCityStatisticsAll(Long projectId) {

        //返回
        List<ConsStatisticsResult> list = new ArrayList<>();
        //查询各地市 用户类型统计
        List<StatisticsByTypeResult> allList = consMapper.statisticsByType();

        //查询各地市通过审核 用户类型统计
        List<ConsStatisticsResult> contractConsList = consMapper.contractStatistics(projectId, CheckStatusEnum.PASS_THE_AUDIT.getCode());

        //查询所有市
        List<Region> regions = systemClientService.queryAll();
        if (!CollectionUtils.isEmpty(regions)) {
            regions = regions.stream().filter( n -> RegionLevelEnum.LEVEL_TWO.getCode().equals(n.getLevel())).collect(Collectors.toList());
            for (Region region : regions) {
                ConsStatisticsResult result = new ConsStatisticsResult();

                result.setRegionName(region.getName());
                result.setRegionCode(region.getId());
                // 注册数总计
                if (!CollectionUtils.isEmpty(allList)) {
                    List<StatisticsByTypeResult> filterList = allList.stream().filter(n -> region.getId().equals(n.getRegionCode())).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(filterList)) {
                        StatisticsByTypeResult filterResult = filterList.get(0);
                        Integer totalCount = filterResult.getTotalCount();
                        result.setTotalCount(totalCount);

                        //工业用户
                        result.setIndustrialCount(filterResult.getIndustrialCount());
                        //居民用户
                        result.setResidentCount(filterResult.getResidentCount());
                        //楼宇用户
                        result.setBuildingCount(filterResult.getBuildingCount());
                        //新兴负荷用户
                        result.setEmergingLoadUser(filterResult.getEmergingLoadUser());
                        //农林牧渔用户
                        result.setAgriculCount(filterResult.getAgriculCount());
                        //其他用户
                        result.setOtherCount(filterResult.getOtherCount());

                    } else {
                        result.setTotalCount(0);
                    }
                } else {
                    result.setTotalCount(0);
                }


                //签约数总计 签约率
                if (!CollectionUtils.isEmpty(contractConsList)) {
                    List<ConsStatisticsResult> filterList = contractConsList.stream().filter(n -> region.getId().equals(n.getRegionCode())).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(filterList)) {
                        ConsStatisticsResult filterResult = filterList.get(0);
                        Integer contractCount = filterResult.getTotalCount();
                        // 签约数 塞入
                        result.setContractCount(contractCount);
                        if (result.getTotalCount() != null && contractCount != null && 0 != result.getTotalCount()) {
                            //用户总注册数 为0  则签约比例也为 0
                            result.setContractRatio(new BigDecimal(contractCount).divide(new BigDecimal(result.getTotalCount()), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
                        }
                    } else {
                        result.setContractCount(0);
                        result.setContractRatio(BigDecimal.ZERO);
                    }
                } else {
                    result.setContractCount(0);
                    result.setContractRatio(BigDecimal.ZERO);
                }
                list.add(result);
            }
        }

        return list;
    }

    /**
     * 统计 每个月用户签约详情 数量
     * @param statisticalParam
     * @return
     */
    @Override
    public Map<String, List> consStatisticsMonthCount(StatisticalParam statisticalParam) {
        Map<String, List> map = new HashMap<>();
        List<ConsStatisticsMonthCountResult> list = consContractInfoMapper.consStatisticsMonthCount(statisticalParam);

        if(!CollectionUtils.isEmpty(list)){
            List<String> dateMonths = new ArrayList<>();
            List<Integer> counts = new ArrayList<>();
            for (ConsStatisticsMonthCountResult consStatisticsMonthCountResult : list) {
                dateMonths.add(consStatisticsMonthCountResult.getDateMonth());
                counts.add(consStatisticsMonthCountResult.getCount());
            }

            map.put("dateMonths",dateMonths);
            map.put("counts",counts);
        }
        return map;
    }

    /**
     * 统计 每个月用户签约详情 数量
     * @param statisticalParam
     * @return
     */
    @Override
    public Map<String, List> consStatisticsCapCount(StatisticalParam statisticalParam) {
        Map<String, List> map = new HashMap<>();
        List<ConsStatisticsMonthCountResult> list = consContractInfoMapper.consStatisticsCapCount(statisticalParam);

        if(!CollectionUtils.isEmpty(list)){
            List<String> dateMonths = new ArrayList<>();
            List<BigDecimal> contractCaps = new ArrayList<>();
            for (ConsStatisticsMonthCountResult consStatisticsMonthCountResult : list) {
                dateMonths.add(consStatisticsMonthCountResult.getDateMonth());
                contractCaps.add(consStatisticsMonthCountResult.getContractCapSum());
            }

            map.put("dateMonths",dateMonths);
            map.put("contractCaps",contractCaps);
        }
        return map;
    }
    
    /**
     * 统计 执行效果统计 事件执行统计
     */
    @Override
    public Page<EventStatistics> pageEventStatistics(StatisticalParam statisticalParam) {
        LambdaQueryWrapper<EventStatistics> queryWrapper = new LambdaQueryWrapper<>();
        if(statisticalParam != null){
            if(statisticalParam.getYear() != null){
                queryWrapper.like(EventStatistics::getRegulateDate,statisticalParam.getYear());
            }
            if(statisticalParam.getProjectId() != null){
                queryWrapper.like(EventStatistics::getProjectId,statisticalParam.getProjectId());
            }
            if(statisticalParam.getOrgNo() != null){
                queryWrapper.like(EventStatistics::getOrgNo,statisticalParam.getOrgNo());
            }
            if(statisticalParam.getRegulateDate() != null){
                queryWrapper.like(EventStatistics::getRegulateDate,statisticalParam.getRegulateDate());
            }
        }
        Page<EventStatistics> page = eventStatisticsService.page(statisticalParam.getPage(), queryWrapper);
        return page;
    }
}
