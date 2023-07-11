package com.xqxy.dr.modular.project.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.JsonObject;
import com.xqxy.core.client.SystemClient;
import com.xqxy.core.client.SystemClientService;
import com.xqxy.dr.modular.device.entity.SysOrgs;
import com.xqxy.dr.modular.device.service.SysOrgsService;
import com.xqxy.dr.modular.event.VO.EventVO;
import com.xqxy.dr.modular.event.entity.Event;
import com.xqxy.dr.modular.event.enums.CheckStatusEnum;
import com.xqxy.dr.modular.event.enums.EventStatusEnum;
import com.xqxy.dr.modular.event.mapper.EventMapper;
import com.xqxy.dr.modular.newloadmanagement.entity.Drevent;
import com.xqxy.dr.modular.project.VO.DrOrgGoalVO;
import com.xqxy.dr.modular.project.VO.EventDetailByProjectVO;
import com.xqxy.dr.modular.project.entity.ConsContractDetail;
import com.xqxy.dr.modular.project.entity.DrOrgGoal;
import com.xqxy.dr.modular.project.enums.AdvancenoticetimeEnum;
import com.xqxy.dr.modular.project.enums.ProjectTimeTypeEnum;
import com.xqxy.dr.modular.project.enums.ResponseTypeEnum;
import com.xqxy.dr.modular.project.mapper.ConsContractDetailMapper;
import com.xqxy.dr.modular.project.mapper.DrOrgGoalMapper;
import com.xqxy.dr.modular.project.service.ConsContractDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.dr.modular.project.service.DrOrgGoalService;
import com.xqxy.dr.modular.workbench.VO.ContractProjecttDetailVO;
import com.xqxy.dr.modular.workbench.param.WorkbenchParam;
import com.xqxy.sys.modular.cust.enums.OrgTitleEnum;
import com.xqxy.sys.modular.utils.JSONObjectToEntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 申报明细 服务实现类
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-13
 */
@Service
public class ConsContractDetailServiceImpl extends ServiceImpl<ConsContractDetailMapper, ConsContractDetail> implements ConsContractDetailService {

    @Resource
    private ConsContractDetailMapper consContractDetailMapper;

    @Resource
    private DrOrgGoalMapper orgGoalMapper;

    @Resource
    private EventMapper eventMapper;

