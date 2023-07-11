package com.xqxy.dr.modular.project.service;

import com.xqxy.dr.modular.project.VO.EventDetailByProjectVO;
import com.xqxy.dr.modular.project.entity.ConsContractDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.project.entity.DrOrgGoal;
import com.xqxy.dr.modular.workbench.VO.ContractProjecttDetailVO;
import com.xqxy.dr.modular.workbench.param.WorkbenchParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 申报明细 服务类
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-13
 */
public interface ConsContractDetailService extends IService<ConsContractDetail> {

    /**
     * 工作台项目签约信息
     * @param workbenchParam
     * @return
     */
    List<DrOrgGoal> contractProjecttDetail(WorkbenchParam workbenchParam);


    /**
     * 事件统计详情
     * @param projectId
     * @return
     */
    Map<String, EventDetailByProjectVO> eventDetailByProject(Long projectId);

    /**
     * 通过 id 修改 备用容量 空调容量 最小响应时长 （置空）
     *
     * @param contractDetail
     */
    void updateByDetailIdToNull(ConsContractDetail contractDetail);

    BigDecimal getContractCapByCondition(Map<String,Object> map);
}
