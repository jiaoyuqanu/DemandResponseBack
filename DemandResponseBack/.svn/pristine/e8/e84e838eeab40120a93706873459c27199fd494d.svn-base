package com.xqxy.dr.modular.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.project.VO.DrConsContractDetailsVO;
import com.xqxy.dr.modular.project.entity.ConsContractInfo;
import com.xqxy.dr.modular.project.entity.CustContractInfo;
import com.xqxy.dr.modular.project.params.ConsContractParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 客户项目申报基本信息 Mapper 接口
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-13
 */
public interface CustContractInfoMapper extends BaseMapper<CustContractInfo> {

    List<CustContractInfo> listCustTractInfo(@Param("projectId") Long projectId, @Param("consIdList")List<String> consIdList);


    /**
     * @description: 查询符合条件的 客户签约详情
     * @param:
     * @return:
     * @author: liqirui
     * @date: 2021/11/02 16:54
     */
    List<DrConsContractDetailsVO> queryDrCustContractDetails(@Param("page") Page<DrConsContractDetailsVO> page,@Param("consContractParam")ConsContractParam consContractParam);


   /**
    * 客户签约项目列表
    *
    * @param page 分页
    * @param custId 客户标识
    * @param projectName 项目名称
    * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.xqxy.dr.modular.project.entity.CustContractInfo>
    * @data 11/13/2021 12:21
    * @author Caoj
    */
    Page<CustContractInfo> pageDeclareProject(Page<?> page, String custId, String projectName, String consId);

    /**
     * 代理用户展示签约项目
     *
     * @param page 分页
     * @param custId 客户标识
     * @param projectName 项目名称
     * @param agentCustId 代理客户标识
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.xqxy.dr.modular.project.entity.CustContractInfo>
     * @data 11/24/2021 16:16
     * @author Caoj
     */
    Page<CustContractInfo> pageAgentContractProject(Page<?> page,
                                                    String custId, String projectName, String agentCustId);


    /**
     * 查询客户签约详情导出
     * @param consContractParam
     * @return
     */
    List<DrConsContractDetailsVO> exportDrConsDetails(ConsContractParam consContractParam);

    Integer getIntegratorContractSize(String projectId);

    List<CustContractInfo> listConsTractInfoReigon(@Param("projectId") Long projectId, @Param("advanceNoticeTime") String advanceNoticeTime, @Param("responseType") String responseType, @Param("timeType") String timeType, @Param("provinceList") List<String> provinceList, @Param("cityList") List<String> cityList, @Param("countyList") List<String> countyList);

}
