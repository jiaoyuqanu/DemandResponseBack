package com.xqxy.dr.modular.anhui.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.dr.modular.anhui.entity.HefeiCurverSgData;
import com.xqxy.eum.DataSourceEnum;
import com.xqxy.rentation.DsSwitcher;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 合肥营销实时数据Mapper 接口
 * </p>
 *
 * @author liuyu
 * @since 2021-12-01
 */
public interface HefeiCurverSgDataMapper extends BaseMapper<HefeiCurverSgData> {

    @DsSwitcher(DataSourceEnum.PG)
    @Select("SELECT  data_time,sum(data_value) as dataValue  FROM `sca_sg_data` where cons_no=#{consNo} and left(data_time,10) BETWEEN #{dateTime} and #{dateTime} GROUP BY data_time ORDER BY data_time ")
    List<HefeiCurverSgData> queryAllByConsNoDate(String consNo,String dateTime);



}


