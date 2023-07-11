package com.xqxy.dr.modular.event.service.abstractService;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xqxy.dr.modular.event.service.IAppealExamineService;
import com.xqxy.dr.modular.subsidy.entity.SubsidyAppeal;
import com.xqxy.dr.modular.subsidy.mapper.SubsidyAppealMapper;
import javax.annotation.Resource;
import java.util.List;

/**
 * <p></p>
 *
 * @author djj Create on 2022/8/23
 * @version 1.0
 */
public abstract class AbstractAppealExamineService implements IAppealExamineService {

    @Resource
    private SubsidyAppealMapper subsidyAppealMapper;

    //未受理，未审批(初始状态)
    public static final String WAIT_PROCESS = "1";
    //审批中，受理中(提交状态)
    public static final String SUBMIT_STATUS = "2";
    //撤回
    public static final String CANCEL_STATUS = "3";
    //被驳回(被上一级驳回)
    public static final String ABORT_STATUS = "4";
    //刷新(本级驳回或上级撤回后刷新本级状态为  9-不可见)
    public static final String REFRESH_STATUS = "9";


    /**
     * 根据id获取当前审批记录，用于查看前后层级审批状态
     *
     * @param id
     * @return
     */
    public SubsidyAppeal getCurrentAppeal(long id) {
        SubsidyAppeal subsidyAppeal = new SubsidyAppeal();
        LambdaQueryWrapper<SubsidyAppeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SubsidyAppeal::getId, id);
        List<SubsidyAppeal> objects = subsidyAppealMapper.selectList(queryWrapper);
        if (objects.size() > 0) {
            subsidyAppeal = objects.get(0);
        }
        return subsidyAppeal;
    }

}
