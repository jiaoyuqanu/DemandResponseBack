package com.xqxy.dr.modular.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.Log;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.annotion.NeedSetValueField;
import com.xqxy.core.client.SystemClient;
import com.xqxy.core.client.SystemClientService;
import com.xqxy.core.enums.*;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.exception.SystemErrorType;
import com.xqxy.core.pojo.login.CurrenUserInfo;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.core.util.OrganizationUtil;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.core.util.SpringUtil;
import com.xqxy.dr.modular.baseline.entity.BaseLine;
import com.xqxy.dr.modular.baseline.entity.BaseLineDetail;
import com.xqxy.dr.modular.baseline.entity.PlanBaseLine;
import com.xqxy.dr.modular.baseline.mapper.BaseLineDetailMapper;
import com.xqxy.dr.modular.baseline.util.CurveUtil;
import com.xqxy.dr.modular.data.dto.WorkProjectInfoDTO;
import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.data.enums.ConsBaseLineExceptionEnum;
import com.xqxy.dr.modular.data.param.WorkProjectParam;
import com.xqxy.dr.modular.data.result.GroupContractByTimeTypeResult;
import com.xqxy.dr.modular.data.result.WorkProjectInfoResult;
import com.xqxy.dr.modular.device.entity.SysOrgs;
import com.xqxy.dr.modular.event.entity.*;
import com.xqxy.dr.modular.event.enums.CheckStatusEnum;
import com.xqxy.dr.modular.event.mapper.BaselineLibraryMapper;
import com.xqxy.dr.modular.event.mapper.ConsInvitationMapper;
import com.xqxy.dr.modular.event.mapper.EventMapper;
import com.xqxy.dr.modular.event.param.ConsBaselineAllParam;
import com.xqxy.dr.modular.event.param.EventParam;
import com.xqxy.dr.modular.event.service.EventService;
import com.xqxy.dr.modular.project.VO.DrConsContractDetailsVO;
import com.xqxy.dr.modular.project.VO.DrOrgGoalVO;
import com.xqxy.dr.modular.project.entity.*;
import com.xqxy.dr.modular.project.enums.ExamineExceptionEnum;
import com.xqxy.dr.modular.project.enums.ProjectCheckStatusEnums;
import com.xqxy.dr.modular.project.enums.ProjectExceptionEnum;
import com.xqxy.dr.modular.project.enums.ProjectStatusEnums;
import com.xqxy.dr.modular.project.mapper.ConsContractDetailMapper;
import com.xqxy.dr.modular.project.mapper.ConsContractInfoMapper;
import com.xqxy.dr.modular.project.mapper.CustContractInfoMapper;
import com.xqxy.dr.modular.project.mapper.ProjectMapper;
import com.xqxy.dr.modular.project.params.DrOrgGoalParam;
import com.xqxy.dr.modular.project.params.ExamineParam;
import com.xqxy.dr.modular.project.params.ProjectDetailParam;
import com.xqxy.dr.modular.project.params.ProjectParam;
import com.xqxy.dr.modular.project.result.ProjectInfo;
import com.xqxy.dr.modular.project.service.*;
import com.xqxy.dr.modular.subsidy.mapper.ConsSubsidyMapper;
import com.xqxy.dr.modular.subsidy.mapper.CustSubsidyMapper;
import com.xqxy.sys.modular.cust.entity.Cons;
import com.xqxy.sys.modular.cust.entity.Cust;
import com.xqxy.sys.modular.cust.enums.OrgTitleEnum;
import com.xqxy.sys.modular.cust.param.BusConfigParam;
import com.xqxy.sys.modular.cust.result.Result;
import com.xqxy.sys.modular.cust.service.ConsService;
import com.xqxy.sys.modular.cust.service.CustService;
import com.xqxy.sys.modular.dict.entity.DictData;
import com.xqxy.sys.modular.dict.service.DictTypeService;
import com.xqxy.sys.modular.utils.JSONObjectToEntityUtils;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.log.XxlJobLogger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

