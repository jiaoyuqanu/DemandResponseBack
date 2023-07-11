package com.xqxy.dr.modular.anhui.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.dr.modular.anhui.entity.HefeiCons;
import com.xqxy.sys.modular.cust.entity.Cons;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * <p>
 * 合肥营销档案 Mapper 接口
 * </p>
 *
 * @author liuyu
 * @since 2021-12-01
 */
public interface HefeiConsMapper extends BaseMapper<HefeiCons> {

    @Select("select * from dr_cons where id!=null  ")
     List<Cons> queryConsNo();
}
