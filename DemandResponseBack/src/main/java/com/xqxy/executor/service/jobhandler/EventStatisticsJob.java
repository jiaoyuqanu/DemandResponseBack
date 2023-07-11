package com.xqxy.executor.service.jobhandler;

import cn.hutool.core.lang.Dict;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xqxy.core.client.SystemClient;
import com.xqxy.core.enums.DrSysDictDataEnum;
import com.xqxy.core.enums.PrvoinceOrgNoEnum;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.device.entity.SysOrgs;
import com.xqxy.dr.modular.dispatch.entity.OrgDemand;
import com.xqxy.dr.modular.dispatch.mapper.OrgDemandMapper;
import com.xqxy.dr.modular.event.mapper.PlanConsMapper;
import com.xqxy.dr.modular.statistics.entity.EventStatistics;
import com.xqxy.dr.modular.statistics.result.EventStatisticsResult;
import com.xqxy.dr.modular.statistics.service.EventStatisticsService;
import com.xqxy.sys.modular.cust.enums.OrgTitleEnum;
import com.xqxy.sys.modular.dict.param.DictTypeParam;
import com.xqxy.sys.modular.dict.service.DictTypeService;
import com.xqxy.sys.modular.utils.JSONObjectToEntityUtils;
import com.xqxy.sys.modular.utils.SnowflakeIdWorker;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.log4j.Log4j2;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author lqr
 * @Date 2022/10/24 15:02
 */

@Log4j2
@Component
public class EventStatisticsJob {

    @Resource
    private PlanConsMapper planConsMapper;

    @Resource
    private OrgDemandMapper orgDemandMapper;

    @Resource
    private DictTypeService dictTypeService;

    @Resource
    private SystemClient client;

    @Resource
    private EventStatisticsService eventStatisticsService;

