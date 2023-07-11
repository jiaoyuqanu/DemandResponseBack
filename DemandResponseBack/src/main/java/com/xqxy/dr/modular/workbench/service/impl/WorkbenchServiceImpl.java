package com.xqxy.dr.modular.workbench.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xqxy.core.client.SystemClient;
import com.xqxy.core.client.SystemClientService;
import com.xqxy.core.exception.SystemErrorType;
import com.xqxy.core.pojo.login.CurrenUserInfo;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.dr.modular.baseline.util.CurveUtil;
import com.xqxy.dr.modular.event.enums.CheckStatusEnum;
import com.xqxy.dr.modular.workbench.entity.Contracts;
import com.xqxy.dr.modular.workbench.entity.LoadCurve;
import com.xqxy.dr.modular.workbench.entity.ReserveSubsidy;
import com.xqxy.dr.modular.workbench.mapper.ContractsMapper;
import com.xqxy.dr.modular.workbench.mapper.LoadCurveMapper;
import com.xqxy.dr.modular.workbench.mapper.ReserveWorkMapper;
import com.xqxy.dr.modular.workbench.param.WorkbenchParam;
import com.xqxy.dr.modular.workbench.service.IWorkbenchService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class WorkbenchServiceImpl implements IWorkbenchService {

    @Resource
    private ReserveWorkMapper mapper;

    @Resource
    private LoadCurveMapper curveMapper;

    @Resource
    private ContractsMapper contractsMapper;

    @Resource
    SystemClientService systemClient;

    /**
     * 工作台容量补偿统计（省级）
     *
     * @param param
     * @return
     */
    @Override
    public ReserveSubsidy reserveSubsidyWorkbench(WorkbenchParam param) {
        List<ReserveSubsidy> rel = mapper.reserveSubsidy(param);
        String orgNo = param.getOrgNo();
        if (ObjectUtil.isNotNull(orgNo)) {
            List<String> orgList = systemClient.getAllNextOrgId(orgNo).getData();
            param.setOrgList(orgList);
        }
        List<ReserveSubsidy> rsc = mapper.reserveSubsidyCity(param);
        // 独立参与用户金额
        List<ReserveSubsidy> collect = rsc.stream().filter(wr -> ObjectUtil.equal(wr.getParticipantType(), "1")).collect(Collectors.toList());
        double independent =
                collect.stream().mapToDouble(ReserveSubsidy::getSubsidyAmount).sum();
        // 代理参与用户金额
        double proxy = rsc.stream().filter(wr -> ObjectUtil.equal(wr.getParticipantType(), "2")).mapToDouble(ReserveSubsidy::getSubsidyAmount).sum();
        // 集成商金额
        double aggregator = rel.stream().filter(wr -> ObjectUtil.equal(wr.getIntegrator(), "1")).mapToDouble(ReserveSubsidy::getSubsidyAmount).sum();
        // 总金额
        double totalAmount = independent + proxy + aggregator;
        ReserveSubsidy wrs = new ReserveSubsidy();
        wrs.setIndependent(independent);
        wrs.setProxy(proxy);
        wrs.setAggregator(aggregator);
        wrs.setTotalAmount(totalAmount);
        return wrs;
    }

    /**
     * 工作台容量补偿统计(地市)
     *
     * @param param
     * @return
     */
    @Override
    public ReserveSubsidy reserveSubsidyCity(WorkbenchParam param) {
        String orgNo = param.getOrgNo();
        List<String> orgList = systemClient.getAllNextOrgId(orgNo).getData();
        param.setOrgList(orgList);
        List<ReserveSubsidy> rsc = mapper.reserveSubsidyCity(param);
        //1 直接参与
        double independent = rsc.stream().filter(reserveSubsidy -> Objects.equals(reserveSubsidy.getParticipantType(), "1")).mapToDouble(ReserveSubsidy::getSubsidyAmount).sum();
        //2 代理参与
        double proxy = rsc.stream().filter(reserveSubsidy -> Objects.equals(reserveSubsidy.getParticipantType(), "2")).mapToDouble(ReserveSubsidy::getSubsidyAmount).sum();
        // 总金额
        double totalAmount = independent + proxy;
        ReserveSubsidy wrs = new ReserveSubsidy();
        wrs.setTotalAmount(totalAmount);
        wrs.setIndependent(independent);
        wrs.setProxy(proxy);
        return wrs;
    }

    /**
     * 签约资源分布
     *
     * @param param
     * @return
     */
    @Override
    public List<Contracts> contracts(WorkbenchParam param) {
        CurrenUserInfo userInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if (Objects.isNull(userInfo)) {
            throw new RuntimeException("获取当前登录用户信息失败");
        }
        String orgId = userInfo.getOrgId();
        String orgNo = param.getOrgNo();
        if (ObjectUtil.isNull(param.getType())) {
            return null;
        }
        if (orgId == null || "".equals(orgId)) {
            orgNo = orgId;
        }
        String[] split = param.getType().split(",");
        String responseType = split[0];
        String timeType = split[1];
        String advanceNoticeTime = split[2];
        JSONObject result = systemClient.parent(orgNo);
        System.out.println("parent: " + result.toString());
        JSONArray data = null;
        if (null != result) {
            data = result.getJSONArray("data");
        } else {
            throw new RuntimeException(String.valueOf(SystemErrorType.SYSTEM_ERROR));
        }

        //只查询 审核通过的
        String checkStatus = CheckStatusEnum.PASS_THE_AUDIT.getCode();
        List<Contracts> contractList = contractsMapper.getContracts(param, responseType, timeType, advanceNoticeTime,checkStatus);
        List<Contracts> list = new ArrayList<>();
        for (Object object : data) {
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(object);
            Contracts c = new Contracts();
            c.setRegionId(jsonObject.getString("regionId"));
            String id = String.valueOf(jsonObject.get("id"));
            // 获取下一级和本级组织
            List<String> orgList = systemClient.getAllNextOrgId(id).getData();


            // 下级和下级所有用户签约的容量数
            BigDecimal nextAll = contractList.stream().filter(contracts -> orgList.contains(contracts.getOrgNo())).map(cons -> {
                BigDecimal contractCap = cons.getContractCap();
                return contractCap == null ? BigDecimal.ZERO : contractCap;
            }).reduce(BigDecimal.ZERO, BigDecimal::add);

            // 下级全部签约容量单位（万千瓦）
            nextAll = nextAll.divide(BigDecimal.valueOf(10000));
            String name = (String) jsonObject.get("name");
            c.setOrgName(name);
            c.setContractNum(nextAll);
            list.add(c);
        }

        return list;
    }

    /**
     * 用电负荷曲线
     *
     * @param param
     * @return
     */
    @Override
    public LoadCurve loadCurve(WorkbenchParam param) {
        String orgNo = param.getOrgNo();
        if (ObjectUtil.isNotNull(orgNo)) {
            List<String> orgList = systemClient.getAllNextOrgId(orgNo).getData();
            param.setOrgList(orgList);
        }
        LoadCurve loadCurve = curveMapper.getLoadCurve(param);
        if(loadCurve != null){
            Class<? extends LoadCurve> curveClass = loadCurve.getClass();
            Field[] declaredFields = curveClass.getDeclaredFields();

            for (Field field : declaredFields) {
                field.setAccessible(true);

                String fieldName = field.getName();
                if(fieldName.startsWith("p") && fieldName.length() <= 3) {
                    //当前p点 之后的p点 的截取
                    try {
                        Object object = field.get(loadCurve);
                        if(object != null){
                            if (BigDecimal.ZERO.compareTo(new BigDecimal(object.toString())) == 0){
                                ReflectUtil.setFieldValue(loadCurve,fieldName,null);
                            }
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String ymd = sdf.format(new Date());

        Integer pn = CurveUtil.covDateTimeToPoint(new Date());
        if(loadCurve != null && loadCurve.getDataDate().compareTo(ymd) == 0 ){
            for (int i = 1; i <= 96; i++) {
                if (pn < i) {
                    ReflectUtil.setFieldValue(loadCurve, "p" + i, null);
                }
            }
        }

        return Objects.isNull(loadCurve) ? new LoadCurve() : loadCurve;
    }
}
