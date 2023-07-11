package com.xqxy.dr.modular.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.data.dto.WorkProjectInfoDTO;
import com.xqxy.dr.modular.data.param.WorkProjectParam;
import com.xqxy.dr.modular.data.result.GroupContractByTimeTypeResult;
import com.xqxy.dr.modular.project.VO.DrConsContractDetailsVO;
import com.xqxy.dr.modular.project.entity.ConsContractInfo;
import com.xqxy.dr.modular.project.params.ConsContractParam;
import com.xqxy.dr.modular.project.po.NewstConsContractInfo;
import com.xqxy.dr.modular.statistics.VO.ConsBigClassCodeVO;
import com.xqxy.dr.modular.statistics.param.StatisticalParam;
import com.xqxy.sys.modular.cust.result.ConsStatisticsMonthCountResult;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 用户项目申报基本信息 Mapper 接口
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-13
 */
public interface ConsContractInfoMapper extends BaseMapper<ConsContractInfo> {


    /**
     * @description: 查询符合条件的 用户签约详情
     * @param:
     * @return:
     * @author: liqirui
     * @date: 2021/11/02 16:54
     */
    List<DrConsContractDetailsVO> queryDrConsContractDetails(@Param("page") Page<DrConsContractDetailsVO> page, @Param("consContractParam") ConsContractParam consContractParam);

    // List<ConsContractInfo> listByProjectId(Long projectId);

    List<ConsContractInfo> listConsTractInfo(@Param("projectId") Long projectId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    List<ConsContractInfo> listConsTractInfoByProvince(@Param("projectId") Long projectId, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("regionCode") String regionCode);

    List<ConsContractInfo> listConsTractInfoByCity(@Param("projectId") Long projectId, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("regionCode") String regionCode);

    List<ConsContractInfo> listConsTractInfoByCounty(@Param("projectId") Long projectId, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("regionCode") String regionCode);

    List<ConsContractInfo> listConsTractInfoByOrg(@Param("projectId") Long projectId, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("regionCode") String regulateRange);

    List<ConsContractInfo> listConsTractInfoReigon(@Param("projectId") Long projectId, @Param("advanceNoticeTime") String advanceNoticeTime, @Param("responseType") String responseType, @Param("timeType") String timeType, @Param("provinceList") List<String> provinceList, @Param("cityList") List<String> cityList, @Param("countyList") List<String> countyList);

    List<ConsContractInfo> listConsTractInfoOrg(@Param("projectId") Long projectId, @Param("advanceNoticeTime") String advanceNoticeTime, @Param("responseType") String responseType, @Param("timeType") String timeType, @Param("orgNoList") List<String> orgNoList);

    /**
     * 直接参与用户签约列表
     *
     * @param page      分页参数
     * @param projectId 项目标识
     * @param custId    客户标识
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.xqxy.dr.modular.project.entity.ConsContractInfo>
     * @date 11/13/2021 12:20
     * @author Caoj
     */
    Page<ConsContractInfo> listConsContract(Page<?> page, Long projectId, Long custId);

    /**
     * 集成商代理用户签约列表
     *
     * @param page      分页
     * @param projectId 项目标识
     * @param custId    客户标识
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.xqxy.dr.modular.project.entity.ConsContractInfo>
     * @date 11/13/2021 12:22
     * @author Caoj
     */
    Page<ConsContractInfo> listConsContractAgg(Page<?> page, Long projectId, Long custId);

    /**
     * 代理用户展示被代理的用户签约列表
     *
     * @param page       分页
     * @param projectId  项目标识
     * @param custId     客户标识
     * @param aggregator 集成商标识
     * @return Page<ConsContractInfo>
     * @author Caoj
     * @date 12/6/2021 14:10
     */
    Page<ConsContractInfo> listConsContractProxy(Page<?> page, Long projectId, Long custId, Long aggregator);

    /**
     * 查询用户签约详情导出
     *
     * @param consContractParam
     * @return
     */
    List<DrConsContractDetailsVO> exportDrConsDetails(@Param("consContractParam") ConsContractParam consContractParam);

    /**
     * 营销用户查看集成商代理的用户签约详情
     *
     * @param page      分页参数
     * @param projectId 项目标识
     * @param custId    客户标识
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.xqxy.dr.modular.project.entity.ConsContractInfo>
     * @author Caoj
     * @date 11/29/2021 10:18
     */
    Page<ConsContractInfo> pageProxyContract(Page<?> page, Long projectId, Long custId);

    List<WorkProjectInfoDTO> getWorkPageCapDetail(@Param("projectId") String projectId, @Param("orgNos") List<String> orgNo);

    List<WorkProjectInfoDTO> getWorkPageCapDetail1(@Param("projectId") String projectId, @Param("orgNos") List<String> orgNo);

    /**
     * 查询（审核通过）用户数量  按照用户大类分组
     * @return
     */
    List<ConsBigClassCodeVO> groupConsBigClassCode(@Param("projectId") Long projectId,@Param("checkStatus") String checkStatus);

    Integer getApprovalConsCount(@Param("orgNos") List<String> orgNo);

    BigDecimal getApprovalConstractCapSum(@Param("orgNos") List<String> orgNo);

    List<NewstConsContractInfo> getNewstConsContractInfo();

    List<Long> getConsContractInfosByDetail(@Param("projectId") String projectId,
                                            @Param("responseType") String responseType,
                                            @Param("timeType") String timeType,
                                            @Param("advanceTimeType") String advanceTimeType);

    /**
     * 提供给 示范工程 根据项目id 和 orgId 查询签约人数 签约负荷总量
     * @param workProjectParam
     * @return
     */
    List<GroupContractByTimeTypeResult> groupContractByTimeType(WorkProjectParam workProjectParam);


    /**
     * 统计 每个月用户签约详情 数量
     * @param statisticalParam
     * @return
     */
    List<ConsStatisticsMonthCountResult> consStatisticsMonthCount(StatisticalParam statisticalParam);


    /**
     * 统计 每个月用户签约详情 数量
     * @param statisticalParam
     * @return
     */
    List<ConsStatisticsMonthCountResult> consStatisticsCapCount(StatisticalParam statisticalParam);
}
