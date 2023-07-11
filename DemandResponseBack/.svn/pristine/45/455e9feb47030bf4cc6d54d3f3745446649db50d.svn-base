package com.xqxy.dr.modular.event.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xqxy.core.client.SystemClient;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.core.util.ConsOrgUtils;
import com.xqxy.core.util.DateUtil;
import com.xqxy.dr.modular.event.VO.ConsBaseLineExcelVo;
import com.xqxy.dr.modular.event.entity.ConsBaseline;
import com.xqxy.dr.modular.event.entity.ConsBaselineAll;
import com.xqxy.dr.modular.event.entity.Plan;
import com.xqxy.dr.modular.event.mapper.ConsBaselineMapper;
import com.xqxy.dr.modular.event.param.ConsBaselineParam;
import com.xqxy.dr.modular.event.service.ConsBaselineAllService;
import com.xqxy.dr.modular.event.service.ConsBaselineService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.dr.modular.event.service.PlanService;
import com.xqxy.sys.modular.cust.entity.Cons;
import com.xqxy.sys.modular.cust.result.Result;
import com.xqxy.sys.modular.cust.service.ConsService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-18
 */
@Service
public class ConsBaselineServiceImpl extends ServiceImpl<ConsBaselineMapper, ConsBaseline> implements ConsBaselineService {

    @Resource
    private PlanService planService;

    @Resource
    private ConsBaselineAllService consBaselineAllService;

    @Resource
    private ConsService consService;

    @Resource
    private SystemClient systemClient;

    @Override
    public List<ConsBaseline> list(ConsBaselineParam consBaselineParam) {
        LambdaQueryWrapper<ConsBaseline> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(consBaselineParam)) {
            // 根据事件编号模糊查询
            if (ObjectUtil.isNotEmpty(consBaselineParam.getBaselineLibId())) {
                queryWrapper.eq(ConsBaseline::getBaselineLibId, consBaselineParam.getBaselineLibId());
            }
        }
        return this.list(queryWrapper);
    }

    @Override
    public List<ConsBaseLineExcelVo> exportListToExcel(ConsBaselineParam consBaselineParam) {
        String orgId = consBaselineParam.getOrgId();
        LambdaQueryWrapper<ConsBaseline> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(consBaselineParam)) {
            // 根据事件编号模糊查询
            if (ObjectUtil.isNotEmpty(consBaselineParam.getBaselineLibId())) {
                queryWrapper.eq(ConsBaseline::getBaselineLibId, consBaselineParam.getBaselineLibId());
            }
        }
        List<ConsBaseline> list = this.list(queryWrapper);
        Map<String, Map<String, String>> cityOrg = ConsOrgUtils.getInstance().getCityOrg(list, ConsBaseline::getConsId);
        return list.stream().map(item -> {
            ConsBaseLineExcelVo consBaseLineExcelVo = new ConsBaseLineExcelVo();
            BeanUtils.copyProperties(item, consBaseLineExcelVo);
            consBaseLineExcelVo.setBaselineDate(item.getBaselineDate().toString());
            if (Objects.equals(item.getNormal(), "Y")) {
                consBaseLineExcelVo.setNormalStr("是");
            } else {
                consBaseLineExcelVo.setNormalStr("否");
            }
            Map<String, String> cityOrgMap = cityOrg.getOrDefault(item.getConsId(), new HashMap<>());
            consBaseLineExcelVo.setProvinceOrgName(cityOrgMap.getOrDefault(ConsOrgUtils.PROVINCE, ""));
            consBaseLineExcelVo.setCityOrgName(cityOrgMap.getOrDefault(ConsOrgUtils.CITY, ""));
            consBaseLineExcelVo.setAreaOrgName(cityOrgMap.getOrDefault(ConsOrgUtils.AREA, ""));
            consBaseLineExcelVo.setOrgName(cityOrgMap.getOrDefault(ConsOrgUtils.ORG, ""));
            return consBaseLineExcelVo;
        }).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        for (int i = 1; i <= 96; i++) {
            System.out.printf("    @Excel(name = \"p%d\", width = 120)\n" +
                    "    private BigDecimal p%d;\n\n", i, i);
        }

    }


    @Override
    public ConsBaseline detail(String consId, LocalDate dataDate) {
        LambdaQueryWrapper<ConsBaseline> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ConsBaseline::getConsId, consId);
        queryWrapper.eq(ConsBaseline::getBaselineDate, dataDate);
        return this.getOne(queryWrapper);
    }

    @Override
    public ConsBaseline detail(String consId, LocalDate dataDate, Long baseLineId) {
        LambdaQueryWrapper<ConsBaseline> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ConsBaseline::getConsId, consId);
        queryWrapper.eq(ConsBaseline::getBaselineDate, dataDate);
        queryWrapper.eq(ConsBaseline::getBaselineLibId, baseLineId);
        return this.getOne(queryWrapper);
    }

    @Override
    public ConsBaseline getConsBaseByLibId(String consId, Long baselinLibId) {
        if (ObjectUtil.isNull(consId) && ObjectUtil.isNull(baselinLibId)) {
            return null;
        }
        LambdaQueryWrapper<ConsBaseline> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ConsBaseline::getConsId, consId);
        queryWrapper.eq(ConsBaseline::getBaselineLibId, baselinLibId);
        return this.getOne(queryWrapper);
    }

    @Override
    public List<ConsBaseline> getConsBaseByEventId(Long eventId, String consId) {
        // 查询事件关联的方案信息，获去事件的基线库
        Plan plan = planService.getByEventId(eventId);
        if (ObjectUtil.isNull(plan) || ObjectUtil.isNull(plan.getBaselinId())) return null;
        LambdaQueryWrapper<ConsBaseline> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ConsBaseline::getConsId, consId);
        queryWrapper.eq(ConsBaseline::getBaselineLibId, plan.getBaselinId());
        return this.list(queryWrapper);
    }

    @Override
    public List<ConsBaselineAll> getBaselineAllByEventId(Long eventId, String consId) {
        // 查询事件关联的方案信息，获去事件的基线库
        Plan plan = planService.getByEventId(eventId);
        if (ObjectUtil.isNull(plan) || ObjectUtil.isNull(plan.getBaselinId())) return null;
        LambdaQueryWrapper<ConsBaselineAll> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ConsBaselineAll::getConsId, consId);
        queryWrapper.eq(ConsBaselineAll::getBaselineLibId, plan.getBaselinId());
        return consBaselineAllService.list(queryWrapper);
    }

}
