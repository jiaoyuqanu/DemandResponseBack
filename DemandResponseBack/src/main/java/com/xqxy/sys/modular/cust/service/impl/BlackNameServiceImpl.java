package com.xqxy.sys.modular.cust.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.sys.modular.cust.entity.BlackName;
import com.xqxy.sys.modular.cust.mapper.BlackNameMapper;
import com.xqxy.sys.modular.cust.param.BlackNameParam;
import com.xqxy.sys.modular.cust.service.BlackNameService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlackNameServiceImpl extends ServiceImpl<BlackNameMapper, BlackName> implements BlackNameService {

    private static final Log log = Log.get();


    @Override
    public Page<BlackName> page(BlackNameParam blackNameParam) {
        //先删除过期黑名单
        baseMapper.deleteByTime();
        //设置查询条件
        LambdaQueryWrapper<BlackName> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotEmpty(blackNameParam.getYear())) {
            queryWrapper.eq(BlackName::getYear, blackNameParam.getYear());
        }
        if (ObjectUtil.isNotEmpty(blackNameParam.getConsId())) {
            queryWrapper.like(BlackName::getConsId, blackNameParam.getConsId());
        }
        if (ObjectUtil.isNotEmpty(blackNameParam.getConsName())) {
            queryWrapper.like(BlackName::getConsName, blackNameParam.getConsName());
        }
        queryWrapper.eq(BlackName::getIsEffective,"Y");
        queryWrapper.orderByDesc(BlackName::getCreateTime);
        Page<BlackName> objectPage = blackNameParam.getPage();
        objectPage.setCurrent(blackNameParam.getCurrent());
        objectPage.setSize(blackNameParam.getSize());
        return this.page(objectPage, queryWrapper);
    }

    @Override
    public void deleteById(BlackNameParam blackNameParam) {
        if(null!=blackNameParam && null!=blackNameParam.getIds()) {
            Long [] ids = blackNameParam.getIds();
            if(ids.length>0) {
                List<BlackName> blackNameList = new ArrayList<>();
                for (Long id : ids) {
                    BlackName blackName = new BlackName();
                    blackName.setId(id);
                    blackName.setIsEffective("N");
                    blackNameList.add(blackName);
                }
                this.updateBatchById(blackNameList);
            }
        }
    }
}