/**
 * <p>
 * 需求响应项目 服务实现类
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-11
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {
    private static final Log log = Log.get();

    @Resource
    private ProjectDetailService projectDetailService;

    @Resource
    private SystemClient systemClient;

    @Resource
    private CustContractInfoService custContractInfoService;

    @Resource
    private CustService custService;

    @Resource
    private ConsService consService;

    @Resource
    private ConsContractInfoService consContractInfoService;

    @Resource
    private DrOrgGoalService orgGoalService;

    @Resource
    private EventService eventService;

    @Resource
    private CustContractInfoMapper custContractInfoMapper;
    @Resource
    private ConsContractInfoMapper consContractInfoMapper;

    @Resource
    private ConsSubsidyMapper consSubsidyMapper;

    @Resource
    private CustSubsidyMapper custSubsidyMapper;

    @Resource
    private EventMapper eventMapper;

    @Resource
    private ConsContractDetailService consContractDetailService;

    @Resource
    private ConsContractDetailMapper consContractDetailMapper;

    @Resource
    private SystemClientService client;

    @Resource
    private DictTypeService dictTypeService;

    @Resource
    private BaseLineDetailMapper baseLineDetailMapper;

    @Resource
    private ConsInvitationMapper consInvitationMapper;

    @Value("${executor.corePoolSize}")
    private int corePoolSize;

    @Value("${executor.maxPoolSize}")
    private int maxPoolSize;

    @Value("${executor.workQueue}")
    private int workQueue;

    @Resource
    private BaselineLibraryMapper baselineLibraryMapper;

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void add(ProjectParam projectParam) {
        if (ObjectUtil.isNotNull(projectParam.getProjectNo())) {
            LambdaQueryWrapper<Project> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Project::getProjectNo, projectParam.getProjectNo());
            List<Project> projects = this.list(queryWrapper);
            if (null != projects && projects.size() > 0) {
                throw new ServiceException(ProjectExceptionEnum.PROJECTNO_REPEAT);
            }
        }

        Project project = new Project();
        BeanUtil.copyProperties(projectParam, project);
        project.setState(ProjectStatusEnums.NEW.getCode());
        project.setCheckStatus(ProjectCheckStatusEnums.PASS_THE_AUDIT.getCode());
        this.save(project);
        // 获取项目明细
        List projectDetailArrayList = new ArrayList<ProjectDetail>();
        List<ProjectDetailParam> projectDetailList = projectParam.getProjectDetailList();
        for (ProjectDetailParam projectDetailParam : projectDetailList) {
            ProjectDetail projectDetail = new ProjectDetail();
            BeanUtil.copyProperties(projectDetailParam, projectDetail);
            projectDetail.setProjectId(project.getProjectId());
            // projectDetail.setResponseType(project.getProjectTarget());
            // 去掉开始时间 结束时间  替换为提前通知时间
            projectDetail.setAdvanceNoticeTime(projectDetailParam.getAdvanceNoticeTime());
            // projectDetail.setTimeType(projectParam.getTimeType());
            projectDetailArrayList.add(projectDetail);
        }
        projectDetailService.saveBatch(projectDetailArrayList);
    }

    @Override
    public void delete(ProjectParam projectParam) {
        Project project = this.getById(projectParam.getProjectId());
        // 只有项目的状态为“新建”、项目审核状态为“未提交”时可以删除
        if (ProjectStatusEnums.NEW.getCode().equals(project.getState())) {
            this.removeById(project.getProjectId());
            //  删除关联的项目详情
            LambdaQueryWrapper<ProjectDetail> detailQueryWrapper = new LambdaQueryWrapper<>();
            detailQueryWrapper.eq(ProjectDetail::getProjectId, projectParam.getProjectId());
            projectDetailService.remove(detailQueryWrapper);
            return;
        }
        throw new ServiceException(ProjectExceptionEnum.UNCORRECT_PROJECT_STATUS);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void edit(ProjectParam projectParam) {
        Project project = this.getById(projectParam.getProjectId());
        // 只有项目的状态为“新建”、项目审核状态为“未提交”时可以编辑
        if (ProjectStatusEnums.NEW.getCode().equals(project.getState())) {
            BeanUtil.copyProperties(projectParam, project);
            project.setState(ProjectStatusEnums.NEW.getCode());
            project.setCheckStatus(ProjectCheckStatusEnums.UNSUBMITTED.getCode());
            this.updateById(project);
            // 更新项目明细获取项目明细
            projectDetailService.deleteByProjectId(projectParam.getProjectId());
            List projectDetailArrayList = new ArrayList<ProjectDetail>();
            List<ProjectDetailParam> projectDetailList = projectParam.getProjectDetailList();
            for (ProjectDetailParam projectDetailParam : projectDetailList) {
                ProjectDetail projectDetail = new ProjectDetail();
                BeanUtil.copyProperties(projectDetailParam, projectDetail);
                projectDetail.setProjectId(project.getProjectId());
                // projectDetail.setResponseType(project.getProjectTarget());
                projectDetail.setAdvanceNoticeTime(projectDetailParam.getAdvanceNoticeTime());
                // projectDetail.setTimeType(projectParam.getTimeType());
                projectDetailArrayList.add(projectDetail);
            }
            projectDetailService.saveBatch(projectDetailArrayList);
            return;
        }
        throw new ServiceException(ProjectExceptionEnum.UNCORRECT_PROJECT_STATUS);
    }

    @Override
    @NeedSetValueField
    public Page<Project> page(ProjectParam projectParam) {
        LambdaQueryWrapper<Project> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(projectParam)) {
            // 根据编号模糊查询
            if (ObjectUtil.isNotEmpty(projectParam.getProjectNo())) {
                queryWrapper.like(Project::getProjectNo, projectParam.getProjectNo());
            }
            // 根据项目名称查询
            if (ObjectUtil.isNotEmpty(projectParam.getProjectName())) {
                queryWrapper.like(Project::getProjectName, projectParam.getProjectName());
            }
            // 根据项目状态查询
            if (ObjectUtil.isNotEmpty(projectParam.getState())) {
                queryWrapper.eq(Project::getState, projectParam.getState());
            }
            // 根据审批状态查询
            if (ObjectUtil.isNotEmpty(projectParam.getCheckStatus())) {
                queryWrapper.eq(Project::getCheckStatus, projectParam.getCheckStatus());
            }
        }
        // queryWrapper.orderByDesc(Project::getUpdateTime);
        queryWrapper.orderByDesc(Project::getCreateTime);
        return this.page(projectParam.getPage(), queryWrapper);
    }


    /**
     * 需求响应项目分页查询已经截止的项目
     *
     * @param projectParam
     * @return
     */
    @Override
    public Page<Project> pageByEndTime(ProjectParam projectParam) {
        LambdaQueryWrapper<Project> queryWrapper = new LambdaQueryWrapper<>();
        LocalDate localDate = LocalDate.now();
        queryWrapper.le(Project::getEndDate, localDate);
        // queryWrapper.orderByDesc(Project::getUpdateTime);
        queryWrapper.orderByDesc(Project::getCreateTime);
        return this.page(projectParam.getPage(), queryWrapper);
    }

    @Override
    public List<Project> list(ProjectParam projectParam) {
        LambdaQueryWrapper<Project> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(projectParam)) {
            // 根据编号模糊查询
            if (ObjectUtil.isNotEmpty(projectParam.getProjectNo())) {
                queryWrapper.like(Project::getProjectNo, projectParam.getProjectNo());
            }
            // 根据项目名称查询
            if (ObjectUtil.isNotEmpty(projectParam.getProjectName())) {
                queryWrapper.like(Project::getProjectName, projectParam.getProjectName());
            }
            // 根据项目状态查询
            if (ObjectUtil.isNotEmpty(projectParam.getState())) {
                queryWrapper.eq(Project::getState, projectParam.getState());
            }
            // 根据审批状态查询
            if (ObjectUtil.isNotEmpty(projectParam.getCheckStatus())) {
                queryWrapper.eq(Project::getCheckStatus, projectParam.getCheckStatus());
            }
            // 根据状态查询
            if (ObjectUtil.isNotEmpty(projectParam.getState())) {
                queryWrapper.eq(Project::getState, projectParam.getState());
            }
            queryWrapper.orderByDesc(Project::getCreateTime);
        }
        return this.list(queryWrapper);
    }

    @Override
    public ProjectInfo detail(ProjectParam projectParam) {
        Project project = this.getById(projectParam.getProjectId());
        List<ProjectDetail> projectDetails = projectDetailService.listByProjectId(projectParam.getProjectId());

        //根据项目详情去 各单位任务指标表 查询累计 任务指标值
        if (!CollectionUtils.isEmpty(projectDetails)) {
            List<Long> detailIds = projectDetails.stream().map(ProjectDetail::getDetailId).collect(Collectors.toList());

            LambdaQueryWrapper<DrOrgGoal> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(DrOrgGoal::getProjectDetailId, detailIds);
            List<DrOrgGoal> orgGoalList = orgGoalService.list(queryWrapper);

            if (!CollectionUtils.isEmpty(orgGoalList)) {
                //根据 ProjectDetailId分分组
                Map<Long, List<DrOrgGoal>> map = orgGoalList.stream()
                        .collect(Collectors.groupingBy(DrOrgGoal::getProjectDetailId));

                for (ProjectDetail detail : projectDetails) {

                    //获取此 id 对应的  任务指标 集合
                    List<DrOrgGoal> drOrgGoals = map.get(detail.getDetailId());

                    if (CollectionUtils.isEmpty(drOrgGoals)) {
                        //如果指标为分解
                        continue;
                    }
                    //总和
                    BigDecimal bigDecimal = new BigDecimal(0);
                    for (DrOrgGoal orgGoal : drOrgGoals) {
                        if (null != orgGoal && null != orgGoal.getGoal()) {
                            bigDecimal = bigDecimal.add(orgGoal.getGoal());
                        }
                    }

                    BigDecimal targetCup = detail.getTargetCup();
                    if (targetCup == null || targetCup.compareTo(BigDecimal.ZERO) == 0) {
                        detail.setCompletionRate("0");
                    } else {
                        detail.setCompletionRate(bigDecimal.divide(targetCup, 2).multiply(new BigDecimal(100)) + "%");
                    }
                }
            }
        }


        ProjectInfo projectResult = new ProjectInfo();
        BeanUtil.copyProperties(project, projectResult);
        projectResult.setProjectDetails(projectDetails);
        return projectResult;
    }

    @Override
    public ResponseData submit(ProjectParam projectParam) {
        BusConfigParam busConfigParam = new BusConfigParam();
        Project project = this.getById(projectParam.getProjectId());
        // 只有项目的状态为“新建”、项目审核状态为“未提交”时可以提交审核
        if (ProjectStatusEnums.NEW.getCode()
                .equals(project.getState()) && (ProjectCheckStatusEnums.UNSUBMITTED.getCode()
                .equals(project.getCheckStatus()) || ProjectCheckStatusEnums.AUDIT_FAILED.getCode()
                .equals(project.getCheckStatus()))) {
            // 暂时不行
            // CurrenUserInfo sysLoginUser = LoginContextHolder.me().getSysLoginUser();
            CurrenUserInfo currentUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
            if (ObjectUtil.isNull(currentUserInfo)) {
                throw new ServiceException(CurrenUserInfoExceptionEnum.CURRENT_USER_INFO_NOT_EXIST);
            }

            String infoId = currentUserInfo.getId();
            if (infoId != null) {
                Long custId = Long.valueOf(infoId);
                if (custId != null) {
                    Cust cust = custService.getById(custId);
                    if (cust != null) {
                        busConfigParam.setApplyManName(cust.getApplyName());
                    }
                }
            }
            busConfigParam.setBusId(String.valueOf(projectParam.getProjectId()));
            busConfigParam.setApplyOrgId(currentUserInfo.getOrgId()); // 申请人组织机构id
            busConfigParam.setBusType(BusTypeEnum.PROJECT_PROCESS.getCode()); // 业务类型
            busConfigParam.setApplyManId(Long.parseLong(currentUserInfo.getId())); // 申请人id
            busConfigParam.setLevel(1);
            busConfigParam.setOperaManId(currentUserInfo.getId());
            //System.out.println(busConfigParam);
            Result result = systemClient.selectInfo(busConfigParam);
            if (ObjectUtil.isNotNull(result) && result.getCode().equals("000000") && result.getData()
                    .getString("handleCode")
                    .equals("000")) {
                // 创建待办任务成功，否则为失败
                // 修改项目的状态为提交审核
                project.setCheckStatus(ProjectCheckStatusEnums.UNDER_REVIEW.getCode());
                this.updateById(project);
                return ResponseData.success();
            }

            return ResponseData.fail(result.getCode(), result.getMesg(), result.getData());
        } else {
            return ResponseData.fail(ProjectExceptionEnum.NOT_EXACTLY_STATUS);
        }

    }

    @Override
    public void examine(ExamineParam examineParam) {
        BusConfigParam busConfigParam = new BusConfigParam();
        BeanUtil.copyProperties(examineParam, busConfigParam);
        Project project = this.getById(examineParam.getBusId());
        // 只有项目的状态为“新建”、项目审核状态为“未提交”时可以审核
        if (ProjectStatusEnums.NEW.getCode()
                .equals(project.getState()) && (ProjectCheckStatusEnums.UNDER_REVIEW.getCode()
                .equals(project.getCheckStatus()))) {
            // CurrenUserInfo sysLoginUser = LoginContextHolder.me().getSysLoginUser();
            CurrenUserInfo currentUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
            if (ObjectUtil.isNull(currentUserInfo)) {
                throw new ServiceException(CurrenUserInfoExceptionEnum.CURRENT_USER_INFO_NOT_EXIST);
            }

            busConfigParam.setOperaManId(currentUserInfo.getId()); // 操作人id

            Result result = systemClient.selectInfo(busConfigParam);
            if (ObjectUtil.isNotNull(result) && result.getCode().equals("000000")) {
                if (result.getData().getString("handleCode").equals(ApprovalCodeEnum.PROCESS_FAIL.getCode())) {
                    // 审核流程失败
                    throw new ServiceException(ExamineExceptionEnum.APPROVAL_FAIL);
                }
                // 修改项目的状态
                // project.setProjectId(Long.parseLong(examineParam.getBusId()));

                // 审核结束
                if (result.getData().getString("handleCode").equals(ApprovalCodeEnum.PROCESS_SUCCESS.getCode())) {
                    project.setCheckStatus(ProjectCheckStatusEnums.PASS_THE_AUDIT.getCode());
                }
                // 申请被驳回
                if (result.getData().getString("handleCode").equals(ApprovalCodeEnum.APPROVAL_REJECT.getCode())) {
                    project.setCheckStatus(ProjectCheckStatusEnums.AUDIT_FAILED.getCode());
                }
                this.updateById(project);
                return;
            }
            // 接口状态码不为000000，抛出异常
            throw new ServiceException(ExamineExceptionEnum.CREATION_FAILED);
        } else {
            throw new ServiceException(ProjectExceptionEnum.NOT_EXACTLY_STATUS);
        }


    }

    @Override
    public void publicity(ProjectParam projectParam) {
        Project project = this.getById(projectParam.getProjectId());
        if (project.getCheckStatus().equals(ProjectCheckStatusEnums.PASS_THE_AUDIT.getCode())) {
            project.setCheckStatus(ProjectStatusEnums.PUBLICITY.getCode());
            this.updateById(project);
            return;
        }
        throw new ServiceException(ProjectExceptionEnum.PROCESS_NOT_FINISHED);
    }

    @Override
    public void editStatus(ProjectParam projectParam) {
        Project project = new Project();
        project.setProjectId(projectParam.getProjectId());
        if (ObjectUtil.isNotNull(projectParam.getState())) {
            project.setState(projectParam.getState());
        }
        if (ObjectUtil.isNotNull(projectParam.getCheckStatus())) {
            project.setCheckStatus(projectParam.getCheckStatus());
        }
        if (ObjectUtil.isNotNull(projectParam.getPubTime())) {
            project.setPubTime(projectParam.getPubTime());
        }
        this.updateById(project);
    }

    @Override
    public List<Project> selectProject(ProjectParam projectParam) {
        String custId = Objects.requireNonNull(SecurityUtils.getCurrentUserInfoUTF8()).getId();
        Cust aggregator = custService.selectAggregator();
        List<Long> projectIds;
        // 查询当前用户已经签约的项目
        if (ObjectUtil.isNotEmpty(projectParam.getConsId())) {
            List<ConsContractInfo> consContractInfos = consContractInfoService.list(Wrappers.<ConsContractInfo>lambdaQuery()
                    .eq(ConsContractInfo::getConsId, projectParam.getConsId())
                    .select(ConsContractInfo::getProjectId)
                    .groupBy(ConsContractInfo::getProjectId));
            projectIds = consContractInfos.stream().map(ConsContractInfo::getProjectId).collect(Collectors.toList());
        } else if (ObjectUtil.isNotEmpty(aggregator)) {
            List<CustContractInfo> custContractInfos = custContractInfoService.list(Wrappers.<CustContractInfo>lambdaQuery()
                    .eq(CustContractInfo::getCustId, aggregator.getId()));
            List<Long> aggProjectIds = custContractInfos.stream()
                    .map(CustContractInfo::getProjectId)
                    .collect(Collectors.toList());

            List<Cons> consList = consService.list(Wrappers.<Cons>lambdaQuery().eq(Cons::getCustId, custId));
            List<String> consIds = consList.stream().map(Cons::getId).collect(Collectors.toList());
            List<ConsContractInfo> consContractInfos = consContractInfoService.list(Wrappers.<ConsContractInfo>lambdaQuery()
                    .in(ConsContractInfo::getConsId, consIds)
                    .groupBy(ConsContractInfo::getProjectId));
            projectIds = consContractInfos.stream().map(ConsContractInfo::getProjectId).collect(Collectors.toList());
            projectIds.retainAll(aggProjectIds);
        } else {
            List<CustContractInfo> custContractInfos = custContractInfoService.list(Wrappers.<CustContractInfo>lambdaQuery()
                    .eq(CustContractInfo::getCustId, custId));
            projectIds = custContractInfos.stream().map(CustContractInfo::getProjectId).collect(Collectors.toList());
        }
        LambdaQueryWrapper<Project> lambdaQueryWrapper = Wrappers.lambdaQuery();
        // 项目筛选条件：审核通过，状态是公示中，且截止时间在当前时间之后，根据开始时间排序
        lambdaQueryWrapper.eq(Project::getCheckStatus, ProjectCheckStatusEnums.PASS_THE_AUDIT.getCode())
                .eq(Project::getState, ProjectStatusEnums.PUBLICITY.getCode())
                .ge(Project::getEndDate, new Date())
                .orderByDesc(Project::getBeginDate);
        if (!CollectionUtils.isEmpty(projectIds)) {
            lambdaQueryWrapper.notIn(Project::getProjectId, projectIds);
        }
        return list(lambdaQueryWrapper);
    }


    /**
     * @description: 指标分解（保存）
     * @param:
     * @return:
     * @author: lqr
     * @date: 2022.01.11 10:03
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public ResponseData addTarget(DrOrgGoalParam orgGoalParam) {
        // 校验参数
        if (ObjectUtil.isNotEmpty(orgGoalParam)) {

            Long projectDetailId = orgGoalParam.getProjectDetailId();

            // 修改项目明细表的 目标负荷字段
            ProjectDetail projectDetail = projectDetailService.getById(projectDetailId);
            if (ObjectUtil.isNotEmpty(projectDetail)) {
                //项目详情表的 目标负荷字段 新增和修改 都是重新赋值
                projectDetail.setTargetCup(orgGoalParam.getTargerCup());
                projectDetailService.updateById(projectDetail);

                //查询任务指标表是否已有对应数据
                LambdaQueryWrapper<DrOrgGoal> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(DrOrgGoal::getProjectDetailId, projectDetailId);
                List<DrOrgGoal> orgGoals = orgGoalService.list(queryWrapper);

                List<DrOrgGoal> orgGoalList = orgGoalParam.getOrgGoalList();
                List<DrOrgGoal> list = new ArrayList<>();
                if (CollectionUtils.isEmpty(orgGoals)) {
                    //批量入库 任务指标表
                    if (!CollectionUtils.isEmpty(orgGoalList)) {
                        for (DrOrgGoal drOrgGoal : orgGoalList) {
                            drOrgGoal.setProjectDetailId(projectDetailId);
                            drOrgGoal.setProjectId(orgGoalParam.getProjectId());

                            list.add(drOrgGoal);
                        }

                        orgGoalService.saveBatch(list);
                    }
                } else {
                    //先清空源数据，然后重新新增
                    orgGoalService.remove(queryWrapper);
                    //批量入库 任务指标表
                    if (!CollectionUtils.isEmpty(orgGoalList)) {
                        for (DrOrgGoal drOrgGoal : orgGoalList) {
                            drOrgGoal.setProjectDetailId(projectDetailId);
                            drOrgGoal.setProjectId(orgGoalParam.getProjectId());

                            list.add(drOrgGoal);
                        }

                        orgGoalService.saveBatch(list);
                    }
                }
                return ResponseData.success();
            }

            return ResponseData.fail("没有对应项目明细");
        }
        return ResponseData.fail(SystemErrorType.PARAMETER_NULL);
    }

    @Override
    public DrOrgGoalVO detailTarget(Long projectDetailId) {
        if (projectDetailId != null) {
            ProjectDetail projectDetail = projectDetailService.getById(projectDetailId);
            if (ObjectUtil.isNotEmpty(projectDetail)) {
                //查询对应项目明细 的 用户签约明细
                String checkStatus = CheckStatusEnum.PASS_THE_AUDIT.getCode();
                List<DrConsContractDetailsVO> consContractDetails = consContractDetailMapper.queryConsContractDetailGroupOrgNo(projectDetailId, checkStatus);

                BigDecimal provinceSum = BigDecimal.ZERO;
                DrOrgGoalVO drOrgGoalVO = new DrOrgGoalVO();

                LambdaQueryWrapper<DrOrgGoal> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(DrOrgGoal::getProjectDetailId, projectDetailId);
                List<DrOrgGoal> orgGoalList = orgGoalService.list(queryWrapper);

                if (null != orgGoalList && orgGoalList.size() > 0) {
                    for (DrOrgGoal drOrgGoal : orgGoalList) {
                        // 获取下一级和本级组织
                        List<String> orgList = systemClient.getAllNextOrgId(drOrgGoal.getOrgId()).getData();

                        // 下级和下级所有用户签约的容量数
                        BigDecimal contractCap = consContractDetails.stream()
                                .filter(n -> orgList.contains(n.getOrgNo()))
                                .map(cons -> {
                                    BigDecimal cap = cons.getContractCap();
                                    return cap == null ? BigDecimal.ZERO : cap;
                                })
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        contractCap = contractCap.divide(new BigDecimal(10000));
                        drOrgGoal.setContractCap(contractCap);
                        if(drOrgGoal.getGoal() != null){
                            if (BigDecimal.ZERO.compareTo(drOrgGoal.getGoal()) < 0) {
                                drOrgGoal.setContractRatio(contractCap.divide(drOrgGoal.getGoal(), 4, BigDecimal.ROUND_HALF_UP));
                            }
                        }

                        // 所有签约容量累加
                        provinceSum = provinceSum.add(contractCap);
                    }
                }
                drOrgGoalVO.setOrgGoalList(orgGoalList);
                drOrgGoalVO.setTargerCup(projectDetail.getTargetCup());
                drOrgGoalVO.setProvinceContractSum(provinceSum);
                return drOrgGoalVO;
            }
        }

        return null;
    }


    /**
     * 根据事件返回项目的项目类型
     *
     * @param eventId
     * @return
     */
    @Override
    public ResponseData getprojectTypeByEventId(Long eventId) {
        if (eventId == null) {
            return ResponseData.fail(SystemErrorType.PARAMETER_NULL);
        }

        Event event = eventService.getById(eventId);
        if (event == null) {
            return ResponseData.fail(SystemErrorType.EVENT_NULL);
        }
        LambdaQueryWrapper<Project> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Project::getProjectId, event.getProjectId());
        Project project = this.getOne(queryWrapper);
        return ResponseData.success(project);
    }

    /**
     * 基线申诉
     * @param eventParam
     * @return
     */
    @Override
    public ResponseData getBaselineGrievance(EventParam eventParam) {
        if (eventParam == null) {
            return ResponseData.fail(SystemErrorType.PARAMETER_NULL);
        }

        BaseLineDetail baseLineDetail = new BaseLineDetail();

        //查询事件表
        Event event = eventService.getById(eventParam.getEventId());
        if (event == null) {
            return ResponseData.fail(SystemErrorType.EVENT_NULL);
        }
        baseLineDetail.setBaselineLibId(event.getBaselinId());

        //查询基线库
        BaselineLibrary byBaselinId = baselineLibraryMapper.getByBaselinId(String.valueOf(event.getBaselinId()));
        if (byBaselinId == null) {
            return ResponseData.fail("500","未找到对应基线库对象","未找到对应基线库对象");
        }
        baseLineDetail.setDescr(byBaselinId.getDescr());
        baseLineDetail.setTimeQuantum(byBaselinId.getTimeQuantum());

        //查询用户时间基线表
        List<BaseLineDetail> drConsBaselineOne = baseLineDetailMapper.getDrConsBaselineOne(String.valueOf(event.getBaselinId()),eventParam.getConsId());
        if (drConsBaselineOne == null) {
            return ResponseData.fail("500","未找到对应用户时间基线对象","未找到对应用户时间基线对象");
        }
        baseLineDetail.setMaxLoadBaseline(drConsBaselineOne.get(0).getMaxLoadBaseline());
        baseLineDetail.setMinLoadBaseline(drConsBaselineOne.get(0).getMinLoadBaseline());
        baseLineDetail.setAvgLoadBaseline(drConsBaselineOne.get(0).getAvgLoadBaseline());
        baseLineDetail.setConsId(drConsBaselineOne.get(0).getConsId());
        baseLineDetail.setConsName(drConsBaselineOne.get(0).getConsName());
        baseLineDetail.setSimplesDate(drConsBaselineOne.get(0).getSimplesDate());
        baseLineDetail.setContractCap(drConsBaselineOne.get(0).getContractCap());

        //查询用户邀约
        List<ConsInvitation> consInvitationOne = consInvitationMapper.getConsInvitationOne(String.valueOf(eventParam.getEventId()),eventParam.getConsId());
        if (consInvitationOne == null) {
            return ResponseData.fail("500","未找到对应用户邀约对象","未找到对应用户邀约对象");
        }
        baseLineDetail.setDeadlineTime(consInvitationOne.get(0).getDeadlineTime());


        return ResponseData.success(baseLineDetail);
    }



    public Page<BaseLineDetail> getBaselineGrievanceData(EventParam eventParam)throws ServiceException{
        Page<BaseLineDetail> baseLineDetailPage = new Page<BaseLineDetail>();

        //查询用户时间基线表
        List<BaseLineDetail> drConsBaselineOne = baseLineDetailMapper.getDrConsBaselineOne(eventParam.getBaselineLibId(),eventParam.getConsId());
        if (drConsBaselineOne == null) {
            throw new ServiceException(99999, "未找到对应用户时间基线对象");
        }

        //查询基线库
        BaselineLibrary byBaselinId = baselineLibraryMapper.getByBaselinId(eventParam.getBaselineLibId());
        if (byBaselinId == null) {
            throw new ServiceException(99999, "未找到对应基线库对象");
        }

        drConsBaselineOne.get(0).setTimeQuantum(byBaselinId.getTimeQuantum());
        baseLineDetailPage.setRecords(drConsBaselineOne);
        return baseLineDetailPage;
    }
    /**
     * 时间段基线负荷估算
     * @param consBaselineAllParam
     * @return
     */
    @Override
    public ResponseData getBaselineGrievanceCalculate(ConsBaselineAllParam consBaselineAllParam)throws ServiceException{
        log.info("时间段基线负荷估算");
        this.createBaseline(consBaselineAllParam);
        return ResponseData.success(consBaselineAllParam);
    }

    public void createBaseline(ConsBaselineAllParam consBaselineAllParam) {
        //一次只查询一个基线库任务
        //查询用户时间基线表
        //查询基线库
        BaselineLibrary byBaselinId = baselineLibraryMapper.getByBaselinId(String.valueOf(consBaselineAllParam.getBaselineLibId()));
        if (byBaselinId == null) {
            throw new ServiceException(99999, "未找到对应基线库对象");
        }

        //查询用户时间基线表
        List<BaseLineDetail> drConsBaselineOne = baseLineDetailMapper.getDrConsBaselineOne(String.valueOf(consBaselineAllParam.getBaselineLibId()),consBaselineAllParam.getConsId());
        if (drConsBaselineOne == null) {
            throw new ServiceException(99999, "未找到对应用户时间基线对象");
        }

        consBaselineAllParam.setStartPeriod(byBaselinId.getStartPeriod());
        consBaselineAllParam.setEndPeriod(byBaselinId.getEndPeriod());

        this.createBaselineTwo(consBaselineAllParam,drConsBaselineOne);
        /*ExecutorService executor = ExecutorBuilder.create()
                .setCorePoolSize(corePoolSize)
                .setMaxPoolSize(maxPoolSize)
                .setWorkQueue(new LinkedBlockingQueue<>(workQueue))
                .build();
        executor.execute(this.createBaselineTwo(consBaselineAllParam,drConsBaselineOne));
        executor.shutdown();*/
    }

    public void createBaselineTwo(ConsBaselineAllParam consBaselineAllParam,List<BaseLineDetail> drConsBaselineOne) {
        log.info(">>> "+"时间段基线负荷估算");
        Map<Integer, Method> consMethodMap2 = new HashMap<>();
        try{
            for (int j=1; j<=96; j++){
                consMethodMap2.put(j, ConsBaselineAllParam.class.getMethod("getP"+j));
            }
        } catch (Exception e) {
            e.getMessage();
        }
        //获取用户列表
        //Runnable runnable = new Runnable() {
            //@Override
            //public void run() {
                //开始下标
                int startIndex = CurveUtil.covDateTimeToPoint(consBaselineAllParam.getStartPeriod());
                int endIndex = CurveUtil.covDateTimeToPoint(consBaselineAllParam.getEndPeriod());
                try {
                List<BigDecimal> samplePowerList = new ArrayList<>();
                for (int i = startIndex; i <= endIndex; i++) {
                    //去除0值和null值数据
                    BigDecimal consCurveHis = null;
                    consCurveHis = (BigDecimal) consMethodMap2.get(i).invoke(consBaselineAllParam);
                    if (null != consCurveHis) {
                        samplePowerList.add(consCurveHis);
                    }
                }
                if (samplePowerList.size() > 0) {
                    BigDecimal maxSamplePower = CollectionUtil.max(samplePowerList);
                    BigDecimal minSamplePower = CollectionUtil.min(samplePowerList);
                    BigDecimal sumPower = samplePowerList.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal avgPower = NumberUtil.div(sumPower, samplePowerList.size(), 2);

                    consBaselineAllParam.setMaxLoadBaseline(maxSamplePower);
                    consBaselineAllParam.setMinLoadBaseline(minSamplePower);
                    consBaselineAllParam.setAvgLoadBaseline(avgPower);

                    //计算申诉提升负荷和申诉提升比率
                    BigDecimal differ=avgPower.subtract(drConsBaselineOne.get(0).getAvgLoadBaseline());
                    BigDecimal num=differ.divide(drConsBaselineOne.get(0).getAvgLoadBaseline(), 4, BigDecimal.ROUND_DOWN);
                    String rate=(num.multiply(new BigDecimal(100)))+"%";
                    consBaselineAllParam.setDiffer(differ);
                    consBaselineAllParam.setRate(rate);
                }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            //}
        //};
        //return runnable;

    }

    @Override
    public List<Project> getProjectByYear(Integer year) {
        LocalDate nowYear = LocalDate.of(year, 1, 1);
        LocalDate lastYear = LocalDate.of(year - 1, 1, 1);
        LocalDate afterYear = LocalDate.of(year + 1, 1, 1);
        LambdaQueryWrapper<Project> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.nested(lam2 -> {
            lam2.ge(Project::getBeginDate, nowYear).lt(Project::getBeginDate, afterYear);
        }).or(lam3 -> {
            lam3.ge(Project::getEndDate, nowYear).lt(Project::getEndDate, afterYear);
        }).orderByDesc(Project::getCreateTime);
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }

    @Override
    public WorkProjectInfoResult getWorkProjectDetail(WorkProjectParam workProjectParam) {
        CurrenUserInfo currenUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        WorkProjectInfoResult workProjectInfoResult = new WorkProjectInfoResult();
        String projectId = workProjectParam.getProjectId();
        String orgNo = workProjectParam.getOrgNo();
        if (StringUtils.isEmpty(orgNo)) {
            orgNo = currenUserInfo.getOrgId();
        }
        List<String> allOrgByOrgNoPamarm = OrganizationUtil.getAllOrgByOrgNoPamarm(orgNo);

        StringBuilder sb = new StringBuilder("(");
        sb.append(allOrgByOrgNoPamarm.stream().collect(Collectors.joining(",")));
        sb.append(")");

        Integer integratorContractSize = custContractInfoMapper.getIntegratorContractSize(projectId);
        LambdaQueryWrapper<ConsContractInfo> consContractInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        consContractInfoLambdaQueryWrapper.eq(ConsContractInfo::getProjectId, projectId);
        consContractInfoLambdaQueryWrapper.eq(ConsContractInfo::getStatus, 2);
        consContractInfoLambdaQueryWrapper.eq(ConsContractInfo::getCheckStatus, 3);
        consContractInfoLambdaQueryWrapper.eq(ConsContractInfo::getParticType, 1);
        consContractInfoLambdaQueryWrapper.inSql(ConsContractInfo::getConsId, "select id from dr_cons where " + "ORG_NO in ( " + allOrgByOrgNoPamarm.stream()
                .map(item -> "'" + item + "'")
                .collect(Collectors.joining(",")) + " )");
        Integer projectConsSize = consContractInfoMapper.selectCount(consContractInfoLambdaQueryWrapper);
        consContractInfoLambdaQueryWrapper.clear();
        consContractInfoLambdaQueryWrapper.eq(ConsContractInfo::getProjectId, projectId);
        consContractInfoLambdaQueryWrapper.eq(ConsContractInfo::getStatus, 2);
        consContractInfoLambdaQueryWrapper.eq(ConsContractInfo::getCheckStatus, 3);
        consContractInfoLambdaQueryWrapper.eq(ConsContractInfo::getParticType, 2);
        consContractInfoLambdaQueryWrapper.inSql(ConsContractInfo::getConsId, "select id from dr_cons where " + "ORG_NO in ( " + allOrgByOrgNoPamarm.stream()
                .map(item -> "'" + item + "'")
                .collect(Collectors.joining(",")) + " )");
        Integer integerProjectConsSize = consContractInfoMapper.selectCount(consContractInfoLambdaQueryWrapper);

        List<WorkProjectInfoDTO> workPageCapDetail = consContractInfoMapper.getWorkPageCapDetail(projectId, allOrgByOrgNoPamarm);

        LambdaQueryWrapper<Event> eventLambdaQueryWrapper = new LambdaQueryWrapper<>();
        eventLambdaQueryWrapper.eq(Event::getProjectId, projectId);
        Integer eventInitSize = eventMapper.selectCount(eventLambdaQueryWrapper);

        QueryWrapper<Event> queryWrapper = new QueryWrapper<>();
        eventLambdaQueryWrapper = queryWrapper.lambda();
        eventLambdaQueryWrapper.eq(Event::getProjectId, projectId);
        eventLambdaQueryWrapper.groupBy(Event::getEventStatus);
        queryWrapper.select("event_status as eventStatus", "count(0) as size");
        List<Map<String, Object>> eventMapLists = eventMapper.selectMaps(queryWrapper);
        Map<Integer, Integer> eventStatsMap = eventMapLists.stream()
                .map(item -> new AbstractMap.SimpleEntry<>(Integer.valueOf(Objects.toString(item.get("eventStatus"), "0"))
                        , Integer.valueOf(Objects.toString(item.get("size"), "0"))))
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));

        WorkProjectInfoResult.ContractCap contractCap = new WorkProjectInfoResult.ContractCap();
        contractCap.setDesMaxPower(getContractCap(workPageCapDetail, 1, 1, 1));
        contractCap.setDesHourMaxPower(getContractCap(workPageCapDetail, 1, 1, 2));
        contractCap.setDesMinusMaxPower(getContractCap(workPageCapDetail, 1, 2, 3));
        contractCap.setDesSecondMaxPower(getContractCap(workPageCapDetail, 1, 2, 4));
        contractCap.setRisReserveMaxPower(getContractCap(workPageCapDetail, 2, 1, null));

        WorkProjectInfoResult.ContractBackupCap contractBackupCap = new WorkProjectInfoResult.ContractBackupCap();
        contractBackupCap.setReserveDesMaxPower(getBackupCap(workPageCapDetail, 1, 1));
        contractBackupCap.setRealTimeDesMaxPower(getBackupCap(workPageCapDetail, 1, 2));
        contractBackupCap.setReserveRisPower(getBackupCap(workPageCapDetail, 2, 1));

        WorkProjectInfoResult.EventStats eventStats = new WorkProjectInfoResult.EventStats();
        eventStats.setInitSize(eventInitSize);
        eventStats.setPreRunSize(eventStatsMap.getOrDefault(2, 0));
        eventStats.setRunningSize(eventStatsMap.getOrDefault(3, 0));
        eventStats.setFinishedSize(eventStatsMap.getOrDefault(4, 0));
        eventStats.setTerminationSize(eventStatsMap.getOrDefault(7, 0));

        WorkProjectInfoResult.SubsidyInfo subsidyInfo = new WorkProjectInfoResult.SubsidyInfo();
        // 根据 传参orgNo 获取下级
        SysOrgs sysOrgs = new SysOrgs();
        JSONObject jsonObject = systemClient.getOrgName(workProjectParam.getOrgNo());
        if (null == jsonObject) {
            throw new ServiceException(ProjectExceptionEnum.NOT_RESULT);
        }
        if ("000000".equals(jsonObject.get("code"))) {
            JSONObject data = jsonObject.getJSONObject("data");
            sysOrgs = JSONObjectToEntityUtils.JSONObjectToSysOrg(data);

        }
        // 不是 省级 就获取下级
        List<String> orgList = null;
        if (!OrgTitleEnum.PROVINCE.getCode().equals(sysOrgs.getOrgTitle())) {
            orgList = systemClient.getAllNextOrgId(workProjectParam.getOrgNo()).getData();
        }

        // 计算 响应补偿
        subsidyInfo.setIntegratorSubsidyAmount(custSubsidyMapper.getProjectCustSubsidy(projectId, 1));
        subsidyInfo.setNotIntegratorSubsidyAmount(consSubsidyMapper.getProjectSubsidyInfo(projectId, orgList, 0));
        subsidyInfo.setConsSubsidyAmount(consSubsidyMapper.getProjectConsSubsidy(projectId, orgList));

        subsidyInfo.setTotalSubsidyAmount(subsidyInfo.getIntegratorSubsidyAmount()
                .add(subsidyInfo.getConsSubsidyAmount())
                .add(subsidyInfo.getNotIntegratorSubsidyAmount()));
        if (OrgTitleEnum.PROVINCE.getCode().equals(currenUserInfo.getOrgTitle())) {
            // 省 全加
            subsidyInfo.setTotalSubsidyAmount(subsidyInfo.getIntegratorSubsidyAmount()
                    .add(subsidyInfo.getNotIntegratorSubsidyAmount())
                    .add(subsidyInfo.getConsSubsidyAmount()));
        } else {
            // 市 少加一个
            subsidyInfo.setTotalSubsidyAmount(subsidyInfo.getNotIntegratorSubsidyAmount()
                    .add(subsidyInfo.getConsSubsidyAmount()));
        }

        workProjectInfoResult.setCustIntegratorSize(integratorContractSize);
        workProjectInfoResult.setConsSize(projectConsSize);
        workProjectInfoResult.setIntegerConsSize(integerProjectConsSize);
        workProjectInfoResult.setContractCap(contractCap);
        workProjectInfoResult.setEventStats(eventStats);
        workProjectInfoResult.setContractBackupCap(contractBackupCap);
        workProjectInfoResult.setSubsidyInfo(subsidyInfo);

        return workProjectInfoResult;
    }

    private BigDecimal getContractCap(List<WorkProjectInfoDTO> workPageCapDetail, Integer responseType, Integer timeType, Integer advanceNoticeTime) {
        List<WorkProjectInfoDTO> workProjectInfoDTOS = workPageCapDetail.stream()
                .filter(item -> Objects.equals(item.getResponseType(), responseType) && Objects.equals(item.getTimeType(), timeType) && (advanceNoticeTime == null || Objects.equals(item.getAdvanceNoticeTime(), advanceNoticeTime)))
                .collect(Collectors.toList());
        if (workProjectInfoDTOS == null || workProjectInfoDTOS.size() == 0) {
            return BigDecimal.valueOf(0);
        } else {
            BigDecimal contractCap = workProjectInfoDTOS.get(0).getContractCap();
            return contractCap == null ? BigDecimal.ZERO : contractCap;
        }
    }

    private BigDecimal getBackupCap(List<WorkProjectInfoDTO> workPageCapDetail, Integer responseType, Integer timeType) {
        List<WorkProjectInfoDTO> workProjectInfoDTOS = workPageCapDetail.stream()
                .filter(item -> Objects.equals(item.getResponseType(), responseType) && Objects.equals(item.getTimeType(), timeType))
                .collect(Collectors.toList());
        if (workProjectInfoDTOS == null || workProjectInfoDTOS.size() == 0) {
            return BigDecimal.valueOf(0);
        } else {
            BigDecimal spareCap = workProjectInfoDTOS.get(0).getSpareCap();
            return spareCap == null ? BigDecimal.ZERO : spareCap;
        }
    }


    /**
     * 提供给第三方 全省的审核通过的签约用户数和每个项目明细的签约负荷
     */
    @Override
    public WorkProjectInfoResult groupConsContractCap(@RequestBody WorkProjectParam workProjectParam) {
        if(workProjectParam == null){
            throw new ServiceException(500,"未传递参数，请核查");
        }

        WorkProjectInfoResult workProjectInfoResult = new WorkProjectInfoResult();
        if(workProjectParam.getYear() != null){

            //当前省orgNo
            String orgNo = workProjectParam.getOrgId();
            //根据年份查询 项目集合
            List<Project> projectList = this.getProjectByYear(workProjectParam.getYear());
            if(!CollectionUtils.isEmpty(projectList)){
                Project project = projectList.get(0);
                if(project != null){
                    Long id = project.getProjectId();
                    if(id != null){
                        String projectId = id.toString();

                        List<String> allOrgByOrgNoPamarm = OrganizationUtil.getAllOrgByOrgNoPamarm(orgNo);

                        StringBuilder sb = new StringBuilder("(");
                        sb.append(allOrgByOrgNoPamarm.stream().collect(Collectors.joining(",")));
                        sb.append(")");

                        Integer integratorContractSize = custContractInfoMapper.getIntegratorContractSize(projectId);
                        LambdaQueryWrapper<ConsContractInfo> consContractInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
                        consContractInfoLambdaQueryWrapper.eq(ConsContractInfo::getProjectId, projectId);
                        consContractInfoLambdaQueryWrapper.eq(ConsContractInfo::getStatus, 2);
                        consContractInfoLambdaQueryWrapper.eq(ConsContractInfo::getCheckStatus, 3);
                        consContractInfoLambdaQueryWrapper.eq(ConsContractInfo::getParticType, 1);
                        consContractInfoLambdaQueryWrapper.inSql(ConsContractInfo::getConsId, "select id from dr_cons where " + "ORG_NO in ( " + allOrgByOrgNoPamarm.stream()
                                .map(item -> "'" + item + "'")
                                .collect(Collectors.joining(",")) + " )");
                        Integer projectConsSize = consContractInfoMapper.selectCount(consContractInfoLambdaQueryWrapper);
                        consContractInfoLambdaQueryWrapper.clear();
                        consContractInfoLambdaQueryWrapper.eq(ConsContractInfo::getProjectId, projectId);
                        consContractInfoLambdaQueryWrapper.eq(ConsContractInfo::getStatus, 2);
                        consContractInfoLambdaQueryWrapper.eq(ConsContractInfo::getCheckStatus, 3);
                        consContractInfoLambdaQueryWrapper.eq(ConsContractInfo::getParticType, 2);
                        consContractInfoLambdaQueryWrapper.inSql(ConsContractInfo::getConsId, "select id from dr_cons where " + "ORG_NO in ( " + allOrgByOrgNoPamarm.stream()
                                .map(item -> "'" + item + "'")
                                .collect(Collectors.joining(",")) + " )");
                        Integer integerProjectConsSize = consContractInfoMapper.selectCount(consContractInfoLambdaQueryWrapper);

                        List<WorkProjectInfoDTO> workPageCapDetail = consContractInfoMapper.getWorkPageCapDetail(projectId, allOrgByOrgNoPamarm);

                        LambdaQueryWrapper<Event> eventLambdaQueryWrapper = new LambdaQueryWrapper<>();
                        eventLambdaQueryWrapper.eq(Event::getProjectId, projectId);
                        Integer eventInitSize = eventMapper.selectCount(eventLambdaQueryWrapper);

                        QueryWrapper<Event> queryWrapper = new QueryWrapper<>();
                        eventLambdaQueryWrapper = queryWrapper.lambda();
                        eventLambdaQueryWrapper.eq(Event::getProjectId, projectId);
                        eventLambdaQueryWrapper.groupBy(Event::getEventStatus);
                        queryWrapper.select("event_status as eventStatus", "count(0) as size");
                        List<Map<String, Object>> eventMapLists = eventMapper.selectMaps(queryWrapper);
                        Map<Integer, Integer> eventStatsMap = eventMapLists.stream()
                                .map(item -> new AbstractMap.SimpleEntry<>(Integer.valueOf(Objects.toString(item.get("eventStatus"), "0"))
                                        , Integer.valueOf(Objects.toString(item.get("size"), "0"))))
                                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));

                        WorkProjectInfoResult.ContractCap contractCap = new WorkProjectInfoResult.ContractCap();
                        contractCap.setDesMaxPower(getContractCap(workPageCapDetail, 1, 1, 1));
                        contractCap.setDesHourMaxPower(getContractCap(workPageCapDetail, 1, 1, 2));
                        contractCap.setDesMinusMaxPower(getContractCap(workPageCapDetail, 1, 2, 3));
                        contractCap.setDesSecondMaxPower(getContractCap(workPageCapDetail, 1, 2, 4));
                        contractCap.setRisReserveMaxPower(getContractCap(workPageCapDetail, 2, 1, null));

                        WorkProjectInfoResult.ContractBackupCap contractBackupCap = new WorkProjectInfoResult.ContractBackupCap();
                        contractBackupCap.setReserveDesMaxPower(getBackupCap(workPageCapDetail, 1, 1));
                        contractBackupCap.setRealTimeDesMaxPower(getBackupCap(workPageCapDetail, 1, 2));
                        contractBackupCap.setReserveRisPower(getBackupCap(workPageCapDetail, 2, 1));

                        WorkProjectInfoResult.EventStats eventStats = new WorkProjectInfoResult.EventStats();
                        eventStats.setInitSize(eventInitSize);
                        eventStats.setPreRunSize(eventStatsMap.getOrDefault(2, 0));
                        eventStats.setRunningSize(eventStatsMap.getOrDefault(3, 0));
                        eventStats.setFinishedSize(eventStatsMap.getOrDefault(4, 0));
                        eventStats.setTerminationSize(eventStatsMap.getOrDefault(7, 0));

                        WorkProjectInfoResult.SubsidyInfo subsidyInfo = new WorkProjectInfoResult.SubsidyInfo();
                        // 根据 传参orgNo 获取下级
                        SysOrgs sysOrgs = new SysOrgs();
                        JSONObject jsonObject = systemClient.getOrgName(orgNo);
                        if (null == jsonObject) {
                            throw new ServiceException(ProjectExceptionEnum.NOT_RESULT);
                        }
                        if ("000000".equals(jsonObject.get("code"))) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            sysOrgs = JSONObjectToEntityUtils.JSONObjectToSysOrg(data);

                        }
                        // 不是 省级 就获取下级
                        List<String> orgList = null;
                        if (!OrgTitleEnum.PROVINCE.getCode().equals(sysOrgs.getOrgTitle())) {
                            orgList = systemClient.getAllNextOrgId(workProjectParam.getOrgNo()).getData();
                        }

                        // 计算 响应补偿
                        subsidyInfo.setIntegratorSubsidyAmount(custSubsidyMapper.getProjectCustSubsidy(projectId, 1));
                        subsidyInfo.setNotIntegratorSubsidyAmount(consSubsidyMapper.getProjectSubsidyInfo(projectId, orgList, 0));
                        subsidyInfo.setConsSubsidyAmount(consSubsidyMapper.getProjectConsSubsidy(projectId, orgList));

                        subsidyInfo.setTotalSubsidyAmount(subsidyInfo.getIntegratorSubsidyAmount()
                                .add(subsidyInfo.getConsSubsidyAmount())
                                .add(subsidyInfo.getNotIntegratorSubsidyAmount()));
                        if (OrgTitleEnum.PROVINCE.getCode().equals(OrgTitleEnum.PROVINCE.getCode())) {
                            // 省 全加
                            subsidyInfo.setTotalSubsidyAmount(subsidyInfo.getIntegratorSubsidyAmount()
                                    .add(subsidyInfo.getNotIntegratorSubsidyAmount())
                                    .add(subsidyInfo.getConsSubsidyAmount()));
                        } else {
                            // 市 少加一个
                            subsidyInfo.setTotalSubsidyAmount(subsidyInfo.getNotIntegratorSubsidyAmount()
                                    .add(subsidyInfo.getConsSubsidyAmount()));
                        }
                        workProjectInfoResult.setCustIntegratorSize(integratorContractSize);
                        workProjectInfoResult.setConsSize(projectConsSize);
                        workProjectInfoResult.setIntegerConsSize(integerProjectConsSize);
                        workProjectInfoResult.setContractCap(contractCap);
                        workProjectInfoResult.setEventStats(eventStats);
                        workProjectInfoResult.setContractBackupCap(contractBackupCap);
                        workProjectInfoResult.setSubsidyInfo(subsidyInfo);
                    }
                }
            }
        }
        return workProjectInfoResult;
    }

    /**
     * 提供给 示范工程 根据项目id 和 orgId 查询签约人数 签约负荷总量
     * @param workProjectParam
     * @return
     */
    @Override
    public List<GroupContractByTimeTypeResult> groupContractByTimeType(WorkProjectParam workProjectParam) {
        if(workProjectParam != null){
            String orgId = workProjectParam.getOrgId();
            if(StringUtils.isNotBlank(orgId)){
                ResponseData<List<String>> allNextOrgId = systemClient.getAllNextOrgId(orgId);
                if(allNextOrgId != null){
                    String code = allNextOrgId.getCode().toString();
                    if("000000".equals(code)){
                        String data = allNextOrgId.getData().toString();
                        // 拿到本次循环 org 的所有下级org
                        List<String> orgNos = Arrays.asList(data.replace("[","").replace("]","").split(","));
                        workProjectParam.setOrgNos(orgNos);
                    }
                }
            }
        }
        List<GroupContractByTimeTypeResult> list = consContractInfoMapper.groupContractByTimeType(workProjectParam);
        return list;
    }
}
