package com.xqxy.dr.modular.dispatch.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.annotion.NeedSetValueField;
import com.xqxy.core.client.SystemClientService;
import com.xqxy.core.pojo.login.CurrenUserInfo;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.dr.modular.baseline.entity.BaseLineSend;
import com.xqxy.dr.modular.baseline.entity.CityTargetSend;
import com.xqxy.dr.modular.baseline.mapper.BaseLineDetailMapper;
import com.xqxy.dr.modular.device.entity.SysOrgs;
import com.xqxy.dr.modular.dispatch.entity.Dispatch;
import com.xqxy.dr.modular.dispatch.entity.DispatchEvent;
import com.xqxy.dr.modular.dispatch.enums.DispatchException;
import com.xqxy.dr.modular.dispatch.mapper.DispatchMapper;
import com.xqxy.dr.modular.dispatch.param.DispatchAndSoltParam;
import com.xqxy.dr.modular.dispatch.param.DispatchEditorParam;
import com.xqxy.dr.modular.dispatch.param.DispatchParam;
import com.xqxy.dr.modular.dispatch.param.DispatchSoltParam;
import com.xqxy.dr.modular.dispatch.service.DispatchService;
import com.xqxy.dr.modular.event.entity.Event;
import com.xqxy.dr.modular.event.entity.Plan;
import com.xqxy.dr.modular.event.entity.PlanConsVo;
import com.xqxy.dr.modular.event.enums.RangeTypeEnum;
import com.xqxy.dr.modular.event.mapper.EventMapper;
import com.xqxy.dr.modular.event.mapper.PlanConsMapper;
import com.xqxy.dr.modular.event.param.EventParam;
import com.xqxy.dr.modular.event.service.EventService;
import com.xqxy.dr.modular.event.service.PlanService;
import com.xqxy.dr.modular.event.utils.OrgUtils;
import com.xqxy.dr.modular.project.entity.ConsContractInfo;
import com.xqxy.dr.modular.project.result.Region;
import com.xqxy.dr.modular.project.service.ConsContractInfoService;
import com.xqxy.sys.modular.cust.mapper.BlackNameMapper;
import com.xqxy.sys.modular.cust.result.Result;
import com.xqxy.sys.modular.dict.param.DictTypeParam;
import com.xqxy.sys.modular.dict.service.DictTypeService;
import com.xqxy.sys.modular.log.entity.SysOpLog;
import com.xqxy.sys.modular.log.service.SysOpLogService;
import com.xqxy.sys.modular.utils.JSONObjectToEntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * <p>
 * 调度需求响应指令 服务实现类
 * </p>
 *
 * @author xiao jun
 * @since 2021-05-14
 */
@Service
public class DispatchServiceImpl extends ServiceImpl<DispatchMapper, Dispatch> implements DispatchService {

    private static final Log log = Log.get();

    @Resource
    DispatchMapper dispatchMapper;

    @Resource
    SystemClientService systemClientService;

    @Resource
    EventService eventService;

    @Resource
    ConsContractInfoService consContractInfoService;

    @Autowired
    private BlackNameMapper blackNameMapper;

    @Resource
    EventMapper eventMapper;

    @Resource
    private DictTypeService dictTypeService;

    @Resource
    private PlanService planService;

    @Resource
    PlanConsMapper planConsMapper;

    @Resource
    BaseLineDetailMapper baseLineDetailMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DispatchService dispatchService;

    @Autowired
    private SysOpLogService sysOpLogService;

