package com.xqxy.sys.modular.cust.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.client.SystemClient;
import com.xqxy.core.enums.ApprovalCodeEnum;
import com.xqxy.core.enums.BusTypeEnum;
import com.xqxy.core.enums.CurrenUserInfoExceptionEnum;
import com.xqxy.core.enums.YesOrNotEnum;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.pojo.login.CurrenUserInfo;
import com.xqxy.core.util.OrganizationUtil;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.dr.modular.project.enums.ExamineExceptionEnum;
import com.xqxy.sys.modular.cust.entity.Cust;
import com.xqxy.sys.modular.cust.entity.CustCertifyFile;
import com.xqxy.sys.modular.cust.enums.CustCheckStatusEnum;
import com.xqxy.sys.modular.cust.enums.CustStatusEnum;
import com.xqxy.sys.modular.cust.enums.IsAggregatorEnum;
import com.xqxy.sys.modular.cust.enums.OrgTitleEnum;
import com.xqxy.sys.modular.cust.mapper.CustMapper;
import com.xqxy.sys.modular.cust.param.BusConfigParam;
import com.xqxy.sys.modular.cust.param.CustInfoParam;
import com.xqxy.sys.modular.cust.param.CustParam;
import com.xqxy.sys.modular.cust.result.Result;
import com.xqxy.sys.modular.cust.service.CustCertifyFileService;
import com.xqxy.sys.modular.cust.service.CustService;
import com.xqxy.sys.modular.sms.enums.SmsTemplateTypeEnum;
import com.xqxy.sys.modular.sms.service.SysSmsSendService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统用户service接口实现类
 *
 * @author shi
 * @date 2021/10/8 16:49
 */
@Service
public class CustServiceImpl extends ServiceImpl<CustMapper, Cust> implements CustService {

    @Autowired
    SystemClient systemClient;

    @Resource
    CustService custService;

    @Resource
    CustCertifyFileService custCertifyFileService;

    @Resource
    private SysSmsSendService sysSmsSendService;

    @Value("${fileTypes}")
    private String fileTypes;

    @Override
    public void approve(Long applyOrgId) {
//        String busId=String.valueOf(System.currentTimeMillis());
        CurrenUserInfo currentUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if (ObjectUtil.isNull(currentUserInfo)) {
            throw new ServiceException(CurrenUserInfoExceptionEnum.CURRENT_USER_INFO_NOT_EXIST);
        }
        Long custId = Long.parseLong(currentUserInfo.getId());

        BusConfigParam busConfigParam = new BusConfigParam();
        if(custId != null){
            Cust cust = this.getById(custId);
            if(cust != null){
                busConfigParam.setApplyManName(cust.getApplyName());
            }
        }
//        Long orgId=Long.parseLong(currentUserInfo.getOrgId());
        busConfigParam.setBusId(String.valueOf(custId));
        busConfigParam.setApplyManId(custId);
        busConfigParam.setBusType(BusTypeEnum.ACCOUNT_PROCESS.getCode());
        busConfigParam.setApplyOrgId(String.valueOf(applyOrgId));
        busConfigParam.setLevel(1);
        busConfigParam.setOperaManId(String.valueOf(custId));

        Result result = null;
        try {
            result = systemClient.selectInfo(busConfigParam);
        } catch (RuntimeException e) {
            log.error("审批接口调用失败，详情为{}", e);
        }
        if (ObjectUtil.isNotNull(result) && result.getCode().equals(ApprovalCodeEnum.SUCCESS.getCode())) {
            JSONObject data = result.getData();
            if (data.getString("handleCode").equals(ApprovalCodeEnum.BUILD_SUCCESS.getCode())) {
                Cust cust = custService.getById(custId);
                cust.setOrgNo(String.valueOf(applyOrgId));
                cust.setCheckStatus(CustCheckStatusEnum.APPROVING.getCode());
                this.updateById(cust);
                return;
            }
            // 创建失败，抛出异常
            throw new ServiceException(ExamineExceptionEnum.CREATION_FAILED);
        } else {
            throw new ServiceException(ExamineExceptionEnum.NO_APPROVAL);
        }
    }

