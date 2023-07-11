package com.xqxy.dr.modular.event.service;

import com.xqxy.dr.modular.event.param.MyRepresentation;
import com.xqxy.dr.modular.subsidy.entity.SubsidyAppeal;

/**
 * <p></p>
 *
 * @author djj Create on 2022/8/23
 * @version 1.0
 */
public interface IAppealExamineService {
    //提交
    void appealExamineSubmit(MyRepresentation myRepresentation);
    //撤回
    void appealExamineCancel(MyRepresentation myRepresentation);
    //查看详情
    SubsidyAppeal appealExamineDetail(MyRepresentation myRepresentation);
    //驳回
    void appealExamineTurnDown(MyRepresentation myRepresentation);
}
