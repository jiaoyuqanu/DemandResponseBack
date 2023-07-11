package com.xqxy.dr.modular.event.mapper;

import com.xqxy.dr.modular.event.entity.BaselineLibrary;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-18
 */
public interface BaselineLibraryMapper extends BaseMapper<BaselineLibrary> {

    BaselineLibrary getByDateAndPeriod(@Param("regulateDate") LocalDate regulateDate, @Param("startTime")String startTime, @Param("endTime")String endTime);

    //根据ID查询基线库部分字段
    BaselineLibrary getByBaselinId(@Param("baselinId") String baselinId);
}
