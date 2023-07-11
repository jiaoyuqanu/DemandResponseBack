package com.xqxy.dr.modular.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.event.entity.Event;
import com.xqxy.dr.modular.project.VO.DrConsContractDetailsVO;
import com.xqxy.dr.modular.project.entity.CustContractInfo;
import com.xqxy.dr.modular.project.params.ConsContractParam;
import com.xqxy.dr.modular.project.params.CustContractParam;
import com.xqxy.dr.modular.project.params.ProjectParam;

import java.util.List;

/**
 * <p>
 * 客户项目申报基本信息 服务类
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-13
 */
public interface CustContractInfoService extends IService<CustContractInfo> {

    /**
     * @description: 查找改项目的某一时段的签约客户
     * @param: 
     * @return: 
     * @author: PengChuqing
     * @date: 2021/10/18 15:26
     */
    List<CustContractInfo> listCustTractInfo(Event event);


    /**
     * @description: 查询符合条件的 用户签约详情
     * @param:
     * @return:
     * @author: liqirui
     * @date: 2021/11/02 16:54
     */
    List<DrConsContractDetailsVO> queryDrCustContractDetails(Page<DrConsContractDetailsVO> page,ConsContractParam consContractParam);

    /**
     * 签约页面分页
     *
     * @param projectParam 项目参数
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.xqxy.dr.modular.project.entity.CustContractInfo>
     * @date 12/7/2021 14:30
     * @author Caoj
     */
    Page<CustContractInfo> pageDeclareProject(ProjectParam projectParam);

    /**
    * 删除客户签约
    * @param custContractParam 客户签约参数
    * @date 11/13/2021 11:16
    * @author Caoj
    */
    void deleteCustContract(CustContractParam custContractParam);

    /**
    * 删除用户签约
    * @param consContractParam 用户签约参数
    * @date 11/13/2021 11:17
    * @author Caoj
    */
    void deleteConsContract(ConsContractParam consContractParam);

    /**
    * 删除签约明细
    * @param consContractParam 用户签约参数
    * @date 11/13/2021 11:17
    * @author Caoj
    */
    void deleteContractDetail(ConsContractParam consContractParam);


    /**
     * 客户签约撤销
     *
     * @param contractId 客户签约标识
     * @date 12/7/2021 14:33
     * @author Caoj
     */
    void recallSigning(Long contractId);

    /**
     * 查询客户签约详情导出
     * @param consContractParam
     * @return
     */
    List<DrConsContractDetailsVO> exportDrConsDetails(ConsContractParam consContractParam);
}
