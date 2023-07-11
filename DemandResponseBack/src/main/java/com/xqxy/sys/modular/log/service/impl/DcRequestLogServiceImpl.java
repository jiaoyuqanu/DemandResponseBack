package com.xqxy.sys.modular.log.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.sys.modular.log.entity.DcRequestLog;
import com.xqxy.sys.modular.log.mapper.DcRequestLogMapper;
import com.xqxy.sys.modular.log.param.DrRequestLogParam;
import com.xqxy.sys.modular.log.service.DcRequestLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiao jun
 * @since 2021-06-21
 */
@Service
public class DcRequestLogServiceImpl extends ServiceImpl<DcRequestLogMapper, DcRequestLog> implements DcRequestLogService {

    @Override
    public Page<DcRequestLog> page(DrRequestLogParam drRequestLogParam) {
        LambdaQueryWrapper<DcRequestLog> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(drRequestLogParam)) {
            // 根据查询参数模糊查询
            if (ObjectUtil.isNotEmpty(drRequestLogParam.getRequestUrl())) {
                queryWrapper.like(DcRequestLog::getRequestUrl, drRequestLogParam.getRequestUrl());
            }
        }
        //根据排序升序排列，序号越小越在前
        queryWrapper.orderByAsc(DcRequestLog::getCreateTime);
        return this.page(drRequestLogParam.getPage(), queryWrapper);
    }
}
