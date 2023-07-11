package com.xqxy.dr.modular.anhui.mapper;


import com.xqxy.dr.modular.data.entity.ConsCurve;

/**
 * <p>
 * 合肥历史负荷数据Mapper 接口
 * </p>
 *
 * @author liuyu
 * @since 2021-12-01
 */
public interface CustHisMapper {

    ConsCurve queryListByTable(String tableName,String consNo,String dataTime);



}
