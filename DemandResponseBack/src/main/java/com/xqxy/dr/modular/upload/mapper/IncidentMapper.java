package com.xqxy.dr.modular.upload.mapper;

import com.xqxy.dr.modular.upload.entity.ContractInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 需求响应事件 Mapper 接口
 * </p>
 *
 */
@Mapper
public interface IncidentMapper {

    List<ContractInfo> getIncident();
}
