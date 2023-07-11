package com.xqxy.dr.modular.event.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.client.SystemClient;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.core.util.ConsOrgUtils;
import com.xqxy.core.util.DateUtil;
import com.xqxy.dr.modular.event.VO.ConsBaseLineExcelVo;
import com.xqxy.dr.modular.event.entity.ConsBaseline;
import com.xqxy.dr.modular.event.entity.ConsBaselineAll;
import com.xqxy.dr.modular.event.mapper.ConsBaselineAllMapper;
import com.xqxy.dr.modular.event.param.ConsBaselineParam;
import com.xqxy.dr.modular.event.service.ConsBaselineAllService;
import com.xqxy.sys.modular.cust.entity.Cons;
import com.xqxy.sys.modular.cust.result.Result;
import com.xqxy.sys.modular.cust.service.ConsService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 基线96点全量计算 服务实现类
 * </p>
 *
 * @author liqirui
 * @since 2022-06-15
 */
@Service
public class ConsBaselineAllServiceImpl extends ServiceImpl<ConsBaselineAllMapper, ConsBaselineAll> implements ConsBaselineAllService {

    @Resource
    private ConsBaselineAllMapper consBaselineAllMapper;
    @Resource
    private SystemClient systemClient;
    @Resource
    private ConsService consService;

    /**
     * 根据事件id 和 用户id  查询基线
     *
     * @param eventId 事件id
     * @param consId  用户id
     * @return
     */
    @Override
    public List<ConsBaselineAll> queryBaseLineByEventAndCons(Long eventId, String consId) {
        return consBaselineAllMapper.queryBaseLineByEventAndCons(eventId, consId);
    }

    @Override
    public List<ConsBaseLineExcelVo> exportListToExcel(ConsBaselineParam consBaselineParam) {
        Long baselineLibId = consBaselineParam.getBaselineLibId();
        String orgId = consBaselineParam.getOrgId();
        LambdaQueryWrapper<ConsBaselineAll> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ConsBaselineAll::getBaselineLibId, baselineLibId);

        List<ConsBaselineAll> consBaselineAlls = this.getBaseMapper().selectList(lambdaQueryWrapper);
        Map<String, Map<String, String>> cityOrg = ConsOrgUtils.getInstance().getCityOrg(consBaselineAlls, ConsBaselineAll::getConsId);
        return consBaselineAlls.stream().map(item -> {
            ConsBaseLineExcelVo consBaseLineExcelVo = new ConsBaseLineExcelVo();
            BeanUtils.copyProperties(item, consBaseLineExcelVo);
            if (null != item && null != item.getBaselineDate()) {
                consBaseLineExcelVo.setBaselineDate(DateUtil.formatDate(item.getBaselineDate(), "yyyy-MM-dd"));
            }
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
}
