package com.xqxy.sys.modular.cust.service;

import com.xqxy.sys.modular.cust.entity.UserConsRela;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户户号关联表 服务类
 * </p>
 *
 * @author Caoj
 * @since 2021-10-18
 */
public interface UserConsRelaService extends IService<UserConsRela> {

    Long getCustIdByConsId(String consId);

    List<UserConsRela> getUserConsByCustId(Long custId);

}
