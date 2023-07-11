package com.xqxy.dr.modular.baseline.service.impl;

import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.dr.modular.baseline.entity.BaseLine;
import com.xqxy.dr.modular.baseline.entity.PlanBaseLine;
import com.xqxy.dr.modular.baseline.entity.PlanBaseLineAll;
import com.xqxy.dr.modular.baseline.mapper.BaseLineMapper;
import com.xqxy.dr.modular.baseline.mapper.PlanBaseLineMapper;
import com.xqxy.dr.modular.baseline.param.BaseLineParam;
import com.xqxy.dr.modular.baseline.service.BaseLineDetailService;
import com.xqxy.dr.modular.baseline.service.PlanBaseLineService;
import com.xqxy.sys.modular.cust.service.ConsService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 事件基线计算任务 服务实现类
 * </p>
 *
 * @author chen zhi jun
 * @since 2021-10-18
 */
@Service
public class PlanBaseLineServiceImpl extends ServiceImpl<PlanBaseLineMapper, PlanBaseLine> implements PlanBaseLineService {

    private static final Log log = Log.get();

    @Resource
    BaseLineMapper baseLineMapper;

    @Override
    public List<PlanBaseLine> getPlanBaseLineList() {
        return baseLineMapper.getPlanBaseLineList();
    }

    @Override
    public List<PlanBaseLine> getPlanBaseLineListAll() {
        return baseLineMapper.getPlanBaseLineListAll();
    }
}