    @Resource
    private SystemClientService client;
    /**
     * 工作台项目签约信息
     * @param workbenchParam
     * @return
     */
    @Override
    public  List<DrOrgGoal> contractProjecttDetail(WorkbenchParam workbenchParam) {
        List<SysOrgs> sysOrgsList = new ArrayList<>();
        List<DrOrgGoal> result = new ArrayList<>();

        JSONObject jsonObject2 = client.queryAllOrg();
        if("000000".equals(jsonObject2.getString("code"))){
            JSONArray data = jsonObject2.getJSONArray("data");
            for (Object ignored : data) {
                JSONObject jsonObject = (JSONObject) JSONObject.toJSON(ignored);
                SysOrgs sysOrgs = JSONObjectToEntityUtils.JSONObjectToSysOrg(jsonObject);

                sysOrgsList.add(sysOrgs);
            }
        }
        if(!CollectionUtils.isEmpty(sysOrgsList)){
            sysOrgsList = sysOrgsList.stream().filter(n -> OrgTitleEnum.CITY.getCode().equals(n.getOrgTitle())).collect(Collectors.toList());

            //只查询 审核通过的 和 审核中 状态
            workbenchParam.setCheckStatus(CheckStatusEnum.PASS_THE_AUDIT.getCode());
            workbenchParam.setUnderReview(CheckStatusEnum.UNDER_REVIEW.getCode());

            //查询对应项目 签约明细
            List<ContractProjecttDetailVO> list = consContractDetailMapper.contractProjecttDetail(workbenchParam);
            if(!CollectionUtils.isEmpty(list)){
                workbenchParam.setProjectDetailId(list.get(0).getProjectDetailId());


                //查询对应项目的 各单位指标
                List<DrOrgGoal> orgGoalList = orgGoalMapper.groupProjectIdAndOrgNo(workbenchParam);
                if(!CollectionUtils.isEmpty(sysOrgsList)) {
                    for (SysOrgs sysOrgs : sysOrgsList) {
                        DrOrgGoal drOrgGoal = new DrOrgGoal();
                        String orgNo = sysOrgs.getId();

                        if(!CollectionUtils.isEmpty(orgGoalList)){
                            // 查询此 项目详情对应 的 指标分解
                            List<DrOrgGoal> filterOrgGoalList = orgGoalList.stream().filter(n -> n.getOrgId().equals(orgNo)).collect(Collectors.toList());
                            if (!CollectionUtils.isEmpty(filterOrgGoalList)) {
                                drOrgGoal.setGoal(filterOrgGoalList.get(0).getGoal());
                            } else {
                                drOrgGoal.setGoal(BigDecimal.ZERO);
                            }

                            //查询 此orgNo 下属所有 作为条件是否包含
                            List<String> orgList = client.getAllNextOrgId(orgNo).getData();
                            if(!CollectionUtils.isEmpty(orgList)){
                                //拿到 用户签约容量 中用户已某个省市开头的数据
                                List<ContractProjecttDetailVO> filterContractProjecttDetail = list.stream().filter(n -> orgList.contains(n.getOrgNo()) && CheckStatusEnum.PASS_THE_AUDIT.getCode().equals(n.getCheckStatus())).collect(Collectors.toList());
                                if (!CollectionUtils.isEmpty(filterContractProjecttDetail)) {
                                    //获取 审核状态（已签约） 的 签约负荷
                                    BigDecimal decimal = BigDecimal.ZERO;
                                    for (ContractProjecttDetailVO contractProjecttDetailVO : filterContractProjecttDetail) {
                                        BigDecimal contractCap = contractProjecttDetailVO.getContractCap();
                                        if(contractCap != null){
                                            decimal = decimal.add(contractProjecttDetailVO.getContractCap());
                                        }
                                    }
                                    drOrgGoal.setContractCap(decimal.divide(new BigDecimal(10000)));
                                    if (drOrgGoal.getGoal() != null && drOrgGoal.getGoal().compareTo(BigDecimal.ZERO) > 0) {
                                        //比例 = 指标除以/签约容量(单位 千瓦)
                                        drOrgGoal.setContractRatio(drOrgGoal.getContractCap().divide(drOrgGoal.getGoal(), 4, BigDecimal.ROUND_HALF_UP));
                                    } else {
                                        drOrgGoal.setContractRatio(BigDecimal.ZERO);
                                    }
                                } else {
                                    drOrgGoal.setContractRatio(BigDecimal.ZERO);
                                    drOrgGoal.setContractCap(BigDecimal.ZERO);
                                }

                                //拿到 用户签约容量（审核中） 中用户的数据
                                List<ContractProjecttDetailVO> underReviewProjecttDetail = list.stream().filter(n -> orgList.contains(n.getOrgNo()) && CheckStatusEnum.UNDER_REVIEW.getCode().equals(n.getCheckStatus())).collect(Collectors.toList());
                                if (!CollectionUtils.isEmpty(underReviewProjecttDetail)) {
                                    //获取 审核状态（审核中） 的 签约负荷
                                    BigDecimal decimal = BigDecimal.ZERO;
                                    for (ContractProjecttDetailVO contractProjecttDetailVO : underReviewProjecttDetail) {
                                        BigDecimal contractCap = contractProjecttDetailVO.getContractCap();
                                        if(contractCap != null){
                                            decimal = decimal.add(contractProjecttDetailVO.getContractCap());
                                        }
                                    }
                                    drOrgGoal.setContractCapUnderReview(decimal.divide(new BigDecimal(10000)));
                                }else {
                                    drOrgGoal.setContractCapUnderReview(BigDecimal.ZERO);
                                }

                                //审核状态（已签约） 的 签约负荷 + 审核状态（审核中） 的 签约负荷
                                drOrgGoal.setContractCapSum(drOrgGoal.getContractCap().add(drOrgGoal.getContractCapUnderReview()));
                                drOrgGoal.setOrgId(sysOrgs.getId());
                                result.add(drOrgGoal);
                                result = result.stream().sorted(Comparator.comparing(DrOrgGoal::getOrgId)).collect(Collectors.toList());
                            }
                        }
                    }
                }
            }

        }
        return result;
    }


