package com.xqxy.sys.modular.sms.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.client.SmsSendCilent;
import com.xqxy.core.client.SystemClient;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.core.util.SmsUtils;
import com.xqxy.dr.modular.event.entity.*;
import com.xqxy.dr.modular.event.enums.EventExceptionEnum;
import com.xqxy.dr.modular.event.enums.EventParticipateEnum;
import com.xqxy.dr.modular.event.mapper.ConsInvitationMapper;
import com.xqxy.dr.modular.event.mapper.CustInvitationMapper;
import com.xqxy.dr.modular.event.param.EventParam;
import com.xqxy.dr.modular.event.service.*;
import com.xqxy.dr.modular.project.enums.AdvanceNoticeEnums;
import com.xqxy.dr.modular.project.enums.ProjectTimeTypeEnum;
import com.xqxy.dr.modular.project.enums.ResponseTypeEnum;
import com.xqxy.dr.modular.project.enums.ResponseTypeEnums;
import com.xqxy.dr.modular.project.service.ConsContractDetailService;
import com.xqxy.dr.modular.project.service.ConsContractInfoService;
import com.xqxy.dr.modular.project.service.CustContractInfoService;
import com.xqxy.sys.modular.cust.entity.Cons;
import com.xqxy.sys.modular.cust.entity.Cust;
import com.xqxy.sys.modular.cust.entity.UserConsRela;
import com.xqxy.sys.modular.cust.enums.IsAggregatorEnum;
import com.xqxy.sys.modular.cust.mapper.BlackNameMapper;
import com.xqxy.sys.modular.cust.service.ConsService;
import com.xqxy.sys.modular.cust.service.CustService;
import com.xqxy.sys.modular.cust.service.UserConsRelaService;
import com.xqxy.sys.modular.sms.entity.SmsSendTemplate;
import com.xqxy.sys.modular.sms.entity.SysSmsSend;
import com.xqxy.sys.modular.sms.enums.*;
import com.xqxy.sys.modular.sms.mapper.SysSmsSendMapper;
import com.xqxy.sys.modular.sms.param.SmsSendParam;
import com.xqxy.sys.modular.sms.service.SmsSendTemplateService;
import com.xqxy.sys.modular.sms.service.SysSmsSendService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 * 短信发送记录 服务实现类
 * </p>
 *
 * @author Caoj
 * @since 2021-10-21
 */
@Service
public class SysSmsSendServiceImpl extends ServiceImpl<SysSmsSendMapper, SysSmsSend> implements SysSmsSendService {
    @Resource
    private ConsService consService;

    @Resource
    private SmsSendTemplateService smsSendTemplateService;

    @Resource
    private EventService eventService;

    @Resource
    private CustService custService;

    @Resource
    private CustInvitationService custInvitationService;

    @Resource
    private ConsInvitationService consInvitationService;

    @Resource
    private ConsInvitationMapper consInvitationMapper;

    @Resource
    private CustInvitationMapper custInvitationMapper;

    @Resource
    private PlanConsService planConsService;

    @Resource
    private PlanCustService planCustService;

    @Resource
    private UserConsRelaService userConsRelaService;

    @Autowired
    private BlackNameMapper blackNameMapper;

    @Resource
    private CustContractInfoService custContractInfoService;

    @Resource
    private ConsContractInfoService consContractInfoService;

    @Resource
    private ConsContractDetailService consContractDetailService;

    @Resource
    private SystemClient systemClient;

    @Resource
    private PlanService planService;


    /**
     * 短信服务
     */
    @Resource
    private SmsSendCilent smsSendCilent;

    @Override
    public String generateSms(String businessRela, String businessCode, Integer userType, String content) {
        if (ObjectUtil.isNotEmpty(businessCode)) {
            Integer code = Integer.valueOf(businessCode);
            if (code >= 40 && code < 50) {
                return eventCommon(businessCode, businessRela, userType, content);
            }
        }
        switch (Objects.requireNonNull(SmsTemplateTypeEnum.get(businessCode))) {
            case REGISTER_RESULT_SUCCESS:
            case REGISTER_RESULT_FAILED:
                // 注册认证审核通过/不通过
                return registerAuthentication(businessRela, businessCode);
            case DECLARE_RESULT_SUCCESS:
            case DECLARE_RESULT_FAILED:
                // 申报审核通过/不通过
                return signingVerify(businessRela, businessCode);
            case DECLARE_NOTICE:
                // 申报公示
                return declareNotice(businessRela);
            case INVITATION_NOTICE_ONCE:
                // 一次邀约通知
                return round1InviteSms(businessRela);
            case INVITATION_NOTICE_TWICE:
                // 二次邀约通知
                return round2InviteSms(businessRela);
            case RESULT_NOTICE:
                // 邀约结果确认
                return resultNotice(businessRela);
            case NOT_PARTICIPATE_EVENT:
                //  不参与事件
                return null;
            case EVENT_MESSAGE_CANCEL:
                // 事件取消通知
                return eventCancel(businessRela);
            case SUBSIDY_NOTICE:
                // 补贴公示
                return subsidyNotice(businessRela);
            case EXECUTE_SCHEME:
                // 日内执行通知
                return executeScheme(businessRela);
            case INVITATION_DEADLINE:
                // 邀约截止通知
                return deadlineNotice(businessRela);
            case INVITATION_DAY_CULL:
                // 日前应邀剔除短信
                return invitationDayCull(businessRela);
            //邀约提前通知短信
            case INVITATION_PRE_NOTICE:
                return invitePreNoticeSms(businessRela);
            case EVENT_COMMON:
                return eventCommon(businessCode, businessRela, userType, content);
            default:
                throw new ServiceException(SmsTemplateException.TEMPLATE_NOT_EXIST);
        }
    }

    @Override
    public String generateSms(String businessRela, String businessCode) {
        return this.generateSms(businessRela, businessCode, null, null);
    }

