package com.xqxy.dr.modular.event.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.client.SystemClient;
import com.xqxy.core.client.SystemClientService;
import com.xqxy.core.enums.YesOrNotEnum;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.pojo.login.CurrenUserInfo;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.core.util.OrganizationUtil;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.device.entity.SysOrgs;
import com.xqxy.dr.modular.evaluation.entity.ConsEvaluationAppeal;
import com.xqxy.dr.modular.evaluation.mapper.ConsEvaluationAppealMapper;
import com.xqxy.dr.modular.evaluation.service.ConsEvaluationAppealService;
import com.xqxy.dr.modular.event.entity.*;
import com.xqxy.dr.modular.event.enums.EventExceptionEnum;
import com.xqxy.dr.modular.event.enums.EventInvitationExceptionEnum;
import com.xqxy.dr.modular.event.enums.NoReplyReason;
import com.xqxy.dr.modular.event.enums.ParticipateTypeEnum;
import com.xqxy.dr.modular.event.mapper.ConsInvitationMapper;
import com.xqxy.dr.modular.event.mapper.PlanConsMapper;
import com.xqxy.dr.modular.event.param.ConsInvitationParam;
import com.xqxy.dr.modular.event.param.CustInvitationParam;
import com.xqxy.dr.modular.event.param.EventParam;
import com.xqxy.dr.modular.event.param.MyRepresentation;
import com.xqxy.dr.modular.event.result.EventInvitationResult;
import com.xqxy.dr.modular.event.service.ConsInvitationService;
import com.xqxy.dr.modular.event.service.CustInvitationService;
import com.xqxy.dr.modular.event.service.EventService;
import com.xqxy.dr.modular.project.entity.ConsContractDetail;
import com.xqxy.dr.modular.project.entity.ConsContractInfo;
import com.xqxy.dr.modular.project.result.Region;
import com.xqxy.dr.modular.project.service.ConsContractDetailService;
import com.xqxy.dr.modular.project.service.ConsContractInfoService;
import com.xqxy.dr.modular.subsidy.entity.SubsidyAppeal;
import com.xqxy.dr.modular.subsidy.param.SubsidyAppealParam;
import com.xqxy.dr.modular.subsidy.service.SubsidyAppealService;
import com.xqxy.sys.modular.cust.entity.Cons;
import com.xqxy.sys.modular.cust.entity.CustCertifyFile;
import com.xqxy.sys.modular.cust.result.Result;
import com.xqxy.sys.modular.cust.service.ConsService;
import com.xqxy.sys.modular.cust.service.CustCertifyFileService;
import com.xqxy.sys.modular.utils.JSONObjectToEntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * <p>
 * 用户邀约 服务实现类
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-09
 */
@Service
public class ConsInvitationServiceImpl extends ServiceImpl<ConsInvitationMapper, ConsInvitation> implements ConsInvitationService {
    @Resource
    EventService eventService;

    @Resource
    PlanConsMapper planConsMapper;

    @Resource
    ConsService consService;

    @Resource
    SystemClientService systemClientService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    CustInvitationService custInvitationService;

    @Resource
    ConsContractInfoService consContractInfoService;

    @Resource
    ConsContractDetailService consContractDetailService;

    @Resource
    private SystemClient systemClient;

    @Resource
    private SubsidyAppealService subsidyAppealService;

    @Resource
    private ConsEvaluationAppealService consEvaluationAppealService;

    @Resource
    private ConsEvaluationAppealMapper consEvaluationAppealMapper;

    @Resource
    CustCertifyFileService custCertifyFileService;


