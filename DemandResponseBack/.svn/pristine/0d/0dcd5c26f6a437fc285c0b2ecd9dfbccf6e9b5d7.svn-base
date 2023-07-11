package com.xqxy.dr.modular.event.service.impl;

import com.xqxy.core.exception.ServiceException;
import com.xqxy.dr.modular.event.param.MyRepresentation;
import com.xqxy.dr.modular.event.service.abstractService.AbstractAppealExamineService;
import com.xqxy.dr.modular.subsidy.entity.SubsidyAppeal;
import com.xqxy.dr.modular.subsidy.param.SubsidyAppealParam;
import com.xqxy.dr.modular.subsidy.service.SubsidyAppealService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p></p>
 *
 * @author lilong Create on 2022/8/23
 * @version 1.0
 */

@Service("userAppealExamineService")
public class UserLevelAppealExamineServiceImpl extends AbstractAppealExamineService {

    @Resource
    private SubsidyAppealService subsidyAppealService;

    @Override
    public void appealExamineSubmit(MyRepresentation myRepresentation) throws ServiceException {
        SubsidyAppealParam subsidyAppealParam = new SubsidyAppealParam();
        subsidyAppealParam.setId(Long.valueOf(myRepresentation.getDrSubsidyAppealId()));
        subsidyAppealParam.setStatus(SUBMIT_STATUS);
        subsidyAppealParam.setStatusCity(WAIT_PROCESS);
        SubsidyAppeal currentAppeal = getCurrentAppeal(Long.valueOf(myRepresentation.getDrSubsidyAppealId()));
        if (currentAppeal != null && currentAppeal.getStatus().equals("6")) {
            throw new ServiceException(99999, "流程申述失败，请重新申述！");
        }
        subsidyAppealService.update(subsidyAppealParam);
    }

    /**
     * 撤回
     *
     * @param myRepresentation
     */
    @Override
    public void appealExamineCancel(MyRepresentation myRepresentation) throws ServiceException {
        SubsidyAppealParam subsidyAppealParam = new SubsidyAppealParam();
        subsidyAppealParam.setId(Long.valueOf(myRepresentation.getDrSubsidyAppealId()));
        subsidyAppealParam.setStatus(CANCEL_STATUS);
        subsidyAppealParam.setStatusCity("9");
        SubsidyAppeal currentAppeal = getCurrentAppeal(Long.valueOf(myRepresentation.getDrSubsidyAppealId()));
        if (currentAppeal != null && (currentAppeal.getStatusCity().equals("1")||currentAppeal.getStatusCity().equals("3"))){
            subsidyAppealService.update(subsidyAppealParam);
        } else {
            throw new ServiceException(99999, "流程已经不可撤回！");
        }

    }

    @Override
    public SubsidyAppeal appealExamineDetail(MyRepresentation myRepresentation) {
        return null;
    }


    @Override
    public void appealExamineTurnDown(MyRepresentation myRepresentation) {

    }
}
