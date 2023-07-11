package com.xqxy.dr.modular.statistics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.dr.modular.statistics.result.TotalStatisticsResult;
import com.xqxy.dr.modular.statistics.result.TotalStatisticsTableResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TotalStatisticsMapper extends BaseMapper<TotalStatisticsTableResult> {

    List<String> getEventDays();

    Integer getConsCount();

    TotalStatisticsResult getCapAndEnergy();

    TotalStatisticsResult getMaxCapAndEnergy();

    List<TotalStatisticsTableResult> getExecuteData(@Param("orgIds") List<String> orgIds);

    List<TotalStatisticsTableResult> getExecuteDataCity(@Param("orgIds") List<String> orgIds);

}
