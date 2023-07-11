package com.xqxy.executor.service.jobhandler;

import cn.hutool.core.lang.Dict;
import cn.hutool.log.Log;
import com.xqxy.sys.modular.cust.entity.BlackName;
import com.xqxy.sys.modular.cust.mapper.BlackNameMapper;
import com.xqxy.sys.modular.cust.service.BlackNameService;
import com.xqxy.sys.modular.dict.param.DictTypeParam;
import com.xqxy.sys.modular.dict.service.DictTypeService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: czj
 * Date: 2021/11/30 11:38
 * Content: 定时查询，将不符合规则用户拉入黑名单
 */
@Component
public class BlackNameJob {
    private static final Log log = Log.get();

    @Autowired
    private DictTypeService dictTypeService;

    @Autowired
    private BlackNameMapper blackNameMapper;

    @Autowired
    private BlackNameService blackNameService;

    private final SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

    @XxlJob("blackName")
    public ReturnT<String> blackName(String param) throws Exception{
        XxlJobLogger.log("黑名单计算开始");
        this.updateBlackName();
        return ReturnT.SUCCESS;
    }

    public void updateBlackName() {
        try {
            //删除过期的黑名单
            blackNameMapper.deleteByTime();
            //黑名单用户集合
            List<BlackName> insertList = new ArrayList<>();
            List<String> cons = new ArrayList<>();
            //查询已有的黑名单记录
            List<BlackName> exsist = blackNameService.list();
            //年内负荷响应率未达标次数阈值(2次)
            Integer executeNum = null;
            //未参与响应次数阈值(3次)
            Integer unPartCount = null;
            //负荷响应率阈值(80%)
            BigDecimal executeRate = null;
            //过期年1
            Integer expireYear1 = null;
            //过期年2
            Integer expireYear2 = null;
            //获取字典配置
            DictTypeParam dictTypeParam = new DictTypeParam();
            dictTypeParam.setCode("black_name_num");
            List<Dict> dictDatas = dictTypeService.dropDown(dictTypeParam);
            if (null != dictDatas && dictDatas.size() > 0) {
                for (Dict dict : dictDatas) {
                    if ("1".equals(dict.get("code"))) {
                        executeNum = Integer.valueOf((String) dict.get("value"));
                    } else if ("2".equals(dict.get("code"))) {
                        unPartCount = Integer.valueOf((String) dict.get("value"));
                    } else if ("3".equals(dict.get("code"))) {
                        executeRate = new BigDecimal((String) dict.get("value"));
                    } else if ("4".equals(dict.get("code"))) {
                        expireYear1 = Integer.valueOf((String) dict.get("value"));
                    } else if ("5".equals(dict.get("code"))) {
                        expireYear2 = Integer.valueOf((String) dict.get("value"));
                    }
                }
                Calendar currCal = Calendar.getInstance();
                int currentYear = currCal.get(Calendar.YEAR) + expireYear1;
                Date date = getYearFirst(currentYear);
                //设置过期时间为明年第一天
                String expireTime = dateFormat2.format(date);
                currCal.clear();
                currCal = Calendar.getInstance();
                //设置过期时间为后年第一天
                int currentYear2 = currCal.get(Calendar.YEAR) + expireYear2;
                Date date2 = getYearFirst(currentYear2);
                String expireTime2 = dateFormat2.format(date2);
                //统计用户次不参与的次数
                List<BlackName> blackNamesInvications = blackNameMapper.getConsInvication();
                //查询用户次不参与的事件信息
                List<BlackName> blackNamesInvicationsEvent = blackNameMapper.getConsInvicationEvent();
                if (null != blackNamesInvications && blackNamesInvications.size() > 0) {
                    for (BlackName blackName : blackNamesInvications) {
                        //未达标次数
                        Integer count = blackName.getCount();
                        String eventNo = "";
                        String eventName = "";
                        String eventId = "";
                        //设置事件信息
                        if (null != blackNamesInvicationsEvent && blackNamesInvicationsEvent.size() > 0) {
                            List<BlackName> eventOne = blackNamesInvicationsEvent.stream().filter(blackName1 -> blackName1.getConsId().equals(blackName.getConsId())
                            ).collect(Collectors.toList());
                            int i=0;
                            if (null != eventOne && eventOne.size() > 0) {
                                for (BlackName event : eventOne) {
                                    if(i<=unPartCount) {
                                        eventNo =  eventNo + "," + event.getEventNo();
                                        eventName = eventName + "|" +  event.getEventName();
                                        eventId =  eventId + "," + event.getEventId();
                                    }
                                    i++;
                                }
                                eventNo = eventNo.substring(1);
                                eventName = eventName.substring(1);
                                eventId = eventId.substring(1);
                            }
                        }
                        //判断黑名单是否已经存在
                        if (null != exsist && exsist.size() > 0) {
                            List<BlackName> exsistOne = exsist.stream().filter(blackName1 -> blackName1.getConsId().equals(blackName.getConsId())
                            ).collect(Collectors.toList());
                            if (null == exsistOne || exsistOne.size() == 0) {
                                if (count > unPartCount) {
                                    blackName.setIsEffective("Y");
                                    blackName.setExpireTime(expireTime2);
                                    blackName.setRemark("年内未参与邀约次数超过" + unPartCount + "次!");
                                    blackName.setEventIds(eventId);
                                    blackName.setEventNames(eventName);
                                    blackName.setEventNos(eventNo);
                                    insertList.add(blackName);
                                    cons.add(blackName.getConsId());
                                }
                            }
                        } else {
                            if (count > unPartCount) {
                                blackName.setIsEffective("Y");
                                blackName.setExpireTime(expireTime2);
                                blackName.setRemark("年内未参与邀约次数超过" + unPartCount + "次!");
                                blackName.setEventIds(eventId);
                                blackName.setEventNames(eventName);
                                blackName.setEventNos(eventNo);
                                insertList.add(blackName);
                                cons.add(blackName.getConsId());
                            }
                        }
                    }
                } else {
                    log.info("没有未参与用户");
                }
                //统计用户次日效果评估负荷响应率未达标次数
                List<BlackName> blackNames = blackNameMapper.getEvalution(executeRate);
                //查询用户次日效果评估负荷响应率未达标参与的事件信息
                List<BlackName> blackNamesEvent = blackNameMapper.getEvalutionEvent(executeRate);
                if (null != blackNames && blackNames.size() > 0) {
                    for (BlackName blackName : blackNames) {
                        //未达标次数
                        Integer count = blackName.getCount();
                        String eventNo = "";
                        String eventName = "";
                        String eventId = "";
                        //设置事件信息
                        if (null != blackNamesEvent && blackNamesEvent.size() > 0) {
                            List<BlackName> eventOne = blackNamesEvent.stream().filter(blackName1 -> blackName1.getConsId().equals(blackName.getConsId())
                            ).collect(Collectors.toList());
                            int i=0;
                            if (null != eventOne && eventOne.size() > 0) {
                                for (BlackName event : eventOne) {
                                    if(i<=executeNum) {
                                        eventNo =  eventNo + "," + event.getEventNo();
                                        eventName = eventName + "|" +  event.getEventName();
                                        eventId =  eventId + "," + event.getEventId();
                                    }
                                }
                                eventNo = eventNo.substring(1);
                                eventName = eventName.substring(1);
                                eventId = eventId.substring(1);
                            }
                        }
                        //判断黑名单是否已经存在
                        if (null != exsist && exsist.size() > 0) {
                            List<BlackName> exsistOne = exsist.stream().filter(blackName1 -> blackName1.getConsId().equals(blackName.getConsId())
                            ).collect(Collectors.toList());
                            if (null == exsistOne || exsistOne.size() == 0) {
                                if (count > executeNum) {
                                    blackName.setIsEffective("Y");
                                    blackName.setExpireTime(expireTime);
                                    blackName.setRemark("年内负荷响应率未达到" + executeRate + ",且超过" + executeNum + "次!");
                                    blackName.setEventIds(eventId);
                                    blackName.setEventNames(eventName);
                                    blackName.setEventNos(eventNo);
                                    if(!cons.contains(blackName.getConsId())) {
                                        insertList.add(blackName);
                                    }
                                }
                            }
                        } else {
                            if (count > executeNum) {
                                blackName.setIsEffective("Y");
                                blackName.setExpireTime(expireTime);
                                blackName.setRemark("年内负荷响应率未达到" + executeRate + ",且超过" + executeNum + "次!");
                                blackName.setEventIds(eventId);
                                blackName.setEventNames(eventName);
                                blackName.setEventNos(eventNo);
                                if(!cons.contains(blackName.getConsId())) {
                                    insertList.add(blackName);
                                }
                            }
                        }
                    }
                } else {
                    log.info("没有未达标用户");
                }
            } else {
                XxlJobLogger.log("请先配置黑名单业务字典!");
                log.error("请先配置黑名单业务字典！");
            }
            //保存黑名单用户
            if (insertList.size() > 0) {
                blackNameService.saveBatch(insertList);
            }
            log.info("黑名单计算成功,共"+insertList.size()+"个用户加入黑名单!");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

        /**
         * 获取某年第一天日期
         * @param year 年份
         * @return Date
         */
        public static Date getYearFirst(int year){
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(Calendar.YEAR, year);
            Date currYearFirst = calendar.getTime();
            return currYearFirst;
        }
}
