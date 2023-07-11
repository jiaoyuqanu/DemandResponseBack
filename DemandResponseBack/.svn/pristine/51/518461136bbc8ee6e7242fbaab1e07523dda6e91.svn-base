package com.xqxy.dr.modular.baseline.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.dr.modular.baseline.entity.BaseLine;
import com.xqxy.dr.modular.baseline.entity.CustBaseLineDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 客户基线管理 Mapper 接口
 * </p>
 *
 * @author chen zhi jun
 * @since 2021-10-18
 */
public interface CustBaseLineMapper extends BaseMapper<CustBaseLineDetail> {
    /**
     * 查询集成商基线
     * @return
     */
    List<CustBaseLineDetail> getCustBaseLine();

    /**
     * 查询集成商基线
     * @return
     */
    List<CustBaseLineDetail> getCustBaseLineAll();

    /**
     * 查询集成商历史曲线
     * @return
     */
    List<CustBaseLineDetail> getCustCurve(@Param("sampleDateList") List<String> sampleDateList,@Param("consList")List<String> cons);

    /**
     * 查询集成商历史曲线
     * @return
     */
    List<CustBaseLineDetail> getCustCurveAll(@Param("sampleDateList") List<String> sampleDateList,@Param("consList")List<String> cons);

    /**
     * 查询客户和用户对应关系
     * @return
     */
    List<CustBaseLineDetail> getCustAndConsInfo();

    /**
     * 查询客户基本信息
     * @return
     */
    List<CustBaseLineDetail> getCustBaseLineInfo();

    /**
     * 查询客户今日曲线
     * @return
     */
    List<CustBaseLineDetail> getCustCurveToday(@Param("sampleDateList") List<String> sampleDateList,@Param("consList")List<String> cons);


}