    /**
     * 事件统计详情
     * @param projectId
     * @return
     */
    @Override
    public Map<String, EventDetailByProjectVO> eventDetailByProject(Long projectId) {
        List<EventVO> eventList = eventMapper.eventDetailByProject(projectId);

        Map<String, EventDetailByProjectVO> map = new HashMap<>();
        if(!CollectionUtils.isEmpty(eventList)) {

            //得到所有 日前 约时 削峰 条件的事件
            List<EventVO> dayDesEvent = eventList.stream().filter(n ->
                                        AdvancenoticetimeEnum.DAY_BEFORE.getCode().equals(n.getAdvanceNoticeTime())
                                        && ResponseTypeEnum.DES.getCode().equals(n.getResponseType())
                                        && ProjectTimeTypeEnum.PRICE_TYPE.getCode().equals(n.getTimeType()))
                    .collect(Collectors.toList());
            EventDetailByProjectVO dayDesEventDetail = getEventDetailByProjectVO(dayDesEvent);
            map.put("dayDesEventDetail",dayDesEventDetail);

            // 小时级 约时 削峰
            List<EventVO> hourDesEvent = eventList.stream().filter(n ->
                                        AdvancenoticetimeEnum.HOUR_BEFORE.getCode().equals(n.getAdvanceNoticeTime())
                                        && ResponseTypeEnum.DES.getCode().equals(n.getResponseType())
                                        && ProjectTimeTypeEnum.PRICE_TYPE.getCode().equals(n.getTimeType()))
                    .collect(Collectors.toList());
            EventDetailByProjectVO hourDesEventDetail = getEventDetailByProjectVO(hourDesEvent);
            map.put("hourDesEventDetail",hourDesEventDetail);

            // 分钟级 约时 削峰
            List<EventVO> minuteDesEvent = eventList.stream().filter(n ->
                                        AdvancenoticetimeEnum.MINUTE_BEFORE.getCode().equals(n.getAdvanceNoticeTime())
                                        && ResponseTypeEnum.DES.getCode().equals(n.getResponseType())
                                        && ProjectTimeTypeEnum.PRICE_TYPE.getCode().equals(n.getTimeType()))
                    .collect(Collectors.toList());
            EventDetailByProjectVO minuteDesEventDetail = getEventDetailByProjectVO(minuteDesEvent);
            map.put("minuteDesEventDetail",minuteDesEventDetail);

            //  秒级 约时 削峰
            List<EventVO> secordDesEvent = eventList.stream().filter(n ->
                                        AdvancenoticetimeEnum.SECORD_BEFORE.getCode().equals(n.getAdvanceNoticeTime())
                                        && ResponseTypeEnum.DES.getCode().equals(n.getResponseType())
                                        && ProjectTimeTypeEnum.PRICE_TYPE.getCode().equals(n.getTimeType()))
                    .collect(Collectors.toList());
            EventDetailByProjectVO secordDesEventDetail = getEventDetailByProjectVO(secordDesEvent);
            map.put("secordDesEventDetail",secordDesEventDetail);

            // 约时 填谷
            List<EventVO> risEvent = eventList.stream().filter(n -> ResponseTypeEnum.RIS.getCode().equals(n.getResponseType())
                            && ProjectTimeTypeEnum.PRICE_TYPE.getCode().equals(n.getTimeType()))
                    .collect(Collectors.toList());
            EventDetailByProjectVO risEventDetail = getEventDetailByProjectVO(risEvent);
            map.put("risEventDetail",risEventDetail);

        }else {
            // 五个对象全部塞入次数 塞0次
            //得到所有 日前 约时 削峰 条件的事件
            EventDetailByProjectVO dayDesEventDetail = getEventDetailByProjectVO(null);
            map.put("dayDesEventDetail",dayDesEventDetail);

            // 小时级 约时 削峰
            EventDetailByProjectVO hourDesEventDetail = getEventDetailByProjectVO(null);
            map.put("hourDesEventDetail",hourDesEventDetail);

            // 分钟级 约时 削峰
            EventDetailByProjectVO minuteDesEventDetail = getEventDetailByProjectVO(null);
            map.put("minuteDesEventDetail",minuteDesEventDetail);

            //  秒级 约时 削峰
            EventDetailByProjectVO secordDesEventDetail = getEventDetailByProjectVO(null);
            map.put("secordDesEventDetail",secordDesEventDetail);

            // 约时 填谷
            EventDetailByProjectVO risEventDetail = getEventDetailByProjectVO(null);
            map.put("risEventDetail",risEventDetail);

        }

        return map;
    }