    @Override
    public void update(CustInfoParam custInfoParam) {
        Cust cust = custInfoParam.getCust();
        Long custId = cust.getId();
        if (ObjectUtil.isNotNull(cust)) {
            if (ObjectUtil.isNotEmpty(cust.getId())) {
                cust.setState(CustStatusEnum.SAVED.getCode());
                cust.setCheckStatus(CustCheckStatusEnum.SAVED.getCode());
                this.updateById(cust);
            } else {
//                Long startTs = System.currentTimeMillis();
//                cust.setId(startTs);//用当前时间戳作为主键
                cust.setState(CustStatusEnum.SAVED.getCode());
                cust.setCheckStatus(CustCheckStatusEnum.SAVED.getCode());
                this.save(cust);
                custId = cust.getId();
            }
        }
        List<CustCertifyFile> custCertifyFiles = custInfoParam.getCustCertifyFileList();

        // 查询 该custId 和 认证界面 所有的文件类型 对应的 客户签约对象
        LambdaQueryWrapper<CustCertifyFile> fileLQueryWrapper = new LambdaQueryWrapper<>();
        fileLQueryWrapper.eq(CustCertifyFile::getCustId, custId);
        fileLQueryWrapper.in(CustCertifyFile::getFileType, Arrays.asList(fileTypes.split(",")));
        boolean remove = custCertifyFileService.remove(fileLQueryWrapper);

        for (CustCertifyFile custCertifyFile : custCertifyFiles) {
            if (ObjectUtil.isEmpty(custCertifyFile.getCustId())) {
                custCertifyFile.setCustId(custId);
            }
        }
        custCertifyFileService.saveBatch(custCertifyFiles);
    }

    @Override
    public void updateByid(Cust cust) {
        if (ObjectUtil.isNotNull(cust)) {
            if (ObjectUtil.isNotEmpty(cust.getId())) {
//                cust.setState(CustStatusEnum.SAVED.getCode());
//                cust.setCheckStatus(CustCheckStatusEnum.SAVED.getCode());
                this.updateById(cust);
            }
        }
    }

    @Override
    public Boolean modifyInformation(CustInfoParam custInfoParam) {
        Cust cust = custInfoParam.getCust();
        List<CustCertifyFile> custCertifyFileList = custInfoParam.getCustCertifyFileList();
        boolean b = !(this.equalsCust(cust) && this.equalsCustCertifyFile(custCertifyFileList, cust.getId()));
        return b;
//        return !(this.equalsCustCertifyFile(custCertifyFileList));
    }

