package com.xqxy.dr.modular.powerplant.mapper;

import com.xqxy.dr.modular.powerplant.entity.DaPowerplant;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * 电厂档案 Mapper 接口
 * @author lixiaojun
 * @since 2021-10-21
 */
public interface DaPowerplantMapper extends BaseMapper<DaPowerplant> {

    @Select("select concat(replace(curdate(),'-',''),'-',lpad(right(ifnull(max(num),'000'),3)+1,3,0)) num from ${value} where date(create_time) = curdate()")
    String getNum(String tableName);

}
