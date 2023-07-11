package com.xqxy.dr.modular.anhui.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.dr.modular.anhui.entity.HefeiCons;
import com.xqxy.dr.modular.anhui.enums.CustStatusEnum;
import com.xqxy.dr.modular.anhui.mapper.HefeiConsMapper;
import com.xqxy.dr.modular.anhui.service.HefeiConsService;
import com.xqxy.sys.modular.cust.entity.Cons;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 合肥营销档案 服务实现类
 * </p>
 *
 * @author liuyu
 * @since 2021-12-01
 */
@Service
public class HefeiConsServiceImpl extends ServiceImpl<HefeiConsMapper, HefeiCons> implements HefeiConsService {

    @Resource
    private HefeiConsMapper hefeiConsDao;

    @Override
    public HefeiCons queryCons(String elecNo, String consName, String orgNo) {
        LambdaUpdateWrapper<HefeiCons> queryWrapper=new LambdaUpdateWrapper<>();
        if(ObjectUtil.isNotNull(elecNo)){
            queryWrapper.eq(HefeiCons::getConsNo,elecNo);
        }
       if(ObjectUtil.isNotNull(consName)){
           queryWrapper.eq(HefeiCons::getCustName,consName);
       }
        if(ObjectUtil.isNotNull(orgNo)){
            queryWrapper.eq(HefeiCons::getOrgNo,orgNo);
        }
        //是否过滤掉停用，注销状态的用户？
        queryWrapper.eq(HefeiCons::getCustStatus, CustStatusEnum.CUST_STATUS_NORMAL.getCode());
        return  this.getOne(queryWrapper);
    }

    @Override
    public List<String> consRes() {
        List<String> res=new ArrayList<>();
        List<Cons> list = hefeiConsDao.queryConsNo();
        if(list!=null && list.size()>0){
            list.stream().forEach((s)->{
                res.add(s.getId());
            });
        }
        return res;
    }
}
