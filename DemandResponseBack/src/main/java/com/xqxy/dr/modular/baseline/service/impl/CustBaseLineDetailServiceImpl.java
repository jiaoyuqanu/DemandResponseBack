package com.xqxy.dr.modular.baseline.service.impl;

import cn.hutool.log.Log;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.dr.modular.baseline.entity.BaseLineDetail;
import com.xqxy.dr.modular.baseline.entity.BaseLineDetailData;
import com.xqxy.dr.modular.baseline.entity.CustBaseLineDetail;
import com.xqxy.dr.modular.baseline.mapper.BaseLineDetailMapper;
import com.xqxy.dr.modular.baseline.mapper.CustBaseLineMapper;
import com.xqxy.dr.modular.baseline.param.BaseLineDetailParam;
import com.xqxy.dr.modular.baseline.service.BaseLineDetailService;
import com.xqxy.dr.modular.baseline.service.CustBaseLineDetailService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 基线详情 服务实现类
 * </p>
 *
 * @author chen zhi jun
 * @since 2021-10-19
 */
@Service
public class CustBaseLineDetailServiceImpl extends ServiceImpl<CustBaseLineMapper, CustBaseLineDetail> implements CustBaseLineDetailService {


}
