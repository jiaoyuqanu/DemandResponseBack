package com.xqxy.dr.modular.workbench.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.dr.modular.workbench.entity.ReserveSubsidy;
import com.xqxy.dr.modular.workbench.param.WorkbenchParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工作台Mapper接口
 */
@Mapper
public interface ReserveWorkMapper extends BaseMapper<ReserveSubsidy> {

    List<ReserveSubsidy> reserveSubsidy(@Param("param") WorkbenchParam param);

    List<ReserveSubsidy> reserveSubsidyCity(@Param("param") WorkbenchParam param);

}