    /**
     * 日前应邀剔除短信
     *
     * @param businessRela
     * @return
     */
    private String invitationDayCull(String businessRela) {
        SmsSendTemplate templateByType = smsSendTemplateService.getTemplateByType(SmsTemplateTypeEnum.INVITATION_DAY_CULL.getCode());

        if (templateByType != null) {
            if (!StringUtils.isEmpty(businessRela)) {

                EventParam eventParam = new EventParam();
                eventParam.setEventId(Long.parseLong(businessRela));
                Event eventDetail = eventService.detail(eventParam);

                if (ObjectUtil.isNull(eventDetail)) {
                    throw new ServiceException(EventExceptionEnum.EVENT_NOT_EXIST);
                }

                List<PlanCons> planConsList = planConsService.list(Wrappers.<PlanCons>lambdaQuery()
                        .eq(PlanCons::getImplement, ImplementEnum.N.getCode())
                        .eq(PlanCons::getPlanId, eventDetail.getPlanId()));
                List<PlanCust> planCustomerList = planCustService.list(Wrappers.<PlanCust>lambdaQuery()
                        .eq(PlanCust::getImplement, ImplementEnum.N.getCode())
                        .eq(PlanCust::getPlanId, eventDetail.getPlanId()));

                List<Long> customer = new ArrayList<>();
                List<Cons> directCons = null;
                List<Cons> consList = new ArrayList<>();

                if (null != planConsList && planConsList.size() > 0) {
                    List<String> consIds = planConsList.stream().map(PlanCons::getConsId).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(consIds)) {
                        directCons = consService.list(Wrappers.<Cons>lambdaQuery().in(Cons::getId, consIds));
                        consList.addAll(directCons);

                        List<Long> directCustomer = directCons.stream().map(Cons::getCustId).collect(Collectors.toList());
                        List<UserConsRela> aggCustomer = userConsRelaService.list(Wrappers.<UserConsRela>lambdaQuery().in(UserConsRela::getConsNo, consIds));
                        List<Long> aggCustomerIds = aggCustomer.stream().map(UserConsRela::getCustId).collect(Collectors.toList());

                        customer.addAll(directCustomer);
                        customer.addAll(aggCustomerIds);
                    }
                }

                if (planCustomerList.size() > 0) {
                    List<Long> customerIds = planCustomerList.stream().map(PlanCust::getCustId).collect(Collectors.toList());
                    customer.addAll(customerIds);
                }

                if (CollectionUtils.isEmpty(customer)) {
                    throw new ServiceException(-1, "无剔除用户");
                }

                // 替换模板信息
                Map<String, String> paramsMap = new HashMap<>();
                paramsMap.put("duration", eventDetail.getRegulateDate() + " " + eventDetail.getStartTime() + "-" + eventDetail.getEndTime());
                String executeSms = getSmsText(paramsMap, templateByType.getTemplateContent());

                List<Long> customerIds = customer.stream().distinct().collect(Collectors.toList());
                List<Cust> customerList = custService.list(Wrappers.<Cust>lambdaQuery().in(Cust::getId, customerIds));

                customerList = customerList.stream().distinct().collect(Collectors.toList());
                List<SysSmsSend> smsSendList = new ArrayList<>();
                List<String> phonesEx = new ArrayList<>();
                //查询已经存在的电话号码
                SmsSendParam smsSendParam = new SmsSendParam();
                smsSendParam.setBusinessCode(SmsTemplateTypeEnum.INVITATION_DAY_CULL.getCode());
                smsSendParam.setBusinessRela(businessRela);
                // 返回手机号 过滤
                List<SysSmsSend> sysSmsSendList = new ArrayList<>();
                JSONObject jsonObject = smsSendCilent.getAllSmsByBusiness(smsSendParam);
                if (jsonObject != null) {
                    if ("000000".equals(jsonObject.getString("code"))) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (Object o : jsonArray) {
                            JSONObject jsonObject2 = JSONObject.fromObject(o);
                            SysSmsSend smsSend = this.jsonToSysSmsSend(jsonObject2);
                            sysSmsSendList.add(smsSend);
                        }
                    }
                }

                List<String> phones = sysSmsSendList.stream().map(SysSmsSend::getPhoneNumbers).collect(Collectors.toList());
                customerList.forEach(cust -> {
                    String join = null;
                    if (1 == cust.getIntegrator()) {
                        join = "3";
                    } else {
                        join = "4";
                        List<Cons> cons = consList.stream()
                                .filter(con -> cust.getId().equals(con.getCustId()))
                                .collect(Collectors.toList());
                        if (null != cons && cons.size() > 0) {
                            for (Cons con : cons) {
                                String firstContract = con.getFirstContactInfo();
                                SysSmsSend sysSmsSend2 = produceSms(executeSms, templateByType, con.getFirstContactInfo(),
                                        SmsSendStatusEnum.NOT_SEND.getCode(), businessRela, null, con.getId(), con.getConsName(), "1");
                                if (null != phones && phones.size() > 0) {
                                    if (!phones.contains(firstContract)) {
                                        if (!phonesEx.contains(firstContract)) {
                                            smsSendList.add(sysSmsSend2);
                                            phonesEx.add(firstContract);
                                        }
                                    }
                                } else {
                                    if (!phonesEx.contains(firstContract)) {
                                        smsSendList.add(sysSmsSend2);
                                        phonesEx.add(firstContract);
                                    }
                                }
                            }
                        }
                    }
                    SysSmsSend sysSmsSend = produceSms(executeSms, templateByType, cust.getTel(),
                            SmsSendStatusEnum.NOT_SEND.getCode(), businessRela, null, cust.getCreditCode(), cust.getLegalName(), join);
                    if (null != phones && phones.size() > 0) {
                        if (!phones.contains(cust.getTel())) {
                            if (!phonesEx.contains(cust.getTel())) {
                                smsSendList.add(sysSmsSend);
                                phonesEx.add(cust.getTel());
                            }
                        }
                    } else {
                        if (!phonesEx.contains(cust.getTel())) {
                            smsSendList.add(sysSmsSend);
                            phonesEx.add(cust.getTel());
                        }
                    }
                });

                if (smsSendList.size() > 0) {
                    smsSendCilent.saveBatch(smsSendList);
                }
                return templateByType.getTemplateContent();
            }
        }

        return templateByType.getTemplateContent();
    }

    /**
     * 邀约截止通知
     *
     * @param businessRela
     * @return
     */
    private String deadlineNotice(String businessRela) {
        SmsSendTemplate templateByType =
                smsSendTemplateService.getTemplateByType(SmsTemplateTypeEnum.INVITATION_DEADLINE.getCode());
        //查询截止未参与的用户
        List<Cons> consList = consInvitationMapper.listInvitationConsumerNoReply(businessRela);
        if (CollectionUtils.isEmpty(consList)) {
            return templateByType.getTemplateContent();
        }
        //剔除黑名单用户
        List<String> blackNames = blackNameMapper.getBlackNameConsIds();
        if (null == blackNames) {
            blackNames = new ArrayList<>();
        }
        for (int i = 0; i < consList.size(); i++) {
            String consId = consList.get(i).getId();
            if (blackNames.contains(consId)) {
                consList.remove(i);
                i--;
            }
        }
        List<Long> custIds = consList.stream().map(Cons::getCustId).collect(Collectors.toList());
        List<Cust> custList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(custIds)) {
            custList = custService.listByIds(custIds);
        }
        List<SysSmsSend> aggSmsSends = new ArrayList<>();
        List<String> phonesEx = new ArrayList<>();
        //查询已经存在的电话号码
        SmsSendParam smsSendParam = new SmsSendParam();
        smsSendParam.setBusinessCode(SmsTemplateTypeEnum.INVITATION_DEADLINE.getCode());
        smsSendParam.setBusinessRela(businessRela);

        // 返回手机号 过滤
        List<SysSmsSend> sysSmsSendList = new ArrayList<>();
        JSONObject jsonObject = smsSendCilent.getAllSmsByBusiness(smsSendParam);
        if (jsonObject != null) {
            if ("000000".equals(jsonObject.getString("code"))) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (Object o : jsonArray) {
                    JSONObject jsonObject2 = JSONObject.fromObject(o);
                    SysSmsSend smsSend = this.jsonToSysSmsSend(jsonObject2);
                    sysSmsSendList.add(smsSend);
                }
            }
        }

        List<String> phones = sysSmsSendList.stream().map(SysSmsSend::getPhoneNumbers).collect(Collectors.toList());
        custList.forEach(cust -> {
            String join = null;
            if (1 == cust.getIntegrator()) {
                join = "3";
            } else {
                join = "4";
            }
            //短信状态直接为未发送状态
            SysSmsSend sysSmsSend = produceSms(templateByType.getTemplateContent(), templateByType, cust.getTel(),
                    SmsSendStatusEnum.NOT_SEND.getCode(), businessRela, null, cust.getCreditCode(), cust.getLegalName(), join);
            if (null != phones && phones.size() > 0) {
                if (!phones.contains(cust.getTel())) {
                    if (!phonesEx.contains(cust.getTel())) {
                        aggSmsSends.add(sysSmsSend);
                        phonesEx.add(cust.getTel());
                    }
                }
            } else {
                if (!phonesEx.contains(cust.getTel())) {
                    aggSmsSends.add(sysSmsSend);
                    phonesEx.add(cust.getTel());
                }
            }
        });
        if (aggSmsSends.size() > 0) {
            smsSendCilent.saveBatch(aggSmsSends);
        }
        return templateByType.getTemplateContent();
    }

    @Override
    public void issueSms(SmsSendParam smsSendParam) {
//        if (SmsTemplateTypeEnum.INVITATION_NOTICE_ONCE.getCode().equals(smsSendParam.getBusinessCode())
//                || SmsTemplateTypeEnum.INVITATION_NOTICE_TWICE.getCode().equals(smsSendParam.getBusinessCode())) {
//            List<Cons> consList = consInvitationMapper.listInvitationConsumer(smsSendParam.getBusinessRela());
//            List<Long> invitationIds = consList.stream().map(Cons::getInvitationId).collect(Collectors.toList());
//            LambdaUpdateWrapper<SysSmsSend> lambdaUpdateWrapper = Wrappers.<SysSmsSend>lambdaUpdate()
//                    .eq(SysSmsSend::getBusinessCode, SmsTemplateTypeEnum.INVITATION_NOTICE_ONCE.getCode())
//                    .eq(SysSmsSend::getStatus, SmsSendStatusEnum.NOT_SEND.getCode());
//            if (CollectionUtils.isEmpty(invitationIds)) {
//                lambdaUpdateWrapper.in(SysSmsSend::getBusinessRela, -1);
//            } else {
//                lambdaUpdateWrapper.in(SysSmsSend::getBusinessRela, invitationIds);
//            }
//            lambdaUpdateWrapper.set(SysSmsSend::getStatus, SmsSendStatusEnum.WAITING_SEND.getCode());
//            update(lambdaUpdateWrapper);
//            List<Cust> custList = custInvitationMapper.listInvitationCustomer(smsSendParam.getBusinessRela());
//            // 集成商去重
//            List<Long> custInvitationIds = custList.stream().map(Cust::getInvitationId).collect(Collectors.toList());
//            if (!CollectionUtils.isEmpty(custInvitationIds)) {
//                update(Wrappers.<SysSmsSend>lambdaUpdate()
//                        .eq(SysSmsSend::getBusinessCode, SmsTemplateTypeEnum.INVITATION_NOTICE_ONCE.getCode())
//                        .in(SysSmsSend::getBusinessRela, custInvitationIds)
//                        .eq(SysSmsSend::getStatus, SmsSendStatusEnum.NOT_SEND.getCode())
//                        .set(SysSmsSend::getStatus, SmsSendStatusEnum.WAITING_SEND.getCode()));
//            }
//        } else {
        Event event = eventService.getById(smsSendParam.getBusinessRela());
        if ("06".equals(smsSendParam.getBusinessCode())) {
            //邀约短信下发
            List<Cons> consList = consInvitationMapper.listInvitationConsumer(smsSendParam.getBusinessRela());
            if (CollectionUtils.isEmpty(consList)) {
                throw new ServiceException(-1, "找不到邀约用户");
            }
            if (null != event) {
                //仅已发布，且邀约未截止可操作
                if (!"06".equals(event.getEventStatus())) {
                    throw new ServiceException(EventExceptionEnum.EVENT_RELEASE_STATE);
                }
                LocalDateTime localDate = LocalDateTime.now();
                LocalDateTime deadLineTime = consList.get(0).getDeadlineTime();
                if (localDate.compareTo(deadLineTime) > 0) {
                    throw new ServiceException(EventExceptionEnum.DEADLINE_TIME_NO);
                }
            } else {
                throw new ServiceException(EventExceptionEnum.EVENT_NOT_EXIST);
            }
        } else if ("12".equals(smsSendParam.getBusinessCode())) {
            //事件撤销短信下发
            if (null != event) {
                //仅撤销状态可操作
                if (!"07".equals(event.getEventStatus())) {
                    throw new ServiceException(EventExceptionEnum.EVENT_REVOKE_STATE);
                }
            } else {
                throw new ServiceException(EventExceptionEnum.EVENT_NOT_EXIST);
            }
        } else if ("14".equals(smsSendParam.getBusinessCode())) {
            //事件执行短信下发
            if (null != event) {
                //仅执行中状态可剔除规则,且事件未开始
                if (!"03".equals(event.getEventStatus())) {
                    throw new ServiceException(EventExceptionEnum.EVENT_EXECUTE_COM_STATE);
                }
                Integer endHour = 0;
                Integer endMinute = 0;
                if (null != event.getStartTime()) {
                    endHour = Integer.parseInt(event.getStartTime().substring(0, 2));
                    endMinute = Integer.parseInt(event.getStartTime().substring(3));
                }
                LocalDate localDate = LocalDate.now();
                LocalTime startTime = LocalTime.of(endHour, endMinute);
                if (event.getRegulateDate().compareTo(localDate) == 0) {
                    if (LocalTime.now().compareTo(startTime) > 0) {
                        throw new ServiceException(EventExceptionEnum.EVENT_BEGIN);
                    }
                } else if (event.getRegulateDate().compareTo(localDate) < 0) {
                    throw new ServiceException(EventExceptionEnum.EVENT_BEGIN);
                }
            } else {
                throw new ServiceException(EventExceptionEnum.EVENT_NOT_EXIST);
            }
        } else if ("17".equals(smsSendParam.getBusinessCode())) {
            //事件执行短信下发
            if (null != event) {
                //仅执行中状态可剔除规则,且事件未开始
                if (!"03".equals(event.getEventStatus())) {
                    throw new ServiceException(EventExceptionEnum.EVENT_EXECUTE_COM_STATE);
                }
                Integer endHour = 0;
                Integer endMinute = 0;
                if (null != event.getStartTime()) {
                    endHour = Integer.parseInt(event.getStartTime().substring(0, 2));
                    endMinute = Integer.parseInt(event.getStartTime().substring(3));
                }
                LocalDate localDate = LocalDate.now();
                LocalTime startTime = LocalTime.of(endHour, endMinute);
                if (event.getRegulateDate().compareTo(localDate) == 0) {
                    if (LocalTime.now().compareTo(startTime) > 0) {
                        throw new ServiceException(EventExceptionEnum.EVENT_BEGIN);
                    }
                } else if (event.getRegulateDate().compareTo(localDate) < 0) {
                    throw new ServiceException(EventExceptionEnum.EVENT_BEGIN);
                }
            } else {
                throw new ServiceException(EventExceptionEnum.EVENT_NOT_EXIST);
            }
        }
//        update(Wrappers.<SysSmsSend>lambdaUpdate()
//                .eq(SysSmsSend::getStatus, SmsSendStatusEnum.NOT_SEND.getCode())
//                .eq(SysSmsSend::getBusinessRela, smsSendParam.getBusinessRela())
//                .eq(ObjectUtil.isNotNull(smsSendParam.getBusinessCode()), SysSmsSend::getBusinessCode, smsSendParam.getBusinessCode())
//                .set(SysSmsSend::getStatus, SmsSendStatusEnum.WAITING_SEND.getCode()));
        smsSendCilent.issueSms(smsSendParam);
//        }
    }

    @Override
    public Page<SysSmsSend> getSmsByBusiness(SmsSendParam smsSendParam) {
        Page<SysSmsSend> page = new Page<>();
        List<SysSmsSend> list = new ArrayList<>();
        JSONObject jsonObject = smsSendCilent.getSmsByBusiness(smsSendParam);
        if (null != jsonObject) {
            if ("000000".equals(jsonObject.getString("code"))) {
                JSONObject jsonObject1 = jsonObject.getJSONObject("data");

                String total = jsonObject1.getString("total");
                String size = jsonObject1.getString("size");
                String current = jsonObject1.getString("current");
                String orders = jsonObject1.getString("orders");
                String pages = jsonObject1.getString("pages");

                page.setTotal(Long.valueOf(total));
                page.setSize(Long.valueOf(size));
                page.setCurrent(Long.valueOf(current));
                page.setPages(Long.valueOf(pages));
                Long currentCount = (page.getCurrent() - 1) * page.getSize();
                Long nextCount = currentCount + page.getSize();
                JSONArray jsonArray = jsonObject1.getJSONArray("records");
              /*  for (Object o : jsonArray) {
                    JSONObject jsonObject2 = JSONObject.fromObject(o);
                    SysSmsSend smsSend = this.jsonToSysSmsSend(jsonObject2);
                    list.add(smsSend);
                }*/
                int length = jsonArray.size();
                if (length > 0) {
                    for (int i = currentCount.intValue(); i < nextCount; i++) {
                        if (length > i) {
                            Object o = jsonArray.get(i);
                            JSONObject jsonObject2 = JSONObject.fromObject(o);
                            SysSmsSend smsSend = this.jsonToSysSmsSend(jsonObject2);
                            list.add(smsSend);
                        }
                    }
                }
            }
        }

        page.setRecords(list);
        return page;
    }


    /**
     * 注册审核
     *
     * @param businessRela 关联审核客户id
     * @param businessCode 关联短信模板类型
     * @return java.lang.String
     * @author Caoj
     * @since 10/28/2021 16:35
     */
    String registerAuthentication(String businessRela, String businessCode) {
        Cust cust = custService.getById(businessRela);
        // 短信模板中需要替换的内容
        SmsSendTemplate templateByType = smsSendTemplateService.getTemplateByType(businessCode);
        String templateContent = templateByType.getTemplateContent();
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("year", new SimpleDateFormat("yyyy").format(new Date()));
        String smsText = getSmsText(paramsMap, templateContent);
        // 生成短信
        SysSmsSend sysSms = this.detailByBusiness(businessRela, templateByType.getTemplateType());
        if (ObjectUtil.isNull(sysSms)) {
            SysSmsSend sysSmsSend = produceSms(smsText, templateByType, cust.getTel(),
                    SmsSendStatusEnum.WAITING_SEND.getCode(), businessRela, null, cust.getCreditCode(), cust.getLegalName(), null);
            List<SysSmsSend> list = new ArrayList<>();
            list.add(sysSmsSend);
            smsSendCilent.saveBatch(list);
        } else {
            return "注册短信已经存在";
        }
        return smsText;
    }

    /**
     * 签约审核
     *
     * @param businessRela 关联审核用户id
     * @param businessCode 关联短信模板类型
     * @return java.lang.String
     * @author Caoj
     * @since 10/28/2021 16:34
     */
    String signingVerify(String businessRela, String businessCode) {
        SmsSendTemplate templateByType = smsSendTemplateService.getTemplateByType(businessCode);
        Cons cons = consService.getById(businessRela);
        if (ObjectUtil.isNull(cons)) {
            throw new ServiceException(-1, "签约用户不存在");
        }
        // 生成短信
        SysSmsSend sysSms = this.detailByBusiness(businessRela, templateByType.getTemplateType());
        if (ObjectUtil.isNull(sysSms)) {
            SysSmsSend sysSmsSend = produceSms(templateByType.getTemplateContent(), templateByType,
                    cons.getFirstContactInfo(), SmsSendStatusEnum.WAITING_SEND.getCode(), businessRela, null, cons.getId(), cons.getConsName(), null);
            List<SysSmsSend> list = new ArrayList<>();
            list.add(sysSmsSend);
            smsSendCilent.saveBatch(list);
        } else {
            return "审核短信已存在";
        }
        return templateByType.getTemplateContent();
    }

    /**
     * 签约公告
     * 申报公示发给客户
     *
     * @param businessRela 关联公告id
     * @return java.lang.String
     * @author Caoj updated on 12/30/2021 10:10
     */
    String declareNotice(String businessRela) {
        SmsSendTemplate templateByType =
                smsSendTemplateService.getTemplateByType(SmsTemplateTypeEnum.DECLARE_NOTICE.getCode());
        List<Cust> custList = custService.list(Wrappers.<Cust>lambdaQuery().groupBy(Cust::getTel));
//        List<String> custPhoneNum = custList.stream().map(Cust::getTel).collect(Collectors.toList());
//        Map<String,String> custPhoneNum = custList.stream().collect(Collectors.toMap(Cust::getTel,Cust::getCustName));

        // 查找是否生成这类短信
        List<SysSmsSend> sysSmsSends = list(Wrappers.<SysSmsSend>lambdaQuery()
                .eq(SysSmsSend::getBusinessRela, businessRela)
                .eq(SysSmsSend::getBusinessCode, SmsTemplateTypeEnum.DECLARE_NOTICE.getCode()));
        sysSmsSends.forEach(sysSmsSend -> {
//            Iterator<String> iterator = custPhoneNum.listIterator();
//            if (iterator.hasNext() && sysSmsSend.getPhoneNumbers().equals(iterator.next())) {
//                iterator.remove();
//            }
//            custPhoneNum.entrySet().stream().filter(e-> sysSmsSend.getPhoneNumbers().equals(e.getKey())).collect(Collectors.toMap(e-> e.getKey(),e-> e.getValue()));
            List<Cust> custs = custList.stream()
                    .filter(c -> sysSmsSend.getPhoneNumbers().equals(c.getTel()))
                    .collect(Collectors.toList());
            custList.removeAll(custs);

        });

        List<SysSmsSend> smsSends = new ArrayList<>();
        custList.forEach(cust -> {
            SysSmsSend sysSmsSend = produceSms(templateByType.getTemplateContent(),
                    templateByType, cust.getTel(), SmsSendStatusEnum.WAITING_SEND.getCode(), businessRela, null, cust.getCreditCode(), cust.getLegalName(), null);
            smsSends.add(sysSmsSend);
        });
        if (CollectionUtils.isEmpty(smsSends)) {
            return "签约公示短信已存在";
        } else {
            smsSendCilent.saveBatch(smsSends);
            return templateByType.getTemplateContent();
        }
    }


    String invitePreNoticeSms(String businessRela) {
        try {

            Event event = eventService.getById(businessRela);
            if (event == null) {
                throw new ServiceException(-1, "找不到邀约事件");
            }
            Long projectId = event.getProjectId();
            List<Long> consIdList = consContractInfoService.getConsByContractInfo(projectId.toString(), event.getResponseType(), event.getTimeType(), event.getAdvanceNoticeTime());
            List<Cons> consList = consService.listByIds(consIdList);

            String regulateRange = event.getRegulateRange();
            JSONArray jsonArray = JSONArray.fromObject(regulateRange);
            Set<String> regulateRangeSet = new HashSet<>();
            List<Pattern> regPatterns = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONArray jsonArray2 = jsonArray.getJSONArray(i);
                if (jsonArray2.size() > 0) {
                    String lastCode = jsonArray2.getString(jsonArray2.size() - 1);
                    if (lastCode.length() != 6) {
                        ResponseData<List<String>> allNextOrgId = systemClient.getAllNextOrgId(lastCode);
                        regulateRangeSet.addAll(allNextOrgId.getData());
                    } else {
                        String reg01 = lastCode.substring(0, 2);
                        String reg02 = lastCode.substring(2, 4);
                        String reg03 = lastCode.substring(4, 6);
                        if (reg01.equals("00")) {
                            regPatterns.add(Pattern.compile("\\d{6}"));
                        } else if (reg02.equals("00")) {
                            regPatterns.add(Pattern.compile(reg01 + "\\d{4}"));
                        } else if (reg03.equals("00")) {
                            regPatterns.add(Pattern.compile(reg01 + reg02 + "\\d{2}"));
                        } else {
                            regPatterns.add(Pattern.compile(reg01 + reg02 + reg03));
                        }
                    }
                }
            }
            consList = consList.stream().filter(item -> {
                if (regulateRangeSet.contains(item.getOrgNo())) {
                    return true;
                }
                for (Pattern regPattern : regPatterns) {
                    if (ObjectUtil.isNotEmpty(item.getCountyCode()) && regPattern.matcher(item.getCountyCode()).find()) {
                        return true;
                    }
                }
                return false;
            }).collect(Collectors.toList());

            if (CollectionUtils.isEmpty(consList)) {
                throw new ServiceException(-1, "找不到邀约用户");
            }
            //剔除黑名单用户
            List<String> blackNames = blackNameMapper.getBlackNameConsIds();
            if (null == blackNames) {
                blackNames = new ArrayList<>();
            }
            for (int i = 0; i < consList.size(); i++) {
                String consId = consList.get(i).getId();
                if (blackNames.contains(consId)) {
                    consList.remove(i);
                    i--;
                }
            }
            List<Long> custIds = consList.stream().map(Cons::getCustId).collect(Collectors.toList());
            custIds.addAll(userConsRelaService.lambdaQuery()
                    .in(UserConsRela::getConsNo, consList.stream().map(Cons::getId).collect(Collectors.toList()))
                    .list().stream().map(UserConsRela::getCustId).collect(Collectors.toList())
            );
            custIds = custIds.stream().distinct().collect(Collectors.toList());

            List<Cust> custList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(custIds)) {
                custList = custService.listByIds(custIds);
            }


            SmsSendTemplate round1Template =
                    smsSendTemplateService.getTemplateByType(SmsTemplateTypeEnum.INVITATION_PRE_NOTICE.getCode());
            String round1Message = round1Template.getTemplateContent();
            // 替换模板信息
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("duration", event.getRegulateDate() + " " +
                    event.getStartTime() + "-" + event.getEndTime());
            String round1Sms = getSmsText(paramsMap, round1Message);

            List<SysSmsSend> aggSmsSends = new ArrayList<>();
            List<String> phonesEx = new ArrayList<>();
            //查询已经存在的电话号码
            SmsSendParam smsSendParam = new SmsSendParam();
            smsSendParam.setBusinessCode(SmsTemplateTypeEnum.INVITATION_PRE_NOTICE.getCode());
            smsSendParam.setBusinessRela(businessRela);

            // 返回手机号 过滤
            List<SysSmsSend> sysSmsSendList = new ArrayList<>();
            JSONObject jsonObject = smsSendCilent.getAllSmsByBusiness(smsSendParam);
            if (jsonObject != null) {
                if ("000000".equals(jsonObject.getString("code"))) {
                    JSONArray jsonArray2 = jsonObject.getJSONArray("data");
                    for (Object o : jsonArray2) {
                        JSONObject jsonObject2 = JSONObject.fromObject(o);
                        SysSmsSend smsSend = this.jsonToSysSmsSend(jsonObject2);
                        sysSmsSendList.add(smsSend);
                    }
                }
            }

            List<String> phones = sysSmsSendList.stream().map(SysSmsSend::getPhoneNumbers).collect(Collectors.toList());
            //设置值
            custList.forEach(cust -> {
                String join = null;
                if (1 == cust.getIntegrator()) {
                    //集成商不给代理用户发
                    join = "3";
                } else {
                    //直接用户给客户发
                    join = "4";
                }
                SysSmsSend sysSmsSend = produceSms(round1Sms, round1Template, cust.getTel(),
                        SmsSendStatusEnum.NOT_SEND.getCode(), businessRela, null, cust.getCreditCode(), cust.getLegalName(), join);
                if (null != phones && phones.size() > 0) {
                    if (!phones.contains(cust.getTel())) {
                        if (!phonesEx.contains(cust.getTel())) {
                            aggSmsSends.add(sysSmsSend);
                            phonesEx.add(cust.getTel());
                        }
                    }
                } else {
                    if (!phonesEx.contains(cust.getTel())) {
                        aggSmsSends.add(sysSmsSend);
                        phonesEx.add(cust.getTel());
                    }
                }
            });

            // 短信记录去重
        /* List<SysSmsSend> custSms = list(Wrappers.<SysSmsSend>lambdaQuery()
                .eq(SysSmsSend::getBusinessCode, SmsTemplateTypeEnum.INVITATION_NOTICE_ONCE.getCode())
                .eq(SysSmsSend::getBusinessRela, businessRela));
       custSms.forEach(sms -> {
            Iterator<SysSmsSend> iterator = aggSmsSends.iterator();
            if (iterator.hasNext() && iterator.next().getPhoneNumbers().equals(sms.getPhoneNumbers())) {
                iterator.remove();
            }
        });*/
            if (aggSmsSends.size() > 0) {
                smsSendCilent.saveBatch(aggSmsSends);
            }
            return round1Sms;
        } catch (Exception e) {
            log.error("生成短信错误", e);
            throw new ServiceException(-1, "生成短信错误");
        }
    }

    /**
     * 通用短信
     *
     * @param businessCode 短信类型
     * @param businessRela 短信标记字段 事件id
     * @param userType     发送短信用户类型 1: 签约用户 2: 邀约用户 3: 执行用户
     * @param content      发送短信内容
     */
    private String eventCommon(String businessCode, String businessRela, Integer userType, String content) {
        Event event = eventService.getById(businessRela);
        if (event == null) {
            throw new ServiceException(-1, "找不到邀约事件");
        }
        List<Cons> consList = null;
        if (userType == 1) {
            // 获取签约用户列表
            Long projectId = event.getProjectId();
            List<Long> consIdList = consContractInfoService.getConsByContractInfo(projectId.toString(), event.getResponseType(), event.getTimeType(), event.getAdvanceNoticeTime());
            consList = consService.listByIds(consIdList);

            String regulateRange = event.getRegulateRange();
            JSONArray jsonArray = JSONArray.fromObject(regulateRange);
            Set<String> regulateRangeSet = new HashSet<>();
            List<Pattern> regPatterns = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONArray jsonArray2 = jsonArray.getJSONArray(i);
                if (jsonArray2.size() > 0) {
                    String lastCode = jsonArray2.getString(jsonArray2.size() - 1);
                    if (lastCode.length() != 6) {
                        ResponseData<List<String>> allNextOrgId = systemClient.getAllNextOrgId(lastCode);
                        regulateRangeSet.addAll(allNextOrgId.getData());
                    } else {
                        String reg01 = lastCode.substring(0, 2);
                        String reg02 = lastCode.substring(2, 4);
                        String reg03 = lastCode.substring(4, 6);
                        if (reg01.equals("00")) {
                            regPatterns.add(Pattern.compile("\\d{6}"));
                        } else if (reg02.equals("00")) {
                            regPatterns.add(Pattern.compile(reg01 + "\\d{4}"));
                        } else if (reg03.equals("00")) {
                            regPatterns.add(Pattern.compile(reg01 + reg02 + "\\d{2}"));
                        } else {
                            regPatterns.add(Pattern.compile(reg01 + reg02 + reg03));
                        }
                    }
                }
            }
            consList = consList.stream().filter(item -> {
                if (regulateRangeSet.contains(item.getOrgNo())) {
                    return true;
                }
                for (Pattern regPattern : regPatterns) {
                    if (ObjectUtil.isNotEmpty(item.getCountyCode()) && regPattern.matcher(item.getCountyCode()).find()) {
                        return true;
                    }
                }
                return false;
            }).collect(Collectors.toList());

            if (CollectionUtils.isEmpty(consList)) {
                throw new ServiceException(-1, "找不到邀约用户");
            }
            //剔除黑名单用户
            List<String> blackNames = blackNameMapper.getBlackNameConsIds();
            if (null == blackNames) {
                blackNames = new ArrayList<>();
            }
            for (int i = 0; i < consList.size(); i++) {
                String consId = consList.get(i).getId();
                if (blackNames.contains(consId)) {
                    consList.remove(i);
                    i--;
                }
            }
        } else {
            Plan plan = planService.getByEventId(event.getEventId());
            if (plan == null) {
                throw new ServiceException(2, "方案不存在");
            }
            List<String> consIdList = new ArrayList<>();
            if (userType == 2) {
                //获取邀约用户列表
                LambdaQueryWrapper<PlanCons> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(PlanCons::getPlanId, plan.getPlanId());
                lambdaQueryWrapper.eq(PlanCons::getInvolvedIn, "Y");
                lambdaQueryWrapper.eq(PlanCons::getDeleted, "N");
                lambdaQueryWrapper.select(PlanCons::getConsId);
                consIdList = planConsService.list(lambdaQueryWrapper).stream().map(PlanCons::getConsId).collect(Collectors.toList());
                if (ObjectUtil.isEmpty(consIdList)) {
                    throw new ServiceException(3, "邀约用户为空");
                }
            } else if (userType == 3) {
                // 获取执行用户户号列表
                LambdaQueryWrapper<PlanCons> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(PlanCons::getPlanId, plan.getPlanId());
                lambdaQueryWrapper.eq(PlanCons::getImplement, "Y");
                lambdaQueryWrapper.select(PlanCons::getConsId);
                consIdList = planConsService.list(lambdaQueryWrapper).stream().map(PlanCons::getConsId).collect(Collectors.toList());
                if (ObjectUtil.isEmpty(consIdList)) {
                    throw new ServiceException(4, "执行用户为空");
                }
            }
            consList = consService.listByIds(consIdList);
            if (ObjectUtil.isEmpty(consList)) {
                throw new ServiceException(5, "找不到发送短信用户户号");
            }
        }

        List<Long> custIds = consList.stream().map(Cons::getCustId).collect(Collectors.toList());
        custIds.addAll(userConsRelaService.lambdaQuery()
                .in(UserConsRela::getConsNo, consList.stream().map(Cons::getId).collect(Collectors.toList()))
                .list().stream().map(UserConsRela::getCustId).collect(Collectors.toList())
        );
        custIds = custIds.stream().distinct().collect(Collectors.toList());

        List<Cust> custList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(custIds)) {
            custList = custService.listByIds(custIds);
        }


        SmsSendTemplate round1Template =
                smsSendTemplateService.getTemplateByType(businessCode);

        String round1Sms = eventService.parseEventSmsContent(event.getEventId(), content);

        List<SysSmsSend> aggSmsSends = new ArrayList<>();
        List<String> phonesEx = new ArrayList<>();
        //查询已经存在的电话号码
        SmsSendParam smsSendParam = new SmsSendParam();
        smsSendParam.setBusinessCode(businessCode);
        smsSendParam.setBusinessRela(businessRela);

        // 返回手机号 过滤