    @Override
    @Transactional
    public Long add(DispatchAndSoltParam dispatchAndSoltParam) {
        Long state =1L;
        DispatchParam dispatchParam = dispatchAndSoltParam.getDispatchParam();
        //生成指令编号规则为当前年月日时分秒+时段编号,时段编号后面产生
        StringBuffer stringNo = new StringBuffer();
        Calendar date = Calendar.getInstance();
        stringNo = stringNo.append(date.get(Calendar.YEAR));
        stringNo = stringNo.append(date.get(Calendar.MONTH)+1);
        stringNo = stringNo.append(date.get(Calendar.DAY_OF_MONTH));
        stringNo = stringNo.append(date.get(Calendar.HOUR_OF_DAY));
        stringNo = stringNo.append(date.get(Calendar.MINUTE));
        stringNo = stringNo.append(date.get(Calendar.SECOND));
        List<DispatchSoltParam> dispatchSoltsParam = dispatchAndSoltParam.getDispatchSolts();
        ArrayList<Dispatch> dispatchs = new ArrayList<>();
        Integer i = 1;
        if(CollectionUtil.isNotEmpty(dispatchSoltsParam)) {
            for (DispatchSoltParam dispatchSoltParam : dispatchSoltsParam) {
                Dispatch dispatchSolt = new Dispatch();
                dispatchSolt.setRegulateNo(stringNo.toString()+i);
                dispatchSolt.setStregyId(stringNo.toString()+i);
                BeanUtils.copyProperties(dispatchParam,dispatchSolt);
                BeanUtils.copyProperties(dispatchSoltParam,dispatchSolt);
                if(null==dispatchSolt.getStatus() || "".equals(dispatchSolt.getStatus())) {
                    dispatchSolt.setStatus("01");
                }
                dispatchSolt.setPeriodDetail(dispatchSoltParam.getTimeType());
                CurrenUserInfo currenUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
                if(null!=currenUserInfo) {
                    dispatchSolt.setCreateUserName(currenUserInfo.getName());
                }
                BigDecimal target = dispatchSolt.getRegulateCap();
                dispatchSolt.setRegulateCap(NumberUtil.mul(target, 10000));
                dispatchs.add(dispatchSolt);
                i++;
            }
            this.saveBatch(dispatchs);
            state = dispatchs.get(0).getRegulateId();
            log.info(">>> 调度指令信息保存完成：{}", dispatchs);
        } else {
            Dispatch dispatch = new Dispatch();
            dispatch.setRegulateNo(stringNo.toString()+i);
            BeanUtils.copyProperties(dispatchParam,dispatch);
            dispatch.setStatus("01");
            this.save(dispatch);
            state = dispatch.getRegulateId();
            log.info(">>> 调度指令信息保存完成：{}", dispatch);
        }
        return state;
    }

    @Override
    public String remove(DispatchParam dispatchParam) {
        Dispatch dispatch = this.getById(dispatchParam.getRegulateId());
        if(null!=dispatch && null !=dispatch.getStatus()) {
            if(dispatch.getStatus().equals("02")) {
                  return DispatchException.DISPATCH_EVENT_EXIST.getMessage();
            }
        }
        dispatch.setStatus("01");
        this.updateById(dispatch);
        return DispatchException.DISPATCH_REMOTE_SUCCESS.getMessage();
    }

    @Override
    public void delete(DispatchParam dispatchParam) {
            Dispatch dispatch = new Dispatch();
            BeanUtils.copyProperties(dispatchParam,dispatch);
            LambdaQueryWrapper<Dispatch> dispatchWrapper = new LambdaQueryWrapper<>();
            dispatchWrapper.eq(Dispatch::getRegulateId, dispatchParam.getRegulateId());
            this.remove(dispatchWrapper);
    }

    @Override
    @Transactional
    public void edit(DispatchEditorParam dispatchAndSoltParam) {
        Dispatch dispatch = new Dispatch();
        BeanUtils.copyProperties(dispatchAndSoltParam,dispatch);
        dispatch.setPeriodDetail(dispatchAndSoltParam.getTimeType());
        dispatch.setRegulateCap(NumberUtil.mul(dispatch.getRegulateCap(), 10000));
        this.updateById(dispatch);
    }

    @Override
    @NeedSetValueField
    public Page<Dispatch> page(DispatchParam dispatchParam) {
        Page<Dispatch> page = new Page<>(dispatchParam.getCurrent(),dispatchParam.getSize());
        List<Dispatch> list = this.baseMapper.getDispatchPageList(page,dispatchParam.getYear());
        page.setRecords(list);
        return page;
    }