    @Override
    public void delete(ConsInvitationParam consInvitationParam) {
        LambdaQueryWrapper<ConsInvitation> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(consInvitationParam)) {
            // 根据事件ID查询
            if (ObjectUtil.isNotEmpty(consInvitationParam.getEventId())) {
                queryWrapper.eq(ConsInvitation::getEventId, consInvitationParam.getEventId());
            }

            // 根据用户ID查询
            if (ObjectUtil.isNotEmpty(consInvitationParam.getConsId())) {
                queryWrapper.eq(ConsInvitation::getConsId, consInvitationParam.getConsId());
            }

            // 根据邀约轮次查询
            if (ObjectUtil.isNotEmpty(consInvitationParam.getInvitationRound())) {
                queryWrapper.eq(ConsInvitation::getInvitationRound, consInvitationParam.getInvitationRound());
            }
        }
        this.remove(queryWrapper);
    }

    @Override
    public List<ConsInvitation> list(ConsInvitationParam consInvitationParam) {

        LambdaQueryWrapper<ConsInvitation> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(consInvitationParam)) {
            // 根据事件ID查询
            if (ObjectUtil.isNotEmpty(consInvitationParam.getEventId())) {
                queryWrapper.eq(ConsInvitation::getEventId, consInvitationParam.getEventId());
            }

            // 根据用户ID查询
            if (ObjectUtil.isNotEmpty(consInvitationParam.getConsId())) {
                queryWrapper.eq(ConsInvitation::getConsId, consInvitationParam.getConsId());
            }

            // 查询不包含自身的
            if (ObjectUtil.isNotEmpty(consInvitationParam.getConsNo())) {
                queryWrapper.ne(ConsInvitation::getConsId, consInvitationParam.getConsId());
            }
            // 根据用户ID集合查询
            if (ObjectUtil.isNotEmpty(consInvitationParam.getConsIdList())) {
                queryWrapper.in(ConsInvitation::getConsId, consInvitationParam.getConsIdList());
            }

            // 是否参与
            if (ObjectUtil.isNotEmpty(consInvitationParam.getIsParticipate())) {
                queryWrapper.eq(ConsInvitation::getIsParticipate, consInvitationParam.getIsParticipate());
            }

        }

        //根据排序升序排列，序号越小越在前
        queryWrapper.orderByAsc(ConsInvitation::getCreateTime);
        return this.list(queryWrapper);
    }

    @Override
    public Page<Event> invitationPage(ConsInvitationParam consInvitationParam) {
        if ("WFK".equals(consInvitationParam.getInvitationStatus())) {
            return new Page<>();
        }

        CurrenUserInfo currenUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if (currenUserInfo != null) {
            String orgId = currenUserInfo.getOrgId();
            List<String> orgNos = systemClientService.getAllNextOrgId(orgId).getData();
            consInvitationParam.setOrgs(orgNos);
        }

        List<String> consIdList = consService.getConsIdByCust();
        if (CollectionUtil.isEmpty(consIdList) || consIdList.size() == 0) {
            Page<Event> eventPage = new Page<Event>();
            return eventPage;
        }
        consInvitationParam.setConsIdList(consIdList);
        Page<Event> eventPage = planConsMapper.getMyResponsePage(consInvitationParam.getPage(), consInvitationParam);
        List<Event> records = eventPage.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            for (Event event : records) {
                JSONArray jsonArray = new JSONArray();
                List<List<String>> regulateRange = new ArrayList<>();
                String regulateRangeStr = "";
                if (null != event) {
                    if (null != event.getRegulateCap()) {
                        event.setRegulateCap(NumberUtil.div(event.getRegulateCap(), 10000));
                    }
                    String rangeType = event.getRangeType();
                    if (null != rangeType && "1".equals(rangeType)) {
                        List<Region> regions = systemClientService.queryAll();
                        if (null != event.getRegulateRange()) {
                            jsonArray = JSONArray.parseArray(event.getRegulateRange());
                            if (null != jsonArray && jsonArray.size() > 0) {
                                for (int j = 0; j < jsonArray.size(); j++) {
                                    JSONArray value = jsonArray.getJSONArray(j);
                                    List<String> list = new ArrayList<>();
                                    if (null != value && value.size() > 0) {
                                        for (int i = 0; i < value.size(); i++) {
                                            list.add(value.get(i).toString());
                                            String code = value.get(i).toString();
                                            Optional<Region> optionalRegion = regions.stream().filter(item -> item.getId().equals(code)).findAny();
                                            if (optionalRegion.isPresent()) {
                                                Region region = optionalRegion.get();
                                                if (i == value.size() - 1) {
                                                    if (null != region && null != region.getName()) {
                                                        regulateRangeStr += region.getName() + "|";
                                                    } else {
                                                        regulateRangeStr = regulateRangeStr.substring(0, regulateRangeStr.length() - 1);
                                                        regulateRangeStr += "|";
                                                    }
                                                } else {
                                                    if (null != region && null != region.getName()) {
                                                        regulateRangeStr += region.getName() + ",";
                                                    }
                                                }
                                            }

                                        }
                                        regulateRange.add(list);
                                    }
                                }
                            }
                        }
                    } else if (null != rangeType && "2".equals(rangeType)) {
                        Result result = systemClientService.getAllOrgs();
                        JSONObject jsonObject = null;
                        if (null != result) {
                            jsonObject = result.getData();
                        }
                        if (null != event.getRegulateRange()) {
                            jsonArray = JSONArray.parseArray(event.getRegulateRange());
                            if (null != jsonArray && jsonArray.size() > 0) {
                                for (int j = 0; j < jsonArray.size(); j++) {
                                    JSONArray value = jsonArray.getJSONArray(j);
                                    List<String> list = new ArrayList<>();
                                    if (null != value && value.size() > 0) {
                                        for (int i = 0; i < value.size(); i++) {
                                            list.add(value.get(i).toString());
                                            if (jsonObject.containsKey(value.get(i).toString())) {
                                                Object data = jsonObject.get(value.get(i).toString());
                                                JSONObject datas = (JSONObject) JSON.toJSON(data);
                                                if (i == value.size() - 1) {
                                                    if (null != datas.get("name")) {
                                                        regulateRangeStr += datas.get("name") + "|";
                                                    } else {
                                                        regulateRangeStr = regulateRangeStr.substring(0, regulateRangeStr.length() - 1);
                                                        regulateRangeStr += "|";
                                                    }
                                                } else {
                                                    if (null != datas.get("name")) {
                                                        regulateRangeStr += datas.get("name") + ",";
                                                    }
                                                }
                                            }
                                        }
                                        regulateRange.add(list);
                                    }
                                }
                            }
                        }
                    }
                }
                if (!StringUtils.isEmpty(regulateRangeStr)) {
                    event.setRegulateRangeStr(regulateRangeStr.substring(0, regulateRangeStr.length() - 1));
                }
            }
            eventPage.setRecords(records);
        }

        return eventPage;
    }


    @Override
    public Page<Event> myRepresentationPage(ConsInvitationParam consInvitationParam) {
        if ("WFK".equals(consInvitationParam.getInvitationStatus())) {
            return new Page<>();
        }

        CurrenUserInfo currenUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if (currenUserInfo != null) {
            String orgId = currenUserInfo.getOrgId();
            List<String> orgNos = systemClientService.getAllNextOrgId(orgId).getData();
            consInvitationParam.setOrgs(orgNos);
        }

        if (!StringUtils.isEmpty(consInvitationParam.getDistinguish())
                && consInvitationParam.getDistinguish().equals("user")) {
            //根据当前登录账号获取用户户号
            List<String> consIdList = consService.getConsIdByCust();
            if (CollectionUtil.isEmpty(consIdList) || consIdList.size() == 0) {
                Page<Event> eventPage = new Page<Event>();
                return eventPage;
            }
            consInvitationParam.setConsIdList(consIdList);
        }

        Page<Event> eventPage = planConsMapper.getMyRepresentationPage(consInvitationParam.getPage(), consInvitationParam);

        List<Event> records = eventPage.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            if (!StringUtils.isEmpty(consInvitationParam.getDistinguish())
                    && consInvitationParam.getDistinguish().equals("user")) {
                //用户侧——判断当前状态是否可撤回
                records.forEach(entity -> {
                    entity.setWhetherToWithdraw("false");
                    if (!StringUtils.isEmpty(entity.getStatus())
                            && entity.getStatus().equals("2")) {
                        entity.setWhetherToWithdraw("true");
                    }
                });
            }

            if (!StringUtils.isEmpty(consInvitationParam.getDistinguish())
                    && consInvitationParam.getDistinguish().equals("province")) {
                //省侧按钮
                records.forEach(entity -> {
                    entity.setIfDeaths("false");
                    entity.setIfTurnDown("false");
                    if (!StringUtils.isEmpty(entity.getStatusProvince())
                            && entity.getStatusProvince().equals("1")) {
                        entity.setIfDeaths("true");
                    }

                    if (!StringUtils.isEmpty(entity.getStatusProvince())
                            && entity.getStatusProvince().equals("1")) {
                        entity.setIfTurnDown("true");
                    }
                });
            }


            if (!StringUtils.isEmpty(consInvitationParam.getDistinguish())
                    && consInvitationParam.getDistinguish().equals("energy")) {
                //能源局侧按钮
                records.forEach(entity -> {
                    entity.setIfDeaths("false");
                    entity.setIfTurnDown("false");
                    if (!StringUtils.isEmpty(entity.getStatusEnergy())
                            && entity.getStatusEnergy().equals("1")) {
                        entity.setIfDeaths("true");
                    }
                    if (!StringUtils.isEmpty(entity.getStatusEnergy())
                            && entity.getStatusEnergy().equals("1")) {
                        entity.setIfTurnDown("true");
                    }
                });
            }


            for (Event event : records) {
                JSONArray jsonArray = new JSONArray();
                List<List<String>> regulateRange = new ArrayList<>();
                String regulateRangeStr = "";
                if (null != event) {
                    if (null != event.getRegulateCap()) {
                        event.setRegulateCap(NumberUtil.div(event.getRegulateCap(), 10000));
                    }
                    String rangeType = event.getRangeType();
                    if (null != rangeType && "1".equals(rangeType)) {
                        List<Region> regions = systemClientService.queryAll();
                        if (null != event.getRegulateRange()) {
                            jsonArray = JSONArray.parseArray(event.getRegulateRange());
                            if (null != jsonArray && jsonArray.size() > 0) {
                                for (int j = 0; j < jsonArray.size(); j++) {
                                    JSONArray value = jsonArray.getJSONArray(j);
                                    List<String> list = new ArrayList<>();
                                    if (null != value && value.size() > 0) {
                                        for (int i = 0; i < value.size(); i++) {
                                            list.add(value.get(i).toString());
                                            String code = value.get(i).toString();
                                            Optional<Region> optionalRegion = regions.stream().filter(item -> item.getId().equals(code)).findAny();
                                            if (optionalRegion.isPresent()) {
                                                Region region = optionalRegion.get();
                                                if (i == value.size() - 1) {
                                                    if (null != region && null != region.getName()) {
                                                        regulateRangeStr += region.getName() + "|";
                                                    } else {
                                                        regulateRangeStr = regulateRangeStr.substring(0, regulateRangeStr.length() - 1);
                                                        regulateRangeStr += "|";
                                                    }
                                                } else {
                                                    if (null != region && null != region.getName()) {
                                                        regulateRangeStr += region.getName() + ",";
                                                    }
                                                }
                                            }

                                        }
                                        regulateRange.add(list);
                                    }
                                }
                            }
                        }
                    } else if (null != rangeType && "2".equals(rangeType)) {
                        Result result = systemClientService.getAllOrgs();
                        JSONObject jsonObject = null;
                        if (null != result) {
                            jsonObject = result.getData();
                        }
                        if (null != event.getRegulateRange()) {
                            jsonArray = JSONArray.parseArray(event.getRegulateRange());
                            if (null != jsonArray && jsonArray.size() > 0) {
                                for (int j = 0; j < jsonArray.size(); j++) {
                                    JSONArray value = jsonArray.getJSONArray(j);
                                    List<String> list = new ArrayList<>();
                                    if (null != value && value.size() > 0) {
                                        for (int i = 0; i < value.size(); i++) {
                                            list.add(value.get(i).toString());
                                            if (jsonObject.containsKey(value.get(i).toString())) {
                                                Object data = jsonObject.get(value.get(i).toString());
                                                JSONObject datas = (JSONObject) JSON.toJSON(data);
                                                if (i == value.size() - 1) {
                                                    if (null != datas.get("name")) {
                                                        regulateRangeStr += datas.get("name") + "|";
                                                    } else {
                                                        regulateRangeStr = regulateRangeStr.substring(0, regulateRangeStr.length() - 1);
                                                        regulateRangeStr += "|";
                                                    }
                                                } else {
                                                    if (null != datas.get("name")) {
                                                        regulateRangeStr += datas.get("name") + ",";
                                                    }
                                                }
                                            }
                                        }
                                        regulateRange.add(list);
                                    }
                                }
                            }
                        }
                    }
                }
                if (!StringUtils.isEmpty(regulateRangeStr)) {
                    event.setRegulateRangeStr(regulateRangeStr.substring(0, regulateRangeStr.length() - 1));
                }
            }
            eventPage.setRecords(records);
        }

        return eventPage;
    }

    @Override
    public ConsEvaluationAppeal getMyRepresentationById(MyRepresentation consInvitationParam) {
        ConsEvaluationAppeal entity = new ConsEvaluationAppeal();
        //查询dr_subsidy_appeal表中数据
        entity = planConsMapper.getMyRepresentationById(consInvitationParam);
        if (!ObjectUtils.isEmpty(entity)) {
            LambdaQueryWrapper<ConsEvaluationAppeal> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ConsEvaluationAppeal::getSubsidyAppealId, consInvitationParam.getDrSubsidyAppealId());
            List<ConsEvaluationAppeal> objects = consEvaluationAppealMapper.selectList(queryWrapper);
            if (objects.size() > 0) {
                ConsEvaluationAppeal consEvaluationAppeal = objects.get(0);
                if (StringUtils.isEmpty(consEvaluationAppeal.getMaxLoadBaseline())) {
                    entity.setMaxLoadBaseline(BigDecimal.ZERO);
                } else {
                    entity.setMaxLoadBaseline(consEvaluationAppeal.getMaxLoadBaseline());
                }

                if (StringUtils.isEmpty(consEvaluationAppeal.getMaxLoadActual())) {
                    entity.setMaxLoadActual(BigDecimal.ZERO);
                } else {
                    entity.setMaxLoadActual(consEvaluationAppeal.getMaxLoadActual());
                }

                if (StringUtils.isEmpty(consEvaluationAppeal.getAvgLoadBaseline())) {
                    entity.setAvgLoadBaseline(BigDecimal.ZERO);
                } else {
                    entity.setAvgLoadBaseline(consEvaluationAppeal.getAvgLoadBaseline());
                }
                if (StringUtils.isEmpty(consEvaluationAppeal.getAvgLoadActual())) {
                    entity.setAvgLoadActual(BigDecimal.ZERO);
                } else {
                    entity.setAvgLoadActual(consEvaluationAppeal.getAvgLoadActual());
                }
                if (StringUtils.isEmpty(consEvaluationAppeal.getConfirmCap())) {
                    entity.setConfirmCap(BigDecimal.ZERO);
                } else {
                    entity.setConfirmCap(consEvaluationAppeal.getConfirmCap());
                }
                if (StringUtils.isEmpty(consEvaluationAppeal.getMinLoadBaseline())) {
                    entity.setMinLoadBaseline(BigDecimal.ZERO);
                } else {
                    entity.setMinLoadBaseline(consEvaluationAppeal.getMinLoadBaseline());
                }
                if (StringUtils.isEmpty(consEvaluationAppeal.getMinLoadActual())) {
                    entity.setMinLoadActual(BigDecimal.ZERO);
                } else {
                    entity.setMinLoadActual(consEvaluationAppeal.getMinLoadActual());
                }
                if (StringUtils.isEmpty(consEvaluationAppeal.getIsEffective())) {
                    entity.setIsEffective("");
                } else {
                    entity.setIsEffective(consEvaluationAppeal.getIsEffective());
                }

                if (StringUtils.isEmpty(consEvaluationAppeal.getInvitationCap())) {
                    entity.setInvitationCap(BigDecimal.ZERO);
                } else {
                    entity.setInvitationCap(consEvaluationAppeal.getInvitationCap());
                }

                if (StringUtils.isEmpty(consEvaluationAppeal.getActualCap())) {
                    entity.setActualCap(BigDecimal.ZERO);
                } else {
                    entity.setActualCap(consEvaluationAppeal.getActualCap());
                }

            }
            if (StringUtils.isEmpty(entity.getConsName())) {
                entity.setAcceptingCompany("");
            } else {
                entity.setAcceptingCompany(entity.getConsName());
            }

            //根据状态查询流程终止意见
            if (!StringUtils.isEmpty(entity.getStatusProvince()) && entity.getStatusProvince().equals("3")) {
                if (!StringUtils.isEmpty(entity.getStatusEnergy()) && entity.getStatusEnergy().equals("2")) {
                    if (!StringUtils.isEmpty(entity.getExamineSuggestionEnergy())) {
                        entity.setReasonOfFailure(entity.getExamineSuggestionEnergy());
                    }
                } else {
                    if (!StringUtils.isEmpty(entity.getExamineSuggestionProvince())) {
                        entity.setReasonOfFailure(entity.getExamineSuggestionProvince());
                    }
                }
            }
        }

        return entity;
    }

    @Transactional
    @Override
    public void updateMyRepresentationById(MyRepresentation myRepresentation) {
        SubsidyAppealParam subsidyAppealParam = new SubsidyAppealParam();
        subsidyAppealParam.setId(Long.valueOf(myRepresentation.getDrSubsidyAppealId()));
        subsidyAppealParam.setFileIds(myRepresentation.getFileIds());
        subsidyAppealParam.setFilesName(myRepresentation.getFilesName());
        subsidyAppealParam.setCheckIds(myRepresentation.getCheckIds());
        subsidyAppealParam.setCheckName(myRepresentation.getCheckName());
        subsidyAppealParam.setRemark(myRepresentation.getRemark());
        subsidyAppealParam.setAppealReason(myRepresentation.getAppealReason());
        subsidyAppealService.update(subsidyAppealParam);
    }


    @Override
    public List<ConsInvitation> invitationList(Long eventId) {
        List<String> consIdList = consService.getConsIdByCust();
        ConsInvitationParam consInvitationParam = new ConsInvitationParam();
        consInvitationParam.setEventId(eventId);
        consInvitationParam.setConsIdList(consIdList);
        return this.list(consInvitationParam);
    }


    @Override
    public Page<ConsInvitation> page(ConsInvitationParam consInvitationParam) {
        //数据权限
        CurrenUserInfo currenUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if (null == currenUserInfo) {
            return new Page<>();
        }
        String orgNo = currenUserInfo.getOrgId();
        String orgTitle = currenUserInfo.getOrgTitle();
        //机构子集
        List<String> list = new ArrayList<>();
        if (null != orgNo && !"".equals(orgNo)) {
            if (!"1".equals(orgTitle)) {
                //获取所有组织机构集合
                list = OrganizationUtil.getAllOrgByOrgNo();
                if (CollectionUtil.isEmpty(list)) {
                    return new Page<>();
                }
            }
        } else {
            return new Page<>();
        }
        QueryWrapper<Object> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotNull(consInvitationParam)) {
            // 根据事件ID模糊查询
            if (ObjectUtil.isNotEmpty(consInvitationParam.getEventId())) {
                queryWrapper.eq("inv.event_id", consInvitationParam.getEventId());
            }
            // 是否参与
            if (ObjectUtil.isNotEmpty(consInvitationParam.getIsParticipate())) {
                queryWrapper.eq("inv.is_participate", consInvitationParam.getIsParticipate());
            }
            //未反馈原因
            if (ObjectUtil.isNotEmpty(consInvitationParam.getNoReplyReason())) {
                queryWrapper.eq("inv.no_reply_reason", consInvitationParam.getNoReplyReason());
            }
            if (ObjectUtil.isNotEmpty(consInvitationParam.getIsReply())) {
                queryWrapper.eq("inv.is_reply", consInvitationParam.getIsReply());
            }
            // 用户名
            if (ObjectUtil.isNotNull(consInvitationParam.getConsName()) && !"".equals(consInvitationParam.getConsName())) {
                queryWrapper.like("c.cons_name", consInvitationParam.getConsName());
            }

            // 营销户号(需求响应2.0中，consId就是营销户号)
            if (ObjectUtil.isNotNull(consInvitationParam.getConsId()) && !"".equals(consInvitationParam.getConsId())) {
                queryWrapper.like("c.id", consInvitationParam.getConsId());
            }
            // 机构等级
            if (ObjectUtil.isNotEmpty(list)) {
                queryWrapper.in("c.ORG_NO", list);
            }
            //市级查询所有用户，省级查询直接用户
            if (ObjectUtil.isNotNull(orgTitle) && "1".equals(orgTitle)) {
                queryWrapper.eq("inv.join_user_type", "1");
            }
            //事件发布以后才可看反馈数据
            List<String> status = new ArrayList<>();
            status.add("06");
            status.add("03");
            status.add("04");
            queryWrapper.in("e.event_status", status);
         /*   // 省码
            if (ObjectUtil.isNotNull(eventInvitationParam.getProvinceCode()) && !"".equals(eventInvitationParam.getProvinceCode())) {
                queryWrapper.eq("dcd.province_code", eventInvitationParam.getProvinceCode());
            }
            // 市码
            if (ObjectUtil.isNotNull(eventInvitationParam.getCityCode()) && !"".equals(eventInvitationParam.getCityCode())) {
                queryWrapper.eq("dcd.city_code", eventInvitationParam.getCityCode());
            }
            // 区码
            if (ObjectUtil.isNotNull(eventInvitationParam.getCountyCode()) && !"".equals(eventInvitationParam.getCountyCode())) {
                queryWrapper.eq("dcd.county_code", eventInvitationParam.getCountyCode());
            }*/

            if (ObjectUtil.isNotNull(consInvitationParam.getSortColumn())) {
                if (consInvitationParam.getOrder().equals("asc")) {
                    queryWrapper.orderByAsc("reply_cap");
                } else {
                    queryWrapper.orderByDesc("reply_cap");
                }
            }
        }
        Page<ConsInvitation> consInvitationPage = getBaseMapper().getReplyPageCons(consInvitationParam.getPage(), queryWrapper);
        if (null != consInvitationPage) {
            List<ConsInvitation> result = consInvitationPage.getRecords();
            if (null != result && result.size() > 0) {
                com.alibaba.fastjson.JSONObject datas = systemClientService.queryAllOrg();
                com.alibaba.fastjson.JSONArray jsonArray = null;
                if (null != datas && "000000".equals(datas.getString("code"))) {
                    jsonArray = datas.getJSONArray("data");
                }
                List<SysOrgs> orgsListDate = new ArrayList<>();
                if (null != datas && datas.size() > 0) {
                    for (Object object : jsonArray) {
                        com.alibaba.fastjson.JSONObject jsonObject = (com.alibaba.fastjson.JSONObject) com.alibaba.fastjson.JSONObject.toJSON(object);
                        SysOrgs sysOrgs = JSONObjectToEntityUtils.JSONObjectToSysOrg(jsonObject);
                        orgsListDate.add(sysOrgs);
                    }
                }
                for (ConsInvitation planCons : result) {
                    List<SysOrgs> single = orgsListDate.stream().filter(n -> planCons.getOrgNo().equals(n.getId())).collect(toList());
                    if (null != single && single.size() > 0) {
                        planCons.setOrgName(single.get(0).getName());
                    }
                }
                consInvitationPage.setRecords(result);
            }
        }
        return consInvitationPage;
    }

    @Override
    public Page<ConsInvitation> replyPageProxy(ConsInvitationParam consInvitationParam) {
        QueryWrapper<Object> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotNull(consInvitationParam)) {
            // 根据事件ID模糊查询
            if (ObjectUtil.isNotEmpty(consInvitationParam.getEventId())) {
                queryWrapper.eq("inv.event_id", consInvitationParam.getEventId());
            }
            // 营销户号(需求响应2.0中，consId就是营销户号)
            if (null != consInvitationParam.getConsIdList() && consInvitationParam.getConsIdList().size() > 0) {
                queryWrapper.in("c.id", consInvitationParam.getConsIdList());
            }
        }
        Page<ConsInvitation> consInvitationPage = getBaseMapper().selectPageVo(consInvitationParam.getPage(), queryWrapper);
        return consInvitationPage;
    }

    @Override
    public Page<ConsInvitation> proxyPage(CustInvitationParam custInvitationParam) {
        QueryWrapper<Object> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotNull(custInvitationParam)) {
            // 根据事件ID模糊查询
            if (ObjectUtil.isNotEmpty(custInvitationParam.getEventId())) {
                queryWrapper.eq("inv.EVENT_ID", custInvitationParam.getEventId());
            }
            //集成商id
            if (ObjectUtil.isNotEmpty(custInvitationParam.getCustId())) {
                queryWrapper.eq("c.id", custInvitationParam.getCustId());
            }
           /* // 是否参与
            if (ObjectUtil.isNotEmpty(custInvitationParam.getIsParticipate())) {
                queryWrapper.eq("dei.is_participate", custInvitationParam.getIsParticipate());
            }
            // 用户名
            if (ObjectUtil.isNotNull(custInvitationParam.getConsName()) && !"".equals(custInvitationParam.getConsName())) {
                queryWrapper.like("dcd.cons_name", custInvitationParam.getConsName());
            }
            // 营销户号
            if (ObjectUtil.isNotNull(custInvitationParam.getConsName()) && !"".equals(custInvitationParam.getConsName())) {
                queryWrapper.like("dcd.elec_cons_no", custInvitationParam.getElecConsNo());
            }*/
        }
        Page<ConsInvitation> proxyInvitationPage = getBaseMapper().proxyPageVo(custInvitationParam.getPage(), queryWrapper);
        return proxyInvitationPage;
    }

    @Override
    public ConsInvitation queryConsInvitationByEventIdAndConsId(long eventId, String consId) {
        LambdaQueryWrapper<ConsInvitation> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(eventId)) {
            queryWrapper.eq(ConsInvitation::getEventId, eventId);
        }

        // 根据用户ID查询
        if (ObjectUtil.isNotNull(consId)) {
            queryWrapper.eq(ConsInvitation::getConsId, consId);
        }

        return this.getOne(queryWrapper);
    }

    @Override
    public List<ConsCurve> trackInvitationReply(ConsInvitationParam consInvitationParam) {
        List<ConsCurve> allList = new ArrayList<>();
        //数据权限
        CurrenUserInfo currenUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if (null == currenUserInfo) {
            return allList;
        }
        String orgNo = currenUserInfo.getOrgId();
        String orgTitle = currenUserInfo.getOrgTitle();
        //机构子集
        List<String> list = new ArrayList<>();
        if (null != orgNo && !"".equals(orgNo)) {
            if (!"1".equals(orgTitle)) {
                //获取所有组织机构集合
                list = OrganizationUtil.getAllOrgByOrgNo();
                if (CollectionUtil.isEmpty(list)) {
                    return allList;
                }
                consInvitationParam.setOrgs(list);
            }
        } else {
            return allList;
        }
        Event event = eventService.getById(consInvitationParam.getEventId());
        if (ObjectUtil.isNull(event)) {
            throw new ServiceException(EventExceptionEnum.EVENT_NOT_EXIST);
        }
        // 查询所有已反馈的邀约用户
        List<ConsInvitation> consInvitations = baseMapper.getPartCons(consInvitationParam);
        // List<EventInvitation> list = this.list(queryWrapper);
        if (CollectionUtil.isEmpty(consInvitations)) {
            return allList;
        }
        // 2.返回结果/容量
        ConsCurve resultCurve = new ConsCurve();
        // 用户数量
        ConsCurve invitationNum = new ConsCurve();
        // 统计反馈容量总和和最新反馈时间
        BigDecimal feedbackCapacity = consInvitations.stream().map(ConsInvitation::getReplyCap).reduce(BigDecimal.ZERO, (d1, d2) -> {
            return Optional.ofNullable(d1).orElse(BigDecimal.ZERO).add(Optional.ofNullable(d2).orElse(BigDecimal.ZERO));
        });
        resultCurve.setFeedbackCapacity(feedbackCapacity);

        resultCurve.setLatestFeedBackTime(CollectionUtil.getLast(consInvitations).getReplyTime());
        invitationNum.setLatestFeedBackTime(CollectionUtil.getLast(consInvitations).getReplyTime());

        int year = consInvitations.get(0).getInvitationTime().getYear();
        int monthValue = consInvitations.get(0).getInvitationTime().getMonthValue();
        int day = consInvitations.get(0).getInvitationTime().getDayOfMonth();
        int hours;
        int minutes;
        int lastHours;
        int lastMinutes;

        resultCurve.setP1(BigDecimal.ZERO);
        invitationNum.setP1(BigDecimal.ZERO);
        for (int i = 2; i < 97; i++) {
            hours = (i - 1) / 4;
            minutes = 15 * ((i - 1) % 4);
            LocalDateTime nowTime = LocalDateTime.of(year, monthValue, day, hours, minutes);
            int lastPoint = i - 1;
            BigDecimal lastFieldValue = (BigDecimal) ReflectUtil.getFieldValue(resultCurve, "p" + lastPoint);
            BigDecimal lastFieldNum = (BigDecimal) ReflectUtil.getFieldValue(invitationNum, "p" + lastPoint);
            lastHours = (lastPoint - 1) / 4;
            lastMinutes = 15 * ((lastPoint - 1) % 4);
            LocalDateTime lastTime = LocalDateTime.of(year, monthValue, day, lastHours, lastMinutes);
            // 获取当前时间段反馈的所有邀约用户清单
            List<ConsInvitation> currentList = consInvitations.stream().filter(invitation -> {
                return (invitation.getReplyTime().isBefore(nowTime) || invitation.getReplyTime().isEqual(nowTime)) && invitation.getReplyTime().isAfter(lastTime);
            }).collect(toList());

            if (CollectionUtil.isEmpty(currentList)) {
                ReflectUtil.setFieldValue(resultCurve, "p" + i, lastFieldValue);
                ReflectUtil.setFieldValue(invitationNum, "p" + i, lastFieldNum);
                continue;
            }
            BigDecimal currentReply = currentList.stream().map(ConsInvitation::getReplyCap).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            BigDecimal pointCap = lastFieldValue.add(currentReply);

            BigDecimal num = new BigDecimal(currentList.size());
            BigDecimal consNum = lastFieldNum.add(num);
            ReflectUtil.setFieldValue(invitationNum, "p" + i, consNum);

            ReflectUtil.setFieldValue(resultCurve, "p" + i, pointCap);
        }
        allList.add(invitationNum);
        allList.add(resultCurve);
        return allList;
    }

    @Override
    public List<ConsInvitation> getReplyInvitation(Long eventId, String isParticipate) {
        LambdaQueryWrapper<ConsInvitation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ConsInvitation::getEventId, eventId);
        queryWrapper.eq(ConsInvitation::getIsParticipate, isParticipate);
        queryWrapper.orderByAsc(ConsInvitation::getReplyTime);
        return this.list(queryWrapper);
    }

    private ConsInvitation queryBy(Long eventId, String consId) {
        LambdaQueryWrapper<ConsInvitation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ConsInvitation::getEventId, eventId);
        queryWrapper.eq(ConsInvitation::getConsId, consId);
        List<ConsInvitation> consInvitationList = this.list(queryWrapper);
        if (CollectionUtil.isEmpty(consInvitationList)) {
            throw new ServiceException(EventInvitationExceptionEnum.EVENT_NOT_EXIST);
        }
        return consInvitationList.get(0);
    }

    @Override
    public ResponseData submitFeedback(ConsInvitationParam consInvitationParam) {
        try {
            if (("Y".equals(consInvitationParam.getIsParticipate())) && (null == consInvitationParam.getReplyCap() || consInvitationParam.getReplyCap().compareTo(BigDecimal.ZERO) < 1)) {
                return ResponseData.fail("-1", "邀约反馈量必须大于0", "邀约反馈量必须大于0");
            }
            Event event = eventService.getById(consInvitationParam.getEventId());
            if (event == null) {
                return ResponseData.fail("-1", "无对应反馈的事件", "无对应反馈的事件");
            }
            ConsInvitation consInvitation = this.queryConsInvitationByEventIdAndConsId(consInvitationParam.getEventId(), consInvitationParam.getConsId());
            if (ObjectUtil.isNull(consInvitation)) {
                return ResponseData.fail("-1", "邀约记录不存在", "邀约记录不存在");
            }
            if (LocalDateTime.now().isAfter(consInvitation.getDeadlineTime())) {
               /* consInvitation.setIsReply("1");
                consInvitation.setIsParticipate("N");
                consInvitation.setNoReplyReason(NoReplyReason.TIME_DEAD.getCode());
                this.updateById(consInvitation);*/
                return ResponseData.fail("-1", "已超过反馈截止时间，反馈失败", "已超过反馈截止时间，反馈失败");
            }
            ConsContractInfo consContractInfo = consContractInfoService.queryByConsIdAndProjectId(consInvitationParam.getConsId(), event.getProjectId());
            //获取签约信息
            if (null == consContractInfo) {
                return ResponseData.fail("-1", "无签约信息，反馈失败", "无签约信息，反馈失败");
            }
            //判断是否未参与，如果未参与，直接保存
            if ("N".equals(consInvitationParam.getIsParticipate())) {
                consInvitation.setIsReply("1");
                consInvitation.setIsParticipate("N");
                Long custId = consContractInfo.getCustId();
                if (null == custId) {
                    return ResponseData.fail("-1", "客户id为空，反馈失败", "客户id为空，反馈失败");
                }
                CustInvitation custInvitation = null;
                //从 关联表 查询该custId 代理的所有户号
                List<String> consIdList = consService.getConsIdListByCust(custId);
                if (null != consIdList && consIdList.size() > 0) {
                    consInvitationParam.setConsIdList(consIdList);
                    consInvitationParam.setIsParticipate("Y");
                    consInvitationParam.setConsNo(consInvitationParam.getConsId());
                    consInvitationParam.setConsId(null);
                    //查询是否有参与用户,补包含该用户本身
                    List<ConsInvitation> consInvitationList = this.list(consInvitationParam);
                    //查询客户信息
                    CustInvitationParam custInvitationParam = new CustInvitationParam();
                    custInvitationParam.setCustId(custId);
                    custInvitationParam.setEventId(consInvitationParam.getEventId());
                    List<CustInvitation> custInvitations = (List<CustInvitation>) (custInvitationService.list(custInvitationParam));
                    if (null != custInvitations) {
                        custInvitation = custInvitations.get(0);
                    } else {
                        return ResponseData.fail("-1", "无客户信息，反馈失败", "无客户信息，反馈失败");
                    }
                    if (null != consInvitationList && consInvitationList.size() > 0) {
                        custInvitation.setIsParticipate("Y");
                        custInvitation.setIsReply("1");
                    } else {
                        custInvitation.setIsParticipate("N");
                        custInvitation.setIsReply("1");
                    }

                } else {
                    return ResponseData.fail("-1", "无用户信息，反馈失败", "无用户信息，反馈失败");
                }
                custInvitationService.updateById(custInvitation);
                this.updateById(consInvitation);
                return ResponseData.success();
            }
            //校验反馈值合理性
            /*if(null!=consContractInfo) {
                Map<String,Object> map = new HashMap<>();
                map.put("contractId",consContractInfo.getContractId());
                map.put("advanceNoticeTime",event.getAdvanceNoticeTime());
                map.put("responseType",event.getResponseType());
                map.put("timeType",event.getTimeType());
                BigDecimal contractCap = consContractDetailService.getContractCapByCondition(map);
                if(null!=contractCap) {
                    if(consInvitationParam.getReplyCap().compareTo(contractCap)>0) {
                        return ResponseData.fail("-1","反馈量超过签约值，反馈失败","反馈量超过签约值，反馈失败");
                    }
                } else {
                    return ResponseData.fail("-1","无签约协议容量，反馈失败","无签约协议容量，反馈失败");
                }

            } else {
                return ResponseData.fail("-1","无签约信息，反馈失败","无签约信息，反馈失败");
            }*/
            consInvitationParam.setConsId(consInvitation.getConsId());
            consInvitationParam.setEventId(consInvitation.getEventId());
            LocalDateTime localDateTime = LocalDateTime.now();//获取当前时间
            consInvitation.setIsParticipate(consInvitationParam.getIsParticipate());
            consInvitation.setIsReply("1");
            consInvitation.setReplyTime(localDateTime);
            consInvitation.setReplySource(ParticipateTypeEnum.XQXY_WEB.getCode());
            consInvitation.setSynchToPlan(YesOrNotEnum.N.getCode());
            //判断 调控负荷 * 调控倍率 <= 此事件已有 所有用户反馈负荷之和
            BigDecimal rap = event.getRegulateCap().multiply(event.getRegulateMultiple());
            LambdaQueryWrapper<ConsInvitation> consInvitationWrapper = new LambdaQueryWrapper<>();
            consInvitationWrapper.eq(ConsInvitation::getEventId, consInvitation.getEventId());
            synchronized (this) {
                //所有用户反馈负荷之和,不包含该用户本身
                BigDecimal rereplyCapSum = this.getReplyCapTotalByEvent(consInvitationParam);
                if (null == rereplyCapSum) {
                    rereplyCapSum = BigDecimal.ZERO;
                }
                if (rap.compareTo(rereplyCapSum) == -1 || rap.compareTo(rereplyCapSum) == 0) {
                    consInvitation.setNoReplyReason(NoReplyReason.CAP_FULL.getCode());
                    consInvitation.setIsParticipate("N");
                    this.updateById(consInvitation);
                    return ResponseData.fail("-1", "超出调控停止响应量，反馈失败", "超出调控停止响应量，反馈失败");
                }
                if ("Y".equals(consInvitationParam.getIsParticipate())) {
                    consInvitation.setReplyPrice(consInvitationParam.getReplyPrice());
                    consInvitation.setReplyCap(consInvitationParam.getReplyCap());
                }
                this.updateById(consInvitation);
            }
            //修改客户反馈 响应负荷以及是否参与
            custInvitationService.submitFeedback(consInvitationParam, event, consContractInfo);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error:" + e);
        }
        return ResponseData.success();
    }

    @Override
    public LocalDateTime getMaxDeadlineTimeByEventId(Long eventId) {
        return getBaseMapper().getMaxDeadlineTimeByEventId(eventId);
    }

    @Override
    public LocalDateTime getMaxDeadlineTimeByEventIdAndState(Long eventId, String synchToPlan) {
        return getBaseMapper().getMaxDeadlineTimeByEventIdAndState(eventId, synchToPlan);
    }

    @Override
    public List<Event> getMaxDeadlineTimeByCon(List<Event> eventList) {
        return getBaseMapper().getMaxDeadlineTimeByCon(eventList);
    }

    @Override
    public List<ConsInvitation> listOrder(Long eventId) {
        LambdaQueryWrapper<ConsInvitation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ConsInvitation::getEventId, eventId);
        queryWrapper.eq(ConsInvitation::getIsParticipate, YesOrNotEnum.Y.getCode());
        queryWrapper.orderByAsc(ConsInvitation::getReplyPrice);
        queryWrapper.orderByDesc(ConsInvitation::getReplyCap);
        return this.list(queryWrapper);
    }

    @Override
    public void secondInvitaiton(Long eventId, LocalDateTime secondRoundDeadlineTime) {
        LambdaUpdateWrapper<ConsInvitation> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ConsInvitation::getEventId, eventId);
        updateWrapper.and(wrapper -> wrapper.isNull(ConsInvitation::getIsParticipate));
        updateWrapper.set(ConsInvitation::getDeadlineTime, secondRoundDeadlineTime);
        this.update(updateWrapper);
    }

    @Override
    public ConsInvitation firstInvitationByEventId(Long eventId) {
        LambdaQueryWrapper<ConsInvitation> invitationWrapper = new LambdaQueryWrapper<>();
        invitationWrapper.eq(ConsInvitation::getEventId, eventId);
        List<ConsInvitation> list = this.list(invitationWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public EventInvitationResult result(Long eventId) {
        EventInvitationResult eventInvitationResult = new EventInvitationResult();
        Event event = eventService.detail(eventId);
        event.setRegulateCap(NumberUtil.div(event.getRegulateCap(), 10000));
        eventInvitationResult.setEvent(event);
        BigDecimal totalReplyCap = getBaseMapper().totalReplyCap(eventId);
        LocalDateTime lastReplyTime = getBaseMapper().lastReplyTime(eventId);
        eventInvitationResult.setTotalReplyCap(totalReplyCap);
        eventInvitationResult.setLastReplyTime(lastReplyTime);
        return eventInvitationResult;
    }

    @Override
    public Map<String, Object> invitationParticipateCount(Long eventId) {
        Map<String, Object> data = new HashMap<>();
        data.put("partCount", "");
        data.put("noPartCount", "");
        data.put("otherCount", "");
        //数据权限
        CurrenUserInfo currenUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if (null == currenUserInfo) {
            return data;
        }
        String orgNo = currenUserInfo.getOrgId();
        String orgTitle = currenUserInfo.getOrgTitle();
        Map<String, Object> param = new HashMap<>();
        //机构子集
        List<String> list = new ArrayList<>();
        if (null != orgNo && !"".equals(orgNo)) {
            if (!"1".equals(orgTitle)) {
                //获取所有组织机构集合
                list = OrganizationUtil.getAllOrgByOrgNo();
                if (CollectionUtil.isEmpty(list)) {
                    return data;
                }
                param.put("orgs", list);
            }
        } else {
            return data;
        }
        Map<String, Object> map = null;
        param.put("eventId", eventId);
        map = baseMapper.getConsCount(param);
        /*LambdaQueryWrapper<ConsInvitation> queryPartWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<ConsInvitation> queryNoPartWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<ConsInvitation> queryCountWrapper = new LambdaQueryWrapper<>();

        queryPartWrapper.eq(ConsInvitation::getEventId, eventId);
        queryPartWrapper.eq(ConsInvitation::getIsParticipate, YesOrNotEnum.Y.getCode());
        Integer partCount = this.count(queryPartWrapper);

        queryNoPartWrapper.eq(ConsInvitation::getEventId, eventId);
        queryNoPartWrapper.eq(ConsInvitation::getIsParticipate, YesOrNotEnum.N.getCode());
        Integer noPartCount = this.count(queryNoPartWrapper);

        queryCountWrapper.eq(ConsInvitation::getEventId, eventId);
        Integer otherCount = this.count(queryCountWrapper) - noPartCount - partCount;

        map.put("partCount", partCount);
        map.put("noPartCount", noPartCount);
        map.put("otherCount", otherCount);*/
        if (null == map) {
            map = data;
        }
        return map;
    }

    @Override
    public ConsInvitation detail(EventParam eventParam) {
        ConsInvitation consInvitation = null;
        LambdaQueryWrapper<ConsInvitation> consInvitationQueryWrapper = new LambdaQueryWrapper<>();
        consInvitationQueryWrapper.eq(ConsInvitation::getEventId, eventParam.getEventId());
        consInvitationQueryWrapper.eq(ConsInvitation::getConsId, eventParam.getConsId());
        List<ConsInvitation> consInvitations = baseMapper.selectList(consInvitationQueryWrapper);
        if (null != consInvitations && consInvitations.size() > 0) {
            consInvitation = consInvitations.get(0);
        }
        if (ObjectUtil.isNull(consInvitation)) {
            throw new ServiceException(EventInvitationExceptionEnum.EVENT_NOT_EXIST);
        }
        Event event = eventService.detailBy(consInvitation.getEventId(), consInvitation.getConsId());
        Cons cons = consService.getById(consInvitation.getConsId());
        consInvitation.setEvent(event);
        consInvitation.setCons(cons);
        //获取签约信息
        ConsContractInfo consContractInfo = consContractInfoService.queryByConsIdAndProjectId(consInvitation.getConsId(), event.getProjectId());
        //校验反馈值合理性
        if (null != consContractInfo) {
            Map<String, Object> map = new HashMap<>();
            map.put("contractId", consContractInfo.getContractId());
            map.put("advanceNoticeTime", event.getAdvanceNoticeTime());
            map.put("responseType", event.getResponseType());
            map.put("timeType", event.getTimeType());
            BigDecimal contractCap = consContractDetailService.getContractCapByCondition(map);
            consInvitation.setContractCapData(contractCap);
        }
        return consInvitation;
    }

    @Override
    public ConsInvitation detailById(Long invitationId) {
        ConsInvitation consInvitation = this.getById(invitationId);
        if (ObjectUtil.isNull(consInvitation)) {
            throw new ServiceException(EventInvitationExceptionEnum.EVENT_NOT_EXIST);
        }
        return consInvitation;
    }

    @Override
    public List<ConsInvitation> getConsInfoByEvent(long eventId) {
        return baseMapper.getConsInfoByEvent(eventId);
    }

    @Override
    public List<ConsInvitation> getConsInfoByEvent(long eventId, String consId) {
        return baseMapper.getConsInfoByEventAndConsId(eventId,consId);
    }

    @Override
    public BigDecimal getReplyCapTotalByEvent(ConsInvitationParam consInvitationParam) {
        return baseMapper.getReplyCapTotalByEvent(consInvitationParam);
    }

    @Override
    public List<ConsInvitation> getConsInfoByEvents(List<Event> eventIds) {
        return baseMapper.getConsInfoByEvents(eventIds);
    }

    @Override
    public List<ConsInvitation> getConsInfoByEvents2(List<Event> eventIds) {
        return baseMapper.getConsInfoByEvents2(eventIds);
    }
}
