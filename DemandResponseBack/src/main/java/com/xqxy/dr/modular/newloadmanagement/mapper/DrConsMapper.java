package com.xqxy.dr.modular.newloadmanagement.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DrConsMapper {
    List<String> consIds(@Param(value = "orgNos") List orgNos);

    List<String> planId(@Param(value = "eventId")String eventId2);
}
