package com.xqxy.sys.modular.log.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.sys.modular.log.entity.SysOpLog;
import com.xqxy.sys.modular.log.mapper.SysOpLogMapper;
import com.xqxy.sys.modular.log.param.SysOpLogParam;
import com.xqxy.sys.modular.log.service.SysOpLogService;
import org.springframework.stereotype.Service;

/**
 * 系统操作日志service接口实现类
 *
 * @author xuyuxiang
 * @date 2020/3/12 14:22
 */
@Service
public class SysOpLogServiceImpl extends ServiceImpl<SysOpLogMapper, SysOpLog> implements SysOpLogService {

    @Override
    public Page<SysOpLog> page(SysOpLogParam sysOpLogParam) {
        LambdaQueryWrapper<SysOpLog> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(sysOpLogParam)) {
            //根据名称模糊查询
            if (ObjectUtil.isNotEmpty(sysOpLogParam.getName())) {
                queryWrapper.like(SysOpLog::getName, sysOpLogParam.getName());
            }
            //根据操作类型查询
            if (ObjectUtil.isNotEmpty(sysOpLogParam.getOpType())) {
                queryWrapper.eq(SysOpLog::getOpType, sysOpLogParam.getOpType());
            }
            //根据是否成功查询
            if (ObjectUtil.isNotEmpty(sysOpLogParam.getSuccess())) {
                queryWrapper.eq(SysOpLog::getSuccess, sysOpLogParam.getSuccess());
            }
            //根据url模糊查询
            if (ObjectUtil.isNotEmpty(sysOpLogParam.getUrl())) {
                queryWrapper.like(SysOpLog::getUrl, sysOpLogParam.getUrl());
            }
            //根据方法名查询
            if (ObjectUtil.isNotEmpty(sysOpLogParam.getMethodName())) {
                queryWrapper.like(SysOpLog::getMethodName, sysOpLogParam.getMethodName());
            }
            //根据账号查询
            if (ObjectUtil.isNotEmpty(sysOpLogParam.getAccount())) {
                queryWrapper.eq(SysOpLog::getAccount, sysOpLogParam.getAccount());
            }
        }
        Page<SysOpLog> page = this.page(sysOpLogParam.getPage(), queryWrapper);
        return page;
    }

    @Override
    public void delete() {
        this.remove(new QueryWrapper<>());
    }
}