    @Override
    public Dispatch getDispatchById(DispatchParam dispatchParam) {
        Dispatch dispatch = dispatchMapper.getDispatchById(dispatchParam);
        JSONArray jsonArray = new JSONArray();
        List<List<String>> regulateRange = new ArrayList<>();
        String regulateRangeStr = "";
        if(null!=dispatch) {
            String rangeType = dispatch.getRangeType();
            if(null!=rangeType && "1".equals(rangeType)) {
                List<Region> regions = systemClientService.queryAll();
                if(null!=dispatch.getRegulateRange()) {
                    String one = dispatch.getRegulateRange().substring(0, 1);
                    if ("[".equals(one)) {
                        jsonArray = JSONArray.parseArray(dispatch.getRegulateRange());
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
                                                    if (null != regulateRangeStr && !"".equals(regulateRangeStr)) {
                                                        regulateRangeStr = regulateRangeStr.substring(0, regulateRangeStr.length() - 1);
                                                    }
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
                } else {
                    List<String> list = new ArrayList<>();
                    list.add(dispatch.getRegulateRange());
                    Optional<Region> optionalRegion = regions.stream().filter(item -> item.getId().equals(dispatch.getRegulateRange())).findAny();
                    if (optionalRegion.isPresent()) {
                        Region region = optionalRegion.get();
                        regulateRangeStr = region.getName();
                    }
                }
            } else if(null!=rangeType && "2".equals(rangeType)) {
                Result result = systemClientService.getAllOrgs();
                JSONObject jsonObject = null;
                if(null!=result) {
                     jsonObject = result.getData();
                }
                if(null!=dispatch.getRegulateRange()) {
                    String one = dispatch.getRegulateRange().substring(0, 1);
                    if ("[".equals(one)) {
                        jsonArray = JSONArray.parseArray(dispatch.getRegulateRange());
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
                                                    if (null != regulateRangeStr && !"".equals(regulateRangeStr)) {
                                                        regulateRangeStr = regulateRangeStr.substring(0, regulateRangeStr.length() - 1);
                                                    }
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
                    } else {
                        if (jsonObject.containsKey(dispatch.getRegulateRange())) {
                            Object data = jsonObject.get(dispatch.getRegulateRange());
                            JSONObject datas = (JSONObject) JSON.toJSON(data);
                            regulateRangeStr = (String) datas.get("name");
                            List<String> list = new ArrayList<>();
                            list.add(dispatch.getRegulateRange());
                            regulateRange.add(list);
                            }
                        }
                }
            }
            //dispatch.setRegulateRangeStr(regulateRangeStr);
            if(null!=regulateRangeStr && !"".equals(regulateRangeStr)) {
                if("|".equals(regulateRangeStr.substring(regulateRangeStr.length()-1))) {
                    regulateRangeStr = regulateRangeStr.substring(0,regulateRangeStr.length()-1);
                }
            }
            dispatch.setRegulateRangeStr(regulateRangeStr);
            dispatch.setRegulateRangeList(regulateRange);
        }
        return dispatch;
    }

    @Override
    public ResponseData issueDispatch(DispatchEditorParam dispatchParam) {
        Dispatch dispatch = this.getById(dispatchParam.getRegulateId());
        //判断调度指令是否超时
        LocalDate localDate = LocalDate.now();
        LocalDate regulateDate = dispatch.getRegulateDate();
        Integer endHour = 0;
        Integer endMinute = 0;
        String start = dispatch.getStartTime();
        String end = dispatch.getEndTime();
        if (null != start) {
            start = start.substring(0,5);
            endHour = Integer.parseInt(start.substring(0, 2));
            endMinute = Integer.parseInt(start.substring(3));
        }
        if(null!=end) {
            end = end.substring(0,5);
        }

        LocalTime startTime = LocalTime.of(endHour, endMinute);
        if (regulateDate.compareTo(localDate) == 0) {
            if (LocalTime.now().compareTo(startTime) > 0) {
                return ResponseData.fail("-1","调度日期已超时,请修改调度日期","调度日期已超时,请修改调度日期");
            }
        } else if (regulateDate.compareTo(localDate) < 0) {
            return ResponseData.fail("-1","调度日期已超时,请修改调度日期","调度日期已超时,请修改调度日期");
        }
        dispatch.setStatus("03");
        dispatch.setStartTime(start);
        dispatch.setEndTime(end);
        this.updateById(dispatch);
        return ResponseData.success();
    }

    @Override
    public Page<DispatchEvent> getPage(DispatchParam dispatchParam) {
        Page<DispatchEvent> page = new Page<>(dispatchParam.getCurrent(),dispatchParam.getSize());
        List<DispatchEvent> list = dispatchMapper.getEventPage(page,dispatchParam.getYear());
        page.setRecords(list);
        return page;
    }

    @Override
    @NeedSetValueField
    public Page<Dispatch> getDispatchResPageList(DispatchParam dispatchParam) {
        Page<Dispatch> page = new Page<>(dispatchParam.getCurrent(),dispatchParam.getSize());
        List<Dispatch> list = this.baseMapper.getDispatchResPageList(page,dispatchParam.getYear());
        page.setRecords(list);
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseData addDispatchEvent(DispatchParam dispatchParam) {
        EventParam eventParam = new EventParam();
        Dispatch dispatch = this.baseMapper.getDispatchById(dispatchParam);
        if(null!=dispatch) {
            //判断调度指令是否超时
            LocalDate localDate = LocalDate.now();
            LocalDate regulateDate = dispatch.getRegulateDate();
            Integer endHour = 0;
            Integer endMinute = 0;
            if (null != dispatch.getStartTime()) {
                endHour = Integer.parseInt(dispatch.getStartTime().substring(0, 2));
                endMinute = Integer.parseInt(dispatch.getStartTime().substring(3));
            }
            LocalTime startTime = LocalTime.of(endHour, endMinute);
            if (regulateDate.compareTo(localDate) == 0) {
                if (LocalTime.now().compareTo(startTime) > 0) {
                    return ResponseData.fail("-1","调度指令已超时","调度指令已超时");
                }
            } else if (regulateDate.compareTo(localDate) < 0) {
                return ResponseData.fail("-1","调度指令已超时","调度指令已超时");
            }
            if(null!=dispatch.getStatus() && !"02".equals(dispatch.getStatus())) {
                eventParam.setRegulateDate(dispatch.getRegulateDate());
                eventParam.setRegulateId(dispatch.getRegulateId());
                eventParam.setEndTime(dispatch.getEndTime());
                eventParam.setStartTime(dispatch.getStartTime());
                eventParam.setRangeType(dispatch.getRangeType());
                eventParam.setRegulateCap(NumberUtil.mul(dispatch.getRegulateCap(), 10000));
                eventParam.setTimeType(dispatch.getPeriodDetail());
                eventParam.setResponseType(dispatch.getResponseType());
                eventParam.setRegulateRange(dispatch.getRegulateRange());
                eventParam.setProjectId(dispatch.getProjectId());
                if(null!=dispatch.getAdvanceNoticeTime()) {
                    eventParam.setAdvanceNoticeTime(dispatch.getAdvanceNoticeTime().toString());
                }
                Event event = new Event();
                BeanUtil.copyProperties(eventParam, event);
                List<ConsContractInfo> consContractList = new ArrayList();
                if (RangeTypeEnum.ADMINISTRATIVE_REGION.getCode().

                        equals(eventParam.getRangeType())) {
                    // 事件执行范围为行政区域时候
                    consContractList = consContractInfoService.listConsTractInfo(event);
                }
                if (RangeTypeEnum.ELECTRICIC_REGION.getCode().

                        equals(eventParam.getRangeType())) {
                    // 事件执行范围为供电单位编码的时候
                    consContractList = consContractInfoService.listConsTractInfoByOrg(event);
                }
                if(null==consContractList || consContractList.size()==0) {
                    return ResponseData.fail("-1","无签约用户，下发失败!","无签约用户，下发失败!");
                } else {
                    List<String> blackNames = blackNameMapper.getBlackNameConsIds();
                    if(null==blackNames) {
                        blackNames = new ArrayList<>();
                    }
                    for (int i = 0; i < consContractList.size(); i++) {
                        String consId = consContractList.get(i).getConsId();
                        if (blackNames.contains(consId)) {
                            consContractList.remove(i);
                            i--;
                        }
                    }
                    if(consContractList.size()==0) {
                        return ResponseData.fail("-1","用户都在黑名单，下发失败!","用户都在黑名单，下发失败!");
                    }
                }
                eventParam.setConsContractInfos(consContractList);
                eventService.add(eventParam);
                Dispatch dispatchEditorParam = new Dispatch();
                dispatchEditorParam.setRegulateId(dispatchParam.getRegulateId());
                dispatchEditorParam.setStatus("02");
                this.baseMapper.updateById(dispatchEditorParam);
            } else {
                return ResponseData.fail("-1","事件不可重复生成!","事件不可重复生成!");
            }
        }
        return ResponseData.success();
    }

    @Override
    public String getRegulateRangeStr (DispatchParam dispatchParam) {
        JSONArray jsonArray = new JSONArray();
        List<List<String>> regulateRange = new ArrayList<>();
        String regulateRangeStr = "";
        String rangeType = dispatchParam.getRangeType();
        if(null!=rangeType && "1".equals(rangeType)) {
            List<Region> regions = systemClientService.queryAll();
            if(null!=dispatchParam.getRegulateRange()) {
                jsonArray = JSONArray.parseArray(dispatchParam.getRegulateRange());
                if (null != jsonArray && jsonArray.size() > 0) {
                    for(int j=0;j<jsonArray.size();j++) {
                        JSONArray value = jsonArray.getJSONArray(j);
                        List<String> list = new ArrayList<>();
                        if (null != value && value.size() > 0) {
                            for (int i = 0; i < value.size(); i++) {
                                list.add(value.get(i).toString());
                                String code = value.get(i).toString();
                                Optional<Region> optionalRegion = regions.stream().filter(item -> item.getId().equals(code)).findAny();
                                if(optionalRegion.isPresent()) {
                                    Region region = optionalRegion.get();
                                    if(i==value.size()-1) {
                                        if (null!=region && null!=region.getName()) {
                                            regulateRangeStr += region.getName()+"|";
                                        } else {
                                            if(null!=regulateRangeStr && !"".equals(regulateRangeStr)) {
                                                regulateRangeStr = regulateRangeStr.substring(0, regulateRangeStr.length() - 1);
                                            }
                                            regulateRangeStr +="|";
                                        }
                                    }else {
                                        if (null!=region && null!=region.getName()) {
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
        } else if(null!=rangeType && "2".equals(rangeType)) {
            Result result = systemClientService.getAllOrgs();
            JSONObject jsonObject = null;
            if(null!=result) {
                jsonObject = result.getData();
            }
            if(null!=dispatchParam.getRegulateRange()) {
                jsonArray = JSONArray.parseArray(dispatchParam.getRegulateRange());
                if (null != jsonArray && jsonArray.size() > 0) {
                    for (int j = 0; j < jsonArray.size(); j++) {
                        JSONArray value = jsonArray.getJSONArray(j);
                        List<String> list = new ArrayList<>();
                        if (null != value && value.size() > 0) {
                            for (int i = 0; i < value.size(); i++) {
                                list.add(value.get(i).toString());
                                if(jsonObject.containsKey(value.get(i).toString())) {
                                    Object data = jsonObject.get(value.get(i).toString());
                                    JSONObject datas = (JSONObject) JSON.toJSON(data);
                                    if(i==value.size()-1) {
                                        if (null != datas.get("name")) {
                                            regulateRangeStr += datas.get("name") + "|";
                                        } else {
                                            if(null!=regulateRangeStr && !"".equals(regulateRangeStr)) {
                                                regulateRangeStr = regulateRangeStr.substring(0,regulateRangeStr.length()-1);
                                            }
                                            regulateRangeStr +="|";
                                        }
                                    }else {
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
        if(null!=regulateRangeStr && !"".equals(regulateRangeStr)) {
            if("|".equals(regulateRangeStr.substring(regulateRangeStr.length()-1))) {
                regulateRangeStr = regulateRangeStr.substring(0,regulateRangeStr.length()-1);
            }
        }
        return regulateRangeStr;

    }

    @Override
    public ResponseData sendCityTarget(EventParam eventParam) {
        boolean flag = redisTemplate.hasKey("sendCityTarget");
        if(flag) {
            return ResponseData.fail("-1","数据正在推送中!","数据正在推送中!");
        }
        try{
            //redis发送状态值,上锁
            Map<String, Object> eventInfo = new HashMap<>();
            eventInfo.put("sendStatus", "1");
            JSONObject eventInfoJson = new JSONObject(eventInfo);
            redisTemplate.convertAndSend("sendBaselineAndCons", eventInfoJson.toJSONString());
            Long eventId = eventParam.getEventId();
            if(null==eventId) {
                return ResponseData.fail("-1","事件编号为空!","事件编号为空!");
            }
            Event event = eventService.getById(eventId);
            String sendStatus = event.getSendStatus();
            JSONObject data = new JSONObject();
            String url = "";
            //查询基线库
            Long baseLineId = eventMapper.getBaseLineIdByEventId(eventId);
            if(null==baseLineId) {
                return ResponseData.fail("-1","基线库编号为空!","基线库编号为空!");
            }
            //查询方案
            LambdaQueryWrapper<Plan> queryWrapperPlan = new LambdaQueryWrapper<>();
            queryWrapperPlan.eq(Plan::getRegulateId,eventId);
            List<Plan> planList = planService.list(queryWrapperPlan);
            if (null == planList || planList.size() == 0) {
                log.info("无方案信息");
                return ResponseData.fail("-1","无方案信息!","无方案信息!");
            }
            //调度指令编号
            Long regulateId = event.getRegulateId();
            //查询地市信息
            List<CityTargetSend> cityTargetSends = planConsMapper.getCityTarget(regulateId);
            if("1".equals(sendStatus)) {
                return ResponseData.fail("-1","地市信息已推送完成，不能再次推送!","地市信息已推送完成，不能再次推送!");
            }
            //查询最终执行的用户信息
            List<PlanConsVo> cons = planConsMapper.queryConsInfoByPlanId(planList.get(0).getPlanId());
            if(null==cons || cons.size()==0) {
                return ResponseData.fail("-1","无参与用户!","无参与用户!");
            }
            String provinceCode = "";
            DictTypeParam dictTypeParam3 = new DictTypeParam();
            dictTypeParam3.setCode("province_code");
            List<Dict> list3 = dictTypeService.dropDown(dictTypeParam3);
            Object value2 = null;
            if(null!=list3 && list3.size()>0) {
                for(Dict dict : list3) {
                    if("anhui_org_code".equals(dict.get("code"))) {
                        value2 = dict.get("value");
                    }
                }
                if (null != value2) {
                    provinceCode = (String) value2;
                }
            } else {
                provinceCode = "34101";
                log.error("未配置省级供电单位编码!");
            }
            DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            //获取组织机构树
            JSONObject result = systemClientService.queryAllOrg();
            JSONArray datas = null;
            if ("000000".equals(result.getString("code"))) {
                datas = result.getJSONArray("data");
            } else {
                log.warn("组织机构不存在");
                return ResponseData.fail("-1","组织机构不存在!","组织机构不存在!");
            }
            if(null!=cityTargetSends && cityTargetSends.size()>0) {
                for (CityTargetSend cityTargetSend : cityTargetSends) {
                    if (null != cityTargetSend) {
                        cityTargetSend.setEventId(eventId);
                        cityTargetSend.setEventName(event.getEventName());
                        cityTargetSend.setStartTime(event.getStartTime());
                        cityTargetSend.setEndTime(event.getEndTime());
                        cityTargetSend.setRegulateDate(simpleDateFormat.format(event.getRegulateDate()));
                        cityTargetSend.setType("1");
                        if(!provinceCode.equals(cityTargetSend.getOrgId())) {
                            //市级
                            OrgUtils orgUtils = new OrgUtils();
                            List<String> orgs = orgUtils.getData(datas, cityTargetSend.getOrgId(), new ArrayList<>());
                            List<PlanConsVo> planConsVoCitys = cons.stream().filter(n -> orgs.contains(n.getOrgNo())).collect(Collectors.toList());
                            if(null!=planConsVoCitys && planConsVoCitys.size()>0) {
                                cityTargetSend.setCountUser(planConsVoCitys.size());
                                BigDecimal target = BigDecimal.ZERO;
                                for(PlanConsVo planConsVo : planConsVoCitys) {
                                    target = target.add(planConsVo.getDemandCap());
                                }
                                if(null!=target) {
                                    target = NumberUtil.div(target,10000);
                                }
                                cityTargetSend.setDemandCap(target);
                            } else {
                                cityTargetSend.setDemandCap(new BigDecimal("0.00"));
                                cityTargetSend.setCountUser(0);
                            }

                        }
                    }
                }
                //查询数据字典url
                DictTypeParam dictTypeParam = new DictTypeParam();
                dictTypeParam.setCode("load_post_url");
                List<Dict> list = dictTypeService.dropDown(dictTypeParam);
                if (null != list && list.size() > 0) {
                    Object value = list.get(0).get("value");
                    if (null != value) {
                        url = (String) value;
                        if (null != url) {
                            url = url + "dict/plan-event/addPlanEvents";
                        }
                    }
                } else {
                    log.error("无url配置信息！");
                    return ResponseData.fail("-1", "无url配置信息!", "无url配置信息!");
                }
                //省级新增一条
                CityTargetSend cityTargetSendProvince = new CityTargetSend();
                cityTargetSendProvince.setEventId(eventId);
                cityTargetSendProvince.setPlanNo(cityTargetSends.get(0).getPlanNo());
                cityTargetSendProvince.setPlanName(cityTargetSends.get(0).getPlanName());
                if(null!=event.getRegulateCap()) {
                    cityTargetSendProvince.setRegulateCap(NumberUtil.div(event.getRegulateCap(),10000));
                }
                cityTargetSendProvince.setEventName(event.getEventName());
                cityTargetSendProvince.setStartTime(event.getStartTime());
                cityTargetSendProvince.setEndTime(event.getEndTime());
                cityTargetSendProvince.setRegulateDate(simpleDateFormat.format(event.getRegulateDate()));
                cityTargetSendProvince.setType("1");
                cityTargetSendProvince.setCountUser(cons.size());
                cityTargetSendProvince.setOrgId(provinceCode);
                BigDecimal targetSum = BigDecimal.ZERO;
                for(PlanConsVo planConsVo : cons) {
                    targetSum = targetSum.add(planConsVo.getDemandCap());
                }
                if(null!=targetSum) {
                    targetSum = NumberUtil.div(targetSum,10000);
                }
                cityTargetSendProvince.setDemandCap(targetSum);
                cityTargetSends.add(cityTargetSendProvince);
                data.put("eventList",cityTargetSends);
                log.info("推送数据：" + cityTargetSends.toString());
                String res = null;
                SysOpLog sysOpLog = new SysOpLog();
                long id = IdWorker.getId();
                sysOpLog.setId(id);
                sysOpLog.setName("负控地市推送:地市数据"+cityTargetSends.size() + "条");
                sysOpLog.setParam(data.toString());
                sysOpLog.setOpTime(new Date());
                sysOpLog.setOpType(1);
                sysOpLog.setAccount(String.valueOf(eventId));
                sysOpLog.setSuccess("Y");
                sysOpLog.setMessage("成功");
                res = HttpUtil.post(url,data.toString());
                if(null!=res) {
                    JSONObject jsonObject = JSONObject.parseObject(res);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");
                    String dataStr = jsonObject.getString("data");
                    if("000000".equals(code)) {
                        log.info("数据保存成功,返回data为:" + dataStr);
                        //更新推送状态
                        sysOpLog.setResult(dataStr);
                        event.setSendStatus("1");
                        eventService.updateById(event);
                    } else {
                        log.warn("数据保存失败,返回data为:"+ dataStr);
                    }
                } else {
                    log.error("返回值为空，保存失败!");
                }
                sysOpLogService.save(sysOpLog);
            } else {
                return ResponseData.fail("-1", "无指标分解数据!", "无指标分解数据!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            redisTemplate.delete("sendCityTarget");
        }
        return ResponseData.success("数据推送中，请稍等一分钟","数据推送中，请稍等一分钟");
    }

    @Override
    public ResponseData sendBaseLineAndCons(EventParam eventParam) {
        Long eventId = eventParam.getEventId();
        if(null==eventId) {
            return ResponseData.fail("-1","事件编号为空!","事件编号为空!");
        }
        Event event = eventService.getById(eventId);
        //查询基线库
        Long baseLineId = eventMapper.getBaseLineIdByEventId(eventId);
        if(null==baseLineId) {
            return ResponseData.fail("-1","基线库编号为空!","基线库编号为空!");
        }
        //查询方案
        LambdaQueryWrapper<Plan> queryWrapperPlan = new LambdaQueryWrapper<>();
        queryWrapperPlan.eq(Plan::getRegulateId,eventId);
        List<Plan> planList = planService.list(queryWrapperPlan);
        if (null == planList || planList.size() == 0) {
            log.info("无方案信息");
            return ResponseData.fail("-1","无方案信息!","无方案信息!");
        }
        Long planId = planList.get(0).getPlanId();
        //查询最终执行的用户户号
        List<String> cons = planConsMapper.queryConsByPlanId(planList.get(0).getPlanId());
        if(null==cons || cons.size()==0) {
            return ResponseData.fail("-1","信息已同步完成，无法再次同步!","信息已同步完成，无法再次同步!!");
        }
        new Thread(() -> sendBaseLineAndConsAs(cons,planId,eventId,event,baseLineId,planList)).start();
        return ResponseData.success("信息正在推送，请稍等一分钟","信息正在推送，请稍等一分钟");
    }

    public ResponseData sendBaseLineAndConsAs(List<String> cons,Long planId,Long eventId,Event event,Long baseLineId,List<Plan> planList) {
        boolean flag = redisTemplate.hasKey("sendBaselineAndCons");
        if(flag) {
            return ResponseData.fail("-1","数据正在推送中!","数据正在推送中!");
        }
        try {
            //redis发送状态值,上锁
            Map<String, Object> eventInfo = new HashMap<>();
            eventInfo.put("sendStatus", "1");
            JSONObject eventInfoJson = new JSONObject(eventInfo);
            redisTemplate.convertAndSend("sendBaselineAndCons", eventInfoJson.toJSONString());
            //查询基线信息
            List<BaseLineSend> baseLineSends = baseLineDetailMapper.baselineSendList(baseLineId,cons);
            //调度指令编号
            Long regulateId = event.getRegulateId();
            String regulateName = null;
            Dispatch dispatch = dispatchService.getById(regulateId);
            String stregyId = null;
            if(null!=dispatch) {
                stregyId = dispatch.getStregyId();
            }
            if(null!=stregyId) {
                regulateName = stregyId;
            }
            int length = 0;
            if(null!=baseLineSends && baseLineSends.size()>0) {
                length =  baseLineSends.size();
            }
            //查询用户信息
            List<PlanConsVo> planCons = planConsMapper.getPlanConsInfoByPlanId(planList.get(0).getPlanId(),cons,baseLineId);
            if(null!=planCons && planCons.size()>0) {
                List<BaseLineSend> baseLineSendList = new ArrayList<>();
                List<PlanConsVo> planConsVoList = new ArrayList<>();
                String url = "";
                //查询数据字典url
                DictTypeParam dictTypeParam = new DictTypeParam();
                dictTypeParam.setCode("load_post_url");
                List<Dict> list = dictTypeService.dropDown(dictTypeParam);
                if(null!=list && list.size()>0) {
                    Object value = list.get(0).get("value");
                    if (null != value) {
                        url = (String) value;
                        if (null != url) {
                            url = url + "dict/cons-baseline/addTConsBaselines";
                        }
                    }
                } else {
                    log.error("无url配置信息！");
                    return ResponseData.fail("-1", "无url配置信息!", "无url配置信息!");
                }
                //查询字典配置一次发送最大数据量
                Integer count = 500;
                DictTypeParam dictTypeParam3 = new DictTypeParam();
                dictTypeParam3.setCode("baseline_data_count");
                List<Dict> list3 = dictTypeService.dropDown(dictTypeParam3);
                Object value2 = null;
                if (null != list3 && list3.size() > 0) {
                    for (Dict dict : list3) {
                        if ("1".equals(dict.get("code"))) {
                            value2 = dict.get("value");
                        }
                    }
                    if (null != value2) {
                        count = Integer.valueOf((String) value2);
                    }
                    if (null == count) {
                        count = 500;
                    }
                } else {
                    log.warn("未配置最大发送数据量!");
                }
                //获取所有组织机构
                List<SysOrgs> orgsList = null;
                JSONArray datas = null;
                List<SysOrgs> orgsListDate = new ArrayList<>();
                JSONObject result = systemClientService.queryAllOrg();
                if ("000000".equals(result.getString("code"))) {
                    datas = result.getJSONArray("data");
                    if (null != datas && datas.size() > 0) {
                        for (Object object : datas) {
                            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(object);
                            SysOrgs sysOrgs = JSONObjectToEntityUtils.JSONObjectToSysOrg(jsonObject);
                            orgsListDate.add(sysOrgs);
                        }
                    } else {
                        log.warn("组织机构为空");
                        return ResponseData.fail("-1", "组织机构为空!", "组织机构为空!");
                    }
                } else {
                    log.warn("组织机构不存在");
                    return ResponseData.fail("-1", "组织机构不存在!", "组织机构不存在!");
                }
                int i = planCons.size();
                for (int j = 0; j < i; j++) {
                    if (length > 0 && length > j) {
                        baseLineSends.get(j).setEventId(eventId);
                        //1：需求响应，2：有序用电，3：两高轮停
                        baseLineSends.get(j).setType("1");
                        baseLineSendList.add(baseLineSends.get(j));
                    }
                    planCons.get(j).setEventId(eventId);
                    planCons.get(j).setEventName(event.getEventName());
                    planCons.get(j).setPlanNo(stregyId);
                    planCons.get(j).setPlanName(regulateName);
                    planCons.get(j).setType("1");
                    String orgId = planCons.get(j).getOrgId();
                    if (null != orgId) {
                        orgsList = orgsListDate.stream().filter(n -> orgId.equals(n.getId())).collect(Collectors.toList());
                        if (null != orgsList && orgsList.size() > 0) {
                            String orgTitle = orgsList.get(0).getOrgTitle();
                            if (!"1".equals(orgTitle)) {
                                if ("2".equals(orgTitle)) {
                                    planCons.get(j).setOrgNo(orgId);
                                } else {
                                    OrgUtils orgUtils = new OrgUtils();
                                    String orNo = orgUtils.getData3(datas, orgId, new String());
                                    planCons.get(j).setOrgNo(orNo);
                                }
                            }
                        }
                    }
                    planCons.get(j).setPlanId(planId);
                    planConsVoList.add(planCons.get(j));
                    //每count条保存一次
                    if((j+1) % count == 0 || j == i - 1) {
                        JSONObject data = new JSONObject();
                        data.put("baseLine", baseLineSendList);
                        data.put("userList", planConsVoList);
                        String res = null;
                        log.info("基线数据推送条数：" + baseLineSendList.size() + "条");
                        log.info("用户数据推送条数：" + planConsVoList.size() + "条");
                        SysOpLog sysOpLog = new SysOpLog();
                        long id = IdWorker.getId();
                        sysOpLog.setId(id);
                        sysOpLog.setName("负控用户和基线推送:基线数据"+baseLineSendList.size() + "条"+";用户数据"+planConsVoList.size() + "条");
                        sysOpLog.setParam(data.toString());
                        sysOpLog.setOpTime(new Date());
                        sysOpLog.setOpType(1);
                        sysOpLog.setAccount(String.valueOf(eventId));
                        sysOpLog.setSuccess("Y");
                        sysOpLog.setMessage("成功");
                        res = HttpUtil.post(url,data.toString());
                        if(null!=res) {
                            JSONObject jsonObject = JSONObject.parseObject(res);
                            if(null!=jsonObject) {
                                sysOpLog.setResult(jsonObject.toString());
                                String code = jsonObject.getString("code");
                                String msg = jsonObject.getString("msg");
                                String dataStr = jsonObject.getString("data");
                                sysOpLog.setResult(dataStr);
                                if("000000".equals(code)) {
                                    log.info("数据保存成功,返回code为:" + code);
                                    log.info("数据保存成功,返回data为:" + dataStr);
                                    //更新推送状态
                                    planConsMapper.updateSendStatus(planConsVoList);
                                    /*if(dataStr) {
                                        log.info("数据保存成功,返回msg为:" + msg);
                                        planConsMapper.updateSendStatus(planConsVoList);
                                    } else {
                                        log.warn("数据处理失败,返回msg为:" + msg);
                                    }*/
                                } else {
                                    log.warn("数据保存失败,返回data为:"+ dataStr);
                                }
                            }
                        } else {
                            log.error("返回值为空，保存失败!");
                        }
                        sysOpLogService.save(sysOpLog);
                        Thread.sleep(2000);
                        planConsVoList = new ArrayList<>();
                        baseLineSendList = new ArrayList<>();
                    }
                }
            } else {
                log.info("无可推送信息用户");
                return ResponseData.fail("-1","无可推送信息用户!","无可推送信息用户!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            redisTemplate.delete("sendBaselineAndCons");
        }
        return ResponseData.success();
    }

}
