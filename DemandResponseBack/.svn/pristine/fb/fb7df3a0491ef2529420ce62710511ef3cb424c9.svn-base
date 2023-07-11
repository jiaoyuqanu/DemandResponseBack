package com.xqxy.dr.modular.newloadmanagement.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.core.client.SystemClientService;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.baseline.util.CurveUtil;
import com.xqxy.dr.modular.device.entity.SysOrgs;
import com.xqxy.dr.modular.event.mapper.EventPowerBaseMapper;
import com.xqxy.dr.modular.newloadmanagement.entity.Drevent;
import com.xqxy.dr.modular.newloadmanagement.mapper.UserListMapper;
import com.xqxy.dr.modular.newloadmanagement.service.UserListService;
import com.xqxy.dr.modular.newloadmanagement.vo.EventLoadVo;
import com.xqxy.dr.modular.newloadmanagement.vo.ParamsVo;
import com.xqxy.dr.modular.newloadmanagement.vo.Point96Vo;
import com.xqxy.dr.modular.newloadmanagement.vo.UserListVo;
import com.xqxy.sys.modular.utils.JSONObjectToEntityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description 未到位用户清单service实现类 && 需求响当前时段执行事件及实时执行情况service实现类
 * @Author Rabbit
 * @Date 2022/6/10 10:03
 */
@Service
@Slf4j
public class UserListServiceImpl implements UserListService {
    @Resource
    private UserListMapper userListMapper;
    @Resource
    private SystemClientService systemClient;

    @Resource
    private EventPowerBaseMapper eventPowerBaseMapper;

    /**
     * 查询未到位用户列表
     * 1.根据当前时间查询当前时间在执行的事件id列表
     * 2.根据事件id列表，组织代码本级以及下级 (ORG_NO)，查询未到位用户列表，返回每个用户最新的监测数据
     *
     * @param page 由于mybatis-plus的分页插件，所以需要传入Page对象
     * @param data 组织代码本级以及下级 (ORG_NO)
     * @return List<UserListVo> 未到位用户列表
     * @author Rabbit
     */
    @Override
    public List<UserListVo> userInfo(Page<UserListVo> page, List<String> data) {
        // 获取当前日期 返回当前时间 年月日格式  yyyy-MM-dd
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        // 获取当前日期 返回当前时间 时分格式    HH:mm
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        log.info("组织代码本级以及下级" + data);
        log.info("查询未到位用户列表当前日期" + date);
        log.info("查询未到位用户列表当前时间" + time);
        // 根据日期和时间，获取当前时间正在执行的事件id，查询dr_event表返回event_id集合
        List<Drevent> drEventList = userListMapper.eventList(date, time);
        log.info("查询到的事件列表：" + drEventList);
        List<UserListVo> userListVos = null;
        if (CollectionUtil.isNotEmpty(drEventList) && drEventList.size() > 0) {
            // 根据event_id集合和组织代码本级以及下级 (ORG_NO)以及mybatis-plus的分页插件，传入Page对象，查询未到位用户列表，返回每个用户最新的监测数据
            userListVos = userListMapper.userListInfo(page, data, drEventList);
        } else {
            userListVos = new ArrayList<>();
        }
        log.info("userListVos集合：" + userListVos);
        return userListVos;
    }

