package com.xqxy.dr.modular.event.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xqxy.dr.modular.event.entity.PlanBaselineTask;
import com.xqxy.dr.modular.event.enums.BaselineStatusEnum;
import com.xqxy.dr.modular.event.mapper.PlanBaselineTaskMapper;
import com.xqxy.dr.modular.event.service.PlanBaselineTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 事件基线计算任务表，事件创建后插入数据，计算后更新 服务实现类
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-19
 */
@Service
public class PlanBaselineTaskServiceImpl extends ServiceImpl<PlanBaselineTaskMapper, PlanBaselineTask> implements PlanBaselineTaskService {
    @Resource
    private PlanBaselineTaskMapper planBaselineTaskMapper;
    @Override
    public Boolean endOfCalculation(Long baselineId) {
        PlanBaselineTask planBaselineTask = new PlanBaselineTask();
        planBaselineTask.setBaselinId(baselineId);
        //判断是否有未计算的基线任务
        Integer i = planBaselineTaskMapper.getCount(planBaselineTask);
        //判断是否有未同步完的用户基线(即客户基线未全部计算完的)
        //Integer j = planBaselineTaskMapper.getConsBaseCount(planBaselineTask);
        //判断用户基线表是否为空
        Integer z = planBaselineTaskMapper.getCustBaseCount(planBaselineTask);
        boolean flag = false;
        if(i>0 || z==0) {
            flag=true;
        }
        return flag;
    }
}
