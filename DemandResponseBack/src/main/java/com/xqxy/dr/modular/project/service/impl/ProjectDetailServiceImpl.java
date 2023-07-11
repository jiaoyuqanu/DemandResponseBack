package com.xqxy.dr.modular.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.client.SystemLoadbusiClient;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.exception.SystemErrorType;
import com.xqxy.dr.modular.project.entity.CustContractFile;
import com.xqxy.dr.modular.project.entity.CustContractInfo;
import com.xqxy.dr.modular.project.entity.ProjectDetail;
import com.xqxy.dr.modular.project.entity.SpareContractDevice;
import com.xqxy.dr.modular.project.enums.FileTypeEnum;
import com.xqxy.dr.modular.project.mapper.ConsContractInfoMapper;
import com.xqxy.dr.modular.project.mapper.ProjectDetailMapper;
import com.xqxy.dr.modular.project.params.ConsContractParam;
import com.xqxy.dr.modular.project.params.CustContractParam;
import com.xqxy.dr.modular.project.params.LoadbusiConsParam;
import com.xqxy.dr.modular.project.service.*;
import com.xqxy.sys.modular.cust.entity.CustCertifyFile;
import com.xqxy.sys.modular.cust.service.CustCertifyFileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 项目明细 服务实现类
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-11
 */
@Service
public class ProjectDetailServiceImpl extends ServiceImpl<ProjectDetailMapper, ProjectDetail> implements ProjectDetailService {

    @Resource
    private ProjectDetailService projectDetailService;

    @Resource
    private CustContractInfoService custContractInfoService;

    @Resource
    private CustContractFileService custContractFileService;

    @Resource
    private SpareContractDeviceService spareContractDeviceService;

    @Resource
    private SystemLoadbusiClient systemLoadbusiClient;

    @Override
    public void deleteByProjectId(Long projectId) {
        LambdaQueryWrapper<ProjectDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProjectDetail::getProjectId,projectId);
        this.remove(queryWrapper);
    }

    @Override
    public List<ProjectDetail> listByProjectId(Long projectId) {
        LambdaQueryWrapper<ProjectDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProjectDetail::getProjectId,projectId);
        queryWrapper.orderByAsc(ProjectDetail::getTimeType);
        queryWrapper.orderByAsc(ProjectDetail::getResponseType);
        queryWrapper.orderByAsc(ProjectDetail::getAdvanceNoticeTime);
        return this.list(queryWrapper);
    }

    @Override
    public Integer getPeriodNum(Long projectId) {
        LambdaQueryWrapper<ProjectDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProjectDetail::getProjectId,projectId);
        return this.count(queryWrapper);
    }

    @Override
    public String getAdvanceNotice(Long projectId) {
        LambdaQueryWrapper<ProjectDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProjectDetail::getProjectId, projectId);
        queryWrapper.select(ProjectDetail::getAdvanceNoticeTime);
        List<ProjectDetail> list = this.list(queryWrapper);
        list.removeAll(Collections.singleton(null));
        List<Integer> advanceTimeList = new ArrayList<>();
        String videoIds = "";
        if(null!=advanceTimeList && advanceTimeList.size()>0) {
            // advanceTimeList转换为逗号分隔的方式
            advanceTimeList = list.stream().map(ProjectDetail::getAdvanceNoticeTime).collect(Collectors.toList());
            videoIds = StringUtils.join(advanceTimeList.toArray(), ",");
        }
        return videoIds;
    }

    @Override
    public Map<String, Object> consDeclareDetail(CustContractParam custContractParam) {
        QueryWrapper<Object> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dcci.contract_id", custContractParam.getContractId());
        Map<String, Object> map = getBaseMapper().consDeclareDetail(queryWrapper);

        // 查询 签约
        LambdaQueryWrapper<CustContractInfo> custContractInfoWrapper = new LambdaQueryWrapper<>();
        custContractInfoWrapper.eq(CustContractInfo::getContractId,custContractParam.getContractId());
        List<CustContractInfo> custContractInfos = custContractInfoService.list(custContractInfoWrapper);

        if(!CollectionUtils.isEmpty(custContractInfos)){
            Long custId = custContractInfos.get(0).getCustId();

            LambdaQueryWrapper<CustContractFile> custFileWrapper = new LambdaQueryWrapper<>();
            custFileWrapper.in(CustContractFile::getFileType, FileTypeEnum.CUST_CONTRACT_AGREEMENT.getCode(),FileTypeEnum.CUST_PROMISE_BOOK.getCode());
            custFileWrapper.eq(CustContractFile::getCustId,custId);
            custFileWrapper.eq(CustContractFile::getProjectId,custContractInfos.get(0).getProjectId());
            //查询 文件
            List<CustContractFile> custContractFiles = custContractFileService.list(custFileWrapper);
            map.put("custContractFiles",custContractFiles);
        }

        return map;
    }


    @Override
    public List<ProjectDetail> listContractDetail(ConsContractParam consContractParam) {
        List<ProjectDetail> projectDetails;
        if (StringUtils.isEmpty(consContractParam.getContractId())) {
            projectDetails = projectDetailService.listByProjectId(consContractParam.getProjectId());
        } else {
            projectDetails = getBaseMapper().listContractInfo(consContractParam.getContractId());
        }
        //从 负控取 新型负荷  20220621 更改需求  取50%合同容量   因此注释
//        LoadbusiConsParam loadbusiConsParam = new LoadbusiConsParam();
//        loadbusiConsParam.setConsNo(consContractParam.getConsId());
//        try {
//            JSONObject jsonObject = systemLoadbusiClient.getConsInfoByConsId(loadbusiConsParam);
//            if(jsonObject != null){
//                if(jsonObject.get("code") != null){
//                    JSONObject data = jsonObject.getJSONObject("data");
//                    if(data != null){
//                        Object o = data.get("ratingLoad");
//                        if(o != null){
//                            BigDecimal bigDecimal = new BigDecimal(o.toString());
//                            for (ProjectDetail projectDetail : projectDetails) {
//                                projectDetail.setControlCap(bigDecimal);
//                            }
//                        }
//                    }else {
//                        for (ProjectDetail projectDetail : projectDetails) {
//                            projectDetail.setControlCap(BigDecimal.ZERO);
//                        }
//                    }
//                }
//            }
//        }catch (Exception e){
//            //访问有异常 塞0
//            log.error("fegin调用负控接口异常 接口路径：/devBranch/getConsInfoByConsId");
//            log.error("传参为："+loadbusiConsParam.toString());
//            for (ProjectDetail projectDetail : projectDetails) {
//                projectDetail.setControlCap(BigDecimal.ZERO);
//            }
//        }


        return projectDetails;
    }
}