//            List<SysSmsSend> sysSmsSendList = new ArrayList<>();
//            JSONObject jsonObject = smsSendCilent.getAllSmsByBusiness(smsSendParam);
//            if (jsonObject != null) {
//                if ("000000".equals(jsonObject.getString("code"))) {
//                    JSONArray jsonArray2 = jsonObject.getJSONArray("data");
//                    for (Object o : jsonArray2) {
//                        JSONObject jsonObject2 = JSONObject.fromObject(o);
//                        SysSmsSend smsSend = this.jsonToSysSmsSend(jsonObject2);
//                        sysSmsSendList.add(smsSend);
//                    }
//                }
//            }

//            List<String> phones = sysSmsSendList.stream().map(SysSmsSend::getPhoneNumbers).collect(Collectors.toList());
        //设置值
        custList.forEach(cust -> {
            String join = null;
            if (1 == cust.getIntegrator()) {
                //集成商不给代理用户发
                join = "3";
            } else {
                //直接用户给客户发
                join = "4";
            }
            SysSmsSend sysSmsSend = produceSms(round1Sms, round1Template, cust.getTel(),
                    SmsSendStatusEnum.NOT_SEND.getCode(), businessRela, null, cust.getCreditCode(), cust.getLegalName(), join);
//                if (null != phones && phones.size() > 0) {
//                    if (!phones.contains(cust.getTel())) {
//                        if (!phonesEx.contains(cust.getTel())) {
//                            aggSmsSends.add(sysSmsSend);
//                            phonesEx.add(cust.getTel());
//                        }
//                    }
//                } else {
            if (!phonesEx.contains(cust.getTel())) {
                aggSmsSends.add(sysSmsSend);
                phonesEx.add(cust.getTel());
            }
//                }
        });

        // 短信记录去重
        /* List<SysSmsSend> custSms = list(Wrappers.<SysSmsSend>lambdaQuery()
                .eq(SysSmsSend::getBusinessCode, SmsTemplateTypeEnum.INVITATION_NOTICE_ONCE.getCode())
                .eq(SysSmsSend::getBusinessRela, businessRela));
       custSms.forEach(sms -> {
            Iterator<SysSmsSend> iterator = aggSmsSends.iterator();
            if (iterator.hasNext() && iterator.next().getPhoneNumbers().equals(sms.getPhoneNumbers())) {
                iterator.remove();
            }
        });*/
        if (aggSmsSends.size() > 0) {
            SmsSendParam delSmsSendParam = new SmsSendParam();
            delSmsSendParam.setBusinessCode(businessCode);
            delSmsSendParam.setBusinessRela(businessRela);
            smsSendCilent.deleteSms(delSmsSendParam);
            smsSendCilent.saveBatch(aggSmsSends);
        }
        return round1Sms;
    }

    /**
     * 一次邀约
     *
     * @param businessRela 关联事件id
     * @return java.lang.String
     * @author Caoj
     * @since 10/28/2021 16:31
     */
    String round1InviteSms(String businessRela) {
        List<Cons> consList = consInvitationMapper.listInvitationConsumer(businessRela);
        if (CollectionUtils.isEmpty(consList)) {
            throw new ServiceException(-1, "找不到邀约用户");
        }
        Event event = eventService.getById(businessRela);
        if (null != event) {
            //仅已发布且邀约未截止可操作
            if (!"06".equals(event.getEventStatus())) {
                throw new ServiceException(EventExceptionEnum.EVENT_RELEASE_STATE);
            }
            LocalDateTime localDate = LocalDateTime.now();
            LocalDateTime deadLineTime = consList.get(0).getDeadlineTime();
            if (localDate.compareTo(deadLineTime) > 0) {
                throw new ServiceException(EventExceptionEnum.DEADLINE_TIME_NO);
            }
        } else {
            throw new ServiceException(EventExceptionEnum.EVENT_NOT_EXIST);
        }
        SmsSendTemplate round1Template =
                smsSendTemplateService.getTemplateByType(SmsTemplateTypeEnum.INVITATION_NOTICE_ONCE.getCode());
        String round1Message = round1Template.getTemplateContent();
        // 替换模板信息
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("duration", consList.get(0).getRegulateDate() + " " +
                consList.get(0).getStartTime() + "-" + consList.get(0).getEndTime());
        paramsMap.put("deadlineTime", DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分")
                .format(consList.get(0).getDeadlineTime()));
        String round1Sms = getSmsText(paramsMap, round1Message);

        List<Long> directCustId = consList.stream().map(Cons::getCustId).collect(Collectors.toList());
        List<Cust> directCust = new ArrayList<>();
        if (!CollectionUtils.isEmpty(directCustId)) {
            directCust = custService.listByIds(directCustId);
        }

        List<Cust> custList = custInvitationMapper.listInvitationCustomer(businessRela);
//        List<String> customer = custList.stream().map(Cust::getTel).collect(Collectors.toList());
//        List<String> cons = directCust.stream().map(Cust::getTel).collect(Collectors.toList());
//        customer.addAll(cons);
//        customer = customer.stream().distinct().collect(Collectors.toList());
        custList.addAll(directCust);
        custList.stream().distinct().collect(Collectors.toList());
        List<SysSmsSend> aggSmsSends = new ArrayList<>();
        List<String> phonesEx = new ArrayList<>();
        //查询已经存在的电话号码
        SmsSendParam smsSendParam = new SmsSendParam();
        smsSendParam.setBusinessCode(SmsTemplateTypeEnum.INVITATION_NOTICE_ONCE.getCode());
        smsSendParam.setBusinessRela(businessRela);

        // 返回手机号 过滤
        List<SysSmsSend> sysSmsSendList = new ArrayList<>();
        JSONObject jsonObject = smsSendCilent.getAllSmsByBusiness(smsSendParam);
        if (jsonObject != null) {
            if ("000000".equals(jsonObject.getString("code"))) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (Object o : jsonArray) {
                    JSONObject jsonObject2 = JSONObject.fromObject(o);
                    SysSmsSend smsSend = this.jsonToSysSmsSend(jsonObject2);
                    sysSmsSendList.add(smsSend);
                }
            }
        }

        List<String> phones = sysSmsSendList.stream().map(SysSmsSend::getPhoneNumbers).collect(Collectors.toList());
        //设置值
        custList.forEach(cust -> {
            String join = null;
            if (1 == cust.getIntegrator()) {
                //集成商不给代理用户发
                join = "3";
            } else {
                //直接用户给客户发
                join = "4";
            }
            SysSmsSend sysSmsSend = produceSms(round1Sms, round1Template, cust.getTel(),
                    SmsSendStatusEnum.NOT_SEND.getCode(), businessRela, null, cust.getCreditCode(), cust.getLegalName(), join);
            if (null != phones && phones.size() > 0) {
                if (!phones.contains(cust.getTel())) {
                    if (!phonesEx.contains(cust.getTel())) {
                        aggSmsSends.add(sysSmsSend);
                        phonesEx.add(cust.getTel());
                    }
                }
            } else {
                if (!phonesEx.contains(cust.getTel())) {
                    aggSmsSends.add(sysSmsSend);
                    phonesEx.add(cust.getTel());
                }
            }
        });

        // 短信记录去重
        /* List<SysSmsSend> custSms = list(Wrappers.<SysSmsSend>lambdaQuery()
                .eq(SysSmsSend::getBusinessCode, SmsTemplateTypeEnum.INVITATION_NOTICE_ONCE.getCode())
                .eq(SysSmsSend::getBusinessRela, businessRela));
       custSms.forEach(sms -> {
            Iterator<SysSmsSend> iterator = aggSmsSends.iterator();
            if (iterator.hasNext() && iterator.next().getPhoneNumbers().equals(sms.getPhoneNumbers())) {
                iterator.remove();
            }
        });*/
        if (aggSmsSends.size() > 0) {
            smsSendCilent.saveBatch(aggSmsSends);
        }
        return round1Sms;
    }

    /**
     * 二次邀约
     *
     * @param businessRela 关联事件id
     * @return java.lang.String
     * @author Caoj
     * @since 10/28/2021 16:31
     */
    String round2InviteSms(String businessRela) {
        SmsSendTemplate templateByType =
                smsSendTemplateService.getTemplateByType(SmsTemplateTypeEnum.INVITATION_NOTICE_TWICE.getCode());
        String roundMessage = templateByType.getTemplateContent();
        List<Cons> consList = consInvitationMapper.listInvitationConsumer(businessRela);
        consList.removeIf(cons -> !"1".equals(cons.getIsParticipate()));
        if (!CollectionUtils.isEmpty(consList)) {
            throw new ServiceException(-1, "找不到邀约用户");
        }
        // 替换模板信息
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("duration", consList.get(0).getRegulateDate() + " " +
                consList.get(0).getStartTime() + "-" + consList.get(0).getEndTime());
        paramsMap.put("deadlineTime", DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分")
                .format(consList.get(0).getDeadlineTime()));
        String round2Sms = getSmsText(paramsMap, roundMessage);

        List<Long> directCustId = consList.stream().map(Cons::getCustId).collect(Collectors.toList());
        List<Cust> directCust = new ArrayList<>();
        if (CollectionUtils.isEmpty(directCustId)) {
            directCust = custService.listByIds(directCustId);
        }

        // 负荷集成商
        List<Cust> custList = custInvitationMapper.listInvitationCustomer(businessRela);
//        List<String> customer = custList.stream().map(Cust::getTel).collect(Collectors.toList());
//        List<String> cons = directCust.stream().map(Cust::getTel).collect(Collectors.toList());
//        customer.addAll(cons);
//        customer = customer.stream().distinct().collect(Collectors.toList());
        custList.addAll(directCust);
        custList.stream().distinct().collect(Collectors.toList());
        List<SysSmsSend> aggSmsSends = new ArrayList<>();
        List<String> phonesEx = new ArrayList<>();
        //查询已经存在的电话号码
        SmsSendParam smsSendParam = new SmsSendParam();
        smsSendParam.setBusinessCode(SmsTemplateTypeEnum.INVITATION_NOTICE_TWICE.getCode());
        smsSendParam.setBusinessRela(businessRela);
        // 返回手机号 过滤
        List<SysSmsSend> sysSmsSendList = new ArrayList<>();
        JSONObject jsonObject = smsSendCilent.getAllSmsByBusiness(smsSendParam);
        if (jsonObject != null) {
            if ("000000".equals(jsonObject.getString("code"))) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (Object o : jsonArray) {
                    JSONObject jsonObject2 = JSONObject.fromObject(o);
                    SysSmsSend smsSend = this.jsonToSysSmsSend(jsonObject2);
                    sysSmsSendList.add(smsSend);
                }
            }
        }

        List<String> phones = sysSmsSendList.stream().map(SysSmsSend::getPhoneNumbers).collect(Collectors.toList());
        custList.forEach(cust -> {
            String join = null;
            if (1 == cust.getIntegrator()) {
                join = "3";
            } else {
                join = "4";
            }
            //过滤出每个客户对应的所有用户信息
            List<Cons> cons = consList.stream()
                    .filter(con -> cust.getId().equals(con.getCustId()))
                    .collect(Collectors.toList());
            SysSmsSend sysSmsSend = produceSms(round2Sms, templateByType, cust.getTel(),
                    SmsSendStatusEnum.NOT_SEND.getCode(), businessRela, null, cust.getCreditCode(), cust.getLegalName(), join);
            //aggSmsSends.add(sysSmsSend);
            if (null != phones && phones.size() > 0) {
                if (!phones.contains(cust.getTel())) {
                    if (!phonesEx.contains(cust.getTel())) {
                        aggSmsSends.add(sysSmsSend);
                        phonesEx.add(cust.getTel());
                    }
                }
            } else {
                if (!phonesEx.contains(cust.getTel())) {
                    aggSmsSends.add(sysSmsSend);
                    phonesEx.add(cust.getTel());
                }
            }
        });

        // 短信记录去重
       /* List<SysSmsSend> custSms = list(Wrappers.<SysSmsSend>lambdaQuery()
                .eq(SysSmsSend::getBusinessCode, SmsTemplateTypeEnum.INVITATION_NOTICE_TWICE.getCode())
                .eq(SysSmsSend::getBusinessRela, businessRela));
        custSms.forEach(sms -> {
            Iterator<SysSmsSend> iterator = aggSmsSends.iterator();
            if (iterator.hasNext() && iterator.next().getPhoneNumbers().equals(sms.getPhoneNumbers())) {
                iterator.remove();
            }
        });*/
        if (aggSmsSends.size() > 0) {
            smsSendCilent.saveBatch(aggSmsSends);
        }
        return round2Sms;
    }

    /**
     * 邀约结果确认
     * 电力用户-> 用户和客户， 集成商-> 客户
     *
     * @param businessRela 关联邀约id
     * @return java.lang.String
     * @author Caoj
     * @since 10/28/2021 16:37
     */
    String resultNotice(String businessRela) {
        ConsInvitation consInvitation = consInvitationService.getById(businessRela);
        if (ObjectUtil.isNull(consInvitation)) {
            throw new ServiceException(-1, "找不到该邀约的用户，请检查业务主键");
        }
        String custId = Objects.requireNonNull(SecurityUtils.getCurrentUserInfoUTF8()).getId();
        Cust cust = custService.getById(custId);

        // 邀约结果确认短信
        SmsSendTemplate templateByType =
                smsSendTemplateService.getTemplateByType(SmsTemplateTypeEnum.RESULT_NOTICE.getCode());
        Event event = eventService.getById(consInvitation.getEventId());
        String duration = event.getRegulateDate() + " " + event.getStartTime() + "-" + event.getEndTime();
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("duration", duration);
        String resultSms = getSmsText(paramsMap, templateByType.getTemplateContent());

        // 事件启动模板短信
        SmsSendTemplate eventTemplate =
                smsSendTemplateService.getTemplateByType(SmsTemplateTypeEnum.EVENT_MESSAGE.getCode());
        String startTime = DateTimeFormatter.ofPattern("yyyy年MM月dd日").format(event.getRegulateDate())
                + " " + event.getStartTime();
        LocalDateTime localDateTime = LocalDateTime.from(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm")
                .parse(startTime));
        LocalDateTime localDateTime1 = localDateTime.minusHours(1);
        Date startDate = Date.from(localDateTime1.atZone(ZoneId.systemDefault()).toInstant());
        Map<String, String> paramsMapEvent = new HashMap<>();
        paramsMapEvent.put("duration", event.getRegulateDate() + " " + event.getStartTime() + "-" + event.getEndTime());
        String eventSms = getSmsText(paramsMapEvent, eventTemplate.getTemplateContent());

        if (IsAggregatorEnum.NOT_AGGREGATOR.getCode().equals(cust.getIntegrator())) {
            Cons cons = consService.getById(consInvitation.getConsId());
            SysSmsSend smsSend = getOne(Wrappers.<SysSmsSend>lambdaQuery()
                    .eq(SysSmsSend::getBusinessRela, consInvitation.getInvitationId())
                    .eq(SysSmsSend::getPhoneNumbers, cons.getFirstContactInfo())
                    .eq(SysSmsSend::getBusinessCode, SmsTemplateTypeEnum.RESULT_NOTICE.getCode()));
            if (ObjectUtil.isNull(smsSend)) {
                SysSmsSend sysSmsSend = produceSms(resultSms, templateByType, cons.getFirstContactInfo(),
                        SmsSendStatusEnum.WAITING_SEND.getCode(), consInvitation.getInvitationId()
                                .toString(), null, cons.getId(), cons.getConsName(), cons.getJoinUserType());
                List<SysSmsSend> list = new ArrayList<>();
                list.add(sysSmsSend);
                smsSendCilent.saveBatch(list);
            }

            SysSmsSend eventSmsSend = getOne(Wrappers.<SysSmsSend>lambdaQuery()
                    .eq(SysSmsSend::getBusinessRela, businessRela)
                    .eq(SysSmsSend::getPhoneNumbers, cons.getFirstContactInfo())
                    .eq(SysSmsSend::getBusinessCode, SmsTemplateTypeEnum.EVENT_MESSAGE.getCode()));
            if (ObjectUtil.isNull(eventSmsSend)) {
                SysSmsSend sysSmsSend = produceSms(eventSms, eventTemplate, cons.getFirstContactInfo(),
                        SmsSendStatusEnum.WAITING_SEND.getCode(), businessRela, startDate, cons.getId(), cons.getConsName(), cons.getJoinUserType());
                List<SysSmsSend> list = new ArrayList<>();
                list.add(sysSmsSend);
                smsSendCilent.saveBatch(list);
            }

            if (!cust.getTel().equals(cons.getFirstContactInfo())) {
                SysSmsSend smsSendCust = getOne(Wrappers.<SysSmsSend>lambdaQuery()
                        .eq(SysSmsSend::getBusinessRela, consInvitation.getInvitationId())
                        .eq(SysSmsSend::getPhoneNumbers, cust.getTel())
                        .eq(SysSmsSend::getBusinessCode, SmsTemplateTypeEnum.RESULT_NOTICE.getCode()));
                if (ObjectUtil.isNull(smsSendCust)) {
                    SysSmsSend sysSmsSendCust = produceSms(resultSms, templateByType, cons.getFirstContactInfo(),
                            SmsSendStatusEnum.WAITING_SEND.getCode(), consInvitation.getInvitationId()
                                    .toString(), null, cons.getId(), cons.getConsName(), cons.getJoinUserType());
                    List<SysSmsSend> list = new ArrayList<>();
                    list.add(sysSmsSendCust);
                    smsSendCilent.saveBatch(list);
                }

                SysSmsSend eventSmsSendCust = getOne(Wrappers.<SysSmsSend>lambdaQuery()
                        .eq(SysSmsSend::getBusinessRela, consInvitation.getInvitationId())
                        .eq(SysSmsSend::getPhoneNumbers, cust.getTel())
                        .eq(SysSmsSend::getBusinessCode, SmsTemplateTypeEnum.EVENT_MESSAGE.getMessage()));
                if (ObjectUtil.isNull(eventSmsSendCust)) {
                    SysSmsSend sysSmsSend = produceSms(eventSms, eventTemplate, cust.getTel(),
                            SmsSendStatusEnum.WAITING_SEND.getCode(), businessRela, startDate, cons.getId(), cons.getConsName(), cons.getJoinUserType());
                    List<SysSmsSend> list = new ArrayList<>();
                    list.add(sysSmsSend);
                    smsSendCilent.saveBatch(list);
                }
            }
            return resultSms + eventSms;
        } else {
            // 集成商
            SmsSendTemplate aggTemplate =
                    smsSendTemplateService.getTemplateByType(SmsTemplateTypeEnum.INVITE_NOTICE_AGGREGATE.getCode());
            // 替换模板信息
            Map<String, String> paramsMapAgg = new HashMap<>();
            paramsMapAgg.put("duration", event.getRegulateDate() + " " + event.getStartTime() + "-" + event.getEndTime());
            String aggSmsContent = getSmsText(paramsMapAgg, aggTemplate.getTemplateContent());
            SysSmsSend aggMessage = getOne(Wrappers.<SysSmsSend>lambdaQuery()
                    .eq(SysSmsSend::getBusinessRela, consInvitation.getInvitationId())
                    .eq(SysSmsSend::getPhoneNumbers, cust.getTel())
                    .eq(SysSmsSend::getBusinessCode, SmsTemplateTypeEnum.INVITE_NOTICE_AGGREGATE.getCode()));
            if (ObjectUtil.isNull(aggMessage)) {
                SysSmsSend aggSms = produceSms(aggSmsContent, aggTemplate, cust.getTel(),
                        SmsSendStatusEnum.WAITING_SEND.getCode(), consInvitation.getInvitationId().toString(),
                        Date.from(consInvitation.getDeadlineTime()
                                .atZone(ZoneId.systemDefault())
                                .toInstant()), consInvitation.getConsId(), consInvitation.getConsName(), consInvitation.getJoinUserType());
                List<SysSmsSend> list = new ArrayList<>();
                list.add(aggSms);
                smsSendCilent.saveBatch(list);
            }

            SysSmsSend aggMessageEvent = getOne(Wrappers.<SysSmsSend>lambdaQuery()
                    .eq(SysSmsSend::getBusinessRela, businessRela)
                    .eq(SysSmsSend::getPhoneNumbers, cust.getTel())
                    .eq(SysSmsSend::getBusinessCode, SmsTemplateTypeEnum.EVENT_MESSAGE.getCode()));
            if (ObjectUtil.isNull(aggMessageEvent)) {
                SysSmsSend aggSms = produceSms(eventSms, eventTemplate, cust.getTel(),
                        SmsSendStatusEnum.WAITING_SEND.getCode(), businessRela, startDate, consInvitation.getConsId(), consInvitation.getConsName(), consInvitation.getJoinUserType());
                List<SysSmsSend> list = new ArrayList<>();
                list.add(aggSms);
                smsSendCilent.saveBatch(list);
            }
            return aggSmsContent + eventSms;
        }
    }

    /**
     * 事件取消
     *
     * @param businessRela 关联事件id
     * @return java.lang.String
     * @author Caoj
     * @since 10/28/2021 16:39
     */
    String eventCancel(String businessRela) {
        Event event = eventService.getById(businessRela);
        if (null != event) {
            //仅撤销状态可操作
            if (!"07".equals(event.getEventStatus())) {
                throw new ServiceException(EventExceptionEnum.EVENT_REVOKE_STATE);
            }
        } else {
            throw new ServiceException(EventExceptionEnum.EVENT_NOT_EXIST);
        }
        SmsSendTemplate templateByType =
                smsSendTemplateService.getTemplateByType(SmsTemplateTypeEnum.EVENT_MESSAGE_CANCEL.getCode());
        if (ObjectUtil.isNull(event)) {
            throw new ServiceException(-1, "业务对应的事件不存在，请检查业务主键");
        }
        String duration = event.getRegulateDate() + " " + event.getStartTime() + "-" + event.getEndTime();
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("duration", duration);
        String cancelSms = getSmsText(paramsMap, templateByType.getTemplateContent());

        List<ConsInvitation> consInvitations = consInvitationService.list(Wrappers.<ConsInvitation>lambdaQuery()
                .eq(ConsInvitation::getEventId, businessRela)
                .eq(ConsInvitation::getIsParticipate, EventParticipateEnum.IS_PARTICIPATE.getCode()));
        if (null == consInvitations || consInvitations.size() == 0) {
            throw new ServiceException(-1, "邀约参与用户不存在");
        }
        List<String> consIds = consInvitations.stream().map(ConsInvitation::getConsId).collect(Collectors.toList());
        if (null == consIds || consIds.size() == 0) {
            throw new ServiceException(-1, "邀约参与用户不存在");
        }
        LambdaQueryWrapper<Cons> consLambdaQueryWrapper = new LambdaQueryWrapper<>();
        consLambdaQueryWrapper.in(Cons::getId, consIds);
        List<Cons> consList = consService.list(consLambdaQueryWrapper);
        if (null == consList || consList.size() == 0) {
            throw new ServiceException(-1, "邀约参与用户不存在");
        }
        List<Long> consCustomer = consList.stream().map(Cons::getCustId).collect(Collectors.toList());
        List<CustInvitation> custInvitationList = custInvitationService.list(Wrappers.<CustInvitation>lambdaQuery()
                .eq(CustInvitation::getEventId, businessRela)
                .eq(CustInvitation::getIsParticipate, EventParticipateEnum.IS_PARTICIPATE.getCode()));
        List<Long> custIds = custInvitationList.stream().map(CustInvitation::getCustId).collect(Collectors.toList());
        if (null == custIds || custIds.size() == 0) {
            throw new ServiceException(-1, "邀约参与客户不存在");
        }
        custIds.addAll(consCustomer);
        LambdaQueryWrapper<Cust> custLambdaQueryWrapper = new LambdaQueryWrapper<>();
        custLambdaQueryWrapper.in(Cust::getId, custIds);
        List<Cust> customerList = custService.list(custLambdaQueryWrapper);
        if (null == customerList || customerList.size() == 0) {
            throw new ServiceException(-1, "邀约参与客户不存在");
        }
        //List<String> phoneNumbers = customerList.stream().map(Cust::getTel).collect(Collectors.toList());
        //List<String> customerPhone = phoneNumbers.stream().distinct().collect(Collectors.toList());
        // 剔除库中存在的短信
       /* List<SysSmsSend> list = list(Wrappers.<SysSmsSend>lambdaQuery()
                .eq(SysSmsSend::getBusinessCode, SmsTemplateTypeEnum.EVENT_MESSAGE_CANCEL.getCode())
                .eq(SysSmsSend::getBusinessRela, businessRela));
        list.forEach(sysSmsSend -> {
            //List<Cust> collect = customerList.stream().filter(cust -> cust.getTel().equals(sysSmsSend.getPhoneNumbers())).collect(Collectors.toList());
            //customerList.removeAll(collect);
        });*/
        //查询已经存在的电话号码
        SmsSendParam smsSendParam = new SmsSendParam();
        smsSendParam.setBusinessCode(SmsTemplateTypeEnum.EVENT_MESSAGE_CANCEL.getCode());
        smsSendParam.setBusinessRela(businessRela);
        // 返回手机号 过滤
        List<SysSmsSend> sysSmsSendList = new ArrayList<>();
        JSONObject jsonObject = smsSendCilent.getAllSmsByBusiness(smsSendParam);
        if (jsonObject != null) {
            if ("000000".equals(jsonObject.getString("code"))) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (Object o : jsonArray) {
                    JSONObject jsonObject2 = JSONObject.fromObject(o);
                    SysSmsSend smsSend = this.jsonToSysSmsSend(jsonObject2);
                    sysSmsSendList.add(smsSend);
                }
            }
        }

        List<String> phones = sysSmsSendList.stream().map(SysSmsSend::getPhoneNumbers).collect(Collectors.toList());
        List<String> phonesEx = new ArrayList<>();
        List<SysSmsSend> sysSmsSends = new ArrayList<>();
        customerList.forEach(cust -> {
            SysSmsSend sysSmsSend = null;
            String join = null;
            if (1 == cust.getIntegrator()) {
                join = "3";
            } else {
                join = "4";
                List<Cons> cons = consList.stream()
                        .filter(con -> cust.getId().equals(con.getCustId()))
                        .collect(Collectors.toList());
                if (null != cons && cons.size() > 0) {
                    for (Cons con : cons) {
                        String firstContract = con.getFirstContactInfo();
                        SysSmsSend sysSmsSend2 = produceSms(cancelSms, templateByType, con.getFirstContactInfo(),
                                SmsSendStatusEnum.NOT_SEND.getCode(), businessRela, null, con.getId(), con.getConsName(), "1");
                        if (null != phones && phones.size() > 0) {
                            if (!phones.contains(firstContract)) {
                                if (!phonesEx.contains(firstContract)) {
                                    sysSmsSends.add(sysSmsSend2);
                                    phonesEx.add(firstContract);
                                }
                            }
                        } else {
                            if (!phonesEx.contains(firstContract)) {
                                sysSmsSends.add(sysSmsSend2);
                                phonesEx.add(firstContract);
                            }
                        }
                    }
                }
            }
            sysSmsSend = produceSms(cancelSms, templateByType, cust.getTel(),
                    SmsSendStatusEnum.NOT_SEND.getCode(), businessRela, null, cust.getCreditCode(), cust.getLegalName(), join);
            if (null != phones && phones.size() > 0) {
                if (!phones.contains(cust.getTel())) {
                    if (!phonesEx.contains(cust.getTel())) {
                        sysSmsSends.add(sysSmsSend);
                        phonesEx.add(cust.getTel());
                    }
                }
            } else {
                if (!phonesEx.contains(cust.getTel())) {
                    sysSmsSends.add(sysSmsSend);
                    phonesEx.add(cust.getTel());
                }
            }
        });
        smsSendCilent.saveBatch(sysSmsSends);
        return cancelSms;
    }

    /**
     * 补贴公示
     *
     * @param businessRela 关联公示id
     * @return java.lang.String
     * @author Caoj
     * @date 10/28/2021 16:41
     */
    String subsidyNotice(String businessRela) {
        List<PlanCons> list = planConsService.list(Wrappers.<PlanCons>lambdaQuery()
                .eq(PlanCons::getInvolvedIn, EventParticipateEnum.IS_PARTICIPATE.getCode()));
        List<String> consIds = list.stream().map(PlanCons::getConsId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(consIds)) {
            return "找不到邀约参与的用户";
        }
        List<Cons> consList = consService.listByIds(consIds);
        // 根据第一联系人去重
        consList = consList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(
                () -> new TreeSet<>(Comparator.comparing(Cons::getFirstContactInfo))), ArrayList::new));
