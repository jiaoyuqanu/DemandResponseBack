package com.xqxy.dr.modular.newloadmanagement.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Mapper
public interface UserRealTimeLoadMapper {

    BigDecimal realLoad(@Param(value = "pi") String pi, @Param(value = "consIds") List cids, @Param("date")String date);


}
