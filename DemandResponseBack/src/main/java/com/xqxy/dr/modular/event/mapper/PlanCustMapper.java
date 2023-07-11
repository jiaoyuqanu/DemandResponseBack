package com.xqxy.dr.modular.event.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.data.param.CustCurveParam;
import com.xqxy.dr.modular.event.entity.PlanCons;
import com.xqxy.dr.modular.event.entity.PlanCust;
import com.xqxy.dr.modular.event.param.DeleteCustParam;
import com.xqxy.dr.modular.event.param.PlanCustParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 方案参与客户 Mapper 接口
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-09
 */
public interface PlanCustMapper extends BaseMapper<PlanCust> {
    /**
     * 执行监测-客户监测-分页
     * @param defaultPage
     * @param planCustParam
     * @return
     */
    Page<PlanCons> pageCustMonitor(Page<Object> defaultPage, @Param("planCustParam") PlanCustParam planCustParam);

    /**
     * 单个聚合商实时曲线
     * @param custCurveParam
     * @return
     */
    ConsCurve getCustCurveToday(CustCurveParam custCurveParam);


    /**
     * 单个聚合商历史曲线
     * @param custCurveParam
     * @return
     */
    ConsCurve getCustCurveHistory(CustCurveParam custCurveParam);

    /**
     * 查询未被剔除的方案用户
     * @return
     */
    List<PlanCust> getPlanNoDe(@Param("eventIds") List<Long> eventIds);

    int getPlanCustCount(DeleteCustParam deleteCustParam);

    void batchUpdateImplement(@Param("planList")  List<PlanCust> planCustList);

    void batchUpdateDelete(@Param("planList")  List<PlanCust> planCustList);

    void batchUpdateDeleteCons(@Param("planList")  List<PlanCons> planCons);

}
