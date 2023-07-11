package com.xqxy.dr.modular.gwapp.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xqxy.core.client.SystemClient;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.gwapp.entity.GwappCons;
import com.xqxy.dr.modular.gwapp.entity.GwappCust;
import com.xqxy.dr.modular.gwapp.mapper.GwappConsMapper;
import com.xqxy.dr.modular.gwapp.service.IGwappConsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.dr.modular.gwapp.service.IGwappCustService;
import com.xqxy.dr.modular.strategy.DataAccessStrategy;
import com.xqxy.dr.modular.strategy.DataAccessStrategyContext;
import com.xqxy.sys.modular.cust.entity.Cons;
import com.xqxy.sys.modular.cust.service.ConsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 电力用户信息表 服务实现类
 * </p>
 *
 * @author Yechs
 * @since 2022-05-20
 */
@Service
public class GwappConsServiceImpl extends ServiceImpl<GwappConsMapper, GwappCons> implements IGwappConsService {

    @Resource
    private DataAccessStrategyContext dataAccessStrategyContext;
    @Resource
    private ConsService consService;
    @Value("${consDataAccessStrategy}")
    private String consDataAccessStrategy;
    @Resource
    private SystemClient systemClient;
    @Resource
    private IGwappCustService gwappCustService;

    @Override
    public List<GwappCons> getConsByCustId(Long custId) {
        LambdaQueryWrapper<GwappCons> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(GwappCons::getCustId, custId);
        lambdaQueryWrapper.orderByAsc(GwappCons::getId);
        return this.list(lambdaQueryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<GwappCons> checkCons(Long custId) {
        List<GwappCons> consByCustId = getConsByCustId(custId);
        if (ObjectUtil.isEmpty(consByCustId)) {
            throw new ServiceException(4, "户号列表为空");
        }

        LambdaQueryWrapper<Cons> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Cons::getId, consByCustId.stream().map(GwappCons::getId).collect(Collectors.toList()));
        lambdaQueryWrapper.select(Cons::getId, Cons::getCustId);
        List<Cons> list = consService.list(lambdaQueryWrapper);
        if (ObjectUtil.isNotEmpty(list)) {
            throw new ServiceException(3, "户号: " + list.stream().map(Cons::getId).collect(Collectors.joining(",")) + " 已存在");
        }

        // 从营销档案获取用户数据,使用模拟接口
        consByCustId.stream().forEach(item -> {
            DataAccessStrategy dataAccessStrategy = dataAccessStrategyContext.strategySelect(consDataAccessStrategy);
            Cons marketCons = dataAccessStrategy.getConsFromMarketing(item.getId(), null, null);
            if (marketCons == null) {
                throw new ServiceException(2, "户号:" + item.getId() + "校核失败");
            }
            if (ObjectUtil.isEmpty(marketCons.getId())) {
                throw new ServiceException(2, "户号:" + item.getId() + "校核失败");
            }
            BeanUtils.copyProperties(marketCons, item);
            item.setCustId(custId);
        });

        String orgId = consByCustId.get(0).getOrgNo();
        JSONObject orgNameJson = systemClient.getOrgName(orgId);
        JSONObject jsonObject = orgNameJson.getJSONObject("data");
        if (!Objects.equals(orgNameJson.getString("code"), "000000") || ObjectUtil.isEmpty(jsonObject) || jsonObject.get("id") == null) {
            throw new ServiceException(4, "获取户号组织机构信息失败");
        }
        String orgTitle = jsonObject.getString("orgTitle");
        String newOrgId = jsonObject.getString("id");
        if (Objects.equals(orgTitle, "4")) {
            newOrgId = jsonObject.getString("parentId");
        }

        gwappCustService.update(new LambdaUpdateWrapper<GwappCust>().eq(GwappCust::getId, custId).set(GwappCust::getOrgNo, newOrgId));

        this.updateBatchById(consByCustId);
        return consByCustId;
    }

}