    @Override
    public List<Long> getByCustNo(String tel) {
        LambdaQueryWrapper<Cust> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotEmpty(tel)) {
            lambdaQueryWrapper.like(Cust::getTel, tel);
        }
        List<Cust> custList = this.list(lambdaQueryWrapper);
        List<Long> custIdList = custList.stream().map(Cust::getId).collect(Collectors.toList());
        return custIdList;
    }

    @Override
    public List<Long> getByCreditCode(String creditCode) {
        LambdaQueryWrapper<Cust> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotEmpty(creditCode)) {
            lambdaQueryWrapper.like(Cust::getCreditCode, creditCode);
        }
        List<Cust> custList = this.list(lambdaQueryWrapper);
        List<Long> custIdList = custList.stream().map(Cust::getId).collect(Collectors.toList());
        return custIdList;
    }

    @Override
    public CustInfoParam detailById() {
        CustInfoParam custInfoParam = new CustInfoParam();
        CurrenUserInfo currentUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        Long custId = Long.parseLong(currentUserInfo.getId());

        Cust cust = this.getById(custId);
        custInfoParam.setCustCertifyFileList(custCertifyFileService.getByCustId(custId));
        custInfoParam.setCust(cust);
        return custInfoParam;
    }

    @Override
    public CustInfoParam detailByCustId(Long id) {
        CustInfoParam custInfoParam = new CustInfoParam();

        Cust cust = this.getById(id);
        custInfoParam.setCustCertifyFileList(custCertifyFileService.getByCustId(id));
        custInfoParam.setCust(cust);
        return custInfoParam;
    }

    /**
     * 比较两个Cust对象值是否相等
     *
     * @return
     */
    private Boolean equalsCust(Cust newCust) {
        Boolean result = null;
        if (ObjectUtil.isNotEmpty(newCust.getId())) {
            Cust oldCust = custService.getById(newCust.getId());
            if (null != newCust.getCreditCode()) {
                result = newCust.getApplyName().equals(oldCust.getApplyName()) &&
                        newCust.getApplyCardType().equals(oldCust.getApplyCardType()) &&
                        newCust.getApplyNo().equals(oldCust.getApplyNo()) &&
                        newCust.getLegalName().equals(oldCust.getLegalName()) &&
                        newCust.getLegalCardType().equals(oldCust.getLegalCardType()) &&
                        newCust.getLegalNo().equals(oldCust.getLegalNo()) &&
                        newCust.getOrgNo().equals(oldCust.getOrgNo()) &&
                        newCust.getCreditCode().equals(oldCust.getCreditCode()) &&
                        (newCust.getIntegrator().equals(oldCust.getIntegrator()));
            } else {
                        result = newCust.getApplyName().equals(oldCust.getApplyName()) &&
                        newCust.getApplyCardType().equals(oldCust.getApplyCardType()) &&
                        newCust.getApplyNo().equals(oldCust.getApplyNo()) &&
                        newCust.getLegalName().equals(oldCust.getLegalName()) &&
                        newCust.getLegalCardType().equals(oldCust.getLegalCardType()) &&
                        newCust.getLegalNo().equals(oldCust.getLegalNo()) &&
                        newCust.getOrgNo().equals(oldCust.getOrgNo()) &&
                        (newCust.getIntegrator().equals(oldCust.getIntegrator()));
            }
        }
        return result;
    }

    /**
     * 比较两个CustCertifyFile是否相等
     *
     * @return
     */
    private Boolean equalsCustCertifyFile(List<CustCertifyFile> custCertifyFileList, Long custId) {
        Boolean base = true;
        LambdaQueryWrapper<CustCertifyFile> custCertifyWrapper = new LambdaQueryWrapper<>();
        custCertifyWrapper.eq(CustCertifyFile::getCustId, custId);
        List<CustCertifyFile> custCertifyFiles = custCertifyFileService.list(custCertifyWrapper);

        for (CustCertifyFile custCertifyFile : custCertifyFiles) {
            List<CustCertifyFile> fileList = custCertifyFileList.stream().filter(n -> n.getCustId().equals(custCertifyFile.getCustId())
                    && n.getFileId().equals(custCertifyFile.getFileId())
                    && n.getFileType().equals(custCertifyFile.getFileType())
                    && n.getFileName().equals(custCertifyFile.getFileName())
            ).collect(Collectors.toList());

            if (CollectionUtils.isEmpty(fileList)) {
                return false;
            }
        }

        for (CustCertifyFile custCertifyFile : custCertifyFileList) {
            List<CustCertifyFile> fileList = custCertifyFiles.stream().filter(n -> n.getCustId().equals(custCertifyFile.getCustId())
                    && n.getFileId().equals(custCertifyFile.getFileId())
                    && n.getFileType().equals(custCertifyFile.getFileType())
                    && n.getFileName().equals(custCertifyFile.getFileName())
            ).collect(Collectors.toList());

            if (CollectionUtils.isEmpty(fileList)) {
                return false;
            }
        }

        return base;
    }

    @Override
    public void add(String tel, Long id) {
        Cust cust = new Cust();
        cust.setTel(tel);
        cust.setId(id);
        this.save(cust);
    }

    @Override
    public void approveResult(BusConfigParam busConfigParam) {
        CurrenUserInfo currentUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if (ObjectUtil.isNull(currentUserInfo)) {
            throw new ServiceException(CurrenUserInfoExceptionEnum.CURRENT_USER_INFO_NOT_EXIST);
        }
        String infoId = currentUserInfo.getId();
        if(infoId != null){
            Long custId = Long.valueOf(infoId);
            if(custId != null){
                Cust cust = custService.getById(custId);
                if(cust != null){
                    busConfigParam.setApplyManName(cust.getApplyName());
                }
            }
        }
        busConfigParam.setOperaManId(infoId);//操作人Id
        busConfigParam.setBusType(BusTypeEnum.ACCOUNT_PROCESS.getCode());//业务类型
        Result result = systemClient.selectInfo(busConfigParam);

        System.out.println(result);
        JSONObject data = result.getData();
        Cust cust = custService.getById(busConfigParam.getApplyManId());
        if (result.getCode().equals(ApprovalCodeEnum.SUCCESS.getCode()))//接口调用成功
        {
            if (data.getString("handleCode").equals(ApprovalCodeEnum.PROCESS_SUCCESS.getCode())) {
                cust.setCheckStatus(CustCheckStatusEnum.AUDIT.getCode());
                cust.setState(CustStatusEnum.APPROVING.getCode());
                cust.setAllowChange(YesOrNotEnum.N.getCode());
                if (ObjectUtil.isNotNull(cust.getId()) && ObjectUtil.isNotNull(cust.getTel())) {
                    sysSmsSendService.generateSms(cust.getId().toString(),
                            SmsTemplateTypeEnum.REGISTER_RESULT_SUCCESS.getCode());
                }
            } else if (data.getString("handleCode").equals(ApprovalCodeEnum.APPROVAL_REJECT.getCode())) {
                cust.setCheckStatus(CustCheckStatusEnum.FAILAUDIT.getCode());
                cust.setState(CustStatusEnum.FAILAUDIT.getCode());
                if (ObjectUtil.isNotNull(cust.getId()) && ObjectUtil.isNotNull(cust.getTel())) {
                    sysSmsSendService.generateSms(cust.getId().toString(),
                            SmsTemplateTypeEnum.REGISTER_RESULT_FAILED.getCode());
                }
            } else if (data.getString("handleCode").equals(ApprovalCodeEnum.PROCESS_FAIL.getCode())) {
                throw new ServiceException(ExamineExceptionEnum.APPROVAL_FAIL);
            }
            this.updateById(cust);
        } else {
            throw new ServiceException(ExamineExceptionEnum.APPROVAL_FAIL);
        }
    }


    @Override
    public CustInfoParam getApproveInfo(Long custId) {
        CustInfoParam custInfoParam = new CustInfoParam();

        Cust cust = this.getById(custId);
        custInfoParam.setCustCertifyFileList(custCertifyFileService.getByCustId(custId));
        custInfoParam.setCust(cust);
        return custInfoParam;
    }

    @Override
    public Cust getCustById(Long id) {
        return this.getById(id);
    }

    @Override
    public List<Long> getAggreListByConsId(List<String> consIdList) {
        return getBaseMapper().getAggreListByConsId(consIdList);
    }

    @Override
    public Page<Cust> page(CustParam custParam) {
        if (null == custParam.getIntegrator()) {
            return new Page<>();
        }
        CurrenUserInfo currenUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        String orgNo = currenUserInfo.getOrgId();
        String orgTitle = currenUserInfo.getOrgTitle();
        //机构子集
        List<String> list = new ArrayList<>();
        List<String> list1 = null;
        List<String> list2 = null;
        if (null != orgNo && !"".equals(orgNo)) {
            if (!"1".equals(orgTitle)) {
                //集成商市级不能查看
                if ("1".equals(custParam.getIntegrator())) {
                    return new Page<>();
                }
                //获取用户权限内所有组织机构集合
                list1 = OrganizationUtil.getAllOrgByOrgNo();
                if (null == list1 || list1.size() == 0) {
                    return new Page<>();
                }
                //根据参数查询其所有子集
                if (null != custParam.getOrgNo()) {
                    list2 = OrganizationUtil.getAllOrgByOrgNoPamarm(custParam.getOrgNo());
                    //筛选用户机构集合中包含参数的机构
                    if (null != list1 && null != list2) {
                        for (String s : list2) {
                            if (list1.contains(s)) {
                                list.add(s);
                            }
                        }
                    }
                    if (CollectionUtil.isEmpty(list)) {
                        return new Page<>();
                    }
                } else {
                    list = list1;
                }
                custParam.setOrgs(list);
            } else {
                if (null != custParam.getOrgNo()) {
                    list = OrganizationUtil.getAllOrgByOrgNoPamarm(custParam.getOrgNo());
                }
                custParam.setOrgs(list);
            }
        } else {
            return new Page<>();
        }
        LambdaQueryWrapper<Cust> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(custParam)) {
            // 根据省
            if (ObjectUtil.isNotEmpty(custParam.getProvinceCode())) {
                lambdaQueryWrapper.eq(Cust::getProvinceCode, custParam.getProvinceCode());
            }
            // 根据市
            if (ObjectUtil.isNotEmpty(custParam.getCityCode())) {
                lambdaQueryWrapper.eq(Cust::getCityCode, custParam.getCityCode());
            }
            // 根据区
            if (ObjectUtil.isNotEmpty(custParam.getCountyCode())) {
                lambdaQueryWrapper.eq(Cust::getCountyCode, custParam.getCountyCode());
            }
            // 根据名称
            if (ObjectUtil.isNotEmpty(custParam.getApplyName())) {
                lambdaQueryWrapper.like(Cust::getApplyName, custParam.getApplyName());
            }
            if (ObjectUtil.isNotEmpty(custParam.getIntegrator())) {
                lambdaQueryWrapper.eq(Cust::getIntegrator, custParam.getIntegrator());
            }// 根据联系方式
            if (ObjectUtil.isNotEmpty(custParam.getTel())) {
                lambdaQueryWrapper.like(Cust::getTel, custParam.getTel());
            }
            if (ObjectUtil.isNotEmpty(custParam.getCreditCode())) {
                lambdaQueryWrapper.like(Cust::getCreditCode, custParam.getCreditCode());
            }
            if (CollectionUtil.isNotEmpty(custParam.getOrgs())) {
                lambdaQueryWrapper.in(Cust::getOrgNo, custParam.getOrgs());
            }
        }
        lambdaQueryWrapper.orderByDesc(Cust::getCreateTime);
        Page<Cust> objectPage = new Page<>();
        objectPage.setCurrent(custParam.getCurrent());
        objectPage.setSize(custParam.getSize());
        Page<Cust> page = this.page(objectPage, lambdaQueryWrapper);
        return page;
    }

    @Override
    public List<Cust> list(CustParam custParam) {
        LambdaQueryWrapper<Cust> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(custParam)) {
            // 根据省
            if (ObjectUtil.isNotEmpty(custParam.getProvinceCode())) {
                lambdaQueryWrapper.eq(Cust::getProvinceCode, custParam.getProvinceCode());
            }
            // 根据市
            if (ObjectUtil.isNotEmpty(custParam.getCityCode())) {
                lambdaQueryWrapper.eq(Cust::getCityCode, custParam.getCityCode());
            }
            // 根据区
            if (ObjectUtil.isNotEmpty(custParam.getCountyCode())) {
                lambdaQueryWrapper.eq(Cust::getCountyCode, custParam.getCountyCode());
            }
            // 根据名称
            if (ObjectUtil.isNotEmpty(custParam.getApplyName())) {
                lambdaQueryWrapper.like(Cust::getApplyName, custParam.getApplyName());
            }
            if (ObjectUtil.isNotEmpty(custParam.getIntegrator())) {
                lambdaQueryWrapper.eq(Cust::getIntegrator, custParam.getIntegrator());
            }// 根据联系方式
            if (ObjectUtil.isNotEmpty(custParam.getCustName())) {
                lambdaQueryWrapper.like(Cust::getCustName, custParam.getCustName());
            }// 根据
            if (ObjectUtil.isNotEmpty(custParam.getTel())) {
                lambdaQueryWrapper.like(Cust::getTel, custParam.getTel());
            }
            if (ObjectUtil.isNotEmpty(custParam.getCreditCode())) {
                lambdaQueryWrapper.eq(Cust::getCreditCode, custParam.getCreditCode());
            }// 根据统一社会信用代码

            lambdaQueryWrapper.orderByDesc(Cust::getCreateTime);
        }
        return this.list(lambdaQueryWrapper);
    }

    @Override
    public Cust getUserByCreditCode(String creditCode) {
        Cust cust = new Cust();
        LambdaQueryWrapper<Cust> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotEmpty(creditCode)) {
            queryWrapper.eq(Cust::getCreditCode, creditCode).or().eq(Cust::getTel,creditCode);
        }
        List<Cust> list = this.list(queryWrapper);
        if (list.size() > 0) {
            cust = list.get(0);
            return cust;
        }
        return cust;
    }

    @Override
    public Cust selectAggregator() {
        String custId = Objects.requireNonNull(SecurityUtils.getCurrentUserInfoUTF8()).getId();
        return getBaseMapper().getAggregatorByCust(custId);
    }

    @Override
    public void addCust(Cust cust) {
        if (ObjectUtil.isNotNull(cust) && ObjectUtil.isNotEmpty(cust)) {
            Cust copyCust = new Cust();
            BeanUtils.copyProperties(cust, copyCust);
            this.save(copyCust);
        }
    }

    @Override
    public void updateCust(CustInfoParam custInfoParam) {
        Cust cust = custInfoParam.getCust();
        Long custId = cust.getId();
        String state = null;

        if (CustCheckStatusEnum.SAVED.getCode().equals(cust.getCheckStatus()) || CustCheckStatusEnum.APPROVING.getCode().equals(cust.getCheckStatus())) {
            state = CustStatusEnum.SAVED.getCode();
        }
        if (CustCheckStatusEnum.AUDIT.getCode().equals(cust.getCheckStatus())) {
            state = CustStatusEnum.APPROVING.getCode();
        }
        if (CustCheckStatusEnum.FAILAUDIT.getCode().equals(cust.getCheckStatus())) {
            state = CustStatusEnum.FAILAUDIT.getCode();
        }

        if (ObjectUtil.isNotNull(cust)) {
            if (ObjectUtil.isNotEmpty(cust.getId())) {
                cust.setState(state);
                cust.setCheckStatus(cust.getCheckStatus());
                this.updateById(cust);
            } else {
                cust.setState(state);
                cust.setCheckStatus(cust.getCheckStatus());
                this.save(cust);
                custId = cust.getId();
            }
        }
        List<CustCertifyFile> custCertifyFiles = custInfoParam.getCustCertifyFileList();
        for (CustCertifyFile custCertifyFile : custCertifyFiles) {
            if (custCertifyFiles.size() != 7) {
                custCertifyFileService.deleteBy(custId, "1");

            }
            if (ObjectUtil.isEmpty(custCertifyFile.getCustId())) {
                custCertifyFile.setCustId(custId);
            }
            if (ObjectUtil.isNotEmpty(custCertifyFile.getId()) && ObjectUtil.isNotNull(custCertifyFile.getId())) {
                custCertifyFileService.updateById(custCertifyFile);
            } else {
//                Long startTs = System.currentTimeMillis();
//                custCertifyFile.setId(startTs);//用当前时间戳作为主键
                custCertifyFileService.save(custCertifyFile);
            }
        }
    }

    @Override
    public Cust getCustByConsId(String consId) {
        Cust cust = null;
        List<Cust> custs = baseMapper.getCustByConsId(consId);
        if(null!=custs && custs.size()>0) {
            cust = custs.get(0);
        }
        return cust;
    }


}
