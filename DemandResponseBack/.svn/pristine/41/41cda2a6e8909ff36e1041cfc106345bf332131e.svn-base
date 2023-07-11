package com.xqxy.sys.modular.cust.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.sys.modular.cust.entity.UserConsRela;
import com.xqxy.sys.modular.cust.enums.ConsExceptionEnum;
import com.xqxy.sys.modular.cust.mapper.UserConsRelaMapper;
import com.xqxy.sys.modular.cust.service.UserConsRelaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户户号关联表 服务实现类
 * </p>
 *
 * @author Caoj
 * @since 2021-10-18
 */
@Service
public class UserConsRelaServiceImpl extends ServiceImpl<UserConsRelaMapper, UserConsRela> implements UserConsRelaService {

    @Override
    public Long getCustIdByConsId(String consId) {
        LambdaQueryWrapper<UserConsRela> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserConsRela::getConsNo, consId);
        List<UserConsRela> UserConsRelalists = this.list(queryWrapper);
        if (UserConsRelalists.size() > 0) {
            Long custId = UserConsRelalists.get(0).getCustId();
            return custId;
        } else {
            throw new ServiceException(ConsExceptionEnum.PROXY_USER_NOT_EXIST);
        }
    }

    @Override
    public List<UserConsRela> getUserConsByCustId(Long custId) {
        return baseMapper.getUserConsByCustId(custId);
    }

}
