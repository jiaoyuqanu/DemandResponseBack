package com.xqxy.dr.modular.upload.service.impl;


import com.xqxy.dr.modular.upload.entity.Drcons;
import com.xqxy.dr.modular.upload.mapper.UserMapper;
import com.xqxy.dr.modular.upload.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 需求响应用户 服务实现类 & 用户年度响应能力
 * </p>
 *
 */
@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Drcons> getUser() {
        return userMapper.getUser();
    }

    @Override
    public List<Drcons> getAbility() {
        return userMapper.getAbility();
    }
}
