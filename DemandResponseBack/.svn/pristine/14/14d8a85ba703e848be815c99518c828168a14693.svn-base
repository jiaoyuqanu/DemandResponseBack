package com.xqxy.dr.modular.event.service.impl;

import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.pojo.login.CurrenUserInfo;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.dr.modular.event.param.MyRepresentation;
import com.xqxy.dr.modular.event.service.abstractService.AbstractAppealExamineService;
import com.xqxy.dr.modular.subsidy.entity.SubsidyAppeal;
import com.xqxy.dr.modular.subsidy.param.SubsidyAppealParam;
import com.xqxy.dr.modular.subsidy.service.SubsidyAppealService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p></p>
 *
 * @author lilong Create on 2022/8/23
 * @version 1.0
 */

@Service("provinceAppealExamineService")
public class provinceLevelAppealExamineServiceImpl extends AbstractAppealExamineService {

    @Resource
    private SubsidyAppealService subsidyAppealService;

    @Override
    public void appealExamineSubmit(MyRepresentation myRepresentation) throws ServiceException {
        SubsidyAppealParam subsidyAppealParam = new SubsidyAppealParam();

        //查询用户信息
        CurrenUserInfo currenUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if (currenUserInfo != null) {
            subsidyAppealParam.setProvinceUser(currenUserInfo.getName());
        }

        subsidyAppealParam.setId(Long.valueOf(myRepresentation.getDrSubsidyAppealId()));
        subsidyAppealParam.setStatusProvince("2");
        subsidyAppealParam.setStatusEnergy("1");
        SubsidyAppeal currentAppeal = getCurrentAppeal(Long.valueOf(myRepresentation.getDrSubsidyAppealId()));
        if (currentAppeal != null && !currentAppeal.getStatusCity().equals(CANCEL_STATUS)) {
            if (!StringUtils.isEmpty(myRepresentation.getFileIds())) {
                subsidyAppealParam.setFileIds(myRepresentation.getFileIds());
            }
            if (!StringUtils.isEmpty(myRepresentation.getFilesName())) {
                subsidyAppealParam.setFilesName(myRepresentation.getFilesName());
            }
            if (!StringUtils.isEmpty(myRepresentation.getExamineSuggestionProvince())) {
                subsidyAppealParam.setExamineSuggestionProvince(myRepresentation.getExamineSuggestionProvince());
            }
            subsidyAppealParam.setSubmitProvinceTime(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
            subsidyAppealService.update(subsidyAppealParam);
        } else {
            throw new ServiceException(99999, "当前申诉记录为空或者已被撤回");
        }
    }

    @Override
    public void appealExamineCancel(MyRepresentation myRepresentation) {

    }

    @Override
    public SubsidyAppeal appealExamineDetail(MyRepresentation myRepresentation) {

        return null;
    }

    /**
     * 驳回
     *
     * @param myRepresentation
     */
    @Override
    public void appealExamineTurnDown(MyRepresentation myRepresentation) {
        SubsidyAppealParam subsidyAppealParam = new SubsidyAppealParam();

        //查询用户信息
        CurrenUserInfo currenUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if (currenUserInfo != null) {
            subsidyAppealParam.setProvinceUser(currenUserInfo.getName());
        }

        subsidyAppealParam.setId(Long.valueOf(myRepresentation.getDrSubsidyAppealId()));
        subsidyAppealParam.setStatus("6");
        subsidyAppealParam.setStatusCity("6");
        subsidyAppealParam.setStatusProvince("3");
        //subsidyAppealParam.setStatusEnergy("2");
        if (!StringUtils.isEmpty(myRepresentation.getFileIds())) {
            subsidyAppealParam.setFileIds(myRepresentation.getFileIds());
        }
        if (!StringUtils.isEmpty(myRepresentation.getFilesName())) {
            subsidyAppealParam.setFilesName(myRepresentation.getFilesName());
        }
        if (!StringUtils.isEmpty(myRepresentation.getExamineSuggestionProvince())) {
            subsidyAppealParam.setExamineSuggestionProvince(myRepresentation.getExamineSuggestionProvince());
        }
        subsidyAppealService.update(subsidyAppealParam);
    }
}
