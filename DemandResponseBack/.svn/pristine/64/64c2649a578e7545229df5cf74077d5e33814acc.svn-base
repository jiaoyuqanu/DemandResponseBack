package com.xqxy.sys.modular.cust.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.sys.modular.cust.entity.UserConsRela;

import java.util.List;

/**
 * <p>
 * 用户户号关联表 Mapper 接口
 * </p>
 *
 * @date 2021-10-18
 * @author Caoj
 */
public interface UserConsRelaMapper extends BaseMapper<UserConsRela> {

    List<UserConsRela> getUserConsByCustId(Long custId);

    /**
     * 负荷聚合商聚合客户数（户）
     *
     * @return 数量
     */
    int userConsRelaCount();
}
