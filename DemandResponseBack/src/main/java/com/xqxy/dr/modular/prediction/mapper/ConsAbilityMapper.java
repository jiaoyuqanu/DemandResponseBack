package com.xqxy.dr.modular.prediction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.dr.modular.prediction.entity.ConsAbility;
import com.xqxy.dr.modular.prediction.param.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wangyunfei
 * @since 2021-11-04
 */
public interface ConsAbilityMapper extends BaseMapper<ConsAbility> {

    /**
     * 区域基线负荷
     * @param statDate
     * @param areaId
     * @param areaType
     * @return
     */
    AreaCurveBase sumByArea(@Param("statDate")String statDate,@Param("areaId")String areaId,@Param("areaType")String areaType);

    /**
     * 用户基线负荷
     * @param consId
     * @param statDate
     * @return
     */
    List<ConsCurveBase> loadConsCurveBase(@Param("consId")String consId, @Param("statDate")String statDate);


    /**
     * 用户能力查询
     * @param consId
     * @param statDate
     * @return
     */
    List<ConsAbilityParam> queryByConsIdAndStatDate(@Param("consId")String consId, @Param("statDate")String statDate);


    /**
     * 区域响应能力汇总
     * @param statDate
     * @param regulatoryType
     * @param areaId
     * @param areaType
     * @return
     */
    AreaAbility sumByAreaIdsAndStatDate( @Param("statDate")String statDate, @Param("regulatoryType")String regulatoryType,@Param("areaId")String areaId,@Param("areaType")String areaType);

    /**
     *  区域实际负荷
     * @param parameter
     * @return
     */
    AreaCurve sumCurveByArea(ConsCurveParam parameter);
}
