package com.xqxy.dr.modular.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.project.VO.DrConsContractDetailsVO;
import com.xqxy.dr.modular.project.entity.ConsContractDetail;
import com.xqxy.dr.modular.workbench.VO.ContractDetailVO;
import com.xqxy.dr.modular.workbench.VO.ContractProjecttDetailVO;
import com.xqxy.dr.modular.workbench.param.WorkbenchParam;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 申报明细 Mapper 接口
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-13
 */
public interface ConsContractDetailMapper extends BaseMapper<ConsContractDetail> {

    /**
     * 查询对应项目明细 的 用户签约明细
     * @param projectDetailId
     * @return
     */
    List<DrConsContractDetailsVO> queryConsContractDetailGroupOrgNo(@Param("projectDetailId") Long projectDetailId,@Param("checkStatus") String checkStatus);

    /**
     * 工作台项目签约信息
     * @param workbenchParam
     * @return
     */
    List<ContractProjecttDetailVO> contractProjecttDetail(WorkbenchParam workbenchParam);

    List<ContractDetailVO> pageContractDetail(@Param("map") Map<String,Object> map);

    List<ContractDetailVO> pageContractDetailPage(Page<ContractDetailVO> page, Long contractId);

    /**
     * 通过 id 修改 备用容量 空调容量 最小响应时长 （置空）
     *
     * @param contractDetail
     */
    void updateByDetailIdToNull(ConsContractDetail contractDetail);

    BigDecimal getContractCapByCondition(Map<String, Object> map);

    List<ContractDetailVO> selectListByContractId(String contractId);

    List<ContractDetailVO> listContractDetailVO(@Param("map") Map<String, Object> map);

    List<DrConsContractDetailsVO> selectInfoByProjectId(@Param("projectId") Long projectId);

    /**
     * 负荷聚合商聚合负荷规模（万kW）上报
     *
     * @return 数量
     */
    BigDecimal consContractDetailCount();

    /**
     * 不同分类行业签约容量
     *
     * @param customerType1 100--->工业用户、200--->楼宇用户、
     * @param orgNoList     供电编码
     * @param projectId     最新项目id
     * @return BigDecimal
     */
    BigDecimal userContractedCapacity(@Param("customerType1") String customerType1, @Param("orgNoList") List<String> orgNoList, @Param("projectId") String projectId);

    /**
     * 不同分类行业签约容量
     *
     * @param customerType1 100--->工业用户、200--->楼宇用户、
     * @param customerType2 100--->工业用户、200--->楼宇用户、
     * @param orgNoList     供电编码
     * @param projectId     最新项目id
     * @return BigDecimal
     */
    BigDecimal userContractedCapacity1(@Param("customerType1") String customerType1, @Param("customerType2") String customerType2, @Param("orgNoList") List<String> orgNoList, @Param("projectId") String projectId);
}
