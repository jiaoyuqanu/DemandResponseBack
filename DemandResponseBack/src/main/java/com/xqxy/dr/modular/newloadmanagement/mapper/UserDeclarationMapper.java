package com.xqxy.dr.modular.newloadmanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.dr.modular.newloadmanagement.entity.DrConsContractDetail;
import com.xqxy.dr.modular.newloadmanagement.entity.Drproject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.hpsf.Decimal;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface UserDeclarationMapper {

    Integer userCount(@Param(value = "projectIds") List pids,@Param(value = "consIds") List cids);

    List<String> contractId(@Param(value = "projectIds") List pids,@Param(value = "consIds") List cids);

    BigDecimal sumContractCap(@Param(value = "contractIds") List contractIds);

}
