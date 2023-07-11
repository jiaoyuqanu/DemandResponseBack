package com.xqxy.dr.modular.newloadmanagement.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.pojo.login.CurrenUserInfo;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.dr.modular.newloadmanagement.entity.Drcust;
import com.xqxy.dr.modular.newloadmanagement.mapper.DrCustMapper;
import com.xqxy.dr.modular.newloadmanagement.param.MobileNumberParam;
import com.xqxy.dr.modular.newloadmanagement.service.MobileNumberChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MobileNumberChangeImpl extends ServiceImpl<DrCustMapper, Drcust> implements MobileNumberChange {

    public static final String TEL_CODE_KEY = "tel_ver_code:";

    @Autowired
    private DrCustMapper drCustMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    public int changeNumber(MobileNumberParam mobileNumberParam) {
        String verCode = mobileNumberParam.getVerCode();
        String o = redisTemplate.opsForValue().get(TEL_CODE_KEY + mobileNumberParam.getTel());
        if(ObjectUtil.isEmpty(o)){
            return -1;
        }
        if (!o.equals(verCode)) {
            return -1;
        }
        CurrenUserInfo currenUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        String id = currenUserInfo.getId();

        List telList = drCustMapper.queryAllTelAtDrCust();
        if(telList.contains(mobileNumberParam.getTel())){
            return -2;
        }else {
            drCustMapper.changeMobile(mobileNumberParam.getTel(), id);
        }

        List mobileList = drCustMapper.queryAllTelAtSysUsers();

        if(mobileList.contains(mobileNumberParam.getTel())){
            return -2;
        }else {
            drCustMapper.changeAdminMobile(mobileNumberParam.getTel(), id);
        }
        return 1;
    }
}