    /**
     * 获取需求响当前时段执行事件及实时执行情况
     *
     * @param orgId 组织id 不传则返回省级加16个地市数据
     * @return List<EventLoadVo> 事件及实时执行情况
     * @author Rabbit
     */
    @Override
    public List<EventLoadVo> timeIntervalList(String orgId) {
        // 获取当前日期 返回当前时间 年月日格式  yyyy-MM-dd
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        // 获取当前日期 返回当前时间 时分格式    HH:mm
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        log.info("接收到负控参数：" + orgId);
        log.info("需求响当前时段执行事件及实时执行情况当前日期" + date);
        log.info("需求响当前时段执行事件及实时执行情况当前时间" + time);
        // 根据日期和时间，获取当前时间正在执行的事件id，查询dr_event表返回event_id集合
        List<Drevent> drEventList = userListMapper.eventList(date, time);
        // 只取第一个事件id
        Long eventId = 0L;
        if (ObjectUtil.isNotEmpty(drEventList) && drEventList.size() > 0) {
            eventId = drEventList.get(0).getEventId();
        }
        log.info("查询到的事件：" + eventId);
        if (eventId == 0) {
            log.warn("查询到的事件列表为空");
            return new ArrayList<>();
        } else {
            // orgId是否有值
            if (ObjectUtil.isNotEmpty(orgId)) { // 有值 进入下一步
                ResponseData<List<String>> allNextOrgId = systemClient.getAllNextOrgId(orgId);
                List<String> data = allNextOrgId.getData();
                log.info("data里面的数据：" + data);
                // 应邀负荷 应邀户数 应邀率
                EventLoadVo eventLoadVo = userListMapper.eventLoad(data, eventId);
                if (ObjectUtil.isNotEmpty(eventLoadVo.getInvitedLoad())) eventLoadVo.setInvitedLoad(accuracy(eventLoadVo.getInvitedLoad(), 1));
                if (ObjectUtil.isNotEmpty(eventLoadVo.getInvitedRate())) eventLoadVo.setInvitedRate(accuracy(eventLoadVo.getInvitedRate(), 2));
                // 组织机构ID
                eventLoadVo.setOrgId(orgId);
                // 单独查询邀约户数
                EventLoadVo eventLoadVo1 = userListMapper.eventLoadAll(data, eventId);
                // 赋值 邀约户数
                if (ObjectUtil.isNotEmpty(eventLoadVo1)) eventLoadVo.setInvitationCons(eventLoadVo1.getInvitationCons());
                // EventLoadVo eventLoadVo2 = userListMapper.realLoadAndExecuteRate(eventId, orgId);
                // // 通过当前时间 HH:mm 获取点数   1-96点 每15分钟一点
                // int point = CurveUtil.covDateTimeToPoint(time);
                // // 获取点p点的值
                // BigDecimal fieldValue = (BigDecimal) ReflectUtil.getFieldValue(eventLoadVo2, "p" + point);

                // 执行率   不取最新执行率，取上一个执行率
                EventLoadVo eventLoadVo2 = userListMapper.realTimeLoadAndExecutionRate(eventId, orgId);
                // 赋值 执行率
                if (ObjectUtil.isNotEmpty(eventLoadVo2)) eventLoadVo.setExecuteRate(accuracy(eventLoadVo2.getExecuteRate(), 2));

                ParamsVo paramsVo = userListMapper.realTimeCapAndExecuteTime(eventId, orgId);
                if (ObjectUtil.isNotEmpty(paramsVo) && ObjectUtil.isNotEmpty(paramsVo.getExecuteTime())) {
                    // 获取执行时间
                    String times = paramsVo.getExecuteTime().format(DateTimeFormatter.ofPattern("HH:mm"));
                    // 开始下标
                    int startIndex = 0;
                    if (null != times && !"".equals(times)) {
                        startIndex = CurveUtil.covDateTimeToPoint(times);
                    }
                    // 不取最新点，取最新点上一个点
                    String point = "P" + startIndex;
                    log.info("orgId是有值的执行时间" + times + "不取最新点，取最新点上一个点：" + point);
                    Point96Vo pointValue = eventPowerBaseMapper.selectPointValue(eventId, orgId);
                    // 获取该点值
                    BigDecimal fieldValue = (BigDecimal) ReflectUtil.getFieldValue(pointValue, point);
                    log.info("orgId是有值P点：" + point + "值：" + fieldValue);
                    // 实时负荷
                    if (ObjectUtil.isNotEmpty(paramsVo.getRealTimeCap()) && ObjectUtil.isNotEmpty(fieldValue))
                        eventLoadVo.setRealLoad(accuracy(fieldValue.subtract(paramsVo.getRealTimeCap()), 1));
                    log.info("orgId是有值的实时负荷：" + eventLoadVo.getRealLoad());
                }
                log.info("orgId是有值的," + orgId + "的当前时段执行事件及实时执行情况：" + eventLoadVo);
                // 将EventLoadVo对象放入集合, Collections.singletonList() ----> 不可变List，且只有一个元素。返回仅包含指定对象的不可变列表,返回的列表是可序列化的。
                return Collections.singletonList(eventLoadVo);
            } else if (ObjectUtil.isEmpty(orgId)) { // 没有值 进入下一步
                // 获取市级组织机构
                List<SysOrgs> orgsList = null;
                List<SysOrgs> orgsList1 = null;
                JSONArray datas = null;
                List<SysOrgs> orgsListDate = new ArrayList<>();
                // 获取所有组织机构
                JSONObject result = systemClient.queryAllOrg();
                if ("000000".equals(result.getString("code"))) {
                    datas = result.getJSONArray("data");
                    if (null != datas && datas.size() > 0) {
                        for (Object object : datas) {
                            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(object);
                            SysOrgs sysOrgs = JSONObjectToEntityUtils.JSONObjectToSysOrg(jsonObject);
                            orgsListDate.add(sysOrgs);
                        }
                    }
                    // 筛选省级组织机构 orgTitle-->1--->省级
                    orgsList1 = orgsListDate.stream().filter(n -> "1".equals(n.getOrgTitle())).collect(Collectors.toList());
                    // 筛选市级组织机构 orgTitle----->2---->市级
                    orgsList = orgsListDate.stream().filter(n -> "2".equals(n.getOrgTitle())).collect(Collectors.toList());
                }
                log.info("orgsList里面市级组织机构的数据：" + orgsList);
                log.info("orgsList里面省级组织机构的数据：" + orgsList1);
                List<String> id = new ArrayList<>();
                if (ObjectUtil.isNotEmpty(orgsList) && orgsList.size() > 0 && ObjectUtil.isNotEmpty(orgsList1) && orgsList1.size() > 0) {
                    orgsList.forEach(n -> id.add(n.getId()));
                    orgsList1.forEach(n -> id.add(n.getId()));
                }
                log.info("orgsList里面市级组织机构的id：" + id);
                List<EventLoadVo> eventLoadVoList = new ArrayList<>();
                for (String s : id) {
                    ResponseData<List<String>> allNextOrgId = systemClient.getAllNextOrgId(s);
                    List<String> data = allNextOrgId.getData();
                    log.info("data里面的数据：" + data);
                    // 应邀负荷 应邀户数 应邀率
                    EventLoadVo eventLoadVo = userListMapper.eventLoad(data, eventId);
                    if (ObjectUtil.isNotEmpty(eventLoadVo.getInvitedLoad())) eventLoadVo.setInvitedLoad(accuracy(eventLoadVo.getInvitedLoad(), 1));
                    if (ObjectUtil.isNotEmpty(eventLoadVo.getInvitedRate())) eventLoadVo.setInvitedRate(accuracy(eventLoadVo.getInvitedRate(), 2));
                    // 组织机构ID
                    eventLoadVo.setOrgId(s);
                    // 单独查询邀约户数
                    EventLoadVo eventLoadVo1 = userListMapper.eventLoadAll(data, eventId);
                    // 赋值 邀约户数
                    if (ObjectUtil.isNotEmpty(eventLoadVo1)) eventLoadVo.setInvitationCons(eventLoadVo1.getInvitationCons());
                    // 执行率  不取最新执行率，取上一个执行率
                    EventLoadVo eventLoadVo2 = userListMapper.realTimeLoadAndExecutionRate(eventId, s);
                    // 赋值 执行率
                    if (ObjectUtil.isNotEmpty(eventLoadVo2)) eventLoadVo.setExecuteRate(accuracy(eventLoadVo2.getExecuteRate(), 2));

                    ParamsVo paramsVo = userListMapper.realTimeCapAndExecuteTime(eventId, s);
                    if (ObjectUtil.isNotEmpty(paramsVo) && ObjectUtil.isNotEmpty(paramsVo.getExecuteTime())) {
                        // 获取执行时间
                        String times = paramsVo.getExecuteTime().format(DateTimeFormatter.ofPattern("HH:mm"));
                        // 开始下标
                        int startIndex = 0;
                        if (null != times && !"".equals(times)) {
                            startIndex = CurveUtil.covDateTimeToPoint(times);
                        }
                        // 不取最新点，取最新点上一个点
                        String point = "P" + startIndex;
                        log.info("orgId是有值的执行时间" + times + "不取最新点，取最新点上一个点：" + point);
                        Point96Vo pointValue = eventPowerBaseMapper.selectPointValue(eventId, s);
                        // 获取该点值
                        BigDecimal fieldValue = (BigDecimal) ReflectUtil.getFieldValue(pointValue, point);
                        log.info("orgId是有值P点：" + point + "值：" + fieldValue);
                        // 实时负荷
                        if (ObjectUtil.isNotEmpty(paramsVo.getRealTimeCap()) && ObjectUtil.isNotEmpty(fieldValue))
                            eventLoadVo.setRealLoad(accuracy(fieldValue.subtract(paramsVo.getRealTimeCap()), 1));
                        log.info("orgId是无值的实时负荷：" + eventLoadVo.getRealLoad());
                    }
                    log.info("orgId是无值的," + s + "的当前时段执行事件及实时执行情况：" + eventLoadVo);
                    eventLoadVoList.add(eventLoadVo);
                }
                // 应邀率和执行率转化成百分号形式
                // DecimalFormat decFormat = new DecimalFormat("#%");
                // System.out.println(decFormat.format(0.80));  // 80%
                log.info("orgId是无值的当前时段执行事件及实时执行情况：" + eventLoadVoList);
                return eventLoadVoList;
            }
        }
        return new ArrayList<>();
    }

    /**
     * BigDecimal 精度  四舍五入
     *
     * @param param bigDecimal 类型
     * @param type  1---->保留2位，2----->保留4位（百分比）
     * @return BigDecimal
     */
    private BigDecimal accuracy(BigDecimal param, int type) {
        if (type == 1) {
            return new BigDecimal(param.toPlainString()).setScale(2, BigDecimal.ROUND_HALF_UP);
        } else {
            return new BigDecimal(param.toPlainString()).setScale(4, BigDecimal.ROUND_HALF_UP);
        }
    }

}
