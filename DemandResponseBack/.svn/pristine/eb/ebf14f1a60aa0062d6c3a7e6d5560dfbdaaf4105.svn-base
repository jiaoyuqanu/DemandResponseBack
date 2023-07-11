package com.xqxy.dr.modular.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.project.entity.ProjectDetail;
import com.xqxy.dr.modular.project.params.ConsContractParam;
import com.xqxy.dr.modular.project.params.CustContractParam;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 项目明细 服务类
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-11
 */
public interface ProjectDetailService extends IService<ProjectDetail> {

    /**
     * @description: 通过项目id(projectId)删除项目明细
     * @param: 
     * @return: 
     * @author: PengChuqing
     * @date: 2021/10/11 18:36
     */
    void deleteByProjectId(Long projectId);

    /**
     * 查询项目对应的多个项目明细
     * @param projectId
     * @return
     */
    List<ProjectDetail> listByProjectId(Long projectId);


    /**
     * @description: 获取项目对应的时段数量
     * @param: 
     * @return: 
     * @author: PengChuqing
     * @date: 2021/10/12 15:14
     */
    Integer getPeriodNum(Long projectId);

    /**
     * @description: 获取提前通知时间
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/12 17:17
     */
    String getAdvanceNotice(Long projectId);

    /**
     * 用户签约详情
     *
     * @param custContractParam 客户签约参数
     * @return com.xqxy.sys.modular.cust.entity.Cons
     * @date 11/10/2021 16:24
     * @author Caoj
     */
    Map<String, Object> consDeclareDetail(CustContractParam custContractParam);

    /**
     * 项目签约明细
     *
     * @param consContractParam 用户签约参数
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @date 11/18/2021 13:54
     * @author Caoj
     */
    List<ProjectDetail> listContractDetail(ConsContractParam consContractParam);
}
