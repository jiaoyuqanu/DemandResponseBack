package com.xqxy.dr.modular.powerplant.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.core.client.SystemClient;
import com.xqxy.core.pojo.login.CurrenUserInfo;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.dr.modular.powerplant.entity.DaConsExecute;
import com.xqxy.dr.modular.powerplant.mapper.DaConsExecuteMapper;
import com.xqxy.dr.modular.powerplant.param.DaConsExecuteParam;
import com.xqxy.dr.modular.powerplant.service.DaConsExecuteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.netty.util.internal.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shi
 * @since 2021-12-15
 */
@Service
public class DaConsExecuteServiceImpl extends ServiceImpl<DaConsExecuteMapper, DaConsExecute> implements DaConsExecuteService {

    @Resource
    SystemClient systemClient;
    @Override
    public  Page<DaConsExecute> page (DaConsExecuteParam daConsExecuteParam)
    {
        LambdaQueryWrapper<DaConsExecute> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        if(ObjectUtil.isNotNull(daConsExecuteParam))
            if(ObjectUtil.isNotEmpty(daConsExecuteParam.getConsId()))
            {
                lambdaQueryWrapper.like(DaConsExecute::getConsNo,daConsExecuteParam.getConsId());
            }
        if(ObjectUtil.isNotEmpty(daConsExecuteParam.getConsName()))
        {
            lambdaQueryWrapper.like(DaConsExecute::getConsName,daConsExecuteParam.getConsName());
        }
        if(ObjectUtil.isNotEmpty(daConsExecuteParam.getOrgNo()))
        {
            lambdaQueryWrapper.eq(DaConsExecute::getOrgNo,daConsExecuteParam.getOrgNo());
        }
        if(ObjectUtil.isNotEmpty(daConsExecuteParam.getTradeCode()))
        {
            lambdaQueryWrapper.eq(DaConsExecute::getTradeCode,daConsExecuteParam.getTradeCode());
        }
        return  this.page(daConsExecuteParam.getPage(),lambdaQueryWrapper);
    }

    @Override
    public List<DaConsExecute> list (DaConsExecuteParam daConsExecuteParam)
    {
        LambdaQueryWrapper<DaConsExecute> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        if(ObjectUtil.isNotNull(daConsExecuteParam))
            if(ObjectUtil.isNotEmpty(daConsExecuteParam.getConsId()))
            {
                lambdaQueryWrapper.eq(DaConsExecute::getConsNo,daConsExecuteParam.getConsId());
            }
        if(ObjectUtil.isNotEmpty(daConsExecuteParam.getConsName()))
        {
            lambdaQueryWrapper.like(DaConsExecute::getConsName,daConsExecuteParam.getConsName());
        }
        if(ObjectUtil.isNotEmpty(daConsExecuteParam.getOrgNo()))
        {
            lambdaQueryWrapper.eq(DaConsExecute::getOrgNo,daConsExecuteParam.getOrgNo());
        }
        if(ObjectUtil.isNotEmpty(daConsExecuteParam.getTradeCode()))
        {
            lambdaQueryWrapper.eq(DaConsExecute::getTradeCode,daConsExecuteParam.getTradeCode());
        }
        return  this.list(lambdaQueryWrapper);
    }

    @Override
    public List<DaConsExecute> regionExecuteStatistics(String orgId){
        String orgNo="";
        if(StringUtil.isNullOrEmpty(orgId))
        {
            CurrenUserInfo currentUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
            orgNo=currentUserInfo.getOrgId();
        }
        return  new ArrayList<>();
    }

}