    /**
     * 计算 事件执行效果
     * @param string
     * @return
     */
    @XxlJob("eventStatistics")
    public ReturnT<String> eventStatistics(String string){

        List<EventStatisticsResult> eventStatistics = planConsMapper.eventStatistics();
        if(!CollectionUtils.isEmpty(eventStatistics)){

            DictTypeParam dictTypeParam = new DictTypeParam();
            dictTypeParam.setCode(DrSysDictDataEnum.CURRENT_REGION.getCode());
            List<Dict> dicts = dictTypeService.dropDown(dictTypeParam);

            //查询省码
            String prvoinceOrgNo = "";
            for (Dict dict : dicts) {
                String value = dict.get("value").toString();
                String code = dict.get("code").toString();
                if(PrvoinceOrgNoEnum.PRVOINCE_ORG_NO_.getCode().equals(code)){
                    prvoinceOrgNo = value;
                }
            }

            List<SysOrgs> sysOrgsList = new ArrayList<>();
            JSONObject jsonObject2 = client.queryAllOrg();
            if("000000".equals(jsonObject2.getString("code"))){
                Object data = jsonObject2.get("data");
                JSONArray jsonArray = JSONArray.fromObject(data);
                for (Object object : jsonArray) {
                    SysOrgs sysOrg = JSONObjectToEntityUtils.JSONObjectToSysOrg(JSONObject.parseObject(object.toString()));
                    sysOrgsList.add(sysOrg);
                }
                String finalPrvoinceOrgNo = prvoinceOrgNo;
                //获取省市
                sysOrgsList = sysOrgsList.stream().filter(n -> OrgTitleEnum.CITY.getCode().equals(n.getOrgTitle()) || finalPrvoinceOrgNo.equals(n.getId())).collect(Collectors.toList());
            }

            //获取 省 市的所有下级单位集合
            Map<String,List<String>> map = new HashMap<>();
            for (SysOrgs sysOrg : sysOrgsList) {
                ResponseData<List<String>> responseData = client.getAllNextOrgId(sysOrg.getId());
                if(responseData != null){
                    List<String> data = responseData.getData();
                    map.put(sysOrg.getId(),data);
                }
            }

            List<EventStatistics>  eventStatisticsList = new ArrayList<>();
            // 查询 电力缺口 查询条件
            List<OrgDemand> orgDemandList = orgDemandMapper.queryOrgDemandByEventAndProject();
            Map<Long, List<OrgDemand>> listMap = orgDemandList.stream().collect(Collectors.groupingBy(OrgDemand::getEventId));
            for (Map.Entry<Long, List<OrgDemand>> entry : listMap.entrySet()) {
                Long eventId = entry.getKey();

                //聚合 省的 事件统计
                EventStatistics statisticsPrvoince = new EventStatistics();
                BigDecimal goalPrvoince = BigDecimal.ZERO;
                Integer consCountPrvoince = 0;
                BigDecimal actualCapPrvoince = BigDecimal.ZERO;
                BigDecimal actualEnergyPrvoince = BigDecimal.ZERO;

                for (OrgDemand orgDemand : entry.getValue()) {
                    String startTime = orgDemand.getStartTime();
                    String endTime = orgDemand.getEndTime();
                    if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime)){
                        Integer start = Integer.parseInt(startTime.substring(0, 2)) * 60 + Integer.parseInt(startTime.substring(3));
                        Integer end = Integer.parseInt(endTime.substring(0, 2)) * 60  + Integer.parseInt(endTime.substring(3));
                        Integer num = end - start;

                        String orgId = orgDemand.getOrgId();
                        if(!StringUtils.isEmpty(orgId)){
                            List<String> orgNoList = map.get(orgId);
                            if(!CollectionUtils.isEmpty(orgNoList)){
                                //获取 该组织机构 的 事件
                                List<EventStatisticsResult> resultList = eventStatistics.stream().filter(n -> eventId.equals(n.getEventId()) && orgNoList.contains(n.getOrgNo())).collect(Collectors.toList());
                                if(!CollectionUtils.isEmpty(resultList)){
                                    EventStatistics statistics = new EventStatistics();
                                    statistics.setOrgNo(orgId);

                                    //塞值户次
                                    statistics.setConsCount(resultList.size());
                                    consCountPrvoince = consCountPrvoince + resultList.size();

                                    statistics.setGoal(orgDemand.getGoal());
                                    if(orgDemand.getGoal() != null){
                                        goalPrvoince = goalPrvoince.add(orgDemand.getGoal());
                                    }

                                    //塞值 调度日期
                                    statistics.setRegulateDate(resultList.get(0).getRegulateDate());
                                    statisticsPrvoince.setRegulateDate(resultList.get(0).getRegulateDate());

                                    //塞值 事件id
                                    statistics.setEventId(eventId);
                                    statisticsPrvoince.setEventId(eventId);

                                    //塞值 项目id
                                    statistics.setProjectId(resultList.get(0).getProjectId());
                                    statisticsPrvoince.setProjectId(resultList.get(0).getProjectId());

                                    BigDecimal actualCapSum = BigDecimal.ZERO;
                                    BigDecimal actualEnergySum = BigDecimal.ZERO;
                                    for (EventStatisticsResult eventStatisticsResult : resultList) {
                                        BigDecimal actualCap = eventStatisticsResult.getActualCap();

                                        // 塞值实际响应负荷 (平均压降负荷 )单位：万千瓦
                                        if(actualCap != null){
                                            actualCapSum = actualCapSum.add(actualCap);
                                            actualCapPrvoince = actualCapPrvoince.add(actualCap);

                                            //要求 把无效的也加上
                                            actualEnergySum = actualEnergySum.add(actualCap.multiply(new BigDecimal(num)).divide(new BigDecimal(60),8, BigDecimal.ROUND_UP));
                                            actualEnergyPrvoince = actualEnergyPrvoince.add(actualCap.multiply(new BigDecimal(num)).divide(new BigDecimal(60),8, BigDecimal.ROUND_UP));
                                        }

                                        // 塞值日影响电量 单位：万千瓦
                                        BigDecimal actualEnergy = eventStatisticsResult.getActualEnergy();
                                        if(actualEnergy != null){
                                        }
                                    }
                                    //乘以事件执行时间 /60 /10000

                                    statistics.setActualCap(actualCapSum.divide(new BigDecimal(10000)));
                                    statistics.setActualEnergy(actualEnergySum.divide(new BigDecimal(10000)));
                                    eventStatisticsList.add(statistics);
                                }
                            }
                        }
                    }
                }

                statisticsPrvoince.setOrgNo(prvoinceOrgNo);
                statisticsPrvoince.setGoal(goalPrvoince);
                statisticsPrvoince.setConsCount(consCountPrvoince);
                statisticsPrvoince.setActualCap(actualCapPrvoince.divide(new BigDecimal(10000)));
                statisticsPrvoince.setActualEnergy(actualEnergyPrvoince.divide(new BigDecimal(10000)));
                eventStatisticsList.add(statisticsPrvoince);
            }

            if(!CollectionUtils.isEmpty(eventStatisticsList)){
                List<EventStatistics> statisticsList = eventStatisticsList.stream().filter(n -> n.getEventId() != null || n.getProjectId() != null).collect(Collectors.toList());
                if(!CollectionUtils.isEmpty(statisticsList)){
                    eventStatisticsService.remove(new LambdaQueryWrapper<>());
                    eventStatisticsService.saveBatch(statisticsList);
                }
            }
        }

        return ReturnT.SUCCESS;
    }
}