    /**
     * 通过 id 修改 备用容量 空调容量 最小响应时长 （置空）
     *
     * @param contractDetail
     */
    @Override
    public void updateByDetailIdToNull(ConsContractDetail contractDetail) {
        consContractDetailMapper.updateByDetailIdToNull(contractDetail);
    }

    @Override
    public BigDecimal getContractCapByCondition(Map<String, Object> map) {
        return consContractDetailMapper.getContractCapByCondition(map);
    }

    private EventDetailByProjectVO getEventDetailByProjectVO(List<EventVO> list){
        EventDetailByProjectVO eventDetail = new EventDetailByProjectVO();
        if (CollectionUtils.isEmpty(list)){
            //发起次数 塞入 0次
            eventDetail.setCount(0);
            //待执行 塞入 0次
            eventDetail.setReviewCount(0);
            //执行中 塞入 0次
            eventDetail.setPassCount(0);
            //已结束 塞入 0次
            eventDetail.setFailCount(0);
            //已终止 塞入 0次
            eventDetail.setThirteenCount(0);
        }else {
            //发起次数 累加该集合 所有count
            Integer sum = 0;
            for (EventVO eventVO : list) {
                if(eventVO.getCount() != null){
                    sum = sum + eventVO.getCount();
                }
            }
            eventDetail.setCount(sum);

            //待执行 塞入
            List<EventVO> reviewCountList = list.stream().filter(n -> EventStatusEnum.STATUS_REVIEW.getCode().equals(n.getEventStatus())).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(reviewCountList)){
                eventDetail.setReviewCount(0);
            }else {
                eventDetail.setReviewCount(reviewCountList.stream().collect(Collectors.summingInt(EventVO::getCount)));
            }
            //执行中 塞入
            List<EventVO> passCountList = list.stream().filter(n -> EventStatusEnum.STATUS_PASS.getCode().equals(n.getEventStatus())).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(passCountList)){
                eventDetail.setPassCount(0);
            }else {
                eventDetail.setPassCount(passCountList.stream().collect(Collectors.summingInt(EventVO::getCount)));
            }

            //已结束 塞入
            List<EventVO> failCountList = list.stream().filter(n -> EventStatusEnum.STATUS_FAIL.getCode().equals(n.getEventStatus())).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(failCountList)){
                eventDetail.setFailCount(0);
            }else {
                eventDetail.setFailCount(failCountList.stream().collect(Collectors.summingInt(EventVO::getCount)));
            }

            //已终止 塞入
            List<EventVO> thirteenCountList = list.stream().filter(n -> EventStatusEnum.STATUS_THIRTEEN.getCode().equals(n.getEventStatus())).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(thirteenCountList)){
                eventDetail.setThirteenCount(0);
            }else {
                eventDetail.setThirteenCount(thirteenCountList.stream().collect(Collectors.summingInt(EventVO::getCount)));
            }
        }

        return eventDetail;
    }
}
