package com.xqxy.dr.modular.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.event.entity.Event;
import com.xqxy.dr.modular.event.param.ConsInvitationParam;
import com.xqxy.dr.modular.project.VO.DrConsContractDetailsVO;
import com.xqxy.dr.modular.project.entity.ConsContractDetail;
import com.xqxy.dr.modular.project.entity.ConsContractInfo;
import com.xqxy.dr.modular.project.entity.ProjectDetail;
import com.xqxy.dr.modular.project.params.ConsContractParam;
import com.xqxy.dr.modular.project.params.CustContractParam;
import com.xqxy.dr.modular.workbench.VO.ContractDetailVO;
import com.xqxy.sys.modular.cust.param.BusConfigParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 用户项目申报基本信息 服务类
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-13
 */
public interface ConsContractInfoService extends IService<ConsContractInfo> {

    /**
     * @description: 通过项目编号查询该项目用户申报信息
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/15 14:27
     */
    List<ConsContractInfo> listByProjectId(Long projectId);

    /**
     * 查看签约用户列表
     *
     * @param custContractParam 客户签约输入参数
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.xqxy.dr.modular.project.entity.ConsContractInfo>
     * @date 11/14/2021 17:21
     * @author Caoj
     */
    Page<ConsContractInfo> listConsContract(CustContractParam custContractParam);

    List<ConsContractInfo> list(ConsInvitationParam consInvitationParam);

    /**
     * 新建签约
     *
     * @param consContractParam 用户签约输入参数
     * @date 10/18/2021 14:47
     * @author Caoj
     */
    Long addSigning(ConsContractParam consContractParam);

    /**
     * @description: 根据项目和时段查询时段信息
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/18 10:39
     */
    List<ConsContractInfo> listConsTractInfo(Event event);



    List<ConsContractInfo> listConsTractInfo2(Event event,List orglist);

    /**
     * 提交签约审核
     *
     * @param consContractParam 用户签约输入参数
     * @date 10/18/2021 14:50
     * @author Caoj
     */
    void submitSigning(ConsContractParam consContractParam);

    /**
     * @description: 查询符合当前事件的用户申报信息
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/10/19 15:57
     */
    List<ConsContractInfo> listConsTractInfoByOrg(Event event);


    List<ConsContractInfo> listConsTractInfoByOrg2(Event event,List orgList);


    /**
     * @description: 查询符合条件的 用户签约详情
     * @param:
     * @return:
     * @author: liqirui
     * @date: 2021/11/02 16:54
     */
    List<DrConsContractDetailsVO> queryDrConsContractDetails(Page<DrConsContractDetailsVO> page, ConsContractParam consContractParam);

    /**
     * 添加用户签约
     *
     * @param consContractParam 用户签约输入参数
     * @date 11/10/2021 16:34
     * @author Caoj
     */
    ResponseData consContract(ConsContractParam consContractParam);

    /**
     * 签约审核
     *
     * @param busConfigParam 代办输入参数
     * @date 11/10/2021 16:31
     * @author Caoj
     */
    void verifySigning(BusConfigParam busConfigParam);

    /**
     * 根据客户id找到签约的用户
     *
     * @param custId 客户标识
     * @return java.util.List<java.lang.String>
     * @date 11/13/2021 18:59
     * @author Caoj
     */
    List<ConsContractInfo> listConsByCust(String custId, String projectId);

    /**
     * 展示待审核的签约用户
     *
     * @param busConfigParam 代办输入参数
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.xqxy.dr.modular.project.entity.ConsContractInfo>
     * @date 11/16/2021 12:20
     * @author Caoj
     */
    Page<ConsContractInfo> listVerifyContract(BusConfigParam busConfigParam);

    /**
     * 根据项目标识显示项目明细
     *
     * @param custContractParam 客户签约输入参数
     * @return java.util.List<com.xqxy.dr.modular.project.entity.ProjectDetail>
     * @date 11/18/2021 17:13
     * @author Caoj
     */
    List<ProjectDetail> listContractCap(CustContractParam custContractParam);

    Page<ContractDetailVO> pageContractDetail(CustContractParam custContractParam);

    /**
     * 查询用户签约 详情根据id
     *
     * @param contractId
     * @return
     */
    List<ConsContractDetail> queryDrConsDetailByInfoId(Long contractId);

    /**
     * 查询用户签约 根据用户Id以及项目Id
     *
     * @param consId
     * @return
     * @author shi
     */
    ConsContractInfo queryByConsIdAndProjectId(String consId, Long projectId);

    /**
     * 查询用户签约详情导出
     *
     * @param consContractParam
     * @return
     */
    List<DrConsContractDetailsVO> exportDrConsDetails(ConsContractParam consContractParam);

    /**
     * 营销用户查看集成商代理的用户签约详情
     *
     * @param custContractParam 客户签约输入参数
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.xqxy.dr.modular.project.entity.ConsContractInfo>
     * @date 11/29/2021 10:01
     * @author Caoj
     */
    Page<ConsContractInfo> pageProxyContract(CustContractParam custContractParam);

    void exportConsContractTemplate(HttpServletRequest request, HttpServletResponse response, String projectId);

    Long importConsContractByExcel(String pageProjectId, MultipartFile multipartFile) throws Exception;

    /**
     * 获取最新项目审核通过的用户数量
     *
     * @return
     */
    Integer getApprovalConsCount(List<String> orgNo);

    /**
     * 获取最新项目审核通过的签约容量
     *
     * @return
     */
    BigDecimal getApprovalConstractCapSum(List<String> orgNo);

    List<Long> getConsByContractInfo(String projectId, String responseType, String timeType, String advanceTimeType);

}
