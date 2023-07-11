package com.xqxy.sys.modular.cust.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.sys.modular.cust.entity.CustCertifyFile;
import com.xqxy.sys.modular.cust.mapper.CustCertifyFileMapper;
import com.xqxy.sys.modular.cust.service.CustCertifyFileService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustCertifyFileServiceImpl extends ServiceImpl<CustCertifyFileMapper, CustCertifyFile> implements CustCertifyFileService {

    @Override
    public List<CustCertifyFile> getByCustId(Long CustId)
    {
        LambdaQueryWrapper<CustCertifyFile> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        if(ObjectUtil.isNotNull(CustId)&&ObjectUtil.isNotEmpty(CustId))
        {
            lambdaQueryWrapper.eq(CustCertifyFile::getCustId,CustId);
        }
        return  this.list(lambdaQueryWrapper);

    }
    @Override
    public void deleteBy(Long custId,String fileType)
    {
        LambdaQueryWrapper<CustCertifyFile> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        if(ObjectUtil.isNotEmpty(custId))
        {
            lambdaQueryWrapper.eq(CustCertifyFile::getCustId,custId);
        }
        if(ObjectUtil.isNotEmpty(fileType))
        {
            lambdaQueryWrapper.eq(CustCertifyFile::getFileType,fileType);
        }
        this.remove(lambdaQueryWrapper);
    }
}
