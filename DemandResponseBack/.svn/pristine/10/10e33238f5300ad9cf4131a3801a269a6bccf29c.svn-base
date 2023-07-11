package com.xqxy.dr.modular.bidding.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.client.SystemClient;
import com.xqxy.core.enums.ApprovalCodeEnum;
import com.xqxy.core.enums.BusTypeEnum;
import com.xqxy.core.enums.CurrenUserInfoExceptionEnum;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.pojo.login.CurrenUserInfo;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.dr.modular.bidding.entity.BiddingDetail;
import com.xqxy.dr.modular.bidding.entity.BiddingNotice;
import com.xqxy.dr.modular.bidding.entity.BiddingRange;
import com.xqxy.dr.modular.bidding.enums.BiddingCheckStatusEnums;
import com.xqxy.dr.modular.bidding.enums.BiddingStateEnums;
import com.xqxy.dr.modular.bidding.mapper.BiddingNoticeMapper;
import com.xqxy.dr.modular.bidding.param.BiddingNoticeParam;
import com.xqxy.dr.modular.bidding.result.BiddingNoticeInfo;
import com.xqxy.dr.modular.bidding.service.BiddingDetailService;
import com.xqxy.dr.modular.bidding.service.BiddingNoticeService;
import com.xqxy.dr.modular.bidding.service.BiddingRangeService;
import com.xqxy.dr.modular.project.enums.ExamineExceptionEnum;
import com.xqxy.dr.modular.project.enums.ProjectCheckStatusEnums;
import com.xqxy.dr.modular.project.params.ExamineParam;
import com.xqxy.sys.modular.cust.entity.Cust;
import com.xqxy.sys.modular.cust.param.BusConfigParam;
import com.xqxy.sys.modular.cust.result.Result;
import com.xqxy.sys.modular.cust.service.CustService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 竞价公告 服务实现类
 * </p>
 *
 * @author Shen
 * @since 2021-10-15
 */
@Service
public class BiddingNoticeServiceImpl extends ServiceImpl<BiddingNoticeMapper, BiddingNotice> implements BiddingNoticeService {

    @Resource
    BiddingDetailService biddingDetailService;

    @Resource
    BiddingRangeService biddingRangeService;

    @Resource
    private SystemClient systemClient;

    @Autowired
    private CustService custService;