//        List<String> consPhone = consList.stream().map(Cons::getFirstContactInfo).collect(Collectors.toList());

        List<SysSmsSend> sysSmsSends = list(Wrappers.<SysSmsSend>lambdaQuery()
                .eq(SysSmsSend::getBusinessRela, businessRela)
                .eq(SysSmsSend::getBusinessCode, SmsTemplateTypeEnum.SUBSIDY_NOTICE.getCode()));
        List<Cons> finalConsList = consList;
        sysSmsSends.forEach(sysSmsSend -> {
//            Iterator<String> iterator = consPhone.listIterator();
//            if (iterator.hasNext() && sysSmsSend.getPhoneNumbers().equals(iterator.next())) {
//                iterator.remove();
//            }
            List<Cons> collect = finalConsList.stream()
                    .filter(c -> sysSmsSend.getPhoneNumbers().equals(c.getFirstContactInfo()))
                    .collect(Collectors.toList());
            finalConsList.removeAll(collect);
        });

        SmsSendTemplate templateByType =
                smsSendTemplateService.getTemplateByType(SmsTemplateTypeEnum.SUBSIDY_NOTICE.getCode());
        finalConsList.forEach(cons -> {
            SysSmsSend sysSmsSend = produceSms(templateByType.getTemplateContent(), templateByType, cons.getFirstContactInfo(),
                    SmsSendStatusEnum.WAITING_SEND.getCode(), businessRela, null, cons.getId(), cons.getConsName(), null);
            sysSmsSends.add(sysSmsSend);
        });
        smsSendCilent.saveBatch(sysSmsSends);
        return templateByType.getTemplateContent();
    }

    /**
     * 执行方案编制通知,下发给客户。从用户方案表和客户方案表找到确认参与的客户并根据手机号去重并生成短信
     *
     * @param businessRela 业务标识,对应事件标识eventId
     * @return java.lang.String
     * @date 1/4/2022 15:27
     * @author Caoj
     */
    String executeScheme(String businessRela) {
        Event event = eventService.getById(businessRela);
        if (null != event) {
            //仅执行中状态操作,且事件未开始
            if (!"03".equals(event.getEventStatus())) {
                throw new ServiceException(EventExceptionEnum.EVENT_EXECUTE_COM_STATE);
            }
            Integer endHour = 0;
            Integer endMinute = 0;
            if (null != event.getStartTime()) {
                endHour = Integer.parseInt(event.getStartTime().substring(0, 2));
                endMinute = Integer.parseInt(event.getStartTime().substring(3));
            }
            LocalDate localDate = LocalDate.now();
            LocalTime startTime = LocalTime.of(endHour, endMinute);
            if (event.getRegulateDate().compareTo(localDate) == 0) {
                if (LocalTime.now().compareTo(startTime) > 0) {
                    throw new ServiceException(EventExceptionEnum.EVENT_BEGIN);
                }
            } else if (event.getRegulateDate().compareTo(localDate) < 0) {
                throw new ServiceException(EventExceptionEnum.EVENT_BEGIN);
            }
        } else {
            throw new ServiceException(EventExceptionEnum.EVENT_NOT_EXIST);
        }
        SmsSendTemplate templateByType =
                smsSendTemplateService.getTemplateByType(SmsTemplateTypeEnum.EXECUTE_SCHEME.getCode());
        EventParam eventParam = new EventParam();
        eventParam.setEventId(Long.parseLong(businessRela));
        Event eventDetail = eventService.detail(eventParam);
        if (ObjectUtil.isNull(eventDetail)) {
            throw new ServiceException(EventExceptionEnum.EVENT_NOT_EXIST);
        }
        List<PlanCons> planConsList = planConsService.list(Wrappers.<PlanCons>lambdaQuery()
                .eq(PlanCons::getImplement, EventParticipateEnum.IS_PARTICIPATE.getCode())
                .eq(PlanCons::getPlanId, eventDetail.getPlanId()));
        List<PlanCust> planCustomerList = planCustService.list(Wrappers.<PlanCust>lambdaQuery()
                .eq(PlanCust::getImplement, EventParticipateEnum.IS_PARTICIPATE.getCode())
                .eq(PlanCust::getPlanId, eventDetail.getPlanId()));

        List<Long> customer = new ArrayList<>();
        List<Cons> directCons = null;
        List<Cons> consList = new ArrayList<>();
        if (null != planConsList && planConsList.size() > 0) {
            List<String> consIds = planConsList.stream().map(PlanCons::getConsId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(consIds)) {
                directCons = consService.list(Wrappers.<Cons>lambdaQuery().in(Cons::getId, consIds));
                consList.addAll(directCons);
                List<Long> directCustomer = directCons.stream().map(Cons::getCustId).collect(Collectors.toList());
                List<UserConsRela> aggCustomer = userConsRelaService
                        .list(Wrappers.<UserConsRela>lambdaQuery().in(UserConsRela::getConsNo, consIds));
                List<Long> aggCustomerIds = aggCustomer.stream()
                        .map(UserConsRela::getCustId)
                        .collect(Collectors.toList());
                customer.addAll(directCustomer);
                customer.addAll(aggCustomerIds);
            }
        }
        if (planCustomerList.size() > 0) {
            List<Long> customerIds = planCustomerList.stream().map(PlanCust::getCustId).collect(Collectors.toList());
            customer.addAll(customerIds);
        }

        if (CollectionUtils.isEmpty(customer)) {
            throw new ServiceException(-1, "没有方案编制最终确认参与的客户");
        }
        // 替换模板信息
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("duration", event.getRegulateDate() + " " +
                event.getStartTime() + "-" + event.getEndTime());
        String executeSms = getSmsText(paramsMap, templateByType.getTemplateContent());
        List<Long> customerIds = customer.stream().distinct().collect(Collectors.toList());
        List<Cust> customerList = custService.list(Wrappers.<Cust>lambdaQuery().in(Cust::getId, customerIds));
//        List<String> customerPhone = customerList.stream().map(Cust::getTel).collect(Collectors.toList());
//        customerPhone = customerPhone.stream().distinct().collect(Collectors.toList());
        customerList = customerList.stream().distinct().collect(Collectors.toList());
        List<SysSmsSend> smsSendList = new ArrayList<>();
        List<String> phonesEx = new ArrayList<>();
        //查询已经存在的电话号码
        SmsSendParam smsSendParam = new SmsSendParam();
        smsSendParam.setBusinessCode(SmsTemplateTypeEnum.EXECUTE_SCHEME.getCode());
        smsSendParam.setBusinessRela(businessRela);
        // 返回手机号 过滤
        List<SysSmsSend> sysSmsSendList = new ArrayList<>();
        JSONObject jsonObject = smsSendCilent.getAllSmsByBusiness(smsSendParam);
        if (jsonObject != null) {
            if ("000000".equals(jsonObject.getString("code"))) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (Object o : jsonArray) {
                    JSONObject jsonObject2 = JSONObject.fromObject(o);
                    SysSmsSend smsSend = this.jsonToSysSmsSend(jsonObject2);
                    sysSmsSendList.add(smsSend);
                }
            }
        }

        List<String> phones = sysSmsSendList.stream().map(SysSmsSend::getPhoneNumbers).collect(Collectors.toList());
        customerList.forEach(cust -> {
            String join = null;
            if (1 == cust.getIntegrator()) {
                join = "3";
            } else {
                join = "4";
                List<Cons> cons = consList.stream()
                        .filter(con -> cust.getId().equals(con.getCustId()))
                        .collect(Collectors.toList());
                if (null != cons && cons.size() > 0) {
                    for (Cons con : cons) {
                        String firstContract = con.getFirstContactInfo();
                        SysSmsSend sysSmsSend2 = produceSms(executeSms, templateByType, con.getFirstContactInfo(),
                                SmsSendStatusEnum.NOT_SEND.getCode(), businessRela, null, con.getId(), con.getConsName(), "1");
                        if (null != phones && phones.size() > 0) {
                            if (!phones.contains(firstContract)) {
                                if (!phonesEx.contains(firstContract)) {
                                    smsSendList.add(sysSmsSend2);
                                    phonesEx.add(firstContract);
                                }
                            }
                        } else {
                            if (!phonesEx.contains(firstContract)) {
                                smsSendList.add(sysSmsSend2);
                                phonesEx.add(firstContract);
                            }
                        }
                    }
                }
            }
            SysSmsSend sysSmsSend = produceSms(executeSms, templateByType, cust.getTel(),
                    SmsSendStatusEnum.NOT_SEND.getCode(), businessRela, null, cust.getCreditCode(), cust.getLegalName(), join);
            //smsSendList.add(sysSmsSend);
            if (null != phones && phones.size() > 0) {
                if (!phones.contains(cust.getTel())) {
                    if (!phonesEx.contains(cust.getTel())) {
                        smsSendList.add(sysSmsSend);
                        phonesEx.add(cust.getTel());
                    }
                }
            } else {
                if (!phonesEx.contains(cust.getTel())) {
                    smsSendList.add(sysSmsSend);
                    phonesEx.add(cust.getTel());
                }
            }
        });

        // 短信记录去重
       /* List<SysSmsSend> custSms = list(Wrappers.<SysSmsSend>lambdaQuery()
                .eq(SysSmsSend::getBusinessCode, SmsTemplateTypeEnum.EXECUTE_SCHEME.getCode())
                .eq(SysSmsSend::getBusinessRela, businessRela));
        custSms.forEach(sms -> {
            Iterator<SysSmsSend> iterator = smsSendList.iterator();
            if (iterator.hasNext() && iterator.next().getPhoneNumbers().equals(sms.getPhoneNumbers())) {
                iterator.remove();
            }
        });*/

        if (smsSendList.size() > 0) {
            smsSendCilent.saveBatch(smsSendList);
        }
        return templateByType.getTemplateContent();
    }

    /**
     * 查找重复短信
     *
     * @param businessRela 业务id
     * @param businessCode 模板类型
     * @return com.xqxy.sys.modular.sms.entity.SysSmsSend
     * @author Caoj
     * @since 10/28/2021 16:40
     */
    SysSmsSend detailByBusiness(String businessRela, String businessCode) {
        List<SysSmsSend> sysSmsSends = list(Wrappers.<SysSmsSend>lambdaQuery()
                .eq(SysSmsSend::getBusinessRela, businessRela)
                .eq(SysSmsSend::getBusinessCode, businessCode));
        if (CollectionUtils.isEmpty(sysSmsSends)) {
            return null;
        } else {
            return sysSmsSends.get(0);
        }
    }

    /**
     * 替换短信模板
     *
     * @param map          替换字段
     * @param htmlTemplate 短信内容
     * @return java.lang.String
     * @author Caoj
     * @since 10/28/2021 16:40
     */
    String getSmsText(Map<String, String> map, String htmlTemplate) {
        return SmsUtils.getSmsText(map, htmlTemplate);
    }

    /**
     * 生成短信
     *
     * @param content      短信内容
     * @param template     通知模板
     * @param phone        手机号
     * @param status       状态
     * @param businessRela 业务类型
     * @param preSendTime  预发送时间
     * @return com.xqxy.sys.modular.sms.entity.SysSmsSend
     * @author Caoj
     * @date 11/3/2021 15:55
     */
    SysSmsSend produceSms(String content, SmsSendTemplate template, String phone, Integer status, String businessRela, Date preSendTime, String userNo, String userName, String joinUserType) {
        SysSmsSend sysSmsSend = new SysSmsSend();
        sysSmsSend.setContent(content);
        sysSmsSend.setTemplateCode(template.getTemplateId().toString());
        sysSmsSend.setPhoneNumbers(phone);
        sysSmsSend.setStatus(status);
        sysSmsSend.setSource(SmsSendSourceEnum.SEND_SOURCE_PC.getCode());
        sysSmsSend.setBusinessCode(template.getTemplateType());
        sysSmsSend.setBusinessRela(businessRela);
        sysSmsSend.setPreSendTime(preSendTime);
        sysSmsSend.setUserNo(userNo);
        sysSmsSend.setUserName(userName);
        sysSmsSend.setJoinUserType(joinUserType);
        return sysSmsSend;
    }

    /**
     * json对象转换成 SysSmsSend
     */
    public SysSmsSend jsonToSysSmsSend(JSONObject object) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SysSmsSend smsSend = new SysSmsSend();
        Long id = object.getLong("id");
        String phoneNumbers = object.getString("phoneNumbers");
        String validateCode = object.getString("validateCode");
        String templateCode = object.getString("templateCode");
        String content = object.getString("content");
        String bizId = object.getString("bizId");
        Integer status = object.getInt("status");
        Integer source = object.getInt("source");
        String businessRela = object.getString("businessRela");
        String businessCode = object.getString("businessCode");
        String preSendTime = object.getString("preSendTime");
        String invalidTime = object.getString("invalidTime");
        String sendTimes = object.getString("sendTimes");
        String createTime = object.getString("createTime");
        String sendTime = object.getString("sendTime");
        String failInfo = object.getString("failInfo");
        String userNo = object.getString("userNo");
        String userName = object.getString("userName");
        String joinUserType = object.getString("joinUserType");
        try {
            if (!"null".equals(sendTimes) && !StringUtils.isEmpty(sendTimes)) {
                smsSend.setSendTimes(Integer.valueOf(sendTimes));
            }
            if (!"null".equals(preSendTime) && !StringUtils.isEmpty(preSendTime)) {
                smsSend.setPreSendTime(simpleDateFormat.parse(preSendTime));
            }
            if (!"null".equals(invalidTime) && !StringUtils.isEmpty(invalidTime)) {
                smsSend.setInvalidTime(simpleDateFormat.parse(invalidTime));
            }
            if (!"null".equals(sendTime) && !StringUtils.isEmpty(sendTime)) {
                smsSend.setSendTime(simpleDateFormat.parse(sendTime));
            }
            if (!"null".equals(createTime) && !StringUtils.isEmpty(createTime)) {
                smsSend.setCreateTime(sdf.parse(createTime));
            }
            if (!"null".equals(failInfo) && !StringUtils.isEmpty(failInfo)) {
                smsSend.setFailInfo(failInfo);
            }
            if (!"null".equals(userNo) && !StringUtils.isEmpty(userNo)) {
                smsSend.setUserNo(userNo);
            }
            if (!"null".equals(userName) && !StringUtils.isEmpty(userName)) {
                smsSend.setUserName(userName);
            }
            if (!"null".equals(joinUserType) && !StringUtils.isEmpty(joinUserType)) {
                smsSend.setJoinUserType(joinUserType);
            }
            if (!"null".equals(status) && !StringUtils.isEmpty(status)) {
                smsSend.setStatus(status);
            }
            if (!"null".equals(id) && !StringUtils.isEmpty(id)) {
                smsSend.setId(id);
            }
            if (!"null".equals(phoneNumbers) && !StringUtils.isEmpty(phoneNumbers)) {
                smsSend.setPhoneNumbers(phoneNumbers);
            }
            if (!"null".equals(validateCode) && !StringUtils.isEmpty(validateCode)) {
                smsSend.setValidateCode(validateCode);
            }
            if (!"null".equals(templateCode) && !StringUtils.isEmpty(templateCode)) {
                smsSend.setTemplateCode(templateCode);
            }
            if (!"null".equals(content) && !StringUtils.isEmpty(content)) {
                smsSend.setContent(content);
            }
            if (!"null".equals(bizId) && !StringUtils.isEmpty(bizId)) {
                smsSend.setBizId(bizId);
            }
            if (!"null".equals(source) && !StringUtils.isEmpty(source)) {
                smsSend.setSource(source);
            }
            if (!"null".equals(businessRela) && !StringUtils.isEmpty(businessRela)) {
                smsSend.setBusinessRela(businessRela);
            }
            if (!"null".equals(businessCode) && !StringUtils.isEmpty(businessCode)) {
                smsSend.setBusinessCode(businessCode);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return smsSend;
    }

}
