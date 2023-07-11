package com.xqxy.dr.modular.upload.service.impl;


import com.xqxy.dr.modular.upload.entity.Event;
import com.xqxy.dr.modular.upload.mapper.EvluateMapper;
import com.xqxy.dr.modular.upload.service.EvluateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 执行评价 服务实现类
 * </p>
 *
 */

@Service
public class EvluateServiceImpl implements EvluateService {



    @Autowired
    private EvluateMapper evluateMapper;
    @Override
    public List<Event> getEvluate() {
        return evluateMapper.getEvluate();
    }
}