    @Override
    public Page<BiddingNotice> page(BiddingNoticeParam biddingNoticeParam) {
        LambdaQueryWrapper<BiddingNotice> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(biddingNoticeParam)) {
            // 根据竞价公告编号模糊查询
            if (ObjectUtil.isNotEmpty(biddingNoticeParam.getNoticeNo())) {
                queryWrapper.like(BiddingNotice::getNoticeNo, biddingNoticeParam.getNoticeNo());
            }

            // 根据竞价公告名称模糊查询
            if (ObjectUtil.isNotEmpty(biddingNoticeParam.getNoticeName())) {
                queryWrapper.like(BiddingNotice::getNoticeName, biddingNoticeParam.getNoticeName());
            }

            // 根据公告状态
            if (ObjectUtil.isNotEmpty(biddingNoticeParam.getState())) {
                queryWrapper.eq(BiddingNotice::getState, biddingNoticeParam.getState());
            }
            // 根据审核状态
            if (ObjectUtil.isNotEmpty(biddingNoticeParam.getCheckStatus())) {
                queryWrapper.eq(BiddingNotice::getCheckStatus, biddingNoticeParam.getCheckStatus());
            }
        }
        //根据竞价生效时间倒序排序
        queryWrapper.orderByDesc(BiddingNotice::getCreateTime);
        return this.page(biddingNoticeParam.getPage(), queryWrapper);
    }

    @Override
    public BiddingNoticeInfo detail(BiddingNoticeParam biddingNoticeParam) {
        BiddingNoticeInfo biddingNoticeInfo = new BiddingNoticeInfo();
        BiddingNotice biddingNotice = this.getById(biddingNoticeParam.getNoticeId());
        BeanUtils.copyProperties(biddingNotice,biddingNoticeInfo);
        List<BiddingDetail> biddingDetailList = biddingDetailService.list(biddingNoticeParam.getNoticeId());
        biddingNoticeInfo.setBiddingDetailList(biddingDetailList);
        List<BiddingRange> biddingRangeList = biddingRangeService.list(biddingNoticeParam.getNoticeId());
        biddingNoticeInfo.setBiddingRangeList(biddingRangeList);
        return biddingNoticeInfo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Long noticeId) {
        // 删除发布范围
        biddingRangeService.delete(noticeId);
        // 删除竞价明细
        biddingDetailService.delete(noticeId);
        // 删除竞价信息
        this.removeById(noticeId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(BiddingNoticeInfo biddingNoticeInfo) {
        // 竞价基本信息
        BiddingNotice biddingNotice = new BiddingNotice();
        BeanUtils.copyProperties(biddingNoticeInfo,biddingNotice);
        biddingNotice.setState(BiddingStateEnums.ISSUED.getCode());
        biddingNotice.setCheckStatus(BiddingCheckStatusEnums.UNSUBMITTED.getCode());
        this.save(biddingNotice);
        Long noticeId = biddingNotice.getNoticeId();
        // 竞价发布范围
        List<BiddingRange> biddingRangeList = biddingNoticeInfo.getBiddingRangeList();
        for (int i = 0; i < biddingRangeList.size(); i++) {
            biddingRangeList.get(i).setNoticeId(noticeId);
        }
        biddingRangeService.addBatch(biddingRangeList);
        // 竞价明细
        List<BiddingDetail> biddingDetailList = biddingNoticeInfo.getBiddingDetailList();
        for (int i = 0; i < biddingDetailList.size(); i++) {
            biddingDetailList.get(i).setNoticeId(noticeId);
        }
        biddingDetailService.addBatch(biddingDetailList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(BiddingNoticeInfo biddingNoticeInfo) {
        // 竞价基本信息
        BiddingNotice biddingNotice = new BiddingNotice();
        BeanUtils.copyProperties(biddingNoticeInfo,biddingNotice);
        this.updateById(biddingNotice);
        Long noticeId = biddingNotice.getNoticeId();
        // 竞价发布范围
        biddingRangeService.delete(noticeId);
        List<BiddingRange> biddingRangeList = biddingNoticeInfo.getBiddingRangeList();
        for (int i = 0; i < biddingRangeList.size(); i++) {
            biddingRangeList.get(i).setNoticeId(noticeId);
        }
        biddingRangeService.addBatch(biddingRangeList);
        // 竞价明细
        biddingDetailService.delete(noticeId);
        List<BiddingDetail> biddingDetailList = biddingNoticeInfo.getBiddingDetailList();
        for (int i = 0; i < biddingDetailList.size(); i++) {
            biddingDetailList.get(i).setNoticeId(noticeId);
        }
        biddingDetailService.addBatch(biddingDetailList);
    }

    @Override
    public void editStatus(BiddingNoticeParam biddingNoticeParam) {
        BiddingNotice biddingNotice = new BiddingNotice();
        BeanUtils.copyProperties(biddingNoticeParam,biddingNotice);
        this.updateById(biddingNotice);
    }

    @Override
    public void submitCheck(BiddingNoticeParam biddingNoticeParam) {
        BusConfigParam busConfigParam = new BusConfigParam();
        // 暂时不行
        // CurrenUserInfo sysLoginUser = LoginContextHolder.me().getSysLoginUser();
        CurrenUserInfo currentUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if(ObjectUtil.isNull(currentUserInfo)) {
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
        busConfigParam.setBusId(String.valueOf(biddingNoticeParam.getNoticeId()));
        // 申请人组织机构id
        busConfigParam.setApplyOrgId(currentUserInfo.getOrgId());
        // 业务类型
        busConfigParam.setBusType(BusTypeEnum.BIDDING_PROCESS.getCode());
        // 申请人id
        busConfigParam.setApplyManId(Long.parseLong(currentUserInfo.getId()));
        busConfigParam.setLevel(1);
        busConfigParam.setOperaManId(currentUserInfo.getId());

        Result result;
        try {
            result = systemClient.selectInfo(busConfigParam);
        }catch (Exception e){
            log.error("未查到配置的审核流程");
            throw new ServiceException(500,"未查到配置的审核流程");
        }

        if(ObjectUtil.isNotNull(result) && result.getCode().equals("000000") && result.getData().getString("handleCode").equals("000")) {
            // 创建待办任务成功，否则为失败
            // 修改竞价的状态为提交审核
            BiddingNotice biddingNotice = new BiddingNotice();
            biddingNotice.setNoticeId(biddingNoticeParam.getNoticeId());
            biddingNotice.setCheckStatus(BiddingCheckStatusEnums.UNDER_REVIEW.getCode());
            this.updateById(biddingNotice);
            return ;
        }
        // 创建失败，抛出异常
        throw new ServiceException(ExamineExceptionEnum.CREATION_FAILED);
    }

    @Override
    public void examine(ExamineParam examineParam) {
        BusConfigParam busConfigParam = new BusConfigParam();
        BeanUtil.copyProperties(examineParam,busConfigParam);
        // 暂时不行
        // CurrenUserInfo sysLoginUser = LoginContextHolder.me().getSysLoginUser();
        CurrenUserInfo currentUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if(ObjectUtil.isNull(currentUserInfo)) {
            throw new ServiceException(CurrenUserInfoExceptionEnum.CURRENT_USER_INFO_NOT_EXIST);
        }

        busConfigParam.setOperaManId(currentUserInfo.getId()); // 操作人id

        Result result = systemClient.selectInfo(busConfigParam);

        if(ObjectUtil.isNotNull(result) && result.getCode().equals("000000")) {
            if(result.getData().getString("handleCode").equals(ApprovalCodeEnum.PROCESS_FAIL.getCode())) {
                // 审核流程失败
                throw new ServiceException(ExamineExceptionEnum.APPROVAL_FAIL);
            }
            // 修改项目的状态
            BiddingNotice biddingNotice = new BiddingNotice();
            biddingNotice.setNoticeId(Long.parseLong(examineParam.getBusId()));

            // 审核结束
            if(result.getData().getString("handleCode").equals(ApprovalCodeEnum.PROCESS_SUCCESS.getCode())) {
                biddingNotice.setCheckStatus(ProjectCheckStatusEnums.PASS_THE_AUDIT.getCode());
            }
            // 申请被驳回
            if(result.getData().getString("handleCode").equals(ApprovalCodeEnum.APPROVAL_REJECT.getCode())) {
                biddingNotice.setCheckStatus(ProjectCheckStatusEnums.AUDIT_FAILED.getCode());
            }
            this.updateById(biddingNotice);
            return ;
        }
        // 接口状态码不为000000，抛出异常
        throw new ServiceException(ExamineExceptionEnum.CREATION_FAILED);
    }
}
