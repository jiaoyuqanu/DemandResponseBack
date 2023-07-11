package com.xqxy.dr.modular.newloadmanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.dr.modular.newloadmanagement.entity.Drcust;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


public interface DrCustMapper extends BaseMapper<Drcust> {

//    @Update("update dr_cust set tel=#{tel} where ID=#{id}")
    int changeMobile(@Param("tel") String tel, @Param("id") String id);

//    @Update("update sc_admin_pro.t_sys_users set mobile=#{tel} where ID=#{id}")
    int changeAdminMobile(@Param("tel") String tel, @Param("id") String id);

//    @Select("select tel from dr_cust")
    List<String> queryAllTelAtDrCust();

//    @Select("select mobile from sc_admin_pro.t_sys_users")
    List<String> queryAllTelAtSysUsers();

}
