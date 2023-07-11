package com.xqxy.sys.modular.cust.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.sys.modular.cust.entity.Cust;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 系统用户mapper接口
 *
 * @author xuyuxiang
 * @date 2020/3/11 17:49
 */
public interface CustMapper extends BaseMapper<Cust> {

    List<Long> getAggreListByConsId(@Param("consIdList") List<String> consIdList);

    /**
     * 根据电力客户id查询集成商
     *
     * @param custId
     * @return com.xqxy.sys.modular.cust.entity.Cust
     * @author Caoj
     * @since 11/14/2021 16:02
     */
    Cust getAggregatorByCust(String custId);

    Map<String, Object> getWorkUserDetail(@Param("orgId") String orgId, @Param("year") String year);

    /**
     * 负荷聚合商数量（户)
     *
     * @return 数量
     */
    int custCount();

    List<Cust> getCustByConsId(String consId);
}
