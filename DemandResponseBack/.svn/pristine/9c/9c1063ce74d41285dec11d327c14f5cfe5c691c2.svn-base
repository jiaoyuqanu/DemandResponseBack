package com.xqxy.dr.modular.workbench.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.dr.modular.workbench.entity.Contracts;
import com.xqxy.dr.modular.workbench.param.WorkbenchParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ContractsMapper extends BaseMapper<Contracts> {
    List<Contracts> getContracts(@Param("param") WorkbenchParam projectId, @Param("responseType") String responseType,
                                 @Param("timeType") String timeType,@Param("advanceNoticeTime") String advanceNoticeTime,
                                 @Param("checkStatus") String checkStatus);
}
